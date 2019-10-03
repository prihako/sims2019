package com.balicamp.service.impl.kalibrasi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.kalibrasi.KalibrasiDao;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.kalibrasi.KalibrasiManager;

@Service("kalibrasiManagerImpl")
public class KalibrasiManagerImpl extends AbstractManager implements KalibrasiManager {

	private static final long serialVersionUID = 8164582310333507644L;
	
	@Autowired
	private KalibrasiDao kalibrasiDao;

	@Override
	public Object getDefaultDao() {
		return null;
	}
	
}
