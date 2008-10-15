package grailscrowd.core.message

import grailscrowd.core.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

/**
 * @author ap
 */
class FreeFormMessageFactoryTest extends GroovyTestCase{

    def anySender, anySubject, anyBody

    void setUp(){
        anySender = MemberFixture.ottoOne
        anySubject = 'This is any subject'
        anyBody = 'Hello, this is any body'
    }


    void testCreateMessage_validData_messageReturned(){
        def message = FreeFormMessageFactory.createMessage(anySender, anySubject, anyBody)
        assertThat(message, is(notNullValue()))
        assertThat(message.payload.subject, is(anySubject))
        assertThat(message.payload.body, is(anyBody))
        assertThat(message.fromMember, is(anySender.name))
    }

}