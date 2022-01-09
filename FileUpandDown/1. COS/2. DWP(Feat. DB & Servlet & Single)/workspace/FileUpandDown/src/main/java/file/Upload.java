package file;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.FileService;


@WebServlet("/Upload") // 서버에서 받는 주소 Spring의 @RequestMapping 즉 Controller의 역할을 수행합니다.
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Upload() {
        super();
        // TODO Auto-generated constructor stub
    }

    // get방식
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// post방식
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet Path: " + this.getServletContext().getRealPath("/")); // this => HttpServlet 객체를 의미
		// 실제 서버의 물리적 경로
		// C:\Users\parksungun\Desktop\test\1. COS\1. Dynamic Web Project(Feat. SERVER & Servlet & Single)\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpandDown\
		
		System.out.println("Request Path: " + request.getServletContext().getRealPath("/"));
		// 실제 서버의 물리적 경로
		// C:\Users\parksungun\Desktop\test\1. COS\1. Dynamic Web Project(Feat. SERVER & Servlet & Single)\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpandDown\
		new FileService().upload(request);
		
		// 업로드 후 다운로드 페이지로 이동하여 다운로드 List를 뿌려줍니다.
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/downloadList.jsp");
		requestDispatcher.forward(request, response);
	}
}
