package filter

import org.codehaus.groovy.grails.plugins.web.filters.DefaultGrailsFiltersClass
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import filters.UtilFilters
import grails.test.*
import org.codehaus.groovy.grails.plugins.web.filters.FilterToHandlerAdapter

/**
 * @author ap
 */
class UtilFiltersTest extends GrailsUnitTestCase {

    def clearFlashMessageFilterConfig

    void setUp() {
        super.setUp()
        // mock call for testing
        registerMetaClass(UtilFilters)
        MockUtils.addCommonWebProperties(UtilFilters)
        // setup instance to test
        DefaultGrailsFiltersClass filtersClass = new DefaultGrailsFiltersClass(UtilFilters)
        List filterConfigs = filtersClass.getConfigs(new UtilFilters())
        clearFlashMessageFilterConfig = filterConfigs[0]
        assertEquals 'clearFlashMessage', clearFlashMessageFilterConfig.name
    }

    // FilterToHandlerAdapter
    void testClearFlashMessage_beforeClosure_flashMessageShouldBeCleared() {
        final String anyMessage = "bla"
        clearFlashMessageFilterConfig.flash.message = anyMessage
        assertThat(clearFlashMessageFilterConfig.flash.message, is(notNullValue()))
        clearFlashMessageFilterConfig.before()
        assertThat(clearFlashMessageFilterConfig.flash.message, is(null))
    }

    void testClearFlashMessage_controllerPattern_matchAllExceptMailbox() {
        def pattern = clearFlashMessageFilterConfig.scope.controller
        assertThat(pattern, is(notNullValue()))
        FilterToHandlerAdapter filterHandler = new FilterToHandlerAdapter(filterConfig: clearFlashMessageFilterConfig)
        String mailboxControllerName = 'mailbox', actionName = "anyAction", uri = 'anyUri'
        assertThat(filterHandler.accept(mailboxControllerName, actionName, uri), is(false))
        assertThat(filterHandler.accept('member', actionName, uri), is(false))
        assertThat(filterHandler.accept('anyController', actionName, uri), is(false))
    }
}