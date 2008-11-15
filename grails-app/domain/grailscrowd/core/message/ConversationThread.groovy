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

    Set participators

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

            case ThreadVisibility.NONE:
                throw new IllegalArgumentException("Current thread is not visible to anyone.")
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


    /** Find messages for given member. */
    public def getMessagesFor(recipient) {
        inThreadContext(recipient) {
            getMessages().findAll(messageForCondition.curry(recipient))
        }
    }

    /** Is recipient and not deleted. */
    private def messageForCondition = {recipient, it->
        !it.isSender(recipient) && !it.isDeleted(recipient)
    }

    /** Is sender and not deleted.  */
    private def messageFromCondition = {sender, it->
        it.isSender(sender) && !it.isDeleted(sender)
    }


    /** Get all messages sent by member.
     */
    public def getMessagesFrom(sender) {
        inThreadContext(sender) {
            return getMessages().findAll(messageFromCondition.curry(sender))
        }
    }

    /** Get member instance of mail sender.
     */
    protected def getSender(mail) {
        if (mail.fromMember) {
            return getParticipators().find {it.name == mail.fromMember}
        }
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


    /** Get latest inbox message in this thread for recipient  */
    private def getLastestMessageFor(recipient) {
        getMessagesFor(recipient).max()
    }

    def getHighlightInboxMessageFor(recipient) {
        def result = getFirstNewMessage(recipient)
        return result ?: getLastestMessageFor(recipient)
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
     * Compare by lastUpdated desc, topic asc
     */
    public int compareTo(other) {
        int result
        if (!this.lastUpdated){
             result = !other.lastUpdated?0:-1
        }else{
            result =  !other.lastUpdated?1:other.lastUpdated <=> this.lastUpdated
        }
        result = result?:this.topic<=>other.topic
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
    NONE, PRIVATE, MEMBERS, ALL
}
