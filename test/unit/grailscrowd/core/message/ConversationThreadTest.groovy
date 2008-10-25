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
           anySenderFixture = new MemberFixture()
           anyRecipientFixture = new MemberFixture()
           anySubject = 'This is any subject'
           anyBody = 'Hello, this is any body'
           anySenderFixture.createTestData()
           anyRecipientFixture.createTestData()
           threadFixture = new ConversationThreadFixture()
       }

    public void testGetNumberOfNewMessagesFor_2new_2returned(){
        threadFixture.participationMemberFixtures= [anySenderFixture, anyRecipientFixture]
        threadFixture.messageFixtures.add(new MessageFixture(anySenderFixture, threadFixture))
        threadFixture.messageFixtures.add(new MessageFixture(anySenderFixture, threadFixture))
        thread = threadFixture.createTestData()
        println thread.dump()
        assertThat(thread.getNewMessagesFor(anyBody).size(), is (2))

    }

}