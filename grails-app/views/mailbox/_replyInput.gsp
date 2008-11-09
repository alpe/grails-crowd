<div>
<g:form method="post" action="reply" id="${fieldValue(bean: thread, field: 'id')}">
<input type="hidden" name="threadId" value="${fieldValue(bean: thread, field: 'id')}"/>
<input type="hidden" name="offset" value="${offset}"/>
<input type="hidden" name="max" value="${max}"/>
    
<p>
    <label for="body">Reply:</label>
    (<a href="http://hobix.com/textile/quick.html" target="_blank">Textile enabled</a>):<br/>
    <textarea name="body" style="width:780px;height:170px;" id="body"><g:fieldValue bean="${formBean}" field="body"/></textarea>
</p>
<p>
    <input class="btn" type="submit" name="send" id="send" value="Send"/>
</p>
</g:form>    
</div>