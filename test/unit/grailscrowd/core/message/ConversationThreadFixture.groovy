package grailscrowd.core.message

import grailscrowd.util.AbstractDomainFixture

/**
 * @author ap
 */
class ConversationThreadFixture extends AbstractDomainFixture{

    MessageFixture messageFixture

    ConversationThreadFixture(){
        this(new MessageFixture())
    }

    ConversationThreadFixture(messageFixture){
        this.messageFixture = messageFixture
    }

    @Override
    def createTestDataInstance(){
        return new ConversationThread()
    }

    @Override
    void addRelationData(obj){

    }

    @Override
    void reset(){
        super.reset()
        fixtureType = ConversationThreadFixtureType.NEW
    }
}

enum ConversationThreadFixtureType{
    NEW
}