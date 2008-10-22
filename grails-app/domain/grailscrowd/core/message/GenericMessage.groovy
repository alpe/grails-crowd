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
    
    /** DB entry last modified field, automatically set */
    Date lastUpdated

    Date sentDate


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
        sentDate = new Date()
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
        this.status = MessageLifecycle.SEEN  
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

    /**
     * Compare by sentData.
     */
    public int compareTo(other){
      return this.sentDate<=>other.sentDate
    }

}