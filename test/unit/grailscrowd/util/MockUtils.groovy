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
         addPersistenceAttributes(clazz, obj)
         addRelations(clazz, obj)
         addCriteriaMethod(clazz, obj)
         addExecuteQuery(clazz, obj)

       grails.test.MockUtils.mockDomain(clazz, [obj])
     }

    static def addCriteriaMethod(Class clazz, obj) {
       clazz.metaClass.'static'.createCriteria ={
           return new Expando([list:{closure->[]}])
       }
    }
    
    static def addExecuteQuery(Class clazz, obj) {
       clazz.metaClass.'static'.executeQuery ={
           return []}
    }



     static def addPersistenceAttributes(Class clazz, obj) {
         ['id', 'version'].each {
            if (!clazz.metaClass.hasProperty(obj, it)) {
                clazz.metaClass."$it" =null
            }
        }
        obj.metaClass = clazz.metaClass
        ['id', 'version'].each {
            // init with long value, must not be integer
            if (!obj."$it") {
                obj."$it" = 1L
            }
          }
    }

    /** add hasMany, belongsTo methods */
    static def addRelations(Class clazz, obj) {
        def missingProperties = GrailsClassUtils.getStaticPropertyValue(clazz, "hasMany")
        if (missingProperties) {
            missingProperties.each {key, value ->
                if (!obj.metaClass.hasProperty(obj, key)) {
                    obj.metaClass."$key" = null
                }
            }
        }

        missingProperties = GrailsClassUtils.getStaticPropertyValue(clazz, "belongsTo")
        if (missingProperties) {
            missingProperties.each {entry->
                if (entry instanceof Map.Entry){
                    if (!obj.metaClass.hasProperty(obj, entry.key)) {
                        obj.metaClass."${entry.key}" = null
                    }
                }
            }
        }

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