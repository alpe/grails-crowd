import grailscrowd.core.message.*
import grailscrowd.core.*
import grailscrowd.util.*
/*
 *
 */
scenario "Mark message as seen for a specific user." , {

    GenericMessage message
    Member newReader
    Map otherReaderWithStatus = [:]

    given "a message with multiple readers",{
        message= new MessageFixture().getTestData()
        3.times{
            def anyOtherReader = new MemberFixture().createTestData()
            otherReaderWithStatus.put(anyOtherReader, message.isUnread(anyOtherReader))
        }
    }
    and "a member as new reader",{
        newReader = new MemberFixture().getTestData()
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
// do cleanup after scenario is run
MockUtils.cleanup()