package grailscrowd.core.message

import grailscrowd.core.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

/**
 * @author ap
 */
class MessageServiceIntegrationTest extends GroovyTestCase {

    MessageService messageService

     def anySender
     def anyRecipient

     void setUp(){
         super.setUp()
         def memberFixi = new MemberDBFixture()
         anySender = memberFixi.getAnyNewSubscribedMember()
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
            message = FreeFormMessageFactory.createNewMessage(anySender, anyBody)
            messageService.startNewConversation(anySubject, anyRecipient, message)
            anyRecipient.save(flush:true)
            assertThat(message.id, is(notNullValue()))
            def persistentMessage = GenericMessage.get(message.id)
            assertThat(persistentMessage, is(notNullValue()))
            assertThat(persistentMessage.subject, is(anySubject))
        }        
    }

}