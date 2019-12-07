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
				+ likeClauseGenerator(invoice);
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
			
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
	
	private String likeClauseGenerator(String invoiceNo){
		
		String sequence, month, year;
		
		if(invoiceNo.length() == 10){
			sequence   = invoiceNo.substring(0, 4);
			month      = invoiceNo.substring(4, 6);
			year       = invoiceNo.substring(invoiceNo.length()-4);
		}else{
			sequence   = invoiceNo.substring(0, 5);
			month      = invoiceNo.substring(5, 7);
			year       = invoiceNo.substring(invoiceNo.length()-4);
		}

		String romanMonth = "";
		if(month.equals("01")) {
			romanMonth = "I";
		}else if(month.equals("02")) {
			romanMonth = "II";
		}else if(month.equals("03")) {
			romanMonth = "III";
		}else if(month.equals("04")) {
			romanMonth = "IV";
		}else if(month.equals("05")) {
			romanMonth = "V";
		}else if(month.equals("06")) {
			romanMonth = "VI";
		}else if(month.equals("07")) {
			romanMonth = "VII";
		}else if(month.equals("08")) {
			romanMonth = "VIII";
		}else if(month.equals("09")) {
			romanMonth = "IX";
		}else if(month.equals("10")) {
			romanMonth = "X";
		}else if(month.equals("11")) {
			romanMonth = "XI";
		}else if(month.equals("12")) {
			romanMonth = "XII";
		}
		
		String like1 = "SP2_NO LIKE '%" + sequence + "/SP2.KAL/BBPPT";
		String like2 = "SP2_NO LIKE '%/" + romanMonth + "/" + year;
				
		return like1 + " AND " + like2;
    }
}
