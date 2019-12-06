package com.balicamp.dao.hibernate.kalibrasi;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.kalibrasi.KalibrasiDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

@Repository
@Transactional
public class KalibrasiDaoHibernate extends KalibrasiGenericDaoHibernate<BaseAdminModel, String> implements KalibrasiDao {

	public KalibrasiDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate,
			Object[] mt940Data) {
		// TODO Auto-generated method stub
		String sql = "select " 
				+ "SP2.SP2_NO, "
				+ "CUS.CUST_ID, " 
				+ "SP2.SP2_VALID DUE_DATE, "
				+ "SP2.SP2_DATE PAYMENT_DATE, "
				+ "CUS.CUST_NAME, "
				+ "BIAYA.BIAYA "
				+ "from "
				+ "T_SP2 SP2, "
				+ "T_APLIKASI APL, "
				+ "T_SP2_BIAYA BIAYA, "
				+ "T_CUSTOMER CUS "
				+ "WHERE SP2.APL_ID = APL.APL_ID "
				+ "AND SP2.SP2_ID = BIAYA.SP2_ID "
				+ "AND APL.CUST_ID = CUS.CUST_ID "
				+"where inv.invoice_number = :invoice "; 
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("invoice", invoice);
			
		Object obj = new Object();
		Object[] objectArray = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectArray = (Object[]) obj;
		}
		return objectArray;
	}

	@Override
	public boolean updateInvoice(ReconcileDto reconcile, Date paymentDate, String remarks) {
		return false;
	}
}
