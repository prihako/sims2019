package com.balicamp.service.impl.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.MxParmDao;
import com.balicamp.model.mx.MxParm;
import com.balicamp.service.MxParmManager;
import com.balicamp.service.impl.GenericManagerImpl;

@Service("mxParmManager")
public class MxParmManagerImpl extends GenericManagerImpl<MxParm, Long> implements MxParmManager {

	@Autowired
	public MxParmManagerImpl(MxParmDao genericDao) {
		super(genericDao);
		this.mxParmDao = genericDao;
	}

	private static final long serialVersionUID = -3908230684618992868L;

	private MxParmDao mxParmDao;
}
