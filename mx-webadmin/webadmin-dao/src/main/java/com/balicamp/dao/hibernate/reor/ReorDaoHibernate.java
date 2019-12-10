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
		
		String sql = null; 
		
		String sql_1 = "select "
				+ "inv.invoice_number, "
				+ "lemdiks.id_lemdik client_id, "
				+ "inv.payment_date, "
				+ "inv.due_date, "
				+ "lemdiks.nama_lemdik, "
				+ "inv.amount "
				+ "from invoices inv, exams exams, lemdiks lemdiks where "
				+ "inv.id_exam = exams.id_exam "
				+ "and lemdiks.id_lemdik = lemdiks.id_lemdik "
				+ "and inv.invoice_number = :invoiceNo ";
		
		String sql_2 = "select "
				+"inv.invoice_number, "
				+"pay.id_payment, "
				+"inv.payment_date, "
				+"inv.due_date, "
				+"reg.nama_registrant, "
				+"inv.amount "
				+"from "
				+"invoices inv, "
				+"payments pay, "
				+"tbl_old_certificate reg  "
				+"where "
				+"inv.id_invoice = pay.id_invoice  "
				+"and pay.id_registrant = reg.id_registrant "
				+"and inv.invoice_number = :invoiceNo ";
		
		if(type.equals('1')) {
			sql = sql_1;
		}else {
			sql = sql_2;
		}
		
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
