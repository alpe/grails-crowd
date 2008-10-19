package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

/**
 * @author ap
 */
class SystemMessageFactoryTest extends AbstractBaseUnitTestCase{

    def anySender
    def anyProject
    MemberFixture memberFixture
    GrailsProjectFixture projectFixture

    void setUp(){
        memberFixture = new MemberFixture()
        projectFixture = new GrailsProjectFixture()
        anySender = memberFixture.createTestData()
        anyProject = projectFixture.createTestData()
    }

    void addInvitationToMailbox(){
        def otherSender = memberFixture.createTestData() 
        def invitation = SystemMessageFactory.createInvitation(otherSender, anyProject)
        anySender.mailbox.addToMessages(invitation)
    }
    void addJoinRequestToMailbox(){

        def otherSender = memberFixture.createTestData()
        def invitation = SystemMessageFactory.createJoinRequest(otherSender, anyProject)
        anySender.mailbox.addToMessages(invitation)

    }

    void testCreateInvitation_withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createInvitation(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_INVITATION)
    }

    void testCreateRejectInvitation__withValidValues_returnMessageAsExpected(){
        addInvitationToMailbox()
        def msg = SystemMessageFactory.createRejectInvitation(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_INVITATION_REJECTION)
    }
    void testCreateAcceptInvitation__withValidValues_returnMessageAsExpected(){
        addInvitationToMailbox()
        def msg = SystemMessageFactory.createAcceptInvitation(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_INVITATION_ACCEPTANCE)
    }

    void testCreateJoinRequest__withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createJoinRequest(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_JOIN_REQUEST)
    }
    

    void testCreateApproveToJoinRequest__withValidValues_returnMessageAsExpected(){
        addJoinRequestToMailbox()
        def msg = SystemMessageFactory.createApproveToJoinRequest(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_REQUEST_APPROVAL)
    }
    void testCreateDisapprovalToJoinRequest__withValidValues_returnMessageAsExpected(){
        addJoinRequestToMailbox()
        def msg = SystemMessageFactory.createDisapprovalToJoinRequest(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_REQUEST_DISAPPROVAL)
    }


    private void assertMessageMachesExpectations(msg, messageType){
        assertThat(msg.fromMember, is(anySender.name))
        def payload = msg.payload
        assertThat(payload.type, is(messageType))
        assertThat(payload.projectName, is (anyProject.name))
        assertThat(payload.projectId, is(anyProject.id))

    }
}