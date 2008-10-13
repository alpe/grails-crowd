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
                    unread:it.isNew(), memberEmail:member.email,
                    memberDisplayName:member.displayName]
        }
        render(view: 'inbox', model: [mailbox: mailbox, messages:messages])
    }

    def sentbox= {
        // TODO: 
        def member = freshCurrentlyLoggedInMember()
        def mailbox = member.mailbox
        def messages = mailbox.sentboxMessages.collect{
            [id:it.id, subject: it.subject, fromMember:it.fromMember,
                    sentDate:it.sentDate,
                    unread:false, memberEmail:member.email,
                    memberDisplayName:member.displayName]
        }
      render(view: 'sentbox', model: [mailbox: mailbox, messages:messages])
    }

    def create = {
        
    }


    def showInboxMessage = {
        def msgId = 0
		try {
			msgId = params.id.toLong()
		}
		catch(NumberFormatException) {
			redirect(uri: '/notAllowed')
            return
        }
		def mailbox = freshCurrentlyLoggedInMember().mailbox
		def msg = mailbox.getInboxMessageAndMarkAsSeen(msgId)
        if (msg) {
            render(view: 'message', model: [message: msg])
			return;
        } else {
            redirect(controller:'inbox')
        }
    }

    def showSentboxMessage = {
        def msgId = 0
		try {
			msgId = params.id.toLong()
		}
		catch(NumberFormatException) {
			redirect(uri: '/notAllowed')
            return
        }
		def mailbox = freshCurrentlyLoggedInMember().mailbox
		def msg = mailbox.getSentboxMessage(msgId)
        if (msg) {
            render(view: 'message', model: [message: msg])
			return;
        } else {
            redirect(controller:'sentbox')
        }
    }

    def archiveMessage = {
        freshCurrentlyLoggedInMember().mailbox.markMessageAsArchived(params.id.toLong())
        redirect(controller:'mailbox')
    }

}
