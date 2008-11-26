import grailscrowd.core.*


class AuthenticationController extends SecureController {


    def allowedMethods = [loginForm: 'GET', handleLogin: 'POST']

    def beforeInterceptor = [action: this.&auth, only: ['handleSuccessfullLogin']]



    def loginForm = {
        if (loggedIn()) {
            redirectToAppropriateControllerAndActionForLoggedInMember()
        }
    }

    def handleSuccessfullLogin = {
        freshCurrentlyLoggedInMember()?.lastLogin = new Date()
        redirectToAppropriateControllerAndActionForLoggedInMember()
    }

    def handleLogout = {
        redirect(uri: '/j_spring_security_logout')
    }

    private def redirectToAppropriateControllerAndActionForLoggedInMember() {
        def redirectParams = session.originalRequestParams
        if (redirectParams) {
            session.originalRequestParams = null
            redirect(redirectParams)
        }
        else {
            redirect(uri: '/')
        }
    }

    private def handleInvalidLogin() {
        onUpdateAttempt 'Incorrect login/password combination. Please try again.', false
        render(view: 'loginForm')
    }
}
