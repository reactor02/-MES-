package P01_auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import P02_dashboard.DashDTO;
import P10_report.ReportDTO;

public class LoginDAO {

	public List<LoginDTO> login(LoginDTO d) {
		System.out.println("/login DAO.login 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " select * from user_info u ";
			query += " left outer join dept d on u.dept_no = d.dept_no ";
			query += " where u.emp_id = ? ";
			query += " and u.password = ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, d.getEmpid());
			ps.setString(2, d.getPassword());

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();
				dto.setEmpid(rs.getString("emp_id"));
				dto.setPassword(rs.getString("password"));
				dto.setEname(rs.getString("ename"));
				dto.setPhone(rs.getInt("phone"));
				dto.setDeptname(rs.getString("DEPT_NAME"));
				dto.setDeptno(rs.getString("DEPT_NO"));
				dto.setAuth(rs.getInt("auth"));

				dto.setHiredate(rs.getDate("hiredate"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("Login 함수 실행 : " + list.size());
		return list;

	}

	public int join(LoginDTO d) {
		System.out.println("/login DAO.join 실행");

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " INSERT INTO USER_INFO ( EMP_ID, ENAME, PHONE, PASSWORD, AUTH, HIREDATE, RETIREDATE, RETIRE, MGR, LICENSE, DEPT_NO) ";
			query += " VALUES ( 'user_' || user_seq.NEXTVAL, ?, ?, ?, null, sysdate, null, null, ?, ?, ? ) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, d.getEname());
			ps.setLong(2, d.getPhone());
			ps.setString(3, d.getPassword());
			ps.setString(4, d.getMgr());
			ps.setString(5, d.getLicense());
			ps.setString(6, d.getDeptno());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("회원가입 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	public LoginDTO empno(LoginDTO d) {
		System.out.println("/login DAO.empno 실행");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// 바구니 소환
		LoginDTO dto = new LoginDTO();

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " select * from user_info u ";
			query += " left outer join dept d on u.dept_no = d.dept_no ";
			query += " where u.ename = ? ";
			query += " and u.password = ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, d.getEname());
			ps.setString(2, d.getPassword());

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			// 신입사원에게 알려줄 내용들
			while (rs.next()) {
				dto.setEname(rs.getString("ename"));
				dto.setEmpid(rs.getString("emp_id"));
				dto.setPassword(rs.getString("password"));
				dto.setDeptno(rs.getString("DEPT_NO"));
				dto.setDeptname(rs.getString("DEPT_NAME"));
				dto.setHiredate(rs.getDate("hiredate"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("Login 함수 실행 : " + dto.toString());
		return dto;

	}

	public LoginDTO editCheck(LoginDTO d) {
		System.out.println("/login DAO.editCheck 실행");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// 바구니 소환
		LoginDTO dto = new LoginDTO();

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " select ename, phone, password from user_info ";
			query += " where emp_id = ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, d.getEmpid());

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용
			while (rs.next()) {
				dto.setEname(rs.getString("ename"));
				dto.setPhone(rs.getLong("phone"));
				dto.setPassword(rs.getString("password"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return dto;

	}

	public int edit(LoginDTO d) {
		System.out.println("/login DAO.edit 실행");

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " update user_info ";
			query += " set ename = ?,  phone = ?, password = ? ";
			query += " where emp_id = ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, d.getEname());
			ps.setLong(2, d.getPhone());
			ps.setString(3, d.getPassword());
			ps.setString(4, d.getEmpid());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("정보 수정 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	public int changepw(LoginDTO d) {
		System.out.println("/login DAO.changepw 실행");

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " update user_info ";
			query += " set password = ? ";
			query += " where emp_id = ? ";
			// query += " and phone = ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, d.getPassword());
			ps.setString(2, d.getEmpid());
			// ps.setLong(3, d.getPhone());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("비밀번호 변경 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	public int readEmp() {
		System.out.println("/login DAO.readEmp 실행");

		int count = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " select u.emp_id, u.ename, d.dept_name from USER_INFO u  ";
			query += " left outer join DEPT d on u.dept_no = d.dept_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				// 숫자세기
				count++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(" readEmp 함수 실행 : " + count);
		return count;

	}

	public List<LoginDTO> paging(int start_no, int countPageNo) {
		System.out.println("/login DAO.paging 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT * FROM ( ";
			query += " SELECT rownum AS rn, a.* FROM ( ";
			query += " SELECT u.emp_id, u.ename, d.dept_name, u.auth ";
			query += " FROM USER_INFO u ";
			query += " LEFT OUTER JOIN DEPT d ON u.dept_no = d.dept_no ";
			query += " ORDER BY u.emp_id DESC ";
			query += " ) a WHERE rownum <= ? ";
			query += " ) WHERE rn > ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, countPageNo);
			ps.setInt(2, start_no);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기
				dto.setEmpid(rs.getString("emp_id"));

				dto.setEname(rs.getString("ename"));

				dto.setDeptname(rs.getString("DEPT_NAME"));
				dto.setAuth(rs.getInt("AUTH"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("paging 함수 실행 : " + list.size());
		return list;

	}

	public LoginDTO detail(String empid) {
		System.out.println("/login DAO.detail 실행");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// 바구니 소환
		LoginDTO dto = new LoginDTO();

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " select * from user_info u ";
			query += " left outer join dept d on u.dept_no = d.dept_no ";
			query += " where u.emp_id = ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, empid);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			// 신입사원에게 알려줄 내용들
			while (rs.next()) {
				dto.setEname(rs.getString("ename"));
				dto.setEmpid(rs.getString("emp_id"));
				dto.setAuth(rs.getInt("auth"));
				dto.setDeptname(rs.getString("DEPT_NAME"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("detail 함수 실행 : " + dto.toString());
		return dto;

	}

	public int permission(LoginDTO d) {
		System.out.println("/login DAO.permission 실행");

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " update user_info ";
			query += " set auth = ? ";
			query += " where emp_id = ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, d.getAuth());
			ps.setString(2, d.getEmpid());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("권한 변경 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	public List<DashDTO> defect() {
		System.out.println("/dashboard DAO.defect 실행");

		List<DashDTO> list = new ArrayList<DashDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT * FROM defect d ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				DashDTO dto = new DashDTO();

				// 바구니에 담기
				dto.setDtype_name(rs.getString("dtype_name"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("defect 함수 실행 : " + list.size());
		return list;

	}

	public int nread() {
		System.out.println("/login DAO.nread 실행");

		int count = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT boardno, title FROM announcement ";
			query += " order by boardno desc ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				// 숫자세기
				count++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(" nread 함수 실행 : " + count);
		return count;

	}

	public List<DashDTO> notice(int nstart_no, int ncountPageNo) {
		System.out.println("/dashboard DAO.notice 실행");

		// 리스트 소환
		List<DashDTO> list = new ArrayList<DashDTO>();

		// 의례
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * FROM ( ";
			query += " SELECT rownum AS rn, a.* FROM ( ";
			query += " SELECT boardno, title ";
			query += " FROM announcement ";
			query += " ORDER BY boardno DESC ";
			query += " ) a WHERE rownum <= ? ";
			query += " ) WHERE rn > ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, ncountPageNo);
			ps.setInt(2, nstart_no);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				DashDTO dto = new DashDTO();

				// 바구니에 담기
				dto.setNboardno(rs.getString("boardno"));
				dto.setNtitle(rs.getString("title"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("notice 함수 실행 : " + list.size());
		return list;

	}

	public int sread() {
		System.out.println("/login DAO.sread 실행");

		int count = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT boardno, title FROM suggestion ";
			query += " order by boardno desc ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				// 숫자세기
				count++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(" sread 함수 실행 : " + count);
		return count;

	}

	public List<DashDTO> suggestion(String empid, int sstart_no, int scountPageNo) {
		System.out.println("/dashboard DAO.suggestion 실행");

		// 리스트 소환
		List<DashDTO> list = new ArrayList<DashDTO>();

		// 의례
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * FROM ( ";
			query += " SELECT rownum AS rn, a.* FROM ( ";
			query += " SELECT boardno, title, complete ";
			query += " FROM suggestion ";
			query += " where emp_id = ? ";
			query += " ORDER BY boardno DESC ";
			query += " ) a WHERE rownum <= ? ";
			query += " ) WHERE rn > ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, empid);
			ps.setInt(2, scountPageNo);
			ps.setInt(3, sstart_no);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				DashDTO dto = new DashDTO();

				// 바구니에 담기
				dto.setSboardno(rs.getString("boardno"));
				dto.setStitle(rs.getString("title"));
				dto.setComplete(rs.getInt("complete"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("suggestion 함수 실행 : " + list.size());
		return list;

	}
	
	

	public List<DashDTO> work_order() {
		System.out.println("/dashboard DAO.work_order 실행");

		List<DashDTO> list = new ArrayList<DashDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT * ";
			query += " FROM Work_order w ";
			query += " LEFT OUTER JOIN production_plan p ON w.plan_id = p.plan_id ";
			query += " WHERE p.plan_sdate >= '2026-04-13' ";
			query += " AND p.plan_edate <= '2026-04-17' ";
			query += " AND w.workdate = '2026-04-16' ";
			query += " ORDER BY w.workdate ASC ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				DashDTO dto = new DashDTO();

				// 바구니에 담기
				dto.setWo_qty(rs.getInt("wo_qty"));
				dto.setPrev_qty(rs.getInt("prev_qty"));
				dto.setItemid(rs.getString("item_id"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("work_order 함수 실행 : " + list.size());
		return list;

	}

	public int aread(String empid) {
		System.out.println("/login DAO.aread 실행");

		int count = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT * FROM alarms ";
			query += " WHERE target_id = ? ";
			query += " AND is_show = 1 ";

			ps = conn.prepareStatement(query);
			ps.setString(1, empid);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				// 숫자세기
				count++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(" aread 함수 실행 : " + count);
		return count;

	}

	public List<DashDTO> a(String empid, int astart_no, int acountPageNo) {
		System.out.println("/dashboard DAO.i 실행");

		List<DashDTO> list = new ArrayList<DashDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * FROM ( ";
			query += " SELECT rownum AS rn, a.* FROM ( ";
			query += " SELECT * FROM alarms ";
			query += " WHERE target_id = ? ";
			query += " and is_show = 1 ";
			query += " ORDER BY reg_date DESC ";
			query += " ) a WHERE rownum <= ? ";
			query += " ) WHERE rn > ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, empid);
			ps.setInt(2, acountPageNo);
			ps.setInt(3, astart_no);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				DashDTO dto = new DashDTO();

				// 바구니에 담기
				dto.setAtitle(rs.getString("title"));
				dto.setAcontent(rs.getString("content"));
				dto.setAlink_url(rs.getString("link_url"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("a 함수 실행 : " + list.size());
		return list;

	}

	public int wread(String empid) {
		System.out.println("/login DAO.dread 실행");

		int count = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT *  ";
			query += " FROM Work_order w ";
			query += " LEFT OUTER JOIN wo_status s ON w.wostatus_no = s.wostatus_no ";
			query += " WHERE w.emp_id = ? ";
			query += " ORDER BY w.workdate ASC ";

			ps = conn.prepareStatement(query);
			ps.setString(1, empid);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				// 숫자세기
				count++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(" wread 함수 실행 : " + count);
		return count;

	}

	public List<LoginDTO> mywork(String empid, int astart_no, int acountPageNo) {
		System.out.println("/mypage DAO.mywork 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * FROM ( ";
			query += " SELECT rownum AS rn, a.* FROM ( ";
			query += " SELECT * ";
			query += " FROM Work_order w ";
			query += " LEFT OUTER JOIN wo_status s ON w.wostatus_no = s.wostatus_no ";
			query += " WHERE w.emp_id = ? ";
			query += " ORDER BY w.workdate DESC ";
			query += " ) a WHERE rownum <= ? ";
			query += " ) WHERE rn > ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, empid);
			ps.setInt(2, acountPageNo);
			ps.setInt(3, astart_no);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기
				dto.setWo_qty(rs.getInt("wo_qty"));
				dto.setPrev_qty(rs.getInt("prev_qty"));
				dto.setWoid(rs.getString("wo_id"));
				dto.setWorkdate(rs.getDate("workdate"));
				dto.setPlanid(rs.getString("plan_id"));
				dto.setContent(rs.getString("content"));
				dto.setWostatusname(rs.getString("wostatus_name"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("mywork 함수 실행 : " + list.size());
		return list;

	}

	public int dread() {
		System.out.println("/login DAO.dread 실행");

		int count = 0;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT dtype_name, solution FROM defect d  ";
			query += " LEFT OUTER JOIN quality_check q ON d.qc_id = q.qc_id ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				// 숫자세기
				count++;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(" dread 함수 실행 : " + count);
		return count;

	}

	public List<LoginDTO> defect_report(int astart_no, int acountPageNo) {
		System.out.println("/loginDAO.defect_report 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * FROM ( ";
			query += " SELECT rownum AS rn, a.* FROM ( ";
			query += " SELECT * FROM defect d ";
			query += " LEFT OUTER JOIN quality_check q ON d.qc_id = q.qc_id ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";
			query += " ORDER BY d.defect_id DESC ";
			query += " ) a WHERE rownum <= ? ";
			query += " ) WHERE rn > ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, acountPageNo);
			ps.setInt(2, astart_no);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기
				dto.setDefect_id(rs.getString("defect_id"));
				dto.setDefect_cnt(rs.getString("defect_cnt"));
				dto.setSolution(rs.getString("solution"));
				dto.setQc_id(rs.getString("qc_id"));
				dto.setQc_sdate(rs.getDate("qc_sdate"));
				dto.setQc_edate(rs.getDate("qc_edate"));
				dto.setDtype_name(rs.getString("dtype_name"));
				dto.setWoid(rs.getString("wo_id"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("defect_report 함수 실행 : " + list.size());
		return list;

	}

	public List<LoginDTO> dMonthChart() {
		System.out.println("/loginDAO.dMonthChart 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " SELECT t.dtype_name, d.solution, d.defect_cnt FROM defect d  ";
			query += " LEFT OUTER JOIN quality_check q ON d.qc_id = q.qc_id ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기

				dto.setDtype_name(rs.getString("dtype_name"));
				dto.setDefect_cnt(rs.getString("defect_cnt"));
				dto.setSolution(rs.getString("solution"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("dMonthChart 함수 실행 : " + list.size());
		return list;

	}

	public List<LoginDTO> selectd() {
		System.out.println("/loginDAO.selectd 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT  DISTINCT d.dept_name FROM user_info u ";
			query += " LEFT OUTER JOIN dept d ON u.dept_no = d.dept_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기
				dto.setDeptname(rs.getString("dept_name"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("selectd 함수 실행 : " + list.size());
		return list;

	}

	public List<LoginDTO> selectm() {
		System.out.println("/loginDAO.selectm 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * FROM user_info u ";
			query += " LEFT OUTER JOIN dept d ON u.dept_no = d.dept_no ";
			query += " where auth >= 2 ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기
				dto.setEmpid(rs.getString("emp_id"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("selectm 함수 실행 : " + list.size());
		return list;

	}

	public List<LoginDTO> selecta() {
		System.out.println("/loginDAO.selecta 실행");

		List<LoginDTO> list = new ArrayList<LoginDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT  DISTINCT u.auth FROM user_info u ";
			query += " LEFT OUTER JOIN dept d ON u.dept_no = d.dept_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();

				// 바구니에 담기
				dto.setAuth(rs.getInt("auth"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("selecta 함수 실행 : " + list.size());
		return list;

	}

	public int changeAuth(LoginDTO d) {
		System.out.println("/login DAO.changeAuth 실행");

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " update user_info ";
			query += " set auth = ? ";
			query += " where emp_id = ? ";
			// query += " and phone = ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, d.getAuth());
			ps.setString(2, d.getEmpid());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("권한 변경 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	public int changeDept(LoginDTO d) {
		System.out.println("/login DAO.changeDept 실행");

		System.out.println("사번 : " + d.getEmpid());
		System.out.println("부서넘버 : " + d.getDept_no());

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " update user_info ";
			query += " set dept_no = ? ";
			query += " where emp_id = ? ";
			// query += " and phone = ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, d.getDept_no());
			ps.setString(2, d.getEmpid());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("부서 변경 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	public int retire(LoginDTO d) {
		System.out.println("/login DAO.retire 실행");

		System.out.println("사번 : " + d.getEmpid());
		System.out.println("retire : " + d.getRetire());

		int count = -1;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			// SQL 준비
			String query = " update user_info ";
			query += " set retire = ? ";
			query += " where emp_id = ? ";
			// query += " and phone = ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, d.getRetire());
			ps.setString(2, d.getEmpid());

			// SQL 실행 및 결과 확보
			count = ps.executeUpdate();

			// 결과 활용

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("퇴사처리 결과 : " + count + " (0이상이면 성공. -1이면 실패) ");
		return count;

	}

	// report 목표대비 달성률
	public List<ReportDTO> date() {
		System.out.println("/loginDAO.date 실행");

		List<ReportDTO> list = new ArrayList<ReportDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * ";
			query += " FROM quality_check q ";
			query += " LEFT OUTER JOIN work_order w ON q.wo_id = w.wo_id ";
			query += " LEFT OUTER JOIN defect d ON d.qc_id = q.qc_id ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";
			query += " WHERE q.qc_edate >= TRUNC(SYSDATE) ";
			query += " AND q.qc_edate < TRUNC(SYSDATE) + 1 ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				ReportDTO dto = new ReportDTO();

				// 바구니에 담기
				dto.setDefect_id(rs.getString("defect_id"));

				dto.setDefect_cnt(rs.getInt("defect_cnt"));

				dto.setSolution(rs.getString("solution"));

				dto.setQc_id(rs.getString("qc_id"));

				dto.setQc_sdate(rs.getDate("qc_sdate"));

				dto.setQc_edate(rs.getDate("qc_edate"));

				dto.setDtype_name(rs.getString("dtype_name"));

				dto.setWo_qty(rs.getInt("wo_qty"));

				dto.setPrev_qty(rs.getInt("prev_qty"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("date 함수 실행 : " + list.size());
		return list;

	}

	// report 목표대비 달성률
	public List<ReportDTO> clear() {
		System.out.println("/loginDAO.clear 실행");

		List<ReportDTO> list = new ArrayList<ReportDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT * ";
			query += " FROM quality_check q ";
			query += " LEFT OUTER JOIN work_order w ON q.wo_id = w.wo_id ";
			query += " LEFT OUTER JOIN defect d ON d.qc_id = q.qc_id ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {
				ReportDTO dto = new ReportDTO();

				// 바구니에 담기
				dto.setDefect_id(rs.getString("defect_id"));

				dto.setDefect_cnt(rs.getInt("defect_cnt"));

				dto.setSolution(rs.getString("solution"));

				dto.setQc_id(rs.getString("qc_id"));

				dto.setQc_sdate(rs.getDate("qc_sdate"));

				dto.setQc_edate(rs.getDate("qc_edate"));

				dto.setDtype_name(rs.getString("dtype_name"));

				dto.setWo_qty(rs.getInt("wo_qty"));

				dto.setPrev_qty(rs.getInt("prev_qty"));

				// 바구니를 리스트에 싣기
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("clear 함수 실행 : " + list.size());
		return list;

	}

	// report 목표대비 달성률
	public ReportDTO sumdate() {
		System.out.println("/loginDAO.sumdate 실행");

		ReportDTO dto = new ReportDTO();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// JNDI 방식
			// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
			Context ctx = new InitialContext();

			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB접속(그런데 이제 커넥션 풀로.)
			conn = dataFactory.getConnection();

			String query = " SELECT  ";
			query += " SUM(prev_qty) as total_prev, ";
			query += " SUM(wo_qty) as total_wo, ";
			query += " SUM(defect_cnt) as total_cnt ";
			query += " FROM quality_check q ";
			query += " LEFT OUTER JOIN work_order w ON q.wo_id = w.wo_id ";
			query += " LEFT OUTER JOIN defect d ON d.qc_id = q.qc_id ";
			query += " LEFT OUTER JOIN defect_type t ON d.dtype_no = t.dtype_no ";
			query += " WHERE q.qc_edate >= TRUNC(SYSDATE) ";
			query += " AND q.qc_edate < TRUNC(SYSDATE) + 1 ";

			ps = conn.prepareStatement(query);

			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();

			// 결과 활용

			while (rs.next()) {

				dto.setTotal_wo(rs.getInt("TOTAL_WO")); // 소문자 대신 대문자로 시도
				dto.setTotal_prev(rs.getInt("TOTAL_PREV"));
				dto.setTotal_cnt(rs.getInt("TOTAL_CNT"));

				// 바구니를 리스트에 싣기

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("sumdate 함수 실행 : " + dto);
		return dto;

	}
	
	// report 목표대비 달성률
		public List<ReportDTO> sumclear() {
			System.out.println("/loginDAO.sumclear 실행");
			
			List<ReportDTO> list = new ArrayList<ReportDTO>();

			

			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				// JNDI 방식
				// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
				Context ctx = new InitialContext();

				// DataSource : 커넥션 풀 관리자
				DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

				// DB접속(그런데 이제 커넥션 풀로.)
				conn = dataFactory.getConnection();

				String query = " SELECT ";
				query += "   i.item_name, ";
				query += "   SUM(w.wo_qty) AS TOTAL_WO, ";     // 목표 수량 합계
				query += "   SUM(w.prev_qty) AS TOTAL_PREV, "; // 실 생산 수량 합계
				query += "   SUM(NVL(d_sum.total_defect, 0)) AS TOTAL_CNT "; // 불량 합계
				query += " FROM work_order w ";
				query += " JOIN production_plan p ON w.plan_id = p.plan_id ";
				query += " JOIN item i ON p.item_id = i.item_id ";
				query += " LEFT OUTER JOIN ( ";
				query += "   SELECT q.wo_id, SUM(d.defect_cnt) AS total_defect ";
				query += "   FROM quality_check q ";
				query += "   JOIN defect d ON q.qc_id = d.qc_id ";
				query += "   GROUP BY q.wo_id ";
				query += " ) d_sum ON w.wo_id = d_sum.wo_id ";
				query += " GROUP BY i.item_name ";
				query += " ORDER BY i.item_name ASC ";
			

				ps = conn.prepareStatement(query);

				// SQL 실행 및 결과 확보
				rs = ps.executeQuery();

				// 결과 활용

				while (rs.next()) {
					
					ReportDTO dto = new ReportDTO();

					dto.setItem_name(rs.getString("item_name")); // 소문자 대신 대문자로 시도
					dto.setTotal_wo(rs.getInt("TOTAL_WO")); // 소문자 대신 대문자로 시도
					dto.setTotal_prev(rs.getInt("TOTAL_PREV"));
					dto.setTotal_cnt(rs.getInt("TOTAL_CNT"));

					// 바구니를 리스트에 싣기
					list.add(dto);

				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			System.out.println("sumclear 함수 실행 : " + list.size());
			return list;

		}
		
		
		// report 불량률 top 5
		public List<ReportDTO> topfive() {
			System.out.println("/loginDAO.topfive 실행");
			
			List<ReportDTO> list = new ArrayList<ReportDTO>();
			
			
			
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				// JNDI 방식
				// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
				Context ctx = new InitialContext();
				
				// DataSource : 커넥션 풀 관리자
				DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
				
				// DB접속(그런데 이제 커넥션 풀로.)
				conn = dataFactory.getConnection();
				
				// 상위 5개만 추출하는 인라인 뷰 쿼리
		        String query = " SELECT * FROM ( "
		                     + "    SELECT "
		                     + "        t.dtype_name, "
		                     + "        SUM(d.defect_cnt) AS total_cnt "
		                     + "    FROM defect d "
		                     + "    JOIN defect_type t ON d.dtype_no = t.dtype_no "
		                     + "    GROUP BY t.dtype_name "
		                     + "    ORDER BY total_cnt DESC "
		                     + " ) WHERE ROWNUM <= 5 ";
				
				
				ps = conn.prepareStatement(query);
				
				// SQL 실행 및 결과 확보
				rs = ps.executeQuery();
				
				// 결과 활용
				
				while (rs.next()) {
					
					ReportDTO dto = new ReportDTO();
					
					dto.setDtype_name(rs.getString("dtype_name")); // 소문자 대신 대문자로 시도
					dto.setTotal_cnt(rs.getInt("total_cnt")); // 소문자 대신 대문자로 시도
					
					// 바구니를 리스트에 싣기
					list.add(dto);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			System.out.println("topfive 함수 실행 : " + list.size());
			return list;
			
		}
		
	

				
		
		// 설비 불량
		public List<ReportDTO> errormch(int start_no, int countPageNo) {
			System.out.println("/loginDAO.errormch 실행");
			
			List<ReportDTO> list = new ArrayList<ReportDTO>();
			
			
			
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				// JNDI 방식
				// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
				Context ctx = new InitialContext();
				
				// DataSource : 커넥션 풀 관리자
				DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
				
				// DB접속(그런데 이제 커넥션 풀로.)
				conn = dataFactory.getConnection();
				
				// 현재 서버 시간 기준으로 오늘 발생한 이슈 조회 (나중에 SYSDATE-1로 조정 가능)
				String query = " SELECT * FROM ( "
			             + "    SELECT eq_log_id, start_time, end_time, eq_id, emp_id, insp_type, insp_content "
			             + "    FROM equipment_log "
			             + "    ORDER BY end_time DESC NULLS LAST "
			             + " ) WHERE ROWNUM <= 5 ";
				
				
				ps = conn.prepareStatement(query);
				
				// SQL 실행 및 결과 확보
				rs = ps.executeQuery();
				
				// 결과 활용
				
				 while (rs.next()) {
			            ReportDTO dto = new ReportDTO();
			            
			            // 새로 만드신 필드명에 정확히 매핑
			            dto.setEq_log_id(rs.getString("eq_log_id"));
			            dto.setStart_time(rs.getDate("start_time"));
			            dto.setEnd_time(rs.getDate("end_time"));
			            dto.setEq_id(rs.getString("eq_id"));
			            dto.setEmp_id(rs.getString("emp_id"));
			            dto.setInsp_type(rs.getString("insp_type")); // DB 컬럼명이 insp_typ인 경우
			            dto.setInsp_content(rs.getString("insp_content"));
			            
			            list.add(dto);
			        }
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			System.out.println("errormch 함수 실행 : " + list.size());
			return list;
			
		}
		
		
		
		
		// report 목표대비 달성률
		public ReportDTO press() {
			System.out.println("/loginDAO.press 실행");

			ReportDTO dto = new ReportDTO();

			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				// JNDI 방식
				// connection.xml 맨 아래에 있는 DB정보로 커넥션 풀을 가져온다. Server 폴더에 있다. 기억!
				Context ctx = new InitialContext();

				// DataSource : 커넥션 풀 관리자
				DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

				// DB접속(그런데 이제 커넥션 풀로.)
				conn = dataFactory.getConnection();

				String query = " SELECT  ";
				query += "  COUNT(CASE WHEN EQ_STATUS = '가동' THEN 1 END) AS active_cnt, ";
				query += " COUNT(CASE WHEN EQ_STATUS IN ('점검 중', '고장') THEN 1 END) AS error_cnt ";
				query += " FROM equipment ";

				ps = conn.prepareStatement(query);

				// SQL 실행 및 결과 확보
				rs = ps.executeQuery();

				// 결과 활용

				while (rs.next()) {

					dto.setActive_cnt(rs.getInt("active_cnt")); // 소문자 대신 대문자로 시도
					dto.setError_cnt(rs.getInt("error_cnt"));
					

					// 바구니를 리스트에 싣기

				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			System.out.println("press 함수 실행 : " + dto);
			return dto;

		}

}
