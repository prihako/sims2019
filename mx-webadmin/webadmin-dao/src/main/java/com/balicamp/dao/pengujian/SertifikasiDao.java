package com.balicamp.dao.pengujian;

import java.util.Date;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

public interface SertifikasiDao extends GenericDao<BaseAdminModel, String> {
	public boolean updateInvoiceEodSertifikasi(ReconcileDto reconcile, Date paymentDate, String remarks);

	public Object[] findBillingByInvoiceAndDate(String invoiceNo, Date trxDate, Object[] mt940Map);	
	
}
