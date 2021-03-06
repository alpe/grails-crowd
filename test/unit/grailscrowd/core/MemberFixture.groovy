package grailscrowd.core

import grailscrowd.util.AbstractDomainFixture

/**
 * @author ap
 */
class MemberFixture extends AbstractDomainFixture{

    MailboxFixture  mailboxFixture
    String username = "anyUser"+System.currentTimeMillis()

    public MemberFixture(){
        this(new MailboxFixture())
        mailboxFixture.ownerFixture = this
    }
    public MemberFixture(MailboxFixture mailboxFixture){
        super()
        this.mailboxFixture = mailboxFixture
    }

    /** {@inheritDoc} */
    @Override
    def createTestDataInstance() {
        def sampleData
        switch (fixtureType) {
            case MemberFixtureType.AYNBODY:
                sampleData = getAnybodySample()
                break
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
        this.fixtureType = MemberFixtureType.AYNBODY
        super.reset()        
    }

    /** {@inheritDoc} */
    @Override
    void addRelationData(obj){
        obj.mailbox = mailboxFixture.testData
    }

    static MemberFixture getOttoFixture(){
        return new MemberFixture(fixtureType:MemberFixtureType.OTTO_ONE)
    }

    static MemberFixture getDonnyFixture(){
        return new MemberFixture(fixtureType:MemberFixtureType.DONNY_DUEMPLEMEIER)
    }
      /**
     * Example user: anybody
     */
    private def getAnybodySample() {
        [name:username, email: "${username}@example.com", password: "xxxxxx", displayName: "any body",
                about: "Example user x for testing purpose"]
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
    AYNBODY, OTTO_ONE, DONNY_DUEMPLEMEIER
}