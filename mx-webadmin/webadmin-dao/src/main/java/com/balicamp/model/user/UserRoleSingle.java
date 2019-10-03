package com.balicamp.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

@Entity
@Table(name = "s_user_role")
public class UserRoleSingle extends BaseAdminModel {
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private Long userId;

	@Id
	@Column(name = "role_id")
	private Long roleId;

	public UserRoleSingle() {
	}

	public UserRoleSingle(Long userId, Long roleId) {
		this.roleId = roleId;
		this.userId = userId;
	}

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

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
