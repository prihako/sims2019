package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IsoBatchLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 873839896739492777L;
	private long isoBatchLogId;
//	private String batchStatus;
	private Date batchDate;
	private long totalFailed;
	private long totalPaid;
	
	private long totalUnpaid;
	private long totalRecord;
	
	public Date getBatchDate() {
		return batchDate;
	}
	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}
	
//	public String getBatchStatus() {
//		return batchStatus;
//	}
//	public void setBatchStatus(String batchStatus) {
//		this.batchStatus = batchStatus;
//	}
	
	public long getIsoBatchLogId() {
		return isoBatchLogId;
	}
	public void setIsoBatchLogId(long isoBatchLogId) {
		this.isoBatchLogId = isoBatchLogId;
	}
	public long getTotalFailed() {
		return totalFailed;
	}
	public void setTotalFailed(long totalFailed) {
		this.totalFailed = totalFailed;
	}
	public long getTotalPaid() {
		return totalPaid;
	}
	public void setTotalPaid(long totalPaid) {
		this.totalPaid = totalPaid;
	}
	public long getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}
	public long getTotalUnpaid() {
		return totalUnpaid;
	}
	public void setTotalUnpaid(long totalUnpaid) {
		this.totalUnpaid = totalUnpaid;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
