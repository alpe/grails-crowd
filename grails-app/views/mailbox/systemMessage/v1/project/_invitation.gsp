<p>
    Please use options below to either <span class="pink"><b>join</b></span> the project or <span class="pink"><b>reject</b></span> the invitation.
</p>

<g:form controller="projectParticipation" method="post">
    <g:hiddenField name="messageId" value="${message.id}" />
    <g:hiddenField name="projectId" value="${message.payload.projectId}" />
    <g:hiddenField name="creator" value="${message.fromMember}" />
    <g:hiddenField name="inviteeOrRequestor" value="${loggedInMember.name}" />
    <g:actionSubmit class="btn" name="join" action="acceptParticipationInvitation" value="Join project" />
    &larr;&rarr;
    <g:actionSubmit class="btn" name="reject" action="rejectParticipationInvitation" value="Reject invitation" />
 </g:form>
