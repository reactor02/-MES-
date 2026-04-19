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

import P11_masterdata.DTO.VendorDTO;

public class VendorDAO {

	public List<VendorDTO> selectAll() {
		List<VendorDTO> list = new ArrayList<VendorDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "select * from vendor ";
			query += "order by nvl(to_number(regexp_substr(vendor_id, '[0-9]+$')), 0), vendor_id";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String vendor_id = rs.getString("vendor_id");
				String vendor_name = rs.getString("vendor_name");
				String vendor_type = rs.getString("vendor_type");
				String phone_no = rs.getString("phone_no");
				String addr = rs.getString("addr");
				String emp_id = rs.getString("emp_id");

				VendorDTO vendorDTO = new VendorDTO();
				vendorDTO.setVendor_id(vendor_id);
				vendorDTO.setVendor_name(vendor_name);
				vendorDTO.setVendor_type(vendor_type);
				vendorDTO.setPhone_no(phone_no);
				vendorDTO.setAddr(addr);
				vendorDTO.setEmp_id(emp_id);

				list.add(vendorDTO);
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

	public int insertVendor(VendorDTO vendorDTO) {
		Connection conn = null;
		PreparedStatement ps = null;

		int result = -1;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "insert into vendor (vendor_id, vendor_name, vendor_type, phone_no, addr, emp_id)";
			query += " values (?,?,?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setString(1, vendorDTO.getVendor_id());
			ps.setString(2, vendorDTO.getVendor_name());
			ps.setString(3, vendorDTO.getVendor_type());
			ps.setString(4, vendorDTO.getPhone_no());
			ps.setString(5, vendorDTO.getAddr());
			ps.setString(6, vendorDTO.getEmp_id());

			result = ps.executeUpdate();
			System.out.println("insert 결과: " + result);

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

		return result;
	}

	public String selectNextVendorId() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String lastVendorId = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "select vendor_id ";
			query += "from ( ";
			query += "    select vendor_id ";
			query += "    from vendor ";
			query += "    where vendor_id is not null ";
			query += "    order by nvl(to_number(regexp_substr(vendor_id, '[0-9]+$')), 0) desc, vendor_id desc ";
			query += ") ";
			query += "where rownum = 1";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			if (rs.next()) {
				lastVendorId = rs.getString("vendor_id");
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

		if (lastVendorId == null || lastVendorId.trim().equals("")) {
			return "vend_1001";
		}

		lastVendorId = lastVendorId.trim();

		int splitIndex = lastVendorId.length();

		while (splitIndex > 0 && Character.isDigit(lastVendorId.charAt(splitIndex - 1))) {
			splitIndex--;
		}

		if (splitIndex == lastVendorId.length()) {
			return lastVendorId + "1";
		}

		String prefix = lastVendorId.substring(0, splitIndex);
		String numberPart = lastVendorId.substring(splitIndex);

		try {
			int currentNumber = Integer.parseInt(numberPart);
			return prefix + (currentNumber + 1);
		} catch (NumberFormatException e) {
			return lastVendorId + "1";
		}
	}

	public int updateVendor(VendorDTO vendorDTO) {
		Connection conn = null;
		PreparedStatement ps = null;

		int result = -1;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "";
			query += "update vendor ";
			query += "set vendor_type = ?, vendor_name = ?, phone_no = ?, addr = ?, emp_id = ? ";
			query += "where vendor_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, vendorDTO.getVendor_type());
			ps.setString(2, vendorDTO.getVendor_name());
			ps.setString(3, vendorDTO.getPhone_no());
			ps.setString(4, vendorDTO.getAddr());
			ps.setString(5, vendorDTO.getEmp_id());
			ps.setString(6, vendorDTO.getVendor_id());

			result = ps.executeUpdate();
			System.out.println("update 결과: " + result);
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

		return result;
	}
	public List<VendorDTO> selectVendorPageList(VendorDTO vendorDTO) {
		List<VendorDTO> list = new ArrayList<VendorDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String keyword = vendorDTO.getKeyword();
			String vendorType = vendorDTO.getVendorType();

			String query = "";
			query += "select * ";
			query += "from ( ";
			query += "    select row_number() over (order by nvl(to_number(regexp_substr(vendor_id, '[0-9]+$')), 0) desc, vendor_id desc) rn, ";
			query += "           vendor_id, vendor_name, vendor_type, phone_no, addr, emp_id ";
			query += "    from vendor ";
			query += "    where 1=1 ";

			if (vendorType != null && !vendorType.trim().equals("")) {
				query += "      and vendor_type = ? ";
			}
			if (keyword != null && !keyword.trim().equals("")) {
				query += "      and vendor_name like ? ";
			}

			query += ") ";
			query += "where rn between ? and ?";

			ps = conn.prepareStatement(query);

			int idx = 1;

			if (vendorType != null && !vendorType.trim().equals("")) {
				ps.setString(idx++, vendorType);
			}
			if (keyword != null && !keyword.trim().equals("")) {
				ps.setString(idx++, "%" + keyword.trim() + "%");
			}

			ps.setInt(idx++, vendorDTO.getStart());
			ps.setInt(idx, vendorDTO.getEnd());

			rs = ps.executeQuery();

			while (rs.next()) {
				VendorDTO dto = new VendorDTO();
				dto.setVendor_id(rs.getString("vendor_id"));
				dto.setVendor_name(rs.getString("vendor_name"));
				dto.setVendor_type(rs.getString("vendor_type"));
				dto.setPhone_no(rs.getString("phone_no"));
				dto.setAddr(rs.getString("addr"));
				dto.setEmp_id(rs.getString("emp_id"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return list;
	}

	public int selectVendorTotalCount(VendorDTO vendorDTO) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String keyword = vendorDTO.getKeyword();
			String vendorType = vendorDTO.getVendorType();

			String query = "";
			query += "select count(*) cnt ";
			query += "from vendor ";
			query += "where 1=1 ";

			if (vendorType != null && !vendorType.trim().equals("")) {
				query += "  and vendor_type = ? ";
			}
			if (keyword != null && !keyword.trim().equals("")) {
				query += "  and vendor_name like ? ";
			}

			ps = conn.prepareStatement(query);

			int idx = 1;

			if (vendorType != null && !vendorType.trim().equals("")) {
				ps.setString(idx++, vendorType);
			}
			if (keyword != null && !keyword.trim().equals("")) {
				ps.setString(idx++, "%" + keyword.trim() + "%");
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return count;
	}

	public int selectVendorTotalCountAll() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "select count(*) cnt from vendor";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return count;
	}

	public int selectVendorTypeCount(String vendorType) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String query = "select count(*) cnt from vendor where vendor_type = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, vendorType);

			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return count;
	}

}