<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equive="Content-Type" charset="text/html' charset=UTF-8">
<title>JSP 파일 업로드</title>
</head>
<body>
	<form action="uploadAction2.jsp" method="post" enctype="multipart/form-data">
		파일 : <input type="file" name="file" />
		<br />
		<button type="sunmit">업로드</button>
	</form>
	<a href="fileDownload.jsp">파일 다운로드페이지</a>
</body>
</html>