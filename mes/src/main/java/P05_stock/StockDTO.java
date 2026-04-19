package P05_stock;

import java.sql.Date;
import java.sql.Timestamp;

public class StockDTO {
	
	// �ܷ�Ű�� �����ϰ� ������
	// ������ DTO
	
	
	private String stock_id;
	private Integer stock_no;
	private Integer safe_qty; 
	
	// ������� DTO
	
	private String io_id;
	private Timestamp io_time;
	private String deleted;
	private int io_type;
	private String io_reason;
	
	// �ŷ�ó ���̵�
	private String vender_id;
	private String vender_name;
	
	// ��ǰ ���̵�
	private String item_id;
	private String g_id;
	private String item_name;
	
	private String unit;
	private String spec;
	
	// lot 코드
	private String lot_id;
	private int lot_qty;
	private int io_qty;
	private Date expiry_date;
	private String lotdeleted;
	
	
	
	// user_info
	
	private String ename;
	private String dept_no;
	private Integer retire;
	private String emp_id;
	
	// paging
	int start;
	int end;
	int page;
	int size;
	
	// filter
	
	private String filterIoType;
	private String filterVendorId;
	private String filterGId;
	private String filterItemId;
	private String filterDateFrom;
	private String filterDateTo;
	private String filterKeyword;
	private String filterEmpId;
	private String filterExpiry;  // "warn" | "over" | null
	
	
	
	
	public String getFilterEmpId() {
		return filterEmpId;
	}
	public void setFilterEmpId(String filterEmpId) {
		this.filterEmpId = filterEmpId;
	}
	public String getFilterExpiry() {
		return filterExpiry;
	}
	public void setFilterExpiry(String filterExpiry) {
		this.filterExpiry = filterExpiry;
	}
	public String getStock_id() {
		return stock_id;
	}
	public void setStock_id(String stock_id) {
		this.stock_id = stock_id;
	}
	public Integer getStock_no() {
		return stock_no;
	}
	public void setStock_no(Integer stock_no) {
		this.stock_no = stock_no;
	}
	public Integer getSafe_no() {
		return safe_qty;
	}
	public void setSafe_no(Integer safe_qty) {
		this.safe_qty = safe_qty;
	}
	public String getIo_id() {
		return io_id;
	}
	public void setIo_id(String io_id) {
		this.io_id = io_id;
	}
	public Timestamp getIo_time() {
		return io_time;
	}
	public void setIo_time(Timestamp io_time) {
		this.io_time = io_time;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public int getIo_type() {
		return io_type;
	}
	public void setIo_type(int io_type) {
		this.io_type = io_type;
	}
	public String getIo_reason() {
		return io_reason;
	}
	public void setIo_reason(String io_reason) {
		this.io_reason = io_reason;
	}
	public String getVender_id() {
		return vender_id;
	}
	public void setVender_id(String vender_id) {
		this.vender_id = vender_id;
	}
	public String getVender_name() {
		return vender_name;
	}
	public void setVender_name(String vender_name) {
		this.vender_name = vender_name;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getG_id() {
		return g_id;
	}
	public void setG_id(String g_id) {
		this.g_id = g_id;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getLot_id() {
		return lot_id;
	}
	public void setLot_id(String lot_id) {
		this.lot_id = lot_id;
	}
	public int getLot_qty() {
		return lot_qty;
	}
	public void setLot_qty(int lot_qty) {
		this.lot_qty = lot_qty;
	}
	public int getIo_qty() {
		return io_qty;
	}
	public void setIo_qty(int io_qty) {
		this.io_qty = io_qty;
	}
	public Date getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getLotdeleted() {
		return lotdeleted;
	}
	public void setLotdeleted(String lotdeleted) {
		this.lotdeleted = lotdeleted;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public Integer getRetire() {
		return retire;
	}
	public void setRetire(Integer retire) {
		this.retire = retire;
	}
	public String getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}
	
	
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
		
		
		
		
	}
	public String getFilterIoType() {
		return filterIoType;
	}
	public void setFilterIoType(String filterIoType) {
		this.filterIoType = filterIoType;
	}
	public String getFilterVendorId() {
		return filterVendorId;
	}
	public void setFilterVendorId(String filterVendorId) {
		this.filterVendorId = filterVendorId;
	}
	public String getFilterGId() {
		return filterGId;
	}
	public void setFilterGId(String filterGId) {
		this.filterGId = filterGId;
	}
	public String getFilterItemId() {
		return filterItemId;
	}
	public void setFilterItemId(String filterItemId) {
		this.filterItemId = filterItemId;
	}
	public String getFilterDateFrom() {
		return filterDateFrom;
	}
	public void setFilterDateFrom(String filterDateFrom) {
		this.filterDateFrom = filterDateFrom;
	}
	public String getFilterDateTo() {
		return filterDateTo;
	}
	public void setFilterDateTo(String filterDateTo) {
		this.filterDateTo = filterDateTo;
	}
	public String getFilterKeyword() {
		return filterKeyword;
	}
	public void setFilterKeyword(String filterKeyword) {
		this.filterKeyword = filterKeyword;
	}
	@Override
	public String toString() {
		return "StockDTO [stock_id=" + stock_id + ", stock_no=" + stock_no + ", safe_qty=" + safe_qty + ", io_id=" + io_id
				+ ", io_time=" + io_time + ", deleted=" + deleted + ", io_type=" + io_type + ", io_reason=" + io_reason
				+ ", vender_id=" + vender_id + ", vender_name=" + vender_name + ", item_id=" + item_id + ", g_id="
				+ g_id + ", item_name=" + item_name + ", unit=" + unit + ", spec=" + spec + ", lot_id=" + lot_id
				+ ", lot_qty=" + lot_qty + ", expiry_date=" + expiry_date + ", lotdeleted=" + lotdeleted + ", ename="
				+ ename + ", dept_no=" + dept_no + ", retire=" + retire + ", emp_id=" + emp_id + ", start=" + start
				+ ", end=" + end + ", page=" + page + ", size=" + size + ", filterIoType=" + filterIoType
				+ ", filterVendorId=" + filterVendorId + ", filterGId=" + filterGId + ", filterItemId=" + filterItemId
				+ ", filterDateFrom=" + filterDateFrom + ", filterDateTo=" + filterDateTo + ", filterKeyword="
				+ filterKeyword + ", filterEmpId=" + filterEmpId + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}