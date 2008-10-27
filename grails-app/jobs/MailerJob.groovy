import grailscrowd.core.message.*


/**
 * Quartz job running every minute to poll new messages from notification FIFO
 * and send as info via email like in grails.org.
 * @author ap
 */
class MailerJob {

    /** notification fifo    */
    MessageNotificationFIFO messageNotificationFIFO

    def mailerJobConfig

    /** blacklist impl    */
    def blacklist

    /** plugin mail service    */
    def mailService

    /** job statistics    */
    def mailerJobStatistics

    def mailCircuitBreaker

    def getStartDelay(){
        return mailerJobConfig?.startDelay
    }
    def getTimeout(){
        return mailerJobConfig?.timeout
    }

    /** Job jump in method.
     */
    def execute() {
        if (!mailerJobConfig?.enabled){ return }
        mailerJobStatistics?.markJobStarted()
        try {
            // execute in fail fast circuit breaker
            mailCircuitBreaker.execute {
                def message
                while ((message = messageNotificationFIFO?.pollOffStack())) {
                    if (!message.isNew()) { return }
                    message.getRecipients().grep {
                        it.canBeNotifiedViaEmail && !isInBlacklist(it)
                    }.each {recipient ->
                        mailService?.sendMail {
                            title "New message in your Grailscrowd mailbox."
                            from "noreply@grailscrowd.com"
                            replyTo "noreply@grailscrowd.com"
                            to recipient.email
                            text createNotificationMessage(recipient, message)
                        }
                    }
                }
            }
        } finally {
            mailerJobStatistics?.markJobEnded()
        }
    }

    /** Is given recipient in blacklist?   */
    public boolean isInBlacklist(recipient) {
        return blacklist.isInBlacklist(recipient.email)
    }

    /**
     * Create text of mailbody.
     * @return text
     */
    def createNotificationMessage = {recipient, message ->
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
