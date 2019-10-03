/**
 * 
 */
package com.balicamp.model.user;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserAdminGroup.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_user_admin_group")
public class UserAdminGroup extends BaseAdminModel {

	private static final long serialVersionUID = 7748844107202517962L;

	@EmbeddedId
	private UserAdminGroupId id;

	/*
	 * @Embedded
	 * private AuditModel auditModel;
	 */

	public UserAdminGroup() {
	}

	public UserAdminGroup(UserAdminGroupId id) {
		this.id = id;
	}

	public UserAdminGroupId getId() {
		return id;
	}

	public void setId(UserAdminGroupId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		UserAdminGroup other = (UserAdminGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserAdminGroup [id=" + id + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
