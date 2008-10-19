package grailscrowd.core.message

import grailscrowd.core.Mailbox
/**
 * @author ap
 */
class ConversationThread {

    SortedSet messages

    static belongsTo = [messages: GenericMessage]


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
                gt('sentDate', new Date()-Mailbox.MAX_DAYS_VISIBILITY)  // not older than x days
            }
            order("sentDate", "asc")
        }

    }
}