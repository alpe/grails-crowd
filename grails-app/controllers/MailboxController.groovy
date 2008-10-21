import grailscrowd.core.*

import grailscrowd.core.message.*


/**
 * Mailbox and message controller.
 */
class MailboxController extends SecureController {

    def allowedMethods = [index: 'GET', showMessage: 'GET', archiveMessage: 'POST']

    def beforeInterceptor = [action: this.&auth]

    /** message infrastructure service */
    MessageService messageService

    /** start with inbox */
    def index = {
        redirect(action:'inbox')
    }

    /** Show incomming mails */
    def inbox= {
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def messages = mailbox.inboxMessages.collect(mapMessagesForInView)
        render(view: 'inbox', model: [mailbox: mailbox, messages:messages])
    }

    /** Show outgoing mails */
    def sentbox= {
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def messages = mailbox.sentboxMessages.collect(mapMessagesForSentView)
      render(view: 'sentbox', model: [mailbox: mailbox, messages:messages])
    }

    private def mapMessagesForInView ={
        def member = Member.findByName(it.fromMember)
        toModel(member, it) + [fromMember:it.fromMember]
    }
    private def mapMessagesForSentView = {
        def member = it.mailbox.member
        return toModel(member, it) +[toMember:member.name]
    }



    /** copy to model */
    private def toModel(sender, message){
        return [id:message.id, subject: message.subject,
                sentDate:message.sentDate,
                unread:message.isNew(),
                answered: message.isAnswered(),
                systemMessage: message.isSystemMessage(),
                payload:message.isSystemMessage()?message.payload:null,
                thread:message.thread.id,
                memberEmail:sender.email,
                memberDisplayName:sender.displayName]
    }


    /** Fill form command and render compose page */
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
        render (view:'compose', model:[formBean:cmd])
        return
/*
        println "redirecting"
        onUpdateAttempt("Free recipient selection not enabled, yet!", true)
        redirect(action:'inbox')
        return false
  */  }

    /** New mail input form */
    def compose = {
           def cmd = new FreeFormCommand(toMemberName:params.to)
           composeFreeForm(cmd)
    }

    /* Create persistent mail */
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

    def showConversation = {
        Long msgId = params.id.toLong()
        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def conversation = mailbox.getInboxConversationAndMarkMessageAsSeen(msgId)
        log.info "Converation messages found for id (${msgId}): "+conversation.messages.size()
        if (conversation) {
            render(view:'conversation', model:[topic:conversation.topic,
                    messages:conversation.messages.collect(mapMessagesForInView), focusMsgId:msgId])
            log.debug "Returning from showConversation"
            return;
        } else {
            redirect(controller:'inbox')
        }
    }

    /** Show single inbox message
     */
    def showInboxMessage = {
        long msgId = params.id.toLong()
//redirect failure:        redirect(uri: '/notAllowed')

        def mailbox = freshCurrentlyLoggedInMember().mailbox
		def msg = mailbox.getInboxMessageAndMarkAsSeen(msgId)
        if (msg) {
            render(view: 'message', model: [message: msg])
			return;
        } else {
            redirect(controller:'inbox')
        }
    }




    /** Show single sent message.
     */
    def showSentboxMessage = {
	    def msgId = params.id.toLong()
//			redirect(uri: '/notAllowed')
		def mailbox = freshCurrentlyLoggedInMember().mailbox
		def msg = mailbox.getSentboxMessage(msgId)
        if (msg) {
            render(view: 'message', model: [message: msg])
			return;
        } else {
            redirect(controller:'sentbox')
        }
    }

    @Deprecated
    def archiveMessage = {
        freshCurrentlyLoggedInMember().mailbox.markMessageAsArchived(params.id.toLong())
        redirect(controller:'mailbox')
    }

    def deleteInboxMessage = {
        def msgId = params.id.toLong()
        log.info "deleting message with id:$msgId"
        if (freshCurrentlyLoggedInMember().mailbox.deleteInboxMessage(msgId)){
            onUpdateAttempt("Message deleted.", false)
        }else{
            onUpdateAttempt("Failed to delete message.", true)
        }
        redirect(action:'inbox')
    }

}

/**
 * Formular validation command.
 */
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

    boolean isKnownMember(){
        return toMember!=null
    }

    static constraints = {
        // should match FreeFormMessagePayload#constraints
        subject(nullable: false, blank:false, maxSize: 100)
        body(nullable: false, blank: false, maxSize: 500)
        // member name is not restricted in size but 100 is a lot
        toMemberName(nullable:false, blank:false, maxSize: 100)
    }
    
}
