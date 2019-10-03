/**
 * 
 */
package com.balicamp.model.personalization;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: UserPGroup.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Entity
@Table(name = "m_user_pgroup")
public class UserPGroup extends BaseAdminModel {

	private static final long serialVersionUID = -8124737337436186081L;

	@EmbeddedId
	private UserPGroupId userPGroupId;

	public UserPGroup() {
	}

	public UserPGroup(UserPGroupId userPGroupId) {
		this.userPGroupId = userPGroupId;
	}

	public UserPGroupId getUserPGroupId() {
		return userPGroupId;
	}

	public void setUserPGroupId(UserPGroupId userPGroupId) {
		this.userPGroupId = userPGroupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userPGroupId == null) ? 0 : userPGroupId.hashCode());
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
		UserPGroup other = (UserPGroup) obj;
		if (userPGroupId == null) {
			if (other.userPGroupId != null)
				return false;
		} else if (!userPGroupId.equals(other.userPGroupId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserPGroup [userPGroupId=" + userPGroupId + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
