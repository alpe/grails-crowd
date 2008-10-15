import grailscrowd.core.*

import grailscrowd.core.message.*


/**
 * Mailbox and message controller.
 */
class MailboxController extends SecureController {

    def allowedMethods = [index: 'GET', showMessage: 'GET', archiveMessage: 'POST']

    def beforeInterceptor = [action: this.&auth]


    MessageService messageService

    /** start with inbox */
    def index = {
        redirect(action:'inbox')
    }

    /** show incomming mails */
    def inbox= {
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def messages = mailbox.inboxMessages.collect{
            def member = Member.findByName(it.fromMember)
            toModel(member, it) + [fromMember:it.fromMember]
        }
        render(view: 'inbox', model: [mailbox: mailbox, messages:messages])
    }

    /** show outgoing mails */
    def sentbox= {
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def messages = mailbox.sentboxMessages.collect{
            def member = it.mailbox.member
            return toModel(member, it) +[toMember:member.name]
        }
      render(view: 'sentbox', model: [mailbox: mailbox, messages:messages])
    }

    private def toModel(member, message){
        return [id:message.id, subject: message.subject,
                sentDate:message.sentDate,
                unread:message.isNew(), answered: message.isAnswered(),
                thread:message.thread.id,
                memberEmail:member.email,
                memberDisplayName:member.displayName]
    }


    /** fill form command and render compose page */
    private def composeFreeForm = {cmd->
        String name = cmd.toMemberName
        if (name){
            def member = Member.findByName(name)
            if (member){
                cmd.toMember =  [name:name, email:member.email,displayName:member.displayName]
                render (view:'compose', model:[formBean:cmd])
                return
            }else{
                onUpdateAttempt("Member not found.", true)                
            }
        }
        println "redirecting"
        onUpdateAttempt("Free recipient selection not enabled, yet!", true)
        redirect(action:'inbox')
        return false
    }

    /** new mail input form */
    def compose = {
           def cmd = new FreeFormCommand(toMemberName:params.to)
           composeFreeForm(cmd)
    }

    /* create persistent mail */
    def create = {FreeFormCommand cmd->
        if (cmd.hasErrors()){
            composeFreeForm(cmd)
            return
        }
        log.info("persisting!"+params)
        def recipient =  Member.findByName(cmd.toMemberName)
        if (!recipient){
            composeFreeForm(cmd)
            return
        }
        def message = FreeFormMessageFactory.createNewMessage(freshCurrentlyLoggedInMember(), cmd.subject, cmd.body)
        messageService.submit (recipient, message)

        onUpdateAttempt("Your message has been sent.", true)
        redirect(action:'inbox')

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

class FreeFormCommand{

    /** transient member data */
    def toMember
    
    String subject
    String body
    String toMemberName
    
    FreeFormCommand(){
        this.subject = ''
        this.body = ''
        this.toMemberName = ''
    }

    static constraints = {
        // should match FreeFormMessagePayload#constraints
        subject(nullable: false, blank:false, maxSize: 100)
        body(nullable: false, blank: false, maxSize: 500)
        // member name is not restricted in size but 100 is a lot
        toMemberName(nullable:false, blank:false, maxSize: 100)
    }
    
}
