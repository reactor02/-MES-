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

import P11_masterdata.DAO.ProcessDAO;
import P11_masterdata.DTO.ProcessDTO;

@WebServlet("/process")
public class ProcessController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProcessController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		ProcessDAO processDAO = new ProcessDAO();

		int size = 5;
		int page = 1;

		String sSize = request.getParameter("size");
		String sPage = request.getParameter("page");
		String keyword = request.getParameter("keyword");
		String selectedProcessId = request.getParameter("processId");

		if (keyword == null) {
			keyword = "";
		}
		keyword = keyword.trim();

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

		if (size < 1) {
			size = 5;
		}
		if (page < 1) {
			page = 1;
		}

		List<ProcessDTO> allProcessList = processDAO.selectProcessList();
		ProcessDTO selectedProcess = null;

		if ((selectedProcessId == null || selectedProcessId.trim().equals("")) && !allProcessList.isEmpty()) {
			selectedProcessId = allProcessList.get(0).getProcess_id();
		}

		for (ProcessDTO dto : allProcessList) {
			if (dto.getProcess_id() != null && dto.getProcess_id().equals(selectedProcessId)) {
				selectedProcess = dto;
				break;
			}
		}

		ProcessDTO processDTO = new ProcessDTO();
		processDTO.setKeyword(keyword);

		int totalCount = processDAO.selectProcessTotalCount(processDTO);
		int totalPage = totalCount / size;
		if (totalCount % size != 0) {
			totalPage++;
		}
		if (totalPage == 0) {
			totalPage = 1;
		}
		if (page > totalPage) {
			page = totalPage;
		}

		processDTO.setPage(page);
		processDTO.setSize(size);
		processDTO.setStart((page - 1) * size + 1);
		processDTO.setEnd(page * size);

		List<ProcessDTO> processList = processDAO.selectProcessPageList(processDTO);
		List<ProcessDTO> stepList = new ArrayList<ProcessDTO>();

		if (selectedProcessId != null && !selectedProcessId.trim().equals("")) {
			stepList = processDAO.selectProcessStepList(selectedProcessId);
		}

		String nextProcessId = processDAO.selectNextProcessId();

		request.setAttribute("allProcessList", allProcessList);
		request.setAttribute("processList", processList);
		request.setAttribute("stepList", stepList);
		request.setAttribute("selectedProcessId", selectedProcessId);
		request.setAttribute("selectedProcess", selectedProcess);
		request.setAttribute("nextProcessId", nextProcessId);
		request.setAttribute("page", page);
		request.setAttribute("size", size);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("keyword", keyword);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/process.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}
