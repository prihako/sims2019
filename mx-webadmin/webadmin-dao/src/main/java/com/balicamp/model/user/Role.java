/**
 * 
 */
package com.balicamp.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: Role.java 381 2013-03-25 03:55:12Z wayan.agustina $
 */
@Entity
@Table(name = "s_role")
public class Role extends BaseAdminModel implements ISequencesModel {

	private static final long serialVersionUID = -4673887043166067461L;

	public static final Long INDIVIDUAL = Long.valueOf(1);

	public static final Long MERCHANT = Long.valueOf(31);

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
	@Column(name = "name", nullable = true)
	private String name;
	/**
	 * <br/>
	 * column :description
	 **/
	@Column(name = "description", nullable = true)
	private String description;
	/**
	 * <br/>
	 * column :rtype
	 **/
	@Column(name = "rtype", nullable = true)
	private String rtype;
	/**
	 * <br/>
	 * column :editable
	 **/
	@Column(name = "editable", nullable = false)
	private Integer editable;

	@Transient
	private boolean selected;

	public Role() {
	}

	public Role(Long id) {
		this.id = id;
	}

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
	 * column :rtype
	 **/
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	/**
	 * <br/>
	 * column :rtype
	 **/
	public String getRtype() {
		return this.rtype;
	}

	/**
	 * <br/>
	 * column :editable
	 **/
	public void setEditable(Integer editable) {
		this.editable = editable;
	}

	/**
	 * <br/>
	 * column :editable
	 **/
	public Integer getEditable() {
		return this.editable;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "id=" + id + ", \nname=" + name + ", \ndescription=" + description + ", \nrtype=" + rtype
				+ ", \neditable=" + editable + ", \nselected=" + selected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((editable == null) ? 0 : editable.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rtype == null) ? 0 : rtype.hashCode());
		result = prime * result + (selected ? 1231 : 1237);
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
		Role other = (Role) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (editable == null) {
			if (other.editable != null)
				return false;
		} else if (!editable.equals(other.editable))
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
		if (rtype == null) {
			if (other.rtype != null)
				return false;
		} else if (!rtype.equals(other.rtype))
			return false;
		if (selected != other.selected)
			return false;
		return true;
	}

	@Override
	public String getSequenceName() {
		return "s_role_id_seq";
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
		// TODO Auto-generated method stub
		return null;
	}
}
