package grailscrowd.core


import grailscrowd.util.MockUtils


/**
 * Preinitialized testdata
 *
 * @author ap
 */
class GrailsProjectFixture {

    /**
     * Get an initialized grailsProject sample for testing purpose.
     */
   public static GrailsProject getGrailscrowdSample(){
       def result = new GrailsProject(uri: 'http://grailscrowd.com',
               name: 'GrailsCrowd',
               description: 'Test app',
               primaryLocation: 'USA',
               architectureDescription: 'grails/mvc')
//                tagTokens: 'grails,groovy,java'.split(','))
/*

               args.tagTokens.each {
                   project.addToTaggings(Tagging.createFor(creator, it, project))
               }
*/
//       result.creatorMemberId = MemberFixture.Sample.id
       MockUtils.mockDomain(result)
       result.id = 1L
       return result
      }
    
    /**
     * Get an initialized grailsProject sample for testing purpose.
     */
   public static GrailsProject getExampleOneSample(){

       def result = new GrailsProject(uri: 'http://example1.com',
               name: 'ExampleOne',
               description: 'Example 1 project',
               primaryLocation: 'USA',
               architectureDescription: 'Rich UI (Flex) based application with Grails backend. Has Atom feeds and number of plugins')
       MockUtils.mockDomain(result)
       result.id = 2L
       return result
      }
}