package P08_quality.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P01_auth.DTO.UserWoDTO;
import P07_work.WoDTO;
import P07_work.WoService;
import P08_quality.QcAddDTO;
import P08_quality.QcService;

@WebServlet("/qcadd")
public class QcAddController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcadd doGet 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		String cmd = request.getParameter("cmd");
		if ("getWo".equals(cmd)) {
			getWo(request, response);
			return;
		}
		
		if ("searchWorker".equals(cmd)) {
			searchWorker(request, response);
			return;
		}
		
		getWoList(request, response);
		
		request.getRequestDispatcher("/WEB-INF/views/P08_quality/add.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcadd doPost 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		addQc(request, response);
		
		response.sendRedirect("/mes/qclist");
	}
	
	protected void getWoList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcadd getWoList 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		QcService service = new QcService();
		List woList = service.getWoList();
		
		System.out.println(woList);
		
		request.setAttribute("woList", woList);
	}
	
	protected void getWo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcadd getWo 실행");

		String woId = request.getParameter("woId");
		
		WoDTO woDTO = new WoDTO();
		woDTO.setWoId(woId);
		
		QcService service = new QcService();
		woDTO = service.setWo(woDTO);
		
		System.out.println(woDTO);
		
		response.setContentType("application/json; charset=UTF-8");

		String json = "{"
		    + "\"woId\":\"" + woDTO.getWoId() + "\","
		    + "\"itemId\":\"" + woDTO.getItemId() + "\","
		    + "\"itemName\":\"" + woDTO.getItemName() + "\","
		    + "\"qty\":\"" + woDTO.getWoQty() + "\","
    		+ "\"woDate\":\"" + woDTO.getWorkDate() + "\""
		    + "}";

		response.getWriter().write(json);
	}
	
	protected void searchWorker(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println("/qcadd searchWorker 실행");

	    String keyword = request.getParameter("keyword").trim();

	    QcService service = new QcService();
	    List<UserWoDTO> list = service.searchWorker(keyword);

	    StringBuilder json = new StringBuilder("[");
	    for (int i = 0; i < list.size(); i++) {
	        UserWoDTO e = list.get(i);

	        json.append("{")
	            .append("\"empId\":\"").append(e.getEmpId()).append("\",")
	            .append("\"empName\":\"").append(e.geteName()).append("\"")
	            .append("}");

	        if (i < list.size() - 1) json.append(",");
	    }
	    json.append("]");

	    response.getWriter().write(json.toString());
	}
	
	protected void addQc(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println("/qcadd addQc 실행");

	    String woId = request.getParameter("woId");
	    Date sDate = Date.valueOf(request.getParameter("qcDate"));
	    String director = ""; // 로그인 정보 받아오기
	    String worker = request.getParameter("workerId");
	    String content = request.getParameter("content");
	    int status = 10;
	    
	    QcAddDTO addDTO = new QcAddDTO();
	    
	    addDTO.setWoId(woId);
	    addDTO.setsDate(sDate);
	    addDTO.setDirector(director);
	    addDTO.setWorker(worker);
	    addDTO.setContent(content);
	    addDTO.setStatus(status);
	    
	    QcService service = new QcService();
	    int result = service.addQc(addDTO);
	    int woStatus = service.woStatus(addDTO);

	    System.out.println(result + ", " + woStatus);
	    
	}

}
