package grailscrowd.core.message

import grailscrowd.util.AbstractDomainFixture

/**
 * @author ap
 */
class ConversationThreadFixture extends AbstractDomainFixture{

    static final String ANY_TOPIC ="anyTopic..blabla"

    MessageFixture messageFixture

    String topic

    ConversationThreadFixture(){
        this(new MessageFixture())
    }

    ConversationThreadFixture(messageFixture){
        super()
        this.messageFixture = messageFixture
    }

    @Override
    def createTestDataInstance(){
        return new ConversationThread(topic:topic)
    }

    @Override
    void addRelationData(obj){

    }

    @Override
    void reset(){
        super.reset()
        fixtureType = ConversationThreadFixtureType.NEW
        topic = ANY_TOPIC
    }
}

enum ConversationThreadFixtureType{
    NEW
}