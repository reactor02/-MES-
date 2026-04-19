package P11_masterdata.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import P11_masterdata.DAO.VendorDAO;
import P11_masterdata.DTO.VendorDTO;

@WebServlet("/vendor")
public class VendorPageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public VendorPageController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		int size = 5;
		int page = 1;

		String sSize = request.getParameter("size");
		String sPage = request.getParameter("page");
		String vendorType = request.getParameter("vendorType");
		String keyword = request.getParameter("keyword");

		if (sSize != null && !sSize.trim().equals("")) {
			try {
				size = Integer.parseInt(sSize);
			} catch (Exception e) {
				size = 5;
			}
		}

		if (sPage != null && !sPage.trim().equals("")) {
			try {
				page = Integer.parseInt(sPage);
			} catch (Exception e) {
				page = 1;
			}
		}

		if (size < 1) {
			size = 5;
		}
		if (page < 1) {
			page = 1;
		}

		VendorDTO vendorDTO = new VendorDTO();
		vendorDTO.setSize(size);
		vendorDTO.setPage(page);
		vendorDTO.setVendorType(vendorType);
		vendorDTO.setKeyword(keyword);

		Map<String, Object> map = vendorlistSelect(vendorDTO);

		request.setAttribute("vendor", map.get("list"));
		request.setAttribute("list", map.get("list"));

		request.setAttribute("page", map.get("page"));
		request.setAttribute("size", map.get("size"));
		request.setAttribute("totalPage", map.get("totalPage"));
		request.setAttribute("totalCount", map.get("totalCount"));

		request.setAttribute("vendorType", vendorType);
		request.setAttribute("keyword", keyword);

		request.setAttribute("nextVendorId", map.get("nextVendorId"));
		request.setAttribute("totalVendor", map.get("totalVendor"));
		request.setAttribute("vendorTypeA", map.get("vendorTypeA"));
		request.setAttribute("vendorTypeB", map.get("vendorTypeB"));

		request.setAttribute("currentEmpId", findSessionEmpId(request));

		RequestDispatcher dispatcher = request.getRequestDispatcher("/vendor.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public Map<String, Object> vendorlistSelect(VendorDTO vendorDTO) {
		int size = vendorDTO.getSize();
		int page = vendorDTO.getPage();

		if (size == 0) {
			size = 5;
		}
		if (page == 0) {
			page = 1;
		}

		int end = page * size;
		int start = end - (size - 1);

		vendorDTO.setStart(start);
		vendorDTO.setEnd(end);
		vendorDTO.setSize(size);
		vendorDTO.setPage(page);

		VendorDAO vendorDAO = new VendorDAO();

		int totalCount = vendorDAO.selectVendorTotalCount(vendorDTO);
		int totalVendor = vendorDAO.selectVendorTotalCountAll();
		int vendorTypeA = vendorDAO.selectVendorTypeCount("공급업체");
		int vendorTypeB = vendorDAO.selectVendorTypeCount("고객사");

		int totalPage = totalCount / size;
		if (totalCount % size != 0) {
			totalPage++;
		}
		if (totalPage == 0) {
			totalPage = 1;
		}

		if (page > totalPage) {
			page = totalPage;
			end = page * size;
			start = end - (size - 1);

			vendorDTO.setPage(page);
			vendorDTO.setStart(start);
			vendorDTO.setEnd(end);
		}

		List<VendorDTO> list = vendorDAO.selectVendorPageList(vendorDTO);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", totalCount);
		map.put("totalPage", totalPage);
		map.put("page", page);
		map.put("size", size);

		map.put("nextVendorId", vendorDAO.selectNextVendorId());
		map.put("totalVendor", totalVendor);
		map.put("vendorTypeA", vendorTypeA);
		map.put("vendorTypeB", vendorTypeB);

		return map;
	}

	private String findSessionEmpId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return "";
		}

		Object empId = session.getAttribute("emp_id");
		if (empId == null) {
			empId = session.getAttribute("empId");
		}
		if (empId == null) {
			empId = session.getAttribute("loginEmpId");
		}

		return empId == null ? "" : String.valueOf(empId).trim();
	}
}
