package grailscrowd.core.message

/**
 * Jmx managed FIFO to store messagesToMail for email notification like in grails.org.
 *
 * @author ap
 */
interface MessageNotificationFIFOMBean {

    /**
     * Get current elements in queue.
     * @return quantity
     */
    public int getCurrentQueueQuantity()

    /**
     * Is fifo impl currently acception new notifications.
     * @return result
     */
    public boolean isAcceptingNewNotification()

    /**
     * Silently ignore all new notifications. They won't be pushed to fifo.  
     */
    public void declineNewNotifications()

    /**
     * Enable fifo to accept new messages.
     */
    public void acceptNewNotifications()
}