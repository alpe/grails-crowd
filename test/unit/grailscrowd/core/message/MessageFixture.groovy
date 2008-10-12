package grailscrowd.core.message

import grailscrowd.core.*

/**
 * @author ap
 */
class MessageFixture {


    static GenericMessage getAnyInviationMessage(){
        def anySender = MemberFixture.ottoOne
        def anyProject = GrailsProjectFixture.grailscrowdSample
        return SystemMessageFactory.createInvitation(anySender, anyProject)
    }

    static GenericMessage getAnyFreeFormMessage(){
        def result = new GenericMessage()
        result.setPayload(new FreeFormMessagePayload(subject:'Testmail', body:'Hello World,\nthis is a test message.\n'))
        return result
    }
}