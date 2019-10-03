/**
 *
 */
package com.balicamp.dao.hibernate.mastermaintenance.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.GenericDaoHibernate;
import com.balicamp.model.mastermaintenance.service.Services;

/**
 * @author hendy yusprasetya
 *
 */
@Repository("servicesDaoHibernate")
public class ServicesDaoHibernate extends GenericDaoHibernate<Services, Long>
		implements ServicesDao {

	private SessionFactory hibernateSessionFactory;

	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("adminHibernateSessionFactory") SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}


	public ServicesDaoHibernate() {
		super(Services.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Services> findAllServices() {
		Query query = getSessionFactory().getCurrentSession().createQuery("select m from Services as m");
		List<Services> serviceList = query.list();
		System.out.println("findAllServices = " + serviceList.get(0).getServiceName());

		return serviceList;
	}

	@Override
	public Services findServicesById(Long serviceId) {
		Query query = getSession()
				.createQuery(
						"select m from Services as m where m.serviceId = "+serviceId);

		Object o = query.uniqueResult();

		return (Services) o;
	}

	@Override
	public Services findServicesByName(String serviceName) {
		Query query = getSession()
				.createQuery(
						"select m from Services as m where m.serviceName = '"+serviceName+"'");

		Object o = query.uniqueResult();

		return (Services) o;
	}



}
