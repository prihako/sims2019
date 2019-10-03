package com.balicamp.service.operational;

import java.util.List;

import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.service.IManager;

public interface ApplicationBandwidthManager extends IManager{
	
	List<ApplicationBandwidth> findByClientName (String clientCompany);
	List<ApplicationBandwidth> findByClientNo (String clientNumber);
	List<ApplicationBandwidth> findByMethod (String bhpMethod);
	List<ApplicationBandwidth> findByLicenceNumber (String apRefNumber);
	ApplicationBandwidth findByLicenceNo (String licenceNo);


}
