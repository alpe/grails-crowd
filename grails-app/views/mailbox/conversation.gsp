
<head>
    <meta name="layout" content="mailbox"/>
</head>

<body id="message">
<div style="width:820px; ">
    <div >
        <h1>${thread.topic.encodeAsHTML()}</h1>
    </div>
    <g:each in="${thread.messages}" var="message">
        <%
        def sender = message.sender
        %>
        
        <div style="clear:both;" />
            <g:render template="messageDetailHeader" model="[message:message]"/>
            <div style="clear:both; padding-left:5px" />
            <hr />
            <g:render style ="" template="/shared/memberIconSmall" model="[name:sender.name, email:sender.email]" />
        <div style="margin-left:38px; margin-top:-30px; margin-bottom:10px;">
                <g:if test="${message.systemMessage}">
                    <g:render template="showSystemMessage" model="[message:message]" />
                </g:if>
                <g:else>
                    <g:render template="showFreeFormMessage" model="[message:message]"/>
                </g:else>
        </div>            
        </div>
    </g:each>


    <div style="clear:both; padding-left:5px">
    <hr />
        <g:render style ="" template="/shared/memberIconSmall" model="[name:loggedInMember.name, email:loggedInMember.email]" />
        <div style="margin-left:38px; margin-top:-30px; margin-bottom:10px;">
            <g:render template="replyInput" model="[messages:messages]"/>
        </div>
    </div>
    <div >
        <g:link class="inline-link" controller="mailbox">&laquo; Go back to mailbox</g:link>
    </div>
</body>
