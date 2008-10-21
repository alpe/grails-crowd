<div id="nav-context"></div>
<div id="mail-edit-box" class="box">
    <g:if test="${formBean.isKnownMember()}">
        <input type="hidden" name="toMemberName" value="${fieldValue(bean:formBean,field:'toMemberName')}" />
    </g:if>
    <g:else>
    <p>
        <label for="subject">To:</label><br/>
        <input type="text" id="toMemberName" name="toMemberName" maxlength="99"
            style="width:200px"
            value="${fieldValue(bean: formBean, field: 'toMemberName')}"/>
    </p>
    </g:else>

    <p>
        <label for="subject">Subject:</label><br/>
        <input type="text" id="subject" name="subject" maxlength="99"
            style="width:400px"
            value="${fieldValue(bean: formBean, field: 'subject')}"/>
    </p>
    <p>
    <label for="body">Body:</label>
    (<a href="http://hobix.com/textile/quick.html" target="_blank">Textile enabled</a>):<br/>
        <textarea name="body" style="width:550px" cols="53" rows="10" id="body"><g:fieldValue bean="${formBean}" field="body"/></textarea>
    </p>
    <p>
        <input class="btn" type="submit" name="signUp" id="send" value="Send"/>
        <span style="padding-left: 10px;"><g:link class="btn" controller="mailbox" action="inbox">Cancel</g:link></span>
    </p>

</div>
