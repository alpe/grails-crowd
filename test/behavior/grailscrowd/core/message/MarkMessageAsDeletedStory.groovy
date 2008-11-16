import grailscrowd.core.message.*
import grailscrowd.core.*

/*
 *
 */
scenario "Mark message as deleted for a specific user." , {

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
/*        3.times{
            def anyOtherReader = fixture.anyNewSubscribedMember
            otherReaderWithStatus.put(anyOtherReader, message.isUnread(anyOtherReader))
        }
  */  }

    when "mark as deleted is called", {
        message.markAsDeleted(newReader)
    }
    then "the message is not unread for this member anymore",{
          message.isDeleted(newReader).shouldBe true
    }
    and "this has no influence on other members message status", {
        otherReaderWithStatus.each{oldReader, expectedStatus->
            message.isDeleted(oldReader).shouldBe expectedStatus
        }
    }

}
