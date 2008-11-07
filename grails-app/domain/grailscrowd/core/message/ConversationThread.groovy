package grailscrowd.core.message

import java.sql.Timestamp
import grailscrowd.core.*

/**
 * @author ap
 */
class ConversationThread implements Comparable {

    private static final String RESPONSE_PREFIX = "RE"

    /** DB entry created automatically set  */
    Timestamp dateCreated
    /** DB entry last modified field, automatically set  */
    Timestamp lastUpdated

    String topic

    SortedSet messages

    SortedSet participators

    ThreadVisibility visibility

    static belongsTo = Mailbox
    static transients = ['subject']
    static hasMany = [messages: GenericMessage, participators: Member]


    static constraints = {
        topic(nullable: false, blank: false, maxSize: 100)
        visibility(nullable: false)
        participators(nullable:false, minSize:2)
        messages(nullable:false)
    }


    /**
     * Factory method to create new instances.
     */
    public static ConversationThread newInstance(String topic, Set members) {
        if (!members || members.size() < 2) {
            throw new IllegalArgumentException("Minimal 2 participators are required!")
        }
        if (!topic) {
            throw new IllegalArgumentException('Given topic must not be null or empty!')
        }
        def result = new ConversationThread(topic: topic,
                visibility: ThreadVisibility.PRIVATE)
        members.each {
            result.addToParticipators(it)
        }
        return result
    }

    /** does given member participate in this conversation  */
    def isParticipator(member) {
        return getParticipators().any {it.name == member.name}
    }

    /** Check visibility and participation status of member, fail when not allowed else execute closure.
     */
    def inThreadContext(member, closure) {
        switch (visibility) {
            case ThreadVisibility.PRIVATE:
                if (!isParticipator(member)) {
                    throw new IllegalArgumentException("Given member is not part of this private conversation!")
                }
                break
            case ThreadVisibility.MEMBERS:
                if (!member) {
                    throw new IllegalArgumentException("Current thread is only visible to members!")
                }
        }
        closure.call()
    }

    /**
     * Add message to current conversation thread
     */
    public void addNewMessage(sender, message) {
        inThreadContext(sender) {
            message.fromMember = sender.name
            addToMessages(message)
        }
    }

    /** contains one or more message for given member  */
    def containsMessageFor(recipient) {
        inThreadContext(recipient) {
            getMessages().any(messageForCondition.curry(recipient))
        }
    }

    /** member instance */
    def getMessagesFor(recipient) {
        inThreadContext(recipient) {
            getMessages().findAll(messageForCondition.curry(recipient))
        }
    }

    /** is recipient and not deleted */
    private def messageForCondition = {recipient, it->
        !it.isSender(recipient) && !it.isDeleted(recipient)
    }

    /** is sender and not deleted  */
    private def messageFromCondition = {sender, it->
        it.isSender(sender) && !it.isDeleted(sender)
    }



    /** contains one or more message from given member  */
    def containsMessageFrom(sender) {
        inThreadContext(sender) {
            getMessages().any(messageFromCondition.curry(sender))
        }
    }

    /** Get all messages sent by member.
     */
    def getMessagesFrom(sender) {
        inThreadContext(sender) {
            return getMessages().findAll(messageFromCondition.curry(sender))
        }
    }

    /** Get member instance of mail sender
     */
    protected def getSender(mail) {
        if (mail.fromMember) {
            return getParticipators().find {it.name == mail.fromMember}
        }
    }

    /** Are any new messages for given member in current conversation thread.
     */
    def hasAnyNewMessages(recipient) {
        getMessagesFor(recipient).any {it.isUnread(recipient)}
    }

    /** Get oldest of unread messages for given member.
     */
    def getFirstNewMessage(recipient) {
        getNewMessagesFor(recipient).min() // order by date desc
    }

    /** Get all unread messages for given member  */
    def getNewMessagesFor(recipient) {
        getMessagesFor(recipient).grep {it.isUnread(recipient)}
    }

    void markNewMessagesAsSeen(recipient) {
        getNewMessagesFor(recipient).each {it.markAsSeen(recipient)}
    }


    /** Get number of unread messages for given membem in this thread.
     */
    def getNumberOfNewMessagesFor(recipient) {
        getNewMessagesFor(recipient).size()
    }

    /** Get last inbox message in this thread for recipient  */
    def getLastMessageFor(recipient) {
        getMessagesFor(recipient).max()
    }

    def getHighlightInboxMessageFor(recipient) {
        def result = getFirstNewMessage(recipient)
        return result ?: getLastMessageFor(recipient)
    }

    /** last sent message of given member  */
    def getHighlightSentboxMessageFor(sender) {
        return getMessagesFrom(sender).max()
    }

    def markInboxMessagesAsDeleted(reader) {
        getMessagesFor(reader).each {it.markAsDeleted(reader)}
        return true
    }

    def markSentboxMessagesAsDeleted(reader) {
        getMessagesFrom(reader).each {it.markAsDeleted(reader)}
        return true
    }


    /**
     * Get message subject.  
     */
    String getSubject(message) {
        return (!isResponse(message) ? '' : RESPONSE_PREFIX + ': ') + topic
    }

    def getRecipients(message) {
        return getParticipators().grep {it.name != message.fromMember}
    }

    /** Is a response message to thread start message.  */
    boolean isResponse(message) {
        getMessages().any {it.fromMember != message.fromMember && it.sentDate < message.sentDate}
    }

    /** Is any message of another author after this in this conversation. */
    boolean hasResponse(message) {
        getMessages().any {it.fromMember != message.fromMember && it.sentDate > message.sentDate}
    }

    /**
     * Compare by dateCreated desc and topic when equals.
     */
    public int compareTo(other) {
        int result
        if (!this.dateCreated){
             result = !other.dateCreated?0:-1
        }else{
            result =  !other.dateCreated?1:other.dateCreated <=> this.dateCreated
        }
        // must match "consistent with equals" contract for TreeSet
        return result?:this.hashCode()<=>other.hashCode()

    }

    /**
     * Get single message, ignore delete status!
     */
    public def getMessageByIdFor(id, recipient) {
        inThreadContext(recipient) {
            getMessagesFor(recipient).find {it.id == id}
        }
    }

}

enum ThreadVisibility {
    PRIVATE, MEMBERS, ALL
}