package com.balicamp.service.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.user.UserAdminGroupDao;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.user.UserAdminGroupManager;

@Service("userAdminGroupManager")
public class UserAdminGroupManagerImpl extends AbstractManager implements
		UserAdminGroupManager {
	private UserAdminGroupDao userAdminGroupDao;

	@Autowired
	public void setUserAdminGroupDao(UserAdminGroupDao userAdminGroupDao) {// NOPMD
		this.userAdminGroupDao = userAdminGroupDao;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int deleteByUserId(Long userId) {
		return userAdminGroupDao.deleteByUserId(userId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int deleteByAdminGroupId(Long adminGroupId) {
		return userAdminGroupDao.deleteByAdminGroupId(adminGroupId);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}
}
