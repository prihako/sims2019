/**
 * 
 */
package com.balicamp.model.parameter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: SystemParameter.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_parameter")
public class SystemParameter extends BaseAdminModel {

	private static final long serialVersionUID = 3840574531021724685L;

	/**
	 * <br/>
	 * column : embededable key
	 **/
	@EmbeddedId
	private SystemParameterId systemParameterId;

	/**
	 * <br/>
	 * column :param_value
	 **/
	@Column(name = "param_value", nullable = true)
	private String paramValue;

	/**
	 * <br/>
	 * column :description
	 **/
	@Column(name = "description", nullable = true)
	private String description;

	/**
	 * <br/>
	 * column :editable
	 **/
	@Column(name = "editable", nullable = true)
	private Long editable;

	/**
	 * <br/>
	 * column :hidden
	 **/
	@Column(name = "hidden", nullable = true)
	private Long hidden;

	/**
	 * <br/>
	 * column :locked
	 **/
	@Column(name = "locked", nullable = true)
	private Long locked;

	/**
	 * <br/>
	 * column :pattern
	 **/
	@Column(name = "pattern", nullable = true)
	private String pattern;

	@Column(name = "type", nullable = true)
	private String type;

	public SystemParameter() {
	}

	public SystemParameter(SystemParameterId systemParameterId) {
		this.systemParameterId = systemParameterId;
	}

	public SystemParameterId getSystemParameterId() {
		return systemParameterId;
	}

	public void setSystemParameterId(SystemParameterId systemParameterId) {
		this.systemParameterId = systemParameterId;
	}

	/**
	 * <br/>
	 * column :param_value
	 **/
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	/**
	 * <br/>
	 * column :param_value
	 **/
	public String getParamValue() {
		return this.paramValue;
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
	 * column :editable
	 **/
	public void setEditable(Long editable) {
		this.editable = editable;
	}

	/**
	 * <br/>
	 * column :editable
	 **/
	public Long getEditable() {
		return this.editable;
	}

	/**
	 * <br/>
	 * column :hidden
	 **/
	public void setHidden(Long hidden) {
		this.hidden = hidden;
	}

	/**
	 * <br/>
	 * column :hidden
	 **/
	public Long getHidden() {
		return this.hidden;
	}

	/**
	 * <br/>
	 * column :locked
	 **/
	public void setLocked(Long locked) {
		this.locked = locked;
	}

	/**
	 * <br/>
	 * column :locked
	 **/
	public Long getLocked() {
		return this.locked;
	}

	/**
	 * <br/>
	 * column :pattern
	 **/
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * <br/>
	 * column :pattern
	 **/
	public String getPattern() {
		return this.pattern;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((editable == null) ? 0 : editable.hashCode());
		result = prime * result + ((hidden == null) ? 0 : hidden.hashCode());
		result = prime * result + ((locked == null) ? 0 : locked.hashCode());
		result = prime * result + ((paramValue == null) ? 0 : paramValue.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + ((systemParameterId == null) ? 0 : systemParameterId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SystemParameter other = (SystemParameter) obj;
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
		if (hidden == null) {
			if (other.hidden != null)
				return false;
		} else if (!hidden.equals(other.hidden))
			return false;
		if (locked == null) {
			if (other.locked != null)
				return false;
		} else if (!locked.equals(other.locked))
			return false;
		if (paramValue == null) {
			if (other.paramValue != null)
				return false;
		} else if (!paramValue.equals(other.paramValue))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (systemParameterId == null) {
			if (other.systemParameterId != null)
				return false;
		} else if (!systemParameterId.equals(other.systemParameterId))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SystemParameter [systemParameterId=" + systemParameterId + ", paramValue=" + paramValue
				+ ", description=" + description + ", editable=" + editable + ", hidden=" + hidden + ", locked="
				+ locked + ", pattern=" + pattern + ", type=" + type + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
