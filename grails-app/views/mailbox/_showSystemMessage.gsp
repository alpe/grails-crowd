<%@ page import="grailscrowd.core.message.SystemMessageType" %>

<g:render template="${message.payload.messageCode.replace('.', '/')}" model="[message:message]" />