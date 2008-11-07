package grailscrowd.util

import grails.test.MockUtils
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.test.GrailsUnitTestCase
import java.beans.PropertyDescriptor
import java.beans.Introspector
import java.beans.PropertyDescriptor



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
         grails.test.MockUtils.mockLogging(clazz)
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
                clazz.metaClass."$it" = 0L
            }
        }
        obj.metaClass = clazz.metaClass
        if (!obj.id) {
            obj.id = System.currentTimeMillis()
        }
    }

    /** add hasMany, belongsTo methods */
    static def addRelations(Class clazz, obj) {
        def missingProperties = GrailsClassUtils.getStaticPropertyValue(clazz, "hasMany")
        if (missingProperties) {
            missingProperties.each {key, value ->
                if (!obj.metaClass.hasProperty(obj, key)) {
                    obj.metaClass."$key" = new TreeSet()
                }
            }
        }
        // constraint on named belongsTo does currently not work without an declared member attribute  
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