import grailscrowd.core.message.*
import grailscrowd.core.*

/**
 *
 */

def mailbox
def reader
def messageFixture

before "setup valid mailbox definition", {
    mailbox = new MemberDBFixture().anyNewSubscribedMember.mailbox
    reader = mailbox.member
    messageFixture = new MessageDBFixture()
    messageFixture.addIncomingConversation(mailbox)
    messageFixture.addResponseMessageToThread(firstMessage(mailbox), 1)
    assert mailbox.getNumberOfNewMessages() ==2
}

it "should count all unread messages of all inbox threads", {
    messageFixture.addIncomingConversation(mailbox)
    mailbox.getNumberOfNewMessages().shouldBe 3
}
it "should increase count when a new message on any inobx thread is recieved",{
    messageFixture.addResponseMessageToThread(firstMessage(mailbox), 1)
    mailbox.getNumberOfNewMessages().shouldBe 3
}
it "should add message quantity to count when a new conversation is added to inbox",{
    messageFixture.addIncomingConversation(mailbox, 5)
    mailbox.getNumberOfNewMessages().shouldBe 7
}
it "should decrease count when an inbox thread is read",{
    def thread = mailbox.getInboxThreads(0,1).iterator().next()
    mailbox.markThreadAsSeen(thread)
    mailbox.getNumberOfNewMessages().shouldBe 0
}

it "should decrease count when an inbox message is read",{
    Mailbox.withTransaction{tx->
        firstMessage(mailbox).markAsSeen(reader)
    }
    mailbox.getNumberOfNewMessages().shouldBe 1
}
it "should decrease count when an inbox message is deleted", {
    Mailbox.withTransaction{tx->
        firstMessage(mailbox).markAsDeleted(reader)
    }
    mailbox.getNumberOfNewMessages().shouldBe 1
}

it "should substract unread thread messages of count when an inbox thread is deleted", {
    mailbox.deleteInboxThread(mailbox.getInboxThreads(0,1).iterator().next().id)
    mailbox.getNumberOfNewMessages().shouldBe 0
}

it "should not change count on sentbox read/delete actions",{
    Mailbox.withTransaction{tx->
        // create new thread
        def thread = messageFixture.addOutgoingConversation(mailbox)
        mailbox.getNumberOfNewMessages().shouldBe 2
        // mark as seen
        def msg = mailbox.getSentboxThreads(0,1).iterator().next().messages.iterator().next()
        msg.markAsSeen(reader)
        mailbox.getNumberOfNewMessages().shouldBe 2
        // delete thread
        mailbox.deleteSentboxThread(msg.thread.id)
        mailbox.getNumberOfNewMessages().shouldBe 2
    }
}

/** helper method */
def firstMessage(mailbox){
    return mailbox.getInboxThreads(0,1).iterator().next().messages.iterator().next()
}


