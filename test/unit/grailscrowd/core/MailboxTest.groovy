package grailscrowd.core

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import grailscrowd.core.message.*
import grailscrowd.util.*

/**
 * @author ap
 */
class MailboxTest extends AbstractBaseUnitTestCase{

    Mailbox mailbox
    
    MailboxFixture fixi

    void setUp(){
        super.setUp()
        mailbox = null
        fixi = new MailboxFixture()
    }

    void testHasAnyNewMessages_3new_true(){
        mailbox = fixi.sampleBox
        assertThat(mailbox, is(notNullValue()))
        assertTrue mailbox.hasAnyNewMessages()
    }

    void testGetInboxMessageAndMarkAsSeen_validId_mesageNotNullAndStatusSeen(){
        mailbox = fixi.sampleBox
        def msg = mailbox.inboxMessages.find{it}
        msg.setId(1)
        def message = mailbox.getInboxMessageAndMarkAsSeen(1)
        assertThat(message, is(msg))
        assertThat(message.status, is(MessageLifecycle.SEEN))
    }

}