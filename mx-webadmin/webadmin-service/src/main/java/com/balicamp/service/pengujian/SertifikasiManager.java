package com.balicamp.service.pengujian;

import java.util.Date;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.IManager;

public interface SertifikasiManager extends IManager {
	public boolean updateInvoiceEodSertifikasi(ReconcileDto reconcile, Date paymentDate, String remarks);
	
}
