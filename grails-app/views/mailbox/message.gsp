<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <title>Message for: ${loggedInMember.name}</title>
    <meta name="layout" content="grailscrowd" />
</head>


<body id="message">
<div class="content">

    <div id="nav-context">
        ${message.subject.encodeAsHTML()}
    </div>
        <div class="main">
            <div class="description-box">
                <span class="content-font">
                    <p>
                        <g:if test="${message.isSystemMessage()}">
                            <g:render template="systemMessage" model="[message:message]" />
                        </g:if>
                        <g:else>
                            <g:render template="freeFormMessage" model="[message:message]" />
                        </g:else>
                    </p>
                </span>
            </div>
            <g:link class="inline-link" controller="mailbox">&laquo; Go back to mailbox</g:link>


        </div> <!-- main -->

    </div> <!-- content -->    

</body>
</html>