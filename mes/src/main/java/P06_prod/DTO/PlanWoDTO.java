package P06_prod.DTO;

import java.sql.Date;

public class PlanWoDTO {
	
	protected String planId;
	protected int planQty;
	protected int prevQty;
	protected Date sDate;
	protected Date eDate;
	protected int planStatus;
	protected String itemId;
	protected String itemName;
	protected String dId;
	protected String dName;
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public int getPlanQty() {
		return planQty;
	}
	public void setPlanQty(int planQty) {
		this.planQty = planQty;
	}
	public int getPrevQty() {
		return prevQty;
	}
	public void setPrevQty(int prevQty) {
		this.prevQty = prevQty;
	}
	public Date getsDate() {
		return sDate;
	}
	public void setsDate(Date sDate) {
		this.sDate = sDate;
	}
	public Date geteDate() {
		return eDate;
	}
	public void seteDate(Date eDate) {
		this.eDate = eDate;
	}
	public int getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(int planStatus) {
		this.planStatus = planStatus;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getdId() {
		return dId;
	}
	public void setdId(String dId) {
		this.dId = dId;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	
	
	@Override
	public String toString() {
		return "PlanWoDTO [planId=" + planId + ", planQty=" + planQty + ", prevQty=" + prevQty + ", sDate=" + sDate
				+ ", eDate=" + eDate + ", planStatus=" + planStatus + ", itemId=" + itemId + ", itemName=" + itemName
				+ ", dId=" + dId + ", dName=" + dName + "]";
	}
	
}
