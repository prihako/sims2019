package com.balicamp.dao.hibernate.mastermaintenance.subservice;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mastermaintenance.service.SubServices;



/**
 * DAO for Sub Service Entity
 *
 * @author hendy yusprasetya
 *
 */
public interface SubServicesDao extends GenericDao<SubServices, Long>{

	List<SubServices> findAllSubServices();

	SubServices findSubServicesById(Long subserviceId);
	SubServices findSubServicesByName(String subServiceName);
	
	//Ini prihako yg ngubah,
	
	List<SubServices> findSubServicesByServiceId(BigDecimal serviceId);
}
