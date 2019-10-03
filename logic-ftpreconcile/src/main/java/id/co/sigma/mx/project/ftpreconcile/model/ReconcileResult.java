package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReconcileResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6323520999232298520L;
	private long reconcileResultId;
	private long reconcileLogId;
	private long transactionId;
	private long reconcileMsgId;
	private String reconcileStatus;
	private String settlementFlag;
	private String activeFlag;
	
	public String getActiveFlag() {
		return activeFlag;
	}


	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}


	public long getReconcileLogId() {
		return reconcileLogId;
	}


	public void setReconcileLogId(long reconcileLogId) {
		this.reconcileLogId = reconcileLogId;
	}


	public long getReconcileMsgId() {
		return reconcileMsgId;
	}


	public void setReconcileMsgId(long reconcileMsgId) {
		this.reconcileMsgId = reconcileMsgId;
	}


	public long getReconcileResultId() {
		return reconcileResultId;
	}


	public void setReconcileResultId(long reconcileResultId) {
		this.reconcileResultId = reconcileResultId;
	}


	public String getReconcileStatus() {
		return reconcileStatus;
	}


	public void setReconcileStatus(String reconcileStatus) {
		this.reconcileStatus = reconcileStatus;
	}


	public String getSettlementFlag() {
		return settlementFlag;
	}


	public void setSettlementFlag(String settlementFlag) {
		this.settlementFlag = settlementFlag;
	}


	public long getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
