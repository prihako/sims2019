package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the MM_LIC_BHP_DTL database table.
 * 
 */
@Entity
@Table(name="MM_LIC_BHP_DTL")
@NamedQuery(name="MmLicBhpDtl.findAll", query="SELECT m FROM MmLicBhpDtl m")
public class MmLicBhpDtl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LIC_BHP_DTL_ID")
	private long licBhpDtlId;

	@Column(name="ANNUAL_IPSFR_COST")
	private BigDecimal annualIpsfrCost;

	@Column(name="ANNUAL_IPSFR_COST_ROUND")
	private BigDecimal annualIpsfrCostRound;

	@Column(name="BHP_PERCENT")
	private BigDecimal bhpPercent;

	@Column(name="CALC_INDEX")
	private BigDecimal calcIndex;

	@Column(name="PAYMENT_YEAR")
	private BigDecimal paymentYear;

	private BigDecimal rate;

	@Column(name="TOTAL_PAYMENT")
	private BigDecimal totalPayment;

	@Column(name="UPFRONT_FEE")
	private BigDecimal upfrontFee;

	//bi-directional many-to-one association to MmLicence
	@ManyToOne
	@JoinColumn(name="LICENCE_ID", referencedColumnName="LICENCE_NO")
	private MmLicence mmLicence;

	public MmLicBhpDtl() {
	}

	public long getLicBhpDtlId() {
		return this.licBhpDtlId;
	}

	public void setLicBhpDtlId(long licBhpDtlId) {
		this.licBhpDtlId = licBhpDtlId;
	}

	public BigDecimal getAnnualIpsfrCost() {
		return this.annualIpsfrCost;
	}

	public void setAnnualIpsfrCost(BigDecimal annualIpsfrCost) {
		this.annualIpsfrCost = annualIpsfrCost;
	}

	public BigDecimal getAnnualIpsfrCostRound() {
		return this.annualIpsfrCostRound;
	}

	public void setAnnualIpsfrCostRound(BigDecimal annualIpsfrCostRound) {
		this.annualIpsfrCostRound = annualIpsfrCostRound;
	}

	public BigDecimal getBhpPercent() {
		return this.bhpPercent;
	}

	public void setBhpPercent(BigDecimal bhpPercent) {
		this.bhpPercent = bhpPercent;
	}

	public BigDecimal getCalcIndex() {
		return this.calcIndex;
	}

	public void setCalcIndex(BigDecimal calcIndex) {
		this.calcIndex = calcIndex;
	}

	public BigDecimal getPaymentYear() {
		return this.paymentYear;
	}

	public void setPaymentYear(BigDecimal paymentYear) {
		this.paymentYear = paymentYear;
	}

	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getTotalPayment() {
		return this.totalPayment;
	}

	public void setTotalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
	}

	public BigDecimal getUpfrontFee() {
		return this.upfrontFee;
	}

	public void setUpfrontFee(BigDecimal upfrontFee) {
		this.upfrontFee = upfrontFee;
	}

	public MmLicence getMmLicence() {
		return this.mmLicence;
	}

	public void setMmLicence(MmLicence mmLicence) {
		this.mmLicence = mmLicence;
	}

}