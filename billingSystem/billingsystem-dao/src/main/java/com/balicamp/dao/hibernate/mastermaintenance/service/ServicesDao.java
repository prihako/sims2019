package com.balicamp.dao.hibernate.mastermaintenance.service;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mastermaintenance.service.Services;



/**
 * DAO for Service Entity
 *
 * @author hendy yusprasetya
 *
 */
public interface ServicesDao extends GenericDao<Services, Long>{

	List<Services> findAllServices();

	Services findServicesById(Long serviceId);
	Services findServicesByName(String serviceName);
}
