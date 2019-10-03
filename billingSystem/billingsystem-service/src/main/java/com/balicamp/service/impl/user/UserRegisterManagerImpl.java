package com.balicamp.service.impl.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.Constants;
import com.balicamp.dao.user.UserRegisterDao;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.user.UserRegisterManager;

@Service("userRegisterManager")
public class UserRegisterManagerImpl extends AbstractManager implements
		UserRegisterManager {

	protected final Log securityLog = LogFactory
			.getLog(Constants.Log.SECURITY_LOG);

	private UserRegisterDao userRegisterDao;
	private SystemParameterManager systemParameterManager;

	@Autowired
	public void setUserRegisterDao(UserRegisterDao userRegisterDao) {
		this.userRegisterDao = userRegisterDao;
	}

	@Autowired
	public void setSystemParameterManager(
			SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
