import grailscrowd.core.*

class LoggedInMemberFilters {

    def filters = {
        exposeLoggedInMember(controller: '*', action: '*') {
            after = {model ->
                if (session.memberId) {
                    def loggedInMember = Member.get(session.memberId)
                    if(model) {
                        model.loggedInMember = loggedInMember
                    }
                    else {
                        model = [loggedInMember:loggedInMember]
                    }
                }
            }
        }
    }
}