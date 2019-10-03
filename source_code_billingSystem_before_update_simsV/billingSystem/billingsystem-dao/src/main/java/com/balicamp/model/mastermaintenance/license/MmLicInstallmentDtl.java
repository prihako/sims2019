package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the MM_LIC_INSTALLMENT_DTL database table.
 * 
 */
@Entity
@Table(name="MM_LIC_INSTALLMENT_DTL")
@NamedQuery(name="MmLicInstallmentDtl.findAll", query="SELECT m FROM MmLicInstallmentDtl m")
public class MmLicInstallmentDtl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="INSTALLMENT_DTL_ID")
	private long installmentDtlId;

	@Temporal(TemporalType.DATE)
	@Column(name="DUE_DATE")
	private Date dueDate;

	@Column(name="INSTALLMENT_NO")
	private BigDecimal installmentNo;

	@Column(name="INSTALLMENT_PERCENT")
	private BigDecimal installmentPercent;

	@Column(name="LICENCE_ID")
	private String licenceId;

	public MmLicInstallmentDtl() {
	}

	public long getInstallmentDtlId() {
		return this.installmentDtlId;
	}

	public void setInstallmentDtlId(long installmentDtlId) {
		this.installmentDtlId = installmentDtlId;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getInstallmentNo() {
		return this.installmentNo;
	}

	public void setInstallmentNo(BigDecimal installmentNo) {
		this.installmentNo = installmentNo;
	}

	public BigDecimal getInstallmentPercent() {
		return this.installmentPercent;
	}

	public void setInstallmentPercent(BigDecimal installmentPercent) {
		this.installmentPercent = installmentPercent;
	}

	public String getLicenceId() {
		return this.licenceId;
	}

	public void setLicenceId(String licenceId) {
		this.licenceId = licenceId;
	}

}