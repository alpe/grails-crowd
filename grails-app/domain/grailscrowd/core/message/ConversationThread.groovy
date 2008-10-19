package grailscrowd.core.message

import grailscrowd.core.Mailbox
/**
 * @author ap
 */
class ConversationThread {

    private static final String RESPONSE_PREFIX = "RE"

    static belongsTo = [messages: GenericMessage]
    static transients = ['subject']

    String topic

    static constraints = {
        topic(nullable:false, blank:false, maxSize:100)
    }



    /**
     * Get message subject.  
     */
    String getSubject(message){
        return (!isResponse(message)?'':RESPONSE_PREFIX+': ')+topic
        
//        if(!previousMessages.empty){
//            return RESPONSE_PREFIX+ /* system message response would be: previousMessages.last().getSubject() */
//        }
//        return topic
    }

    boolean isResponse(message){
        def orderedMessages = getOrderedMessageList(this)
        int pos = orderedMessages.indexOf(message)
        return pos>0        
    }

    boolean hasResponse(message){
        def orderedMessages = getOrderedMessageList(this)
        int pos = orderedMessages.indexOf(message)
        return pos!=-1 && pos+1<orderedMessages.size()
    }

    private List getOrderedMessageList(ConversationThread thread){
        def c = GenericMessage.createCriteria()
        return  c.list{
            and {
                eq('thread', thread)
          //      gt('sentDate', new Date()-Mailbox.MAX_DAYS_VISIBILITY)  // not older than x days
            }
            order("sentDate", "asc")
        }

    }
}