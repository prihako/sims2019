package com.balicamp.model.mx;

import java.io.Serializable;

public class TransactionFeeModel implements Serializable {
	private static final long serialVersionUID = 3215921359128490357L;

	private String trxCode;
	private String trxName;
	private String projectCode;
	private String productCode;
	private Long fee;
	private Transactions transaction;
	private PriorityRouting priority;
	//
	private int no;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getTrxName() {
		return trxName;
	}

	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Transactions getTransaction() {
		return transaction;
	}

	public void setTransaction(Transactions transaction) {
		this.transaction = transaction;
	}

	public PriorityRouting getPriority() {
		return priority;
	}

	public void setPriority(PriorityRouting priority) {
		this.priority = priority;
	}
}
