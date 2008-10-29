<g:set var="sender" value="${message.sender}" scope="page" />
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
            </span>
        </span>

