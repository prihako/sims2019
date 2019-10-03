package com.balicamp.model.mastermaintenance.variable;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

import java.math.BigDecimal;


/**
 * The persistent class for the MM_VAR_ANN_PERCENT_DTL database table.
 * 
 */
@Entity
@Table(name="T_VAR_ANN_PERCENT_DTL")
public class VariableAnnualPercentageDetail extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {
	private static final long serialVersionUID = 1L;
	

	public VariableAnnualPercentageDetail() {
	}

	@Id
	@SequenceGenerator(name="ANNUAL_PERCENT_DTL_ID_SEQ", sequenceName="ANNUAL_PERCENT_DTL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="ANNUAL_PERCENT_DTL_ID_SEQ")
	@Column(name="ANN_PERCENT_DTL_ID")
	private Long annPercentDtlId;

	@Column(name="ANNUAL_PERCENT_ID")
	private Long annualPercentId;


	@Column(name="PERCENTAGE")
	private BigDecimal percentage;

	@Column(name="YEAR_TO")
	private int yearTo;
	
	public Long getAnnPercentDtlId() {
		return annPercentDtlId;
	}

	public void setAnnPercentDtlId(Long annPercentDtlId) {
		this.annPercentDtlId = annPercentDtlId;
	}
	

	
	public int getYearTo() {
		return yearTo;
	}

	public void setYearTo(int yearTo) {
		this.yearTo = yearTo;
	}

	public Long getAnnualPercentId() {
		return annualPercentId;
	}

	public void setAnnualPercentId(Long annualPercentId) {
		this.annualPercentId = annualPercentId;
	}



	public BigDecimal getPercentage() {
		return this.percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
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
		return "annual_percent_dtl_id_seq";
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