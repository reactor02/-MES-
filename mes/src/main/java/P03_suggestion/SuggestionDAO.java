package P03_suggestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class SuggestionDAO {

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

    // 검색 조건 반영한 총건수
    public int selectTotal(SuggestionDTO dto) {
        int totalCount = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) cnt FROM suggestion WHERE 1=1 ");
        if (dto.getSearchKeyword() != null && !dto.getSearchKeyword().trim().isEmpty()) {
            sql.append(" AND title LIKE ? ");
        }
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (dto.getSearchKeyword() != null && !dto.getSearchKeyword().trim().isEmpty()) {
                ps.setString(idx++, "%" + dto.getSearchKeyword().trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalCount = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    public List<SuggestionDTO> selectList(SuggestionDTO dto) {
        List<SuggestionDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (")
           .append("  SELECT rownum AS rnum, s.* FROM (")
           .append("    SELECT s.boardno, s.title, s.ctime,")
           .append("           TO_CHAR(FROM_TZ(CAST(s.ctime AS TIMESTAMP), 'UTC') AT TIME ZONE 'Asia/Seoul', 'YYYY-MM-DD HH24:MI') AS ctime_str,")
           .append("           s.emp_id, s.complete, s.views, u.ename")
           .append("    FROM suggestion s")
           .append("    LEFT JOIN user_info u ON s.emp_id = u.emp_id")
           .append("    WHERE 1=1 ");
        if (dto.getSearchKeyword() != null && !dto.getSearchKeyword().trim().isEmpty()) {
            sql.append("    AND s.title LIKE ? ");
        }
        sql.append("    ORDER BY TO_NUMBER(SUBSTR(s.boardno, 6)) DESC")
           .append("  ) s")
           .append(") WHERE rnum >= ? AND rnum <= ?");

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (dto.getSearchKeyword() != null && !dto.getSearchKeyword().trim().isEmpty()) {
                ps.setString(idx++, "%" + dto.getSearchKeyword().trim() + "%");
            }
            ps.setInt(idx++, dto.getStart());
            ps.setInt(idx++, dto.getEnd());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SuggestionDTO row = new SuggestionDTO();
                    row.setBoardno(rs.getString("boardno"));
                    row.setTitle(rs.getString("title"));
                    row.setCtime(rs.getTimestamp("ctime"));
                    row.setCtimeStr(rs.getString("ctime_str"));
                    row.setEmpId(rs.getString("emp_id"));
                    row.setComplete(rs.getInt("complete"));
                    row.setViews(rs.getInt("views"));
                    row.setEname(rs.getString("ename"));
                    list.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public SuggestionDTO selectOne(String boardno) {
        SuggestionDTO dto = null;
        // ctime_kst_ms: 한국시간으로 보정된 ctime의 epoch 밀리초.
        // JDBC Timestamp.getTime()의 타임존 해석 이슈를 우회하기 위해 Oracle에서 직접 계산.
        // (그 시각을 UTC로 파싱 → 1970-01-01 00:00:00 UTC와의 차이를 DAY로 뽑아 ms로 환산)
        String sql =
            "SELECT s.boardno, s.title, s.content, s.ctime, s.mtime,"
          + "       TO_CHAR(FROM_TZ(CAST(s.ctime AS TIMESTAMP), 'UTC') AT TIME ZONE 'Asia/Seoul', 'YYYY-MM-DD HH24:MI') AS ctime_str,"
          + "       TO_CHAR(FROM_TZ(CAST(s.mtime AS TIMESTAMP), 'UTC') AT TIME ZONE 'Asia/Seoul', 'YYYY-MM-DD HH24:MI') AS mtime_str,"
          + "       ROUND("
          + "         ("
          + "           CAST(FROM_TZ(CAST(s.ctime AS TIMESTAMP), 'UTC') AT TIME ZONE 'Asia/Seoul' AS DATE)"
          + "           - TO_DATE('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS')"
          + "         ) * 86400000"
          + "       ) AS ctime_kst_ms,"
          + "       s.views, s.emp_id, s.complete, s.origin_name, s.save_name, u.ename"
          + " FROM suggestion s"
          + " LEFT JOIN user_info u ON s.emp_id = u.emp_id"
          + " WHERE s.boardno = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new SuggestionDTO();
                    dto.setBoardno(rs.getString("boardno"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCtime(rs.getTimestamp("ctime"));
                    dto.setMtime(rs.getTimestamp("mtime"));
                    dto.setCtimeStr(rs.getString("ctime_str"));
                    dto.setMtimeStr(rs.getString("mtime_str"));
                    dto.setCtimeKstMs(rs.getLong("ctime_kst_ms"));
                    dto.setViews(rs.getInt("views"));
                    dto.setEmpId(rs.getString("emp_id"));
                    dto.setComplete(rs.getInt("complete"));
                    dto.setOriginName(rs.getString("origin_name"));
                    dto.setSaveName(rs.getString("save_name"));
                    dto.setEname(rs.getString("ename"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public void updateViews(String boardno) {
        String sql = "UPDATE suggestion SET views = views + 1 WHERE boardno = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int updateComplete(String boardno) {
        int result = 0;
        // complete 2(답변달림) 상태일 때만 1(검토완료)로 변경
        String sql = "UPDATE suggestion SET complete = 1, mtime = SYSDATE WHERE boardno = ? AND complete = 2";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardno);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 댓글 등록 시 complete 0 → 2 자동 변경 (작성자 본인 제외)
    public void updateCompleteToAnswered(String boardno) {
        String sql = "UPDATE suggestion SET complete = 2, mtime = SYSDATE WHERE boardno = ? AND complete = 0";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int insert(SuggestionDTO dto) {
        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement("SELECT 'sugg_' || sugg_seq.nextval AS boardno FROM dual");
            rs = ps.executeQuery();
            String boardno = null;
            if (rs.next()) boardno = rs.getString("boardno");
            rs.close();
            ps.close();

            ps = conn.prepareStatement(
                "INSERT INTO suggestion"
              + " (boardno, title, content, ctime, mtime, views, emp_id, complete, deleted, origin_name, save_name)"
              + " VALUES (?, ?, ?, SYSDATE, SYSDATE, 0, ?, 0, 'N', ?, ?)"
            );
            ps.setString(1, boardno);
            ps.setString(2, dto.getTitle());
            ps.setString(3, dto.getContent());
            ps.setString(4, dto.getEmpId());
            ps.setString(5, dto.getOriginName());
            ps.setString(6, dto.getSaveName());
            result = ps.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (rs   != null) rs.close();   } catch (Exception e) { e.printStackTrace(); }
            try { if (ps   != null) ps.close();   } catch (Exception e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        return result;
    }

    public int delete(String boardno) {
        int result = 0;
        String sql = "DELETE FROM suggestion WHERE boardno = ?";
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardno);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<CommentDTO> selectCommentList(String boardno) {
        List<CommentDTO> list = new ArrayList<>();
        String sql =
            "SELECT comno, parent_comno, content, ctime," +
            "       TO_CHAR(FROM_TZ(CAST(ctime AS TIMESTAMP), 'UTC') AT TIME ZONE 'Asia/Seoul', 'YYYY-MM-DD HH24:MI') AS ctime_str," +
            "       boardno, LEVEL - 1 AS depth " +
            "FROM comment_info " +
            "WHERE boardno = ? " +
            "START WITH parent_comno IS NULL " +
            "CONNECT BY PRIOR comno = parent_comno " +
            "ORDER SIBLINGS BY ctime";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CommentDTO dto = new CommentDTO();
                    dto.setComno(rs.getString("comno"));
                    dto.setParentComno(rs.getString("parent_comno"));
                    dto.setContent(rs.getString("content"));
                    dto.setCtime(rs.getTimestamp("ctime"));
                    dto.setCtimeStr(rs.getString("ctime_str"));
                    dto.setBoardno(rs.getString("boardno"));
                    dto.setDepth(rs.getInt("depth"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insertComment(String boardno, String content, String parentComno) {
        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement("SELECT 'cmt_' || comment_seq.nextval AS comno FROM dual");
            rs = ps.executeQuery();
            String comno = null;
            if (rs.next()) comno = rs.getString("comno");
            rs.close();
            ps.close();

            ps = conn.prepareStatement(
                "INSERT INTO comment_info (comno, content, ctime, mtime, boardno, parent_comno) " +
                "VALUES (?, ?, SYSDATE, SYSDATE, ?, ?)"
            );
            ps.setString(1, comno);
            ps.setString(2, content);
            ps.setString(3, boardno);
            ps.setString(4, parentComno);
            result = ps.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (rs   != null) rs.close();   } catch (Exception e) { e.printStackTrace(); }
            try { if (ps   != null) ps.close();   } catch (Exception e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        return result;
    }
}