package grailscrowd.core.message

import grailscrowd.core.Member
/**
 * Factory to create free form messages.
 * @author ap
 */
class FreeFormMessageFactory {


      /**
     * Create a free form message with given subject and body.
     * @return message
     */
    public static def createMessage(Member mailCreator, String subject, String body){
        if (subject==null){
            throw new IllegalArgumentException("Given subject parameter must not be null!")
        }
        if (body==null){
            throw new IllegalArgumentException("Given body parameter must not be null!")
        }
        if (!body && !subject){
            throw new IllegalArgumentException("Subject and body must not empty!")
        }
        def payload = new FreeFormMessagePayload(subject:subject, body:body)
        String senderInternalName = mailCreator.name
        return new GenericMessage(fromMember: senderInternalName, payload: payload)
    }


}