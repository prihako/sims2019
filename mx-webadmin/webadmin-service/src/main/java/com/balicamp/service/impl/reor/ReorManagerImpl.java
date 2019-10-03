package com.balicamp.service.impl.reor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.reor.ReorDao;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.reor.ReorManager;

@Service("reorManagerImpl")
public class ReorManagerImpl extends AbstractManager implements ReorManager {

	private static final long serialVersionUID = -710098086253876468L;

	@Autowired
	private ReorDao reorDao;

	@Override
	public Object getDefaultDao() {
		return null;
	}
	
}
