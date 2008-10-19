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
        fixi.fixtureType = MailboxFixtureType.SAMPLE_BOX
        mailbox = fixi.createTestData()
    }


    void testHasAnyNewMessages_3new_true(){
        assertThat(mailbox, is(notNullValue()))
        assertTrue mailbox.hasAnyNewMessages()
    }

    void testGetInboxMessageAndMarkAsSeen_validId_mesageNotNullAndStatusSeen(){
        def msg = mailbox.inboxMessages.find{it}
        msg.id = 123123L
        def message = mailbox.getInboxMessageAndMarkAsSeen(msg.id)
        assertThat(message, is(msg))
        assertThat(message.status, is(MessageLifecycle.SEEN))
    }


    void testVisibleInInbox_validSample_true(){
        def msg =[isDeleted:{false}, sentDate: new Date()]
        assertTrue(mailbox.visibleInInbox.call(msg))
        Calendar cal = GregorianCalendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, (Mailbox.MAX_DAYS_VISIBILITY)*-1)
        cal.add(Calendar.SECOND, 1)
        msg =[isDeleted:{false}, sentDate: cal.time]
        assertThat(mailbox.visibleInInbox.call(msg), is(true))
    }

    void testVisibleInInbox_deletedStatus_false(){
        def msg =[isDeleted:{true}, sentDate: new Date()]
        assertThat(mailbox.visibleInInbox.call(msg), is(false))
    }
    void testVisibleInInbox_expiredSentDate_false(){
        def msg =[isDeleted:{false}, sentDate: new Date()-Mailbox.MAX_DAYS_VISIBILITY]
        assertThat(mailbox.visibleInInbox.call(msg), is(false))
    }


}