import grailscrowd.core.message.*
import grailscrowd.core.*
import grailscrowd.util.*

/**
 *
 */

def mailbox
def reader


before " setup valid mailbox definition", {
//     mailbox =  Mailbox.get(4)
//     reader = mailbox.member
}

it "should count all unread messages of all inbox threads", {
//    mailbox.getNumberOfNewMessages().shouldBe 1
}
it "should increase count when a new message on any inobx thread is recieved",{

}
it "should add message quantity to count when a new conversation is added to inbox",{

}

it "should decrease count when an inbox message is read",{

}
it "should decrease count when an inbox message is deleted", {

}

it "should substract unread thread messages of count when an inbox thread is deleted", {

}

it "should not change count on sentbox read/delete actions",{
    
}

