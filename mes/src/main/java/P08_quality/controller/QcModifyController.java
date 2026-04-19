package P08_quality.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P07_work.WoAddDTO;
import P07_work.WoService;
import P08_quality.QcAddDTO;
import P08_quality.QcDTO;
import P08_quality.QcService;

@WebServlet("/qcmodify")
public class QcModifyController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcmodify doGet 실행");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");
		
		getQc(request, response);
		
		request.getRequestDispatcher("/WEB-INF/views/P08_quality/qcModify.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcmodify doPost 실행");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");
		
		String cmd = request.getParameter("cmd");
		
		if ("update".equals(cmd)) {
			modifyQc(request, response);
		} else if ("delete".equals(cmd)) {
			deleteQc(request, response);
		};
	}
	
	protected void getQc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/qcmodify getQc 실행");
		
		String qcId = request.getParameter("qcId");
		
		QcService service = new QcService();
		QcDTO qcDTO = service.getQc(qcId);
		
		System.out.println(qcDTO);
		
		request.setAttribute("qcInfo", qcDTO);
	}
	
	protected void modifyQc(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println("/qcadd modifyQc 실행");

	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=UTF-8");
	    
	    String qcId = request.getParameter("qcId");
	    
	    Date sDate = Date.valueOf(request.getParameter("sDate"));
	    String worker = request.getParameter("workerId");
	    String content = request.getParameter("content");
	    
	    QcAddDTO addDTO = new QcAddDTO();
	    
	    addDTO.setsDate(sDate);
	    addDTO.setWorker(worker);
	    addDTO.setContent(content);
	    
	    QcService service = new QcService();
	    int result = service.modifyOrder(qcId, addDTO);

	    System.out.println(result);
		
		response.sendRedirect("/mes/qcdetail?qcId=" + qcId);
	}
	
	protected void deleteQc(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println("/qcadd deleteQc 실행");

	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=UTF-8");
	    
	    String qcId = request.getParameter("qcId");
	    String woId = request.getParameter("woId");
	    
	    System.out.println(woId);
	    
	    QcService service = new QcService();
	    int result = service.deleteQc(qcId);
	    int woStatus = service.delWoStatus(woId);

	    System.out.println(result + ", " + woStatus);
		
		response.sendRedirect("/mes/qclist");
	}

}
