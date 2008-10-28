package grailscrowd.core.message

import grailscrowd.core.*
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 * Factory to create system messages.
 * @author ap
 */
class SystemMessageFactory extends AbstractMessageFactory {



    /**
     * To support new and legacy message a version number is stored in payload.
     * @return latest version by type
     */
    static int getLatestMessageVersion(messageType) {
        return 1
    }

    //createNewThread(getSubject(messageType, grailsProject.name)

    public static String getSubject(GenericMessage msg, projectName) {
        return getSubject(msg.payload.type, projectName)
    }

    /**
     * Get message subject in default language: en.
     */
    public static String getSubject(SystemMessageType messageType, String projectName) {
        // always latest version
        switch (messageType) {
            case SystemMessageType.PROJECT_JOIN_REQUEST:
                return "Project '${projectName}' participation request"
            case SystemMessageType.PROJECT_INVITATION:
                return "Project Invitation to '${projectName}'"
            default:
                throw new AssertionError("Not supported messageType: " + messageType)
        }
    }



    /**
     * Create an invitation message.
     * @return message
     */
    public static def createInvitation(Member mailCreator, def grailsProject) {
        createSystemMail(SystemMessageType.PROJECT_INVITATION, mailCreator.name, grailsProject)
    }

    /**
     * Create a rejection of invitation  message.
     * @return message
     */
    public static def createRejectInvitation(Member mailCreator, def grailsProject) {
//        def thread = mailCreator.mailbox.getConverationThread(SystemMessageType.PROJECT_INVITATION, grailsProject)
        createSystemMail(SystemMessageType.PROJECT_INVITATION_REJECTION, mailCreator.name, grailsProject)
    }

    /**
     * Create an acceptance of invitation message.
     * @return message
     */
    public static def createAcceptInvitation(Member mailCreator, def grailsProject) {
        createSystemMail(SystemMessageType.PROJECT_INVITATION_ACCEPTANCE, mailCreator.name, grailsProject)
    }

    /**
     * Create a join request message.
     * @return message
     */
    public static def createJoinRequest(Member mailCreator, def grailsProject) {
        createSystemMail(SystemMessageType.PROJECT_JOIN_REQUEST, mailCreator.name, grailsProject)
    }

    /**
     * Create a approve to join request.
     * @return message
     */
    public static def createApproveToJoinRequest(Member mailCreator, def grailsProject) {
        createSystemMail(SystemMessageType.PROJECT_REQUEST_APPROVAL, mailCreator.name, grailsProject)
    }

    /**
     * Create a disapprove to join request.
     * @return message
     */
    public static def createDisapprovalToJoinRequest(Member mailCreator, def grailsProject) {
        createSystemMail(SystemMessageType.PROJECT_REQUEST_DISAPPROVAL, mailCreator.name, grailsProject,)
    }


    /**
     * Create mail with system payload for given type.
     */
    static def createSystemMail(SystemMessageType messageType, String senderInternalName, grailsProject) {
        def payload = new SystemMessagePayload(type: messageType,
                projectId: grailsProject.id,
                messageVersion: getLatestMessageVersion(messageType)
        )
        return createSimpleMail(senderInternalName, payload)
    }


    public static TransientMessage createProjectCommentNotificationMessage(recipients, grailsProject, Comment comment) {
        def appTagLib = new ApplicationTagLib()        
        String subject = "A new comment for project [${grailsProject.name}] has been posted"
        StringBuilder sb = new StringBuilder()
        sb << "${comment.member.displayName} said:\n"
        sb << "\n"
        sb << "${comment.body}\n"
        sb << "\n"
        sb << "See the comment in context: ${appTagLib.createLink(controller: 'grailsProject', action: 'viewProject', id: grailsProject.id, absolute: true)}\n"
        return new TransientMessage(recipients: recipients, subject: subject, body: sb.toString())
    }
}
