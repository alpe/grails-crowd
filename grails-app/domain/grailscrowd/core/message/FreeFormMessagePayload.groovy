package grailscrowd.core.message

import grailscrowd.core.message.GenericMessage

/**
 * A message with customizable subject and body fields.
 *
 * @author ap
 */
class FreeFormMessagePayload extends GenericMessagePayload{

    String body

    static transients =  ['systemPayload']

    static constraints = {
        body(nullable:false, blank:false, maxSize:3500)
      }


    /**
     * Is system message payload.
     * @return true
     */
    boolean isSystemPayload(){
        return false
    }

}