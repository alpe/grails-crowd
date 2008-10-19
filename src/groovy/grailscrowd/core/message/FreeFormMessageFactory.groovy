package grailscrowd.core.message

import grailscrowd.core.Member
/**
 * Factory to create free form messages.
 * @author ap
 */
class FreeFormMessageFactory extends AbstractMessageFactory{


      /**
     * Create a free form message with given subject and body.
     * @return message
     */
    public static def createNewMessage(Member mailCreator, String subject, String body){
        if (subject==null){
            throw new IllegalArgumentException("Given subject parameter must not be null!")
        }
        if (body==null){
            throw new IllegalArgumentException("Given body parameter must not be null!")
        }
        if (!body && !subject){
            throw new IllegalArgumentException("Subject and body must not empty!")
        }
        def payload = new FreeFormMessagePayload(body:body)
        return createSimpleMail(mailCreator.name, payload, createNewThread(subject))
    }
   /**
     * Create a free form message with given subject and body.
     * @return message
     */
    public static def createResponseMessage(Member mailCreator, String subject, String body, long threadId){
        def thread = ConversationThread.get(threadId);
        def payload = new FreeFormMessagePayload(subject:subject, body:body)
        String senderInternalName = mailCreator.name
        return createSimpleMail(mailCreator.name, payload, thread) 
    }

}