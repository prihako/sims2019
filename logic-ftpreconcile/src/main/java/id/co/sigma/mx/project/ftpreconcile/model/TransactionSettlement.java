package id.co.sigma.mx.project.ftpreconcile.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransactionSettlement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -377938895120255413L;

	private Long settlementId = null;

	private Long reconcileResultId = null;

	private Long settlementMsgId = null;

	private Long transactionId = null;

	private Date settlementDate = null;

	private String settlementRemarks = null;

	private String createdWho = null;

	private BigDecimal prevAmount = null;

	private String prevInvoiceId = null;

	private String prevClientId = null;

	private String prevTransactionStatus = null;

	public String getCreatedWho() {
		return createdWho;
	}

	public void setCreatedWho(String createdWho) {
		this.createdWho = createdWho;
	}

	public BigDecimal getPrevAmount() {
		return prevAmount;
	}

	public void setPrevAmount(BigDecimal prevAmount) {
		this.prevAmount = prevAmount;
	}

	public String getPrevClientId() {
		return prevClientId;
	}

	public void setPrevClientId(String prevClientId) {
		this.prevClientId = prevClientId;
	}

	public String getPrevInvoiceId() {
		return prevInvoiceId;
	}

	public void setPrevInvoiceId(String prevInvoiceId) {
		this.prevInvoiceId = prevInvoiceId;
	}

	public String getPrevTransactionStatus() {
		return prevTransactionStatus;
	}

	public void setPrevTransactionStatus(String prevTransactionStatus) {
		this.prevTransactionStatus = prevTransactionStatus;
	}

	public Long getReconcileResultId() {
		return reconcileResultId;
	}

	public void setReconcileResultId(Long reconcileResultId) {
		this.reconcileResultId = reconcileResultId;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public Long getSettlementMsgId() {
		return settlementMsgId;
	}

	public void setSettlementMsgId(Long settlementMsgId) {
		this.settlementMsgId = settlementMsgId;
	}

	public String getSettlementRemarks() {
		return settlementRemarks;
	}

	public void setSettlementRemarks(String settlementRemarks) {
		this.settlementRemarks = settlementRemarks;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
