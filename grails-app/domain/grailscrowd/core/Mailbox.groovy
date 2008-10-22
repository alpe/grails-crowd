package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.core.message.ConversationThread
import org.hibernate.SessionFactory

/** added a test line to determine svn working status (mgk) **/
//The idea of mailbox is kind of like del.icio.us for:username idea.
//The mailbox could be used to communicate invitations and requests approvals to join a project, etc.
//Another way would be to use regular email, but that would be the "more intrusive" option, IMO 

class Mailbox {

    /** maximum days any message is visible in in- or sentbox */
    public static final int MAX_DAYS_VISIBILITY = 80

    SortedSet conversations
    
    static hasMany = [conversations: ConversationThread]
    static belongsTo = [member: Member]
//    static fetchMode = [messages: 'eager']


    /** has any new message in any thread */
    def hasAnyNewMessages() {
        return getNumberOfNewMessages()
//            conversations.any{it.hasAnyNewMessages(member)}
    }

    /** get number of new message in all thread */
    def getNumberOfNewMessages() {
        def result =  Mailbox.executeQuery(
            "select count(m.id) from grailscrowd.core.Mailbox as b inner join b.conversations as c inner join c.messages as m where b.id=? and m.status =? and m.fromMember!=?",
                [id, MessageLifecycle.NEW, member.name]
        )
        if (result){
            result = result.iterator().next()
        }
        println "found: "+result
        return result
        
/*        conversations.inject(0){count, it->
            count+it.getNumberOfNewMessagesFor(member)
        }
*/
    }


    def visibleInInbox = {msg->
        !msg.isDeleted() && msg.sentDate >( new Date()-MAX_DAYS_VISIBILITY)
    }

/*
    def getInboxMessageAndMarkAsSeen(id) {
        def msg = getInboxMessage(id)
        msg?.markAsSeen()
        return msg
    }
    */

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


    /** get converation thread for given message type and grails project
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
 */
    def deleteInboxMessage(id){
/*        def msg = getInboxMessage(id)
        if (!msg){
            return false
        }
        msg.markAsDeleted()
        return true
        */
    }

    public def getTheadById(id){
        return conversations.find{it.id == id}
    }

    public def getTheadByIdAndMarkAllAsSeen(id){
        def result =getTheadById(id)
        result.getNewMessagesFor(member).each{it.markAsSeen()}
        return result
    }

    public Collection getInboxThreads(){
        return conversations.grep{it.containsMessageFor(member)}
    }

    public Collection getSentboxThreads(){
        return conversations.grep{it.containsMessageFrom(member)}
    }


    /**
     * Get all messages sent within the last 80 days.
     * @return List of GenericMessages

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
         */

}
