package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.*

/**
 * @author ap
 */
class MessageFixture extends AbstractDomainFixture{

    static final String ANY_FREEFORM_SUBJECT = 'Testmail'
    static final String ANYINVITATION_SUBJECT = 'Project Invitation'
//    static final String ANYINVITATION_ACCEPT_SUBJECT = 'Grails Crowd member has joined your project'
//    static final String ANYINVITATION_REJECTION_SUBJECT = 'Grails Crowd member has rejected your invitation'


    MemberFixture anySenderFixture
    GrailsProjectFixture anyProjectFixture
    ConversationThreadFixture conversationThreadFixture

    MessageFixture(){
        this(new MemberFixture())
    }
    MessageFixture(MemberFixture anySenderFixture){
        this(anySenderFixture, new ConversationThreadFixture())
        conversationThreadFixture.messageFixtures.add(this)
    }
    MessageFixture(MemberFixture anySenderFixture, ConversationThreadFixture conversationThreadFixture){        
        super()
        this.anySenderFixture = anySenderFixture
        anyProjectFixture = new GrailsProjectFixture(anySenderFixture)
        this.conversationThreadFixture = conversationThreadFixture
    }


    @Override
    def createTestDataInstance(){
        switch(fixtureType){
           case MessageFixtureType.FREEFORM:
               return createFreeFormMessage()
           case MessageFixtureType.INVITATION:
                return getAnyInviationMessage()
        }
    }

    @Override
    void addRelationData(obj){
        obj.thread = conversationThreadFixture.getTestData()
    }

    @Override
    void reset(){
        super.reset()
        fixtureType = MessageFixtureType.FREEFORM
    }

    /** short way to single freeform message */
     public def getAnyFreeFormMessage(def subject =ANY_FREEFORM_SUBJECT){
        fixtureType = MessageFixtureType.FREEFORM
        conversationThreadFixture.topic = subject
        conversationThreadFixture.createTestData()
        return this.createTestData()
     }


    /** fake message.property access  */
   private static def createFakeMessages(def messageResult){
        [getMessage:{a,b,c,d->messageResult}]
    }

   private GenericMessage getAnyInviationMessage(){
        return  doInContext{anySender, anyProject->
            def result = SystemMessageFactory.createInvitation(anySender, anyProject)

//            result.payload.messageSource = createFakeMessages(ANYINVITATION_SUBJECT)
            return result
        }
    }
/*
    static GenericMessage getAnyInviationAcceptMessage(){
        return  doInContext{anySender, anyProject->
            def result = SystemMessageFactory.createAcceptInvitation(anySender, anyProject)
            result.payload.messageSource = createFakeMessages(ANYINVITATION_ACCEPT_SUBJECT)
            return result
        }
    }
     static GenericMessage getAnyInviationRejectedlMessage(){
        return  doInContext{anySender, anyProject->
            def result = SystemMessageFactory.createRejectInvitation(anySender, anyProject)
            result.payload.messageSource = createFakeMessages(ANYINVITATION_REJECTION_SUBJECT)
            return result
        }
    }
  */

    /** system message context */
   private def doInContext(Closure c){
        def result = c.call(anySenderFixture.testData, anyProjectFixture.testData)
        return result
    }



   private GenericMessage createFreeFormMessage(def anyBody ="Hello World,\nthis is a test message.\n"){
       return new FreeFormMessageFactory().createNewMessage(anySenderFixture.testData, anyBody)
    }
}
enum MessageFixtureType{
    FREEFORM, INVITATION
}