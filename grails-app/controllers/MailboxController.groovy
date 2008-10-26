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
        log.debug "Loading inbox view for member: "+session.memberId
        def member = freshCurrentlyLoggedInMember()
        log.debug "Fresh member instance loaded"
        def mailbox = member.mailbox
        log.debug "Mailbox loaded: "+mailbox?.id
        def threads = mailbox.inboxThreads.collect{
            log.debug "Collecting unread messages for thread: "+it.id
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
                    [highlightMessage: mapMessageForView(msg, false, false)]
        }
        render(view: 'sentbox', model: [mailbox: mailbox, threads:threads])
    }

    /** thread domain to data transfer object */
    def mapThreadForView = {member, thread ->
        log.debug "Mapping thread $thread.id for view"
        return [id:thread.id, topic:thread.topic ]
    }

    def mapMessageForView = {msg, withPayload = false, displaySender=true->
        log.debug "Mapping message for view: "+msg?.id
        if (!msg){return null}
        def sender = displaySender? msg.getSender():msg.getRecipients().iterator().next()
        sender = [name:sender.name, displayName:sender.displayName, email:sender.email,]
        def message = [id:msg.id, subject:msg.subject, systemMessage:msg.isSystemMessage(), sentDate:msg.sentDate,
                hasReply:msg.isAnswered(),sender:sender, unread:msg.isNew()]
        if (withPayload){
            log.debug "Mapping payload for view"
             if (message.systemMessage){
                 def reader = freshCurrentlyLoggedInMember()
                 // do not show actions in sentbox && anything pending                               
                 boolean responseActionPending=  msg.getSender().name!=reader.name &&
                         msg.payload.isResponseActionPending(reader)
                message = message +[payload:[messageCode:msg.payload.messageCode, projectId:msg.payload.projectId,
                        responseActionPending:responseActionPending]]
             }else{
                 message = message +[payload:[body:msg.payload.body]]
             }
        }
        return message
    }


    def showConversation = {
        Long threadId = params.id.toLong()
        renderConversation(threadId)
    }

    def renderConversation ={Long threadId ->
        def member = freshCurrentlyLoggedInMember()
        def mailbox = member.mailbox
        def thread = mailbox.getTheadById(threadId)
        log.info "Looking for thread ${threadId}: "+thread.messages.size()
        if (thread) {
            render(view:'conversation', model:[thread:mapThreadForView(member, thread)+
                    [messages:thread.messages.collect{mapMessageForView(it, true)}]
            ])
            mailbox.markThreadAsSeen(thread)            
            return
        } else {
            redirect(controller:'inbox')
        }

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
        def currentMember = freshCurrentlyLoggedInMember()
        if (cmd.toMemberName==currentMember.name){
            onUpdateAttempt("Soliloquies may be supported in a future version.", true)
            cmd.toMemberName = ""
            composeFreeForm(cmd)
            return
        }
        def recipient =  Member.findByName(cmd.toMemberName)
        if (!recipient){
            composeFreeForm(cmd)
            return
        }
        messageService.startNewFreeFormConversation (cmd.subject, currentMember, recipient, cmd.body)
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

    def reply = { ReplyFormCommand cmd->
        if (cmd.hasErrors()){
            renderConversation(cmd.threadId)
            return 
        }
        def message = FreeFormMessageFactory.createNewMessage(freshCurrentlyLoggedInMember(), cmd.body)
        messageService.respondToThread (cmd.threadId, freshCurrentlyLoggedInMember(), message)
        onUpdateAttempt("Your message has been sent.", true)
        redirect(action:'inbox')
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
        def threadId = params.id.toLong()
        log.info "deleting message with id:$threadId"
        if (freshCurrentlyLoggedInMember().mailbox.deleteInboxThread(threadId)){
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
        body(nullable: false, blank: false, maxSize: 3500)
        // member name is not restricted in size but 100 is a lot
        toMemberName(nullable:false, blank:false, maxSize: 100)
    }
}

/**
 * Formular validation command.
 */
class ReplyFormCommand{
    Long threadId
    String body

    static constraints = {
        threadId(nullable: false)
        body(nullable: false, blank: false, maxSize: 3500)
    }
}