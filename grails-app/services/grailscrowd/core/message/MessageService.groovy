package grailscrowd.core.message

import grailscrowd.core.Member


/**
 * Message sending service.
 *
 * @author ap
 */
class MessageService {

    boolean transactional = true

    public void startNewSystemConversation(projectName, Member sender, Member recipient, GenericMessage msg){
        if(!msg.isSystemMessage()){
            throw new IllegalAccessException("Given message is not a system message!");
        }
        def topic = SystemMessageFactory.getSubject(msg, projectName)
        startNewConversation(topic, sender, recipient, msg)
    }

    public void startNewFreeFormConversation(String topic, Member sender, Member recipient, String body){
        def message = FreeFormMessageFactory.createNewMessage(sender, body)
        startNewConversation(topic, sender, recipient, message)
    }

    protected void startNewConversation(String topic, Member sender, Member recipient, GenericMessage msg){
        assert sender
        assert recipient
        assert sender !=recipient
        Set participators = [sender, recipient] as Set
        def thread = ConversationThread.newInstance(topic, participators)
        participators.each{it.mailbox.addToConversations(thread)}
        addMessage(thread, sender, msg)
    }

    public void respondToMessage(long messageId, Member sender, GenericMessage msg){
        responseTo(GenericMessage.get(messageId), sender, msg)
    }

    public void respondToMessage(GenericMessage message, Member sender, GenericMessage msg){
        responseTo(message.getThread(), sender, msg)
    }

    public void respondToThread(long threadId, Member sender, GenericMessage msg){
        responseTo(ConversationThread.get(threadId), sender, msg)
    }

    public void responseTo(ConversationThread thread, Member sender, GenericMessage msg){
        addMessage(thread, sender, msg)
    }

    private void addMessage(thread, sender, msg){
        msg.fromMember = sender.name
        thread.addNewMessage(sender, msg)
        // Todo: inform conversation members except sender
        // recipient....canBeNotifiedViaEmail
    }
  
}