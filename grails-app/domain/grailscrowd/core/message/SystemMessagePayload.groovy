package grailscrowd.core.message


/**
 * Grailscrowd internal system message payload.
 * Subject and body will be accessed by i18n message property.
 *
 * @author ap
 */
class SystemMessagePayload extends GenericMessagePayload{


    /** project display name */
    String projectName
    /** DB id of project */
    Long projectId

    /** kind of message */
    SystemMessageType type

    /** transient i18n message source, injected by IoC */
    def messageSource

    static transients =  ['messageSource', 'subject'/*, 'messageVersion'*/]


    /**
     * Package private constructor to prevent manual instanciation. Use SystemMessageFactory.
     */
    SystemMessagePayload(){
         super()
    }

    /**
     * System message are versioned to not change the past view while rendering.
     * Fix to <code>1</code> but should be persisted and upgraded when introducing new text versions. 
     */
    private int getMessageVersion(){
        return 1
    }

    /**
     * Get full versioned i18n message code.
     */
    private String getSubjectMessageCode(int version){
        return type.defaultSubjectMessageCode+".v$version"
    }

    /**
     * Get message subject in default language: en.
     */
    public String getSubject(){
        // use locale from menber, default to: en
        String defaultMessageCode = getSubjectMessageCode(1)
        messageSource.getMessage(getSubjectMessageCode(getMessageVersion()),
                [projectName].toArray(), defaultMessageCode, Locale.ENGLISH)
    }
}


