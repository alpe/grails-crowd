package grailscrowd.core.message
import grailscrowd.core.*
import grailscrowd.util.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat


/**
 * @author ap
 */
class ConversationThreadTest extends AbstractBaseUnitTestCase{

    def anySenderFixture,anyRecipientFixture
    def anySubject, anyBody
    ConversationThreadFixture threadFixture

    ConversationThread thread

       void setUp(){
           super.setUp()
           thread = null
           anySenderFixture = MemberFixture.ottoFixture
           anyRecipientFixture = MemberFixture.donnyFixture
           anySubject = 'This is any subject'
           anyBody = 'Hello, this is any body'
           threadFixture = new ConversationThreadFixture()
       }

    public void testGetNumberOfNewMessagesFor_2new_2returned(){
        threadFixture.participationMemberFixtures= [anySenderFixture, anyRecipientFixture]
        threadFixture.messageFixtures.add(new MessageFixture(anySenderFixture, threadFixture))
        threadFixture.messageFixtures.add(new MessageFixture(anySenderFixture, threadFixture))
        thread = threadFixture.testData
        assertThat(thread.getNewMessagesFor(anyRecipientFixture.testData).size(), is (2))

    }

}