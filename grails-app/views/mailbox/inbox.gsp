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
                </tbody>
            </table>
            <g:paginate controller="mailbox" action="inbox" total="${total}" next="Forward" prev="Back"/>
        </g:else>
    </span>
</div><!-- main-->
</body>
