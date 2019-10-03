package com.balicamp.service.impl.mx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.mx.DayOffDao;
import com.balicamp.model.mx.DayOff;
import com.balicamp.service.DayOffManager;
import com.balicamp.service.impl.GenericManagerImpl;

@Service("dayOffManager")
public class DayOffManagerImpl extends GenericManagerImpl<DayOff, Long> implements DayOffManager {
	private DayOffDao dayOffDao;

	@Autowired
	public DayOffManagerImpl(DayOffDao genericDao) {
		super(genericDao);
		this.dayOffDao = genericDao;
	}

}
