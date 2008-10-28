package grailscrowd.core.util

/**
 * JMX management interface for Circuit Breaker impl.
 * See: Release It! ISBN-10: 0978739213
 * @author ap
 */
interface CircuitBreakerMBean {

    /**
     * Set milliseconds any open state holds before switching to half_open. <br />
     * State lock will be updateded when current time + new value is < lockUntil.
     * @param milliseconds to keep any open state lock. can be negative to keep circuitbreaker half_open/closed.
     */
    public void setResetTime(long timeInMillis)

     /**
     * Get milliseconds any open state holds before switching to half_open.
     */
    public long getResetTime()

    /**
     * Get current value of failure counter.
     */
    public int getFailureCount()

    /**
     * Get Date untill this circuitbreaker will not delegate any execution request due to open state.
     * @return date in the future or null when no lock set.
     */
    public Date getLockedUntil()

    /**
     * Get current threshold.
     */
    public int getThreshold()
    
    /**
     * Set new thewshold value. When new value is below current failure counter<br />
     * counter is set to new threshold.
     * @param value must be >0 and < int.max
     */
    public void setThreshold(int newThreshold)


      /**
     * Get current state of this circuit breaker.
     * @see CircuitBreakerState
     *
     */
    public String getDisplayableState()

     /**
     * Open circuit breaker for a time defined in resetTime.
     */
    public void trip()

    /**
     * Clear failureCount and internal status.
     */
    public void reset()

    /**
     * Get current service level in percent. <br />
     * @return  ((threshold- failureCount)/threshold )*100
     */
    public float getServiceLevel()

    /**
     * Simulate any exception in excecution. For testing purpose only!
     */
    public void simulateFailureCall()
    
    /**
     * Simulate a successfull excecution. For testing purpose only!
     */
    public void simulateSuccessCall()
}