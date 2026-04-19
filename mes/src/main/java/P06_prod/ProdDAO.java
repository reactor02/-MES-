package P06_prod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ProdDAO {

    private Connection getConn() {
        Connection conn = null;
        try {
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /* ── [공통] ResultSet → ProdDTO 매핑 ─────────────────── */
    private ProdDTO mapRow(ResultSet rs) throws Exception {
        ProdDTO dto = new ProdDTO();
        dto.setPlanId(   rs.getString("plan_id")  );
        dto.setPlanQty(  rs.getInt("plan_qty")    );
        dto.setPrevQty(  rs.getInt("prev_qty")    );
        dto.setPlanSdate(rs.getDate("plan_sdate") );
        dto.setPlanEdate(rs.getDate("plan_edate") );
        dto.setStatus(   rs.getInt("status")      );
        dto.setItemId(   rs.getString("item_id")  );
        dto.setEmpId(    rs.getString("emp_id")   );
        dto.setItemName( rs.getString("item_name"));
        dto.setEname(    rs.getString("ename")    );

        int planQty = dto.getPlanQty();
        int prevQty = dto.getPrevQty();
        dto.setProgressPct(planQty > 0 ? (int)(prevQty * 100.0 / planQty) : 0);

        return dto;
    }

    /* ── [공통] production_plan + item + user_info JOIN SELECT절 ── */
    private String basePlanSelectSql() {
        return new StringBuilder()
            .append("SELECT pp.plan_id, ")
            .append("       pp.plan_qty, ")
            .append("       pp.prev_qty, ")
            .append("       pp.plan_sdate, ")
            .append("       pp.plan_edate, ")
            .append("       pp.status, ")
            .append("       pp.item_id, ")
            .append("       pp.emp_id, ")
            .append("       i.item_name, ")
            .append("       u.ename ")
            .append("FROM   production_plan pp ")
            .append("JOIN   item      i ON pp.item_id = i.item_id ")
            .append("JOIN   user_info u ON pp.emp_id  = u.emp_id ")
            .toString();
    }

    /* ── 목록 조회 (페이지네이션 + JOIN) ──────────────────── */
    public List<ProdDTO> selectAll(ProdDTO prodDTO) {
        List<ProdDTO> list = new ArrayList<>();

        String sql = new StringBuilder()
            .append("SELECT * FROM ( ")
            .append("  SELECT rownum AS rnum, p.* FROM ( ")
            .append(basePlanSelectSql())
            .append("    ORDER  BY pp.plan_sdate DESC ")
            .append("  ) p ")
            .append(") ")
            .append("WHERE rnum >= ? AND rnum <= ?")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prodDTO.getStart());
            ps.setInt(2, prodDTO.getEnd());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("ProdDAO list.size: " + list.size());
        return list;
    }

    /* ── 전체 건수 ────────────────────────────────────────── */
    public int selectTotal() {
        int totalCount = 0;
        String sql = "SELECT COUNT(*) cnt FROM production_plan";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) totalCount = rs.getInt("cnt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    /* ── 상세 조회 ────────────────────────────────────────── */
    public ProdDTO selectOne(String planId) {
        ProdDTO dto = null;

        String sql = new StringBuilder()
            .append(basePlanSelectSql())
            .append("WHERE  pp.plan_id = ?")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /* ── 수정 (진행률 100% 시 status → 2 자동처리) ────────── */
    public int updatePlan(ProdDTO dto) {
        String sql = new StringBuilder()
            .append("UPDATE production_plan ")
            .append("SET    item_id    = ?, ")
            .append("       emp_id     = ?, ")
            .append("       plan_qty   = ?, ")
            .append("       plan_sdate = TO_DATE(?, 'YYYY-MM-DD'), ")
            .append("       plan_edate = TO_DATE(?, 'YYYY-MM-DD'), ")
            .append("       status     = CASE WHEN prev_qty >= ? THEN 2 ELSE ? END ")
            .append("WHERE  plan_id    = ?")
            .toString();

        int result = 0;
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getItemId());
            ps.setString(2, dto.getEmpId());
            ps.setInt   (3, dto.getPlanQty());
            ps.setString(4, dto.getPlanSdate() != null ? dto.getPlanSdate().toString() : "");
            ps.setString(5, dto.getPlanEdate() != null ? dto.getPlanEdate().toString() : "");
            ps.setInt   (6, dto.getPlanQty());   // CASE 비교용 plan_qty
            ps.setInt   (7, dto.getStatus());    // 진행률 미만일 때 사용할 status
            ps.setString(8, dto.getPlanId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /* ── 대분류 목록 (GROUP_INFO JOIN) ───────────────────── */
    public List<Map<String, String>> selectGroupList() {
        List<Map<String, String>> list = new ArrayList<>();

        String sql = new StringBuilder()
            .append("SELECT DISTINCT i.g_id, g.itemgroup_name ")
            .append("FROM   item i ")
            .append("JOIN   group_info g ON i.g_id = g.g_id ")
            .append("ORDER  BY g.itemgroup_name")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("gId",           rs.getString("g_id"));
                map.put("itemgroupName", rs.getString("itemgroup_name"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ── 소분류 전체 목록 (JS 필터링용) ─────────────────── */
    public List<Map<String, String>> selectItemList() {
        List<Map<String, String>> list = new ArrayList<>();

        String sql = new StringBuilder()
            .append("SELECT item_id, item_name, g_id, unit, spec ")
            .append("FROM   item ")
            .append("ORDER  BY item_name")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("itemId",   rs.getString("item_id")  );
                map.put("itemName", rs.getString("item_name"));
                map.put("gId",      rs.getString("g_id")     );
                map.put("unit",     rs.getString("unit") != null ? rs.getString("unit") : "");
                map.put("spec",     rs.getString("spec") != null ? rs.getString("spec") : "");
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ── 담당자 검색 (auth >= 2, DEPT JOIN, 페이지네이션) ── */
    public List<Map<String, String>> selectEmpList(String keyword, int start, int end) {
        List<Map<String, String>> list = new ArrayList<>();

        String sql = new StringBuilder()
            .append("SELECT * FROM ( ")
            .append("  SELECT rownum AS rnum, p.* FROM ( ")
            .append("    SELECT u.emp_id, u.ename, d.dept_name ")
            .append("    FROM   user_info u ")
            .append("    JOIN   dept d ON u.dept_no = d.dept_no ")
            .append("    WHERE  u.auth >= 2 ")
            .append("    AND    (u.ename LIKE ? OR u.emp_id LIKE ?) ")
            .append("    ORDER  BY u.ename ")
            .append("  ) p ")
            .append(") ")
            .append("WHERE rnum >= ? AND rnum <= ?")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setInt(3, start);
            ps.setInt(4, end);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("empId",    rs.getString("emp_id")   );
                    map.put("ename",    rs.getString("ename")    );
                    map.put("deptName", rs.getString("dept_name"));
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ── 담당자 검색 건수 ────────────────────────────────── */
    public int selectEmpTotal(String keyword) {
        int total = 0;

        String sql = new StringBuilder()
            .append("SELECT COUNT(*) cnt ")
            .append("FROM   user_info u ")
            .append("JOIN   dept d ON u.dept_no = d.dept_no ")
            .append("WHERE  u.auth >= 2 ")
            .append("AND    (u.ename LIKE ? OR u.emp_id LIKE ?)")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) total = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    /* ── item_id로 process_id 조회 (공정 흐름도용) ───────── */
    public String selectProcessIdByItemId(String itemId) {
        String processId = null;

        String sql = new StringBuilder()
            .append("SELECT process_id ")
            .append("FROM   process ")
            .append("WHERE  item_id = ? ")
            .append("AND    ROWNUM = 1")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) processId = rs.getString("process_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processId;
    }

    /* ── process_id로 공정 단계 목록 조회 (공정 흐름도용) ── */
    public List<Map<String, Object>> selectProcessStepList(String processId) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = new StringBuilder()
            .append("SELECT seq, step_name ")
            .append("FROM   process_step ")
            .append("WHERE  process_id = ? ")
            .append("ORDER  BY seq, process_step_id")
            .toString();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, processId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("seq",      rs.getInt("seq"));
                    map.put("stepName", rs.getString("step_name"));
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}