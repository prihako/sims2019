package com.balicamp.service.impl.ebs;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.ebs.ExternalBillingSystemDao;
import com.balicamp.service.ebs.ExternalBillingSystemManager;
import com.balicamp.service.impl.AbstractManager;

@Service("externalBillingSystemManagerImpl")
public class ExternalBillingSystemManagerImpl extends AbstractManager implements ExternalBillingSystemManager {

	private static final long serialVersionUID = -710098086253876468L;

	@Autowired
	private ExternalBillingSystemDao externalBillingSystemDao;

	@Override
	public Object findInvoiceByInvoiceNo(String invoiceNo) {

		return externalBillingSystemDao.findInvoiceByInvoiceNo(invoiceNo);
	}

	@Override
	public Object getDefaultDao() {
		return externalBillingSystemDao;
	}

	@Override
	public boolean updateInvoicePaid(String invoiceNo, Date paymentDate, String remarks, String username) {
		// TODO Auto-generated method stub
		return externalBillingSystemDao.updateInvoicePaid(invoiceNo, paymentDate, remarks, username);
	}

	@Override
	public boolean updateInvoiceUnpaid(String invoiceNo) {
		// TODO Auto-generated method stub
		return externalBillingSystemDao.updateInvoiceUnpaid(invoiceNo);
	}

	@Override
	public boolean updateInvoiceEod(String invoiceNo, Date paymentDate, String remarks) {

		return externalBillingSystemDao.updateInvoiceEod(invoiceNo, paymentDate, remarks);
	}

}
