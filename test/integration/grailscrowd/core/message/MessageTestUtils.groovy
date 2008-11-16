package grailscrowd.core.message
/**
 * @author ap
 */
class MessageTestUtils {


    static def createMessages = {threadQuantity = 1, int msgQuantity = 1 ->
        def anySubject = "anySubject"
        def anyBody = "anyBody"
        def result = []
        ConversationThread.withTransaction {ctx ->
            threadQuantity.times {index ->
                def message = FreeFormMessageFactory.createNewMessage(anySender, anyBody + "$index 1/$msgQuantity")
                assert message
                messageService.startNewConversation(anySubject, anySender, anyRecipient, message)
                result << message
                def addRespClosure = MessageTestUtils.addRespMessage
                addRespClosure.delegate = delegate
                result.addAll addRespClosure((msgQuantity - 1), message)
                anyRecipient.save(flush: true)
                assert message.id                
            }
        }
        return result
    }

    static def addRespMessage = {int msgQuantity, message ->
        def anyBody = "anyRespBody"
        def result = []
        ConversationThread.withTransaction {ctx ->
            msgQuantity.times {
                def responseMsg = FreeFormMessageFactory.createNewMessage(anySender, anyBody + " ${1 + it}/$msgQuantity")
                messageService.respondToMessage(message, anySender, responseMsg)
                result << responseMsg                
            }
        }
        return result
    }


}