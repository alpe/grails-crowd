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
    }

    void tearDown(){
        // TODO: cleanup
    }

    void testInviteParticipant_systemMessageSent(){
        GrailsProject.withTransaction{tx->
            def memberFixi = new MemberDBFixture()            
            anyMember = memberFixi.getAnyNewSubscribedMember()
            projectOwner = memberFixi.getAnyNewSubscribedMember()
            project = new GrailsProjectDBFixture().getAnyNewEnteredProject(projectOwner)
            assertThat(project, is(notNullValue()))
            project.inviteParticipant(projectOwner, anyMember)
            project.save(flush:true)
        }
        assertThat(anyMember.mailbox.getNumberOfNewMessages(), is (1L))
        def inboxThreads = anyMember.mailbox.getInboxThreads(0, 10)
        assertThat(inboxThreads.size(), is(1))
        def message = inboxThreads.iterator().next().getHighlightInboxMessageFor(anyMember)
        assertThat(message.payload.type, is(SystemMessageType.PROJECT_INVITATION))
        def sentThreads = projectOwner.mailbox.getSentboxThreads(0, 10)
        assertThat(sentThreads, is(notNullValue()))
        assertThat(sentThreads.size(), is(1))
        message = inboxThreads.iterator().next().getMessagesFrom(projectOwner).iterator().next()
        assertThat(message.payload.type, is(SystemMessageType.PROJECT_INVITATION))
        //TODO: thread->message: assertThat(sentFolder.iterator().next(), is(message))
    }

}
