package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.core.message.ConversationThread

/** added a test line to determine svn working status (mgk) **/
//The idea of mailbox is kind of like del.icio.us for:username idea.
//The mailbox could be used to communicate invitations and requests approvals to join a project, etc.
//Another way would be to use regular email, but that would be the "more intrusive" option, IMO 

class Mailbox {

    SortedSet conversations
    
    Member member
    
    static hasMany = [conversations: ConversationThread]
    
    static belongsTo = [member: Member]

    static constraints = {
        member(nullable: false)
    }



    /** has any new message in any thread */
    boolean hasAnyNewMessages() {
        return getNumberOfNewMessages()
    }

    /** get number of new message in all thread */
    def getNumberOfNewMessages() {
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


    def deleteInboxThread(id){
       getThreadById(id)?.markInboxMessagesAsDeleted(member)
    }

    def deleteSentboxThread(id){
         getThreadById(id)?.markSentboxMessagesAsDeleted(member)
    }

    public def getThreadById(id){
        def thread = ConversationThread.get(id)
        thread?.inThreadContext(member){
            return thread
        }

        return getConversations().find{it.id == id}
    }

    /** Mark all unread messages of given Thread as read.
     */
    public void markThreadAsSeen(ConversationThread thread){
        thread?.markNewMessagesAsSeen(member)
    }

    public Collection getInboxThreads(int offset, int max){
        StringBuilder sb = new StringBuilder()
        sb << "select c "
        sb << "from grailscrowd.core.Mailbox as b "
        sb << "inner join b.conversations as c "
        sb << "inner join c.messages as m "
        sb << "left join m.statusContext s "
        sb << "where  b.id=:boxId and m.fromMember!=:name "
        sb << "and (s is null or s.readerName=:name and s.status!=:status) "
        sb << "group by c.id order by c.lastUpdated desc, c.topic asc"
        def result = Mailbox.executeQuery(sb.toString() ,[boxId:id, name:member.name, status:MessageLifecycle.DELETED],[offset:offset, max:max])
       return result
   }
    
    def getTotalInboxThreads(){
        StringBuilder sb = new StringBuilder()
        sb << "select count(distinct c.id) "
        sb << "from grailscrowd.core.Mailbox as b "
        sb << "inner join b.conversations as c "
        sb << "inner join c.messages as m "
        sb << "left join m.statusContext s "
        sb << "where  b.id=:boxId and m.fromMember!=:name "
        sb << "and (s is null or s.readerName=:name and s.status!=:status) "
        def result = Mailbox.executeQuery(sb.toString() ,[boxId:id, name:member.name, status:MessageLifecycle.DELETED])
        if (result){
            result = result.iterator().next()
        }
        return result
    }


    /** Get Collection of threads with messages sent out by this member.
     * @return collection
     */
    @Deprecated
    public Collection getSentboxThreads(){
        return getConversations().grep{it.containsMessageFrom(member)}
    }
    public Collection getSentboxThreads(int offset, int max){

    }

    /** Find inbox message by given id.
     * @return msg or null when not found.
     */
    def getInboxMessageById(msgId){
        def msg = GenericMessage.get(msgId)
        msg?.thread?.inThreadContext(member){
            return msg
        }
    }

}
