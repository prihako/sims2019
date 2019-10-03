package com.balicamp.service.mastermaintenance.service;

import java.util.List;

import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.service.IManager;



public interface ServicesManager extends IManager {

	List<Services> findAllServices();

	Services findServicesById(Long serviceId);
	Services findServicesByName(String serviceName);

}
