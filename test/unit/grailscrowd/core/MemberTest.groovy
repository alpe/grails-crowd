package grailscrowd.core

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat
import grailscrowd.core.message.*
import grailscrowd.util.*

/**
 * @author ap
 */
class MemberTest  extends AbstractBaseUnitTestCase{

    def anyMemberFixture
    def anyMember


     void setUp(){
        super.setUp()
        anyMemberFixture = new MemberFixture()
        anyMember = anyMemberFixture.testData
    }


    void testCompareToMatchesEqualContract_same(){
        assertThat(anyMember.equals(anyMember), is(true))
        assertThat(anyMember.compareTo(anyMember), is(0))
    }
    
    void testCompareToMatchesEqualContract_sameDisplayNameButNotEquals(){        
        def otherMember = anyMemberFixture.createTestData()
        assertThat(anyMember.displayName, is(otherMember.displayName))
        assertThat(anyMember.is(otherMember), is(false))
        assertThat(anyMember.equals(otherMember), is(false))
        assertThat(anyMember.compareTo(otherMember), is(not(0)))
    }
    
}