package P01_auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/pdetail")
public class PermissionDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("permission의 doget 실행");
		// TODO Auto-generated method stub

		// 주소 : http://localhost:8080/mes/login.jsp

		// 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=UTF-8");
		// 함수 모음집 소환
		LoginService s = new LoginService();
		// 데이터 바구니 소환
		LoginDTO d = new LoginDTO();

		// Paging 페이징
		String empid = request.getParameter("empid");

		d = s.detail(empid);
		List<LoginDTO> selecta = s.selecta();
		
		List<LoginDTO> selectd = s.selectd();

		// 세션 소환
		HttpSession session = request.getSession();

		// 세션으로 보내기
		session.setAttribute("d", d);
		session.setAttribute("selecta", selecta);
		session.setAttribute("selectd", selectd);

		// 세션이니 그냥 주소 바뀌게 ㄱㄱ.
		request.getRequestDispatcher("/WEB-INF/views/P01_auth/perdetail.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
//				response.setContentType("text/html; charset=UTF-8");
		// 함수 모음집 소환
		LoginService s = new LoginService();
		// 데이터 바구니 소환
		LoginDTO d = new LoginDTO();
		

		// 이 값들이 있다면 비밀번호 변경으로
		String permission = request.getParameter("permission");
		String changeAuth = request.getParameter("change-auth");
		String changeDept = request.getParameter("dept");
	    
		int realPermission = -1;

			System.out.println("/pdtail doget.pdtail 실행");
			

			System.out.println(" 권한 : " + permission);
			System.out.println(" 아이디 : " + changeAuth);
			System.out.println(" 부서번호 : " + changeDept);
			
			
			
			d = s.detail(changeAuth);
			
			// 세션 소환
		    HttpSession session = request.getSession();
			


		
			// 권한
			if (realPermission != -1) {					

				if ("권한없음".equals(permission))
					realPermission = 0;
				if ("작업자".equals(permission))
					realPermission = 1;
				if ("관리자".equals(permission))
					realPermission = 2;
				if ("슈퍼바이저".equals(permission))
					realPermission = 3;
				
				System.out.println("권한 레벨 확인 : "+realPermission);
				
				// empid 바구니에 넣기.
				d.setAuth(realPermission);
				d.setEmpid(changeAuth);
				
				if(realPermission == 0) {
					int retire = 0;
					
					d.setRetire(retire);
					
					
					s.retire(d);
					
					
					request.setAttribute("error", "퇴사처리가 완료되었습니다!");
					
				} else {
					
					if (s.changeAuth(d) > 0) {
						
						// 세션으로 보내기
						System.out.println("권한 변경이 완료 되었습니다.");
						request.setAttribute("error", "권한 변경이 완료되었습니다!");
					} else  {
						
						// 세션으로 보내기
						
						System.out.println("권한 변경에 실패 했습니다.");
						request.setAttribute("error", "권한 변경에 실패했습니다. 다시 시도해주세요.");
					}
					
				}
				
				
				
		
				
			} 
			// 부서
			if (changeDept != null && !"".equals(changeDept)) {
				
				int deptno = 0;
				
				
				if ("Production".equals(changeDept))
					deptno = 10;
				if ("QC".equals(changeDept))
					deptno = 20;
				if ("admin".equals(changeDept))
					deptno = 30;
				
				System.out.println("변경 부서 확인 : "+deptno);
				
				// empid 바구니에 넣기.
				d.setDept_no(deptno);
				d.setEmpid(changeAuth);
				
				
			} 

			
			
			
			if (s.changeDept(d) > 0) {
			
				// 세션으로 보내기
				
				System.out.println("부서 변경이 완료 되었습니다.");
				session.setAttribute("error1", "부서 변경이 완료되었습니다!");
			} else {
				
				// 세션으로 보내기
				
				System.out.println("부서 변경에 실패 했습니다.");
				session.setAttribute("error1", "부서 변경에 실패했습니다. 다시 시도해주세요.");
			}
			
			response.sendRedirect("/mes/pdetail");
			return;

		}

	

}
