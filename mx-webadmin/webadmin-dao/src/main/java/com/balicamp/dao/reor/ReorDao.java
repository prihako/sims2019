package com.balicamp.dao.reor;

import java.util.Date;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

public interface ReorDao extends GenericDao<BaseAdminModel, String> {
	public boolean updateInvoice(ReconcileDto reconcile, Date paymentDate, String remarks);	
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate, Object[] mt940Data);
}
