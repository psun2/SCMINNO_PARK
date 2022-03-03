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
			파일:&nbsp;<input type="file" name="file0" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file1" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file2" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file3" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file4" />
		</label>
		<br />
		<button type="submit">submit 전송</button>
	</form>
	<button type="button">ajax 전송</button>
</body>
</html>