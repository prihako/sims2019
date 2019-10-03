package com.balicamp.service.user;

import com.balicamp.service.IManager;

public interface UserAdminGroupManager extends IManager {
	public int deleteByUserId(Long userId);	
	public int deleteByAdminGroupId(Long adminGroupId);
}


