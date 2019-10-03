/**
 *
 */
package com.balicamp.dao.hibernate.mastermaintenance.subservice;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.GenericDaoHibernate;
import com.balicamp.model.mastermaintenance.service.SubServices;

/**
 * @author hendy yusprasetya
 *
 */
@Repository("subServicesDaoHibernate")
public class SubServicesDaoHibernate extends GenericDaoHibernate<SubServices, Long>
		implements SubServicesDao {

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

	public SubServicesDaoHibernate() {
		super(SubServices.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<SubServices> findAllSubServices() {
		
//		Query query = getSession().createQuery("select s from SubServices as s");
//		List<SubServices> subService = new ArrayList<SubServices>();
//		subService = query.list();
//		System.out.println("findAllSubServices = " + subService.get(0).getSubServiceName());
		
		Query query = getSessionFactory().getCurrentSession().createQuery("select s from SubServices as s");
		List<SubServices> subServiceList = query.list();
		System.out.println("findAllSubServices = " + subServiceList.get(0).getSubserviceName());

		return subServiceList;
	}

	@Override
	public SubServices findSubServicesById(Long subserviceId) {
		Query query = getSession()
				.createQuery(
						"select m from SubServices as m where m.subserviceId = "+subserviceId);

		Object o = query.uniqueResult();

		return (SubServices) o;
	}

	@Override
	public SubServices findSubServicesByName(String subServiceName) {
		Query query = getSession()
				.createQuery(
						"select m from SubServices as m where m.subserviceName = '"+subServiceName+"'");

		Object o = query.uniqueResult();

		return (SubServices) o;
	}

	@Override
	public List<SubServices> findSubServicesByServiceId(BigDecimal serviceId) {
		Query query = getSession().createSQLQuery("SELECT * FROM m_subservices s WHERE s.service_id = :serviceId")
				.addEntity(SubServices.class).setParameter("serviceId", serviceId);
		List<SubServices> result = query.list();
		return result;
	}


}
