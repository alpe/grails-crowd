package grailscrowd.core

import grailscrowd.core.message.*
import grailscrowd.util.*

/**
 *
 * @author ap
 */
class MailboxFixture {

    static MessageFixture msgFixture = new MessageFixture()



    static Mailbox getEmptyBox(){
        Mailbox result = new Mailbox()        
        MockUtils.mockDomain(result)
        return result
    }


    /**
     * 2 unread
     * 3 read
     * 1 deleted
     *
     */
    static Mailbox getSampleBox(){
        def result = getEmptyBox()
        2.times{
            result.addToMessages(msgFixture.getAnyFreeFormMessage('bla$it'))
        }
        3.times{
            def msg = msgFixture.getAnyFreeFormMessage('bla$it')
            msg.status = MessageLifecycle.SEEN
            result.addToMessages(msg)
        }
        1.times{
            def msg = msgFixture.getAnyFreeFormMessage('bla$it')
            msg.status = MessageLifecycle.DELETED
            result.addToMessages(msg)
        }

        return result
    }

    static Mailbox getMailboxWithUnread(int quantity){
        def result = getEmptyBox()
//        quantity.times{
//            result.addToMessages(msgFixture.anyFreeFormMessage('bla$it'))
//        }
        return result
    }
}