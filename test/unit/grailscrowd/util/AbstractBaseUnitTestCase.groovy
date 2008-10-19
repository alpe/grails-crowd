package grailscrowd.util
/**
 * @author ap
 */
abstract class AbstractBaseUnitTestCase extends GroovyTestCase{

    /**
     * Cleanup 
     */
    void tearDown(){
        MockUtils.cleanup()
        super.tearDown()
    }

}