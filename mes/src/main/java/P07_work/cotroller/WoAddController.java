package P07_work.cotroller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P01_auth.DTO.UserWoDTO;
import P06_prod.DTO.PlanWoDTO;
import P07_work.WoAddDTO;
import P07_work.WoService;

@WebServlet("/workadd")
public class WoAddController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/workadd doGet 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		String cmd = request.getParameter("cmd");
		
		if ("getPlan".equals(cmd)) {
			getPlan(request, response);
			return;
		}
		
		if ("searchWorker".equals(cmd)) {
		    searchWorker(request, response);
		    return;
		}
		
		planList(request,response);
		
		request.getRequestDispatcher("/WEB-INF/views/P07_work/add.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/workadd doPost 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		addOrder(request, response);
		
		response.sendRedirect("/mes/worklist");
		
	}
	
	protected void planList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/workadd planList 실행");
		
		WoService service = new WoService();
		List planList = service.planList();
		
		// forward
		request.setAttribute("planList", planList);
		
	}
	
	protected void getPlan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/workadd getPlan 실행");

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8;");
		
		String planId = request.getParameter("planId");
		
		PlanWoDTO planDTO = new PlanWoDTO();
		planDTO.setPlanId(planId);
		
		WoService service = new WoService();
		planDTO = service.getPlan(planDTO);
		
//		request.setAttribute("selectedPlan", planDTO);
		response.setContentType("application/json; charset=UTF-8");

		String json = "{"
		    + "\"planId\":\"" + planDTO.getPlanId() + "\","
		    + "\"itemId\":\"" + planDTO.getItemId() + "\","
		    + "\"itemName\":\"" + planDTO.getItemName() + "\","
		    + "\"prevQty\":\"" + planDTO.getPrevQty() + "\","
		    + "\"planQty\":\"" + planDTO.getPlanQty() + "\","
		    + "\"dName\":\"" + planDTO.getdName() + "\","
		    + "\"dId\":\"" + planDTO.getdId() + "\","
    		+ "\"sDate\":\"" + planDTO.getsDate() + "\","
    		+ "\"eDate\":\"" + planDTO.geteDate() + "\""
		    + "}";

		response.getWriter().write(json);
	}
	
	protected void searchWorker(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println("/workadd searchWorker 실행");

	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=UTF-8");

	    String keyword = request.getParameter("keyword").trim();

	    WoService service = new WoService();
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
	
	protected void addOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println("/workadd addOrder 실행");

	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=UTF-8");
	    
	    String planId = request.getParameter("planId");
	    int woQty = Integer.parseInt(request.getParameter("targetQty"));
	    String workDate = request.getParameter("workDate");
	    String worker = request.getParameter("workerId");
	    String content = request.getParameter("content");
	    
	    WoAddDTO addDTO = new WoAddDTO();
	    
	    addDTO.setPlanId(planId);
	    addDTO.setWoQty(woQty);
	    addDTO.setWorkDate(workDate);
	    addDTO.setWorker(worker);
	    addDTO.setContent(content);
	    
	    WoService service = new WoService();
	    int result = service.addOrder(addDTO);

	    System.out.println(result);
	    
	}
	
	private String safe(String str) {
	    return str == null ? "" : str.replace("\"", "\\\"");
	}
	
}
