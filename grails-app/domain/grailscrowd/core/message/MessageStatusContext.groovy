package grailscrowd.core.message

import java.sql.Timestamp
import grailscrowd.core.Member

/**
 * A message could be read in a conversation by multiple members. In status context each
 * individual lifecycle is stored.
 * 
 * @author ap
 */
class MessageStatusContext {

    static belongsTo = [message:GenericMessage]

    /** DB entry last modified field, automatically set  */
    Timestamp lastUpdated

    /** DB entry created field, automatically set */
    Timestamp dateCreated

    MessageLifecycle status

    String readerName

    static constraints = {
        status(nullable: false)
        readerName(nullable: false, blank:false)        
     }


    MessageStatusContext(){ }


    /**
     * Factory method to create and initialize status context instance.
     * @return new instance
     */
    static MessageStatusContext newInstance(Member reader){
        if (!reader){throw new IllegalArgumentException("Given reader instance must not be null!") }
        return new MessageStatusContext(readerName:reader.name, status:MessageLifecycle.NEW)
    }


    /**
     * Set new status when an allowed transition from current to new one exists.
     * @return true when successfull
     * @see MessageLifecycle#canSwitchTo(MessageLifecycle) 
     */
    boolean switchToStatus(MessageLifecycle newStatus){
        if (status.canSwitchTo(newStatus)){
            status=newStatus
            return true
        }
        return false
    }
}