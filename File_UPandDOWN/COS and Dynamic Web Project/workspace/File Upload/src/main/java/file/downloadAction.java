package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/downloadAction")
public class downloadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public downloadAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
		
		String fileName = request.getParameter("file");
		
		// String directory = this.getServletContext().getRealPath("/upload/");
		// D:\\File_UPandDOWN\\COS and Dynamic Web Project\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\File Upload\\upload\\1.sql
		
		String directory =  "D:\\\\File_UPandDOWN\\COS and Dynamic Web Project\\workspace\\File Upload\\src\\main\\webapp\\uploadFileForder";
		File file = new File(directory + "/" + fileName);
		
		String mimeType = getServletContext().getMimeType(file.toString()); // 어떠한 데이터를 주고 받는지에 대한 정보
		if(mimeType == null) {
			response.setContentType("application/octet-stream"); // 파일 관련 데이터 를 주고 받을때 사용 하는 contentType
		}
		
		String downloadName = null;
		if(request.getHeader("user-agent").indexOf("MSIE") == -1) { // IE 를 제외한 그 외 플렛폼
			downloadName = new String(fileName.getBytes("UTF-8"), "8859_1"); // fileName을 byte로 변경하여, 8859_1 : encoding 방식의 한 종류
		} else { // IE
			downloadName = new String(fileName.getBytes("EUC-KR"), "8859_1");
		}
		
		System.out.println(downloadName);
		
		response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadName + "\";");
		
		// 실제로 response 패킷에 전달할 파일 데이터를 담아 줍니다.
		FileInputStream fileInputStream = new FileInputStream(file);
		ServletOutputStream outputStream = response.getOutputStream();
		
		// 한번에 1024byte 단위로 쪼개서 보내줍니다
		byte[] tempFile = new byte[1024];
		int data = 0;
		
		while((data = (fileInputStream.read(tempFile, 0, tempFile.length))) != -1) {
			System.out.println(Arrays.toString(tempFile));
			System.out.println(tempFile.length);
			System.out.println(data);
			
			outputStream.write(tempFile, 0, data);
		}
		
		outputStream.flush();
		outputStream.close();
		fileInputStream.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost");
		
	}

}
