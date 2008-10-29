package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.core.message.ConversationThread

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

    static constraints = {
        member(nullable: false)
    }



    /** has any new message in any thread */
    boolean hasAnyNewMessages() {
        return getNumberOfNewMessages()
    }

    /** get number of new message in all thread */
    def getNumberOfNewMessages() {
        assert getMember()
        assert getMember().name
        // TODO: put into cache when performance is bad
        StringBuilder sb = new StringBuilder()
        sb << "select count(m.id) from grailscrowd.core.Mailbox as b "
        sb << "inner join b.conversations as c inner join c.messages as m "
        sb << "left join m.statusContext s "
        sb << "where (s is null or s.readerName=:name and s.status=:status) "
        sb << "and b.id=:boxId and m.fromMember!=:name"
        def result =  Mailbox.executeQuery(sb.toString() ,[boxId:id, name:getMember().name, status:MessageLifecycle.NEW])
        if (result){
            result = result.iterator().next()
        }
        return result
    }


    def visibleInInbox = {msg->
        !msg.isDeleted() && msg.sentDate >( new Date()-MAX_DAYS_VISIBILITY)
    }

    def deleteInboxThread(id){
       getTheadById(id)?.markInboxMessagesAsDeleted(member)
    }

    def deleteSentboxThread(id){
         getTheadById(id)?.markSentboxMessagesAsDeleted(member)
    }

    public def getTheadById(id){
        return getConversations().find{it.id == id}
    }

    /** Mark all unread messages of given Thread as read.
     */
    public void markThreadAsSeen(ConversationThread thread){
        thread?.markNewMessagesAsSeen(member)
    }

    /** Get Collection of threads with messages for this member
     * @return collection
     */
    public Collection getInboxThreads(){
        log.debug "Fetching inbox conversations for member: "+member
        return getConversations().grep{
            log.debug "Searching thread $it.id for inbox messages."
            it.containsMessageFor(member)
        }
    }

    /** Get Collection of threads with messages sent out by this member.
     * @return collection
     */
    public Collection getSentboxThreads(){
        return getConversations().grep{it.containsMessageFrom(member)}
    }

    /** Find inbox message by given id.
     * @return msg or null when not found.
     */
    def getInboxMessageById(msgId){
        for (thread in getConversations()){
            def result = thread.getMessageByIdFor(msgId, member)
            if (result){ return result}
         }
    }

}
