package com.balicamp.dao.hibernate.operational;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.hibernate.mastermaintenance.license.LicenseSpectraDao;
import com.balicamp.dao.operational.ApplicationBandwidthDao;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;
import com.balicamp.model.operational.ApplicationBandwidth;

@Repository
public class ApplicationBandwidthDaoHibernate extends
		AdminGenericDaoImpl<ApplicationBandwidth, Long> implements
		ApplicationBandwidthDao {

	private static final String QUERY_LIST_INVOICE_FROM_SPECTRA = " from ApplicationBandwidth where ";

	public ApplicationBandwidthDaoHibernate() {
		super(ApplicationBandwidth.class);
	}

	@Override
	public List<ApplicationBandwidth> findByClientName(String clientCompany) {
		// TODO Auto-generated method stub
		List<ApplicationBandwidth> list = null;
//		String extraQuery = "clientCompany like '%" + clientCompany + "%'";
		Criteria criteria = getSession().createCriteria(ApplicationBandwidth.class);
		criteria.add(Restrictions.ilike("clientCompany", "%"+clientCompany+"%"));
		criteria.addOrder(Order.asc("clientCompany"));

		list = criteria.list();
		return list;
	}

	@Override
	public List<ApplicationBandwidth> findByMethod(String bhpMethod) {
		List<ApplicationBandwidth> list = null;

		String extraQuery = "bhpMethod ='" + bhpMethod + "' order by clientCompany";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_SPECTRA + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<ApplicationBandwidth> findByLicenceNumber(String licenceNumber) {
		List<ApplicationBandwidth> list = null;

		String extraQuery = "licenceNumber ='" + licenceNumber+"' order by clientCompany";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_SPECTRA + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<ApplicationBandwidth> findByClientNo(String clientNumber) {
		List<ApplicationBandwidth> list = null;

		String extraQuery = "clientNumber ='" + clientNumber + "' order by clientCompany";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_SPECTRA + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public ApplicationBandwidth findByLicenceNo(String licenceNo) {
		String extraQuery = "licenceNumber ='" + licenceNo+"' order by clientCompany";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_SPECTRA + extraQuery);
		Object obj = query.uniqueResult();
		return (ApplicationBandwidth) obj;
	}

}
