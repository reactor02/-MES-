package P11_masterdata.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
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

@WebServlet("/vendorr")
public class VendorController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public VendorController() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		VendorDAO vendorDAO = new VendorDAO();
		List<VendorDTO> list = vendorListSelect(vendorDAO);

		int totalVendor = 0;
		int vendorTypeA = 0;
		int vendorTypeB = 0;

		if (list != null) {
			totalVendor = list.size();

			for (VendorDTO dto : list) {
				if ("공급업체".equals(dto.getVendor_type())) {
					vendorTypeA++;
				} else if ("고객사".equals(dto.getVendor_type())) {
					vendorTypeB++;
				}
			}
		}

		request.setAttribute("vendor", list);
		request.setAttribute("list", list);
		request.setAttribute("nextVendorId", vendorDAO.selectNextVendorId());
		request.setAttribute("totalVendor", totalVendor);
		request.setAttribute("vendorTypeA", vendorTypeA);
		request.setAttribute("vendorTypeB", vendorTypeB);
		request.setAttribute("page", 1);
		request.setAttribute("size", list != null ? list.size() : 0);
		request.setAttribute("totalPage", 1);
		request.setAttribute("currentEmpId", findSessionEmpId(request));

		RequestDispatcher dispatcher = request.getRequestDispatcher("/vendor.jsp");
		dispatcher.forward(request, response);
	}

	private List<VendorDTO> vendorListSelect(VendorDAO vendorDAO) {
		return vendorDAO.selectAll();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	private String findSessionEmpId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return "";
		}

		String[] directAttrNames = { "emp_id", "empId", "loginEmpId" };

		for (String attrName : directAttrNames) {
			Object attrValue = session.getAttribute(attrName);
			String empId = trimToEmpty(attrValue == null ? null : String.valueOf(attrValue));

			if (!empId.equals("")) {
				return empId;
			}
		}

		String[] objectAttrNames = { "dto", "loginDTO", "loginInfo", "user" };

		for (String attrName : objectAttrNames) {
			String empId = extractEmpId(session.getAttribute(attrName));

			if (!empId.equals("")) {
				return empId;
			}
		}

		Enumeration<String> attrNames = session.getAttributeNames();

		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			Object attrValue = session.getAttribute(attrName);

			String directEmpId = "";
			String lowerAttrName = attrName == null ? "" : attrName.toLowerCase();

			if (lowerAttrName.contains("emp")) {
				directEmpId = trimToEmpty(attrValue == null ? null : String.valueOf(attrValue));
			}

			if (!directEmpId.equals("")) {
				return directEmpId;
			}

			String objectEmpId = extractEmpId(attrValue);
			if (!objectEmpId.equals("")) {
				return objectEmpId;
			}
		}

		return "";
	}

	private String extractEmpId(Object sourceObject) {
		if (sourceObject == null) {
			return "";
		}

		if (sourceObject instanceof Map<?, ?>) {
			Map<?, ?> sourceMap = (Map<?, ?>) sourceObject;
			String[] mapKeys = { "empid", "empId", "emp_id" };

			for (String mapKey : mapKeys) {
				Object value = sourceMap.get(mapKey);
				String empId = trimToEmpty(value == null ? null : String.valueOf(value));

				if (!empId.equals("")) {
					return empId;
				}
			}
		}

		String[] getterNames = { "getEmpid", "getEmpId", "getEmp_id" };

		for (String getterName : getterNames) {
			try {
				Method getter = sourceObject.getClass().getMethod(getterName);
				Object value = getter.invoke(sourceObject);
				String empId = trimToEmpty(value == null ? null : String.valueOf(value));

				if (!empId.equals("")) {
					return empId;
				}
			} catch (Exception e) {
			}
		}

		String[] fieldNames = { "empid", "empId", "emp_id" };

		for (String fieldName : fieldNames) {
			try {
				Field field = sourceObject.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				Object value = field.get(sourceObject);
				String empId = trimToEmpty(value == null ? null : String.valueOf(value));

				if (!empId.equals("")) {
					return empId;
				}
			} catch (Exception e) {
			}
		}

		return "";
	}

	private String trimToEmpty(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}