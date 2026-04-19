package P11_masterdata.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import P11_masterdata.DTO.ProcessDTO;

public class ProcessDAO {

	private Connection getConnection() throws NamingException, SQLException {
		Context ctx = new InitialContext();
		DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
		return dataFactory.getConnection();
	}

	public List<ProcessDTO> selectProcessList() {

		List<ProcessDTO> list = new ArrayList<ProcessDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "";
			query += "SELECT process_id, ";
			query += "       process_name, ";
			query += "       seq, ";
			query += "       item_id, ";
			query += "       process_info, ";
			query += "       process_type ";
			query += "FROM process ";
			query += "ORDER BY process_id";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ProcessDTO dto = new ProcessDTO();
				dto.setProcess_id(rs.getString("process_id"));
				dto.setProcess_name(rs.getString("process_name"));
				dto.setSeq(rs.getInt("seq"));
				dto.setItem_id(rs.getString("item_id"));
				dto.setProcess_info(rs.getString("process_info"));
				dto.setProcess_type(rs.getString("process_type"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public int selectProcessTotalCount() {
		return selectProcessTotalCount(null);
	}

	public int selectProcessTotalCount(ProcessDTO processDTO) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int totalCount = 0;

		try {
			conn = getConnection();

			String keyword = "";
			if (processDTO != null && processDTO.getKeyword() != null) {
				keyword = processDTO.getKeyword().trim();
			}

			String query = "";
			query += "SELECT COUNT(*) cnt ";
			query += "FROM process ";
			if (!"".equals(keyword)) {
				query += "WHERE process_name LIKE ? ";
			}

			ps = conn.prepareStatement(query);

			if (!"".equals(keyword)) {
				ps.setString(1, "%" + keyword + "%");
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				totalCount = rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return totalCount;
	}

	public List<ProcessDTO> selectProcessPageList(ProcessDTO processDTO) {
		List<ProcessDTO> list = new ArrayList<ProcessDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String keyword = "";
			if (processDTO != null && processDTO.getKeyword() != null) {
				keyword = processDTO.getKeyword().trim();
			}

			String query = "";
			query += "SELECT * ";
			query += "FROM ( ";
			query += "    SELECT ROWNUM rnum, t.* ";
			query += "    FROM ( ";
			query += "        SELECT process_id, ";
			query += "               process_name, ";
			query += "               seq, ";
			query += "               item_id, ";
			query += "               process_info, ";
			query += "               process_type ";
			query += "        FROM process ";
			if (!"".equals(keyword)) {
				query += "        WHERE process_name LIKE ? ";
			}
			query += "        ORDER BY process_id ";
			query += "    ) t ";
			query += "    WHERE ROWNUM <= ? ";
			query += ") ";
			query += "WHERE rnum >= ?";

			ps = conn.prepareStatement(query);

			int index = 1;
			if (!"".equals(keyword)) {
				ps.setString(index++, "%" + keyword + "%");
			}
			ps.setInt(index++, processDTO.getEnd());
			ps.setInt(index, processDTO.getStart());

			rs = ps.executeQuery();

			while (rs.next()) {
				ProcessDTO dto = new ProcessDTO();
				dto.setProcess_id(rs.getString("process_id"));
				dto.setProcess_name(rs.getString("process_name"));
				dto.setSeq(rs.getInt("seq"));
				dto.setItem_id(rs.getString("item_id"));
				dto.setProcess_info(rs.getString("process_info"));
				dto.setProcess_type(rs.getString("process_type"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public List<ProcessDTO> selectProcessStepList(String processId) {

		List<ProcessDTO> list = new ArrayList<ProcessDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "";
			query += "SELECT process_step_id, ";
			query += "       seq, ";
			query += "       step_name, ";
			query += "       process_id ";
			query += "FROM process_step ";
			query += "WHERE process_id = ? ";
			query += "ORDER BY seq, process_step_id";

			ps = conn.prepareStatement(query);
			ps.setString(1, processId);

			rs = ps.executeQuery();

			while (rs.next()) {
				ProcessDTO dto = new ProcessDTO();
				dto.setProcess_step_id(rs.getString("process_step_id"));
				dto.setSeq(rs.getInt("seq"));
				dto.setStep_name(rs.getString("step_name"));
				dto.setProcess_id(rs.getString("process_id"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public String selectNextProcessId() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String nextProcessId = "proc_1001";

		try {
			conn = getConnection();

			String query = "";
			query += "SELECT NVL(MAX(TO_NUMBER(SUBSTR(process_id, 6))), 1000) AS max_num ";
			query += "FROM process ";
			query += "WHERE process_id LIKE 'proc_%'";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			if (rs.next()) {
				nextProcessId = "proc_" + (rs.getInt("max_num") + 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return nextProcessId;
	}

	public int insertProcess(ProcessDTO processDTO) {
		Connection conn = null;
		PreparedStatement ps = null;

		int result = 0;

		try {
			conn = getConnection();

			String insertQuery = "";
			insertQuery += "INSERT INTO process (process_id, process_name, seq, item_id, process_info, process_type) ";
			insertQuery += "VALUES (?, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(insertQuery);
			ps.setString(1, processDTO.getProcess_id());
			ps.setString(2, processDTO.getProcess_name());
			ps.setInt(3, processDTO.getSeq());
			ps.setString(4, processDTO.getItem_id());
			ps.setString(5, processDTO.getProcess_info());
			ps.setString(6, processDTO.getProcess_type());

			result = ps.executeUpdate();
			System.out.println("process insert 결과: " + result);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public ProcessDTO selectProcessDetail(String processId) {
		ProcessDTO dto = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "";
			query += "SELECT process_id, ";
			query += "       process_name, ";
			query += "       seq, ";
			query += "       item_id, ";
			query += "       process_info, ";
			query += "       process_type ";
			query += "FROM process ";
			query += "WHERE process_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, processId);

			rs = ps.executeQuery();

			if (rs.next()) {
				dto = new ProcessDTO();
				dto.setProcess_id(rs.getString("process_id"));
				dto.setProcess_name(rs.getString("process_name"));
				dto.setSeq(rs.getInt("seq"));
				dto.setItem_id(rs.getString("item_id"));
				dto.setProcess_info(rs.getString("process_info"));
				dto.setProcess_type(rs.getString("process_type"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return dto;
	}

	public List<ProcessDTO> selectMaterialList(String processId) {
		List<ProcessDTO> list = new ArrayList<ProcessDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "";
			query += "SELECT i.item_id, ";
			query += "       i.item_name, ";
			query += "       i.unit ";
			query += "FROM process p ";
			query += "JOIN item i ";
			query += "  ON p.item_id = i.item_id ";
			query += "WHERE p.process_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, processId);

			rs = ps.executeQuery();

			while (rs.next()) {
				ProcessDTO dto = new ProcessDTO();
				dto.setItem_id(rs.getString("item_id"));
				dto.setItem_name(rs.getString("item_name"));
				dto.setUnit(rs.getString("unit"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public List<ProcessDTO> selectEquipmentList(String processId) {
		List<ProcessDTO> list = new ArrayList<ProcessDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "";
			query += "SELECT e.eq_id, ";
			query += "       e.eq_name ";
			query += "FROM equipment e ";
			query += "WHERE e.eq_name LIKE '%' || ( ";
			query += "    SELECT p.process_name ";
			query += "    FROM process p ";
			query += "    WHERE p.process_id = ? ";
			query += ") || '%' ";
			query += "AND (e.deleted IS NULL OR e.deleted <> 'Y') ";
			query += "ORDER BY e.eq_id";

			ps = conn.prepareStatement(query);
			ps.setString(1, processId);

			rs = ps.executeQuery();

			while (rs.next()) {
				ProcessDTO dto = new ProcessDTO();
				dto.setEq_id(rs.getString("eq_id"));
				dto.setEq_name(rs.getString("eq_name"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public int updateProcess(ProcessDTO processDTO) {
		Connection conn = null;
		PreparedStatement ps = null;

		int update_result = -1;

		try {
			conn = getConnection();

			String query = "UPDATE process";
			query += " SET process_name = ?, process_info = ?, process_type = ?";
			query += " WHERE process_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, processDTO.getProcess_name());
			ps.setString(2, processDTO.getProcess_info());
			ps.setString(3, processDTO.getProcess_type());
			ps.setString(4, processDTO.getProcess_id());

			update_result = ps.executeUpdate();
			System.out.println("update 결과: " + update_result);

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return update_result;
	}
}
