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

    def getMessage(id) {
        this.messages.find {it.id == id}
    }

    def markMessageAsSeen(id) {
        def msg = getMessage(id)
        msg?.markAsSeen()
        return msg
    }

    def markMessageAsArchived(id) {
        // remove
    }

    def markMessageAsAcknowleged(id) {
        // remove
        
    }

    /**
     * Get all messages sent within the last 80 days.
     * @return List of GenericMessages
     */
    public List getSentMessages(){
        def c = GenericMessage.createCriteria()
        return  c.list {
            and {
                eq('fromMember', member.name)
                gt('sentDate', new Date()-80)  // not older than 80 days
            }
            order("sentDate", "desc")
        }
    }
}
