package grailscrowd.core.message
/**
 * @author ap
 */
abstract class AbstractMessageFactory {


    /**
     * Add mail body to given payload
     */
    static def createSimpleMail(String senderInternalName, payload) {
        if (!senderInternalName){
            throw new IllegalArgumentException("Given sender name must not be null or empty!")
        }
        if (!payload){
            throw new IllegalArgumentException('Given payload must not be null!')
        }
        return createSimpleMail(senderInternalName, payload, createNewThread())

    }

    static def createSimpleMail(String senderInternalName, payload, thread) {
        if(!thread){
            throw new IllegalArgumentException("Given conversation thread must not be null!")
        }
        return new GenericMessage(fromMember: senderInternalName, payload: payload, thread:thread)
    }

    static def createNewThread(){
         return new ConversationThread()
    }
}