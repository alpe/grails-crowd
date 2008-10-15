package grailscrowd.core.message
/**
 * @author ap
 */
class ConversationThread {

    static belongsTo = GenericMessage


//    static hasMany = [messages: GenericMessage]

    boolean hasResponse(message){
        def orderedMessages = getOrderedMessageList()
        int pos = orderedMessages.indexOf(message)
        return pos!=-1 && pos+1<orderedMessages.size()
    }

    private List getOrderedMessageList(){
        def c = GenericMessage.createCriteria()
        return  c.list{
            and {
                eq('thread', this)
                gt('sentDate', new Date()-80)  // not older than 80 days
            }
            order("sentDate", "asc")
        }
    }
}