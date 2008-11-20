/**
 * @author ap
 */
class TransientMailerJob extends AbstractMailerJob {

    /** notification fifo       */
    def transientMessageNotificationQueue

    def transientMailerJobConfig

    /** job statistics       */
    def transientMailerJobStatistics

    /** internal name of job */
    def getName(){
        return mailboxMailerJobConfig.name
    }


    def getMailerJobStatistics() {
        return transientMailerJobStatistics
    }

    def getMailerJobConfig() {
        return transientMailerJobConfig
    }

    def getMessageFIFO() {
        return transientMessageNotificationQueue
    }

    /**
     * Create subject.
     * @return subject
     */
    String createSubject(recipient, message) {
        return message.subject
    }

    /**
     * Create text of mailbody.
     * @return text
     */
    String createBody(recipient, message) {
        StringBuilder sb = new StringBuilder()
        sb << message.body
        sb << defaultFooter()
        return sb.toString()
    }
}