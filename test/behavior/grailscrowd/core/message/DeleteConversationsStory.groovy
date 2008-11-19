
import grailscrowd.core.message.*
import grailscrowd.core.*


/*
 */
scenario "delete an inbox conversation thread" , {

    def mailbox
    def threadToDel

    given "a mailbox with conversations",{
        mailbox = new MemberDBFixture().anyNewSubscribedMember.mailbox
    }
    and "an inbox conversation to delete", {
        threadToDel = new MessageDBFixture().addIncomingConversation(mailbox)
        assert threadToDel
        assert threadToDel.id 
    }
    when "delete on this conversation is called by a reader", {
        mailbox.deleteInboxThread(threadToDel.id)

    }
    then "all incoming messages of this conversation are marked as deleted",{
        threadToDel.getMessagesFor(mailbox.member).each{msg->
            msg.isDeleted(mailbox.member).shouldBe true
        }
    }
    and "not visible to reader anymore", {
        ConversationThread.get(threadToDel.id).getMessagesFor(mailbox.member).size().shouldBe 0
    }
    and "the deleted conversation thread will not be found in inbox",{
        mailbox.getInboxThreads(0,9999).each{thread->
            thread.id.shouldNotEqual threadToDel.id
        }

    }

}
/*
 */
scenario "delete an sentbox conversation thread" , {

    def mailbox
    def threadToDel

    given "a mailbox with conversations",{
        mailbox = new MemberDBFixture().anyNewSubscribedMember.mailbox
    }
    and "a sentbox conversation to delete", {
       threadToDel = new MessageDBFixture().addOutgoingConversation(mailbox)
    }
    when "delete on this conversation is called by a reader", {
        mailbox.deleteSentboxThread(threadToDel.id)

    }
    then "all sent messages of this conversation are marked as deleted",{
        threadToDel.getMessagesFrom(mailbox.member).each{msg->
            msg.isDeleted(mailbox.member).shouldBe true
        }
    }
    and "not visible to reader anymore", {
        ConversationThread.get(threadToDel.id).getMessagesFrom(mailbox.member).size().shouldBe 0
    }
    and "the deleted conversation thread will not be found in sebox",{
        mailbox.getSentboxThreads(0,9999).each{thread->
            thread.id.shouldNotEqual threadToDel.id
        }

    }

}