class UtilFilters {

    private static final Integer MAX_RESULTS_PER_PAGE = 20
    private static final Integer MAX_MAIL_RESULTS_PER_PAGE = 10

    def filters = {
        clearFlashMessage(controller: '*', action: '*') {
            before = {
				if(flash.message) {                
//					flash.message = null
				}
            }
        }

        setupCommonQueryParams(controller: "(member|grailsProject|comment|mailbox)",
                action: "(listProjectLocations|listMemberLocations|findByName|findByLocation|findByTagGlobally|findByTagForMember|list|inbox|sentbox)") {
            before = {

                if (actionName == 'listProjectLocations' || actionName == 'listMemberLocations') {
                    configureDefaultMaxResults(params, MAX_RESULTS_PER_PAGE)
                    convertPaginationParamsToInteger(params)
                }
                if (actionName == 'inbox'|| actionName == 'sentbox'){
                    configureDefaultMaxResults(params, MAX_MAIL_RESULTS_PER_PAGE)
                    configureDefaultResultOffset(params)                    
                    convertPaginationParamsToInteger(params)
                }
                if (controllerName == 'grailsProject' && actionName == 'findByLocation') {
                    if (!commonQueryCharacteristicsInit(sortBy: 'name', params: params)) {
                        redirect(uri: '/notAllowed')
                        return false
                    }
                }
                if (controllerName == 'member' && actionName == 'findByLocation') {
                    if (!commonQueryCharacteristicsInit(sortBy: 'displayName', params: params)) {
						redirect(uri: '/notAllowed')                        
						return false
                    }
                }
                if (actionName == 'findByName' || actionName == 'findByTagGlobally' || actionName == 'findByTagForMember' || actionName == 'list') {
                    configureDefaultMaxResults(params, MAX_RESULTS_PER_PAGE)
                }
            }
        }

        //This is a work around the fact that richUI tagCloud taglib does not take params,
        //only controller and action. Need to expose member here to grab it in the controller
        //quering for member's projects for a tag comming from member's tag cloud.
        exposeCloudOwningMemberInSession(controller: 'tag', action: 'cloudForMember') {
            after = {model ->                
                session.cloudOwningMemberName = model.member.name
            }
        }

        //This is a work around the fact that richUI tagCloud taglib does not take params,
        //only controller and action. Need to expose member from session here after projects
        //for that member and tag have been retrieved.
        clearCloudOwningMemberFromSession(controller: 'grailsProject', action: 'findByTagForMember') {
            after = {model ->
                session.cloudOwningMemberName = null
            }
        }
    }

    private commonQueryCharacteristicsInit(options) {
        if (!options.params.q) {
            return false
        }
        configureDefaultMaxResults(options.params)
        options.params.sort = options.sortBy
        true
    }

    private configureDefaultMaxResults(params, max) {
        if (!params.max) {
            params.max = max
        }
    }
    private configureDefaultResultOffset(params) {
        if (!params.offset) {
            params.offset = 0
        }
    }

    private convertPaginationParamsToInteger(params) {
        if (params.max && params.offset) {
            params.max = params.max.toInteger()
            params.offset = params.offset.toInteger()
        }
    }
}
