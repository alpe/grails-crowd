    <p>
        Please use options below to either <span class="pink"><b>approve</b></span> the request to participate in the project or <span class="pink"><b>reject</b></span> the request.
    </p>

    <g:form controller="projectParticipation" method="post">
        <g:hiddenField name="messageId" value="${message.id}" />
        <g:hiddenField name="projectId" value="${message.payload.projectId}" />
        <g:hiddenField name="creator" value="${loggedInMember.name}" />
        <g:hiddenField name="inviteeOrRequestor" value="${message.fromMember}" />
        <g:actionSubmit class="btn" name="approve" action="approveParticipationRequest" value="Approve request" />
        &larr;&rarr;
        <g:actionSubmit class="btn" name="reject" action="rejectParticipationRequest" value="Reject request" />
     </g:form>