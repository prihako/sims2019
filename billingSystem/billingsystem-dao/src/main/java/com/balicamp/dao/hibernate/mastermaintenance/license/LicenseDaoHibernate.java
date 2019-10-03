/**
 *
 */
package com.balicamp.dao.hibernate.mastermaintenance.license;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.exception.ApplicationException;
import com.balicamp.model.mastermaintenance.license.Licenses;

/**
 * @author <a href="mailto:christian.soewoeh@sigma.co.id">Christian Suwuh</a>
 *
 */
@Repository
public class LicenseDaoHibernate extends AdminGenericDaoImpl<Licenses, Long>
		implements LicenseDao {

	public LicenseDaoHibernate() {
		super(Licenses.class);
	}

//	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	private SessionFactory hibernateSessionFactory;
	String client;
	String bhpMethod;
	String licenceId;
	String licenceNo;

	// list is working as a database
	List<Licenses> licenses;

	@SuppressWarnings("unchecked")
	@Override
	public List<Licenses> findAllLicense(String bhpMethod, BigDecimal clientID,
			String licenceId, String licenceNo) {
		Query query = getSession().createQuery(
				"SELECT m FROM Licenses m where m.clientID='" + clientID
						+ "' and m.licenceId='" + licenceId
						+ "' and m.licenceNo='" + licenceNo
						+ "' and m.bhpMethod='" + bhpMethod + "'");
		List<Licenses> licenseList = query.list();
		return licenseList;
	}

	private final String QUERY_FIND_LICENSE_NUMBER = "From Licenses WHERE licenceNo = ? ORDER BY yearTo ASC";

	@Override
	public List<Licenses> findLicenseByNo(String licenceNo) {
		List<Licenses> licenseList = new ArrayList<Licenses>();
		Query qq = getSessionFactory().getCurrentSession().createQuery(
				QUERY_FIND_LICENSE_NUMBER);
		qq.setString(0, licenceNo);
		licenseList = qq.list();
		return licenseList;
	}

	private final String QUERY_FIND_LICENSE_BY_ID = "From Licenses WHERE licenceId = ? ORDER BY yearTo ASC";

	@Override
	public List<Licenses> findLicenseById(String licenceId) {
		List<Licenses> licenseList = new ArrayList<Licenses>();
		Query qq = getSessionFactory().getCurrentSession().createQuery(
				QUERY_FIND_LICENSE_BY_ID);
		qq.setString(0, licenceId);
		licenseList = qq.list();
		return licenseList;
	}

	@Override
	public String findLicenseYear(String licenseId) {
		String year = null;
		List licenseYear = new ArrayList();
		String query = " select max(YEAR_TO) from OP_INVOICES where LICENCE_ID='"+licenseId+"' ";
		Query qq = getSessionFactory().getCurrentSession().createSQLQuery(query);
		licenseYear = qq.list();
		if(licenseYear.get(0)!=null && !licenseYear.get(0).toString().equals("")){
			year = licenseYear.get(0).toString();
		}
		return year;
	}

	private static final String QUERY_LIST_LICENSE = " from Licenses where ";

	@SuppressWarnings("unchecked")
	@Override
	public List<Licenses> findLicenseForCreateInvoice(final String licenseId, final String yearTo) {

		List<Licenses> list = null;
		try {
			list = (List) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String extraQuery = "licenceId = '"+licenseId+"' "
							+ "and yearTo = '"+yearTo+"'";

					Query query = session.createQuery(QUERY_LIST_LICENSE+extraQuery);
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

	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	private final String QUERY_FIND_BHP_RATE = "SELECT percentage FROM MM_VAR_ANN_PERCENT_DTL WHERE year_to = ? ";

	@Override
	public Integer findBhpAnnualRate(Integer year) {
		Integer bhpRate = new Integer(0);
		Query qq = getSessionFactory().getCurrentSession().createSQLQuery(
				QUERY_FIND_BHP_RATE);
		qq.setInteger(0, year);
		bhpRate = Integer.parseInt(qq.list().get(0).toString());
		return bhpRate;
	}

	private final String QUERY_FIND_CREATED_LICENSE = "SELECT * FROM MM_LICENCES WHERE LICENCE_NO = ? ";

	@Override
	public List<Licenses> findListByLicenseNo(String licenceNo) {
		List<Licenses> licenseList = null;
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				QUERY_FIND_CREATED_LICENSE);
		query.setString(0, licenceNo);
		licenseList = query.list();
		return licenseList;
	}

	@Override
	public Licenses findLicenseById(Long bsLicenceId, String licenceId) {
		Query query = getSession().createQuery(
				"select m from Licenses as m where m.bsLicenceId="
						+ bsLicenceId + " and  m.licenceId = '" + licenceId
						+ "'");

		Object o = query.uniqueResult();

		return (Licenses) o;
	}

	@Override
	public Licenses getDetailLicense(String licenseNumber, String licenceId, String licenseYear) {
		String queryString = "from Licenses where ";

		queryString += "licenceNo = '"+licenseNumber+"' and ";
		queryString += "licenceId = '"+licenceId+"' and ";
		queryString += "yearTo = '"+licenseYear+"' ";

		Query query = getSession().createQuery(queryString);
		Object o 	= query.uniqueResult();

		return (Licenses) o;
	}

	@Override
	public void deleteLicenseById(Long bsLicenceId, String licenceId) {
		Query query = getSession().createQuery(
				"delete Licenses as m where m.bsLicenceId=" + bsLicenceId
						+ " and  m.licenceId = '" + licenceId + "'");
		query.executeUpdate();
	}

//	private static final String QUERY_LIST_LICENSE = " from Licenses where ";

	@SuppressWarnings("unchecked")
	@Override
	public List<Licenses> searchLicense(final BigDecimal serviceId,
			final BigDecimal subServiceId, final String methodBhp ,final String clientName,
			final String licenceNo) {
		List<Licenses> list = null;

		System.out.println("SERVICE_ID = "+serviceId);
		System.out.println("SUBSERVICE_ID = "+subServiceId);
		System.out.println("BHP_METHOD = "+methodBhp);
		System.out.println("CLIENT_NAME = "+clientName);
		System.out.println("LICENCE_NO = "+licenceNo);

		List<Licenses> licenseList = null;
//		Query query = getSessionFactory().getCurrentSession().createSQLQuery(QUERY_FIND_CREATED_LICENSE);
		String extraQuery = "";
		if(licenceNo!=null && !licenceNo.equals("")){
			extraQuery = "licenceNo='"+licenceNo+"'";
		}else{
			extraQuery = "serviceId="+serviceId
					+ " and subServiceId="+subServiceId
					+ " and bhpMethod='"+methodBhp+"'"
					+ " and clientName like '%"+clientName+"%'";
		}
		extraQuery 	+= "and (licenceStatus!=null and licenceStatus!='C') order by clientName, yearTo asc";
		Query query = getSessionFactory().getCurrentSession().createQuery(QUERY_LIST_LICENSE+extraQuery);

		licenseList = query.list();
		return licenseList;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Licenses> findLicense(final Licenses license) {
		List<Licenses> list = null;
		try {
			list = (List) getHibernateTemplate().execute(
					new HibernateCallback() {
						@Override
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							String extraQuery =
									"clientName like '%" + license.getClientName()
									+ "%' " + " or licenceNo='"
									+ license.getLicenceNo() + "' order by licenceNo";
							Query query = session
									.createQuery(QUERY_LIST_LICENSE
											+ extraQuery);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List findLicenseIdList(final String serviceId, final String subServiceId,
			final String metodeBhp, final String clientName, final String skmNumber,
			final String licenseId) {
		List list = null;
		try {
			list = (List) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String QUERY = "select distinct LICENCE_ID from MM_LICENCES where "
							+ "SERVICE_ID like '%"+serviceId+"%' "
							+ "and SUBSERVICE_ID like '%"+subServiceId+"%' "
							+ "and CLIENT_NAME like '%"+clientName+"%' "
							+ "and LICENCE_ID like '%"+licenseId+"%' "
							+ "and LICENCE_NO like '%"+skmNumber+"%' ";
					Query query = session.createSQLQuery(QUERY);
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
	public void deleteByLicenseId(String licenceId) {
		Query query = getSession().createQuery("delete Licenses as m where m.licenceId = '" + licenceId + "'");
		query.executeUpdate();
	}
}
