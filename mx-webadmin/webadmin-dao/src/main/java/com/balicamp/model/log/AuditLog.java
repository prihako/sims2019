/**
 *
 */
package com.balicamp.model.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: AuditLog.java 368 2013-03-08 04:09:59Z wayan.agustina $
 */
@Entity
@Table(name = "t_audit_log")
@SequenceGenerator(name="auditLogId",sequenceName="t_audit_log_id_seq")
public class AuditLog extends BaseAdminModel {

	private static final long serialVersionUID = 9178784235982107376L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="auditLogId")
	private long id;


	/**
	* <br/>
	* column :reference_number
	**/
	@Column(name = "reference_number", nullable = false)
	private String referenceNumber;
	/**
	* <br/>
	* column :data_type
	**/
	@Column(name = "data_type", nullable = false)
	private String dataType;
	/**
	* <br/>
	* column :info
	**/
	@Column(name = "info", nullable = false)
	private String info;
	/**
	* <br/>
	* column :created_by
	**/
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
	/**
	* <br/>
	* column :created_date
	**/
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Transient
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	* <br/>
	* column :reference_number
	**/
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	/**
	* <br/>
	* column :reference_number
	**/
	public String getReferenceNumber() {
		return this.referenceNumber;
	}

	/**
	* <br/>
	* column :data_type
	**/
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	* <br/>
	* column :data_type
	**/
	public String getDataType() {
		return this.dataType;
	}

	/**
	* <br/>
	* column :info
	**/
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	* <br/>
	* column :info
	**/
	public String getInfo() {
		return this.info;
	}

	/**
	* <br/>
	* column :created_by
	**/
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	* <br/>
	* column :created_by
	**/
	public Long getCreatedBy() {
		return this.createdBy;
	}

	/**
	* <br/>
	* column :created_date
	**/
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	* <br/>
	* column :created_date
	**/
	public Date getCreatedDate() {
		return this.createdDate;
	}



	@Override
	public String toString() {
		return "AuditLog [referenceNumber=" + referenceNumber + ", dataType="
				+ dataType + ", info=" + info + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", userName=" + userName
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result
				+ ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditLog other = (AuditLog) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return ""+id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
