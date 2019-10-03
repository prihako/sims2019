package com.balicamp.dao.ebs;

import java.util.Date;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.admin.BaseAdminModel;

public interface ExternalBillingSystemDao extends GenericDao<BaseAdminModel, String> {

	public Object findInvoiceByInvoiceNo(String invoiceNo);
	
	public boolean updateInvoicePaid(String invoiceNo, Date paymentDate, String remarks, String username);
	
	public boolean updateInvoiceUnpaid(String invoiceNo);

	public boolean updateInvoiceEod(String invoiceNo, Date paymentDate, String remarks);

	
}
