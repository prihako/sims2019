package com.balicamp.dao.operational;

import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.operational.License;

public interface LicenseDAO extends AdminGenericDao<License, Long> {

	List<Object> findInvoiceByClientName(String clientName, String invoiceStatus);
	List<Object> findInvoiceByClientID(String clientNo, String invoiceStatus);
	List<Object> findInvoiceByMethod(String bhpMethod, String invoiceStatus);
	List<Object> findInvoiceByLicenceNo(String licenceNo, String invoiceStatus);
	
	License findLicenseByID(Object licenceID);


	List<License> findByClientName (String clientName);
	List<License> findByClientNo (String clientNumber);
	List<License> findByMethod (String bhpMethod);
	List<License> findByLicenceNo (String licenceNo);
//--------------------------------AFTER GAP ANALYSIS---------------------------------------------------------------------------------------hendy

	License searchLicenseById(String tLicenceId);
	List<License> findByAnnualPercentId(Long annualPercentId);
	
	List<Object> findInvoiceDueDate();
}
