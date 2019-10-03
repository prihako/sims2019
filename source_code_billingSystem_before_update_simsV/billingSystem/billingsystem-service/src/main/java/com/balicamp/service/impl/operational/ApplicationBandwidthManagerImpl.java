package com.balicamp.service.impl.operational;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.operational.ApplicationBandwidthDao;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;

@Service("applicationBandwidthManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ApplicationBandwidthManagerImpl extends AbstractManager implements ApplicationBandwidthManager{
	
	
	private static final long serialVersionUID = 1L;
	private final ApplicationBandwidthDao applicationBandwidthDao;

	@Autowired
	public ApplicationBandwidthManagerImpl(ApplicationBandwidthDao applicationBandwidthDao){
		this.applicationBandwidthDao=applicationBandwidthDao;
	}

	@Override
	public List<ApplicationBandwidth> findByClientName(String clientCompany) {
		// TODO Auto-generated method stub
		List<ApplicationBandwidth> list = applicationBandwidthDao.findByClientName(clientCompany);
		return list;
	}

	@Override
	public List<ApplicationBandwidth> findByMethod(String bhpMethod) {
		// TODO Auto-generated method stub
		List<ApplicationBandwidth> list = applicationBandwidthDao.findByMethod(bhpMethod);
		
		return list;
	}

	@Override
	public List<ApplicationBandwidth> findByLicenceNumber(String apRefNumber) {
		// TODO Auto-generated method stub
		List<ApplicationBandwidth> list = applicationBandwidthDao.findByLicenceNumber(apRefNumber);

		return list;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ApplicationBandwidth> findByClientNo(String clientNumber) {
		List<ApplicationBandwidth> list = applicationBandwidthDao.findByClientNo(clientNumber);

		return list;
	}

	@Override
	public ApplicationBandwidth findByLicenceNo(String licenceNo) {
		ApplicationBandwidth obj = applicationBandwidthDao.findByLicenceNo(licenceNo);
		return obj;
	}


}
