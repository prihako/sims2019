package com.balicamp.model.mx;

// Generated Aug 11, 2014 10:05:10 AM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.annotation.Generated;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

@Entity
@Table(name = "force_payment_log")
public class ForcePaymentLog extends BaseAdminModel implements 
		Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "invoice_no")
	private String invoiceNo;

	@Column(name = "client_id")
	private String clientId;
	
	@Column(name = "transaction_type")
	private String transactionType;
	
	@Column(name = "response_message")
	private String responseMessage;
	
	@Column(name = "response_code")
	private String responseCode;

	@Column(name = "created_on")
	private Timestamp createdOn;
	
	@Column(name = "is_request")
	private Boolean isRequest;

	public ForcePaymentLog() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Boolean getIsRequest() {
		return isRequest;
	}

	public void setIsRequest(Boolean isRequest) {
		this.isRequest = isRequest;
	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
