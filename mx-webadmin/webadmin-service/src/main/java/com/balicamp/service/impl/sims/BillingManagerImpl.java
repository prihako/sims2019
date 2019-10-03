package com.balicamp.service.impl.sims;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.sims.BillingDao;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.sims.BillingManager;

@Service("billingManagerImpl")
public class BillingManagerImpl extends AbstractManager implements BillingManager {
	private static final long serialVersionUID = -3908230684618992868L;

	@Autowired
	private BillingDao billingDao;

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return billingDao;
	}

	@Override
	public Object findBillingByInvoiceNo(String invoiceNo) {
		Object result = billingDao.findBillingByInvoiceNo(invoiceNo);
		return result;
	}

	@Override
	public Object findBillingByInvoiceNo(String invoiceNo, String[] biTypes) {
		Object result = billingDao.findBillingByInvoiceNo(invoiceNo, biTypes);
		return result;
	}

	@Override
	public Map<String, Object[]> findBillingByInvoiceNo(Set<String> invoiceNo, String[] biTypes, String clientId) {
		Map<String, Object[]> result = billingDao.findBillingByInvoiceNo(invoiceNo, biTypes, clientId);
		return result;
	}

	@Override
	public Map<String, Object[]> findBillingByInvoiceNo(Set<String> invoiceNo, String clientId) {
		Map<String, Object[]> result = billingDao.findBillingByInvoiceNo(invoiceNo, clientId);
		return result;
	}

	@Override
	public Map<String, Object[]> findAllBillingPaidByInvoiceNoAndDate(Set<String> invoiceNo, Date trxDate, String[] biTypes) {
		Map<String, Object[]> result = billingDao.findAllBillingPaidByInvoiceNoAndDate(invoiceNo, trxDate, biTypes);
		return result;
	}

	@Override
	public Map<String, Object[]> findAllBillingPaidByInvoiceNoAndDateNotSettle(Set<String> invoiceNo, Date trxDate, String[] biTypes) {
		Map<String, Object[]> result = billingDao.findAllBillingPaidByInvoiceNoAndDateNotSettle(invoiceNo, trxDate, biTypes);
		return result;
	}

	@Override
	public Map<String, Object[]> findAllBillingUnpaidByInvoiceNoAndDate(Set<String> invoiceNo, Date trxDate, String[] biTypes) {
		Map<String, Object[]> result = billingDao.findAllBillingUnpaidByInvoiceNoAndDate(invoiceNo, trxDate, biTypes);
		return result;
	}

	@Override
	public void addRemarks(String invoiceNo, Date trxDate, String[] biTypes, String remarks) {
		billingDao.addRemarks(invoiceNo, trxDate, biTypes, remarks);
	}

	@Override
	public Map<String, Object[]> findAllBillingByInvoiceNoAndDate(Set<String> invoiceNo, Date trxDate) {
		Map<String, Object[]> result = billingDao.findAllBillingByInvoiceNoAndDate(invoiceNo, trxDate);
		return result;
	}

	@Override
	public Map<String, Object[]> findAllBillingDendaByInvoiceNo(Set<String> biId) {
		Map<String, Object[]> result = billingDao.findAllBillingDendaByInvoiceNo(biId);
		return result;
	}

	@Override
	public Map<String, Object[]> findAllBillingReconciledByInvoiceNo(Set<String> invoiceNo) {
		Map<String, Object[]> result = billingDao.findAllBillingReconciledByInvoiceNo(invoiceNo);
		return result;
	}

}
