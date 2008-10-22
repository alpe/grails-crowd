<head>
    <meta name="layout" content="mailbox"/>
</head>

<body id="message">
<div class="content">
    <div id="nav-context">
        ${thread.topic.encodeAsHTML()}
    </div>
    <g:each in="${thread.messages}" var="message">
        <g:if test="${message.systemMessage}">
            <g:render template="showSystemMessage" model="[message:message]" />
        </g:if>
        <g:else>
            <g:render template="showFreeFormMessage" model="[message:message]"/>
        </g:else>
    </g:each>
    <div style="clear:both; padding-left:10px">
        <g:render template="replyInput" model="[messages:messages]"/>
    </div>
    <div >
        <g:link class="inline-link" controller="mailbox">&laquo; Go back to mailbox</g:link>
    </div>
</div> <!-- content -->
</body>
