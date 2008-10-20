<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <title>Mailbox: ${loggedInMember.name}</title>
    <meta name="layout" content="grailscrowd" />
</head>


<body id="mailbox">
      <g:render template="/shared/messagesRenderer" model="[:]" />
<!-- mailbox nav -->
      <div class="content" style="width: 120px; padding-left: 5px;padding-right: 5px; float: left;background: #EDECE3;">
          <h6><g:link class="inline-link" controller="mailbox" action="compose" >Compose Message</g:link></h6>
          <h6>Inbox <g:if test="${mailbox.hasAnyNewMessages()}">(${mailbox.numberOfNewMessages} new)</g:if></h6>
          <h6><g:link class="inline-link" controller="mailbox" action="sentbox" >Sent Messages</g:link></h6>
    </div>
<!-- mailbox list content -->
    <div  style="width: 850px; padding-top:10px; padding-left: 10px; float: left">
        <div >
            <span class="content-font">
            <g:if test="${messages}">
                <table class="data">
                    <tbody>
                    <g:each in="${messages}" var="message">
                        <g:if test="${message.unread}">
                            <tr class="new" style="vertical-align:middle">
                        </g:if>
                        <g:else>
                            <tr>
                        </g:else>
                        <td style="height:70px">
                            <div style="width:820px">
                                <span style="float:left;">
                                    <span style="padding-left:5px;">
                                    <g:if test="${message.unread}">
                                        <img src="${createLinkTo(dir:'images',file:'../images/icons/letter.gif')}" alt="new"/>
                                    </g:if>
                                    <g:else>
                                        <img src="${createLinkTo(dir:'images',file:'../images/icons/letter_open.gif')}" alt="new"/>
                                    </g:else>
                                </span>
                                <span>
                                    <g:link style="text-decoration:none;;vertical-align:top" controller="member" action="viewProfile" params="[_name:message.fromMember]">
                                        ${message.memberDisplayName.encodeAsHTML()}
                                    </g:link>
                                </span>
                                </span>
                                <span style="float: right;">
                                    <span ><g:niceAgoDate date="${message.sentDate}" /></span>
                                    <span style="padding-left:10px;">

                                <g:link style="margin-left: 10px;vertical-align:top;text-decoration:none; " controller="mailbox" action="showConversation" params="[id:message.id]">
                                <g:if test="${message.answered}">
                                    <img src="${createLinkTo(dir:'images',file:'../images/icons/arrow_right.png')}" alt="response"/>
                                </g:if>
                                <g:else>
                                    <img src="${createLinkTo(dir:'images',file:'../images/icons/reply.gif')}" alt="reply"/>
                                </g:else>
                                </g:link>

                             </span>

                              <span style="padding-left:10px;">
                                <g:link  controller="mailbox" action="deleteInboxMessage" params="[id:message.id]">
                                    <img src="${createLinkTo(dir:'images',file:'../images/icons/action_delete.gif')}" alt="delete"/>
                                 </g:link>
                              </span>
                            </span>                                    
                            </div>

                            <div style="clear:both; padding-top:5px;padding-left:25px;">
                                <avatar:gravatar style="" email="${message.memberEmail}" defaultGravatarUrl="${'http://grailscrowd.com/images/default-gravatar-50.png'.encodeAsURL()}" size="30"/>
                                <g:link class="inline-link" style="margin-left: 10px;vertical-align:top;text-decoration:none; " controller="mailbox" action="showConversation" params="[id:message.id]">
                                <strong>${message.subject.encodeAsHTML()}</strong>
                             </g:link>
                            </div>
                            <div style="clear:both;" />
                        </td>
                         </tr>
                     </g:each>
                     </tbody>
                </table>
            </g:if>
            <g:else>
                <h2>Your mailbox is empty</h2>
            </g:else>
            </span>
        </div><!-- main-->

    </div> <!-- content -->

</body>
</html>