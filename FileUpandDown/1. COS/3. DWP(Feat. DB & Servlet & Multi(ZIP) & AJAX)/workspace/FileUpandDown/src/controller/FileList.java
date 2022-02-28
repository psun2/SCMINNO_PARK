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
		
		if(fileList.size() > 0) {
			request.setAttribute("fileList", fileList);
			
			requestDispatcher = request.getRequestDispatcher("/downloadList.jsp");
		} else {
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
