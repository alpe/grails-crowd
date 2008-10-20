package grailscrowd.core.message

import grailscrowd.core.Mailbox
import grailscrowd.core.Member 

/**
 * Base class to all message implementation with common attributes and methods.
 * Must not be instanciated by anyone directly. Use any message factory to create and
 * set the payload.
 * @author ap
 */
class GenericMessage implements Comparable {

    Date sentDate

    /** internal unique member name */
    String fromMember

    MessageLifecycle status

    GenericMessagePayload payload


    static belongsTo = [mailbox: Mailbox]
    
    ConversationThread thread

    static transients =  ['subject']

    static constraints = {
        fromMember(nullable:false, blank: false)
        status(nullable:false)
        payload(nullable:false)
        thread(nullable:false)
    }

    GenericMessage(){
        sentDate = new Date()
        status = MessageLifecycle.NEW
    }

    /**
     * Get message subject. 
     */
    public String getSubject(){
            return thread.getSubject(this)
    }
 
    def markAsSeen() {
        this.status = MessageLifecycle.SEEN  
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
       member && this.fromMember == member.name
   }
    def isRecipient(Member member){
        member && mailbox.member == member 
    }

    /**
     * Compare by sentData asc.
     */
    public int compareTo(other){
      return other.sentDate<=>this.sentDate
    }

}