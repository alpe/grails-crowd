package grailscrowd.core.message

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Jmx managed FIFO to store messagesToMail for email notification like in grails.org.
 *
 * @author ap
 */
class MessageNotificationFIFO implements MessageNotificationFIFOMBean {

    private final AtomicBoolean active = new AtomicBoolean(true)

    private final Queue messagesToMail = new ConcurrentLinkedQueue()

    /**
     * @see MessageNotificationFIFOMBean#getCurrentQueueQuantity()
     */
    public int getCurrentQueueQuantity() {
        return messagesToMail.size()
    }

    /**
     * @see MessageNotificationFIFOMBean#isAcceptingNewNotification()
     */
    public boolean isAcceptingNewNotification() {
        return active.get()
    }

    /**
     * @see MessageNotificationFIFOMBean#declineNewNotifications()
     */
    public void declineNewNotifications() {
        active.set(false)
    }

    /**
     * @see MessageNotificationFIFOMBean#acceptNewNotifications()
     */
    public void acceptNewNotifications() {
        active.set(true)
    }


    /**
     * @see MessageNotificationFIFOMBean#reset()
     */
    public void reset() {
        messagesToMail.clear()
        active.set(true)
    }

    /**
     * Put given message into queue when acceptance flag is enabled. (default: setting)
     */
    public void pushOnStack(GenericMessage message) {
        if (active.get()) {
            messagesToMail << message
        }
    }

    /**
     * Remove first element from queue and return.
     */
    public GenericMessage pollOffStack() {
        messagesToMail.poll()
    }


}

