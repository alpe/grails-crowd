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
        def member = freshCurrentlyLoggedInMember()
        def mailbox = member.mailbox
        def threads = mailbox.inboxThreads .collect{
            def msg = it.getHighlightInboxMessageFor(member)
            mapThreadForView(member, it) +
                    [highlightUnreadMessages:true]+
                    [highlightMessage: mapMessageForView(msg)]
        }
        render(view: 'inbox', model: [mailbox: mailbox, threads:threads])
    }

    /** Show outgoing mails */
    def sentbox= {
        def member = freshCurrentlyLoggedInMember()
        def mailbox = member.mailbox
        def threads = mailbox.sentboxThreads .collect{

            def msg = it.getHighlightSentboxMessageFor(member)
            mapThreadForView(member, it) +
                    [highlightUnreadMessages:false] +
                    [highlightMessage: mapMessageForView(msg)]
        }
        render(view: 'sentbox', model: [mailbox: mailbox, threads:threads])
    }

    /** thread domain to data transfer object */
    def mapThreadForView = {member, thread ->
        return [id:thread.id, topic:thread.topic ]
    }

    def mapMessageForView = {msg, withPayload = false->
        if (!msg){return null}
        def sender = msg.getSender()
        sender = [name:sender.name, displayName:sender.displayName, email:sender.email,]
        def message = [id:msg.id, subject:msg.subject, systemMessage:msg.isSystemMessage(), sentDate:msg.sentDate,
                hasReply:msg.isAnswered(),sender:sender, unread:msg.isNew()]
        if (withPayload && message.systemMessage){
            message = message +[payload:[messageCode:msg.payload.messageCode, projectId:msg.payload.projectId]] 
        }
        return message
    }


    def showConversation = {
        Long threadId = params.id.toLong()
        def member = freshCurrentlyLoggedInMember()
        def mailbox = member.mailbox
        def thread = mailbox.getTheadByIdAndMarkAllAsSeen(threadId)
        log.info "Looking for thread ${threadId}: "+thread.messages.size()
        if (thread) {
            render(view:'conversation', model:[thread:mapThreadForView(member, thread)+
                    [messages:thread.messages.collect{mapMessageForView(it, true)}]
            ])
            log.debug "Returning from showConversation"
            return;
        } else {
            redirect(controller:'inbox')
        }
    }


  /*


    private def mapMessagesForInView ={
        def member = Member.findByName(it.fromMember)
        toModel(member, it) + [fromMember:it.fromMember]
    }
    private def mapMessagesForSentView = {
        def member = it.mailbox.member
        return toModel(member, it) +[toMember:member.name]
    }
*/


    /** copy to model
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
*/

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
 }

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
        def message = FreeFormMessageFactory.createNewMessage(freshCurrentlyLoggedInMember(), cmd.body)
        messageService.startNewConversation (cmd.subject, freshCurrentlyLoggedInMember(), recipient, message)
        onUpdateAttempt("Your message has been sent.", true)
        redirect(action:'inbox')

    }


    /** Show single inbox message
     */
    def showInboxMessage = {
        params.each{println it}
//        long msgId = params.id.toLong()
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
