package com.balicamp.service.mastermaintenance.license;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mastermaintenance.license.LicenseDisplay;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.License;
import com.balicamp.service.IManager;

public interface LicenseManager extends IManager {

	Licenses findByLicenseId(Long bsLicenceId, String licenceId);
	List<Licenses> findLicenceByNo(String licenceNo);
	List<LicenseDisplay> findLicense(SearchCriteria searchCriteria,
			int firstResult, int maxResult);
	void saveLicense(String service, String subService, String bhpMethod, String clientName, String licenceId,
			String licenceNo, String licenceDate, String licenceBegin,
			String licenceEnd);
	void save(Licenses license);
	void delete(Long bsLicenceId, String licenceId);

//-----------------------------------------------------------------------------------------------------------------------------------------hendy
	Integer findBhpAnnualRate(Integer year);
	List<Licenses> findListByLicenseNo(String licenceNo);
	String findLicenseYear(String licenseId);
	List<Licenses> findLicenseForCreateInvoice(String licenseId, String yearTo);
	List findLicenseIdList(String serviceId,
			String subServiceId, String metodeBhp, String clientName,
			String skmNumber, String licenseId);
	Licenses getDetailLicense(String licenseNumber, String licenceId, String licenseYear);
	List<Licenses> findLicense(Licenses license);
	List<Licenses> searchLicense(final BigDecimal serviceId,
			final BigDecimal subServiceId, final String methodBhp, final String clientName,
			 final String licenceNo);
	List<Licenses> findByLicenseId(String licenceId);
	void deleteByLicenseId(String licenceId);

//--------------------------------AFTER GAP ANALYSIS---------------------------------------------------------------------------------------hendy
	
	License searchLicenseById(String tLicenceId);
	
}
