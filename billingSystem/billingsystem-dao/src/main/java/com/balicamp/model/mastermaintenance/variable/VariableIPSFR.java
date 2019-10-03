package com.balicamp.model.mastermaintenance.variable;

import java.io.Serializable;

import javax.persistence.*;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.ui.PropertySelectionData;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the MM_VAR_IPSFR database table.
 * 
 */
@Entity
@Table(name="MM_VAR_IPSFR")
public class VariableIPSFR extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VAR_IPSFR_ID_SEQ", sequenceName="VAR_IPSFR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="VAR_IPSFR_ID_SEQ")
	@Column(name="VAR_IPSFR_ID")
	private Long varIpsfrId;
	
	@Column(name="SERVICE_ID")
	private BigDecimal serviceId;

	@Column(name="SUBSERVICE_ID")
	private BigDecimal subserviceId;

	@Column(name="BHP_METHOD")
	private String bhpMethod;

	@Column(name="BLOCK")
	private BigDecimal block;
	
	@Column(name="C")
	private BigDecimal c;

	@Column(name="CLIENT_ID")
	private BigDecimal clientId;

	@Column(name="CLIENT_NAME")
	private String clientName;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	private Date createdOn;

	@Column(name="FREQUENCY")
	private BigDecimal frequency;
	
	@Column(name="I")
	private BigDecimal i;

	@Column(name="LICENCE_NO")
	private String licenceNo;

	@Column(name="N_K")
	private BigDecimal nK;



	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_ON")
	private Date updatedOn;

	public VariableIPSFR() {
	}
	
	public VariableIPSFR(VariableIPSFR variableIPSFR) {
		super();
		this.setVarIpsfrId(variableIPSFR.getVarIpsfrId());
		this.setServiceId(variableIPSFR.getServiceId());
		this.setSubserviceId(variableIPSFR.getSubserviceId());
		this.setBhpMethod(variableIPSFR.getBhpMethod());
		this.setClientId(variableIPSFR.getClientId());
		this.setClientName(variableIPSFR.getClientName());
		this.setFrequency(variableIPSFR.getFrequency());
		this.setLicenceNo(variableIPSFR.getLicenceNo());
		this.setBlock(variableIPSFR.getBlock());
		this.setNK(variableIPSFR.getNK());
		this.setI(variableIPSFR.getI());
		this.setC(variableIPSFR.getC());
		this.setCreatedOn(variableIPSFR.getCreatedOn());
		this.setCreatedBy(variableIPSFR.getCreatedBy());
		this.setUpdatedOn(variableIPSFR.getUpdatedOn());
		this.setUpdatedBy(variableIPSFR.getUpdatedBy());


	}

	public Long getVarIpsfrId() {
		return this.varIpsfrId;
	}

	public void setVarIpsfrId(Long varIpsfrId) {
		this.varIpsfrId = varIpsfrId;
	}

	public String getBhpMethod() {
		return this.bhpMethod;
	}

	public void setBhpMethod(String bhpMethod) {
		this.bhpMethod = bhpMethod;
	}

	public BigDecimal getBlock() {
		return this.block;
	}

	public void setBlock(BigDecimal block) {
		this.block = block;
	}

	public BigDecimal getC() {
		return this.c;
	}

	public void setC(BigDecimal c) {
		this.c = c;
	}

	public BigDecimal getClientId() {
		return this.clientId;
	}

	public void setClientId(BigDecimal clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public BigDecimal getFrequency() {
		return this.frequency;
	}

	public void setFrequency(BigDecimal frequency) {
		this.frequency = frequency;
	}

	public BigDecimal getI() {
		return this.i;
	}

	public void setI(BigDecimal i) {
		this.i = i;
	}

	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public BigDecimal getNK() {
		return this.nK;
	}

	public void setNK(BigDecimal nK) {
		this.nK = nK;
	}

	public BigDecimal getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getSubserviceId() {
		return this.subserviceId;
	}

	public void setSubserviceId(BigDecimal subserviceId) {
		this.subserviceId = subserviceId;
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
		return "var_ipsfr_id_seq";
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