import grailscrowd.core.message.*

/**
 * @author ap
 */
class MailboxMailerJob extends AbstractMailerJob {


    /** notification fifo      */
    def mailboxMessageNotificationFIFO

    def mailboxMailerJobConfig

    /** job statistics      */
    def mailboxMailerJobStatistics


    def getMailerJobStatistics() {
        return mailboxMailerJobStatistics
    }

    def getMailerJobConfig() {
        return mailboxMailerJobConfig
    }

    def getMessageFIFO() {
        return mailboxMessageNotificationFIFO
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
        sb << "\n"
        sb << "Regards,\n"
        sb << "Your GC Email Monkey\n"
        sb << "-- \n"
        sb << "You can change your mail and notification settings at:\n"
        sb << "http://www:grailscrowd.com/grailscrowd/account\n"
        return sb.toString()
    }

}