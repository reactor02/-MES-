package P01_auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/join")
public class JoinController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/join doget 실행");
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
		
		
		List<LoginDTO> selectd = s.selectd();
		List<LoginDTO> selectm = s.selectm();
		
		
		request.setAttribute("selectd", selectd);
		request.setAttribute("selectm", selectm);

		// 회원가입으로
		request.getRequestDispatcher("/WEB-INF/views/P01_auth/join.jsp").forward(request, response);
		return;

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("/join dopost 실행");

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

		// 이 값들이 있다면 회원가입
		String join_name = request.getParameter("join_name");
		String join_phone = request.getParameter("join_phone");
		String join_dept = request.getParameter("join_dept");
		String join_license = request.getParameter("join_license");
		String join_mgr = request.getParameter("join_mgr");
		String join_pw = request.getParameter("join_pw");
		String join_pw2 = request.getParameter("join_pw2");

		if (join_phone != null) {
			phone = Integer.parseInt(join_phone);
		}

		// 회원가입 로직
		if (join_name != null && join_pw != null && join_pw2 != null && join_mgr != null) {
			System.out.println("/login doget.join 실행");

			// 비밀번호 SHA-256 암호화
			System.out.println("암호화 비밀번호 확인 : " + s.encrypt(join_pw));

			d.setEname(join_name);
			d.setPhone(phone);
			d.setDeptno(join_dept);
			d.setMgr(join_mgr);
			d.setLicense(join_license);
			d.setPassword(s.encrypt(join_pw));
			d.setPassword2(join_pw2);

			List list = s.join(d);

			// 여서 에러남. if( list.get(0) > 0)했는데 안되었음. 원인은 리스트에 다른
			// 형태데이터 넣어서 안됨. 해결책은 앞에 클래스 명을 붙여 형변환 후 사용하는 것.
			// 그냥은 Object 처리 되어서 숫자 비교가 안되는 거였음.
			if (!list.isEmpty() && (int) list.get(0) > 0) {
				System.out.println("회원가입이 완료되었습니다!");

				// 값 들려서 그? 다른 창 소환.
				request.setAttribute("list", (LoginDTO) list.get(1));
				request.getRequestDispatcher("/WEB-INF/views/P01_auth/joinResult.jsp").forward(request, response);
				return;

			} else {
				System.out.println("회원가입에 실패했습니다. 값을 모두 입력해주세요.");

				// 에러값과 함께 회원가입 페이지로 추방
				request.setAttribute("error", "회원가입에 실패했습니다. 다시.");
				request.getRequestDispatcher("/WEB-INF/views/P01_auth/join.jsp").forward(request, response);
				return;
			}

		}

	}

}
