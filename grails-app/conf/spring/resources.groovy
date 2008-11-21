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

}