package P05_stock;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/io")
public class Controller extends HttpServlet {
    StockService service = new StockService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        // 출고 등록 시 LOT 목록 AJAX 요청
        if ("getInList".equals(action)) {
            List<StockDTO> inList = service.getInList();

            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < inList.size(); i++) {
                StockDTO d = inList.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"io_id\":"     ).append("\"").append(d.getIo_id()).append("\",")
                  .append("\"item_id\":"   ).append("\"").append(d.getItem_id()).append("\",")
                  .append("\"item_name\":").append("\"").append(d.getItem_name() != null ? d.getItem_name() : "").append("\",")
                  .append("\"lot_id\":"    ).append("\"").append(d.getLot_id()).append("\",")
                  .append("\"lot_qty\":"   ).append(d.getLot_qty()).append(",")
                  // ★ 수정: spec 따옴표 추가 + null 처리
                  .append("\"spec\":"      ).append("\"").append(d.getSpec() != null ? d.getSpec() : "").append("\",")
                  .append("\"unit\":"      ).append("\"").append(d.getUnit() != null ? d.getUnit() : "").append("\"")
                  .append("}");
            }
            sb.append("]");

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(sb.toString());
            return;
        }
        if ("getLotList".equals(action)) {
            String keyword = request.getParameter("keyword");
            if (keyword != null && keyword.trim().isEmpty()) keyword = null;
            List<StockDTO> lotList = service.getAvailableLotList(keyword);

            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < lotList.size(); i++) {
                StockDTO d = lotList.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"lot_id\":"     ).append("\"").append(d.getLot_id()).append("\",")
                  .append("\"remaining_qty\":").append(d.getLot_qty()).append(",")
                  .append("\"expiry_date\":").append("\"").append(d.getExpiry_date() != null ? d.getExpiry_date().toString() : "").append("\",")
                  .append("\"item_id\":"    ).append("\"").append(d.getItem_id()).append("\",")
                  .append("\"item_name\":"  ).append("\"").append(d.getItem_name() != null ? d.getItem_name() : "").append("\",")
                  // ★ 수정: spec 따옴표 추가 + null 처리
                  .append("\"spec\":"       ).append("\"").append(d.getSpec() != null ? d.getSpec() : "").append("\",")
                  .append("\"unit\":"       ).append("\"").append(d.getUnit() != null ? d.getUnit() : "").append("\",")
                  .append("\"emp_id\":"     ).append("\"").append(d.getEmp_id()).append("\",")
                  .append("\"ename\":"      ).append("\"").append(d.getEname() != null ? d.getEname() : "").append("\"")
                  .append("}");
            }
            sb.append("]");

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(sb.toString());
            return;
        }
        if ("getUserList".equals(action)) {
            String keyword = request.getParameter("keyword");
            if (keyword != null && keyword.trim().isEmpty()) keyword = null;
            List<StockDTO> userList = service.getUserList(keyword);

            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < userList.size(); i++) {
                StockDTO d = userList.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"emp_id\":\"").append(d.getEmp_id()).append("\",")
                  .append("\"ename\":\"").append(d.getEname() != null ? d.getEname() : "").append("\",")
                  .append("\"dept_no\":\"").append(d.getDept_no() != null ? d.getDept_no() : "").append("\"")
                  .append("}");
            }
            sb.append("]");

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(sb.toString());
            return;
        }

        // 목록 페이지 조회
        response.setContentType("text/html; charset=UTF-8;");

        int size = 10;
        int page = 1;
        String sSize = request.getParameter("size");
        String sPage = request.getParameter("page");
        String filterIoType   = request.getParameter("filterIoType");
        String filterVendorId = request.getParameter("filterVendorId");
        String filterGId      = request.getParameter("filterGId");
        String filterItemId   = request.getParameter("filterItemId");
        String filterDateFrom = request.getParameter("filterDateFrom");
        String filterDateTo   = request.getParameter("filterDateTo");
        String filterKeyword  = request.getParameter("filterKeyword");
        String filterExpiry   = request.getParameter("filterExpiry");
        try { if (sSize != null) size = Integer.parseInt(sSize); } catch (Exception e) { e.printStackTrace(); }
        try { if (sPage != null) page = Integer.parseInt(sPage); } catch (Exception e) { e.printStackTrace(); }
        if (filterIoType   != null && filterIoType.isEmpty())   filterIoType   = null;
        if (filterVendorId != null && filterVendorId.isEmpty()) filterVendorId = null;
        if (filterGId      != null && filterGId.isEmpty())      filterGId      = null;
        if (filterItemId   != null && filterItemId.isEmpty())   filterItemId   = null;
        if (filterDateFrom != null && filterDateFrom.isEmpty()) filterDateFrom = null;
        if (filterDateTo   != null && filterDateTo.isEmpty())   filterDateTo   = null;
        if (filterKeyword  != null && filterKeyword.isEmpty())  filterKeyword  = null;
        if (filterExpiry   != null && filterExpiry.isEmpty())   filterExpiry   = null;

        StockDTO stockDTO = new StockDTO();
        stockDTO.setSize(size);
        stockDTO.setPage(page);
        stockDTO.setFilterIoType(filterIoType);
        stockDTO.setFilterVendorId(filterVendorId);
        stockDTO.setFilterGId(filterGId);
        stockDTO.setFilterItemId(filterItemId);
        stockDTO.setFilterDateFrom(filterDateFrom);
        stockDTO.setFilterDateTo(filterDateTo);
        stockDTO.setFilterKeyword(filterKeyword);
        stockDTO.setFilterExpiry(filterExpiry);

        Map map = service.getListStock(stockDTO);
        map.put("size",       size);
        map.put("page",       page);
        map.put("vendorList", service.getVendorList());
        map.put("itemList",   service.getItemList());
        map.put("groupList",  service.getGroupList());
        map.put("filterIoType",   filterIoType);
        map.put("filterVendorId", filterVendorId);
        map.put("filterGId",      filterGId);
        map.put("filterItemId",   filterItemId);
        map.put("filterDateFrom", filterDateFrom);
        map.put("filterDateTo",   filterDateTo);
        map.put("filterKeyword",  filterKeyword);
        map.put("filterExpiry",   filterExpiry);
        map.put("expiryWarnCount", service.getExpiryWarnCount());
        map.put("expiryOverCount", service.getExpiryOverCount());

        request.setAttribute("map", map);
        request.getRequestDispatcher("WEB-INF/views/P05_stock/io.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        StockDTO dto = new StockDTO();
        try {
            dto.setIo_type(Integer.parseInt(request.getParameter("io_type")));
            dto.setIo_reason(request.getParameter("io_reason"));
            dto.setVender_id(request.getParameter("vender_id"));
            dto.setItem_id(request.getParameter("item_id"));
            dto.setLot_qty(Integer.parseInt(request.getParameter("lot_qty")));
            dto.setIo_qty(Integer.parseInt(request.getParameter("lot_qty")));
            String ioTimeStr = request.getParameter("io_time");
            ZonedDateTime kst = LocalDateTime.parse(ioTimeStr + "T00:00:00")
                                             .atZone(ZoneId.of("Asia/Seoul"));
            dto.setIo_time(Timestamp.from(kst.toInstant()));

            // 출고 시 기존 lot_id 사용
            String lotId = request.getParameter("lot_id");
            if (lotId != null && !lotId.isEmpty()) {
                dto.setLot_id(lotId);
            }

            String expiryStr = request.getParameter("expiry_date");
            if (expiryStr != null && !expiryStr.isEmpty()) {
                dto.setExpiry_date(Date.valueOf(expiryStr));
            }

            dto.setEmp_id(request.getParameter("emp_id"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dto.getIo_type() == 1) {  // 출고 시 재고 검사
            int currentStock = service.getStockNo(dto.getItem_id());
            if (currentStock - dto.getLot_qty() < 0) {
                request.setAttribute("errorMsg",
                    "재고 부족입니다. (현재 재고: " + currentStock + ")");
                request.getRequestDispatcher("WEB-INF/views/P05_stock/io.jsp")
                       .forward(request, response);
                return;
            }
        }

        service.insert(dto);
        response.sendRedirect("/mes/io");
    }
}