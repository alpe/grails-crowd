<div id="mail-edit-box" class="box">
    <g:if test="${formBean.isKnownMember()}">
        <input type="hidden" name="toMemberName" value="${fieldValue(bean: formBean, field: 'toMemberName')}"/>
    </g:if>
    <g:else>
        <resource:autoComplete skin="default" />
        <p>
            <label for="subject">To:</label><br/>
            <richui:autoComplete style="margin-top: -18px;" name="toMemberName" action="${createLinkTo('dir': 'member/searchAJAX')}" />
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
        <textarea name="body" style="width:780px;height:400px;" id="body"><g:fieldValue bean="${formBean}" field="body"/></textarea>
    </p>
    <p>
        <input class="btn" type="submit" name="signUp" id="send" value="Send"/>
        <span style="padding-left: 10px;"><g:link class="btn" controller="mailbox" action="inbox">Cancel</g:link></span>
    </p>

</div>
