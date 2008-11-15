import grailscrowd.core.message.*
import grailscrowd.core.*
import grailscrowd.util.*
/*
 *
 */
scenario "Mark message as deleted for a specific user." , {

    GenericMessage message
    Member newReader
    Map otherReaderWithStatus = [:]

    given "a message with multiple readers",{
        message= new MessageFixture().getTestData()
        3.times{
            def anyOtherReader = new MemberFixture().createTestData()
            otherReaderWithStatus.put(anyOtherReader, message.isDeleted(anyOtherReader))
        }
    }
    and "a member as new reader",{
        newReader = new MemberFixture().getTestData()
    }
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
// do cleanup after scenario is run
MockUtils.cleanup()