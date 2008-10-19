package grailscrowd.core.message
/**
 * @author ap
 */
abstract class AbstractMessageFactory {


    /**
     * Add mail body to given payload
     */
    static def createSimpleMail(String senderInternalName, payload,String topic) {
        if (!senderInternalName){
            throw new IllegalArgumentException("Given sender name must not be null or empty!")
        }
        if (!payload){
            throw new IllegalArgumentException('Given payload must not be null!')
        }
        return createSimpleMail(senderInternalName, payload, createNewThread(topic))
    }


    static def createSimpleMail(String senderInternalName, payload, thread) {
        if(!thread){
            throw new IllegalArgumentException("Given conversation thread must not be null!")
        }
        return new GenericMessage(fromMember: senderInternalName, payload: payload, thread:thread)
    }

    static def createNewThread(String topic){
        if (!topic){
            throw new IllegalArgumentException('Given conversation topic must not be null!')
        }
         return new ConversationThread(topic:topic)
    }
    
}