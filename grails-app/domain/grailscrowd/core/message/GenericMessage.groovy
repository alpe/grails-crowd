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

    /** DB entry last modified field, automatically set  */
    Timestamp lastUpdated

    Timestamp sentDate

    /** internal unique member name  */
    String fromMember


    GenericMessagePayload payload
    
    ConversationThread thread
    
    Set statusContext

    static belongsTo = [thread: ConversationThread]

    static hasMany = [statusContext: MessageStatusContext]

    static transients = ['subject']


    static constraints = {
        fromMember(nullable: false, blank: false)
        payload(nullable: false)
        thread(nullable: false)
    }

    GenericMessage() {
        sentDate = new Timestamp(System.currentTimeMillis())
    }


    /**
     * Get message status for current reader.
     * When not set message is always new. When new a new MessageStatusContext is persisted.
     * @return status in MessageLifecycle for current reader  
     */
    private MessageLifecycle getCurrentStatus(currentReader) {
        if (!currentReader) { return MessageLifecycle.NEW }
        return findOrCreateContext(currentReader).status
    }

    /**
     * Set message status for current reader.
     * @param newStatus status to set
     */
    private void setCurrentStatus(currentReader, MessageLifecycle newStatus) {
        if (!currentReader) {return}
        findOrCreateContext(currentReader).switchToStatus(newStatus)
    }

    /**
     * Load status context for corrent reader or create a one when none exists.
     * @return persistent status context 
     */
    private MessageStatusContext findOrCreateContext(currentReader) {
        def context = getStatusContext().find{it.readerName == currentReader.name}
        if (!context) {
            log.debug "Creating status context for reader: "+ currentReader
            context = MessageStatusContext.newInstance(currentReader)
            addToStatusContext(context)
        }
        return context
    }

    /**
     * Get message subject. 
     */
    public String getSubject() {
        return thread.getSubject(this)
    }

    def markAsSeen(currentReader) {
        setCurrentStatus(currentReader, MessageLifecycle.SEEN)
    }

    def markAsDeleted(currentReader) {
        setCurrentStatus(currentReader, MessageLifecycle.DELETED)
    }

    def isUnread(Member currentReader) {
        if (isSender(currentReader)){
            return !isSeenByAnyMember()
        }
        MessageLifecycle.NEW == getCurrentStatus(currentReader)
    }

    def isDeleted(currentReader) {
        MessageLifecycle.DELETED == getCurrentStatus(currentReader)
    }

    /**
     * Is message not new for any member. Can be read or already deleted.
     * @return boolean result
     */
    def isSeenByAnyMember(){
        return statusContext?.any{it.status!=MessageLifecycle.NEW}
    }

    /**
     * Is Message delete in all current member context?
     * @return boolean result
     */
    def isDeletedByAllReaders(){
        return statusContext?.every{it.status==MessageLifecycle.DELETED}
    }

    def isSystemMessage() {
        payload.with {isSystemPayload()}
    }

    def isAnswered() {
        thread.hasResponse(this)
    }


    def isSender(Member member) {
        member && isSender(member.name)
    }

    protected isSender(String name) {
        name && this.fromMember == name
    }

    def getSender() {
        thread.getSender(this)
    }

    def getRecipients() {
        thread.getRecipients(this)
    }

    /**
     * Compare by sentData desc
     */
    public int compareTo(other) {
        int result =  other.sentDate <=> this.sentDate
        // must match "consistent with equals" contract for TreeSet
        return result?:this.hashCode()<=>other.hashCode()        
    }

}