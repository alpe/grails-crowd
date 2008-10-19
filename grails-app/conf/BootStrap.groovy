import grailscrowd.core.*
import grailscrowd.core.message.*
import grails.util.GrailsUtil

class BootStrap {

    ProjectService projectService
    MessageService messageService

    def init = {servletContext ->


        if (GrailsUtil.environment == "development") {
            //Just for testing
            List sampleMembers = []
            10.times {i ->
                def result = new Member(name: "name$i", email: "name$i@example.com", password: "passwd$i", displayName: "Name $i",
                        about: "hellooo", mailbox: new Mailbox())
                assert result.save(flush: true)
                sampleMembers.add(result)
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

            def message = FreeFormMessageFactory.createNewMessage(sampleMembers[3], 'anySubject', 'anyBody')
            messageService.submit(sampleMembers[4], message)
        }
    }

    def destroy = {
    }


} 
