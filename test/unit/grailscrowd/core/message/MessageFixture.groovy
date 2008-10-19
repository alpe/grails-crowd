package grailscrowd.core.message

import grailscrowd.core.*
import grailscrowd.util.*

/**
 * @author ap
 */
class MessageFixture extends AbstractDomainFixture{

    static final String ANY_FREEFORM_SUBJECT = 'Testmail'
    static final String ANYINVITATION_SUBJECT = 'Test: Project Invitation'
    static final String ANYINVITATION_ACCEPT_SUBJECT = 'Test: Grails Crowd member has joined your project'
    static final String ANYINVITATION_REJECTION_SUBJECT = 'Test: Grails Crowd member has rejected your invitation'


    MemberFixture anySenderFixture
    GrailsProjectFixture anyProjectFixture
    ConversationThreadFixture conversationThreadFixture

    String freeFormMailSubject

    MessageFixture(){
        this(new MemberFixture())
    }
    MessageFixture(anySenderFixture){
        super()
        this.anySenderFixture = anySenderFixture
        anyProjectFixture = new GrailsProjectFixture(anySenderFixture)
        conversationThreadFixture = new ConversationThreadFixture(this)
    }


    @Override
    def createTestDataInstance(){
        switch(fixtureType){
           case MessageFixtureType.FREEFORM:
               return createFreeFormMessage(freeFormMailSubject)
           case MessageFixtureType.INVITATION:
                return getAnyInviationMessage()
        }
    }

    @Override
    void addRelationData(obj){
         // already done?
        obj.thread = conversationThreadFixture.getTestData()
    }

    @Override
    void reset(){
        super.reset()
        fixtureType = MessageFixtureType.FREEFORM
        freeFormMailSubject = ANY_FREEFORM_SUBJECT
    }


     public def getAnyFreeFormMessage(def subject =ANY_FREEFORM_SUBJECT){
        fixtureType = MessageFixtureType.FREEFORM
        freeFormMailSubject = subject
         return this.createTestData()
     }


    /** fake message.property access  */
   private static def createFakeMessages(def messageResult){
        [getMessage:{a,b,c,d->messageResult}]
    }

   private GenericMessage getAnyInviationMessage(){
        return  doInContext{anySender, anyProject->
            def result = SystemMessageFactory.createInvitation(anySender, anyProject)
            result.payload.messageSource = createFakeMessages(ANYINVITATION_SUBJECT)
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



   private GenericMessage createFreeFormMessage(def subject =ANY_FREEFORM_SUBJECT){
        def result = new GenericMessage()
        result.setPayload(new FreeFormMessagePayload(subject:subject, body:"Hello World,\nthis is a test message.\n with subject: $subject"))
        return result
    }
}
enum MessageFixtureType{
    FREEFORM, INVITATION
}