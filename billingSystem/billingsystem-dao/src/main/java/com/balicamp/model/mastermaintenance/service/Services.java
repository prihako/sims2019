package com.balicamp.model.mastermaintenance.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;




/**
 * The persistent class for the M_SERVICES database table.
 *
 */
@Entity
@Table(name="M_SERVICES")
public class Services extends BaseAdminModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SERVICE_ID")
	public BigDecimal serviceId;

	@Column(name="SERVICE_NAME")
	public String serviceName;

	public Services() {
	}

	public BigDecimal getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}