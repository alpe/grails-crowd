package grailscrowd.core.message

import grailscrowd.core.Member

/**
 * Factory to create system messages.
 * @author ap
 */
class SystemMessageFactory {


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
        creaateSystemMail(SystemMessageType.PROJECT_REQUEST_DISAPPROVAL, mailCreator.name, grailsProject)
    }


    /**
     * Create mail with system payload for given type.
     */
    static def createSystemMail(SystemMessageType messageType, String senderInternalName, grailsProject) {
        if (!senderInternalName){
            throw new IllegalArgumentException("Given sender name must not be null or empty!")
        }
        def payload = new SystemMessagePayload(type: messageType,
                projectId: grailsProject.id,
                projectName: grailsProject.name
        )
        return createSimpleMail(senderInternalName, payload)
    }

    /**
     * Add mail body to given payload
     */
    static def createSimpleMail(String senderInternalName, payload) {
        if (!payload){
            throw new IllegalArgumentException('Given payload must not be null!')
        }
        return new GenericMessage(fromMember: senderInternalName, payload: payload)
    }

}