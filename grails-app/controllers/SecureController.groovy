import grailscrowd.core.*
import org.grails.plugins.springsecurity.controller.AuthBase
import org.grails.plugins.springsecurity.service.AuthenticateService

import org.springframework.security.context.SecurityContextHolder as SCH
import org.springframework.web.servlet.support.RequestContextUtils as RCU

/**
 * Customized secure controller with much copy paste from acegi plugin AuthBase!
 */
abstract class SecureController {

    /** Authenticate Service  */
    AuthenticateService authenticateService

    /** Login user class  */
    def loginUser

    /** principal  */
    def authPrincipal

    /** is Admin  */
    boolean isAdmin

    /** Locale  */
    Locale locale

    /** main request permission setting  */
    def requestAllowed = 'ROLE_USER'

    /** is user logged in as member */
    protected def loggedIn() {
        return loginUser || authenticateService.userDomain()
    }

    protected def freshCurrentlyLoggedInMember() {
        Member.get(loginUser?.id)
    }


    /**
     * @deprecated move to utility method
     */
    protected onUpdateAttempt(message, success) {
        flash.messageClass = success ? 'info-box' : 'error-box'
        flash.message = message
    }


    /**
     * check authentification of current use. to be used by beforeInterceptor.
     */
    protected def auth() {
        if (requestAllowed != null && !authenticateService.ifAnyGranted(requestAllowed)) {
            log.debug 'request not allowed: ' + requestAllowed
            def originalRequestParams = [controller: controllerName, action: actionName, params: params]
            session.originalRequestParams = originalRequestParams
            redirect(uri: '/signin')
            return false
        }

        loginUser = authenticateService.userDomain()
        if (loginUser) {
            isAdmin = authenticateService.ifAnyGranted('ROLE_SUPERVISOR')
        }

        /* i18n: if lang params */
        if (params['lang']) {
            locale = new Locale(params['lang'])
            RCU.getLocaleResolver(request).setLocale(request, response, locale)
            session.lang = params['lang']
        }
        /* need this for jetty */
        if (session.lang != null) {
            locale = new Locale(session.lang)
            RCU.getLocaleResolver(request).setLocale(request, response, locale)
        }
        if (locale == null) {
            locale = RCU.getLocale(request)
        }

        /* cache */
        response.setHeader('Cache-Control', 'no-cache') // HTTP 1.1
        response.setDateHeader('max-age', 0)
        response.setIntHeader('Expires', -1) //prevents caching at the proxy server
        response.addHeader('cache-Control', 'private') //IE5.x only
    }


}