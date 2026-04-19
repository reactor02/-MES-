package P11_masterdata.DTO;

public class ProcessDTO {

	private String process_id;
	private String process_name;
	private int seq;
	private String item_id;
	private String process_info;
	private String process_type;
	private String keyword;
	private int page;
	private int size;
	private int start;
	private int end;

	private String process_step_id;
	private String step_name;
	private String item_name;
	private String unit;
	private String eq_id;
	private String eq_name;


	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getEq_id() {
		return eq_id;
	}

	public void setEq_id(String eq_id) {
		this.eq_id = eq_id;
	}

	public String getEq_name() {
		return eq_name;
	}

	public void setEq_name(String eq_name) {
		this.eq_name = eq_name;
	}

	public String getProcess_id() {
		return process_id;
	}

	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}

	public String getProcess_name() {
		return process_name;
	}

	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getProcess_info() {
		return process_info;
	}

	public void setProcess_info(String process_info) {
		this.process_info = process_info;
	}

	public String getProcess_type() {
		return process_type;
	}

	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getProcess_step_id() {
		return process_step_id;
	}

	public void setProcess_step_id(String process_step_id) {
		this.process_step_id = process_step_id;
	}

	public String getStep_name() {
		return step_name;
	}

	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}
}
