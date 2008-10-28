package grailscrowd.core.message

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Jmx managed FIFO to store messagesToMail for email notification like in grails.org.
 *
 * @author ap
 */
class MessageNotificationQueue implements MessageNotificationQueueMBean {

    private final AtomicBoolean active = new AtomicBoolean(true)

    private final Queue messagesToMail = new ConcurrentLinkedQueue()

    /**
     * @see MessageNotificationQueueMBean#getCurrentQueueQuantity()
     */
    public int getCurrentQueueQuantity() {
        return messagesToMail.size()
    }

    /**
     * @see MessageNotificationQueueMBean#isAcceptingNewNotification()
     */
    public boolean isAcceptingNewNotification() {
        return active.get()
    }

    /**
     * @see MessageNotificationQueueMBean#declineNewNotifications()
     */
    public void declineNewNotifications() {
        active.set(false)
    }

    /**
     * @see MessageNotificationQueueMBean#acceptNewNotifications()
     */
    public void acceptNewNotifications() {
        active.set(true)
    }


    /**
     * @see MessageNotificationQueueMBean#reset()
     */
    public void reset() {
        messagesToMail.clear()
        active.set(true)
    }

    /**
     * Put given message into queue when acceptance flag is enabled. (default: setting)
     */
    public void pushOnStack(message) {
        if (active.get()) {
            messagesToMail << message
        }
    }

    /**
     * Remove first element from queue and return.
     */
    public def pollOffStack() {
        messagesToMail.poll()
    }


}

