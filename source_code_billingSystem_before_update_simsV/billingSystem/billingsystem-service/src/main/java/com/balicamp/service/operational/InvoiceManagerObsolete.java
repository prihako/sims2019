/**
 *
 */
package com.balicamp.service.operational;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.Invoices;
import com.balicamp.service.IManager;

/**
 * @author hyusprasetya
 *
 */
public interface InvoiceManagerObsolete extends IManager{

	List findGeneratedInvoice(String serviceId,
			String subServiceId, String metodeBhp, String clientName,
			String licenseNo, String invoiceNo);

	List findGeneratedInvoiceByLicense(String licenseNo, String licenseId, String yearTo);

	String generateInvoiceNumber(BigDecimal invoiceAmount, Licenses license, String user);

	void saveOrUpdateInvoices(Invoices invoices);

	Invoices getDetailGeneratedInvoice(String licenseNo, String licenseId,
			String invoiceNo, String invoiceYear);

	void createFirstYearInvoice(Licenses license, String user);
}
