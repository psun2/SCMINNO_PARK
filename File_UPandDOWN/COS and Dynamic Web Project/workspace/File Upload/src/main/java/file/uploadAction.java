package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/uploadAction")
public class uploadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public uploadAction() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost");
		
		System.out.println(request.getParameter("file")); 
		System.out.println(request.getParameter("hidden")); 
		System.out.println(request.getParameter("hidden2")); 
		
		System.out.println(request.getContentLength());
		System.out.println(request.getContentLengthLong());
		
		System.out.println(request.getQueryString()); // null
		
		// Application 내장 객체는 전체 프로젝트에 대한 자원을 관리하는 객체입니다.  
		// 서버의 실제 프로젝트 경로에서 자원을 찾을 때 가장 많이 사용합니다.
		// String directory = application.getRealPath("/uploadFileForder/");
		String directory = "D:\\\\File_UPandDOWN\\COS and Dynamic Web Project\\workspace\\File Upload\\src\\main\\webapp\\uploadFileForder";
		int maxSize = 1024 * 1024 * 100;
		// int maxSize = 1 * 1 * 1;
		String encoding = "UTF-8";
		
		MultipartRequest multipartRequest = new MultipartRequest(request, directory, maxSize, encoding, new DefaultFileRenamePolicy());
		
		String fileName = multipartRequest.getOriginalFileName("file");
		String fileRealName = multipartRequest.getFilesystemName("file");
		
		System.out.println(fileName);
		System.out.println(fileRealName);
		
		request.setAttribute("fileName", fileName);
		request.setAttribute("fileRealName", fileRealName);
		
		if(new File(directory + "\\" + fileName).exists()) {
			System.out.println("[1]" + fileName + " 은 존재 하는 file 입니다.");
		}
		
		new FileDAO().upload(fileName, fileRealName);
		
		// RequestDispatcher rd = request.getRequestDispatcher("이동할 페이지");
		// rd.forward(request, response);
		// 이렇게 하면 페이지 주소는 바뀌지 않고 내용만 바뀐다.
		// 페이지가 바뀌어도  reqeust는 바뀌지 않으므로
		// request에 attribute를 설정해서 이동할 페이지로 값을 가져갈 수 있다.
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/uploadAction.jsp");
		requestDispatcher.forward(request, response);
		
		// response.sendRedirect("이동할 페이지");
		// 페이지 주소도 바뀌고 request도 바뀌게 되므로  reqeust의 attribute는 사용할 수 없다.
		// response.sendRedirect("/uploadAction.jsp");
		
	}

}
