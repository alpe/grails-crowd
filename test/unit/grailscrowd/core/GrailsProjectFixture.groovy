package grailscrowd.core


import grails.test.MockUtils


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
       MockUtils.mockDomain(GrailsProject)
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
       result.metaClass.id = 1L
       return result
      }
    
    /**
     * Get an initialized grailsProject sample for testing purpose.
     */
   public static GrailsProject getExampleOneSample(){
       MockUtils.mockDomain(GrailsProject)
       def result = new GrailsProject(uri: 'http://example1.com',
               name: 'ExampleOne',
               description: 'Example 1 project',
               primaryLocation: 'USA',
               architectureDescription: 'Rich UI (Flex) based application with Grails backend. Has Atom feeds and number of plugins')
       result.metaClass.id = 2L
       return result
      }
}