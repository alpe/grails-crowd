package grailscrowd.core.message

/**
 * @author ap
 */
class ContextAwareMessageAdapter {

    private def message
//    def thread
    private def currentReader


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
        return isNew()
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


    boolean isNew() {
        message.isNew(currentReader)
    }

    boolean isDeleted() {
        message.isDeleted(currentReader)
    }

    /**
     * Create new View adapter for inbox messages.
     */
    static ContextAwareMessageAdapter newInstance(currentReader, message){
        if (!message){return null}
        return  new ContextAwareMessageAdapter(currentReader:currentReader, message:message)
    }


}