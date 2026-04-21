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

import P11_masterdata.DTO.Item_masterDTO;

public class Item_masterDAO {

	public List<Item_masterDTO> selectItemList() {

		List<Item_masterDTO> list = new ArrayList<Item_masterDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "SELECT i.item_id, ";
			query += "       i.item_name, ";
			query += "       i.unit, ";
			query += "       i.spec, ";
			query += "       i.g_id, ";
			query += "       NVL(i.safe_qty, 0) AS safe_qty, ";
			query += "       NVL(i.pay, 0) AS pay, ";
			query += "       g.itemgroup_name ";
			query += "FROM item i ";
			query += "LEFT OUTER JOIN group_info g ";
			query += "  ON i.g_id = g.g_id ";
			query += "ORDER BY i.ROWID DESC";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				Item_masterDTO item_masterDTO = new Item_masterDTO();
				item_masterDTO.setItem_id(rs.getString("ITEM_ID"));
				item_masterDTO.setItem_name(rs.getString("ITEM_NAME"));
				item_masterDTO.setUnit(rs.getString("UNIT"));
				item_masterDTO.setSpec(rs.getString("SPEC"));
				item_masterDTO.setG_id(rs.getInt("G_ID"));
				item_masterDTO.setSafe_qty(rs.getInt("safe_qty"));
				item_masterDTO.setPay(rs.getInt("pay"));
				item_masterDTO.setItemgroup_name(rs.getString("ITEMGROUP_NAME"));

				list.add(item_masterDTO);
			}
		} catch (NamingException e) {
			e.printStackTrace();
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

	public int insertItem(Item_masterDTO item_masterDTO) {
		Connection conn = null;
		PreparedStatement psItem = null;
		PreparedStatement psStock = null;
		PreparedStatement psMax = null;
		ResultSet rs = null;

		int result = -1;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			conn.setAutoCommit(false);

			String itemQuery = "";
			itemQuery += "INSERT INTO item (item_id, item_name, unit, spec, g_id, safe_qty, pay) ";
			itemQuery += "VALUES (?, ?, ?, ?, ?, ?, ?)";

			psItem = conn.prepareStatement(itemQuery);
			psItem.setString(1, item_masterDTO.getItem_id());
			psItem.setString(2, item_masterDTO.getItem_name());
			psItem.setString(3, item_masterDTO.getUnit());
			psItem.setString(4, item_masterDTO.getSpec());
			psItem.setInt(5, item_masterDTO.getG_id());
			psItem.setInt(6, item_masterDTO.getSafe_qty());
			psItem.setInt(7, item_masterDTO.getPay());

			int itemResult = psItem.executeUpdate();

			String maxQuery = "";
			maxQuery += "SELECT MAX(TO_NUMBER(SUBSTR(stock_id, 5))) AS max_num ";
			maxQuery += "FROM stock";

			psMax = conn.prepareStatement(maxQuery);
			rs = psMax.executeQuery();

			int nextStockNum = 1001;
			if (rs.next()) {
				int maxNum = rs.getInt("max_num");
				if (!rs.wasNull()) {
					nextStockNum = maxNum + 1;
				}
			}

			String stockId = "sto_" + nextStockNum;

			String stockQuery = "";
			stockQuery += "INSERT INTO stock (stock_id, item_id, stock_no, deleted) ";
			stockQuery += "VALUES (?, ?, ?, ?)";

			psStock = conn.prepareStatement(stockQuery);
			psStock.setString(1, stockId);
			psStock.setString(2, item_masterDTO.getItem_id());
			psStock.setInt(3, 0);
			psStock.setString(4, "N");

			int stockResult = psStock.executeUpdate();

			if (itemResult > 0 && stockResult > 0) {
				conn.commit();
				result = 1;
			} else {
				conn.rollback();
				result = 0;
			}

			System.out.println("item insert 결과: " + itemResult);
			System.out.println("stock insert 결과: " + stockResult);
			System.out.println("생성된 stock_id: " + stockId);

		} catch (Exception e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psMax != null) {
				try {
					psMax.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psStock != null) {
				try {
					psStock.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psItem != null) {
				try {
					psItem.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public int updateItem(Item_masterDTO item_masterDTO) {
		Connection conn = null;
		PreparedStatement ps = null;

		int update_result = -1;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "UPDATE item ";
			query += "SET g_id = ?, unit = ?, spec = ?, item_name = ?, safe_qty = ?, pay = ? ";
			query += "WHERE item_id = ?";

			ps = conn.prepareStatement(query);

			ps.setInt(1, item_masterDTO.getG_id());
			ps.setString(2, item_masterDTO.getUnit());
			ps.setString(3, item_masterDTO.getSpec());
			ps.setString(4, item_masterDTO.getItem_name());
			ps.setInt(5, item_masterDTO.getSafe_qty());
			ps.setInt(6, item_masterDTO.getPay());
			ps.setString(7, item_masterDTO.getItem_id());

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

	public int selectItemTotalCountAll() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int totalCount = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "SELECT COUNT(*) cnt ";
			query += "FROM item";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			if (rs.next()) {
				totalCount = rs.getInt("cnt");
			}

		} catch (NamingException e) {
			e.printStackTrace();
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

	public int selectItemTotalCount(Item_masterDTO item_masterDTO) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int totalCount = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String groupKeyword = item_masterDTO.getGroupKeyword();
			String keyword = item_masterDTO.getKeyword();

			String query = "";
			query += "SELECT COUNT(*) cnt ";
			query += "FROM item i ";
			query += "WHERE 1=1 ";

			if ("30".equals(groupKeyword)) {
				query += "AND i.g_id = 30 ";
			} else if ("20".equals(groupKeyword)) {
				query += "AND i.g_id = 20 ";
			} else if ("10".equals(groupKeyword)) {
				query += "AND i.g_id = 10 ";
			}

			if (keyword != null && !keyword.trim().equals("")) {
				query += "AND i.item_name LIKE ? ";
			}

			ps = conn.prepareStatement(query);

			int idx = 1;
			if (keyword != null && !keyword.trim().equals("")) {
				ps.setString(idx++, "%" + keyword.trim() + "%");
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				totalCount = rs.getInt("cnt");
			}

		} catch (NamingException e) {
			e.printStackTrace();
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

	public int selectItemGroupCount(int gId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "SELECT COUNT(*) cnt ";
			query += "FROM item ";
			query += "WHERE g_id = ?";

			ps = conn.prepareStatement(query);
			ps.setInt(1, gId);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt("cnt");
			}

		} catch (NamingException e) {
			e.printStackTrace();
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

		return count;
	}

	public int selectItemOrderCount(String itemId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int orderCount = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "SELECT COUNT(*) cnt ";
			query += "FROM item ";
			query += "WHERE item_id <= ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, itemId);
			rs = ps.executeQuery();

			if (rs.next()) {
				orderCount = rs.getInt("cnt");
			}

		} catch (NamingException e) {
			e.printStackTrace();
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

		return orderCount;
	}

	public List<Item_masterDTO> selectItemPageList(Item_masterDTO item_masterDTO) {
		List<Item_masterDTO> list = new ArrayList<Item_masterDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String groupKeyword = item_masterDTO.getGroupKeyword();
			String keyword = item_masterDTO.getKeyword();

			String query = "";
			query += "SELECT * ";
			query += "FROM ( ";
			query += "    SELECT ROWNUM rnum, t.* ";
			query += "    FROM ( ";
			query += "        SELECT i.item_id, ";
			query += "               i.item_name, ";
			query += "               i.unit, ";
			query += "               i.spec, ";
			query += "               i.g_id, ";
			query += "               NVL(i.safe_qty, 0) AS safe_qty, ";
			query += "               NVL(i.pay, 0) AS pay, ";
			query += "               g.itemgroup_name ";
			query += "        FROM item i ";
			query += "        LEFT OUTER JOIN group_info g ";
			query += "          ON i.g_id = g.g_id ";
			query += "        WHERE 1=1 ";

			if ("30".equals(groupKeyword)) {
				query += "AND i.g_id = 30 ";
			} else if ("20".equals(groupKeyword)) {
				query += "AND i.g_id = 20 ";
			} else if ("10".equals(groupKeyword)) {
				query += "AND i.g_id = 10 ";
			}

			if (keyword != null && !keyword.trim().equals("")) {
				query += "AND i.item_name LIKE ? ";
			}

			query += "        ORDER BY i.ROWID DESC ";
			query += "    ) t ";
			query += "    WHERE ROWNUM <= ? ";
			query += ") ";
			query += "WHERE rnum >= ?";

			ps = conn.prepareStatement(query);

			int idx = 1;
			if (keyword != null && !keyword.trim().equals("")) {
				ps.setString(idx++, "%" + keyword.trim() + "%");
			}
			ps.setInt(idx++, item_masterDTO.getEnd());
			ps.setInt(idx++, item_masterDTO.getStart());

			rs = ps.executeQuery();

			while (rs.next()) {
				Item_masterDTO dto = new Item_masterDTO();
				dto.setItem_id(rs.getString("item_id"));
				dto.setItem_name(rs.getString("item_name"));
				dto.setUnit(rs.getString("unit"));
				dto.setSpec(rs.getString("spec"));
				dto.setG_id(rs.getInt("g_id"));
				dto.setSafe_qty(rs.getInt("safe_qty"));
				dto.setPay(rs.getInt("pay"));
				dto.setItemgroup_name(rs.getString("itemgroup_name"));
				list.add(dto);
			}

		} catch (NamingException e) {
			e.printStackTrace();
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
}
