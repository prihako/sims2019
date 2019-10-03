package com.balicamp.service.mastermaintenance.license;

import java.util.List;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;
import com.balicamp.model.mastermaintenance.license.LicenseSpectraDisplay;
import com.balicamp.service.IManager;

public interface LicenseSpectraManager extends IManager {

	List<LicenseSpectraDisplay> findLicenses(SearchCriteria searchCriteria,
			int firstResult, int maxResult);


	List<LicenseSpectra> findAllLicense(String addressCompany, String addressNumber,
			String licenceId, String licenceNo, String serviceId,
			String subserviceId);

	LicenseSpectra findLicenseByClient(String addressNumber, String addressCompany,
			 String serviceId, String service, String subserviceId, String subservice);

	LicenseSpectra findLicenseByClientAndLicenceNumberID(String addressNumber,String addressCompany,
			String licenceId, String licenceNo, String serviceId, String service, String subserviceId, String subservice);

	//------------------------------------------------------------------------------------------------------------------------------------hendy

	List<LicenseSpectra> searchLicenseForCreate(final String serviceName,
			final String subServiceName, final String clientName,
			final String licenseId, final String licenseNumber);

	List<LicenseSpectra> searchLicenseByNumber(final String licenseNumber);

}
