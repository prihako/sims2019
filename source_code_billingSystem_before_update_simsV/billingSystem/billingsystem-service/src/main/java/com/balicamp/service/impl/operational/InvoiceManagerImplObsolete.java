/**
 *
 */
package com.balicamp.service.impl.operational;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.operational.InvoiceDaoObsolete;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.Invoices;
import com.balicamp.service.operational.InvoiceManagerObsolete;

/**
 * @author hyusprasetya
 *
 */

@Service("invoiceManagerO")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class InvoiceManagerImplObsolete implements InvoiceManagerObsolete {

	/**
	 *
	 */

	private final InvoiceDaoObsolete invoiceDaoObsolete;

	@Autowired
	public InvoiceManagerImplObsolete(InvoiceDaoObsolete invoiceDaoObsolete) {
		this.invoiceDaoObsolete = invoiceDaoObsolete;
	}

	@Override
	public List findGeneratedInvoiceByLicense(String licenseNo,
			String licenseId, String yearTo) {
		List invoiceList = invoiceDaoObsolete.findGeneratedInvoiceByLicense(licenseNo, licenseId, yearTo);
		return invoiceList;
	}

	@Override
	public Serializable findById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> findById(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> findByCriteria(SearchCriteria searchCriteria,
			int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findSingleByCriteria(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateInvoiceNumber(BigDecimal invoiceAmount, Licenses license, String user) {
		String invoiceNumber = invoiceDaoObsolete.generateInvoiceNumber(invoiceAmount, license, user);
		return invoiceNumber;
	}

	@Override
	public void saveOrUpdateInvoices(Invoices invoices) {
		invoiceDaoObsolete.saveOrUpdate(invoices);
	}

	@Override
	public List findGeneratedInvoice(String serviceId, String subServiceId,
			String metodeBhp, String clientName, String licenseNo,
			String invoiceNo) {
		List invoiceList = invoiceDaoObsolete.findGeneratedInvoice(serviceId,
				subServiceId, metodeBhp, clientName, licenseNo, invoiceNo);
		return invoiceList;
	}

	@Override
	public Invoices getDetailGeneratedInvoice(String licenseNo,
			String licenseId, String invoiceNo, String invoiceYear) {
		Invoices invoice = invoiceDaoObsolete.getDetailGeneratedInvoice(licenseNo, licenseId, invoiceNo, invoiceYear);
		return invoice;
	}

	@Override
	public void createFirstYearInvoice(Licenses license, String user) {
		invoiceDaoObsolete.createFirstYearInvoice(license, user);
	}
}
