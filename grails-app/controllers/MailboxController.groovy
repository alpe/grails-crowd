import grailscrowd.core.*

class MailboxController extends SecureController {

    def allowedMethods = [index: 'GET', showMessage: 'GET', archiveMessage: 'POST']

    def beforeInterceptor = [action: this.&auth]

    def index = {
        redirect(action:'inbox')
    }

    def inbox= {
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def messages = mailbox.inboxMessages.collect{
            def member = Member.findByName(it.fromMember)
            [id:it.id, subject: it.subject, fromMember:it.fromMember,
                    sentDate:it.sentDate,
                    unread:it.isNew(), member_email:member.email,
                    member_displayName:member.displayName]
        }
        render(view: 'mailbox', model: [mailbox: mailbox, messages:messages])
    }

    def sent= {
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        render(view: 'mailbox', model: [mailbox: mailbox, messages:mailbox.sentMessages])
    }

    def create = {
        
    }


    def showMessage = {
        def msgId = 0
		try {
			msgId = params.id.toLong()
		}
		catch(NumberFormatException) {
			redirect(uri: '/notAllowed')
		}
		
		def mailbox = freshCurrentlyLoggedInMember().mailbox
		def msg = mailbox.getMessage(msgId)
		if(!(msg?.isArchived() || msg?.isAcknowleged())) {
			if(msg.isNew()) {
				mailbox.markMessageAsSeen(msgId)
			}
			renderMessageViewOrGoToMailbox(msg)
		}
		else {
			renderMessageViewOrGoToMailbox(null)
		}
    }

    def archiveMessage = {
        freshCurrentlyLoggedInMember().mailbox.markMessageAsArchived(params.id.toLong())
        redirect(controller:'mailbox')
    }

    private renderMessageViewOrGoToMailbox(msg) {        
        if (msg) {
            render(view: 'message', model: [message: msg])
			return;
        }
        else {
            redirect(controller:'mailbox')
        }
    }
}
