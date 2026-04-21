package P10_report;

import java.sql.Date;

import java.io.Serializable;

public class ReportDTO implements Serializable{
	
	String qc_id;
	Date qc_sdate;
	Date qc_edate;
	String worker;
	String wo_id;
	Date workdate;
	String plan_id;
	String defect_id;
	String solution;
	String dtype_name;
	String item_id;
	String item_name;
	
	int wo_qty;
	int prev_qty;
	int defect_cnt;
	int total_wo;
	int total_prev;
	int total_cnt;
	
	String eq_log_id;
	Date Start_time;
	Date end_time;
    String eq_id;
	String emp_id;
	String insp_type;
	String insp_content;
	
	int active_cnt;
	int error_cnt;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getActive_cnt() {
		return active_cnt;
	}
	public void setActive_cnt(int active_cnt) {
		this.active_cnt = active_cnt;
	}
	public int getError_cnt() {
		return error_cnt;
	}
	public void setError_cnt(int error_cnt) {
		this.error_cnt = error_cnt;
	}
	public String getEq_log_id() {
		return eq_log_id;
	}
	public void setEq_log_id(String eq_log_id) {
		this.eq_log_id = eq_log_id;
	}
	public Date getStart_time() {
		return Start_time;
	}
	public void setStart_time(Date start_time) {
		Start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public String getEq_id() {
		return eq_id;
	}
	public void setEq_id(String eq_id) {
		this.eq_id = eq_id;
	}
	public String getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}
	public String getInsp_type() {
		return insp_type;
	}
	public void setInsp_type(String insp_type) {
		this.insp_type = insp_type;
	}
	public String getInsp_content() {
		return insp_content;
	}
	public void setInsp_content(String insp_content) {
		this.insp_content = insp_content;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	
	@Override
	public String toString() {
		return "ReportDTO [qc_id=" + qc_id + ", qc_sdate=" + qc_sdate + ", qc_edate=" + qc_edate + ", worker=" + worker
				+ ", wo_id=" + wo_id + ", workdate=" + workdate + ", plan_id=" + plan_id + ", defect_id=" + defect_id
				+ ", solution=" + solution + ", dtype_name=" + dtype_name + ", wo_qty=" + wo_qty + ", prev_qty="
				+ prev_qty + ", defect_cnt=" + defect_cnt + ", total_wo=" + total_wo + ", total_prev=" + total_prev
				+ ", total_cnt=" + total_cnt + "]";
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
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getWo_id() {
		return wo_id;
	}
	public void setWo_id(String wo_id) {
		this.wo_id = wo_id;
	}
	public Date getWorkdate() {
		return workdate;
	}
	public void setWorkdate(Date workdate) {
		this.workdate = workdate;
	}
	public String getPlan_id() {
		return plan_id;
	}
	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}
	public String getDefect_id() {
		return defect_id;
	}
	public void setDefect_id(String defect_id) {
		this.defect_id = defect_id;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getDtype_name() {
		return dtype_name;
	}
	public void setDtype_name(String dtype_name) {
		this.dtype_name = dtype_name;
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
	public int getDefect_cnt() {
		return defect_cnt;
	}
	public void setDefect_cnt(int defect_cnt) {
		this.defect_cnt = defect_cnt;
	}
	public int getTotal_wo() {
		return total_wo;
	}
	public void setTotal_wo(int total_wo) {
		this.total_wo = total_wo;
	}
	public int getTotal_prev() {
		return total_prev;
	}
	public void setTotal_prev(int total_prev) {
		this.total_prev = total_prev;
	}
	public int getTotal_cnt() {
		return total_cnt;
	}
	public void setTotal_cnt(int total_cnt) {
		this.total_cnt = total_cnt;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
