package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTO.FileDTO;
import service.FileService;

/**
 * Servlet implementation class FileList
 */
@WebServlet("/fileList")
public class FileList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ArrayList<FileDTO> fileList = new FileService().getFileList();
		RequestDispatcher requestDispatcher = null;
		
		// 조회된 업로드 파일이 있는경우 downloadList.jsp에서 볼 수 있도록 하며, 다운로드 할 수 있도록 구현합니다.
		if(fileList.size() > 0) {
			request.setAttribute("fileList", fileList);
			
			requestDispatcher = request.getRequestDispatcher("/downloadList.jsp");
		} else { // 조회된 업로드 파일이 없는경우 index.jsp에서 파일을 업로드 할 수 있도록 업로드폼으로 이동합니다.
			requestDispatcher = request.getRequestDispatcher("/index.jsp");
		}
		
		if(requestDispatcher != null) requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
