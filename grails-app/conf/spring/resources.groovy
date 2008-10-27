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


    //-------------------------------------------
    // grailscrowd beans
    //-------------------------------------------
    mailerJobConfig(MailerJobConfig){
        startDelay = 6000
        timeout = 60000     // execute job every minute
        enabled = true
    }

    mailerJobStatistics(MailerJobStatistics){
        // noting to configure
    }

    blacklist(Blacklist){
        // noting to configure
    }

    messageNotificationFIFO(MessageNotificationFIFO){
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
                 'gc.mail:name=notification.fifo':messageNotificationFIFO,
                'gc.mail:name=job.settings':mailerJobConfig,
                'gc.mail:name=job.statistics':mailerJobStatistics,
                'gc.mail:name=blacklist':blacklist,
                'gc.mail:name=circuitbreaker.ext':mailCircuitBreaker
        ]
    }

}