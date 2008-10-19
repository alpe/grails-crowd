package grailscrowd.util
/**
 * @author ap
 */
abstract class AbstractDomainFixture {

    boolean nullInstance

    /** test data object */
    private def testData

    def fixtureType


    public AbstractDomainFixture(){
        reset()
    }

    /**
     * Create new test data object.
     */
     abstract def createTestDataInstance()


     /** add any relations to given testdata intance */
     abstract void addRelationData(obj)


    /**
     * Reset internal fixture data to defaults. 
     */
    public void reset(){
        nullInstance = false
    }


    /**
     * Get test data object instance crates only a one if not initialized!
     */
    public def getTestData(){
        return isInitialized()?testData:createTestData()
    }

    private boolean isInitialized(){
        return (testData!=null||nullInstance)
    }

    /**
     * Set fixtureType and create new test data object store internal and return.
     */
    public def createTestData(fixtureType){
        this.fixtureType = fixtureType
        return createTestData()
    }


    /**
     * Create new test data object store internal and return.
     */
    public def createTestData(){
       testData  = nullInstance?null: mockDomain(createTestDataInstance())
       if(testData){ addRelationData(testData) }
       return testData
    }


    /**@see MockUtils*/
    def mockDomain(obj){
        MockUtils.mockDomain(obj)
        return obj
    }
}