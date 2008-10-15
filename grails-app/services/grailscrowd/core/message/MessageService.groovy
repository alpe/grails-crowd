package grailscrowd.core.message

import grailscrowd.core.Member


/**
 * Message sending service.
 *
 * @author ap
 */
class MessageService {

    boolean transactional = true

    /**
     * Send given message to list of Members as recipients.
     */
    public void submit(List<Member> recipients, GenericMessage msg){
        recipients.each{
           submit(it, msg)
        }
    }

    /**
     * Send given message to recipient.
     */
    public void submit(Member recipient, GenericMessage msg){
        log.debug "sending message to "+recipient.name+ ">"+msg.dump()
        recipient.mailbox.addToMessages(msg)
//        Thread.start{
            // TODO: push to message submit stack.
//        }
    }
}