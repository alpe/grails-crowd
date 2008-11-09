<g:set var="message" value="${thread.highlightMessage}" scope="page" />
<g:set var="sender" value="${!sentboxView?message.sender:message.getRecipients()?.iterator().next()}" scope="page" />

<g:if test="${thread.highlightUnreadMessages && message.unread}">
    <tr class="new" style="vertical-align:middle">
</g:if>
<g:else>
    <tr>
</g:else>
<td style="height:70px">
    <div style="width:820px; ">
        <g:render template="messageListHeader" model="[threadId:thread.id, message:message, sender:sender]" />
    </div>
    <div style="clear:both; padding-top:5px;padding-left:25px;">
        <g:render template="/shared/memberIconSmall" model="[name:sender.name, email:sender.email]" />
        <g:link class="inline-link" style="padding-left: 10px;vertical-align:top;text-decoration:none; " controller="mailbox" action="showConversation" params="[id:thread.id, msgId:message.id, offset:params?.offset, max:params?.max]">
            <strong>${message.subject.encodeAsHTML()}</strong>
        </g:link>
    </div>
    <div style="clear:both;"/>
</td>
</tr>
