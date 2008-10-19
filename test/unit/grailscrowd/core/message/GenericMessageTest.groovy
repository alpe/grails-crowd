package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat


/**
 * @author ap
 */
class GenericMessageTest extends AbstractBaseUnitTestCase{

    def message


    void setUp(){
        super.setUp()
        message = MessageFixture.anyFreeFormMessage
    }

    void testIsSystemMessage_withInvitationMessage_expectTrue(){
        message = MessageFixture.anyInviationMessage
        assertTrue(message.isSystemMessage())
    }


    void testIsSystemMessage_withFreeFormMessage_expectFalse(){
            assertFalse(message.isSystemMessage())
        }

    void testIsDeleted_notDelStatus_false(){
        for (status in MessageLifecycle.values()){
            if (status == MessageLifecycle.DELETED){ continue}
            message.status=status
            assertFalse(message.isDeleted())
        }
    }
    void testIsDeleted_delStatus_true(){
        message.status=MessageLifecycle.DELETED
        assertTrue(message.isDeleted())
    }

    void testCompareTo_samples_orderedAsExpected(){
        message = MessageFixture.anyFreeFormMessage
        message.sentDate = new Date()
        def message2 = MessageFixture.anyInviationMessage
        message2.sentDate = new Date()-10
        def message3 = MessageFixture.anyFreeFormMessage
        message3.sentDate = new Date()-5
        def sortedList = [message, message2, message3].sort()
        assertThat("recieved: "+sortedList*.sentDate, sortedList, is ([message2, message3, message]))
    }

    void testGetSubject_freeForm_subjectAsExpected(){
        assertThat( message.subject, is (MessageFixture.ANY_FREEFORM_SUBJECT))
    }
    void testGetSubject_invitationMessage_subjectAsExpected(){
        message = MessageFixture.anyInviationMessage
        assertThat('System message subjects are in message.property. Plesase look for modifications.',
                message.subject, is (MessageFixture.ANYINVITATION_SUBJECT))
    }


}