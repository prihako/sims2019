package com.balicamp.dao.hibernate.mastermaintenance.variable;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.exception.ApplicationException;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;

@Repository("variableDaoHibernate")
public class VariableAnnualPercentageDaoHibernate extends
		AdminGenericDaoImpl<VariableAnnualPercentage, Long> implements
		VariableAnnualPercentageDao {

	public VariableAnnualPercentageDaoHibernate() {
		super(VariableAnnualPercentage.class);
		// TODO Auto-generated constructor stub
	}

	private static final String QUERY_LIST_VARIABLE = " from VariableAnnualPercentage where ";

	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	@Override
	public List<VariableAnnualPercentage> findByStatus(final int variableStatus) {

		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualPercentage as v where v.variableStatus="
						+ variableStatus);
		List<VariableAnnualPercentage> variableAnnualPercentageList = query
				.list();
		List<VariableAnnualPercentage> list = null;
		try {
			list = (List) getHibernateTemplate().execute(
					new HibernateCallback() {
						@Override
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							String extraQuery = null;
							if (variableStatus == 2) {
								extraQuery = "select v from VariableAnnualPercentage as v";
							} else {
								extraQuery = "select v from VariableAnnualPercentage as v where v.variableStatus="
										+ variableStatus
										+ " order by v.annualPercentId";

							}
							Query query = session.createQuery(extraQuery);
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
	public void saveVariable(VariableAnnualPercentage variableAnnualPercentage) {

		// VariableAnnualPercentage variables = variableAnnualPercentage;
		// Query query = getSessionFactory().getCurrentSession().createQuery(
		// "insert into VariableAnnualPercentage (ANNUAL_PERCENT_ID,SERVICE_ID,SUBSERVICE_ID,BHP_METHOD,VARIABLE_STATUS,VALID_FROM,BASE_ON_NOTE,TOTAL_YEAR,CREATED_ON,CREATED_BY,UPDATED_ON,UPDATED_BY) values ("
		// + variables.getServiceId()+""++"");
	}

	@Override
	public List<VariableAnnualPercentageDetail> findByAnnualPercentId(
			Long annualPercentId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualPercentageDetail as v where v.annualPercentId="
						+ annualPercentId);
		List<VariableAnnualPercentageDetail> variableAnnualPercentageDetailList = query
				.list();
		return variableAnnualPercentageDetailList;
	}

	@Override
	public VariableAnnualPercentage findByAnnPercentId(Long annualPercentId) {

		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualPercentage as v where v.annualPercentId="
						+ annualPercentId);
		Object o = query.uniqueResult();

		return (VariableAnnualPercentage) o;
	}

	@Override
	public void replaceToInactive(int variableStatus) {

		Query query = getSessionFactory().getCurrentSession().createQuery(
				"update  VariableAnnualPercentage  set variableStatus="
						+ variableStatus + " where variableStatus=1");
		query.executeUpdate();
	}

	@Override
	public void replaceToActive(int variableStatus, Long annualPercentId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"update  VariableAnnualPercentage  set variableStatus="
						+ variableStatus + " where variableStatus=0 and annualPercentId="+annualPercentId);
		query.executeUpdate();		
	}

	@Override
	public VariableAnnualPercentage findByYear(Integer year) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualPercentage as v where v.percentYear="
						+ year);
		Object o = query.uniqueResult();

		return (VariableAnnualPercentage) o;
	}

	@Override
	public VariableAnnualPercentage findByKmNo(String kmNo) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select v from VariableAnnualPercentage as v where v.kmNo= '"
						+ kmNo + "'");
		VariableAnnualPercentage result = (VariableAnnualPercentage) query.uniqueResult();

		return result;
	}

}
