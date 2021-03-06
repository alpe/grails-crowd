<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <title>Projects</title>
    <meta name="layout" content="grailscrowd" />
</head>


<body id="list-projects">

    <div id="nav-context">
        <g:render template="${navMenu}" model="${menuContext}"/>
    </div>

	<br />
	<g:render template="/shared/alphabet" model="[searchAction:'searchForProjectsByFirstLetter']"/>

    <div class="content">

        <div class="main">
            <span class="content-font">
                <g:each in="${projects}" var="project">
                    <g:render template="/shared/projectLinkAndShortenedDescription" model="[project:project]" />
                    <hr />
                </g:each>
            </span>

         </div>

    </div>

	<br />            
    <g:if test="${paginatingController && paginatingAction}">
    	<g:render template="/shared/paginator" model="[controller:paginatingController, action:paginatingAction, total:total]"/>
    </g:if>

</body>
</html>
