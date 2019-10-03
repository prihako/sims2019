package com.balicamp.service.pengujian;

import java.util.Date;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.IManager;

public interface PengujianManager extends IManager {
	public boolean updateInvoiceEodPengujian(ReconcileDto reconcile, Date paymentDate, String remarks);
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate, Object[] mt940Data);
}
