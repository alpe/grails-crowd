<content tag="pageNav">compose</content>
<head>
    <title>Compose new message.</title>
    <meta name="layout" content="mailbox"/>
</head>
<body id="compose message">
<g:render template="/shared/messagesRenderer" model="[modelBean:formBean]"/>
<% def recipient = formBean.toMember %>
<g:if test="${formBean?.isKnownMember()}">
    <g:render template="/shared/memberIconSmall" model="[email:recipient.email, name:recipient.name, displayName: recipient.displayName]"/>    
    <g:link class="membernameBig" controller="member" action="viewProfile" params="[_name:recipient.name]">
        ${recipient.displayName.encodeAsHTML()}
    </g:link>
</g:if>

<g:form method="post" action="create" id="compose">
    <div style="width:820px">
        <g:render template="freeFormMessageInput" model="[formBean:formBean]"/>
    </div>
</g:form>
</body>
