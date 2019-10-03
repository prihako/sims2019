package com.balicamp.model.personalization;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.user.User;

/**
 * @version $Id: UserPGroupId.java 141 2013-01-03 07:55:27Z bagus.sugitayasa $
 */
@Embeddable
public class UserPGroupId extends BaseAdminModel {

	private static final long serialVersionUID = 1425370764355115059L;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = PGroup.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "pgroup_id")
	private PGroup pgroup;

	public UserPGroupId() {
		// do nothing
	}

	public UserPGroupId(User user, PGroup pgroup) {
		this.user = user;
		this.pgroup = pgroup;
	}

	/**
	* <br/>
	* column :user_id
	**/
	public User getUser() {
		return user;
	}

	/**
	* <br/>
	* column :user_id
	**/
	public void setUser(User user) {
		this.user = user;
	}

	/**
	* <br/>
	* column :pgroup_id
	**/
	public PGroup getPgroup() {
		return pgroup;
	}

	/**
	* <br/>
	* column :pgroup_id
	**/
	public void setPgroup(PGroup pgroup) {
		this.pgroup = pgroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pgroup == null) ? 0 : pgroup.hashCode());
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
		UserPGroupId other = (UserPGroupId) obj;
		if (pgroup == null) {
			if (other.pgroup != null)
				return false;
		} else if (!pgroup.equals(other.pgroup))
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
		return "UserPGroupId [user=" + user + ", pgroup=" + pgroup + "]";
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
