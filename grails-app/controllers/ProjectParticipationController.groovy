import grailscrowd.core.*

import grailscrowd.core.message.GenericMessage

class ProjectParticipationController extends SecureController {


    def allowedMethods = [invite: 'POST', acceptParticipationInvitation: 'POST',
            rejectParticipationInvitation: 'POST', requestParticipation: 'POST',
            approveParticipationRequest: 'POST', rejectParticipationRequest: 'POST', index: 'POST']

    def beforeInterceptor = [action: this.&auth]

    def index = {}

    def invite = {
        def invitee = Member.get(params['invitee.id'])

        long projectId =  !params['project.id'] || !(params['project.id'].isLong())?-1L:Long.valueOf(params['project.id'])
        if (projectId<1) {
            redirect(controller: 'member', action: 'viewProfile', params: [_name: invitee.name, ])
            return
        }
        def project = GrailsProject.get(projectId)
        def projectCreator = freshCurrentlyLoggedInMember()
        project.inviteParticipant(projectCreator, invitee)
		flash.messageClass = 'info-box'
        flash.message = """Project participation invitation has been submitted.
                           A reply from the member you are inviting should appear in your inbox."""
        render(view: '/member/viewProfile', model: [member: invitee])
    }

    def acceptParticipationInvitation = {ProjectResponseForm cmd->
          handleForm(cmd){
            GrailsProject.get(cmd.projectId).acknowlegeParticipationAcceptance(freshCurrentlyLoggedInMember(), cmd.messageId)
            onUpdateAttempt("Your response is sent to the project owner.", true)
        }
    }

    def rejectParticipationInvitation = {ProjectResponseForm cmd->
          handleForm(cmd){
            GrailsProject.get(cmd.projectId).rejectParticipationInvitation(freshCurrentlyLoggedInMember(), cmd.messageId)
        }
    }

    def requestParticipation = {
        def requestor = Member.get(params['requestor.id'])
        if (!params['project.id']) {
            redirect(controller: 'member', action: 'viewProfile', params: [_name: requestor.name])
        }
        def project = GrailsProject.get(params['project.id'])
        project.requestParticipation(requestor)
        flash.messageClass = 'info-box'
        flash.message = """Project participation request has been submitted.
                           A reply from the project creator should appear in your inbox."""
        render(view: '/grailsProject/viewProject', model: [grailsProject: project])

    }

    /** handle project response form in no error context */
    private def handleForm(ProjectResponseForm cmd, c){
        if (!cmd.hasErrors()){
            try{
                c.call()
            }catch (Exception e){
                log.debug "Failed to handle submitted form.", e
                onUpdateAttempt("Failed to submit your respons, technical problems..", false)
            }
        }else{
            onUpdateAttempt("Failed to submit your respons, data incomplete.", false)
        }
        redirect(controller: 'mailbox')
    }

    def approveParticipationRequest = {ProjectResponseForm cmd->
        handleForm(cmd){
            def reader  = freshCurrentlyLoggedInMember()
            assert reader
            def msg = reader.mailbox.getInboxMessageById(cmd.messageId)
            assert msg
            def sender = msg.getSender()
            assert sender
            def p = GrailsProject.get(cmd.projectId)

            p.approveRequestedParticipation(reader, sender, msg)
            onUpdateAttempt("Your response is sent to ${sender.displayName}.", true)
        }
    }

    def rejectParticipationRequest = {ProjectResponseForm cmd->
        handleForm(cmd){
            def reader  = freshCurrentlyLoggedInMember()
            def msg = reader.mailbox.getInboxMessageById(cmd.messageId)
            def sender = msg.getSender()
            GrailsProject.get(cmd.projectId).rejectRequestedParticipation(reader, sender, msg)
            onUpdateAttempt("Your response is sent to ${sender.displayName}.", true)
        }

    }

}
/**
 * Input form used by system message form submition. 
 * @author ap
 */
class ProjectResponseForm{

    Long messageId, projectId

    static constraints = {
        messageId(nullable: false)
        projectId(nullable: false)
    }
}

