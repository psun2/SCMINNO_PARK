<%@page import="java.io.File"%>

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
		String files[] = new File(directory).list();
		
		System.out.println(request.getContextPath());
		
		for(String file: files) {
			// out.write("<a href=\"" + request.getContextPath() + "\"" + "/downloadAction?file=" + java.net.URLEncoder.encode(file, "UTF-8")  + " />" + file + "</a><br />");
			out.write("<a href=\"" + "/downloadAction?file=" + java.net.URLEncoder.encode(file, "UTF-8")  + "\" />" + file + "</a><br />");
		}
	%>
</body>
</html>