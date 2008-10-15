<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <title>Compose new message.</title>
    <meta name="layout" content="grailscrowd" />
</head>
<body id="compose message">
    <g:render template="/shared/messagesRenderer" model="[modelBean:formBean]" />

    <g:render template="/shared/memberIconAndNameSmall" model="[email:formBean.toMember.email, name:formBean.toMember.name, displayName: formBean.toMember.displayName]" />
    
    <g:form method="post" action="create" id="compose">
        <input type="hidden" name="toMemberName" value="${fieldValue(bean:formBean,field:'toMemberName')}" />
        <g:render template="freeFormMessageInput" model="[formBean:formBean]" />
    </g:form>
</body>
</html>