package grailscrowd.core

import static org.hamcrest.CoreMatchers.*
import org.junit.Assert


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
            Assert.assertThat(project, is(notNullValue()))

    }

    void tearDown(){
        // TODO: cleanup
    }

    void testInviteParticipant_systemMessageSent(){
        project.inviteParticipant(projectOwner, anyMember)
        Assert.assertThat(anyMember.mailbox.messages, is(notNullValue()))
        Assert.assertThat(anyMember.mailbox.messages.size(), is (1))
        def message = anyMember.mailbox.messages.iterator().next()
        Assert.assertThat(message.payload.type, is(SystemMessageType.PROJECT_INVITATION))
        def sentFolder = projectOwner.mailbox.sentMessages
        Assert.assertThat(sentFolder, is(notNullValue()))
        Assert.assertThat(sentFolder.size(), is(1))
        Assert.assertThat(sentFolder.iterator().next(), is(message))
    }

}
