package com.balicamp.model.operational;

public class InvoiceMain {

	private String number;

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	private String skmNumber;
	private String licenseId;
	private String year;
	private String invoiceType;
	private Long invoiceNumber;
	private String invoiceStatus;
	private Long amount;
	public String getSkmNumber() {
		return skmNumber;
	}
	public void setSkmNumber(String skmNumber) {
		this.skmNumber = skmNumber;
	}
	public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Long getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
		result = prime * result
				+ ((invoiceStatus == null) ? 0 : invoiceStatus.hashCode());
		result = prime * result
				+ ((invoiceType == null) ? 0 : invoiceType.hashCode());
		result = prime * result
				+ ((licenseId == null) ? 0 : licenseId.hashCode());
		result = prime * result
				+ ((skmNumber == null) ? 0 : skmNumber.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceMain other = (InvoiceMain) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (invoiceNumber == null) {
			if (other.invoiceNumber != null)
				return false;
		} else if (!invoiceNumber.equals(other.invoiceNumber))
			return false;
		if (invoiceStatus == null) {
			if (other.invoiceStatus != null)
				return false;
		} else if (!invoiceStatus.equals(other.invoiceStatus))
			return false;
		if (invoiceType == null) {
			if (other.invoiceType != null)
				return false;
		} else if (!invoiceType.equals(other.invoiceType))
			return false;
		if (licenseId == null) {
			if (other.licenseId != null)
				return false;
		} else if (!licenseId.equals(other.licenseId))
			return false;
		if (skmNumber == null) {
			if (other.skmNumber != null)
				return false;
		} else if (!skmNumber.equals(other.skmNumber))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "InvoiceMain [skmNumber=" + skmNumber + ", licenseId="
				+ licenseId + ", year=" + year + ", invoiceType=" + invoiceType
				+ ", invoiceNumber=" + invoiceNumber + ", invoiceStatus="
				+ invoiceStatus + ", amount=" + amount + "]";
	}



}
