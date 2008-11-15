package grailscrowd.core.message

import grailscrowd.core.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat

/**
 * @author ap
 */
class MessageServiceIntegrationTest extends GroovyTestCase {

    MessageService messageService

    def anySender
    def anyRecipient
    def memberFixi

     void setUp(){
         super.setUp()
         memberFixi = new MemberDBFixture()
         anyRecipient = memberFixi.getAnyNewSubscribedMember()
     }

     void tearDown(){
         // TODO: cleanup
     }

    void testStartNewConversation_freeFormMessage_persisted(){
        def anySubject = "anySubject"
        def anyBody = "anyBody"
        def message = null
        ConversationThread.withTransaction{ctx->
            10.times{index->
            anySender = memberFixi.getAnyNewSubscribedMember()
            message = FreeFormMessageFactory.createNewMessage(anySender, anyBody)
            messageService.startNewConversation( anySubject,  anySender,  anyRecipient, message)            
            anyRecipient.save(flush:true)
            assertThat(message.id, is(notNullValue()))
            def persistentMessage = anyRecipient.mailbox.getInboxMessageById(message.id) 
            assertThat(persistentMessage, is(notNullValue()))
            assertThat(persistentMessage.subject, is(anySubject))
            assertThat GenericMessage.exists(message.id), is(true)
            }
            assertThat(anyRecipient.mailbox.getInboxThreads(0, 10000).size(), is(10))
        }
       // assertThat(anyRecipient.mailbox.getNumberOfNewMessages(), is(10L))
        
    }

}