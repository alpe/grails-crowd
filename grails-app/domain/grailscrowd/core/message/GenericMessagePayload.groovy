package grailscrowd.core.message
/**
 * @author ap
 */
class GenericMessagePayload {

    static belongsTo = [message:GenericMessage]
/*    static constraints = {
        message(nullable:false)
      }
*/
    
}