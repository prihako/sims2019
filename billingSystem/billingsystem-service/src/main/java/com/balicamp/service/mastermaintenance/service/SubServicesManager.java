package com.balicamp.service.mastermaintenance.service;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.service.IManager;



public interface SubServicesManager extends IManager {

	List<SubServices> findAllSubServices();

	SubServices findSubServicesById(Long subserviceId);
	SubServices findSubServicesByName(String subserviceName);
	
	//Ini prihako yg ngubah
	List<SubServices> findSubServicesByServiceId(BigDecimal serviceId);

}
