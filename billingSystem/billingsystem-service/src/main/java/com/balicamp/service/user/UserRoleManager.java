package com.balicamp.service.user;

import java.util.List;

import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserRole;
import com.balicamp.service.IManager;

public interface UserRoleManager extends IManager {
	int deleteByUserIdAndRoleId(Long userId, Long roleId);

	int deleteByRoleId(Long roleId);

	int deleteByUserId(Long userId);

	List<UserRole> findByUser(User user);

	AuditLog saveChangeUserRole(User user, Long roleId);

	void syncMerchantOrIndividu(User user);

	void save(UserRole userRole);

}
