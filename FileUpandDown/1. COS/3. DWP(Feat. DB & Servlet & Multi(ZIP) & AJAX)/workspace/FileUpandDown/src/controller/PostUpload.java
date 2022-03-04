package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.FileService;

@WebServlet("/postUpload")
public class PostUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public PostUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet Path: " + this.getServletContext().getRealPath("/")); // this => HttpServlet 객체를 의미
		// 실제 서버의 물리적 경로
		// D:\park\SCMINNO_PARK\FileUpandDown\1. COS\3. DWP(Feat. DB & Servlet & Multi(ZIP) & AJAX)\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpandDown\
		
		System.out.println("Request Path: " + request.getServletContext().getRealPath("/"));
		// 실제 서버의 물리적 경로
		// D:\park\SCMINNO_PARK\FileUpandDown\1. COS\3. DWP(Feat. DB & Servlet & Multi(ZIP) & AJAX)\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpandDown\
		new FileService().upload(request);
		
		// 업로드 후 JSON 형식으로 내보내 줍니다.
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    StringBuilder json = new StringBuilder();
	    json.append("{\"STATUS\":\"SUCCESS\",\"FORWARD\":\"/downloadList.jsp\"}");
	    System.out.println(json.toString());
	    response.getWriter().write(json.toString());
	}

}
