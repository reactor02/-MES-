package P11_masterdata.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P11_masterdata.DAO.ProcessDAO;
import P11_masterdata.DTO.ProcessDTO;

@WebServlet("/ProcessAddController")
public class ProcessAddController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProcessAddController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");

		ProcessDAO processDAO = new ProcessDAO();

		String process_id = trimToEmpty(request.getParameter("process_id"));
		int seq = parseIntOrDefault(request.getParameter("seq"), 1);
		String process_name = trimToEmpty(request.getParameter("process_name"));
		String process_type = trimToEmpty(request.getParameter("process_type"));
		String item_id = trimToEmpty(request.getParameter("item_id"));
		String process_info = trimToEmpty(request.getParameter("process_info"));

		if (process_id.equals("")) {
			process_id = processDAO.selectNextProcessId();
		}
		if (seq < 1) {
			seq = 1;
		}
		if (process_type.equals("")) {
			process_type = "wo";
		}

		if (process_info.equals("")) {
			process_info = process_name;
		}
		if (process_name.equals("") || item_id.equals("")) {
			response.sendRedirect(request.getContextPath() + "/process");
			return;
		}

		ProcessDTO processDTO = new ProcessDTO();
		processDTO.setProcess_id(process_id);
		processDTO.setSeq(seq);
		processDTO.setProcess_name(process_name);
		processDTO.setProcess_type(process_type);
		processDTO.setItem_id(item_id);
		processDTO.setProcess_info(process_info);
		
		int result = addProcess(processDTO);

		if (result > 0) {
			response.sendRedirect(request.getContextPath() + "/process?page=1&processId=" + process_id);
		} else {
			response.sendRedirect(request.getContextPath() + "/process");
		}
	}

	public int addProcess(ProcessDTO processDTO) {
		ProcessDAO processDAO = new ProcessDAO();
		return processDAO.insertProcess(processDTO);
	}

	private int parseIntOrDefault(String value, int defaultValue) {
		try {
			if (value == null || value.trim().equals("")) {
				return defaultValue;
			}
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private String trimToEmpty(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}
