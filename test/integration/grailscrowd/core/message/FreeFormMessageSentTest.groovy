package grailscrowd.core.message

import grailscrowd.core.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

/**
 * @author ap
 */
class FreeFormMessageSentTest extends GroovyTestCase {

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

    void testFreeFormMessagePersisted(){
        def anySubject = "anySubject"
        def anyBody = "anyBody"
        def message = null
        GenericMessage.withTransaction{ctx->
        message = FreeFormMessageFactory.createMessage(anySender, anySubject, anyBody)
        messageService.submit(anyRecipient, message)
        anyRecipient.save(flush:true)
        }
        assertThat(message.id, is(notNullValue()))
        def persistentMessage = GenericMessage.get(message.id)
        assertThat(persistentMessage, is(notNullValue()))
        assertThat(persistentMessage.subject, is(anySubject))
        
    }

}