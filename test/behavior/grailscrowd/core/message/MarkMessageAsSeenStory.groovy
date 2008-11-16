import grailscrowd.core.message.*
import grailscrowd.core.*


/*
 *
 */
scenario "Mark message as seen for a specific user." , {

    def fixture = new MemberDBFixture()
    GenericMessage message
    Member newReader
    Map otherReaderWithStatus = [:]

    given "a member as reader",{
        newReader = fixture.anyNewSubscribedMember
    }

    and "an unread message",{
        def messageFixture = new MessageDBFixture()
        def thread = messageFixture.addIncomingConversation(newReader.mailbox)
        message = thread.messages.iterator().next()
        Mailbox.withTransaction{tx->
            3.times{
                def anyOtherReader = fixture.anyNewSubscribedMember
                otherReaderWithStatus.put(anyOtherReader, message.isUnread(anyOtherReader))
            }
        }
    }
    when "mark as seen is called", {
        message.markAsSeen(newReader)
    }
    then "the message is not unread for this member anymore",{
          message.isUnread(newReader).shouldBe false
    }
    and "this has no influence on other members message status", {
        otherReaderWithStatus.each{oldReader, expectedStatus->
            message.isUnread(oldReader).shouldBe expectedStatus
        }
    }
    
}
