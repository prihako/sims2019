package com.balicamp.model.operational;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the T_VAR_ANNUAL_RATE database table.
 * 
 */
@Entity
@Table(name="T_VAR_ANNUAL_RATE")
@NamedQuery(name="TVarAnnualRate.findAll", query="SELECT t FROM TVarAnnualRate t")
public class TVarAnnualRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ANNUAL_RATE_ID")
	private long annualRateId;

	@Column(name="ACTIVE_STATUS")
	private BigDecimal activeStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	private Date createdOn;

	@Column(name="KM_NO")
	private String kmNo;

	@Column(name="RATE_VALUE")
	private BigDecimal rateValue;

	@Column(name="RATE_YEAR")
	private BigDecimal rateYear;

	@Column(name="SAVE_STATUS")
	private BigDecimal saveStatus;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_ON")
	private Date updatedOn;

	public TVarAnnualRate() {
	}

	public long getAnnualRateId() {
		return this.annualRateId;
	}

	public void setAnnualRateId(long annualRateId) {
		this.annualRateId = annualRateId;
	}

	public BigDecimal getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(BigDecimal activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getKmNo() {
		return this.kmNo;
	}

	public void setKmNo(String kmNo) {
		this.kmNo = kmNo;
	}

	public BigDecimal getRateValue() {
		return this.rateValue;
	}

	public void setRateValue(BigDecimal rateValue) {
		this.rateValue = rateValue;
	}

	public BigDecimal getRateYear() {
		return this.rateYear;
	}

	public void setRateYear(BigDecimal rateYear) {
		this.rateYear = rateYear;
	}

	public BigDecimal getSaveStatus() {
		return this.saveStatus;
	}

	public void setSaveStatus(BigDecimal saveStatus) {
		this.saveStatus = saveStatus;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}