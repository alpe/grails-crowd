package grailscrowd.core

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import grailscrowd.core.message.*
import grailscrowd.util.*

/**
 * @author ap
 */
class MailboxTest extends AbstractBaseUnitTestCase{

    Mailbox mailbox
    
    MailboxFixture fixi
    MemberFixture ownerFixture, anyOtherMemberFixture
    List threadFixtures
    def sentMessageFixture, recievedMessageFixture

    void setUp(){
        super.setUp()
        // init mailbox with an in and out thread
        fixi = new MailboxFixture()
        ownerFixture = fixi.ownerFixture
        anyOtherMemberFixture = new MemberFixture(fixtureType:MemberFixtureType.DONNY_DUEMPLEMEIER)
        threadFixtures = [new ConversationThreadFixture(topic:'thread1',participationMemberFixtures:[ownerFixture, anyOtherMemberFixture])]
        assert threadFixtures[0]
        threadFixtures.add  new ConversationThreadFixture(topic:'thread2',participationMemberFixtures:[anyOtherMemberFixture, ownerFixture])
        assert threadFixtures[1]        
        fixi.threadFixtures = threadFixtures

        sentMessageFixture = new MessageFixture(ownerFixture, threadFixtures[0])
        recievedMessageFixture = new MessageFixture(anyOtherMemberFixture, threadFixtures[1])
        threadFixtures[0].messageFixtures= [sentMessageFixture]
        threadFixtures[1].messageFixtures= [recievedMessageFixture]
        mailbox = fixi.createTestData()
    }


    void testGetSentboxThreads_singleMsgWithoutResp_resultIsExpectedThread(){
        assertThat(mailbox.sentboxThreads, is([threadFixtures[0].testData]))
    }

    void testGetInboxThreads__singleMsgWithoutResp_resultIsExpectedThread(){
        assertThat(mailbox.inboxThreads, is([threadFixtures[1].testData]))
    }

}
