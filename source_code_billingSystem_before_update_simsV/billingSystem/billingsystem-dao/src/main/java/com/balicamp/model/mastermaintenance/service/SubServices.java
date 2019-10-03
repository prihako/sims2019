package com.balicamp.model.mastermaintenance.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

@Entity
@Table(name="M_SUBSERVICES")
@NamedQuery(name="SubServices.findAll", query="SELECT ss FROM SubServices ss")
public class SubServices extends BaseAdminModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SUBSERVICE_ID")
	public BigDecimal subserviceId;

	@Column(name="SUBSERVICE_NAME")
	public String subserviceName;

	public SubServices() {
	}

	public BigDecimal getSubserviceId() {
		return this.subserviceId;
	}

	public void setSubServiceId(BigDecimal subServiceId) {
		this.subserviceId = subServiceId;
	}

	public String getSubserviceName() {
		return this.subserviceName;
	}

	public void setSubServiceName(String subServiceName) {
		this.subserviceName = subServiceName;
	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}