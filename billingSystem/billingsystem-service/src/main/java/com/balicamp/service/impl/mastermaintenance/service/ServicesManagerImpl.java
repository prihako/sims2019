package com.balicamp.service.impl.mastermaintenance.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.service.ServicesDao;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;

@Service("servicesManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ServicesManagerImpl extends AbstractManager implements
		ServicesManager, InitializingBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final ServicesDao servicesDao;

	private static final Log log = LogFactory.getLog(ServicesManagerImpl.class);

	@Autowired
	public ServicesManagerImpl(ServicesDao servicesDao) {
		this.servicesDao = servicesDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Services> findAllServices() {
//		List<Services> serviceList = new ArrayList<Services>();
//		serviceList = servicesDao.findAllServices();
		return servicesDao.findAllServices();
	}

	@Override
	public Services findServicesById(Long serviceId) {
		return servicesDao.findServicesById(serviceId);
	}

	@Override
	public Services findServicesByName(String serviceName) {
		// TODO Auto-generated method stub
		return servicesDao.findServicesByName(serviceName);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return servicesDao;
	}


}
