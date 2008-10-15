<h4>
    <avatar:gravatar email="${email}" defaultGravatarUrl="${'http://grailscrowd.com/images/default-gravatar-50.png'.encodeAsURL()}" size="30"/>
    <g:link controller="member" action="viewProfile" params="[_name:name]">${displayName}</g:link>
</h4>