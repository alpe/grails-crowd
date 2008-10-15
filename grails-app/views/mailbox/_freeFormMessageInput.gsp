<div id="nav-context"></div>
 <div id="mail-edit-box" class="box">
     <p><label for="displayName">Subject:</label><br/> <input type="text" name="subject" id="subject"
         value="${fieldValue(bean:formBean,field:'subject')}"/>

     <p><label for="body">Body:</label> (<a href="http://hobix.com/textile/quick.html" target="_blank">Textile enabled</a>):<br/>
         <textarea name="body" cols="53" rows="10" id="body"><g:fieldValue bean="${formBean}" field="body" /></textarea>
     </p>
     <p><input class="btn" type="submit" name="signUp" id="send" value="Send"/>
     <span style="padding-left: 10px;"><g:link class="btn" controller="mailbox" action="inbox" >Cancel</g:link></span></p>

 </div>
