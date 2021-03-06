package file;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.FileService;

@WebServlet("/downloadList")
public class DownloadList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DownloadList() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get");
		
		ArrayList<FileDTO> fileList = new FileService().getFileList();
		RequestDispatcher requestDispatcher = null;
		
		if(fileList != null && fileList.size() >0) {
			request.setAttribute("fileList", fileList);
			
			requestDispatcher = request.getRequestDispatcher("/downloadList.jsp");
		} else {
			// 목록이 없을 경우 index.jsp 로 forwording 시켜 업로드를 할 수 있도록 진행 합니다.
			requestDispatcher = request.getRequestDispatcher("/index.jsp"); 
		}
		
		if(requestDispatcher != null) requestDispatcher.forward(request, response);			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("post");
	}
}
