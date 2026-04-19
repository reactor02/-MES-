package P01_auth;

import java.sql.Date;

import java.io.Serializable; // 1. 임포트 추가

public class LoginDTO {
	
	String empid;
	String ename;
	long phone;
	String password;
	String password2;
	String mgr;
	String license;
	String deptno;
	String deptname;
	int auth;
	Date hiredate;

	
	int wo_qty;
	int prev_qty;
	
	String woid;
	Date workdate;
	String planid;
	String content;
	String wostatusname;
	
	String defect_id;
	String defect_cnt;
	String solution;
	String qc_id;
	Date qc_sdate;
	Date qc_edate;
	String dtype_name;
	
    
    
    
	
	
	
	
	
	public String getDefect_id() {
		return defect_id;
	}



	public void setDefect_id(String defect_id) {
		this.defect_id = defect_id;
	}



	public String getDefect_cnt() {
		return defect_cnt;
	}



	public void setDefect_cnt(String defect_cnt) {
		this.defect_cnt = defect_cnt;
	}



	public String getSolution() {
		return solution;
	}



	public void setSolution(String solution) {
		this.solution = solution;
	}



	public String getQc_id() {
		return qc_id;
	}



	public void setQc_id(String qc_id) {
		this.qc_id = qc_id;
	}



	public Date getQc_sdate() {
		return qc_sdate;
	}



	public void setQc_sdate(Date qc_sdate) {
		this.qc_sdate = qc_sdate;
	}



	public Date getQc_edate() {
		return qc_edate;
	}



	public void setQc_edate(Date qc_edate) {
		this.qc_edate = qc_edate;
	}



	public String getDtype_name() {
		return dtype_name;
	}



	public void setDtype_name(String dtype_name) {
		this.dtype_name = dtype_name;
	}







	@Override
	public String toString() {
		return "LoginDTO [empid=" + empid + ", ename=" + ename + ", phone=" + phone + ", password=" + password
				+ ", password2=" + password2 + ", mgr=" + mgr + ", license=" + license + ", deptno=" + deptno
				+ ", deptname=" + deptname + ", auth=" + auth + ", hiredate=" + hiredate + ", wo_qty=" + wo_qty
				+ ", prev_qty=" + prev_qty + ", woid=" + woid + ", workdate=" + workdate + ", planid=" + planid
				+ ", content=" + content + ", wostatusname=" + wostatusname + ", defect_id=" + defect_id
				+ ", defect_cnt=" + defect_cnt + ", solution=" + solution + ", qc_id=" + qc_id + ", qc_sdate="
				+ qc_sdate + ", qc_edate=" + qc_edate + ", dtype_name=" + dtype_name + "]";
	}



	public String getWostatusname() {
		return wostatusname;
	}



	public void setWostatusname(String wostatusname) {
		this.wostatusname = wostatusname;
	}




	public String getWoid() {
		return woid;
	}



	public void setWoid(String woid) {
		this.woid = woid;
	}



	public String getPlanid() {
		return planid;
	}



	public void setPlanid(String planid) {
		this.planid = planid;
	}



	public Date getWorkdate() {
		return workdate;
	}



	public void setWorkdate(Date workdate) {
		this.workdate = workdate;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}


	

	public int getWo_qty() {
		return wo_qty;
	}

	public void setWo_qty(int wo_qty) {
		this.wo_qty = wo_qty;
	}

	public int getPrev_qty() {
		return prev_qty;
	}

	public void setPrev_qty(int prev_qty) {
		this.prev_qty = prev_qty;
	}
	
	
	

	
	
	

	public int getAuth() {
		return auth;
	}
	
	
	public void setAuth(int auth) {
		this.auth = auth;
	}

	public String getDeptname() {
		return deptname;
	}


	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}


	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getMgr() {
		return mgr;
	}
	public void setMgr(String mgr) {
		this.mgr = mgr;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getDeptno() {
		return deptno;
	}
	public void setDeptno(String deptno) {
		this.deptno = deptno;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	

}
