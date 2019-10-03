package com.balicamp.service.ebs;

import java.util.Date;

import com.balicamp.service.IManager;

public interface ExternalBillingSystemManager extends IManager {

	public Object findInvoiceByInvoiceNo(String invoiceNo);

	public boolean updateInvoicePaid(String invoiceNo, Date paymentDate, String remarks, String username);

	public boolean updateInvoiceUnpaid(String invoiceNo);

	public boolean updateInvoiceEod(String invoiceNo, Date paymentDate, String remarks);

}
