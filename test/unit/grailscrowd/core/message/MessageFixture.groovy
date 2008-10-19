package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.MockUtils

/**
 * @author ap
 */
class MessageFixture {

    static String ANY_FREEFORM_SUBJECT = 'Testmail'
    static String ANYINVITATION_SUBJECT = 'Project Invitation'

    /** fake message.property access  */
    static def createFakeMessages(def messageResult){
        [getMessage:{a,b,c,d->messageResult}]
    }

    static GenericMessage getAnyInviationMessage(){
        def anySender = MemberFixture.ottoOne
        def anyProject = GrailsProjectFixture.grailscrowdSample
        def result = SystemMessageFactory.createInvitation(anySender, anyProject)
        result.payload.messageSource = createFakeMessages(ANYINVITATION_SUBJECT)
        MockUtils.mockDomain(result)
        return result
    }

    static GenericMessage getAnyFreeFormMessage(def subject =ANY_FREEFORM_SUBJECT){
        def result = new GenericMessage()
        result.setPayload(new FreeFormMessagePayload(subject:subject, body:"Hello World,\nthis is a test message.\n with subject: $subject"))
        MockUtils.mockDomain(result)        
        return result
    }
}