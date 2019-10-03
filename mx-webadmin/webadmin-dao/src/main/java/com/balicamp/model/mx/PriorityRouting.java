package com.balicamp.model.mx;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 *
 */
@Entity
@Table(name = "priority_routing")
public class PriorityRouting extends BaseAdminModel implements ISequencesModel,
		Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "transaction_code")
	private String transactionCode;

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "description")
	private String description;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "routing_code")
	private String routingCode;

	@Column(name = "is_active")
	private boolean activeStatus;

	@Column(name = "product_code_ext")
	private String productCodeExt;

	@Column(name = "gl_code")
	private String glCode;

	@Transient
	private boolean selected;

	@Transient
	private String activeStatusTr;

	@Transient
	private String appStatusTr;

	public String getAppStatusTr() {
		return appStatusTr;
	}

	public void setAppStatusTr(String appStatusTr) {
		this.appStatusTr = appStatusTr;
	}

	public String getActiveStatusTr() {
		if (activeStatus) {
			this.activeStatusTr = "Aktif";
		} else {
			this.activeStatusTr = "Tidak Aktif";
		}
		return activeStatusTr;
	}

	public void setActiveStatusTr(String activeStatusTr) {
		this.activeStatusTr = activeStatusTr;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}

	public String getProductCodeExt() {
		return productCodeExt;
	}

	public void setProductCodeExt(String productCodeExt) {
		this.productCodeExt = productCodeExt;
	}

	@Override
	public Object getPKey() {
		return null;
	}

	@Override
	public String getSequenceName() {
		return "priority_routing_id_seq";
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}