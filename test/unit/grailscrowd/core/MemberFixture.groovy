package grailscrowd.core

import grailscrowd.util.MockUtils

/**
 * @author ap
 */
class MemberFixture {

    /**
     * Example user: Otto One, empty mailbox
     */
    static Member getOttoOne(){
        def result =  new Member(name: 'OttoOne', email: "ottoOne@example.com", password: "xxxxxx", displayName: "Otto One",
            about: "Example user 1 for testing purpose", mailbox: MailboxFixture.emptyBox)
        MockUtils.mockDomain(result)
        return result
    }

    /**
     * Example user: Dommy Duemplemeier, empty mailbox
     */
    static Member getDonnyDuempelmeier(){
        def result =  new Member(name: 'dommyD', email: "dd@example.com", password: "xxxxxx", displayName: "Donny Duempelmeier",
            about: "Example user 2 for testing purpose", mailbox: MailboxFixture.emptyBox)
        MockUtils.mockDomain(result)        
        return result
    }
}