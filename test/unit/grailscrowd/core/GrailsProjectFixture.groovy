package grailscrowd.core


import grailscrowd.util.AbstractDomainFixture



/**
 * Preinitialized testdata
 *
 * @author ap
 */
class GrailsProjectFixture extends AbstractDomainFixture{

    MemberFixture creatorFixture

    GrailsProjectFixture(){
        this(new MemberFixture())
    }

    GrailsProjectFixture(creatorFixture){
        super()
        this.creatorFixture = creatorFixture
    }
   /** {@inheritDoc} */
   def createTestDataInstance(){
         def result
         switch(fixtureType){
             case GrailsProjectFixtureType.GRAILSCROWD:
                 result =   new GrailsProject(getGrailscrowdSample())
                 break
             case GrailsProjectFixtureType.EXAMPLE1:
                 result = new GrailsProject(getExampleOneSample())
                 break
         }
         return result
     }

    /** {@inheritDoc} */
    void addRelationData(obj){
        obj.creatorMemberId = creatorFixture.getTestData().id
    }
    

    /** {@inheritDoc} */
    void reset(){
        super.reset()
        fixtureType = GrailsProjectFixtureType.GRAILSCROWD
    }


    /**
     * Get an initialized grailsProject sample for testing purpose.
     */
   private  def getGrailscrowdSample(){
       return [uri: 'http://grailscrowd.com',
               name: 'GrailsCrowd',
               description: 'Test app',
               primaryLocation: 'USA',
               architectureDescription: 'grails/mvc']
      }
    
    /**
     * Get an initialized grailsProject sample for testing purpose.
     */
   private  def getExampleOneSample(){
       return [uri: 'http://example1.com',
               name: 'ExampleOne',
               description: 'Example 1 project',
               primaryLocation: 'USA',
               architectureDescription: 'Rich UI (Flex) based application with Grails backend. Has Atom feeds and number of plugins']
      }
}
enum GrailsProjectFixtureType{
    GRAILSCROWD, EXAMPLE1
}