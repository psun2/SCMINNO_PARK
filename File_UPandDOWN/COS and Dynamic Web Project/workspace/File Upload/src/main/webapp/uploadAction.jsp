<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="file.FileDAO"%>
<%@ page import="file.FileDTO" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equive="Content-Type" charset="text/html' charset=UTF-8">
<title>JSP 파일 업로드</title>
</head>
<body>
	<%
		String fileName = (String) request.getAttribute("fileName");
		String fileRealName = (String) request.getAttribute("fileRealName");
		out.write("파일명 : " + fileName + "<br />");
		out.write("실제 파일명 : " + fileRealName + "<br />");
	%>
</body>
</html>