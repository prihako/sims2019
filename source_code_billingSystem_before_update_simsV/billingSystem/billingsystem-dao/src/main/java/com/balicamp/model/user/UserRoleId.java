package com.balicamp.model.user;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * @version $Id: UserRoleId.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Embeddable
public class UserRoleId extends BaseAdminModel {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;

	public UserRoleId() {
	}

	public UserRoleId(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		UserRoleId other = (UserRoleId) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
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
		return "UserRoleId [user=" + user + ", role=" + role + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
