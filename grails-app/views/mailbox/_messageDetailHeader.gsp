<% def sender = message.sender %>
        <span style="float:left;">
            <span style="padding-left:5px;">
                <g:if test="${message.unread}">
                    <img src="${createLinkTo(dir: 'images', file: '../images/icons/letter.gif')}" alt="new"/>
                </g:if>
                <g:else>
                    <img src="${createLinkTo(dir: 'images', file: '../images/icons/letter_open.gif')}" alt="read"/>
                </g:else>
            </span>
            <span>
                <g:link style="text-decoration:none;;vertical-align:top" controller="member" action="viewProfile" params="[_name:sender.name]">
                    ${sender.displayName.encodeAsHTML()}
                </g:link>
            </span>
        </span>
        <span style="float: right;">
            <span><g:niceAgoDate date="${message.sentDate}"/></span>
            <span style="padding-left:10px;">
<%/*
                <g:link style="margin-left: 10px;vertical-align:top;text-decoration:none; " controller="mailbox" action="showConversation" params="[id:threadId, msgId:message.id]">
                    <g:if test="${message.hasReply}">
                        <img src="${createLinkTo(dir: 'images', file: '../images/icons/arrow_right.png')}" alt="response"/>
                    </g:if>
                    <g:else>
                        <img src="${createLinkTo(dir: 'images', file: '../images/icons/reply.gif')}" alt="reply"/>
                    </g:else>
                </g:link>

            </span>

            <span style="padding-left:10px;">
                <g:link controller="mailbox" action="deleteInboxMessage" params="[id:threadId, msgId:message.id]">
                    <img src="${createLinkTo(dir: 'images', file: '../images/icons/action_delete.gif')}" alt="delete"/>
                </g:link>
*/ %>            </span>
        </span>
