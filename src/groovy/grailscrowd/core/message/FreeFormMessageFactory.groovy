package grailscrowd.core.message

import grailscrowd.core.Member

/**
 * Factory to create free form messages.
 * @author ap
 */
class FreeFormMessageFactory extends AbstractMessageFactory {


    /**
     * Create a free form message with given subject and body.
     * @return message
     */
    public static def createNewMessage(Member mailCreator, String body) {
        if (body == null) {
            throw new IllegalArgumentException("Given body parameter must not be null!")
        }
        def payload = new FreeFormMessagePayload(body: body)
        return createSimpleMail(mailCreator.name, payload)
    }

}