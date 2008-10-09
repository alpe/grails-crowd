package grailscrowd.core.message

import grailscrowd.core.*
import static org.hamcrest.CoreMatchers.*
import org.junit.Assert

/**
 * @author ap
 */
class SystemMessageFactoryTest extends GroovyTestCase{

    def anySender
    def anyProject

    void setUp(){
        anySender = MemberFixture.ottoOne
        anyProject = GrailsProjectFixture.grailscrowdSample
    }

    void testCreateInvitation_withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createInvitation(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_INVITATION)
    }

    void testCreateRejectInvitation__withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createRejectInvitation(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_INVITATION_REJECTION)
    }
    void testCreateAcceptInvitation__withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createAcceptInvitation(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_INVITATION_ACCEPTANCE)
    }

    void testCreateApproveToJoinRequest__withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createApproveToJoinRequest(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_REQUEST_APPROVAL)
    }
    void testCreateDisapprovalToJoinRequest__withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createDisapprovalToJoinRequest(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_REQUEST_DISAPPROVAL)
    }
    void testCreateJoinRequest__withValidValues_returnMessageAsExpected(){
        def msg = SystemMessageFactory.createJoinRequest(anySender, anyProject)
        assertMessageMachesExpectations(msg, SystemMessageType.PROJECT_JOIN_REQUEST)
    }






    private void assertMessageMachesExpectations(msg, messageType){
        Assert.assertThat(msg.fromMember, is(anySender.name))
        def payload = msg.payload
        Assert.assertThat(payload.type, is(messageType))
        Assert.assertThat(payload.projectName, is (anyProject.name))
        Assert.assertThat(payload.projectId, is(anyProject.id))

    }
}