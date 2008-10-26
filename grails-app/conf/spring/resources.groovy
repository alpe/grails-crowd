import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter
import org.hibernate.jmx.StatisticsService
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
import org.springframework.jmx.export.naming.MetadataNamingStrategy

import grailscrowd.core.message.MessageNotificationFIFO




// Place your Spring DSL code here
beans = {


    //-------------------------------------------
    // grailscrowd beans
    //-------------------------------------------

    messageNotificationFIFO(MessageNotificationFIFO){
        // currently noting to configure
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

    namingStrategy(MetadataNamingStrategy){
        attributeSource = jmxAttributeSource
    }

    exporter(MBeanExporter){
        server = mbeanServer
        assembler = assembler
//        namingStrategy = namingStrategy
        beans = ['gc.hibernate:name=hibernate.statistics':hibernateStatistics,
                 'gc.mail:name=notification.fifo':messageNotificationFIFO
        ]
    }

}