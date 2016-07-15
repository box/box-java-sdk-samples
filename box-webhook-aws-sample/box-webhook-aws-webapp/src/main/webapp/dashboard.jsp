<jsp:include page="top.jsp"/>

<% if (request.getParameter("error") != null) { %>
<%-- TODO Escape and encode ${param.error} properly. It can be done using jstl c:out. --%>
<span style="color: red;">${param.error}</span>
<% } %>

<h2>Your Dashboard</h2>
<h3><i class="fa fa-exclamation-circle"></i> ${dashboardMessage} </h3>
<hr/>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3>Folders</h3>
<c:forEach items="${folders}" var="folder">
	<h4>${folder.getName()}</h4>
</c:forEach>

<h3>Files</h3>
<c:forEach items="${files}" var="file">
	<div class="row" style="margin-left: 0px;">
		<a href="/docdetails?id=${file.getID()}"><c:out value="${file.getName()}"/></a>
	</div>
</c:forEach>

<br/>
<h4>Upload File using CORS</h4>
<div class="row">
	<div class="col-md-4">
		<form action='blah' id='file-form' method='POST'>
			<div class='form-group'>
				<input class='form-control' id='file-select' name='files' type='file'>
			</div>
			<div class='form-group'>
				<button class='btn btn-default' id='upload-button' type='submit'>
					Upload
				</button>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript"> var accessToken = "${accessToken}";    </script>
<script src='corsupload.js'></script>
<script type="text/javascript">

	<jsp:include page="bottom.jsp"/>
