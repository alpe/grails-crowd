package grailscrowd.core.message

import grailscrowd.core.*

/**
 * @author ap
 */
class GenericMessageTest extends GroovyTestCase{

    def message

    void testIsSystemMessage_withInvitationMessage_expectTrue(){
        message = MessageFixture.anyInviationMessage
        assertTrue(message.isSystemMessage())
    }


    void testIsSystemMessage_withFreeFormMessage_expectFalse(){
            message = MessageFixture.anyFreeFormMessage
            assertFalse(message.isSystemMessage())
        }


}