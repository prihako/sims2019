package com.balicamp.model.user;

import com.balicamp.model.common.BaseObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * class for displaying user in User Maintenance Screen
 * @author yohan
 *
 */
public class UserDisplay extends BaseObject{	
	private static final long serialVersionUID = 446630580550794172L;

	public UserDisplay()
	{
		
	}
	
	public UserDisplay(User user)
	{
		this.userName = user.getUsername();
		this.email = user.getEmail();
		this.userId = user.getId();
		this.name = user.getUserFullName();
	}
	
	public UserDisplay(Long userId, Long userRoleId, String userName, String email, String roleName, String name) {
		this.userName = userName;
		this.email = email;
		this.roleName = roleName;
		this.userId = userId;
		this.roleId = userRoleId;
		this.name = name;
	}
	
	public UserDisplay(Long userId, Long userRoleId, String userName, String email, String roleName, String name,String cardNumber,Long userParentId) {
		this.userName = userName;
		this.email = email;
		this.roleName = roleName;
		this.userId = userId;
		this.roleId = userRoleId;
		this.name = name;
		this.cardNumber = cardNumber;
		this.userParentId = userParentId;
	}
	
	private Long userId;
	private Long roleId;
	private String userName;
	private String email;
	private String roleName;
	private String name;
	private String cardNumber;
	private Long userParentId;
	private String parentUsername;
	
	private boolean selected;
		
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return new StringBuffer(this.getClass().getName()).append(userName).append(email).append(roleName).append(name).toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1859313099, 613304453).append(this.userName).append(this.selected)
				.append(this.roleId).append(this.roleName).append(
						this.userId).append(this.email).append(this.name).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof UserDisplay)) {
			return false;
		}
		UserDisplay rhs = (UserDisplay) object;
		return new EqualsBuilder().append(
				this.userName, rhs.userName)
				.append(this.selected, rhs.selected).append(this.roleId,
						rhs.roleId).append(this.roleName, rhs.roleName)
				.append(this.userId, rhs.userId).append(this.email, rhs.email)
				.append(this.name, rhs.name)
				.isEquals();
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public Long getUserParentId() {
		return userParentId;
	}

	public String getParentUsername() {
		return parentUsername;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setUserParentId(Long userParentId) {
		this.userParentId = userParentId;
	}

	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}

				
}
