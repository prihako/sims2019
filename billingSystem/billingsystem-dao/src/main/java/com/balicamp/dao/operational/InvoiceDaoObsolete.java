/**
 *
 */
package com.balicamp.dao.operational;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.Invoices;

/**
 * @author hyusprasetya
 *
 */
public interface InvoiceDaoObsolete extends AdminGenericDao<Invoices, Long>{

	List findGeneratedInvoice(String serviceId,
			String subServiceId, String metodeBhp, String clientName,
			String licenseNo, String invoiceNo);
	List findGeneratedInvoiceByLicense(String licenseNo, String licenseId, String yearTo);
	String generateInvoiceNumber(BigDecimal invoiceAmount, Licenses license, String user);
	Invoices getDetailGeneratedInvoice(String licenseNo, String licenseId,
			String invoiceNo, String invoiceYear);
	void createFirstYearInvoice(Licenses license, String user);
}
