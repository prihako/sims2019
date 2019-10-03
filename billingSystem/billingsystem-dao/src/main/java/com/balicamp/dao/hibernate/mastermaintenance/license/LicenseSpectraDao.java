package com.balicamp.dao.hibernate.mastermaintenance.license;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;

public interface LicenseSpectraDao extends AdminGenericDao<LicenseSpectra, Long> {
	List<LicenseSpectra> findAllLicense(String addressCompany, String addressNumber,
			String licenceId, String licenceNo, String serviceId,
			String subServiceId);

	LicenseSpectra findLicenseByClient(String addressNumber, String addressCompany,
			 String serviceId, String service, String subServiceId, String subService);

	LicenseSpectra findLicenseByClientAndLicenceNumberID(String addressNumber,String addressCompany,
			String licenceId, String licenceNo, String serviceId, String service, String subServiceId, String subService);


//------------------------------------------------------------------------------------------------------------------------------------hendy
	List<LicenseSpectra> searchLicenseForCreate(final String serviceName,
			final String subServiceName, final String clientName,
			final String licenseId, final String licenseNumber);

	List<LicenseSpectra> searchLicenseByNumber(final String licenseNumber);
}