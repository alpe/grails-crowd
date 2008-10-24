package grailscrowd.core.message

/**
 * System message types.
 * When modifying please ensure that there is a matching
 * message key in i18n property files and legacy versions.
 * 
 * @author ap
 */
 enum SystemMessageType{


    PROJECT_JOIN_REQUEST('joinrequest', true),

    PROJECT_REQUEST_APPROVAL(PROJECT_JOIN_REQUEST.messageCode+'Approval', false),

    PROJECT_REQUEST_DISAPPROVAL(PROJECT_JOIN_REQUEST.messageCode+'Disapproval', false),


    PROJECT_INVITATION('invitation', true),

    PROJECT_INVITATION_REJECTION(PROJECT_INVITATION.messageCode+'Rejection', false),

    PROJECT_INVITATION_ACCEPTANCE(PROJECT_INVITATION.messageCode+'Accepted', false)

    

    private static String DEFAULT_PREFIX = "systemMessage"

    private final messageCode
    private final boolean responseRequired

    private SystemMessageType(String messageCode, boolean responseRequired){
        this.messageCode = messageCode
        this.responseRequired = responseRequired
    }

     /** Response requiered to start any action */
     boolean isResponseRequired(){
         return responseRequired
     }


    /** Get message code used in i18n property file. */
    public String getMessageCode(int version){
        return DEFAULT_PREFIX+".v${version}.project."+messageCode
    }
  
}