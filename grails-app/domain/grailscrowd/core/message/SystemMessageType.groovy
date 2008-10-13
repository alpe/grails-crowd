package grailscrowd.core.message

/**
 * System message types.
 * When modifying please ensure that there is a matching
 * message key in i18n property files and legacy versions.
 * 
 * @author ap
 */
 enum SystemMessageType{


    PROJECT_JOIN_REQUEST('joinrequest'),

    PROJECT_REQUEST_APPROVAL(PROJECT_JOIN_REQUEST.messageCode+'Approval'),

    PROJECT_REQUEST_DISAPPROVAL(PROJECT_JOIN_REQUEST.messageCode+'Disapproval'),


    PROJECT_INVITATION('invitation'),

    PROJECT_INVITATION_REJECTION(PROJECT_INVITATION.messageCode+'Rejection'),

    PROJECT_INVITATION_ACCEPTANCE(PROJECT_INVITATION.messageCode+'Accepted')

    

    private static String DEFAULT_PREFIX = "systemMessage"

    private final messageCode

    private SystemMessageType(String messageCode){
        this.messageCode = messageCode
    }
    /** Get message code used in i18n property file. */
    public String getMessageCode(int version){
        return DEFAULT_PREFIX+".v${version}.project."+messageCode
    }
  
}