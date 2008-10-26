
import grailscrowd.core.message.*


/**
 * Quartz job running every minute to poll new messages from notification FIFO
 * and send as info via email like in grails.org.
 * @author ap
 */
class MailerJob {

    /**
     * To prevent flooding test accounts, any outgoing mailaddress must not
     * be in this regExp blacklist.
     */
    static final List BLACKLIST =[ ".*@example.com", "gash@gmx.de"]    


    def startDelay = 6000
    def timeout = 60000     // execute job every minute

    MessageNotificationFIFO messageNotificationFIFO

    def mailService


    def execute() {
        def message
        while((message = messageNotificationFIFO?.pollOffStack())) {
            if (!message.isNew()){ return}
            message.getRecipients().grep{
                it.canBeNotifiedViaEmail && !isInBlacklist(it)
            }.each{recipient->
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

    /** Is given recipient in blacklist?*/
    public boolean isInBlacklist(recipient){
        return BLACKLIST.any{recipient.email ==~it}
    }

    /**
     * Create text of mailbody.
     * @return text
     */
    def createNotificationMessage = {recipient, message->
        StringBuilder sb = new StringBuilder()
        sb<<"Hi ${recipient.displayName},\n\n"
        sb<<"you have recieved a new message from ${message.sender.displayName} with subject:\n"
        sb<<"\n"        
        sb<<"'${message.subject}'\n"
        sb<<"\n"
        sb<<"Visit it at: http://www.grailscrowd.com/grailscrowd/mailbox/inbox\n"
        sb<<"\n"
        sb<<"Regards,\n"
        sb<<"Your GC Email Monkey\n"
        sb<<"-- \n"
        sb<<"You can change your mail and notification settings at:\n"
        sb<<"http://www:grailscrowd.com/grailscrowd/account\n"
        return sb.toString()
    }

}
