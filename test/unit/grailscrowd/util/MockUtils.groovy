package grailscrowd.util

import grails.test.MockUtils
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.test.GrailsUnitTestCase

/**
 * Helper class to mock domain instances and fake framework methods.
 * @see grails.test.MockUtils
 * @see grails.test.GrailsUnitTestCase
 * declare additiona
 *
 * @author ap
 */
class MockUtils {

    /* store unmodified metaclass for undo */
    private static ThreadLocal metaStorage = new ThreadLocalMetaClassStorage()

    static void saveMetaClassForUndo(clazz){
        metaStorage.get().registerMetaClass(clazz)
    }

    static void cleanup(){
        metaStorage.get().tearDown()
    }

    /**
     * Add additional domain fields to mocked class.
     * @see grails.test.MockUtils#mockDomain
     */
     static void mockDomain(obj){
       Class clazz = obj.getClass()
       saveMetaClassForUndo(clazz)
       ['id', 'version'].each{
           if (!clazz.metaClass.hasProperty(obj, it)){
                clazz.metaClass."$it" = null
           }
           // init with long value, must not be integer 
           if(!obj.getProperty(it)){
               obj.setProperty(it, 1L)
           }
       }

       def missingProperties = GrailsClassUtils.getStaticPropertyValue(clazz, "hasMany")
       if (missingProperties){
         missingProperties.each{key, value->
             if (!clazz.metaClass.hasProperty(obj, key)){
                  clazz.metaClass."$key" = null
             }
         }
       }
      missingProperties = GrailsClassUtils.getStaticPropertyValue(clazz, "belongsTo")
         if (missingProperties){
           missingProperties.each{key, value->
               if (!clazz.metaClass.hasProperty(obj, key)){
                    clazz.metaClass."$key" = null
               }
           }
         }

       grails.test.MockUtils.mockDomain(clazz, [obj])
     }
}

/** Use GrailsUnitTestCase metaclass store functionality */ 
class ThreadLocalMetaClassStorage extends ThreadLocal{

    protected def initialValue(){
        def result =  new GrailsUnitTestCase();
        result.setUp()
        return result
    }
}