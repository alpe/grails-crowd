package grailscrowd.core.message
/**
 * @author ap
 */
class GenericMessagePayload {

    static belongsTo = [message:GenericMessage]

    static constraints = {
          message(nullable:false)
      }

    static mapping = {
        tablePerHierarchy false
//        cache usage:'nonstrict-read-write'
    }
          
}