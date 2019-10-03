package com.balicamp.service.operational;

import java.util.List;

import com.balicamp.model.operational.License;
import com.balicamp.service.IManager;

public interface LicenseManager extends IManager{
	List<Object> findInvoiceByClientName(String clientName, String invoiceStatus);
	List<Object> findInvoiceByClientID(String clientNo, String invoiceStatus);
	List<Object> findInvoiceByMethod(String bhpMethod, String invoiceStatus);
	List<Object> findInvoiceByLicenceNo(String licenceNo, String invoiceStatus);
	
	List<License> findByClientName (String clientName);
	List<License> findByClientNo (String clientNumber);
	List<License> findByMethod (String bhpMethod);
	List<License> findByLicenceNo (String licenceNo);
	
	License findLicenseByID(Object licenceID);


	void save(License license);


//--------------------------------AFTER GAP ANALYSIS---------------------------------------------------------------------------------------hendy

	License searchLicenseById(String tLicenceId);
	List<License> findByAnnualPercentId(Long annualPercentId);
	
	List<Object> findInvoiceDueDate();
}
