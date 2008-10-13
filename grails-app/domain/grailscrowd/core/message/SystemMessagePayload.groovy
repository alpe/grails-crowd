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

    static transients =  ['messageSource', 'subject', 'systemPayload', 'messageCode'/*, 'messageVersion'*/]

    /**
     * Package private constructor to prevent manual instanciation. Use SystemMessageFactory.
     */
    SystemMessagePayload(){
         super()
    }

    /**
     * Is system message payload.
     * @return true
     */
    boolean isSystemPayload(){
        return true
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
    public String getMessageCode(){
        return type.getMessageCode(getMessageVersion())
    }


    /**
     * Get message subject in default language: en.
     */
    public String getSubject(){
        // use locale from menber, default to: en
        String defaultMessageCode = type.getMessageCode(1)+'.subject'
        String currentMessageCode = getMessageCode()+'.subject'
        messageSource.getMessage(currentMessageCode,
                [projectName].toArray(), defaultMessageCode, Locale.ENGLISH)
    }
}


