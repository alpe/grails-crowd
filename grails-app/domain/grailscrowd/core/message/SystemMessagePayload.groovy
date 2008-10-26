package grailscrowd.core.message

import grailscrowd.core.GrailsProject


/**
 * Grailscrowd internal system message payload.
 * Subject and body will be accessed by i18n message property.
 * @author ap
 */
class SystemMessagePayload extends GenericMessagePayload{

    /** DB id of project */
    Long projectId

    /** kind of message */
    SystemMessageType type

    int messageVersion

    static transients =  ['systemPayload', 'messageCode']

    static constraints = {
        projectId(nullable:false)
        type(nullable:false)
    }


    /**
     * Package private constructor to prevent manual instanciation. Use SystemMessageFactory.
     */
    SystemMessagePayload(){
        super()
        messageVersion = 1
    }

    /**
     * Is system message payload.
     * @return true
     */
    boolean isSystemPayload(){
        return true
    }

    /**
     * Get full versioned i18n message code.
     */
    public String getMessageCode(){
        return type.getMessageCode(getMessageVersion())
    }

    /** Is response required by given member */
   def isResponseActionPending(member){
       if (type.isResponseRequired()){
           def project = GrailsProject.get(projectId)
           switch(type){
               case SystemMessageType.PROJECT_JOIN_REQUEST:
               return project.isParticipitionResponseOpenFor(message.sender)                   
               case SystemMessageType.PROJECT_INVITATION:
               return project.isParticipitionResponseOpenFor(member)
           }
       }
       return false
   }

}


