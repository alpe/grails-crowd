import grailscrowd.core.*

import grailscrowd.core.message.*


/**
 * Mailbox and message controller.
 */
class MailboxController extends SecureController {

    def allowedMethods = [index: 'GET', showMessage: 'GET', archiveMessage: 'POST']

    def beforeInterceptor = [action: this.&auth]

    /** message infrastructure service  */
    MessageService messageService

    /** start with inbox  */
    def index = {
        redirect(action: 'inbox', params: [offset: params.offset, max: params.max])
    }

    /** Show incomming mails  */
    def inbox = {
        Mailbox.withTransaction {tx ->
            def member = freshCurrentlyLoggedInMember()
            def mailbox = member.mailbox
            def threads = mailbox.getInboxThreads(params.offset, params.max)
            threads = threads.collect {
                def msg = it.getHighlightInboxMessageFor(member)
                mapThreadForView(member, it) +
                        [highlightUnreadMessages: true] +
                        [highlightMessage: ContextAwareMessageAdapter.newInstance(member, msg)]
            }
            render(view: 'inbox', model: [mailbox: mailbox, threads: threads, total: mailbox?.totalInboxThreads])
        }
    }

    /** Show outgoing mails  */
    def sentbox = {
        Mailbox.withTransaction {tx ->
            println "offset: $params.offset"
            def member = freshCurrentlyLoggedInMember()
            def mailbox = member.mailbox
            def threads = mailbox.getSentboxThreads(params.offset, params.max)
            threads = threads.collect {
                def msg = it.getHighlightSentboxMessageFor(member)
                mapThreadForView(member, it) +
                        [highlightUnreadMessages: false] +
                        [highlightMessage: ContextAwareMessageAdapter.newInstance(member, msg)]
            }
            render(view: 'sentbox', model: [mailbox: mailbox, threads: threads, total: mailbox?.totalSentboxThreads])
        }
    }

    /** thread domain to data transfer object  */
    def mapThreadForView = {member, thread ->
        log.debug "Mapping thread $thread.id for view"
        return [id: thread.id, topic: thread.topic, participators: thread.participators.collect(mapMemberForView)]
    }

    def mapMemberForView = {member ->
        [name: member.name, displayName: member.displayName, email: member.email]
    }


    def showConversation = {
        Long threadId = params.id.toLong()
        renderConversation(threadId, params)
    }

    def renderConversation = {Long threadId, params ->
        def member = freshCurrentlyLoggedInMember()
        def mailbox = member.mailbox
        def thread = mailbox.getThreadById(threadId)
        log.debug "Looking for thread ${threadId}: " + thread.messages.size()
        if (thread) {
            render(view: 'conversation', model: [thread: mapThreadForView(member, thread) +
                    [messages: thread.messages.collect {
                        ContextAwareMessageAdapter.newInstance(member, it)
                    }.grep {it}.sort()
                    ],
                    offset: params.offset,
                    max: params.max
            ])
            mailbox.markThreadAsSeen(thread)
            return
        } else {
            redirect(action: 'inbox', params: [offset: params.offset, max: params.max])
        }

    }

    /** Fill form command and render compose page  */
    private def composeFreeForm = {cmd ->
        String name = cmd.toMemberName
        if (name) {
            def member = Member.findByName(name)
            if (member) {
                cmd.toMember = [name: name, email: member.email, displayName: member.displayName]
                render(view: 'compose', model: [formBean: cmd])
                return
            } else {
                onUpdateAttempt("Member not found.", true)
            }
        }
        render(view: 'compose', model: [formBean: cmd])
        return
    }

    /** New mail input form  */
    def compose = {
        def cmd = new FreeFormCommand(toMemberName: params.to)
        composeFreeForm(cmd)
    }

    /* Create persistent mail */
    def create = {FreeFormCommand cmd ->
        if (cmd.hasErrors()) {
            composeFreeForm(cmd)
            return
        }
        def currentMember = freshCurrentlyLoggedInMember()
        if (cmd.toMemberName == currentMember.name) {
            onUpdateAttempt("Soliloquies may be supported in a future version.", true)
            cmd.toMemberName = ""
            composeFreeForm(cmd)
            return
        }
        def recipient = Member.findByName(cmd.toMemberName)
        if (!recipient) {
            composeFreeForm(cmd)
            return
        }
        messageService.startNewFreeFormConversation(cmd.subject, currentMember, recipient, cmd.body)
        onUpdateAttempt("Your message has been sent.", true)
        redirect(action: 'inbox', params: [offset: params.offset, max: params.max])
    }


    /** Show single inbox message thread
     */
    def showInboxMessage = {
        params.each {println it}
//        long msgId = params.id.toLong()
//redirect failure:        redirect(uri: '/notAllowed')

        def mailbox = freshCurrentlyLoggedInMember().mailbox
        def msg = mailbox.getInboxMessageAndMarkAsSeen(msgId)
        if (msg) {
            render(view: 'message', model: [message: msg])
            return;
        } else {
            redirect(action: 'inbox', params: [offset: params.offset, max: params.max])
        }
    }

    def reply = {ReplyFormCommand cmd ->
        if (cmd.hasErrors()) {
            renderConversation(cmd.threadId, params)
            return
        }
        def message = FreeFormMessageFactory.createNewMessage(freshCurrentlyLoggedInMember(), cmd.body)
        messageService.respondToThread(cmd.threadId, freshCurrentlyLoggedInMember(), message)
        onUpdateAttempt("Your message has been sent.", true)
        redirect(action: 'inbox', params: [offset: 0, max: params.max])
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
            redirect(action: 'sentbox', params: [offset: params.offset, max: params.max])
        }
    }

    def deleteInboxMessage = {
        def threadId = params.id.toLong()
        log.info "Deleting inbox messages of thread id: $threadId"
        if (freshCurrentlyLoggedInMember().mailbox.deleteInboxThread(threadId)) {
            onUpdateAttempt("Message deleted.", true)
        } else {
            onUpdateAttempt("Failed to delete message.", false)
        }
        redirect(action: 'inbox', params: [offset: params.offset, max: params.max])
    }

    def deleteSentboxMessage = {
        def threadId = params.id.toLong()
        log.info "Deleting sentbox messages of thread id: $threadId"
        if (freshCurrentlyLoggedInMember().mailbox.deleteSentboxThread(threadId)) {
            onUpdateAttempt("Message deleted.", true)
        } else {
            onUpdateAttempt("Failed to delete message.", false)
        }
        redirect(action: 'sentbox')
    }

}

/**
 * Formular validation command.
 */
class FreeFormCommand {

    /** transient member data  */
    def toMember

    String subject
    String body
    String toMemberName

    FreeFormCommand() {
        this.subject = ''
        this.body = ''
        this.toMemberName = ''
    }

    boolean isKnownMember() {
        return toMember != null
    }

    static constraints = {
        // should match FreeFormMessagePayload#constraints
        subject(nullable: false, blank: false, maxSize: 100)
        body(nullable: false, blank: false, maxSize: 3500)
        // member name is not restricted in size but 100 is a lot
        toMemberName(nullable: false, blank: false, maxSize: 100)
    }
}

/**
 * Formular validation command.
 */
class ReplyFormCommand {
    Long threadId
    String body

    static constraints = {
        threadId(nullable: false)
        body(nullable: false, blank: false, maxSize: 3500)
    }
}
