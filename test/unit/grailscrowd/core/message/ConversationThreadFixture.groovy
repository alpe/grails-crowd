package grailscrowd.core.message

import grailscrowd.util.AbstractDomainFixture

/**
 * @author ap
 */
class ConversationThreadFixture extends AbstractDomainFixture {

    static final String ANY_TOPIC = "anyTopic..blabla"

    List messageFixtures
    List participationMemberFixtures

    String topic

    ConversationThreadFixture() {
        this([])
    }
    ConversationThreadFixture(MessageFixture messageFixture) {
        this([messageFixture])
    }


    ConversationThreadFixture(List messageFixtures) {
        super()
        this.messageFixtures = []
        this.messageFixtures.addAll(messageFixtures)
        this.participationMemberFixtures =[]
    }

    @Override
    def createTestDataInstance() {
        return new ConversationThread(topic:topic, visibility:ThreadVisibility.PRIVATE)
    }

    @Override
    void addRelationData(obj) {
        participationMemberFixtures.collect{it.testData}.each{
            obj.addToParticipators(it)
        }
        messageFixtures.each {
            obj.addNewMessage(it.anySenderFixture.testData, it.testData)
        }

    }

    @Override
    void reset() {
        super.reset()
        fixtureType = ConversationThreadFixtureType.NEW
        topic = ANY_TOPIC
        messageFixtures?.clear()
        participationMemberFixtures?.clear()
    }
}

enum ConversationThreadFixtureType {
    NEW
}