import grailscrowd.core.*
import grailscrowd.core.message.*
import grails.util.GrailsUtil

class BootStrap {

    ProjectService projectService
    MessageService messageService

    def init = {servletContext ->

        if (GrailsUtil.environment == "development") {
                GrailsProject.withTransaction{tx->
            //Just for testing
            List sampleMembers = []
            10.times {i ->
                def result = new Member(name: "name$i", email: "name$i@example.com", password: "passwd$i", displayName: "Name $i",
                        about: "hellooo", mailbox: new Mailbox())
                result.validate()
                if (result.hasErrors()){
                    result.errors.each{
                        log.debug it
                    }
                }
                result.save(flush: true)
                sampleMembers.add(result)
            }
           int x = 1
           int counter = 0
           [sampleMembers, sampleMembers].combinations().each{mList->
               def sender = mList[0]
               def recipient = mList[1]
               if (sender==recipient){return}
               x.times{
                    messageService.startNewFreeFormConversation('anySubject'+counter++, sender, recipient, 'anyBody'+i)
                }
           }
                    
            def creatorMember = sampleMembers[0]
            assert creatorMember
            def project = projectService.createProject(uri: 'http://grailscrowd.com',
                    name: 'GrailsCrowd',
                    description: 'Test app',
                    primaryLocation: 'USA',
                    architectureDescription: 'grails/mvc', tagTokens: 'grails,groovy,java'.encodeAsUniqueList(),
                    creatorMember)
            if (project.hasErrors()) {
                log.info(project.errors)
            }
            projectService.createProject(uri: 'http://example1.com',
                    name: 'ExampleOne',
                    description: 'Example 1 project',
                    primaryLocation: 'USA',
                    architectureDescription: 'Rich UI (Flex) based application with Grails backend. Has Atom feeds and number of plugins',
                    tagTokens: 'grails,flex,social network'.encodeAsUniqueList(),
                    creatorMember)
            def anyMember = sampleMembers[1]
            assert anyMember
            project.inviteParticipant(creatorMember, anyMember)
      def result = new Member(name: "alpe", email: "a@a.com", password: "xxxxxx", displayName: "Alex Peters",
                    about: "hellooo", mailbox: new Mailbox())
            assert result.save(flush: true)
    }
        }
    }

    def destroy = {
    }


} 
