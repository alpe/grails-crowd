package grailscrowd.core

import grailscrowd.capability.NumberOfViewsTrackable
import grailscrowd.core.message.*

class GrailsProject extends NumberOfViewsTrackable implements Comparable {

    String uri
    String name
    String description
    String primaryLocation
    String architectureDescription

    // auto-timestamping
    Date dateCreated
    Date lastUpdated

    //The creator or owner of this project in grailscrowd 
    Long creatorMemberId = -1L

    static hasMany = [participants: ProjectParticipation, taggings: Tagging, comments: Comment, favoriteReferences:FavoriteProjectReference]
	SortedSet taggings
	SortedSet comments

    MessageService messageService

    static transients =  ['messageService']

    static constraints = {
        uri(blank: false, unique: true, url: true)
        name(blank: false, maxSize: 100, unique: true)
        description(blank: false, maxSize: 4000)
        primaryLocation(nullable: true, maxSize: 2000)
        architectureDescription(nullable: true, maxSize: 4000)
    }
    //static fetchMode = [participants: 'eager']


    def inviteParticipant(creator, invitee) {
        enforceParticipationInvariants(creator, invitee)
        GenericMessage.withTransaction{tx->
        if (!invitee.isAwareOf(this)) {
            ProjectParticipation.pending(invitee, this)
            def msg = SystemMessageFactory.createInvitation(creator, this)
            messageService.startNewSystemConversation(name, creator, invitee, msg)
        }
      }
    }

    def acknowlegeParticipationAcceptance(invitee, messageId) {
        acknowlegeParticipationAcceptance(getCreator(), invitee, messageId)
    }
    private def acknowlegeParticipationAcceptance(creator, invitee, messageId) {
        enforceParticipationInvariants(creator, invitee)
        ProjectParticipation.pending(invitee, this).accept()
        messageService.respondToMessage(messageId, invitee,
                SystemMessageFactory.createRejectInvitation(invitee, this))
    }

    def rejectParticipationInvitation(invitee, messageId) {
        rejectParticipationInvitation(getCreator(), invitee, messageId)
    }

    private def rejectParticipationInvitation(creator, invitee, messageId) {
        enforceParticipationInvariants(creator, invitee)
        ProjectParticipation.pending(invitee, this).reject()
        messageService.respondToMessage(messageId, invitee,
                SystemMessageFactory.createRejectInvitation(invitee, this))
    }

   def requestParticipation(Member requestor) {
        if (requestor.id == this.creatorMemberId) {
            throw new IllegalArgumentException("The member is the creator of this project. Cannot request participation for the owning project")
        }
        if (participatesInThisProject(requestor)) {
            throw new IllegalArgumentException("The member already participates in this project or has a pending participation request. Cannot request participation in projects more than once")
        }
        if (!requestor.isAwareOf(this)) {
            ProjectParticipation.request(requestor, this)
            def msg = SystemMessageFactory.createJoinRequest(creator, this)
            messageService.startNewSystemConversation(name, requestor, creator, msg)
        }
    }    

    def approveRequestedParticipation(creator, requestor, messageId) {
        enforceParticipationInvariants(creator, requestor)
        ProjectParticipation.request(requestor, this).accept()
        messageService.respondToMessage(messageId, creator,
                SystemMessageFactory.createApproveToJoinRequest(creator, this))
    }

    def rejectRequestedParticipation(creator, requestor, messageId) {
        enforceParticipationInvariants(creator, requestor)
        ProjectParticipation.request(requestor, this).reject()
        messageService.respondToMessage(messageId, creator,
                SystemMessageFactory.createDisapprovalToJoinRequest(creator, this))
    }


    void setCreator(Member owner) {
        if (hasACreator() && this.creatorMemberId != owner.id) {
            throw new IllegalArgumentException("Cannot change the owner of this project.")
        }
        this.creatorMemberId = owner.id
    }

    def getCreator() {
        if (!hasACreator()) {
            throw new IllegalStateException("Cannot call this method until this project has been fully created.")
        }
        def creator = this.participants.find {it.participant.id == this.creatorMemberId}
        if (!creator) {
            throw new IllegalStateException("Cannot call this method until this project has been fully created.")
        }
        return creator.participant
    }

    def allActiveParticipants() {
        this.participants.findAll {(it.participant.id != this.creatorMemberId) &&
                                    (it.isActive())}.participant
    }

    def relationshipRoleFor(member) {
        if(participatesInThisProject(member)) {
            return isCreator(member.id) ? 'I am the creator of' : 'I participate in'
        }
        else {
            return 'None'
        }
    }

    def isCreator(memberId) {
        getCreator().id == memberId
    }

    def getCommaSeparatedTags() {
        this.taggings?.tag?.name?.join(',')
    }
    
    def hasAnyMiscInfo() {
        (this.uri != null) || (this.primaryLocation != null)
    }

	def getUniqueMembersWhoPostedComments() {
		this.comments.member.unique()
	}

    def hasAnyActiveParticipants() {
        allActiveParticipants().size() > 0
    }

    def hasAnyTags() {
        this.taggings.size() > 0
    }

    // projects are sorted alphabetically, by default
    public int compareTo(Object obj) {
        return name <=> obj.name
    }


    /**
     * Is a participition response to this project open for given member?
     * @return result
     */
    boolean isParticipitionResponseOpenFor(member){
         return member && this.participants.any {
             (it.participant.id == member.id) &&
                 it.isUnfinished()
         }
    }
 


    private def enforceParticipationInvariants(creator, requestor) {
        if (creator?.id != this.creatorMemberId) {
            throw new IllegalArgumentException('Wrong project creator')
        }
        if (creator?.id == requestor?.id) {
            throw new IllegalArgumentException('Cannot act for yourself')
        }
        if (participatesInThisProject(requestor)) {
            throw new IllegalArgumentException("The member already participates in this project.")
        }
    }

    private def participatesInThisProject(member) {
        null != findParticipantionFor(member, ProjectParticipation.ACTIVE)
    }

    private def findParticipantionFor(member, participationStatus) {
        this.participants.find {
            (it.participant.id == member.id
                && it.status == participationStatus)}
    }

    private def hasACreator() {
        -1L != this.creatorMemberId
    }
}
