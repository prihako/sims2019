package com.balicamp.service.impl.iar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.iar.IarDao;
import com.balicamp.service.iar.IarManager;
import com.balicamp.service.impl.AbstractManager;

@Service("iarManagerImpl")
public class IarManagerImpl extends AbstractManager implements IarManager {
	
	private static final long serialVersionUID = 7963560560699607466L;
	
	@Autowired
	private IarDao iarDao;

	@Override
	public Object getDefaultDao() {
		return null;
	}
	
}
