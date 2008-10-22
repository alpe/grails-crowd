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
		
		if(invitee.canBeNotifiedViaEmail) {
/*TODO:			try {
				sendMail {
		   			to invitee.email
		   			subject "[Grails Crowd] project participation invitation"
		   			body "Grails Crowd member '$projectCreator.displayName' wants you to join project '$project.name'\n\nGo to your mailbox to see more details: ${createLink(controller: 'mailbox', action: 'index', absolute: true)}"
				}
			}
			catch (Exception e) {
				log.debug("Exception is caught during send mail [${e.getMessage()}] Continueing...")
			}
				
*/		}

    }

    def acceptParticipationInvitation = {InvitationResponseForm cmd->
        if (!cmd.hasErrors()){
            GrailsProject.get(cmd.projectId).acknowlegeParticipationAcceptance(freshCurrentlyLoggedInMember(), cmd.messageId)
        }
  redirect(controller: 'mailbox')
    }

    def rejectParticipationInvitation = {InvitationResponseForm cmd->
        if (!cmd.hasErrors()){
            GrailsProject.get(cmd.projectId).rejectParticipationInvitation(freshCurrentlyLoggedInMember(), cmd.messageId)
        }
        redirect(controller: 'mailbox')
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
		
		if(project.creator.canBeNotifiedViaEmail) {
/*TODO:		try {
				sendMail {
					to project.creator.email
					subject "[Grails Crowd] project participation request"
					body "Grails Crowd member '$requestor.displayName' has requested to join project '$project.name' as a participant.\n\nGo to your mailbox to see more details: ${createLink(controller: 'mailbox', action: 'index', absolute: true)}"
				}
			}
			catch (Exception e) {
				log.debug("Exception is caught during send mail [${e.getMessage()}] Continueing...")
			}
*/		}

    }

    def approveParticipationRequest = {
        withProject {projectMap ->
            projectMap.project.approveRequestedParticipation(projectMap.creator,
                    projectMap.inviteeOrRequestor, params.messageId.toLong())
		
			if(projectMap.inviteeOrRequestor.canBeNotifiedViaEmail) {
/*TODO:				try {
					sendMail {
						to projectMap.inviteeOrRequestor.email
						subject "[Grails Crowd] project participation approval"
						body "Grails Crowd member '$projectMap.creator.displayName' has approved your request to join project '$projectMap.project.name' as a participant.\n\nGo to your mailbox to see more details: ${createLink(controller: 'mailbox', action: 'index', absolute: true)}"
					}
				}
				catch (Exception e) {
					log.debug("Exception is caught during send mail [${e.getMessage()}] Continueing...")
				}
*/			}
        }
    }

    def rejectParticipationRequest = {
        withProject {projectMap ->
            projectMap.project.rejectRequestedParticipation(projectMap.creator,
                    projectMap.inviteeOrRequestor, params.messageId.toLong())
			
			if(projectMap.inviteeOrRequestor.canBeNotifiedViaEmail) {
/*TODO:				try {
					sendMail {
						to projectMap.inviteeOrRequestor.email
						subject "[Grails Crowd] project participation disapproval"
						body "Grails Crowd member '$projectMap.creator.displayName' has rejected your request to join project '$projectMap.project.name' as a participant.\n\nGo to your mailbox to see more details: ${createLink(controller: 'mailbox', action: 'index', absolute: true)}"
					}
				}
				catch (Exception e) {
					log.debug("Exception is caught during send mail [${e.getMessage()}] Continueing...")
				}
*/			}
        }
    }

    private def withProject(callable) {
        GenericMessage.withTransaction{tx->
            def inviteeOrRequestor = Member.findByName(params.inviteeOrRequestor)
            def creator = Member.findByName(params.creator)
            def project = GrailsProject.get(params.projectId)
            callable([project: project, creator: creator, inviteeOrRequestor: inviteeOrRequestor])
        }
        redirect(controller: 'mailbox')
    }
}
/**
 * Form
 * @author ap
 */
class InvitationResponseForm{

    Long messageId, projectId

    static constraints = {
        messageId(nullable: false)
        projectId(nullable: false)
    }
}

