package com.balicamp.model.mastermaintenance.variable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.ui.PropertySelectionData;


/**
 * The persistent class for the MM_VAR_ANNUAL_PERCENT database table.
 * @Author Prihako Nurukat
 */
@Entity
@Table(name="T_VAR_ANNUAL_PERCENT")
public class VariableAnnualPercentage extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ANNUAL_PERCENT_ID_SEQ", sequenceName="ANNUAL_PERCENT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="ANNUAL_PERCENT_ID_SEQ")
	@Column(name="ANNUAL_PERCENT_ID")
	private Long annualPercentId;

	@Column(name="PERCENT_YEAR")
	private Integer percentYear;

	@Column(name="KM_NO")
	private String kmNo;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_ON")
	private Date createdOn;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_ON")
	private Date updatedOn;

	@Column(name="VARIABLE_STATUS")
	private int variableStatus;
	
	// bi-directional many-to-one association to T_Var_Annual_Percent
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "variableAnnualPercent")
	private List<License> licenses;

	public VariableAnnualPercentage() {

	}

	public List<License> getLicenses(){
		return licenses;
	}
	
	public void setLicense(List<License> licenses){
		this.licenses = licenses;
	}
	
	public Long getAnnualPercentId() {
		return annualPercentId;
	}

	public void setAnnualPercentId(Long annualPercentId) {
		this.annualPercentId = annualPercentId;
	}

	public Integer getPercentYear() {
		return percentYear;
	}



	public void setPercentYear(Integer percentYear) {
		this.percentYear = percentYear;
	}



	public String getKmNo() {
		return kmNo;
	}



	public void setKmNo(String kmNo) {
		this.kmNo = kmNo;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public Date getCreatedOn() {
		return createdOn;
	}



	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



	public String getUpdatedBy() {
		return updatedBy;
	}



	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}



	public Date getUpdatedOn() {
		return updatedOn;
	}



	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}



	public int getVariableStatus() {
		return variableStatus;
	}



	public void setVariableStatus(int variableStatus) {
		this.variableStatus = variableStatus;
	}



	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return "annual_percent_id_seq";
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

	//Fungsi ini digunakan untuk menampilkan kata2 aktif, atau tidak aktif, bukannya 1 ato 0
	public String getStatus() {

		String str=null;
		if (getVariableStatus()==1){
			str="Active";
		}else{
			str="Inactive";
		}
		return str;
	}

}