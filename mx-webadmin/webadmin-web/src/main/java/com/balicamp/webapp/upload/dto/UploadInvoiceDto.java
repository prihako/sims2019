package com.balicamp.webapp.upload.dto;

import java.io.Serializable;

public class UploadInvoiceDto implements Serializable {

	private static final long serialVersionUID = -3848798365574038632L;

	private int no;

	private String klienId;

	private String invoiceNo;

	private String status;

	public UploadInvoiceDto() {
		super();
	}

	public UploadInvoiceDto(String klienId, String invoiceNo) {
		super();
		this.klienId = klienId;
		this.invoiceNo = invoiceNo;
	}

	public UploadInvoiceDto(String klienId, String invoiceNo, String status) {
		super();
		this.klienId = klienId;
		this.invoiceNo = invoiceNo;
		this.status = status;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getKlienId() {
		return klienId;
	}

	public void setKlienId(String klienId) {
		this.klienId = klienId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
