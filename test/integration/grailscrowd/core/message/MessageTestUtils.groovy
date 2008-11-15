package grailscrowd.core.message
/**
 * @author ap
 */
class MessageTestUtils {


    static def createMessages= {threadQuantity=1, int msgQuantity = 1->
          def anySubject = "anySubject"
          def anyBody = "anyBody"
          def result = []
          ConversationThread.withTransaction{ctx->
              threadQuantity.times{index->
                  def message = FreeFormMessageFactory.createNewMessage(anySender, anyBody+"$index 1/$msgQuantity")
                  messageService.startNewConversation( anySubject,  anySender,  anyRecipient, message)
                  (msgQuantity-1).times{
                      def responseMsg = FreeFormMessageFactory.createNewMessage(anySender, anyBody+"$index ${1+it}/$msgQuantity")
                      messageService.respondToMessage(message,anySender, responseMsg)
                  }

                  anyRecipient.save(flush:true)
                  assert message.id
                  result<<message
              }
          }
          return result
      }

}