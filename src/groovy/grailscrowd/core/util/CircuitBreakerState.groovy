package grailscrowd.core.util

/**
 * @author ap
 */
enum CircuitBreakerState {
    /** No call is delegated, fail fast Exception will be thrown. */
   OPEN,
   /** lock time is departed but service not recovered */
   HALF_CLOSED,
   /** delegate all calls */
   CLOSED

}
