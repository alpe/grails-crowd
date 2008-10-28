import grailscrowd.core.message.*


/**
 * Quartz job running to poll new messages from notification FIFO
 * and send as info via email like in grails.org.
 * @author ap
 */
abstract class AbstractMailerJob {

    /** blacklist impl    */
    def blacklist

    /** plugin mail service    */
    def mailService

    def mailCircuitBreaker

    abstract def getMailerJobStatistics()
    abstract def getMailerJobConfig()
    abstract def getMessageFIFO()
    
    abstract String createSubject(recipient, messag)
    abstract String createBody(recipient, messag)

    def getStartDelay() {
        return mailerJobConfig?.startDelay
    }

    def getTimeout() {
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
                while ((message = messageFIFO?.pollOffStack())) {
                    if (!message.isNew()) { return }
                    message.getRecipients().grep {
                        it.canBeNotifiedViaEmail && !isInBlacklist(it)
                    }.each {recipient ->
                        mailService?.sendMail {
                            title  createSubject(recipient, message)
                            from "noreply@grailscrowd.com"
                            replyTo "noreply@grailscrowd.com"
                            to recipient.email
                            text createBody(recipient, message)
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


}
