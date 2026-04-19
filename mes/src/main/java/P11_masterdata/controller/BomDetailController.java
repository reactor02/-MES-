package P11_masterdata.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P11_masterdata.DAO.BomDAO;
import P11_masterdata.DTO.BomDTO;

@WebServlet("/bomDetail")
public class BomDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		int size = 5;
		int page = 1;

		String sSize = request.getParameter("size");
		String sPage = request.getParameter("page");
		String bomId = request.getParameter("bomId");

		try {
			if (sSize != null && !sSize.trim().equals("")) {
				size = Integer.parseInt(sSize);
			}
		} catch (Exception e) {
			size = 5;
		}

		try {
			if (sPage != null && !sPage.trim().equals("")) {
				page = Integer.parseInt(sPage);
			}
		} catch (Exception e) {
			page = 1;
		}

		BomDAO bomDAO = new BomDAO();

		if (bomId == null || bomId.trim().equals("")) {
			List<BomDTO> bomList = bomDAO.selectAll();
			if (bomList != null && !bomList.isEmpty()) {
				bomId = bomList.get(0).getBom_id();
			}
		}

		BomDTO bomInfo = null;
		List<BomDTO> detailList = new ArrayList<BomDTO>();
		List<BomDTO> itemList = new ArrayList<BomDTO>();
		int totalCount = 0;
		int totalPage = 1;

		if (bomId != null && !bomId.trim().equals("")) {
			BomDTO pageDTO = new BomDTO();
			pageDTO.setBom_id(bomId);
			pageDTO.setSize(size);
			pageDTO.setPage(page);

			int end = page * size;
			int start = end - (size - 1);

			pageDTO.setStart(start);
			pageDTO.setEnd(end);

			bomInfo = bomDAO.selectBomInfo(bomId);
			detailList = bomDAO.selectBomDetailPage(pageDTO);
			itemList = bomDAO.selectBomDetailItemList(null);

			totalCount = bomDAO.selectBomDetailTotalCount(bomId);
			totalPage = totalCount / size;
			if (totalCount % size != 0) {
				totalPage++;
			}
			if (totalPage == 0) {
				totalPage = 1;
			}
		}

		request.setAttribute("bomInfo", bomInfo);
		request.setAttribute("detailList", detailList);
		request.setAttribute("itemList", itemList);
		request.setAttribute("page", page);
		request.setAttribute("size", size);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("nextBomDetailId", bomDAO.selectNextBomDetailId());

		RequestDispatcher dispatcher = request.getRequestDispatcher("/bomDetail.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
