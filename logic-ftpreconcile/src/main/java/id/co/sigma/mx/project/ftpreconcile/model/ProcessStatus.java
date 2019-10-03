package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProcessStatus implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6836963958739962594L;
	
	private Long processStatusId = null;
	private Long reconcileLogId = null;
	private Long isoBatchLogId = null;
	private String status = null;
	
	public Long getIsoBatchLogId() {
		return isoBatchLogId;
	}
	public void setIsoBatchLogId(Long isoBatchLogId) {
		this.isoBatchLogId = isoBatchLogId;
	}
	public Long getProcessStatusId() {
		return processStatusId;
	}
	public void setProcessStatusId(Long processStatusId) {
		this.processStatusId = processStatusId;
	}
	public Long getReconcileLogId() {
		return reconcileLogId;
	}
	public void setReconcileLogId(Long reconcileLogId) {
		this.reconcileLogId = reconcileLogId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}