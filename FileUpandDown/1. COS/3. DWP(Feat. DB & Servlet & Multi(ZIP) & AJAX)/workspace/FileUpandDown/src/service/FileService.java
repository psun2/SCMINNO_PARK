package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import DAO.FileDAO;
import DTO.FileDTO;
import util.ZipUtilCustom;

public class FileService {
	
	FileDAO dao;
	FileDTO dto;
	
	public FileService() {
		super();
		dao = new FileDAO();
		dto = new FileDTO();
	}
	
	// 업로드
	public void upload(HttpServletRequest request) {
		
		System.out.println("Service Path: " + request.getServletContext().getRealPath("/"));
		// 실제 서버의 물리적 경로
		// D:\park\SCMINNO_PARK\FileUpandDown\1. COS\3. DWP(Feat. DB & Servlet & Multi(ZIP) & AJAX)\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpandDown\
		
		String direction = request.getServletContext().getRealPath("upload"); // 서버내 실제 경로의 upload폴더를 지칭합니다.
		// String direction = "서버컴퓨터의 local의 사용자 임의 업로드 폴더 경로 설정 하여 보안을 적용 할 수 있습니다.";
		int fileMaxSize = 1024 * 1024 * 100; // 100MB
		String enCoding = "UTF-8";
		
		// 서버의 해당 디렉토리에 이미 파일 업로드가 끝난 상태(MultipartRequest 객체 생성시)
		try {
			MultipartRequest multipartRequest = 
					new MultipartRequest(request, direction, fileMaxSize, enCoding, new DefaultFileRenamePolicy());
			
			// 반복문을 통하여 db insert
			Enumeration<String> enumerration = multipartRequest.getFileNames();
			
			while(enumerration.hasMoreElements()) {
				String parameterName = (String) enumerration.nextElement();
				
				// 중복된 이름이 있다면 DefaultFileRenamePolicy 객체를 통해 변경된 이름 획득
				String fileName = multipartRequest.getFilesystemName(parameterName); // file => input type="file" 의 name 값
				
				if(fileName != null) {
					// 보안코딩 추가
					if(fileName.endsWith(".jsp") || fileName.endsWith(".js")) {
						File file = multipartRequest.getFile(parameterName);
						file.delete();
						continue;
					}
					
					// 사용자가 업로드한 진짜 파일명
					String fileRealName = multipartRequest.getOriginalFileName(parameterName); // file => input type="file" 의 name 값
					String extention = fileRealName.substring(fileRealName.lastIndexOf(".") + 1);
					long fileSize = multipartRequest.getFile(parameterName).length();
					
					// DB Insert
					dao.upload(fileName, fileRealName, extention, fileSize);
				}
			}
			// 다운로드 list 페이지 이동을 위한 database SELECT
			ArrayList<FileDTO> fileList = dao.getUploadList();
			request.setAttribute("fileList", fileList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // end upload()
	
	// 다운로드 list
	public ArrayList<FileDTO> getFileList() {
		return dao.getUploadList();
	} // end getFileList()
	
	// 다운로드 서비스
	public void download(HttpServletRequest request, HttpServletResponse response) {
		try {
			// downloadList.jsp에서 a태그의 주소를 살펴보면
			// <a href="/download?file=%ED%8C%8C%EC%9D%BC%EC%97%85%EB%A1%9C%EB%93%9C+TEST.txt"><span>파일업로드 TEST.txt</span><span>&nbsp;다운로드 횟수: 0 회</span><span>&nbsp;파일크기: 37byte</span></a>
			// 인코딩 된 파일명으로 file이란 이름으로 보내주고 있습니다. 보내주는 파라미터를 받아 보도록 하겠습니다.
			String fileName = request.getParameter("file"); // 서버 업로드 폴더에 업로드 DefaultFileRenamePolicy 로 변경된 이름을 받아 옵니다.
			
			// 인코딩 된 파일명을 받았으니 디코딩을 시켜 파일명을 복호화 합니다.
			fileName = URLDecoder.decode(fileName, "UTF-8");
			
			// file명은 위를 통해 취득했으니 이제 업로드된 서버의 실제 경로를 설정합니다.  
			String direction = request.getServletContext().getRealPath("/");
			// String direction = "서버컴퓨터의 local의 사용자 임의 업로드 폴더 경로 설정 하여 보안을 적용 할 수 있습니다.";
			
			// 서버에 업로드된 실제 경로와 파일명을 + 하여 조합합니다.
			StringBuffer sb = new StringBuffer();
			sb.append(direction);
			sb.append("upload");
			sb.append(File.separator);
			sb.append(fileName);
			String filePath = sb.toString();
			
			File file = new File(filePath);
			
			// 어떤한 정보를 주고 받는지에 대한 response(응답)에 대한 contentType setting
			String mimeType = request.getServletContext().getMimeType(file.toString());
			if(mimeType == null) {
				// application/octet-seream => 파일관련 정보를 주고 받는 ContentType
				response.setContentType("application/octet-seream");
			}
			
			// 파일은 실제 서버의 물리적 경로에 업로드된 파일을 사용하되,
			// 파일명은 사용자가 실제 올린 파일명으로 다운로드가 진행되어야 하기 때문에
			// DB를 조회해서 실제 파일명을 SELECT 합니다.
			String fileRealName = dao.getFileRealName(fileName);
			
			// 사용자에게 파일을 다운로드 받을시 다시 파일명을 해당 플랫폼에 따라 인코딩 시켜줄수 있도록 encoding을 설정합니다.
			// 여기서 플랫폼이란 IE, 엣지, 크롬 등을 의미 합니다.
			String downloadName = null;
			// 요청온 해더의 유저정보에서 확일 할 수 있습니다.
			if(request.getHeader("user-agent").indexOf("MISE") != -1) { // MSIE = IE
				// -1 이 아니라면... 즉, 익스플로러가 아니라면
				downloadName = new String(fileRealName.getBytes("UTF-8"), "8859_1"); // 8859_1 : encoding 방식의 한 종류
			} else { // 익스플로러 라면....
				downloadName = new String(fileRealName.getBytes("EUC-KR"), "8859_1"); // 8859_1 : encoding 방식의 한 종류
			}
			
			// response(응답)에 대한 header setting
			sb = new StringBuffer();
			sb.append("attachment;filename=\"");
			sb.append(downloadName);
			sb.append("\"");
			String contentDisposition = sb.toString();
			response.setHeader("Content-Disposition", contentDisposition);
			
			// 실제로 Stream을 사용하며 사용자 컴퓨터에 다운로드
			FileInputStream fis = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			
			// Stream은 byte단위로 1024byte로 쪼개서 보낼 수 있도록 합니다.
			byte[] tempFile = new byte[1024];
			int data = 0;
			while((data = fis.read(tempFile, 0, tempFile.length)) != -1) {
				os.write(tempFile, 0, data);
			}

			// Stream 종료
			os.flush();
			os.close();
			fis.close();
			
			// 다운로드가 끝난뒤 다운로드 횟수 증가
			dao.downIncrease(fileName);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // download()
	
	// pdf file view 
	public void pdfViewer(HttpServletRequest request, HttpServletResponse response) {
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		try {
			String fileName = request.getParameter("fileRealName"); // 서버 업로드 폴더에 업로드 DefaultFileRenamePolicy 로 변경된 이름을 받아 옵니다.
			
			// 인코딩 된 파일명을 받았으니 디코딩을 시켜 파일명을 복호화 합니다.
			fileName = URLDecoder.decode(fileName, "UTF-8");
			
			// file명은 위를 통해 취득했으니 이제 업로드된 서버의 실제 경로를 설정합니다.  
			String direction = request.getServletContext().getRealPath("/");
			// String direction = "서버컴퓨터의 local의 사용자 임의 업로드 폴더 경로 설정 하여 보안을 적용 할 수 있습니다.";
			
			// 서버에 업로드된 실제 경로와 파일명을 + 하여 조합합니다.
			StringBuffer sb = new StringBuffer();
			sb.append(direction);
			sb.append("upload");
			sb.append(File.separator);
			sb.append(fileName);
			String filePath = sb.toString();
			
			File file = new File(filePath);
			
			// pdf 파일을 보여 주기위한 response setting
			response.setContentType("application/pdf");
			response.setHeader("Content-Description", "JSP Generated Data");
			
			response.flushBuffer();
			
			fileInputStream = new FileInputStream(file);
			outputStream = response.getOutputStream();
			
			int read = -1;
			byte[] buffer = new byte[1024];
			while((read = fileInputStream.read(buffer, 0, buffer.length)) >= 0) {
				outputStream.write(buffer, 0, buffer.length);
			}
			outputStream.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end pdfViewer()
	
	// 다중 다운로드 전 GET방식으로 보내는 다운로드 특성상 URL의 길이가 초과 될 수 있는 에러 발생 확률이 있음
	// 다운로드 실행전 미리 파일명들을 DB로 보내 insert
	public void createMultiDownInsert(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader bufferedReader =  request.getReader();
			String readLine = null;
			StringBuilder json = new StringBuilder();
			while((readLine = bufferedReader.readLine()) != null) {
				json.append(URLDecoder.decode(readLine, "UTF-8"));
			}
			
			// Jackson lib 사용
			Map<String, Object> params = new ObjectMapper().readValue(json.toString(), HashMap.class);
			
			String fileNames = (String) params.get("fileNamesStr");
			String seq = dao.mutilFileInsert(fileNames);
			response.setContentType("application/json");
	   		response.setCharacterEncoding("UTF-8");
	   		
	   		Map<String, String> result = new HashMap<String, String>();
	   		String resultJson = "";
			if(seq != null && !seq.equals("")) {
				// PK인 시퀀스 반환
				result.put("SEQ", seq);
				
				
				// jackson 라이브 러리 이용
				result.put("STATUS", "SUCCESS");
				resultJson = new ObjectMapper().writeValueAsString(result);
			} else {
				// jackson 라이브 러리 이용
				result.put("STATUS", "FAIL");
				result.put("MESSAGE", "파일명을 받을 수 없습니다.");	
				resultJson = new ObjectMapper().writeValueAsString(result);
			}
			response.getWriter().write(resultJson);		
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end createMultiDownInsert()
	
	// 다중 다운로드 전 GET방식으로 보내는 다운로드 특성상 URL의 길이가 초과 될 수 있는 에러 발생 확률이 있음
	// 이를 방지하기 위하여 GET방식으로 보낼 param을 post방식으로 미리 TEMP table에 insert 합니다
	public void createMultiDownload(HttpServletRequest request, HttpServletResponse response) {
		try {			
			// file명은 위를 통해 취득했으니 이제 업로드된 서버의 실제 경로를 설정합니다.  
			String direction = request.getServletContext().getRealPath("/");
			// String direction = "서버컴퓨터의 local의 사용자 임의 업로드 폴더 경로 설정 하여 보안을 적용 할 수 있습니다.";
			direction = direction + "upload/";
			
			ZipUtilCustom zip = new ZipUtilCustom(request);
			/*
			for(String fileName : fileNameArr) {
				// String fileRealName = dao.getFileRealName(URLDecoder.decode(fileName, "UTF-8"));
				String fileRealName = URLDecoder.decode(fileName, "UTF-8");
				File file = new File(direction + fileRealName);
				zip.compare(file);
			}
			*/
			
			String seq = request.getParameter("SEQ");
			String fileNames = dao.getDownloadFileNames(seq);
			String[] fileNameArr = fileNames.split(",");
			for(String fileName : fileNameArr) {
				// String fileRealName = dao.getFileRealName(URLDecoder.decode(fileName, "UTF-8"));
				String fileRealName = URLDecoder.decode(fileName, "UTF-8");
				File file = new File(direction + fileRealName);
				zip.compare(file);
			}
			zip.close();
			
			File downLoadZip = zip.getDownLoadZipFile();
			String downLoadZipName = zip.getZipFileName();
			
			// 사용자에게 파일을 다운로드 받을시 다시 파일명을 해당 플랫폼에 따라 인코딩 시켜줄수 있도록 encoding을 설정합니다.
			// 여기서 플랫폼이란 IE, 엣지, 크롬 등을 의미 합니다.
			String downloadName = null;
			// 요청온 해더의 유저정보에서 확일 할 수 있습니다.
			if(request.getHeader("user-agent").indexOf("MISE") != -1) { // MSIE = IE
				// -1 이 아니라면... 즉, 익스플로러가 아니라면
				downloadName = new String(downLoadZipName.getBytes("UTF-8"), "8859_1"); // 8859_1 : encoding 방식의 한 종류
			} else { // 익스플로러 라면....
				downloadName = new String(downLoadZipName.getBytes("EUC-KR"), "8859_1"); // 8859_1 : encoding 방식의 한 종류
			}
			
			// response(응답)에 대한 header setting
			StringBuffer sb = new StringBuffer();
			sb.append("attachment;filename=\"");
			sb.append(downloadName);
			sb.append("\"");
			String contentDisposition = sb.toString();
			response.setHeader("Content-Disposition", contentDisposition);
			
			// 실제로 Stream을 사용하며 사용자 컴퓨터에 다운로드
			FileInputStream fis = new FileInputStream(downLoadZip);
			OutputStream os = response.getOutputStream();
			
			// Stream은 byte단위로 1024byte로 쪼개서 보낼 수 있도록 합니다.
			byte[] tempFile = new byte[1024];
			int data = 0;
			while((data = fis.read(tempFile, 0, tempFile.length)) != -1) {
				os.write(tempFile, 0, data);
			}

			// Stream 종료
			os.flush();
			// os.close();
			// fis.close();
			
			// 다운로드가 끝난뒤 다운로드 횟수 증가
			/*
			for(String fileName : fileNameArr) {
				dao.downIncrease(fileName);
			}
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end createMultiDownload()
	
	private String[] push(String[] arr, String param) {
		String[] temp = new String[arr.length + 1];
		try {
			if(arr.length > 0) {
				for(int i = 0 ; i < arr.length; i++) {
					temp[i] = arr[i];
				}
			}
			temp[temp.length - 1] = param;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return temp;
	} // end push()
}
