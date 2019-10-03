package com.balicamp.model.mx;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.user.User;

@Entity
@Table(name = "m_bank")
@SequenceGenerator(name="bankId",sequenceName="m_bank_id_seq")
public class Bank extends BaseAdminModel implements Serializable {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="bankId")
	private Long id;

	@Column(name = "bank_code", nullable = false)
	private String bankCode;

	@Column(name = "bank_name", nullable = false)
	private String bankName;

	@Column(name = "bin_prefix", nullable = false)
	private String binPrefix;

	@Column(name = "is_alto", nullable = false)
	private boolean isAlto;

	@Column(name = "is_atmb", nullable = false)
	private boolean isAtmb;

	@Column(name = "priority", nullable = false)
	private String priority;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name="created_by")
	private User createdBy;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name="updated_by")
	private User updatedBy;

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}


	public boolean getIsAlto() {
		return isAlto;
	}

	public void setIsAlto(boolean isAlto) {
		this.isAlto = isAlto;
	}

	public boolean getIsAtmb() {
		return isAtmb;
	}

	public void setIsAtmb(boolean isAtmb) {
		this.isAtmb = isAtmb;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBinPrefix() {
		return binPrefix;
	}

	public void setBinPrefix(String binPrefix) {
		this.binPrefix = binPrefix;
	}

	@Override
	public String getPKey() {
		return this.id.toString();
	}
}
