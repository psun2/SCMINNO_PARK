package service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import file.FileDAO;
import file.FileDTO;

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
		// C:\Users\parksungun\Desktop\test\1. COS\1. Dynamic Web Project(Feat. SERVER & Servlet & Single)\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpandDown\
		
		String direction = request.getServletContext().getRealPath("/upload/"); // 서버내 실제 경로의 upload폴더를 지칭합니다.
		// String direction = "서버컴퓨터의 local의 사용자 임의 업로드 폴더 경로 설정 하여 보안을 적용 할 수 있습니다.";
		int fileMaxSize = 1024 * 1024 * 100; // 100MB
		String enCoding = "UTF-8";
		
		try {
			// 서버의 local 환경에 있는 upload 파일 전체 삭제
			// upload후 삭제하려 했지만, 파일이 사용 중엔 삭제가 되지 않아 upload 시작시 이전 upload 파일 삭제
			File[] files = new File(direction).listFiles();
			for(File f : files) {
				if(f.exists()) {
					if(f.isFile()) {
						f.delete();
					}
				}
			}
		
			// 서버의 해당 디렉토리에 이미 파일 업로드가 끝난 상태(MultipartRequest 객체 생성시)
			MultipartRequest multipartRequest = 
					new MultipartRequest(request, direction, fileMaxSize, enCoding, new DefaultFileRenamePolicy());
			
			// 중복된 이름이 있다면 DefaultFileRenamePolicy 객체를 통해 변경된 이름 획득
			String fileName = multipartRequest.getFilesystemName("file"); // file => input type="file" 의 name 값
			
			// 보안코딩 추가
			if(fileName.endsWith(".jsp") || fileName.endsWith(".js")) {
				File file = multipartRequest.getFile("file");
				file.delete();
				return;
			}
			
			// 사용자가 업로드한 진짜 파일명
			String fileRealName = multipartRequest.getOriginalFileName("file"); // file => input type="file" 의 name 값
			String extention = fileRealName.substring(fileRealName.lastIndexOf(".") + 1);
			long fileSize = multipartRequest.getFile("file").length();
			
			// DB Insert를 위한 file 의 byte[]
			File file = multipartRequest.getFile("file");
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] fileByteArray = fileInputStream.readAllBytes(); // Stream을 byte[]로 DB에 insert
			
			// DB Insert
			dao.upload(fileName, fileRealName, fileByteArray, extention, fileSize);
			
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
			String seqStr = request.getParameter("seq");
			int seq = !seqStr.equals("") && seqStr != null ? Integer.parseInt(seqStr) : 0;
			
			// 인코딩 된 파일명을 받았으니 디코딩을 시켜 파일명을 복호화 합니다.
			fileName = URLDecoder.decode(fileName, "UTF-8");
			
			// 파일명은 사용자가 실제 올린 파일명으로 다운로드가 진행되어야 하기 때문에
			// DB를 조회해서 실제 파일명과 FileInputStream 을 SELECT 합니다.
			Map<String, Object> fileMap = dao.getFile(fileName, seq);
			String fileRealName = (String) fileMap.get("FILEREALNAME");
			byte[] fileInputStreamByteArr = (byte[]) fileMap.get("FILESTREAM");
			
			// 어떤한 정보를 주고 받는지에 대한 response(응답)에 대한 contentType setting
			String mimeType = response.getContentType();
			if(mimeType == null || !mimeType.equalsIgnoreCase("application/octet-stream")) {
				response.setContentType("application/octet-stream");
			}
			
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
			StringBuffer sb = new StringBuffer();
			sb.append("attachment;filename=\"");
			sb.append(downloadName);
			sb.append("\"");
			String contentDisposition = sb.toString();
			response.setHeader("Content-Disposition", contentDisposition);
			
			InputStream is = new ByteArrayInputStream(fileInputStreamByteArr);
			OutputStream os = response.getOutputStream();			
			// Stream은 byte단위로 1024byte로 쪼개서 보낼 수 있도록 합니다.
			byte[] tempFile = new byte[1024];
			int data = 0;
			while((data = is.read(tempFile, 0, tempFile.length)) != -1) {
				os.write(tempFile, 0, data);
			}

			// Stream 종료
			os.flush();
			os.close();
			is.close();
			
			// 다운로드가 끝난뒤 다운로드 횟수 증가
			dao.downIncrease(fileName, seq);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // download()
}
