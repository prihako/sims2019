package com.balicamp.service.impl.mastermaintenance.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.subservice.SubServicesDao;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;

@Service("subServicesManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class SubServicesManagerImpl extends AbstractManager implements
		SubServicesManager, InitializingBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final SubServicesDao subServicesDao;

	private static final Log log = LogFactory.getLog(SubServicesManagerImpl.class);

	@Autowired
	public SubServicesManagerImpl(SubServicesDao servicesDao) {
		this.subServicesDao = servicesDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SubServices> findAllSubServices() {
//		List<SubServices> subServiceList = new ArrayList<SubServices>();
//		subServiceList = subServicesDao.findAllSubServices();
		return subServicesDao.findAllSubServices();
	}

	@Override
	public SubServices findSubServicesById(Long subserviceId) {
		// TODO Auto-generated method stub
		return subServicesDao.findSubServicesById(subserviceId);
	}

	@Override
	public SubServices findSubServicesByName(String subserviceName) {
		// TODO Auto-generated method stub
		return subServicesDao.findSubServicesByName(subserviceName);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return subServicesDao;
	}

	@Override
	public List<SubServices> findSubServicesByServiceId(BigDecimal serviceId) {
		
		return subServicesDao.findSubServicesByServiceId(serviceId);
	}


}
