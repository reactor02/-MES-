package P09_equip.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P07_work.WoService;
import P09_equip.EqService;
import P09_equip.DTO.EqLogDTO;

@WebServlet("/eqlogmodify")
public class EqLogModifyController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/eqlogmodify doGet 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		setEqInfo(request, response);
		
		request.getRequestDispatcher("/WEB-INF/views/P09_equip/modifyInsp.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/eqlogmodify doPost 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
//		String cmd = request.getParameter("cmd");
//		
//		if ("delete".equals(cmd))) {
//			deleteInsp(request, response);
//		}
		modifyLog(request, response);
		
	}
	
	protected void setEqInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/eqlogmodify setEqInfo 실행");
		
		String logId = request.getParameter("logId");
		
		EqLogDTO dto = new EqLogDTO();
		dto.setLogId(logId);
		
		EqService service = new EqService();
		
		dto = service.getLogDetail(dto);
		System.out.println(dto);
		
		String sDate = null;
		String sTime = null;
		
		if(dto.getsTime() != null) {
			
			LocalDateTime ldt = dto.getsTime().toLocalDateTime();
			
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			
			sDate = ldt.format(dateFormatter);
			sTime = ldt.format(timeFormatter);
			
		}
		
		String eDate = null;
		String eTime = null;
		
		if(dto.geteTime() != null) {
			
			LocalDateTime ldt = dto.geteTime().toLocalDateTime();
			
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			
			eDate = ldt.format(dateFormatter);
			eTime = ldt.format(timeFormatter);
			
		}
		
		request.setAttribute("logInfo", dto);
		request.setAttribute("sDate", sDate);
		request.setAttribute("sTime", sTime);
		request.setAttribute("eDate", eDate);
		request.setAttribute("eTime", eTime);
	}
	

	protected void modifyLog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/eqlogadd modifyLog 실행");
		EqLogDTO dto = new EqLogDTO();
		
		String logId = request.getParameter("logId");
		String eqId = request.getParameter("eqId");
		String wId = request.getParameter("wId");
		
		String sDateStr = request.getParameter("sDate");
		String sTimeStr = request.getParameter("sTime")+":00";
		String sTimeFull = sDateStr + " " + sTimeStr;
		Timestamp sTime = Timestamp.valueOf(sTimeFull);
		
		String eDateStr = request.getParameter("eDate");
		System.out.println("eDate : " + eDateStr);
		Timestamp eTime = null;
		if (!("".equals(eDateStr))) {
			String eTimeStr = request.getParameter("eTime")+":00";
			String eTimeFull = eDateStr + " " + eTimeStr;
			eTime = Timestamp.valueOf(eTimeFull);
			dto.seteTime(eTime);
		}
		
		String inspType = request.getParameter("inspType");
		String content = request.getParameter("content");
		
		dto.setLogId(logId);
		dto.setEqId(eqId);
		dto.setwId(wId);
		dto.setsTime(sTime);
		dto.setInspType(inspType);
		dto.setInspContent(content);
		
		EqService service = new EqService();
		int result = service.modifyLog(dto);
		
		System.out.println(result);
		
		response.sendRedirect("/mes/eqdetail?eqId=" + eqId);
	}
	
//	protected void deleteInsp(HttpServletRequest request, HttpServletResponse response) throws IOException {
//	    System.out.println("/workadd deleteInsp 실행");
//
//	    request.setCharacterEncoding("UTF-8");
//	    response.setContentType("application/json; charset=UTF-8");
//	    
//	    String logId = request.getParameter("logId");
//	    
//	    EqService service = new EqService();
//	    int result = service.deleteInsp(logId);
//
//	    System.out.println(result);
//		
//		response.sendRedirect("/mes/equipment");
//	}

}
