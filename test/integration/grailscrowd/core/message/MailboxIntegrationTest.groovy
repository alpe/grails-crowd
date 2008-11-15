package grailscrowd.core.message

import grailscrowd.core.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat


/**
 * @author ap
 */
class MailboxIntegrationTest extends GroovyTestCase {

    MessageService messageService

    def anySender
    def anyRecipient
    def memberFixi
    def mailbox
    def createMessages
    def messagesToDel = []

     void setUp(){
         super.setUp()
         memberFixi = new MemberDBFixture()
         anySender = memberFixi.getAnyNewSubscribedMember()
         anyRecipient = memberFixi.getAnyNewSubscribedMember()
         mailbox = anyRecipient.mailbox
         createMessages = MessageTestUtils.createMessages
         createMessages.delegate = this
     }


     void tearDown(){
//          messagesToDel.each{
//            anyRecipient.mailbox.removeFromConversations(it.thread)
//          }
     }


    void testGetInboxThreads(){
        def msgs = createMessages(5, 5)
        messagesToDel.addAll(msgs)


        def result = mailbox.getInboxThreads(0, 1000)
        def converations = new HashSet()
        msgs.each{converations.add(it.thread)}
        assertThat(result.sort(), is (converations.sort()))
    }
}