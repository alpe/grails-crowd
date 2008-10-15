package grailscrowd.core.message
/**
 * @author ap
 */
class GenericMessagePayload {

    static belongsTo = [message:GenericMessage]
    
    static mapping = {
        tablePerHierarchy false
//        cache usage:'nonstrict-read-write'
    }
          
}