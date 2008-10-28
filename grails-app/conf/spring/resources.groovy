import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter
import org.hibernate.jmx.StatisticsService
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
import org.springframework.jmx.export.naming.MetadataNamingStrategy

import grailscrowd.core.message.MessageNotificationFIFO
import grailscrowd.core.message.Blacklist
import grailscrowd.core.message.MailerJobStatistics
import grailscrowd.core.message.MailerJobConfig
import grailscrowd.core.util.CircuitBreaker




// Place your Spring DSL code here
beans = {


    //-------------------------------------------
    // Circuit breaker
    //-------------------------------------------

    mailCircuitBreaker(CircuitBreaker){
        threshold = 12 // failure counts until open
        resetTime = 8*60*1000 // ms
    }

    blacklist(Blacklist){
        blacklistRegExps =[".*@example.com", "gash@gmx.de"]
    }
    //-------------------------------------------
    // grailscrowd beans
    //-------------------------------------------
    mailboxMailerJobConfig(MailerJobConfig){
        startDelay = 6000
        timeout = 60000     // execute job every minute
        enabled = true
    }

    mailboxMailerJobStatistics(MailerJobStatistics){
        // noting to configure
    }



    mailboxMessageNotificationFIFO(MessageNotificationFIFO){
        // noting to configure
    }


    //-------------------------------------------
    // jmx stuff
    //-------------------------------------------

    mbeanServer(MBeanServerFactoryBean){
           locateExistingServerIfPossible = true
       }
    hibernateStatistics(StatisticsService){sessionFactory->
        statisticsEnabled = true
        this.sessionFactory = sessionFactory
    }

    jmxAttributeSource(AnnotationJmxAttributeSource){}

    assembler(MetadataMBeanInfoAssembler){
        attributeSource = jmxAttributeSource
    }

    exporter(MBeanExporter){
        server = mbeanServer
        assembler = assembler
        beans = ['gc.hibernate:name=hibernate.statistics':hibernateStatistics,
                 'gc.mail:name=mailbox.notification.fifo':mailboxMessageNotificationFIFO,
                 'gc.mail:name=mailboxjob.settings':mailboxMailerJobConfig,
                 'gc.mail:name=mailboxjob.statistics':mailboxMailerJobStatistics,
                 'gc.mail:name=blacklist':blacklist,
                 'gc.mail:name=circuitbreaker.ext':mailCircuitBreaker
        ]
    }

}