<content tag="pageNav">inbox</content>
<head>
    <meta name="layout" content="mailbox"/>
</head>

<body>
<div>
    <span class="content-font">
        <g:if test="${!threads}">
            <h2>Your inbox is empty</h2>
        </g:if>
        <g:else>
            <table class="data">
                <tbody>
                    <g:render template="conversationSummary" collection="${threads}" var="thread"/>
                    <%/*g:render template="messageSummary" collection="${messages}" var="message"/*/%>
                </tbody>
            </table>
        </g:else>
    </span>
</div><!-- main-->
</body>
