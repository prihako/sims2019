package com.balicamp.service.impl.operational;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.operational.InvoiceDaoHibernate;
import com.balicamp.dao.operational.InvoicesDAO;
import com.balicamp.model.operational.DocumentLetter;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.operational.InvoiceManager;

@Service("invoiceManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class InvoiceManagerImpl extends AbstractManager implements
		InvoiceManager {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final InvoicesDAO invoicesDAO;

	@Autowired
	public InvoiceManagerImpl(InvoicesDAO invoiceDAO) {
		this.invoicesDAO = invoiceDAO;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invoice> findByInvoiceStatus(String invoiceStatus) {
		// TODO Auto-generated method stub
		List<Invoice> list = invoicesDAO.findByInvoiceStatus(invoiceStatus);
		return list;
	}

	@Override
	public List<Invoice> findByInvoiceNo(String invoiceNo) {
		// TODO Auto-generated method stub
		List<Invoice> list = invoicesDAO.findByInvoiceNo(invoiceNo);
		return list;
	}

	@Override
	public List<Invoice> findByLicenceNo(String licenceNo) {
		// TODO Auto-generated method stub
		List<Invoice> list = invoicesDAO.findByLicenceNo(licenceNo);
		return list;
	}

	@Override
	public Invoice searchByLicenseNoAndYear(String licenceNo, String yearTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invoice> findInvoiceByClientName(String clientName,
			String invoiceStatus) {
		List<Invoice> list = invoicesDAO.findInvoiceByClientName(clientName,
				invoiceStatus);
		return list;
	}

	@Override
	public List<Invoice> findByLicenceID(Long licenceID) {
		List<Invoice> list = invoicesDAO.findByLicenceID(licenceID);
		return list;
	}

	@Override
	public Object findInvoiceByInvoiceNo(Long licenceID, Long invoiceID,
			String invoiceNo, int yearTo) {
		Object invoice = invoicesDAO.findInvoiceByInvoiceNo(licenceID,
				invoiceID, invoiceNo, yearTo);
		return invoice;
	}

	@Override
	public Invoice findInvoiceByID(Long invoiceID) {
		Invoice invoice = invoicesDAO.findInvoiceByID(invoiceID);
		return invoice;
	}

	@Override
	public List<Invoice> findInvoiceListByID(Long invoiceID) {
		List<Invoice> invoice = invoicesDAO.findInvoiceListByID(invoiceID);
		return invoice;
	}

	@Override
	public String generateInvoiceNumber(String licenceNo) {
		// TODO Auto-generated method stub

		String invoiceNO = invoicesDAO.generateInvoiceNumber(licenceNo);
		return invoiceNO;
	}

	// Special for case fine 24, cause fine 24 have 3 invoice(1 for
	// remainder, 1 for djkn, 1 for somasi)
	public List<Invoice> removeDuplicateInvoiceFine24(List<Invoice> list) {

		List<Integer> indexToRemoved = new ArrayList<Integer>();

		for (int i = 0; i < list.size(); i++) {
			Invoice row = (Invoice) list.get(i);

			if (i < (list.size() - 1)) {
				Invoice nextRow = (Invoice) list.get(i + 1);
				if ( row.getTLicence().getTLicenceId() == nextRow.getTLicence().getTLicenceId()) {
					if ((row.getMonthTo() != null) && (nextRow.getMonthTo() != null)) {
						if (row.getMonthTo().compareTo(new BigDecimal("24")) == 0 
								&& row.getMonthTo().compareTo(nextRow.getMonthTo()) == 0) {
							if (row.getInvoiceId() < nextRow.getInvoiceId()) {
								indexToRemoved.add(i);
							} else {
								indexToRemoved.add(i + 1);
							}
						}else if ((row.getInvoiceType().equals("4") || row.getInvoiceType().equals("6")) 
								&& row.getMonthTo().compareTo(new BigDecimal("12")) == 0 
								&& row.getMonthTo().compareTo(nextRow.getMonthTo()) == 0) {
							if (row.getInvoiceId() < nextRow.getInvoiceId()) {
								indexToRemoved.add(i);
							} else {
								indexToRemoved.add(i + 1);
							}
						}
					}
				}
			}
		}

		for (int i = indexToRemoved.size() - 1; i >= 0; i--) {
			Object row = list.get(indexToRemoved.get(i));
			list.remove(row);
		}

		return list;
	}

	@Override
	public void save(Invoice invoice) {
		// TODO Auto-generated method stub
		invoicesDAO.saveOrUpdate(invoice);

	}

	@Override
	public void saveList(List<Invoice> invoiceList) {
		// TODO Auto-generated method stub
		invoicesDAO.saveCollection(invoiceList);
	}

	@Override
	public List<Invoice> findInvoiceByLicenceID(Long licenceID) {
		// TODO Auto-generated method stub

		List<Invoice> invoiceList = invoicesDAO
				.findInvoiceByLicenceID(licenceID);
		return invoiceList;
	}

	@Override
	public Invoice searchByYearTo(Long licenseId, BigDecimal yearTo) {
		Invoice result = invoicesDAO.searchByYearTo(licenseId, yearTo);

		return result;
	}

	@Override
	public Map generateInvoice(String licenceNo, BigDecimal bhpTotal,
			Date paymentDueDate) {
		// TODO Auto-generated method stub

		Map invoiceMap = invoicesDAO.generateInvoice(licenceNo, bhpTotal,
				paymentDueDate);
		return invoiceMap;
	}

	@Override
	public List<Invoice> getInvoiceByType(Long licenseId, String invoiceType) {
		List<Invoice> invoiceList = invoicesDAO.getInvoiceByType(licenseId,
				invoiceType);
		return invoiceList;
	}

	@Override
	public Invoice getInvoicePokok(Long licenseId, String invoiceType,
			BigDecimal yearTo) {
		Invoice result = invoicesDAO.getInvoicePokok(licenseId, invoiceType,
				yearTo);
		return result;
	}

	@Override
	public byte[] printLetter(BigDecimal letterID) throws IOException {
		// TODO Auto-generated method stub
		byte[] letterDocument = invoicesDAO.printLetter(letterID);
		return letterDocument;
	}

	@Override
	public DocumentLetter printLetterDocument(BigDecimal letterID) {
		// TODO Auto-generated method stub
		DocumentLetter letterDocument = invoicesDAO
				.printLetterDocument(letterID);
		return letterDocument;
	}

	@Override
	public void cancelInvoice(Invoice invoiceNo, String comment) {
		invoicesDAO.cancelInvoice(invoiceNo, comment);
	}

	@Override
	public List<Invoice> findInvoiceDueDate() {
		return invoicesDAO.findInvoiceDueDate();
	}

}
