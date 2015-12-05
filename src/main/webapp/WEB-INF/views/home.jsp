<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />

<title>FinishInbox</title>
</head>
<body>



	<div class="span7" style="margin-left: 10px">
		<div id="wrapper" align="right"
			style="margin-right: 5px; padding-right: 5px">
			<p>Logged in as: ${userName}</p>
			<a href=" ${logoutURL}"> LogOut </a>
		</div>
		<br /> <br /> <br />

		<p>Welcome to your Finish Inbox. The purpose of the application is
			to use GAE with GmailAPI and TranslateAPI. It takes your unread
			English emails using GmailAPI and translates them to Finish using TranslateAPI,
			 then saves them into a MySQL DB in CloudSQL. 
			 If you can't see them and the application did not request for 
			 permission to read your emails, please refresh the page</p>
		<h3>These are your unread emails and their translation to FINISH
		</h3>

		<table style="width: 80%; border-spacing: 0;"
			class="table table-striped table-bordered table-condensed">
			<tr>
				<th>Email Snippet</th>
				<th>Translated Snippet</th>
			</tr>

			<c:forEach items="${toStoreEmails}" var="msg">
				<tr>
					<td>${msg.snippet}</td>
					<td>${msg.translatedSnippet}</td>
				</tr>
			</c:forEach>

		</table>

		<h3>These emails are read from the DB. They will be deleted from the db when you refresh the page.</h3>

		<table style="width: 80%; border-spacing: 0;"
			class="table table-striped table-bordered table-condensed">
			<tr>
				<th>Email Snippet</th>
				<th>Translated Snippet</th>
			</tr>

			<c:forEach items="${emailsFromDB}" var="email">
				<tr>
					<td>${email.snippet}</td>
					<td>${email.translatedSnippet}</td>

				</tr>
			</c:forEach>

		</table>

	</div>
	<br />
	<br />
	<br />
	<br />
	<br />
	<div align="center">
		<!-- Footer -->
		<footer id="footer">

			<p>&copy; finishInbox</p>

		</footer>
	</div>

</body>
</html>
