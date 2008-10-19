package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.util.*

/**
 *
 * @author ap
 */
class MailboxFixture extends AbstractDomainFixture{


    MemberFixture ownerFixture

    MessageFixture msgFixture


    MailboxFixture(){
        this(new MemberFixture())
    }
    MailboxFixture(MemberFixture ownerFixture){
        super()
        this.ownerFixture = ownerFixture
        this.msgFixture = new MessageFixture(ownerFixture)
    }

   /** {@inheritDoc} */
    @Override
    def createTestDataInstance() {
        return new Mailbox()
    }

    /** {@inheritDoc} */
    @Override
    void addRelationData(obj){
        def sampleData
        switch (fixtureType) {
            case MailboxFixtureType.EMPTY_BOX:
                sampleData = []
                break
            case MailboxFixtureType.SAMPLE_BOX:
                sampleData = getSampleBoxMessages()
                break
        }
        obj.member = ownerFixture.testData
        sampleData.each{
            obj.addToMessages(it)
        }
    }


   /** {@inheritDoc} */
    @Override
    void reset() {
        super.reset()
        fixtureType = MailboxFixtureType.EMPTY_BOX
    }


    /**
     * 2 unread
     * 3 read
     * 1 deleted
     *
     */
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


}
enum MailboxFixtureType{
    EMPTY_BOX, SAMPLE_BOX
}