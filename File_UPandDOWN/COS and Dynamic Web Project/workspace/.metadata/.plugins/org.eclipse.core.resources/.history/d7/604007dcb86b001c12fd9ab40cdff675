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
		// Application 내장 객체는 전체 프로젝트에 대한 자원을 관리하는 객체입니다.  
		// 서버의 실제 프로젝트 경로에서 자원을 찾을 때 가장 많이 사용합니다.
		// String directory = application.getRealPath("/uploadFileForder/");
		String directory = "D:\\\\File_UPandDOWN\\COS and Dynamic Web Project\\workspace\\File Upload\\src\\main\\webapp\\uploadFileForder";
		int maxSize = 1024 * 1024 *100;
		String encoding = "UTF-8";
		
		MultipartRequest multipartRequest = new MultipartRequest(request, directory, maxSize, encoding, new DefaultFileRenamePolicy());
		
		String fileName = multipartRequest.getOriginalFileName("file");
		String fileRealName = multipartRequest.getFilesystemName("file");
		
		new FileDAO().upload(fileName, fileRealName);
		out.write("파일명 : " + fileName + "<br />");
		out.write("실제 파일명 : " + fileRealName + "<br />");
	%>
</body>
</html>