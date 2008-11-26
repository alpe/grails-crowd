/**
 * @author ap
 */
security {
    active = true
/*
def filterNames = []
filterNames << 'httpSessionContextIntegrationFilter'
filterNames << 'logoutFilter'
filterNames << 'authenticationProcessingFilter'
if (conf.useOpenId) {
    filterNames << 'openIDAuthenticationProcessingFilter'
}
filterNames << 'securityContextHolderAwareRequestFilter'
filterNames << 'rememberMeProcessingFilter'
filterNames << 'anonymousProcessingFilter'
filterNames << 'exceptionTranslationFilter'
filterNames << 'filterInvocationInterceptor'
filterNames << 'oauthRequestTokenFilter'
filterNames << 'oauthAuthenticateTokenFilter'
filterNames << 'oauthAccessTokenFilter'
filterNames << 'oauthProtectedResourceFilter'
*/

    loginUserDomainClass = 'grailscrowd.core.Member'
    userName = 'name'
    password = 'password'
    algorithm = 'SHA'
    encodeHashAsBase64 = true
    loginFormUrl = '/signin'
    authenticationFailureUrl = '/signin'
    defaultTargetUrl='/signedin'
    useRequestMapDomainClass = false
    requestMapString = """
CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
PATTERN_TYPE_APACHE_ANT
/login/**=IS_AUTHENTICATED_ANONYMOUSLY
/admin/**=ROLE_USER
/book/test/**=IS_AUTHENTICATED_FULLY
/book/**=ROLE_SUPERVISOR
/**=IS_AUTHENTICATED_ANONYMOUSLY
"""

}