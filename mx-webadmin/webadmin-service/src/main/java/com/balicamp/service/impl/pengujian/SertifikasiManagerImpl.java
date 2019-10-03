package com.balicamp.service.impl.pengujian;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.pengujian.SertifikasiDao;
import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.pengujian.SertifikasiManager;

@Service("sertifikasiManagerImpl")
public class SertifikasiManagerImpl extends AbstractManager implements SertifikasiManager {

	private static final long serialVersionUID = -710098086253876468L;

	@Autowired
	private SertifikasiDao sertifikasiDao;

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateInvoiceEodSertifikasi(ReconcileDto reconcile,
			Date paymentDate, String remarks) {
		// TODO Auto-generated method stub
		return sertifikasiDao.updateInvoiceEodSertifikasi(reconcile, paymentDate, remarks);
	}


}
