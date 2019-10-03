package com.balicamp.model.user;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 * @version $Id: UserAdminGroupId.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Embeddable
public class UserAdminGroupId extends BaseAdminModel {

	private static final long serialVersionUID = -162452607773861246L;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = AdminGroup.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_group_id")
	private AdminGroup adminGroup;

	public UserAdminGroupId() {
	}

	public UserAdminGroupId(User user, AdminGroup adminGroup) {
		this.user = user;
		this.adminGroup = adminGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AdminGroup getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(AdminGroup adminGroup) {
		this.adminGroup = adminGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adminGroup == null) ? 0 : adminGroup.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		UserAdminGroupId other = (UserAdminGroupId) obj;
		if (adminGroup == null) {
			if (other.adminGroup != null)
				return false;
		} else if (!adminGroup.equals(other.adminGroup))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserAdminGroupId [user=" + user + ", adminGroup=" + adminGroup + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
