
import grailscrowd.core.message.*


/**
 * Notification job like in grails.org.
 * @author ap
 */
class MailerJob {
    
    def startDelay = 6000
    def timeout = 60000     // execute job every minute

    MessageNotificationFIFO messageNotificationFIFO
//    def mailService
//    def cacheService
    
    def execute() {
        def message
        while((message = messageNotificationFIFO?.pollOffStack())) {
            message.getRecipients().grep{it.canBeNotifiedViaEmail}.each{recipient->
                println "Sending notification '${message.subject}' to: "+recipient
            }
        }
	}
}
