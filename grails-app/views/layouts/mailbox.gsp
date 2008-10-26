<g:applyLayout name="grailscrowd">
  <head>
    <title>Mailbox: ${loggedInMember.name}</title>
  </head>

  <body id="mailbox">


  <div class="content" >
    <div style="width: 120px; float: left; padding-left: 5px;padding-right: 5px; background: #EDECE3;">
        <g:set var="model" value="${pageProperty(name:"page.pageNav")}" scope="page" />
        <g:render template="/mailbox/mailboxMenu" model="[location: model]"/>
    </div>
    <div  style="width: 850px; float: left; padding-top:10px; padding-left: 10px; ">
        <g:render template="/shared/messagesRenderer" model="[modelBean:formBean]" />
        <g:layoutBody/>
    </div>
  </div>
  </body>
</g:applyLayout>    