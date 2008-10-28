package grailscrowd.core.util

import org.apache.log4j.Logger

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicInteger

/**
 * Circuit breaker to be adapter to another service or method call with fail fast
 * functionality.<br />
 * Any exception increments a failure counter until a threshold is reached and internal
 * state is switched to open.<br />
 * New calls will fail in this state with IllegalStateException until a reset time is
 * departed. Then switched to half_closed->closed to delegate as initial. <br />
 * Self healing
 *
 *
 *
 * see: Release It!
 *
 * @author ap
 */
class CircuitBreaker implements CircuitBreakerMBean {

    private static final Logger log = Logger.getLogger(CircuitBreaker.class)

    private volatile CircuitBreakerState state

    private final AtomicLong resetTime

    private final AtomicLong lockedUntil

    private final AtomicInteger threshold

    private final AtomicInteger failureCount

    // todo: als erweiterung koennen ausgewaehlte exceptions auch ignoriert werden


    public CircuitBreaker(){
        state = CircuitBreakerState.CLOSED
        resetTime = new AtomicLong(-1)
        lockedUntil = new AtomicLong(0L)
        threshold = new AtomicInteger(5)
        failureCount = new AtomicInteger(0)
    }


    /**
     * Set milliseconds any open state holds before switching to half_open. <br />
     * State lock will be updateded when current time + new value is < lockUntil.
     * @param milliseconds to keep any open state lock. can be negative to keep circuitbreaker half_open/closed.
     */
    public void setResetTime(long timeInMillis) {
		resetTime.set(timeInMillis)
		updateLockedUntil()
	}

    /**
     * Get milliseconds any open state holds before switching to half_open. 
     */
    public long getResetTime() {
		return resetTime.get()
	}

    /**
     * Get current value of failure counter.
     */
    public int getFailureCount(){
        return failureCount.get()
    }

    /**
     * Get Date untill this circuitbreaker will not delegate any execution request due to open state.
     * @return date in the future or null when no lock set. 
     */
    public Date getLockedUntil(){
        return !isLocked()?null:new Date(lockedUntil.get())
    }

    /**
     * Set new thewshold value. When new value is below current failure counter<br />
     * counter is set to new threshold.
     * @param value must be >0 and < int.max
     */
    public void setThreshold(int newThreshold){
        if (newThreshold<1){
            throw new IllegalArgumentException("Given threshold parameter must not be <1!")
        }
        threshold.set(newThreshold)
        if (newThreshold< failureCount.get()){
            failureCount.set(newThreshold)
        }
    }

    /**
     * Get current threshold.
     */
    public int getThreshold(){
      return threshold.get()
    }

    /**
     * Get current state of this circuit breaker.
     * @see CircuitBreakerState
     *
     */
    protected CircuitBreakerState getState() {
        synchronized(this){
            if (CircuitBreakerState.OPEN == state){
               if(!isLocked()){
                   if (failureCount.get()<threshold.get()){
                       state = CircuitBreakerState.CLOSED
                   }else{
                      return CircuitBreakerState.HALF_CLOSED
                }
            }
            }
        }
        return state
    }

    /**
     * String representation of CircuitBreakerState. 
     */
    public String getDisplayableState(){
        return getState().toString()
    }

    /** is locked until greater current time.
     */
    private boolean isLocked(){
        return System.currentTimeMillis()<lockedUntil.get()
    }


    /**
     * Clear failureCount and internal status.
     */
    public void reset() {
        log.debug "Circuit breaker reset."
        failureCount.set(0)
        lockedUntil.set(0L)
    }

    /**
     * Open circuit breaker for a time defined in resetTime.
     */
    public void trip(){
          log.debug "Circuit breaker is tripped!"
          if (CircuitBreakerState.OPEN!=getState()){
              state = CircuitBreakerState.OPEN
          }
        lockedUntil.set(newLockedUntilTime())
    }

    /**
     * new lock woud be= currentTime + resetTime
     */
    private long newLockedUntilTime(){
        return System.currentTimeMillis() + resetTime.get()
    }

    /**
     * Update any lock until value with respect to current reset time.
     */
    protected void updateLockedUntil(){
        if (!lockedUntil){ return} // not set, skip
        long newLockTime = newLockedUntilTime()
        if (lockedUntil.get()>newLockTime){
            lockedUntil.set(newLockTime)
        }
    }


    /**
     * Get current service level in percent. <br />
     * @return  ((threshold- failureCount)/threshold )*100
     */
    public float getServiceLevel(){
        return (float)((threshold.get() - failureCount.get()) / threshold.get()) * 100 
    }

    def execute(Closure c){
        switch(getState()){
            case CircuitBreakerState.OPEN:
                  throw new IllegalStateException("Circuit breaker is open!")
          case CircuitBreakerState.HALF_CLOSED:
          case CircuitBreakerState.CLOSED:
            try{
                def result = c.call()
                countSuccess()
                return result
            }catch(Exception e){
                countFailure()
                throw e
            }
        }
    }

    /**
     * Count any successfull processing.
     */
   protected void countSuccess(){
        if (failureCount.get()>0){
            failureCount.decrementAndGet()
        }
    }

    /**
     * Count any exception in processing and trips when threshold reached.
     */
   protected void countFailure(){
        if (failureCount.get()<threshold.get()){
            failureCount.incrementAndGet()
        }
        if (failureCount.get()>=threshold.get()){
            trip()
        }
    }

    public void simulateFailureCall(){
        try{
            execute {log.debug "Simulated failure call!"; throw new IllegalArgumentException("ap, test!") }
        }catch(Exception e){
            log.debug "Test, ignore", e
        }
    }

    public void simulateSuccessCall(){
        try{
            execute { log.debug "Simulated success call!" }
        }catch(Exception e){
            log.debug "Test, ignore", e
        }
    }

}