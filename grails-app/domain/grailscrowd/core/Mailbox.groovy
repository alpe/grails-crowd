package grailscrowd.core

import grailscrowd.core.message.GenericMessage

/** added a test line to determine svn working status (mgk) **/
//The idea of mailbox is kind of like del.icio.us for:username idea.
//The mailbox could be used to communicate invitations and requests approvals to join a project, etc.
//Another way would be to use regular email, but that would be the "more intrusive" option, IMO 

class Mailbox {


    /** maximum days any message is visible in in- or sentbox */
    public static final int MAX_DAYS_VISIBILITY = 80

    SortedSet messages
    
    static hasMany = [messages: GenericMessage]
    static belongsTo = [member: Member]
    static fetchMode = [messages: 'eager']


    def hasAnyDisplayableMessages() {
        !getInboxMessages().empty
    }

    def hasAnyNewMessages() {
        this.messages.any { it.isNew() && visibleInInbox(it) }
    }

    def getNumberOfNewMessages() {
        this.inboxMessages.findAll {it.isNew() }.size()
    }

    def getInboxMessages(){
        this.messages.findAll(visibleInInbox)
    }

    private def getInboxMessage(id) {
        this.inboxMessages.find {it.id}
    }

    def visibleInInbox = {msg->
        !msg.isDeleted() && msg.sentDate >( new Date()-MAX_DAYS_VISIBILITY)
    }

    def getInboxMessageAndMarkAsSeen(id) {
        def msg = getInboxMessage(id)
        msg?.markAsSeen()
        return msg
    }

    @Deprecated
    def markMessageAsArchived(id) {
        throw new AssertionError("Will be removed after refactoring")
        // remove
    }

    @Deprecated
    def markMessageAsAcknowleged(id) {
        throw new AssertionError("Will be removed after refactoring")        
        // remove

    }


    /** get converation thread for given message type and grails project */
    def getConverationThread(systemMessageType, grailsProject){
        def msg = getInboxMessages().find{
             return it.isSystemMessage() &&
             it.payload.type==systemMessageType &&
             it.payload.projectId == grailsProject.id
         }
        return msg?.thread
    }

    def getInboxConversationAndMarkMessageAsSeen(id){
        log.debug "Looking for conversation of message id: "+id
        def msg = getInboxMessageAndMarkAsSeen(id)
        return msg?.thread
    }

    def deleteInboxMessage(id){
        def msg = getInboxMessage(id)
        if (!msg){
            return false
        }
        msg.markAsDeleted()
        return true
    }


    /**
     * Get all messages sent within the last 80 days.
     * @return List of GenericMessages
     */
    public List getSentboxMessages(){
        def c = GenericMessage.createCriteria()
        return  c.list(sentboxMessageCriteria)
    }
   

    def getSentboxMessage(id){
        def c = GenericMessage.createCriteria()
        return  c.get{
                idEq(id)
                and(sentboxMessageCriteria)
                }
    }

    private def sentboxMessageCriteria ={
            and {
                eq('fromMember', member.name)
                gt('sentDate', new Date()-MAX_DAYS_VISIBILITY)  // not older than x days
            }
            order("sentDate", "desc")
        }


}
