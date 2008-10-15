package grailscrowd.core

import grailscrowd.core.message.GenericMessage

/** added a test line to determine svn working status (mgk) **/
//The idea of mailbox is kind of like del.icio.us for:username idea.
//The mailbox could be used to communicate invitations and requests approvals to join a project, etc.
//Another way would be to use regular email, but that would be the "more intrusive" option, IMO 

class Mailbox {

    static hasMany = [messages: GenericMessage]
    static belongsTo = [member: Member]
    static fetchMode = [messages: 'eager']


    def hasAnyDisplayableMessages() {
        getInboxMessages().size() > 0
    }

    def hasAnyNewMessages() {
        this.messages.any { it.isNew() }
    }

    def getNumberOfNewMessages() {
        this.messages.findAll { it.isNew() }.size()
    }

    def getInboxMessages(){
        this.messages.findAll {it.isVisibleInInbox()}
    }

    private def getInboxMessage(id) {
        this.messages.find {it.id == id}
    }

    def getInboxMessageAndMarkAsSeen(id) {
        def msg = getInboxMessage(id)
        msg?.markAsSeen()
        return msg
    }

    def markMessageAsArchived(id) {
        // remove
    }

    def markMessageAsAcknowleged(id) {
        // remove

    }


    /** get converation thread for given message type and grails project */ 
    def getConverationThread(systemMessageType, grailsProject){
        def msg = getInboxMessages().find{
            println it;
             return it.isSystemMessage() &&
             it.payload.type==systemMessageType &&
             it.payload.projectId == grailsProject.id
         }
        return msg?.thread
    }

    def getSentboxMessage(id){
        def c = GenericMessage.createCriteria()
        return  c.get{
                idEq(id)
                and(sentboxMessageCriteria)
                }
    }

    /**
     * Get all messages sent within the last 80 days.
     * @return List of GenericMessages
     */
    public List getSentboxMessages(){
        def c = GenericMessage.createCriteria()
        return  c.list(sentboxMessageCriteria)
    }

    private def sentboxMessageCriteria ={
            and {
                eq('fromMember', member.name)
                gt('sentDate', new Date()-80)  // not older than 80 days
            }
            order("sentDate", "desc")
        }
}
