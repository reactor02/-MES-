package P07_work.cotroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P07_work.LotDTO;
import P07_work.WoBOMDTO;
import P07_work.WoDTO;
import P07_work.WoService;

@WebServlet("/contentmodify")
public class WoContentController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/contentmodify doGet 실행");
		

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");
		
		setContent(request, response);
		
		request.getRequestDispatcher("/WEB-INF/views/P07_work/contentModify.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/contentmodify doPost 실행");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");
		
	    String woId = request.getParameter("woId");
	    String statusStr = request.getParameter("status");
	    String prevQtyStr = request.getParameter("prevQty");
	    String worker = request.getParameter("worker");

	    try {
	        if (woId == null || woId.trim().isEmpty()) {
	            throw new RuntimeException("작업지시 코드가 없습니다.");
	        }

	        if (statusStr == null || statusStr.trim().isEmpty()) {
	            throw new RuntimeException("작업 상태가 선택되지 않았습니다.");
	        }

	        if (worker == null || worker.trim().isEmpty()) {
	            throw new RuntimeException("작업자 정보가 없습니다.");
	        }
	        
	        int status = Integer.parseInt(statusStr);

	        int prevQty = 0;
	        if (status == 30) {
	            if (prevQtyStr == null || prevQtyStr.trim().isEmpty()) {
	                throw new RuntimeException("완료 수량이 입력되지 않았습니다.");
	            }
	            prevQty = Integer.parseInt(prevQtyStr);
	        } else {
	            if (prevQtyStr != null && !prevQtyStr.trim().isEmpty()) {
	                prevQty = Integer.parseInt(prevQtyStr);
	            }
	        }

	        WoService service = new WoService();
	        service.processContentModify(woId, status, prevQty, worker);

	        response.sendRedirect("/mes/workorder?woId=" + woId);

	    } catch (NumberFormatException e) {
	        request.setAttribute("errorMsg", "숫자 형식이 올바르지 않습니다.");
	        setContent(request, response);
	        request.getRequestDispatcher("/WEB-INF/views/P07_work/contentModify.jsp").forward(request, response);

	    } catch (RuntimeException e) {
	        request.setAttribute("errorMsg", e.getMessage());
	        setContent(request, response);
	        request.getRequestDispatcher("/WEB-INF/views/P07_work/contentModify.jsp").forward(request, response);
	    }
		
//		updateContent(request, response);
	}
	
	protected void setContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/contentmodify setContent 실행");
		
		String woId = request.getParameter("woId");
		WoDTO dto = new WoDTO();
		dto.setWoId(woId);
		
		WoService service = new WoService();
		WoDTO woDTO = service.detail(dto);
		
		System.out.println(dto);
		
		// forward
		request.setAttribute("woInfo", woDTO);

	}
//	
//	protected void updateContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
//	    System.out.println("/workadd modifyWo 실행");
//	    
//	    String woId = request.getParameter("woId");
//	    int status = 0;
//	    int prevQty = 0;
//	    
//	    if (!("".equals(request.getParameter("status")))) {
//	    	status = Integer.parseInt(request.getParameter("status"));
//	    }
//	    if (!("".equals(request.getParameter("prevQty")))) {
//	    	prevQty = Integer.parseInt(request.getParameter("prevQty"));
//	    }
//	    
//	    
//	    WoService service = new WoService();
//	    int result = service.updateContent(woId, status, prevQty);
//
//	    System.out.println(result);
//	    
//	    updatePlan(request, response);
//	    
//	    if (status == 30) {
//	    	completeWork(request, response);
//	    }
//	    
//		response.sendRedirect("/mes/workorder?woId=" + woId);
//	}
//	
//	protected void updatePlan(HttpServletRequest request, HttpServletResponse response) throws IOException {
//	    System.out.println("/workadd updatePlan 실행");
//	    
//	    WoService service = new WoService();
//	    int result = service.updatePlan();
//
//	    System.out.println(result);
//		
//	}
//	
//	protected void completeWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("/workadd completeWork 실행");
//		
//		// BOM 계산
//		System.out.println("BOM 계산");
//		String woId = request.getParameter("woId");
//		String worker = request.getParameter("worker");
//		WoBOMDTO dto = new WoBOMDTO();
//		dto.setWoId(woId);
//		
//		WoService service = new WoService();
//		List<WoBOMDTO> bom = service.setBOM(dto);
//		
//		System.out.println(bom);
//		
//		int woQty = 0;
//		if (request.getParameter("woQty") != null) {
//			woQty = Integer.parseInt(request.getParameter("woQty"));
//		}
//		
//
//		for (int i=0; i<bom.size(); i++) {
//			WoBOMDTO bomDTO = bom.get(i);
//			double ea = bomDTO.getEa();
//			
//			double eq = ea * woQty;
//			System.out.println("eq : " + eq);
//			
//			while (eq != 0) {
//				LotDTO lotDTO = service.getLot(bomDTO.getcId());
//				
//				System.out.println(lotDTO.getItemId() + " : " + lotDTO.getLotId());
//				
//				double lot = lotDTO.getQty();
//				System.out.println("lot : " + lot);
//
//				int minStock = service.minStock(bomDTO.getcId(), eq);
//				
//				if (lot != 0 && lot <= eq) {
//					int out = service.insertOut(lotDTO, worker);
//					System.out.println("out : " + out);
//					
//					eq = eq - lot;
//					lot = 0;
//					
//					lotDTO.setQty(lot);
//					lotDTO.setStatus("사용 완료");
//					
//					int updLot = service.updateLot(lotDTO);
//					System.out.println("updLot : " + updLot);
//					
//				} else if (lot != 0 && lot > eq) {
//					int out = service.insertOut(lotDTO, worker);
//					System.out.println("out2 : " + out);
//
//					lot = lot - eq;
//					eq = 0;
//					
//					lotDTO.setQty(lot);
//					lotDTO.setStatus("사용 중");
//					
//					int updLot = service.updateLot(lotDTO);
//					System.out.println("upd2 : " + updLot);
//					int in = service.insertIn(lotDTO, worker);
//					System.out.println("in : " + in);
//				}
//				
//				
//			} // while
//			
//		} // for
//		
//		
//	    
//	}
//	
//	protected void setBOM(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("/workadd setBOM 실행");
//		
//		String woId = request.getParameter("woId");
//		WoBOMDTO dto = new WoBOMDTO();
//		dto.setWoId(woId);
//		
//		WoService service = new WoService();
//		List<WoBOMDTO> bom = service.setBOM(dto);
//		
//		System.out.println(bom);
//		
//		// forward
//		request.setAttribute("bom", bom);
//	}

}
