<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업 & 다운로드</title>
</head>
<body>
	<form action="Upload" method="post" enctype="multipart/form-data">
		<label>
			파일:&nbsp;<input type="file" name="file" />
		</label>
		<button type="submit">전송</button>
	</form>
</body>
</html>