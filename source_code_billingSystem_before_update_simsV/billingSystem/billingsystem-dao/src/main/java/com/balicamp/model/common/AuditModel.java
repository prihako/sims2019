package com.balicamp.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: AuditModel.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
@Embeddable
public class AuditModel implements Serializable, Cloneable {

	private static final long serialVersionUID = -5354901851958028103L;

	/**
	* <br/>
	* column :created_by
	**/
	@Column(name = "created_by", nullable = false, updatable = false, insertable = false)
	private Long createdBy;
	/**
	* <br/>
	* column :created_date
	**/
	@Column(name = "created_date", nullable = false, updatable = false, insertable = false)
	private Date createdDate;
	/**
	* <br/>
	* column :changed_by
	**/
	@Column(name = "changed_by", nullable = true, updatable = false, insertable = false)
	private Long changedBy;
	/**
	* <br/>
	* column :changed_date
	**/
	@Column(name = "changed_date", nullable = true, updatable = false, insertable = false)
	private Date changedDate;

	public AuditModel() {
	}

	public AuditModel( Long createdBy, Date createdDate, Long changedBy, Date changedDate ) {
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.changedBy = changedBy;
		this.changedDate = changedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(Long changedBy) {
		this.changedBy = changedBy;
	}

	public Date getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changedBy == null) ? 0 : changedBy.hashCode());
		result = prime * result + ((changedDate == null) ? 0 : changedDate.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
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
		AuditModel other = (AuditModel) obj;
		if (changedBy == null) {
			if (other.changedBy != null)
				return false;
		} else if (!changedBy.equals(other.changedBy))
			return false;
		if (changedDate == null) {
			if (other.changedDate != null)
				return false;
		} else if (!changedDate.equals(other.changedDate))
			return false;
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
		return true;
	}

	@Override
	public String toString() {
		return "AuditModel [createdBy=" + createdBy + ", createdDate=" + createdDate + ", changedBy=" + changedBy +
				", changedDate=" + changedDate + "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException //NOPMD 
	{
		return new AuditModel(createdBy, createdDate, changedBy, changedDate);
	}

}
