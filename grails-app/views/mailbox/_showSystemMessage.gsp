<%@ page import="grailscrowd.core.message.SystemMessageType" %>


    <div class="description-box" style="text-align:center;">
        <span class="content-font">
            <g:render template="${message.payload.messageCode.replace('.', '/')}" model="[message:message]"/>
        </span>
    </div>

