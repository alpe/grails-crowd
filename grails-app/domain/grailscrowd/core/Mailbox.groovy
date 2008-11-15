package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.core.message.ConversationThread

/** added a test line to determine svn working status (mgk) **/
//The idea of mailbox is kind of like del.icio.us for:username idea.
//The mailbox could be used to communicate invitations and requests approvals to join a project, etc.
//Another way would be to use regular email, but that would be the "more intrusive" option, IMO 

class Mailbox {

    Set conversations
    
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
        StringBuilder sb = new StringBuilder()
        sb << "select count(distinct m) from grailscrowd.core.Mailbox as b "
        sb << "inner join b.conversations as c "
        sb << "inner join c.messages as m "
        sb << "where "
        sb << "b.id=:boxId and m.fromMember!=:name "
        sb << "and (not exists elements(m.statusContext) "
        sb << "or exists ( "
        sb << "  select s from grailscrowd.core.message.MessageStatusContext s "
        sb << "where s.message=m "
        sb << "and (s.readerName!=:name or s.readerName=:name and s.status=:status) "
        sb << " ) "
        sb << ") "
        def result =  Mailbox.executeQuery(sb.toString() ,[boxId:id, name:getMember().name, status:MessageLifecycle.NEW])
        return result?result.iterator().next():result
    }


    def deleteInboxThread(id){
        Mailbox.withTransaction{tx->
            log.debug "Deleting inbox thread with id :$id"
            getThreadById(id)?.markInboxMessagesAsDeleted(member)
        }
    }

    def deleteSentboxThread(id){
        Mailbox.withTransaction{tx->
            log.debug "Deleting sentbox thread with id :$id"
            getThreadById(id)?.markSentboxMessagesAsDeleted(member)
        }
    }

    public def getThreadById(id){
        def thread = ConversationThread.get(id)
        thread?.inThreadContext(member){
            log.debug ("Returning thread: "+thread)
            return thread
        }
    }

    /** Mark all unread messages of given Thread as read.
     */
    public void markThreadAsSeen(ConversationThread thread){
        thread?.markNewMessagesAsSeen(member)
    }

    public Collection getInboxThreads(int offset, int max){
       return getThreads(conversationSelect.curry(true), offset, max)
    }


    def getTotalInboxThreads(){
        return getTotalThreadCount(conversationSelect.curry(true))
    }
    public Collection getSentboxThreads(int offset, int max){
        return getThreads(conversationSelect.curry(false), offset, max)
    }

    def getTotalSentboxThreads(){
        return getTotalThreadCount(conversationSelect.curry(false))
    }

    def getTotalTrashboxThreads(){
       return getTotalThreadCount(deletedThreadSelect())
    }

    public Collection getTrashboxThreads(int offset, int max){
        return getThreads(deletedThreadSelect, offset, max)
    }

    private def getThreads = {Closure select,  int offset, int max->
        StringBuilder sb = new StringBuilder()
        sb << "select distinct c "
        sb << select.call()
        sb << "order by c.lastUpdated desc, c.topic asc"
        def result = Mailbox.executeQuery(sb.toString() ,[boxId:id, name:member.name, status:MessageLifecycle.DELETED],[offset:offset, max:max])
       return result
   }

    private def getTotalThreadCount = {Closure select ->
        StringBuilder sb = new StringBuilder()
        sb << "select count(distinct c) "
        sb << select.call()
        def result = Mailbox.executeQuery(sb.toString() ,[boxId:id, name:member.name, status:MessageLifecycle.DELETED])
        return result?result.iterator().next():result
    }

    def conversationSelect = {boolean inbox ->
        StringBuilder sb = new StringBuilder()
        sb << "from grailscrowd.core.Mailbox as b "
        sb << "inner join b.conversations as c "
        sb << "where  b.id=:boxId "
        sb << "and exists ( "
        sb << "  select m from grailscrowd.core.message.GenericMessage m left join m.statusContext s "
        sb << "where m.thread=c "
        if (inbox){
            sb << "and m.fromMember!=:name "
        }else{
            sb << "and m.fromMember=:name "
        }
        sb << "and (s is null or s.readerName=:name and s.status!=:status) "
        sb << " ) group by c "
        return sb
    }

    def deletedThreadSelect = {
        StringBuilder sb = new StringBuilder()
        sb << "from grailscrowd.core.Mailbox as b "
        sb << "inner join b.conversations as c "
        sb << "where  b.id=:boxId "
        sb << "and not exists ( "
        sb << "  select m from grailscrowd.core.message.GenericMessage m left join m.statusContext s "
        sb << "where m.thread=c "
        sb << "and (s is null or s.readerName=:name and s.status!=:status) "
        sb << " ) "
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
