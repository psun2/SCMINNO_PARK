<%@page import="java.net.URLEncoder"%>
<%@page import="DTO.FileDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업 & 다운로드</title>
</head>
<body>
	<h1>다운로드 페이지</h1>
	<button onclick="allDownload();">전체 다운로드(압축)</button>
	<div id="box">
	<%
		ArrayList<FileDTO> fileList = (ArrayList) request.getAttribute("fileList");
		
		if(fileList != null && fileList.size() > 0) { // 업로드 하고 페이지가 자동으로 넘어온 경우
			for(FileDTO file : fileList) {
				String outputStr = "";
				if(file.getFileName().toLowerCase().endsWith("pdf")) {
					outputStr =  "<a href=\"/download?file=" 
							// 파일명이 한글이라면 QueryString 즉 get방식으로 보낼때
							// encoding 되지 않아 에러를 발생시킵니다.  
							// 이를 방지하기 위해 java.net 패키지에 있는 URLEncoder 클래스를 사용해서 
							// URL로 인코딩 하여 서버에 보낼 수 있도록 합니다.
							+ URLEncoder.encode(file.getFileName(), "UTF-8") + "\">"
							+ "<span>"
							+ file.getFileRealName() 
							+ "</span>"
							+ "<span>&nbsp;"
							+ "다운로드 횟수: "
							+ file.getDownCount()
							+ " 회"
							+ "</span>"
							+ "<span>&nbsp;"
							+ "파일크기: "
							+ file.getFileSize()
							+ "byte"
							+ "</span>"
							+ "</a>"
							+ "&nbsp;"
							/*
							+ "<button onclick=\"location.href='/pdfView?fileNam="
							+ URLEncoder.encode(file.getFileName(), "UTF-8")
							+ "&fileRealName="
							+ URLEncoder.encode(file.getFileRealName(), "UTF-8")
							+ "'\">PDF 보기</button>"
							*/
							+ "<button onclick=\"handlePdf('"
							+ file.getFileRealName()
							+ "', '"
							+ file.getFileRealName()
							+ "'"
							+");\">PDF 보기</button>"
							+ "<br />";
				} else {
					outputStr =  "<a href=\"/download?file=" 
							// 파일명이 한글이라면 QueryString 즉 get방식으로 보낼때
							// encoding 되지 않아 에러를 발생시킵니다.  
							// 이를 방지하기 위해 java.net 패키지에 있는 URLEncoder 클래스를 사용해서 
							// URL로 인코딩 하여 서버에 보낼 수 있도록 합니다.
							+ URLEncoder.encode(file.getFileName(), "UTF-8") + "\">"
							+ "<span>"
							+ file.getFileRealName() 
							+ "</span>"
							+ "<span>&nbsp;"
							+ "다운로드 횟수: "
							+ file.getDownCount()
							+ " 회"
							+ "</span>"
							+ "<span>&nbsp;"
							+ "파일크기: "
							+ file.getFileSize()
							+ "byte"
							+ "</span>"
							+ "</a>"
							+ "<br />";
				}
				out.write(outputStr);
			}			
		} else { // downloadList.jsp 로 페이지에 접근한 경우
			response.sendRedirect("/fileList"); // list를 반환하는 servlet에 접근 후 redirection
		}
	%>
	</div>
	<script>
		function handlePdf(fileName, fileRealName) {
			window.fileName = fileName;
			window.fileRealName = fileRealName;
			window.open('/pdfViewer.jsp', '_BLANK');
		} // end handlePdf()
		
		function allDownload() {
			let boxAList = Array.from(document.querySelectorAll('#box a'));
			
			let fileNamesStr = '';
			let fileNamesArr = [];
			for(var i = 0; i < boxAList.length; i++) {
				let fileRealName = boxAList[i].firstChild.innerText;
				
				fileNamesArr.push(fileRealName);
				fileNamesStr += fileRealName;
				if(i < boxAList.length - 1) fileNamesStr += ',';
			}
			
			let data = {FILENAMESSTR: fileNamesStr, FILEREALNAMESARR: fileNamesArr};
			
			let url = '/createMultiDownList';
			let fetchOption = {method: 'post'};
			fetchOption.body = JSON.stringify(data);
			
			fetch(url, fetchOption) //
			.then((response) => {
				console.log(response);
			}) //
			.then((json) => {
				
			}) //
			.catch((e) => {
				console.error(e);
			})
		} // end allDownload()
	</script>
</body>
</html>