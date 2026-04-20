package P11_masterdata.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import P11_masterdata.DAO.ProcessDAO;
import P11_masterdata.DTO.ProcessDTO;

@WebServlet("/ProcessStepAddController")
public class ProcessStepAddController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/process");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");

		String processId = trimToEmpty(request.getParameter("process_id"));
		String stepName = trimToEmpty(request.getParameter("step_name"));
		int seq = parseIntOrDefault(request.getParameter("seq"), 1);

		if (processId.equals("") || stepName.equals("")) {
			response.sendRedirect(request.getContextPath() + "/process");
			return;
		}

		if (seq < 1) {
			seq = 1;
		}

		ProcessDTO processDTO = new ProcessDTO();
		processDTO.setProcess_id(processId);
		processDTO.setStep_name(stepName);
		processDTO.setSeq(seq);

		ProcessDAO processDAO = new ProcessDAO();
		int result = processDAO.insertProcessStep(processDTO);

		if (result > 0) {
			response.sendRedirect(request.getContextPath() + "/process?processId=" + processId);
		} else {
			response.sendRedirect(request.getContextPath() + "/process?processId=" + processId);
		}
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
