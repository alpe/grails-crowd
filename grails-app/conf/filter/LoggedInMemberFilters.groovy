package filter

import org.springframework.security.context.SecurityContextHolder as SCH
import org.grails.plugins.springsecurity.service.AuthenticateService

import grailscrowd.core.*

class LoggedInMemberFilters {

    AuthenticateService authenticateService

    def filters = {
        exposeLoggedInMember(controller: '*', action: '*') {
            after = {model ->
                println "principal: "+authenticateService.principal()
                def loggedInMember = Member.get(authenticateService.userDomain()?.id)
                if (model) {
                    model.loggedInMember = loggedInMember
                }
                else {
                    model = [loggedInMember: loggedInMember]
                }
            }
        }
    }
}