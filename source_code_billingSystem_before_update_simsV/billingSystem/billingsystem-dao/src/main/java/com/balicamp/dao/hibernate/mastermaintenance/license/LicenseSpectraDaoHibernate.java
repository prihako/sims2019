/**
 *
 */
package com.balicamp.dao.hibernate.mastermaintenance.license;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;

/**
 * @author <a href="mailto:christian.soewoeh@sigma.co.id">Christian Suwuh</a>
 *
 */
@Repository
public class LicenseSpectraDaoHibernate extends
		AdminGenericDaoImpl<LicenseSpectra, Long> implements LicenseSpectraDao {

	public LicenseSpectraDaoHibernate() {
		super(LicenseSpectra.class);
	}

	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	private SessionFactory hibernateSessionFactory;

	@Override
	@Autowired(required = true)
	public void setHibernateSessionFactory(
			@Qualifier("adminHibernateSessionFactory") SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
		super.setSessionFactory(hibernateSessionFactory);
	}

	@Override
	public SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}

	String client;
	String bhpMethod;
	String licenceId;
	String licenceNo;
	String serviceId;
	String subServiceId;

	// list is working as a database
	List<LicenseSpectra> spectraView;

	@Override
	public List<LicenseSpectra> findAllLicense(String addressCompany,
			String addressNumber, String licenceId, String licenceNo,
			String serviceId, String subServiceId) {
		// TODO Auto-generated method stub
		Query query = getSession().createQuery(
				"SELECT m FROM LicenseSpectra m where m.address_company='"
						+ addressCompany + "'or m.address_number='"
						+ addressNumber + "'or m.licence_number='" + licenceId
						+ "' or m.ap_ref_number='" + licenceNo
						+ "' or m.sv_id='" + serviceId + "' or m.ss_id='"
						+ subServiceId + "'");
		List<LicenseSpectra> licenseList = query.list();

		return licenseList;
	}

	@Override
	public LicenseSpectra findLicenseByClient(String addressNumber, String addressCompany,
			 String serviceId, String service, String subServiceId, String subService) {
		Query query = getSession().createQuery(
				"SELECT m FROM LicenseSpectra m where (m.address_number='"
						+ addressNumber + "' or m.address_company='"
						+ addressCompany + "') and (m.sv_id='" + serviceId + "' or m.sv='"
						+ service + "') and (m.ss_id='" + subServiceId
						+ "' or m.ss='" + subService + "')");

		List<?> list = query.list();
		if ((list == null) || list.isEmpty()) {
			return null;
		}

		return (LicenseSpectra) list.get(0);
	}

	@Override
	public LicenseSpectra findLicenseByClientAndLicenceNumberID(
			String addressNumber, String addressCompany, String licenceId,
			String licenceNo, String serviceId, String service,
			String subServiceId, String subService) {
		Query query = getSession().createQuery(
				"SELECT m FROM LicenseSpectra m where (m.address_number='"
						+ addressNumber + "' or m.address_company='"
						+ addressCompany + "') and (m.licence_number='"
						+ licenceId + "' or m.ap_ref_number='" + licenceNo
						+ "') and (m.sv_id='" + serviceId + "' or m.sv='"
						+ service + "') and (m.ss_id='" + subServiceId
						+ "' or m.ss='" + subService + "')");

		List<?> list = query.list();
		if ((list == null) || list.isEmpty()) {
			return null;
		}

		return (LicenseSpectra) list.get(0);
	}
//-------------------------------------------------------------------------------------------------------------------------------------------hendy

	private static final String QUERY_LIST_LICENSE_FROM_SPECTRA = " from LicenseSpectra where ";

	@SuppressWarnings("unchecked")
	@Override
	public List<LicenseSpectra> searchLicenseForCreate(final String serviceName,
			final String subServiceName, final String clientName, final String licenseId,
			final String licenseNumber) {

		List<LicenseSpectra> list = null;
		String extraQuery = "";
		if(licenseNumber!=null && !licenseNumber.equals("")){
			extraQuery = "licenceNumber='"+licenseNumber+"'";
		}else{
			extraQuery = "sv like '%"+serviceName+"%' "
					+ "and ss like '%"+subServiceName+"%' "
					+ "and addressCompany like '%"+clientName+"%' ";
		}
		extraQuery 	+= " order by addressCompany, apRefNumber asc";

		Query query = getSessionFactory().getCurrentSession().createQuery(QUERY_LIST_LICENSE_FROM_SPECTRA+extraQuery);
		list = query.list();
		return list;
	}

	private static final String QUERY_LIST_LICENSE_FROM_SPECTRA_BY_NUMBER = " from LicenseSpectra where ";

	@SuppressWarnings("unchecked")
	@Override
	public List<LicenseSpectra> searchLicenseByNumber(final String licenseNumber) {

		List<LicenseSpectra> list = null;
		String extraQuery = "";
		if(licenseNumber!=null && !licenseNumber.equals("")){
			extraQuery = "apRefNumber='"+licenseNumber+"'";
		}
		extraQuery 	+= " order by addressCompany, apRefNumber asc";

		Query query = getSessionFactory().getCurrentSession().createQuery(QUERY_LIST_LICENSE_FROM_SPECTRA_BY_NUMBER+extraQuery);
		list = query.list();
		return list;
	}
}
