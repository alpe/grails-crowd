<content tag="pageNav">sentbox</content>
<head>
    <meta name="layout" content="mailbox"/>
</head>
<body>
<div>
    <span class="content-font">
        <g:if test="${!threads}">
            <h2>Your sentbox is empty</h2>
        </g:if>
        <g:else>
            <table class="data">
                <tbody>
                    <g:set var="sentboxView" value="${true}" scope="request"/>
                    <g:render template="conversationSummary" collection="${threads}" var="thread"/>                    
                </tbody>
            </table>
        </g:else>
    </span>
</div><!-- main-->
</body>

