package P02_dashboard;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import P01_auth.LoginDTO;
import P01_auth.LoginService;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("dashboard의 doget 실행");
		// TODO Auto-generated method stub

		// 주소 : http://localhost:8080/mes/login.jsp

		// 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=UTF-8");
		// 함수 모음집 소환
		LoginService s = new LoginService();
		// 데이터 바구니 소환
		DashDTO d = new DashDTO();
		
		
		
		

		// Paging 페이징(notice)
		String npage = request.getParameter("n_btn");
		System.out.println("npage : " + npage);

		// 처음은 1로 스타트되게
		if (npage == null) {
			npage = "1";
		}
		// 한 페이지당 보여줄 개수
		int ncountPage = 5;

		// 시작 번호 1이면 0. 2면 5. 3이면 10.
		int nstart_no = (Integer.parseInt(npage) - 1) * ncountPage;

		System.out.println("nstart_no : " + nstart_no);

		int ncountPageNo = nstart_no + ncountPage;
		System.out.println(" ncountPageNo : " + ncountPageNo);

		// 함수 소환 후 결과(전체 사원수)를 인트에 저장.
		int ncount = s.nread();
		System.out.println("ncount : " + ncount);

		int npage_no = (int) Math.ceil((double)ncount / ncountPage);
		System.out.println("npage_no : " + npage_no);
		
		
		
		
		
		
		
		// Paging 페이징(suggestion)
		String spage = request.getParameter("s_btn");
		System.out.println("spage : " + spage);
		
		// 처음은 1로 스타트되게
		if (spage == null) {
			spage = "1";
		}
		// 한 페이지당 보여줄 개수
		int scountPage = 5;
		
		// 시작 번호 1이면 0. 2면 5. 3이면 10.
		int sstart_no = (Integer.parseInt(spage) - 1) * scountPage;
		
		System.out.println("sstart_no : " + sstart_no);
		
		int scountPageNo = sstart_no + scountPage;
		System.out.println(" scountPageNo : " + scountPageNo);
		
		// 함수 소환 후 결과(전체 사원수)를 인트에 저장.
		int scount = s.sread();
		System.out.println("scount : " + scount);
		
		int spage_no = (int) Math.ceil((double)scount / scountPage);
		
		
		// 1. 세션 가져오기
	    HttpSession session = request.getSession();

	    // 2. 세션에서 값 꺼내기 (로그인 시 저장했던 이름으로)
	    
	    // getAttribute는 Object를 반환하므로 적절하게 형변환(Casting)이 필요합니다.
	    LoginDTO l = (LoginDTO) session.getAttribute("dto");
	    String empid = l.getEmpid();
	    
	    if (empid != null) {
	        System.out.println("현재 로그인된 아이디: " + empid);
	    }
		
		
		// Paging 페이징(alarms)
		String apage = request.getParameter("a_btn");
		System.out.println("apage : " + apage);
		
		// 처음은 1로 스타트되게
		if (apage == null) {
			apage = "1";
		}
		// 한 페이지당 보여줄 개수
		int acountPage = 5;
		
		// 시작 번호 1이면 0. 2면 5. 3이면 10.
		int astart_no = (Integer.parseInt(apage) - 1) * acountPage;
		
		System.out.println("astart_no : " + astart_no);
		
		int acountPageNo = astart_no + acountPage;
		System.out.println(" acountPageNo : " + acountPageNo);
		
		// 함수 소환 후 결과(전체 사원수)를 인트에 저장.
		int acount = s.aread(empid);
		System.out.println("acount : " + acount);
		
		int apage_no = (int) Math.ceil((double)acount / scountPage);
		
		
		System.out.println("spage_no : "+spage_no);
		
		
        //불량 유형별 차트 자료
		List<DashDTO> list = s.defect();
		
		//일일 작업량과 목표수량 
		List<DashDTO> work_order = s.work_order();
		
		//알람 조회
		List<DashDTO> a = s.a(empid, astart_no, acountPageNo);
		
		//공지사항 페이징
		List<DashDTO> notice = s.notice(nstart_no, ncountPageNo);
		
		//건의 사항 페이징
		List<DashDTO> suggestion = s.suggestion(sstart_no, scountPageNo);

	

		// 세션으로 보내기
		//불량 차트 자료
		session.setAttribute("list", list);
		//공지사항 
		session.setAttribute("npage_no", npage_no);
		session.setAttribute("notice", notice);
		//작업 차트
		session.setAttribute("work", work_order);
		//알람
		session.setAttribute("apage_no", apage_no);
		session.setAttribute("alarms", a);
		//작업량
		System.out.println("work : "+work_order);
		//건의사항
		session.setAttribute("spage_no", spage_no);
		session.setAttribute("suggestion", suggestion);

		// 세션이니 그냥 주소 바뀌게 ㄱㄱ.
		request.getRequestDispatcher("/WEB-INF/views/P02_dashboard/dashboard.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("dashboard의 dopost 실행");


	}

}
