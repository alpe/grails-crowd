<h6><g:if test="${location != 'compose'}">
    <g:link class="inline-link" controller="mailbox" action="compose" ><g:message code="mailbox.menu.compose" /></g:link>
</g:if>
 <g:else>
    <g:message code="mailbox.menu.compose" />
 </g:else>
</h6>
<h6>
<g:if test="${location != 'inbox'}">
    <g:link class="inline-link" controller="mailbox" action="inbox" >
        <g:message code="mailbox.menu.inbox" /> <g:if test="${mailbox.hasAnyNewMessages()}">(${mailbox.numberOfNewMessages} new)</g:if>
    </g:link>
</g:if>
<g:else>
   <g:message code="mailbox.menu.inbox" /> <g:if test="${mailbox.hasAnyNewMessages()}">(${mailbox.numberOfNewMessages} new)</g:if>
</g:else>
</h6>
<h6>
<g:if test="${location != 'sentbox'}">
    <g:link class="inline-link" controller="mailbox" action="sentbox" >
        <g:message code="mailbox.menu.sentbox" />
    </g:link>
</g:if>
<g:else>
   <g:message code="mailbox.menu.sentbox" />
</g:else>
</h6>
