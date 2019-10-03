package com.balicamp.dao.operational;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.operational.DocumentLetter;
import com.balicamp.model.operational.Invoice;

public interface InvoicesDAO extends AdminGenericDao<Invoice, Long> {

	List<Invoice> findByInvoiceStatus (String invoiceStatus);
	List<Invoice> findByInvoiceNo (String invoiceNo);
	List<Invoice> findByLicenceNo (String licenceNo);
	List<Invoice> findInvoiceByClientName(String clientName, String invoiceStatus);
	List<Invoice> findByLicenceID(Long licenceID);
	List<Invoice> findInvoiceByLicenceID(Long licenceID);

	Object findInvoiceByInvoiceNo(Long licenceID, Long invoiceID, String invoiceNo, int yearTo);
	Invoice findInvoiceByID(Long invoiceID);

	List<Invoice> findInvoiceListByID(Long invoiceID);
	
	String generateInvoiceNumber(String licenceNo);
	Map generateInvoice(String licenceNo, BigDecimal bhpTotal, Date paymentDueDate);
	
	byte[] printLetter(BigDecimal letterID);
	
	DocumentLetter printLetterDocument(BigDecimal letterID);
	
	void doPrint();


	Invoice searchByLicenseNoAndYear(String licenceNo, String yearTo);
	
	Invoice searchByYearTo(Long licenseId, BigDecimal yearTo);
	
	List<Invoice> getInvoiceByType(Long licenseId, String invoiceType);
	
	Invoice getInvoicePokok(Long licenseId, String invoiceType, BigDecimal yearTo);
	
	void cancelInvoice(Invoice invoiceNo, String comment);
	
	List<Invoice> findInvoiceDueDate();
	
	int updateNilaiBhpFrekuensi(Long nilaiBhpFrekuensi, Long invoiceId, String remarks);
}
