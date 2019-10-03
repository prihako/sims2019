package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReconcileLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 653481385394102022L;

	private long reconcileLogId;

	private Date reconcileDate;

	private long totalFullMatch;
	private long totalPartialMatch;
	private long totalNotMatch;
	private long totalPending;
	private long totalRecord;
	public Date getReconcileDate() {
		return reconcileDate;
	}
	public void setReconcileDate(Date reconcileDate) {
		this.reconcileDate = reconcileDate;
	}
	public long getReconcileLogId() {
		return reconcileLogId;
	}
	public void setReconcileLogId(long reconcileLogId) {
		this.reconcileLogId = reconcileLogId;
	}
	public long getTotalFullMatch() {
		return totalFullMatch;
	}
	public void setTotalFullMatch(long totalFullMatch) {
		this.totalFullMatch = totalFullMatch;
	}
	public long getTotalNotMatch() {
		return totalNotMatch;
	}
	public void setTotalNotMatch(long totalNotMatch) {
		this.totalNotMatch = totalNotMatch;
	}
	public long getTotalPartialMatch() {
		return totalPartialMatch;
	}
	public void setTotalPartialMatch(long totalPartialMatch) {
		this.totalPartialMatch = totalPartialMatch;
	}
	public long getTotalPending() {
		return totalPending;
	}
	public void setTotalPending(long totalPending) {
		this.totalPending = totalPending;
	}
	public long getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
