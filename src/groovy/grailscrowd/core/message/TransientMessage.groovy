package grailscrowd.core.message
/**
 * @author ap
 */
class TransientMessage {

    String subject
    List recipients
    String body


    def isSystemMessage() {
        return true
    }

    def isUnread(anyOne){
        return true
    }


}