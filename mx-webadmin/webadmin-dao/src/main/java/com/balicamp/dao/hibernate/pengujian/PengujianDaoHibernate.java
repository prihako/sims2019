package com.balicamp.dao.hibernate.pengujian;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.pengujian.PengujianDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

@Repository @Transactional
public class PengujianDaoHibernate extends
		PengujianGenericDaoHibernate<BaseAdminModel, String> implements
		PengujianDao {
	
	public PengujianDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate,
			Object[] mt940Data) {
		// TODO Auto-generated method stub
			String sql = "select "
					+ "cast(TicketID AS varchar), "
					+ "cast(StateActivityId AS varchar), "
					+ "cast(NoInvoice AS varchar), "
					+ "cast(TanggalBayar AS varchar), "
					+ "cast(TotalBiaya AS varchar), "
					+ "cast(Bank AS varchar), "
					+ "cast(PerusahaanName AS varchar), "
					+ "cast(NoPermohonan AS varchar), "
					+ "cast(Month AS varchar), "
					+ "cast(Year AS varchar), "
					+ "cast(TanggalPendaftaranPengujian AS varchar) "
					+ "from sip2telv3.dbo.siptel_Ticket where cast(TicketID AS varchar) = cast(:ticketID AS varchar)";
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if(invoice.length() > 6) {
			query.setParameter("ticketID", invoice.substring(0,invoice.length()-6));
		}else {
			query.setParameter("ticketID", invoice);
		}
			
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
	public boolean updateInvoiceEodPengujian(ReconcileDto reconcile,
			Date paymentDate, String remarks) {
		// TODO Auto-generated method stub
		String queryString = "update sip2telv3.dbo.siptel_Ticket"
				+ "set "
				+ "TanggalBayar = :trxDate, "
				+ "Bank = :bankName "
				+ "where TicketID = :ticketID";
		
		String pattern 			= "yyyy-MM-dd HH:mm:ss";
		String reconcileDate	= null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		
		reconcileDate = format.format(paymentDate);
		
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter("trxDate", reconcileDate);
		query.setParameter("bankName", reconcile.getBankName());
		query.setParameter("invoiceId", reconcile.getInvoiceNo().substring(0,reconcile.getInvoiceNo().length()-6));
		

		if (query.executeUpdate() > 0) {
			if(insertInvoiceEodPengujian(reconcile, paymentDate, remarks) == true){
				return true;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean insertInvoiceEodPengujian(ReconcileDto reconcile, Date paymentDate, String remarks) {
		String sql = "select "
				+ "TicketID, "
				+ "StateActivityId, "
				+ "NoInvoice, "
				+ "TanggalBayar, "
				+ "TotalBiaya, "
				+ "Bank, "
				+ "PerusahaanName, "
				+ "NoPermohonan, "
				+ "Month, "
				+ "Year, "
				+ "TanggalPendaftaranPengujian "
				+ "from sip2telv3.dbo.siptel_Ticket where TicketID = :ticketID";
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("ticketID", reconcile.getInvoiceNo().substring(0,reconcile.getInvoiceNo().length()-6));
			
		Object obj = new Object();
		Object[] objectArray = null;
		
		List<Object> result = query.list();
		if (result.size()>0) {
			obj = result.get(0);
			objectArray = (Object[]) obj;
		}

		String sql2 = "INSERT INTO sip2telv3.dbo.siptel_TicketBayar( "
				+ "TicketID, "
				+ "StateActivityId, "
				+ "NoInvoice, "
				+ "TanggalBayar, "
				+ "TotalBiaya, "
				+ "Bank, "
				+ "PerusahaanName, "
				+ "NoPermohonan, "
				+ "Month, "
				+ "Year, "
				+ "TanggalPendaftaranPengujian" 
				+ ") values (" 
					+ ":ticketID, "
					+ ":stateActivityId, "
					+ ":noInvoice, "
					+ ":tanggalBayar, "
					+ ":totalBiaya, "
					+ ":bank, "
					+ ":perusahaanName, "
					+ ":noPermohonan, "
					+ ":month, "
					+ ":year, "
					+ ":tanggalPendaftaranPengujian"
					+ ")";
		
		Query query2 = getSession().createSQLQuery(sql2);
		query2.setParameter("ticketID", objectArray[0]);
		query2.setParameter("stateActivityId", objectArray[1]);
		query2.setParameter("noInvoice", objectArray[2]);
		query2.setParameter("tanggalBayar", objectArray[3]);
		query2.setParameter("totalBiaya", objectArray[4]);
		query2.setParameter("bank", objectArray[5]);
		query2.setParameter("perusahaanName", objectArray[6]);
		query2.setParameter("noPermohonan", objectArray[7]);
		query2.setParameter("month", objectArray[8]);
		query2.setParameter("year", objectArray[9]);
		query2.setParameter("tanggalPendaftaranPengujian", objectArray[10]);
		
		if (query2.executeUpdate() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		String test = "12375092018";
		System.out.println(test.substring(0,test.length()-6));
	}
}
