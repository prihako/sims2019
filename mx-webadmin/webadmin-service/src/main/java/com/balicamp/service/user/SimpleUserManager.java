package com.balicamp.service.user;

import java.util.Set;

import com.balicamp.model.user.UserRole;
import com.balicamp.service.IManager;

public interface SimpleUserManager extends IManager {

	public Set<UserRole> findUserRoleByUserId(Long userId);

}
