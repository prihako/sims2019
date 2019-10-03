/**
 * 
 */
package com.balicamp.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: AdminGroup.java 381 2013-03-25 03:55:12Z wayan.agustina $
 */
@Entity
@Table(name = "s_admin_group")
public class AdminGroup extends BaseAdminModel implements ISequencesModel {

	private static final long serialVersionUID = -4946943442334212454L;

	/**
	* <br/>
	* column :id
	**/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	/**
	* <br/>
	* column :name
	**/
	@Column(name = "name", nullable = false)
	private String name;
	/**
	* <br/>
	* column :description
	**/
	@Column(name = "description", nullable = true)
	private String description;
	/**
	* <br/>
	* column :created_by
	**/
	@Column(name = "created_by", nullable = false)
	private String createdBy;
	/**
	* <br/>
	* column :created_Date
	**/
	@Column(name = "created_Date", nullable = false)
	private Date createdDate;
	/**
	* <br/>
	* column :changed_by
	**/
	@Column(name = "changed_by", nullable = true)
	private String changedBy;
	/**
	* <br/>
	* column :changed_Date
	**/
	@Column(name = "changed_Date", nullable = true)
	private Date changedDate;

	@Transient
	private boolean selected;

	/**
	* <br/>
	* column :name
	**/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* <br/>
	* column :name
	**/
	public String getName() {
		return this.name;
	}

	/**
	* <br/>
	* column :description
	**/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	* <br/>
	* column :description
	**/
	public String getDescription() {
		return this.description;
	}

	/**
	* <br/>
	* column :created_by
	**/
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	* <br/>
	* column :created_by
	**/
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	* <br/>
	* column :created_Date
	**/
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	* <br/>
	* column :created_Date
	**/
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	* <br/>
	* column :changed_by
	**/
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	* <br/>
	* column :changed_by
	**/
	public String getChangedBy() {
		return this.changedBy;
	}

	/**
	* <br/>
	* column :changed_Date
	**/
	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	/**
	* <br/>
	* column :changed_Date
	**/
	public Date getChangedDate() {
		return this.changedDate;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nname=" + name + ", \ndescription=" + description + ", \ncreatedBy=" + createdBy
				+ ", \ncreatedDate=" + createdDate + ", \nchangedBy=" + changedBy + ", \nchangedDate=" + changedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changedBy == null) ? 0 : changedBy.hashCode());
		result = prime * result + ((changedDate == null) ? 0 : changedDate.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AdminGroup other = (AdminGroup) obj;
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String getSequenceName() {
		return "s_admin_group_id_seq";
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPKey() {
		return null;
	}
}
