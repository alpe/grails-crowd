package grailscrowd.core


import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import grailscrowd.core.message.SystemMessageType
/**
 * @author ap
 */
class GrailsProjectSystemMessageTest extends GroovyTestCase {

    def project
    def projectOwner
    def anyMember

    void setUp(){
        super.setUp()
            def memberFixi = new MemberDBFixture()
            anyMember = memberFixi.getAnyNewSubscribedMember()
            projectOwner = memberFixi.getAnyNewSubscribedMember()
            project = new GrailsProjectDBFixture().getAnyNewEnteredProject(projectOwner)
            assertThat(project, is(notNullValue()))

    }

    void tearDown(){
        // TODO: cleanup
    }

    void testInviteParticipant_systemMessageSent(){
        project.inviteParticipant(projectOwner, anyMember)
        assertThat(anyMember.mailbox.messages, is(notNullValue()))
        assertThat(anyMember.mailbox.messages.size(), is (1))
        def message = anyMember.mailbox.messages.iterator().next()
        assertThat(message.payload.type, is(SystemMessageType.PROJECT_INVITATION))
        def sentFolder = projectOwner.mailbox.sentboxMessages
        assertThat(sentFolder, is(notNullValue()))
        assertThat(sentFolder.size(), is(1))
        //TODO: thread->message: assertThat(sentFolder.iterator().next(), is(message))
    }

}
