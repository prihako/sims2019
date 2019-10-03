package com.balicamp.dao.operational;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.operational.ApplicationBandwidth;

public interface ApplicationBandwidthDao extends AdminGenericDao<ApplicationBandwidth, Long> {
	
	List<ApplicationBandwidth> findByClientName (String clientCompany);
	List<ApplicationBandwidth> findByClientNo (String clientNumber);
	List<ApplicationBandwidth> findByMethod (String bhpMethod);
	List<ApplicationBandwidth> findByLicenceNumber (String apRefNumber);
	ApplicationBandwidth findByLicenceNo (String licenceNo);
}
