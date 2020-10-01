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
		
		String sql = "select "
				+ "inv.invoice_number, "
				+ "inv.status, "
				+ "inv.due_date, "
				+ "inv.payment_date, "
				+ "lemdiks.id_lemdik, "
				+ "lemdiks.nama_lemdik, "
				+ "inv.amount "
				+ "from invoices inv, exams exams, lemdiks lemdiks "
				+ "where "
				+ "inv.id_exam = exams.id_exam "
				+ "and exams.id_lemdik = lemdiks.id_lemdik "
				+ "and inv.invoice_number = :invoiceNo "
				+ "and inv.payment_date is not null "
				+ "and inv.status = '1' ";

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setParameter("invoiceNo", invoice);
			
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
