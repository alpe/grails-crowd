        <div class="comments-box">
            <span class="meta-info">
                <g:link class="inline-link" controller="member" action="viewProfile" params="[_name:comment.member.name]">${comment.member.displayName}</g:link> on <g:niceDate date="${comment.dateCreated}" />
            </span>
            <br/>
            <br/>
            <span class="content-font">${message.payload.body.encodeAsTextile()}</span>
        </div>
        <br/>
