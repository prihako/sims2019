package com.balicamp.service.impl.pengujian;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.pengujian.PengujianDao;
import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.pengujian.PengujianManager;

@Service("pengujianManagerImpl")
public class PengujianManagerImpl extends AbstractManager implements PengujianManager {

	private static final long serialVersionUID = -710098086253876468L;

	@Autowired
	private PengujianDao pengujianDao;

	@Override
	public boolean updateInvoiceEodPengujian(ReconcileDto reconcile,
			Date paymentDate, String remarks) {
		// TODO Auto-generated method stub
		return pengujianDao.updateInvoiceEodPengujian(reconcile, paymentDate, remarks);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate,
			Object[] mt940Data) {
		// TODO Auto-generated method stub
		return pengujianDao.findBillingByInvoiceAndDate(invoice, trxDate, mt940Data);
	}

	
}
