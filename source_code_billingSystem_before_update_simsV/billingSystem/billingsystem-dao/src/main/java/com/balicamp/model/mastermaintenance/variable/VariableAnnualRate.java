package com.balicamp.model.mastermaintenance.variable;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the MM_VAR_ANNUAL_RATE database table.
 * 
 */
@Entity
@Table(name = "T_VAR_ANNUAL_RATE")
// @NamedQuery(name="MmVarAnnualRate.findAll",
// query="SELECT m FROM MmVarAnnualRate m")
public class VariableAnnualRate extends BaseAdminModel implements
		ISequencesModel, PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ANNUAL_RATE_ID_SEQ", sequenceName = "ANNUAL_RATE_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ANNUAL_RATE_ID_SEQ")
	@Column(name = "ANNUAL_RATE_ID")
	private Long annualRateId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;
	
	@Column(name = "SAVE_STATUS")
	private int saveStatus;


	@Column(name = "KM_NO")
	private String kmNo;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "RATE_VALUE")
	private BigDecimal rateValue;

	@Column(name = "RATE_YEAR")
	private BigDecimal rateYear;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	public VariableAnnualRate() {
	}

	public Long getAnnualRateId() {
		return this.annualRateId;
	}

	public void setAnnualRateId(Long annualRateId) {
		this.annualRateId = annualRateId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getKmNo() {
		return this.kmNo;
	}

	public void setKmNo(String kmNo) {
		this.kmNo = kmNo;
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
	
	public int getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(int saveStatus) {
		this.saveStatus = saveStatus;
	}


	public String getStatus() {

		String str = null;
		if (getActiveStatus() == 1) {
			str = "Active";
		} else {
			str = "Inactive";
		}
		return str;
	}
	
	public String getDraftSubmit() {

		String str = null;
		if (getSaveStatus() == 1) {
			str = "Submit";
		} else {
			str = "Draft";
		}
		return str;
	}

	@Override
	public String getPsdValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPsdLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPsdDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSequenceName() {
		return "annual_rate_id_seq";
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}