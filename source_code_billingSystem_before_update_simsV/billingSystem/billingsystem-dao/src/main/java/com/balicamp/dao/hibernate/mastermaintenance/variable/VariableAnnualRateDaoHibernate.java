package com.balicamp.dao.hibernate.mastermaintenance.variable;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.exception.ApplicationException;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.License;

@Repository("variableAnnualRateDaoHibernate")
public class VariableAnnualRateDaoHibernate extends
		AdminGenericDaoImpl<VariableAnnualRate, Long> implements
		VariableAnnualRateDao {

	public VariableAnnualRateDaoHibernate() {          
		super(VariableAnnualRate.class);
	}

	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	@Override
	public List<VariableAnnualRate> findByStatus(final int activeStatus) {
		// Query query = getSessionFactory().getCurrentSession().createQuery(
		// "select v from VariableAnnualRate as v where v.activeStatus="
		// + activeStatus);
		// List<VariableAnnualRate> variableAnnualRateList = query.list();
		List<VariableAnnualRate> list = null;
		try {
			list = (List) getHibernateTemplate().execute(
					new HibernateCallback() {
						@Override
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							String extraQuery = null;
							System.out.println("status = " + activeStatus);

							if (activeStatus == 2) {
								extraQuery = "select v from VariableAnnualRate as v order by v.rateYear";
							} else {
								extraQuery = "select v from VariableAnnualRate as v where v.activeStatus="
										+ activeStatus + " order by v.rateYear";

							}
							Query query = getSessionFactory()
									.getCurrentSession()
									.createQuery(extraQuery);
							return query.list();
						}
					});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}
		return list;
	}

	@Override
	public List<VariableAnnualRate> findByBaseOnNoteAndStatus(
			final String baseOnNote, final int activeStatus) {
//		List<VariableAnnualRate> list = null;
//		try {
//			list = (List) getHibernateTemplate().execute(
//					new HibernateCallback() {
//						@Override
//						public Object doInHibernate(Session session)
//								throws HibernateException, SQLException {
//							System.out.println("status = " + activeStatus);
//
//							String extraQuery = null;
//							if (activeStatus == 2) {
//								extraQuery = "select v from VariableAnnualRate as v where v.kmNo='"
//										+ baseOnNote + "' order by v.rateYear";
//							} else {
//								extraQuery = "select v from VariableAnnualRate as v where v.kmNo like '%"
//										+ baseOnNote
//										+ "%' and v.activeStatus="
//										+ activeStatus + " order by v.rateYear";
//							}
//							Query query = session.createQuery(extraQuery);
//							return query.list();
//						}
//					});
//		} catch (DataAccessException ex) {
//			daoLog.error(ex);
//			ex.printStackTrace();
//			throw new ApplicationException(ex.getMessage());
//		}
//		return list;
		
		List<VariableAnnualRate> list = null;
		
		if (activeStatus == 2) {
			Criteria crit = getSession().createCriteria(VariableAnnualRate.class);
			crit.add(Restrictions.ilike("kmNo", "%" + baseOnNote + "%"));
			crit.addOrder(Order.asc("rateYear"));
	
			list = crit.list();
		}else{
			Criteria crit = getSession().createCriteria(VariableAnnualRate.class);
			crit.add(Restrictions.ilike("kmNo", "%" + baseOnNote + "%")).add(Restrictions.eq("activeStatus", activeStatus));
			crit.addOrder(Order.asc("rateYear"));
	
			list = crit.list();
		}
		
		return  list;
	}

	@Override
	public VariableAnnualRate findByAnnualRateId(Long annualRateId) {

		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualRate as v where v.annualRateId="
						+ annualRateId);
		Object obj = query.uniqueResult();
		return (VariableAnnualRate) obj;
	}

	@Override
	public void replaceToInactive(int activeStatus) {
		// TODO Auto-generated method stub
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"update  VariableAnnualRate  set activeStatus="
						+ activeStatus + " where activeStatus=1");
		query.executeUpdate();

	}

	@Override
	public void replaceToActive(int activeStatus, Long annualRateId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"update  VariableAnnualRate  set activeStatus="
						+ activeStatus
						+ " where activeStatus=0 and annualRateId="
						+ annualRateId);
		query.executeUpdate();

	}

	@Override
	public VariableAnnualRate findByYear(BigDecimal year) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualRate as v where v.rateYear="
						+ year);
		Object obj = query.uniqueResult();
		return (VariableAnnualRate) obj;
	}

}
