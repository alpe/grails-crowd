<content tag="pageNav">compose</content>
<head>
    <title>Compose new message.</title>
    <meta name="layout" content="mailbox" />
</head>
<body id="compose message">
    <g:render template="/shared/messagesRenderer" model="[modelBean:formBean]" />

<g:if test="${formBean?.isKnownMember()}">
    <g:render template="/shared/memberIconAndNameSmall" model="[email:formBean.toMember.email, name:formBean.toMember.name, displayName: formBean.toMember?.displayName]" />
</g:if>

    <g:form method="post" action="create" id="compose">
        <div style="width:600px;">
            <g:render template="freeFormMessageInput" model="[formBean:formBean]" />
        </div>
    </g:form>
</body>
