package com.balicamp.dao.hibernate.reor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.reor.ReorDao;
import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.mx.ReconcileDto;

@Repository
@Transactional
public class ReorDaoHibernate extends ReorGenericDaoHibernate<BaseAdminModel, String> implements ReorDao {

	public ReorDaoHibernate() {
		super(BaseAdminModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] findBillingByInvoiceAndDate(String invoice, Date trxDate,
			Object[] mt940Data) {
		Character type = invoice.charAt(0);
		
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
	public boolean updateInvoice(ReconcileDto reconcile,
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
		

		return true;
	}
}
