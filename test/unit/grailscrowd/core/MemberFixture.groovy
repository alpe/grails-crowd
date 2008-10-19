package grailscrowd.core

import grailscrowd.util.AbstractDomainFixture

/**
 * @author ap
 */
class MemberFixture extends AbstractDomainFixture{

    MailboxFixture  mailboxFixture

    MemberFixture(){
        super()
        mailboxFixture = new MailboxFixture(this)
    }


    /** {@inheritDoc} */
    @Override
    def createTestDataInstance() {
        def sampleData
        switch (fixtureType) {
            case MemberFixtureType.OTTO_ONE:
                sampleData = getOttoOneSample()
                break
            case MemberFixtureType.DONNY_DUEMPLEMEIER:
                sampleData = getDonnyDuempelmeier()
                break
        }
        def result = new Member(sampleData)
        return result
    }

    /** {@inheritDoc} */
    @Override
    void reset() {
        this.fixtureType = MemberFixtureType.OTTO_ONE
        super.reset()        
    }

    /** {@inheritDoc} */
    @Override
    void addRelationData(obj){
        obj.mailbox = mailboxFixture.testData
    }

    /**
     * Example user: Otto One, empty mailbox
     */
    private static def getOttoOneSample() {
        [name: 'OttoOne', email: "ottoOne@example.com", password: "xxxxxx", displayName: "Otto One",
                about: "Example user 1 for testing purpose"]
    }

    /**
     * Example user: Dommy Duemplemeier, empty mailbox
     */
    private static def getDonnyDuempelmeier() {
        [name: 'dommyD', email: "dd@example.com", password: "xxxxxx", displayName: "Donny Duempelmeier",
                about: "Example user 2 for testing purpose"]
    }
}
enum MemberFixtureType {
    OTTO_ONE, DONNY_DUEMPLEMEIER
}