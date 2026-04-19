package P11_masterdata.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P11_masterdata.DAO.BomDAO;
import P11_masterdata.DTO.BomDTO;

@WebServlet("/BomDetailUpdateController")
public class BomDetailUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/bom");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String bomId = request.getParameter("bom_id");
		String childItemId = request.getParameter("child_item_id");
		String sBomDetailId = request.getParameter("bom_detail_id");
		String sEa = request.getParameter("ea");

		int bomDetailId = 0;
		BigDecimal ea = BigDecimal.ZERO;

		try {
			bomDetailId = Integer.parseInt(sBomDetailId);
		} catch (Exception e) {
			bomDetailId = 0;
		}

		try {
			ea = new BigDecimal(sEa);
		} catch (Exception e) {
			ea = BigDecimal.ZERO;
		}

		BomDAO bomDAO = new BomDAO();

		if (bomId == null || bomId.trim().equals("")
				|| childItemId == null || childItemId.trim().equals("")
				|| bomDetailId <= 0
				|| ea.compareTo(BigDecimal.ZERO) <= 0
				|| !bomDAO.isValidChildItem(null, childItemId)) {
			response.sendRedirect(request.getContextPath() + "/bomDetail?bomId=" + bomId);
			return;
		}

		BomDTO bomDTO = new BomDTO();
		bomDTO.setBom_id(bomId);
		bomDTO.setBom_detail_id(bomDetailId);
		bomDTO.setChild_item_id(childItemId);
		bomDTO.setEa(ea);

		bomDAO.updateBomDetail(bomDTO);

		response.sendRedirect(request.getContextPath() + "/bomDetail?bomId=" + bomId);
	}
}
