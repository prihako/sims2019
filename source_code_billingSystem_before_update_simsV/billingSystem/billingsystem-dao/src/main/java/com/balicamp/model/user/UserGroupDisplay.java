package com.balicamp.model.user;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.balicamp.model.common.BaseObject;

/**
 * class for displaying user in Group Maintenance Screen
 * @author yohan
 *
 */
public class UserGroupDisplay  extends BaseObject {
	private Long userId;
	private String userName;
	private String userRoles;
	
	public UserGroupDisplay(Long userId, String userName, String userRoles) {
		this.userId = userId;
		this.userName = userName;
		this.userRoles = userRoles;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}
	
	public String getDisplay() {
		StringBuffer sb = new StringBuffer(userName);
		if (userRoles != null) {
			sb.append(" <").append(userRoles).append(">");
		}
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof UserGroupDisplay)) {
			return false;
		}
		UserGroupDisplay rhs = (UserGroupDisplay) object;
		return new EqualsBuilder().append(
				this.userName, rhs.userName).append(this.userId, rhs.userId)
				.append(this.userRoles, rhs.userRoles).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1834513317, 1923481367)
				.append(this.userName).append(this.userId)
				.append(this.userRoles).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("userRoles", this.userRoles)
				.append("userId", this.userId)
				.append("userName", this.userName).toString();
	}
	
	
}
