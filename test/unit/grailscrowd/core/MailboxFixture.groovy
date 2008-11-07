package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.util.*

/**
 *
 * @author ap
 */
class MailboxFixture extends AbstractDomainFixture{


    MemberFixture ownerFixture

    List threadFixtures

    public MailboxFixture(){
       this(null, [])
       this.ownerFixture = new MemberFixture(this)
    }
    
/*    public MailboxFixture(MemberFixture ownerFixture){
        this(ownerFixture, new ConversationThreadFixture(new MessageFixture(ownerFixture)))
    }

    public MailboxFixture(MemberFixture ownerFixture, ConversationThreadFixture threadFixture){
        this(ownerFixture, [threadFixture])
    }
  */
    public MailboxFixture(MemberFixture ownerFixture, List threadFixtures){
//        super()
        this.ownerFixture = ownerFixture
        this.threadFixtures = []
        this.threadFixtures.addAll(threadFixtures)

    }

   /** {@inheritDoc} */
    @Override
    def createTestDataInstance() {
        return new Mailbox()
    }

    /** {@inheritDoc} */
    @Override
    void addRelationData(obj){
/*        def sampleData
        switch (fixtureType) {
            case MailboxFixtureType.EMPTY_BOX:
                sampleData = []
                break
            case MailboxFixtureType.SAMPLE_BOX:
                sampleData = getSampleBoxMessages()
                break
        }      */
        assert obj
        obj.member = ownerFixture?.testData
        threadFixtures.each{
              obj.addToConversations(it.testData)
        }
        long newMessageCount = !obj.getConversations()?0:obj.getConversations().inject(0){count, thread->
                thread.getNumberOfNewMessagesFor(obj.member)
            }

        obj.metaClass.getNumberOfNewMessages = {-> newMessageCount}
    }


   /** {@inheritDoc} */
    @Override
    void reset() {
        super.reset()
        fixtureType = MailboxFixtureType.EMPTY_BOX
    }

   public static def setupSampleBoxFixture(){
       MailboxFixture result = new MailboxFixture()
       def ownerFixture = result.ownerFixture
       assert ownerFixture
       def anyMember1Fixture = new MemberFixture()
       def thread1Fixture = new ConversationThreadFixture(topic:"sample thread1", participationMemberFixtures:[ownerFixture,anyMember1Fixture])
       result.threadFixtures.add(thread1Fixture)
       def msg1Fixture = new MessageFixture(ownerFixture, thread1Fixture)
       assert msg1Fixture.createTestData()
       thread1Fixture.messageFixtures = [msg1Fixture]
       result.createTestData()
       return result
   }

    /**
     * 2 unread
     * 3 read
     * 1 deleted
     *

    private def getSampleBoxMessages(){
         def messages =[]
        2.times{
            messages << msgFixture.getAnyFreeFormMessage('bla$it')
        }
        3.times{
            def msg = msgFixture.getAnyFreeFormMessage('bla$it')
            msg.status = MessageLifecycle.SEEN
            messages << msg
        }
        1.times{
            def msg = msgFixture.getAnyFreeFormMessage('bla$it')
            msg.status = MessageLifecycle.DELETED
            messages << msg
        }

        return messages
    }
      */

}
enum MailboxFixtureType{
    EMPTY_BOX, SAMPLE_BOX
}