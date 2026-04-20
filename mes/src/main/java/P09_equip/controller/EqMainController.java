package P09_equip.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P07_work.SearchDTO;
import P07_work.WoDTO;
import P07_work.WoService;
import P09_equip.EqService;
import P09_equip.DTO.EqDTO;
import P09_equip.DTO.EqSearchDTO;


@WebServlet("/equipment")
public class EqMainController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/equipment doGet 실행");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");
		
		getAllList(request, response);
		
		String cmd = request.getParameter("cmd");
		System.out.println("cmd : " + cmd);
		
		if ("search".equals(cmd)) {
			search(request, response);
		} else if ("detail".equals(cmd)) {
			detail(request, response);
			return;
		} else {
			getList(request, response);			
		}
		
		request.getRequestDispatcher("/WEB-INF/views/P09_equip/main.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/equipment doPost 실행");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");
		
	}
	
	protected void getList (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/equipment getList 실행");
		
		int size = 10;
		int page = 1;
		
		String pageStr = request.getParameter("page");
		
		try {
			page = Integer.parseInt(pageStr);			
		} catch (Exception e) {

		}
		
		EqDTO dto = new EqDTO();
		dto.setSize(size);
		dto.setPage(page);
		
		EqService service = new EqService();
		Map eqMap = service.getList(dto);
		
		// forward
		request.setAttribute("eqMap", eqMap);
		
		System.out.println(eqMap);
		System.out.println(eqMap.get("list"));
		
	}
	
	protected void getAllList (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/equipment getAllList 실행");
		
		EqService service = new EqService();
		List<EqDTO> list = service.getAllList();
		
		// forward
		request.setAttribute("eqAllList", list);
		
	}
	
	protected void search (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/equipment search 실행");

		int size = 10;
		int page = 1;

		String pageStr = request.getParameter("page");
		
		try {
			page = Integer.parseInt(pageStr);			
		} catch (Exception e) {

		}
		
		EqDTO dto = new EqDTO();
		dto.setSize(size);
		dto.setPage(page);
		
		// 검색
		String status = request.getParameter("status");
		if ("전체".equals(status)) {
		    status = "";
		}
		

		String keyword = "";
		keyword = request.getParameter("keyword").trim();
		
		EqSearchDTO search = new EqSearchDTO();
		
		search.setStatus(status);
		search.setKeyword(keyword);
		
		// service
		EqService service = new EqService();
		Map eqMap = service.search(dto, search);
		
		// forward
		request.setAttribute("eqMap", eqMap);
		
		System.out.println(eqMap);
		System.out.println(eqMap.get("list"));
	}
	
	protected void detail (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/equipment detail 실행");
		
		String eqId = request.getParameter("eqId");
		
		String cp = request.getContextPath();
		response.sendRedirect(cp + "/eqdetail?eqId=" + eqId);
	}

}
