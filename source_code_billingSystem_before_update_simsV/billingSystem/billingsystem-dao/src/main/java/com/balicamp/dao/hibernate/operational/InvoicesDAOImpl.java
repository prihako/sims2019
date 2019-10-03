package com.balicamp.dao.hibernate.operational;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.operational.InvoicesDAO;
import com.balicamp.exception.ApplicationException;
import com.balicamp.model.operational.DocumentLetter;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;

@Repository
public class InvoicesDAOImpl extends AdminGenericDaoImpl<Invoice, Long>
		implements InvoicesDAO {
	
	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	private static final String 
					QUERY_LIST_INVOICE_FROM_EBS 		= " from Invoice where ";
	private static final String 
					QUERY_INNER_JOIN_LICENSE_INVOICE 	= " from License license, Invoice invoice where license.tLicenceId = invoice.License and ";
	private static final String QUERY_INVOICE_FROM_EBS 	= " from Invoice where ";

	private static final String NATIVE_QUERY 			= "select invoice_id, invoice_no, year_to, to_char(inv_begin_date, 'YYYY'), bhp_upfront_fee, bhp_annual, bhp_total, month_to, bhp_fine_current, bhp_fine_accumulate, payment_amount from t_invoice where ";
	private static final String 
					QUERY_VIEW_DOCUMENT_LETTER 			= " from DocumentLetter where ";

	private static final String PRINT_LETTER_QUERY 		= "select i.dcit_doc from spectra.doc_item i,spectra.letter le where le.DCIT_DCIT_ID = i.DCIT_ID and";

	private static final String INVOICE_DUE_DATE_QUERY 	= "select * from t_invoice where payment_due_date between "
															+ "(select trunc (sysdate, 'month') from dual) "
															+ "and (select last_day(sysdate) from dual) "
															+ "order by payment_due_date, t_licence_id";
	
	public InvoicesDAOImpl() {
		super(Invoice.class);
	}

	@Override
	public List<Invoice> findByInvoiceStatus(String invoiceStatus) {
		List<Invoice> list = null;

		String extraQuery = "invoiceStatus ='" + invoiceStatus
				+ "' order by yearTo, invoiceType, monthTo";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<Invoice> findByInvoiceNo(String invoiceNo) {
		List<Invoice> list = null;

		String extraQuery = "invoiceNo ='" + invoiceNo
				+ "' order by yearTo, invoiceType, monthTo";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<Invoice> findByLicenceNo(String licenceNo) {
		List<Invoice> list = null;

		String extraQuery = "licenceNo ='" + licenceNo
				+ "' order by yearTo, invoiceType, monthTo";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public Invoice searchByLicenseNoAndYear(String licenceNo, String yearTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invoice> findInvoiceByClientName(String clientName,
			String invoiceStatus) {
		List<Invoice> list = null;

		String extraQuery = " invoiceStatus ='" + invoiceStatus
				+ "' order by yearTo, invoiceType, monthTo";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<Invoice> findByLicenceID(Long licenceID) {
		List<Invoice> list = null;

		String extraQuery = " t_licence_id ='" + licenceID + "'";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<Invoice> findInvoiceByLicenceID(Long licenceID) {
		// TODO Auto-generated method stub
		List<Invoice> list = null;

		String extraQuery = "License ='" + licenceID
				+ "' order by yearTo, invoiceType, monthTo, invoiceId";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_INVOICE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public Object findInvoiceByInvoiceNo(Long licenceID, Long invoiceID,
			String invoiceNo, int yearTo) {
		String extraQuery = null;

		if (invoiceNo == null) {
			extraQuery = "t_licence_id ='" + licenceID + "' and invoice_id='"
					+ invoiceID + "' and year_to='" + yearTo + "'";

		} else {
			extraQuery = "t_licence_id ='" + licenceID + "' and invoice_no='"
					+ invoiceNo + "' and year_to='" + yearTo + "'";

		}
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery);
		Object invoice = query.uniqueResult();
		return invoice;
	}

	@Override
	public Invoice findInvoiceByID(Long invoiceID) {

		String extraQuery = "invoiceId ='" + invoiceID + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_INVOICE_FROM_EBS + extraQuery);
		Invoice invoice = (Invoice) query.uniqueResult();
		return invoice;
	}

	@Override
	public List<Invoice> findInvoiceListByID(Long invoiceID) {
		List<Invoice> list = null;

		String extraQuery = "invoiceId ='" + invoiceID + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_INVOICE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public String generateInvoiceNumber(String licenceNo) {

		System.out.println("LICENCE NUMBER = " + licenceNo);

		// TODO Auto-generated method stub
		Connection con = getSessionFactory().getCurrentSession().connection();

		Date date = new Date();
		CallableStatement cstmt = null;
		String invoiceNo = null;
		String invoiceStatus = null;
		String invoiceIsError = null;
		String invoiceErrorCode = null;
		Long invoiceLetterID = null;

		try {

			cstmt = con
					.prepareCall("{call SP_GET_INVOICE_SPECTRA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			cstmt.setString(1, "FEE");
			cstmt.setString(2, licenceNo);
			cstmt.setString(3, "");
			cstmt.setInt(4, 0);
			cstmt.setDate(5, new java.sql.Date(date.getTime()));
			cstmt.setString(6, "");
			cstmt.setString(7, "");
			cstmt.setString(8, null);
			cstmt.setString(9, "");
			cstmt.registerOutParameter(10, Types.VARCHAR);
			cstmt.registerOutParameter(11, Types.VARCHAR);
			cstmt.registerOutParameter(12, Types.VARCHAR);
			cstmt.registerOutParameter(13, Types.VARCHAR);
			cstmt.registerOutParameter(14, Types.VARCHAR);

			cstmt.executeUpdate();

			invoiceNo = cstmt.getString(10);
			invoiceStatus = cstmt.getString(11);
			invoiceIsError = cstmt.getString(12);
			invoiceErrorCode = cstmt.getString(13);
			invoiceLetterID = cstmt.getLong(14);

			System.out.println("Invoice No = " + invoiceNo);
			System.out.println("Invoice Status = " + invoiceStatus);
			System.out.println("Invoice is Error = " + invoiceIsError);
			System.out.println("Invoice Error Code = " + invoiceErrorCode);
			System.out.println("Invoice Letter ID = " + invoiceLetterID);

			cstmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return invoiceNo;
	}

	@Override
	public Invoice searchByYearTo(Long licenseId, BigDecimal yearTo) {
		String criteria = " from Invoice i where i.License = " + licenseId
				+ " and i.yearTo = " + yearTo;
		Query query = getSessionFactory().getCurrentSession().createQuery(
				criteria);
		Invoice invoice = (Invoice) query.uniqueResult();
		return invoice;
	}

	@Override
	public Map generateInvoice(String licenceNo, BigDecimal bhpTotal, Date paymentDueDate) {

		System.out.println("LICENCE NUMBER = " + licenceNo);
		System.out.println("BHP TOTAL = " + bhpTotal);
		System.out.println("Payment Due Date " + paymentDueDate);

		// TODO Auto-generated method stub
		Connection con = getSessionFactory().getCurrentSession().connection();

		Date date = new Date();
		CallableStatement cstmt = null;
		String invoiceNo = null;
		String invoiceStatus = null;
		String invoiceIsError = null;
		String invoiceErrorCode = null;
		Long invoiceLetterID = null;

		try {

			cstmt = con
					.prepareCall("{call SP_GET_INVOICE_SPECTRA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			cstmt.setString(1, "FEE");
			cstmt.setString(2, licenceNo);
			cstmt.setString(3, "");
			cstmt.setLong(4, bhpTotal.longValue());// Nilai BHP total
			cstmt.setDate(5, new java.sql.Date(date.getTime()));
			cstmt.setDate(6, new java.sql.Date(paymentDueDate.getTime()));
			cstmt.setString(7, "0");
			cstmt.setString(8, "Y");
			cstmt.setString(9, "");
			cstmt.setString(10, "");
			cstmt.registerOutParameter(11, Types.VARCHAR);
			cstmt.registerOutParameter(12, Types.VARCHAR);
			cstmt.registerOutParameter(13, Types.VARCHAR);
			cstmt.registerOutParameter(14, Types.VARCHAR);
			cstmt.registerOutParameter(15, Types.VARCHAR);

			cstmt.executeUpdate();

			invoiceNo = cstmt.getString(11);
			invoiceStatus = cstmt.getString(12);
			invoiceIsError = cstmt.getString(13);
			invoiceErrorCode = cstmt.getString(14);
			invoiceLetterID = cstmt.getLong(15);

			System.out.println("Invoice No = " + invoiceNo);
			System.out.println("Invoice Status = " + invoiceStatus);
			System.out.println("Invoice is Error = " + invoiceIsError);
			System.out.println("Invoice Error Code = " + invoiceErrorCode);
			System.out.println("Invoice Letter ID = " + invoiceLetterID);

			cstmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("letterID", invoiceLetterID);
		map.put("invoiceNo", invoiceNo);
		map.put("invoiceStatus", invoiceStatus);
		map.put("invoiceIsError", invoiceIsError);
		map.put("invoiceErrorCode", invoiceErrorCode);

		return map;
	}

	@Override
	public List<Invoice> getInvoiceByType(Long licenseId, String invoiceType) {
		String criteria = " from Invoice i where i.License = " + licenseId
				+ " and i.invoiceType = " + invoiceType + " order by i.monthTo";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				criteria);
		List<Invoice> invoices = query.list();
		return invoices;
	}

	@Override
	public Invoice getInvoicePokok(Long licenseId, String invoiceType,
			BigDecimal yearTo) {
		String criteria = " from Invoice i where i.License = " + licenseId
				+ " and i.invoiceType = " + invoiceType + " and i.yearTo = "
				+ yearTo;
		Query query = getSessionFactory().getCurrentSession().createQuery(
				criteria);
		Invoice invoice = (Invoice) query.uniqueResult();
		return invoice;
	}

	@Override
	public void doPrint() {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] printLetter(BigDecimal letterID) {
		// TODO Auto-generated method stub
		String getletter = " le.le_id=" + letterID;
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				PRINT_LETTER_QUERY + getletter);
		Object letterDocument = query.uniqueResult();

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(letterDocument);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] doc = b.toByteArray();

		System.out.println("LETTER BLOB = " + doc);
		return doc;
	}

	@Override
	public DocumentLetter printLetterDocument(BigDecimal letterID) {
		// TODO Auto-generated method stub
		String getletter = " letterId=" + letterID;
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_VIEW_DOCUMENT_LETTER + getletter);
		DocumentLetter letterDocument = (DocumentLetter) query.uniqueResult();

		System.out.println(" BLOB = " + letterDocument);

		return letterDocument;

	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cancelInvoice(final Invoice invoice, final String comment) {
		
		try {
			getHibernateTemplate().execute(new HibernateCallback(){

				@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					
					
					String queryString = "update spectra.billing set bi_type=12,bi_is_cancelled=1,bi_comment='"+comment+"'"
							+ " where bi_ref_no='" + invoice.getInvoiceNo() + "'";
					Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
					
					System.out.println("Query CANCEL INVOICE: "+query);
					
					query.executeUpdate();
					
					return null;
				}
				
			});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}
	}
	
	@Override
	public List<Invoice> findInvoiceDueDate() {
		List<Invoice> list = null;

		Query query = getSessionFactory().getCurrentSession().createQuery(INVOICE_DUE_DATE_QUERY);
		list = query.list();
		return list;
	}
}
