package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import java.sql.Timestamp


/**
 * @author ap
 */
class GenericMessageTest extends AbstractBaseUnitTestCase{

    def message
    def messageFixture

    void setUp(){
        super.setUp()
        messageFixture = new MessageFixture()
        message = messageFixture.createTestData()
    }
    

    void testIsSystemMessage_withInvitationMessage_expectTrue(){
        message = messageFixture.createTestData(MessageFixtureType.INVITATION)
        assertTrue(message.isSystemMessage())
    }


    void testIsSystemMessage_withFreeFormMessage_expectFalse(){
            assertFalse(message.isSystemMessage())
        }
/*
    void testIsDeleted_notDelStatus_returnsFalse(){
        for (status in MessageLifecycle.values()){
            if (status == MessageLifecycle.DELETED){ continue}
            message.statusContext=status
            assertFalse(message.isDeleted())
        }
    }
    void testIsDeleted_delStatus_returnsTrue(){
        message.statusContext=[MessageLifecycle.DELETED] as Set
        assertThat(message.isDeleted(), is(true))
    }
    void testMarkAsDeleted_success(){
        message.markAsDeleted()
        assertThat(message.isDeleted(), is(true))
    }
  */
    void testCompareTo_samples_orderedAsExpected(){
        message.sentDate = new Timestamp(System.currentTimeMillis())
        def message2 = messageFixture.createTestData(MessageFixtureType.INVITATION)
        message2.sentDate = new Timestamp((new Date()-10).time)
        def message3 = messageFixture.createTestData(MessageFixtureType.FREEFORM)
        message3.sentDate = new Timestamp((new Date()-5).time)
        def sortedList = [message, message2, message3].sort()
        assertThat("recieved: "+sortedList*.sentDate, sortedList, is ([message, message3, message2]))
    }

    void testGetSubject_subjectIsConversationTopic_true(){
        // get subject from conversation
        messageFixture.conversationThreadFixture.testData.topic =MessageFixture.ANY_FREEFORM_SUBJECT
        assertThat( message.subject, is (MessageFixture.ANY_FREEFORM_SUBJECT))
    }


/*    void testGetSubject_invitationResponseMessages_subjectAsExpected(){
        message = MessageFixture.anyInviationAcceptMessage
        assertThat('System message subjects are in message.property. Plesase look for modifications.',
                message.subject, is (MessageFixture.ANYINVITATION_ACCEPT_SUBJECT))
        message = MessageFixture.anyInviationRejectedlMessage
        assertThat('System message subjects are in message.property. Plesase look for modifications.',
                message.subject, is (MessageFixture.ANYINVITATION_REJECTION_SUBJECT))

    }
  */


}