package com.balicamp.service.impl.user;

import java.io.Serializable;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.dao.user.UserDao;
import com.balicamp.dao.user.UserRoleDao;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserRole;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.user.SimpleUserManager;

//@Service("simpleUserManager")
public class SimpleUserManagerImpl extends AbstractManager implements
		SimpleUserManager, InitializingBean {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public User findById(Serializable id) {
		return userDao.findById((Long) id);
	}

	@Override
	public Set<UserRole> findUserRoleByUserId(Long userId) {
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		userDao.findById(new Long(8));

	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
