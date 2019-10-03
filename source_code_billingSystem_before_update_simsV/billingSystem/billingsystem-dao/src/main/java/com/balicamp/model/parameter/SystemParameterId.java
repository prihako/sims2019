package com.balicamp.model.parameter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
@Embeddable
public class SystemParameterId extends BaseAdminModel {

	private static final long serialVersionUID = 4759749463062394886L;

	/**
	 * <br/>
	 * column :param_group
	 **/
	@Column(name = "param_group", nullable = false)
	private String group;

	/**
	 * <br/>
	 * column :param_name
	 **/
	@Column(name = "param_name", nullable = false)
	private String name;

	public SystemParameterId() {
	}

	public SystemParameterId(String groupName, String paramName) {
		this.group = groupName;
		this.name = paramName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		SystemParameterId other = (SystemParameterId) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SystemParameterId [group=" + group + ", name=" + name + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
