package com.balicamp.service.impl.ikrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.reor.ReorDao;
import com.balicamp.service.ikrap.IkrapManager;
import com.balicamp.service.impl.AbstractManager;

@Service("ikrapManagerImpl")
public class IkrapManagerImpl extends AbstractManager implements IkrapManager {


	private static final long serialVersionUID = 7140320672003295449L;
	
	@Autowired
	private ReorDao IkrapDao;

	@Override
	public Object getDefaultDao() {
		return null;
	}
	
}
