package grailscrowd.core.message

import grailscrowd.core.Mailbox
import grailscrowd.core.Member
import java.sql.Timestamp

/**
 * Base class to all message implementation with common attributes and methods.
 * Must not be instanciated by anyone directly. Use any message factory to create and
 * set the payload.
 * @author ap
 */
class GenericMessage implements Comparable {
    
    /** DB entry last modified field, automatically set */
    Timestamp lastUpdated

    Timestamp sentDate


    /** internal unique member name */
    String fromMember

    MessageLifecycle status

    GenericMessagePayload payload


    static belongsTo = [thread: ConversationThread]

    static transients =  ['subject']

    static constraints = {
        fromMember(nullable:false, blank: false)
        status(nullable:false)
        payload(nullable:false)
//        thread(nullable:false)
    }

    GenericMessage(){
        sentDate = new Timestamp(System.currentTimeMillis())
        status = MessageLifecycle.NEW
    }

    /**
     * Get message subject. 
     */
    @Deprecated
    public String getSubject(){
            return thread.getSubject(this)
    }
 
    def markAsSeen() {
        if(MessageLifecycle.NEW == this.status){
            this.status = MessageLifecycle.SEEN
        }
    }
    
    def markAsDeleted() {
        this.status = MessageLifecycle.DELETED  
    }


    def isNew() {
        MessageLifecycle.NEW == this.status 
    }

    def isDeleted(){
        MessageLifecycle.DELETED == this.status
    }

    def isSystemMessage(){
        payload.with{isSystemPayload()}
    }

    def isAnswered(){
        thread.hasResponse(this)
   }

   def isSender(Member member){
       member && isSender(member.name)
   }
   protected isSender(String name){
        name && this.fromMember == name
   }

   def getSender(){
       thread.getSender(this)
   }

    def getRecipients(){
        thread.getRecipients(this)
    }

    /**
     * Compare by sentData.
     */
    public int compareTo(other){
      return this.sentDate<=>other.sentDate
    }

}