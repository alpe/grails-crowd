package grailscrowd.core.message

import grailscrowd.core.Member 

/**
 * Message adapter to generic message with current reader as context.
 * To be used within Controller or Views.
 *
 * @author ap
 */
class ContextAwareMessageAdapter implements Comparable{

    private def message
//    def thread
    private def currentReader

    /** was message new when fetching it */
    private boolean isNewFlag

    def getSender() {
        return message.getSender()
    }

    def getRecipients() {
        return message.getRecipients()
    }

    def getId() {
        return message.id
    }

    def getSubject() {
        message.subject
    }

    boolean isSystemMessage() {
        return message.isSystemMessage()
    }

    def getSentDate() {
        return message.sentDate
    }

    boolean getHasReply() {
        return message.isAnswered()
    }

    boolean isUnread() {
       isNewFlag
    }
    boolean isNew() {
        return isNewFlag
    }
    

    def getPayload() {
        if (message.isSystemMessage()) {
            boolean responseActionPending = message.getSender().name != currentReader.name &&
                    message.payload.isResponseActionPending(currentReader)
            return [messageCode: message.payload.messageCode, projectId: message.payload.projectId,
                    responseActionPending: responseActionPending]
        } else {
            return [body: message.payload.body]
        }
    }


    void markAsSeen() {
        message.markAsSeen(currentReader)
    }

    void markAsDeleted() {
        message.markAsDeleted(currentReader)
    }


    boolean isDeleted() {
        message.isDeleted(currentReader)
    }

    /**
     * Create new View adapter for inbox messages.
     * @return wrapped message or null when deleted for user or message was null
     */
    static ContextAwareMessageAdapter newInstance(Member currentReader, GenericMessage message){
        if (!message){return null}
        return  new ContextAwareMessageAdapter(currentReader:currentReader, message:message, isNewFlag:message.isUnread(currentReader))
    }

    /**
     * Compare by sentData asc
     */
    public int compareTo(other) {
        int result =  this.sentDate <=> other.sentDate
        // must match "consistent with equals" contract for TreeSet
        return result?:this.hashCode()<=>other.hashCode()
    }

}