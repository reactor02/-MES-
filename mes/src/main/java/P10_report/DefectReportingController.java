package P10_report;

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
import P02_dashboard.DashDTO;

@WebServlet("/defectreporting")
public class DefectReportingController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("defectreporting의 doget 실행");
		// TODO Auto-generated method stub

		// 주소 : http://localhost:8080/mes/login.jsp

		// 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=UTF-8");
		// 함수 모음집 소환
		LoginService s = new LoginService();

		// 데이터 바구니 소환
		LoginDTO d = new LoginDTO();
		
		
		
		 //불량 유형별 차트 자료
		List<DashDTO> list = s.defect();
		List<LoginDTO> dMonthChart = s.dMonthChart();
		
		
		
		

		// Paging 페이징
		String page = request.getParameter("d_btn");
		System.out.println("page : " + page);

		// 처음은 1로 스타트되게
		if (page == null) {
			page = "1";
		}
		// 한 페이지당 보여줄 개수
		int countPage = 5;

		// 시작 번호 1이면 0. 2면 5. 3이면 10.
		int start_no = (Integer.parseInt(page) - 1) * countPage;

		System.out.println("start_no : " + start_no);

		int countPageNo = start_no + countPage;
		System.out.println(" countPageNo : " + countPageNo);

		// 함수 소환 후 결과(전체 사원수)를 인트에 저장.
		int count = s.dread();

		int page_no = (int) Math.ceil((double) count / countPage);
		System.out.println("page_no : " + page_no);

		// 함수 소환 후 결과를 리스트에 저장.
		List<LoginDTO> defect_report= s.defect_report(start_no, countPageNo);

		// 세션 소환
		HttpSession session = request.getSession();

		// 세션으로 보내기
		
		//불량 차트 자료
		session.setAttribute("list", list);
		
		//월간 불량 자료
		session.setAttribute("dMonthChart", dMonthChart);
				
		//부적합 보고서 내용		
		session.setAttribute("page_no", page_no);
		session.setAttribute("defect_report", defect_report);

		// 세션이니 그냥 주소 바뀌게 ㄱㄱ.
		request.getRequestDispatcher("/WEB-INF/views/P10_report/defectReporting.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
