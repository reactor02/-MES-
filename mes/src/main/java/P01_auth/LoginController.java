package P01_auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/login doget 실행");
		// TODO Auto-generated method stub

		// 주소 : http://localhost:8080/mes/login.jsp

		// 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=UTF-8");
		
		
		// 함수 모음집 소환
		LoginService s = new LoginService();
		// 데이터 바구니 소환
		LoginDTO d = new LoginDTO();

		// 전화번호. 숫자 21억 넘어서 long으로 저장.
		long phone = 0;
	
		
		//로그인으로
		request.getRequestDispatcher("/WEB-INF/views/P01_auth/login.jsp").forward(request, response);
			
			

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// TODO Auto-generated method stub
		System.out.println("/login dopost 실행");

				// 주소 : http://localhost:8080/mes/login.jsp

				// 한글깨짐 방지
				request.setCharacterEncoding("UTF-8");
//				response.setContentType("text/html; charset=UTF-8");
				// 함수 모음집 소환
				LoginService s = new LoginService();
				// 데이터 바구니 소환
				LoginDTO d = new LoginDTO();

				// 전화번호. 숫자 21억 넘어서 long으로 저장.
				long phone = 0;
				System.out.println("request.getContextPath() : " + request.getContextPath());

				// 이 값이 있다면 로그인
				String id = request.getParameter("login_id");
				String pw = request.getParameter("login_pw");

				System.out.println("/login doget.login 실행");
				
				//비밀번호 SHA-256 암호화
				System.out.println("암호화 비밀번호 확인 : "+s.encrypt(pw));

				// 값 저장
				d.setEmpid(id);
				d.setPassword(s.encrypt(pw));

				// 로그인 함수 소환후 함수 작동결과를 변수에 담기
				List<LoginDTO> list = s.login(d);

				// 로그인 함수 작동 결과 값이 있다면
				if (list.size() == 1) {
					System.out.println("로그인 성공");

					// 세션 소환
					HttpSession session = request.getSession();

					session.setAttribute("dto", list.get(0));
					session.setAttribute("login", "true");
					session.setAttribute("auth", list.get(0).getAuth());
//					response.sendRedirect(request.getContextPath() + "/WEB-INF/views/P01_auth/mypage.jsp");
					response.sendRedirect(request.getContextPath() + "/dashboard");
					return;

					// 아이디와 패스워드가 없다면. 로그인 페이지로. 
				} else if( ( id == null ) && ( pw == null ) ) {
					
					System.out.println("login 페지이에 오신것을 환영합니다!");
					
					request.setAttribute("error", "로그인 페이지에 오신것을 환영합니다!");
					
					request.getRequestDispatcher("/WEB-INF/views/P01_auth/login.jsp").forward(request, response);
					
					
					// 없다면 로그인 실패 메세지와 함께 로그인 페이지로.
				} else {
					System.out.println("로그인 실패");
					request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
					request.setAttribute("empid", id);
					request.getRequestDispatcher("/WEB-INF/views/P01_auth/login.jsp").forward(request, response);
					return;
				}


	}

}
