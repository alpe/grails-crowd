<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <title>Mailbox: ${loggedInMember.name}</title>
    <meta name="layout" content="grailscrowd" />
</head>


<body id="mailbox">
      <g:render template="/shared/messagesRenderer" model="[:]" />
      <div class="content" style="width: 120px; padding-left: 5px;padding-right: 5px; float: left;background: red">
          <h6><g:link class="inline-link" controller="mailbox" action="compose" >Compose Message</g:link></h6>
          <h6>Inbox <g:if test="${mailbox.hasAnyNewMessages()}">(${mailbox.numberOfNewMessages} new)</g:if></h6>
          <h6><g:link class="inline-link" controller="mailbox" action="sentbox" >Sent Messages</g:link></h6>
    </div>
    <div  style="width: 800px; padding-top:10px; padding-left: 10px; float: left;background: yellow">
        <div class="main">
            <span class="content-font">
            <g:if test="${messages}">
                <table class="data">
                    <tbody>
                    <g:each in="${messages}" var="message">
                        <g:if test="${message.unread}">
                            <tr class="new">
                        </g:if>
                        <g:else>
                            <tr>
                        </g:else>
                        <td>
                             <g:render template="/shared/memberIconAndNameSmall" model="[email:message.memberEmail, name:message.fromMember, displayName: message.memberDisplayName]" />
                        </td>
                            <td>
                                ${message.subject}
                            </td>
                            <td style="padding-left: 10px">
                                <g:link class="inline-link" controller="mailbox" action="showInboxMessage" params="[id:message.id]">details...</g:link>
                            </td>
                             <td><g:formatDate format="yyyy-MM-dd hh:mm.ss" date="${message.sentDate}"/></td>
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

    <div style="clear: both;" />
    </div> <!-- content -->

</body>
</html>