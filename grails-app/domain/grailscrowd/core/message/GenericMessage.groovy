package grailscrowd.core.message

import grailscrowd.core.Mailbox

/**
 * Base class to all message implementation with common attributes and methods.
 * Must not be instanciated by anyone directly. Use any message factory to create and
 * set the payload.
 * @author ap
 */
class GenericMessage {

    Date sentDate

    /** internal unique member name */
    String fromMember

    MessageLifecycle status

//    GenericMessagePayload payload


    static belongsTo = [mailbox: Mailbox]

    static transients =  ['subject']

    static constraints = {
//        subject(blank: false, maxSize: 1000)
//        body(blank: false, maxSize: 4000)
//        projectParticipationId(nullable: true)
        fromMember(blank: false, maxSize: 50)
        status(nullable:false)
//        payload(nullable:false)
    }

    GenericMessage(){
        sentDate = new Date()
        status = MessageLifecycle.NEW
    }

    /**
     * Get message subject. 
     */
    public String getSubject(){
  //      return payload.subject
    }
 
    def markAsSeen() {
        this.status = MessageLifecycle.SEEN
    }

    def isNew() {
        this.status == MessageLifecycle.NEW
    }

    def isVisibleInInbox(){
        this.status != MessageLifecycle.DELETED        
    }


}

