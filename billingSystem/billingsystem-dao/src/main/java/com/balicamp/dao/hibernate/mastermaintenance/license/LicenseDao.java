package com.balicamp.dao.hibernate.mastermaintenance.license;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.license.Licenses;



/**
 * DAO for License Entity
 *
 * @author <a href="mailto:christian.soewoeh@sigma.co.id">Christian Suwuh</a>
 *
 */
public interface LicenseDao extends AdminGenericDao<Licenses, Long>{

	List<Licenses> findAllLicense(String bhpMethod, BigDecimal clientID, String licenceId, String licenceNo);
	void deleteLicenseById(Long bsLicenceId, String licenceId);
	Licenses findLicenseById(Long bsLicenceId, String licenceId);
	List<Licenses> findLicenseByNo(String licenceNo);

//-------------------------------------------------------------------------------------------------------------------------------------------hendy
	List<Licenses> findListByLicenseNo(String licenceNo);
	Integer findBhpAnnualRate(Integer year);
	String findLicenseYear(String licenseId);
	List<Licenses> findLicenseForCreateInvoice(String licenseId, String yearTo);
	List findLicenseIdList(String serviceId,
			String subServiceId, String metodeBhp, String clientName,
			String skmNumber, String licenseId);
	Licenses getDetailLicense(String licenseNumber, String licenceId, String licenseYear);

	public List<Licenses> findLicense(Licenses license);

	List<Licenses> searchLicense(final BigDecimal serviceID,
			final BigDecimal subServiceId, final String methodBhp, final String clientName,
			 final String licenceNo);
	List<Licenses> findLicenseById(String licenceId);
	void deleteByLicenseId(String licenceId);
}
