package P06_prod;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/prod/*")
public class ProdController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    ProdService prodService = new ProdService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        System.out.println(request.getServletPath());
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/list";

        switch (pathInfo) {

            /* ── 목록 ──────────────────────────────────────────── */
            case "/list": {
                int size = 10;
                int page = 1;
                try { size = Integer.parseInt(request.getParameter("size")); } catch (Exception e) {}
                try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception e) {}

                ProdDTO planDTO = new ProdDTO();
                planDTO.setSize(size);
                planDTO.setPage(page);

                Map map = prodService.getPlanList(planDTO);
                map.put("size", size);
                map.put("page", page);
                request.setAttribute("map", map);

                request.setAttribute("groupList", prodService.getGroupList());
                request.setAttribute("itemList",  prodService.getItemList());

                request.getRequestDispatcher("/WEB-INF/views/P06_prod/prodList.jsp")
                       .forward(request, response);
                break;
            }

            /* ── 상세 ──────────────────────────────────────────── */
            case "/detail": {
                String planId = request.getParameter("planId");
                if (planId == null || planId.isEmpty()) {
                    response.sendRedirect("list");
                    break;
                }
                ProdDTO dto = prodService.getPlanDetail(planId);
                if (dto == null) {
                    response.sendRedirect("list");
                    break;
                }
                request.setAttribute("planDto", dto);
                request.setAttribute("groupList", prodService.getGroupList());
                request.setAttribute("itemList",  prodService.getItemList());

                // item_id로 공정 process_id 조회 → 공정 단계 목록 조회
                List<Map<String, Object>> stepList = prodService.getProcessStepList(dto.getItemId());
                request.setAttribute("stepList", stepList);

                request.getRequestDispatcher("/WEB-INF/views/P06_prod/prodDetail.jsp")
                       .forward(request, response);
                break;
            }

            /* ── 담당자 검색 AJAX ──────────────────────────────── */
            case "/api/empSearch": {
                response.setContentType("application/json;charset=UTF-8");

                String keyword = request.getParameter("keyword");
                if (keyword == null) keyword = "";
                int size = 5;
                int page = 1;
                try { size = Integer.parseInt(request.getParameter("size")); } catch (Exception e) {}
                try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception e) {}

                Map empMap     = prodService.getEmpList(keyword, page, size);
                List<Map> list = (List<Map>) empMap.get("list");
                int totalCount = (int) empMap.get("totalCount");

                StringBuilder sb = new StringBuilder();
                sb.append("{\"totalCount\":").append(totalCount).append(",\"list\":[");
                for (int i = 0; i < list.size(); i++) {
                    Map emp = list.get(i);
                    if (i > 0) sb.append(",");
                    sb.append("{")
                      .append("\"empId\":\"").append(emp.get("empId")).append("\",")
                      .append("\"ename\":\"").append(emp.get("ename")).append("\",")
                      .append("\"deptName\":\"").append(emp.get("deptName")).append("\"")
                      .append("}");
                }
                sb.append("]}");

                PrintWriter out = response.getWriter();
                out.print(sb.toString());
                out.flush();
                break;
            }

            default: {
                response.sendRedirect("list");
                break;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/list";

        switch (pathInfo) {

            /* ── 수정 ──────────────────────────────────────────── */
            case "/update": {
                ProdDTO dto = new ProdDTO();
                dto.setPlanId(request.getParameter("planId"));
                dto.setItemId(request.getParameter("itemId"));
                dto.setEmpId (request.getParameter("empId"));

                // 목표수량
                try { dto.setPlanQty(Integer.parseInt(request.getParameter("planQty"))); } catch (Exception e) {e.printStackTrace();}

                // 날짜: String → java.sql.Date
                try { dto.setPlanSdate(Date.valueOf(request.getParameter("planSdate"))); } catch (Exception e) {e.printStackTrace();}
                try { dto.setPlanEdate(Date.valueOf(request.getParameter("planEdate"))); } catch (Exception e) {e.printStackTrace();}

                // 상태 (detail 수정 모달에서만 넘어옴, list 수정 시엔 현재 DB 값 유지)
                // CASE WHEN prev_qty >= plan_qty THEN 2 ELSE ? END 에서 ? 에 들어갈 값
                // list 수정 모달엔 status 셀렉트가 없으므로 기본값 처리
                String statusParam = request.getParameter("status");
                if (statusParam != null && !statusParam.isEmpty()) {
                    try { dto.setStatus(Integer.parseInt(statusParam)); } catch (Exception e) {e.printStackTrace();}
                } else {
                    // status 파라미터가 없으면 현재 DB 값 유지를 위해 상세 조회 후 세팅
                    ProdDTO current = prodService.getPlanDetail(dto.getPlanId());
                    if (current != null) dto.setStatus(current.getStatus());
                }

                prodService.updatePlan(dto);
                response.sendRedirect("/mes/prod/list");
                break;
            }

            default: {
                response.sendRedirect("list");
                break;
            }
        }
    }
}