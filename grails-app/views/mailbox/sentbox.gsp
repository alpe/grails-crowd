<content tag="pageNav">sentbox</content>
<head>
    <meta name="layout" content="mailbox" />
</head>

<body>
        <div >
            <span class="content-font">
            <g:if test="${messages}">
                <table class="data">
                    <tbody>
                        <g:render template="messageSummary" collection="${messages}" var="message" />
                     </tbody>
                </table>
            </g:if>
            <g:else>
                <h2>Your sentbox is empty</h2>
            </g:else>
            </span>
        </div><!-- main-->
</body>
