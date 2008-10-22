package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat

/**
 * @author ap
 */
class FreeFormMessageFactoryTest extends AbstractBaseUnitTestCase{

    def anySender, anySubject, anyBody

    void setUp(){
        super.setUp()
        anySender = new MemberFixture().createTestData()
        anySubject = 'This is any subject'
        anyBody = 'Hello, this is any body'
    }


    void testCreateMessage_validData_messageReturned(){
        def message = FreeFormMessageFactory.createNewMessage(anySender, anyBody)
        MockUtils.mockDomain(message)
        assertThat(message, is(notNullValue()))
        assertThat(message.payload.body, is(anyBody))
        assertThat(message.fromMember, is(anySender.name))
    }

}