import grailscrowd.core.message.*

/**
 * @author ap
 */
class MailboxMailerJob extends AbstractMailerJob {


    /** notification fifo       */
    def mailboxMessageNotificationQueue

    def mailboxMailerJobConfig

    /** job statistics       */
    def mailboxMailerJobStatistics


    def getMailerJobStatistics() {
        return mailboxMailerJobStatistics
    }

    def getMailerJobConfig() {
        return mailboxMailerJobConfig
    }

    def getMessageFIFO() {
        return mailboxMessageNotificationQueue
    }

    /**
     * Create subject.
     * @return subject
     */
    String createSubject(recipient, message) {
        return "New message in your Grailscrowd mailbox."
    }

    /**
     * Create text of mailbody.
     * @return text
     */
    String createBody(recipient, message) {
        StringBuilder sb = new StringBuilder()
        sb << "Hi ${recipient.displayName},\n\n"
        sb << "you have recieved a new message from ${message.sender.displayName} with subject:\n"
        sb << "\n"
        sb << "'${message.subject}'\n"
        sb << "\n"
        sb << "Visit it at: http://www.grailscrowd.com/grailscrowd/mailbox/inbox\n"
        sb << defaultFooter()
        return sb.toString()
    }

}