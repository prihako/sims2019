package com.balicamp.model.operational;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the T_VAR_ANNUAL_PERCENT database table.
 * 
 */
@Entity
@Table(name="T_VAR_ANNUAL_PERCENT")
@NamedQuery(name="TVarAnnualPercent.findAll", query="SELECT t FROM TVarAnnualPercent t")
public class TVarAnnualPercent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ANNUAL_PERCENT_ID")
	private long annualPercentId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	private Date createdOn;

	@Column(name="KM_NO")
	private String kmNo;

	@Column(name="PERCENT_YEAR")
	private BigDecimal percentYear;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_ON")
	private Date updatedOn;

	@Column(name="VARIABLE_STATUS")
	private BigDecimal variableStatus;

	public TVarAnnualPercent() {
	}

	public long getAnnualPercentId() {
		return this.annualPercentId;
	}

	public void setAnnualPercentId(long annualPercentId) {
		this.annualPercentId = annualPercentId;
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

	public BigDecimal getPercentYear() {
		return this.percentYear;
	}

	public void setPercentYear(BigDecimal percentYear) {
		this.percentYear = percentYear;
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

	public BigDecimal getVariableStatus() {
		return this.variableStatus;
	}

	public void setVariableStatus(BigDecimal variableStatus) {
		this.variableStatus = variableStatus;
	}

}