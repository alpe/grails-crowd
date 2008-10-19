package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.MockUtils

/**
 * @author ap
 */
class MessageFixture {


    static GenericMessage getAnyInviationMessage(){
        def anySender = MemberFixture.ottoOne
        def anyProject = GrailsProjectFixture.grailscrowdSample
        def result = SystemMessageFactory.createInvitation(anySender, anyProject)
        MockUtils.mockDomain(result)
        return result
    }

    static GenericMessage getAnyFreeFormMessage(def subject ='Testmail'){
        def result = new GenericMessage()
        result.setPayload(new FreeFormMessagePayload(subject:subject, body:"Hello World,\nthis is a test message.\n with subject: $subject"))
        MockUtils.mockDomain(result)        
        return result
    }
}