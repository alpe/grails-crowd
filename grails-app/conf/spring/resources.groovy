import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter
import org.hibernate.jmx.StatisticsService
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
import org.springframework.jmx.export.naming.MetadataNamingStrategy

import grailscrowd.core.message.MessageNotificationQueue
import grailscrowd.core.message.Blacklist
import grailscrowd.core.message.MailerJobStatistics
import grailscrowd.core.message.MailerJobConfig
import grailscrowd.core.util.CircuitBreaker

import org.springframework.security.oauth.provider.BaseConsumerDetails
import org.springframework.security.oauth.provider.InMemoryConsumerDetailsService
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices
import org.springframework.security.oauth.provider.AccessTokenProcessingFilter;
import org.springframework.security.oauth.provider.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.UnauthenticatedRequestTokenProcessingFilter;
import org.springframework.security.oauth.provider.UserAuthorizationProcessingFilter;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret
import org.springframework.security.config.ConfigUtils


// Place your Spring DSL code here
beans = {

    //-------------------------------------------
    // Circuit breaker
    //-------------------------------------------

    mailCircuitBreaker(CircuitBreaker) {
        threshold = 12 // failure counts until open
        resetTime = 8 * 60 * 1000 // ms
    }

    blacklist(Blacklist) {
        blacklistRegExps = [".*@example.com", "gash@gmx.de"]
    }
    //-----------------------------------------------
    // mailbox notification priority queue job config
    //-----------------------------------------------
    mailboxMailerJobConfig(MailerJobConfig) {
        startDelay = 6000
        timeout = 60000     // execute job every minute
        enabled = true
        jobName = "mailboxMailer"
        quartzScheduler = ref("quartzScheduler")
    }

    mailboxMailerJobStatistics(MailerJobStatistics) {
        // noting to configure
    }

    mailboxMessageNotificationQueue(MessageNotificationQueue) {
        // noting to configure
    }

    //-----------------------------------------------
    // comment notification queue job config
    //-----------------------------------------------

    transientMessageNotificationQueue(MessageNotificationQueue) {
        // noting to configure
    }

    transientMailerJobConfig(MailerJobConfig) {
        startDelay = 6000
        timeout = 20000     // execute job every 2 minutes
        enabled = true
        jobName = "transientMailer"
        quartzScheduler = ref("quartzScheduler")
    }

    transientMailerJobStatistics(MailerJobStatistics) {
        // noting to configure
    }

    //-------------------------------------------
    // jmx stuff
    //-------------------------------------------

    mbeanServer(MBeanServerFactoryBean) {
        locateExistingServerIfPossible = true
    }
    hibernateStatistics(StatisticsService) {
        statisticsEnabled = true
        sessionFactory = ref("sessionFactory")
    }

    jmxAttributeSource(AnnotationJmxAttributeSource) {}

    assembler(MetadataMBeanInfoAssembler) {
        attributeSource = jmxAttributeSource
    }

    exporter(MBeanExporter) {
        server = mbeanServer
        assembler = assembler
        beans = ['gc.hibernate:name=hibernate.statistics': hibernateStatistics,
                'gc.mail:name=mailbox.notificationQueue': mailboxMessageNotificationQueue,
                'gc.mail:name=mailboxJob.settings': mailboxMailerJobConfig,
                'gc.mail:name=mailboxJob.statistics': mailboxMailerJobStatistics,
                'gc.mail:name=blacklist': blacklist,
                'gc.mail:name=circuitbreaker.ext': mailCircuitBreaker,
                'gc.mail:name=secondary.notificationQueue': transientMessageNotificationQueue,
                'gc.mail:name=secondaryJob.settings': transientMailerJobConfig,
                'gc.mail:name=secondaryJob.statistics': transientMailerJobStatistics,
        ]
    }


/*
    //------------------------------
    tokenServices(InMemoryProviderTokenServices){
        
    }

    consumer1(BaseConsumerDetails){
        consumerName="Tonr.com"
        consumerKey="tonr-consumer-key"
        signatureSecret= new SharedConsumerSecret("SHHHHH!!!!!!!!!!")
        resourceName="Your Photos"
        resourceDescription="Your photos that you have uploaded to sparklr.com."
    }

    consumerDetailsService(InMemoryConsumerDetailsService){
        String key = consumer1.consumerName
        consumerDetailsStore = [key:consumer1]
    }

    requestTokenFilterBean(UnauthenticatedRequestTokenProcessingFilter){
        consumerDetailsService =ref('consumerDetailsService')
        tokenServices = ref('tokenServices')
        // request-token-url
        filterProcessesUrl = "/oauth/request_token"
        // optional: nonceServices = ref('nonceServices')
        // support-ref
        // optional: providerSupport = ref('providerSupport')
    }

    authenticateTokenFilterBean(UserAuthorizationProcessingFilter){
        tokenServices = ref('tokenServices')
        // authenticate-token-url
        filterProcessesUrl = '/oauth/authorize'
        // access-granted-url
        defaultTargetUrl = '/request_token_authorized.jsp'
        // authentication-failed-url
        authenticationFailureUrl = "/oauth/confirm_access"
        // token-id-param
        // optional: tokenIdParameterName = ""
        // callback-url-param
        // optional: callbackParameterName = ""
    }

    accessTokenFilterBean(AccessTokenProcessingFilter){
        consumerDetailsService =ref('consumerDetailsService')
        tokenServices = ref('tokenServices')
        // access-token-url
        filterProcessesUrl = '/oauth/access_token'
        // optional: nonceServices = ref('nonceServices')
        // support-ref
        // optional: providerSupport = ref('providerSupport')
    }
    protectedResourceFilterBean(ProtectedResourceProcessingFilter){
        consumerDetailsService =ref('consumerDetailsService')
        tokenServices = ref('tokenServices')
        // optional: nonceServices = ref('nonceServices')
        // support-ref
        // optional: providerSupport = ref('providerSupport')
    }
    */
    /*
    filters(){
        RootBeanDefinition filterList = ref(BeanIds.FILTER_LIST)

             ManagedList filters;
             MutablePropertyValues pvs = filterList.getPropertyValues();
             if (pvs.contains("filters")) {
                 filters = (ManagedList) pvs.getPropertyValue("filters").getValue();
             } else {
                 filters = new ManagedList();
                 pvs.addPropertyValue("filters", filters);
             }

             filters.add(filter);

    }
      */
}