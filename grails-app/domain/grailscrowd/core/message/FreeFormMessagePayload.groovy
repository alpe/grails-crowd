package grailscrowd.core.message

import grailscrowd.core.message.GenericMessage

/**
 * A message with customizable subject and body fields.
 *
 * @author ap
 */
class FreeFormMessagePayload extends GenericMessagePayload{

    String subject
    
    String body

    static transients =  ['systemPayload']

    static constraints = {
        subject(nullable:false, blank:false, maxSize:100)
        body(nullable:false, blank:false, maxSize:500)
      }



    /**
     * Is system message payload.
     * @return true
     */
    boolean isSystemPayload(){
        return false
    }

}