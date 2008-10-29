
<head>
    <meta name="layout" content="mailbox"/>
</head>

<body id="message">
<div style="width:820px; ">
    <div >
        <h1 style="float:left;">${thread.topic.encodeAsHTML()}</h1>
        <div style="float:right;">
        <g:each in="${thread.participators}" var="participator">
            <g:render style ="" template="/shared/memberIconSmall" model="[name:participator.name, email:participator.email]" />            
        </g:each>
        </div>
    </div>
    <g:each in="${thread.messages}" var="message">
        <g:set var="sender" value="${message.sender}" scope="page" />
        <div style="clear:both;" />
            <g:render template="messageDetailHeader" model="[message:message]"/>
            <div style="clear:both; padding-left:5px" />
            <hr />
            <g:render style ="" template="/shared/memberIconSmall" model="[name:sender.name, email:sender.email]" />
        <div style="margin-left:38px; margin-top:-30px; margin-bottom:10px;">
                <g:if test="${message.isDeleted()}">
                    <g:render template="showDeletedMessage" model="[message:message]" />
                </g:if>
                <g:elseif test="${message.systemMessage}">
                    <g:render template="showSystemMessage" model="[message:message]" />
                </g:elseif>
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
