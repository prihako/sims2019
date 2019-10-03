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
 * @version $Id: UserRole.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "s_user_role")
/*
 * @AssociationOverrides({ @AssociationOverride(name = "userRoleId.user.id",
 * joinColumns =
 * 
 * @JoinColumn(name = "id")),
 * 
 * @AssociationOverride(name = "user.id",
 * joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id"),
 * 
 * @JoinColumn(name = "role_id", referencedColumnName = "id") }) })
 */
public class UserRole extends BaseAdminModel {

	private static final long serialVersionUID = -6095464356600394902L;

	@EmbeddedId
	private UserRoleId userRoleId;

	public UserRole() {
	}

	public UserRole(UserRoleId userRoleId) {
		this.userRoleId = userRoleId;
	}

	public UserRoleId getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(UserRoleId userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userRoleId == null) ? 0 : userRoleId.hashCode());
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
		UserRole other = (UserRole) obj;
		if (userRoleId == null) {
			if (other.userRoleId != null)
				return false;
		} else if (!userRoleId.equals(other.userRoleId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserRole [userRoleId=" + userRoleId + "]";
	}

	@Override
	public String getPKey() {
		return null;
	}

}
