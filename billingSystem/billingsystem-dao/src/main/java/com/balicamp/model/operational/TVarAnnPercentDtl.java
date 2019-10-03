package com.balicamp.model.operational;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the T_VAR_ANN_PERCENT_DTL database table.
 * 
 */
@Entity
@Table(name="T_VAR_ANN_PERCENT_DTL")
@NamedQuery(name="TVarAnnPercentDtl.findAll", query="SELECT t FROM TVarAnnPercentDtl t")
public class TVarAnnPercentDtl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ANN_PERCENT_DTL_ID")
	private long annPercentDtlId;

	@Column(name="ANNUAL_PERCENT_ID")
	private BigDecimal annualPercentId;

	private BigDecimal percentage;

	@Column(name="YEAR_TO")
	private BigDecimal yearTo;

	public TVarAnnPercentDtl() {
	}

	public long getAnnPercentDtlId() {
		return this.annPercentDtlId;
	}

	public void setAnnPercentDtlId(long annPercentDtlId) {
		this.annPercentDtlId = annPercentDtlId;
	}

	public BigDecimal getAnnualPercentId() {
		return this.annualPercentId;
	}

	public void setAnnualPercentId(BigDecimal annualPercentId) {
		this.annualPercentId = annualPercentId;
	}

	public BigDecimal getPercentage() {
		return this.percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getYearTo() {
		return this.yearTo;
	}

	public void setYearTo(BigDecimal yearTo) {
		this.yearTo = yearTo;
	}

}