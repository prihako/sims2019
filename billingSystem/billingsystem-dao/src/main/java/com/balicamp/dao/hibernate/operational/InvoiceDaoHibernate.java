/**
 *
 */
package com.balicamp.dao.hibernate.operational;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.operational.InvoiceDaoObsolete;
import com.balicamp.exception.ApplicationException;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.Invoices;

/**
 * @author hyusprasetya
 *
 */

@Repository
public class InvoiceDaoHibernate extends AdminGenericDaoImpl<Invoices, Long>
	implements InvoiceDaoObsolete{

	public InvoiceDaoHibernate() {
		super(Invoices.class);
		// TODO Auto-generated constructor stub
	}

	protected final Log daoLog = LogFactory.getLog("LOGIC_LOG");

	private final String FIND_INVOICE_BASE_QUERY =
		"SELECT ROWNUM, LICENCE_NO, LICENCE_ID, YEAR_TO, "
		+ "LICENCE_DATE, LICENCE_BEGIN, LICENCE_END, "
		+ "TRANSMIT_MIN, TRANSMIT_MAX, RECEIVE_MIN, RECEIVE_MAX, "
		+ "TOTAL_INV_AMOUNT, BHP_PAYMENT_TYPE, INVOICE_NO, DESCRIPTION "
		+ "FROM ( "
		+ "SELECT i.LICENCE_NO, i.LICENCE_ID, i.YEAR_TO, "
		+ "l.LICENCE_DATE, l.LICENCE_BEGIN, l.LICENCE_END, "
		+ "l.TRANSMIT_MIN, l.TRANSMIT_MAX, l.RECEIVE_MIN, l.RECEIVE_MAX, "
		+ "i.TOTAL_INV_AMOUNT, l.BHP_PAYMENT_TYPE, i.INVOICE_NO, p.DESCRIPTION "
		+ "FROM MM_LICENCES l, OP_INVOICES i, S_PARAMETER p "
		+ "WHERE l.LICENCE_ID = i.LICENCE_ID "
		+ "AND l.LICENCE_NO = i.LICENCE_NO "
		+ "AND l.YEAR_TO = i.YEAR_TO "
		+ "AND i.INVOICE_STATUS = p.PARAM_VALUE "
		+ "AND p.PARAM_GROUP = 'invoicestatus' "
		+ "AND i.INVOICE_NO IS NOT NULL ";

	@SuppressWarnings("unchecked")
	@Override
	public List findGeneratedInvoiceByLicense(final String licenseNo, final String licenseId, final String yearTo) {
		List invoiceList = null;

		try {
			invoiceList = (List) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String queryString = FIND_INVOICE_BASE_QUERY;

					queryString += "AND i.LICENCE_ID like '%"+licenseId+"%' ";
					queryString += "AND i.LICENCE_NO like '%"+licenseNo+"%' ";
					queryString += "AND i.YEAR_TO = '"+yearTo+"' ";

					queryString += "ORDER BY i.LICENCE_ID asc, i.YEAR_TO desc )";

					Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
					return query.list();
				}
			});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}

		return invoiceList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List findGeneratedInvoice(final String serviceId,
			final String subServiceId, final String metodeBhp, final String clientName,
			final String licenseNo, final String invoiceNo) {
		List invoiceList = null;

		try {
			invoiceList = (List) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String queryString = FIND_INVOICE_BASE_QUERY;

					queryString += "AND l.SERVICE_ID = '"+serviceId+"' ";
					queryString += "AND l.SUBSERVICE_ID = '"+subServiceId+"' ";
					queryString += "AND l.BHP_METHOD = '"+metodeBhp+"' ";

					if(clientName!=null){
						queryString += "AND l.CLIENT_NAME like '%"+clientName+"%' ";
					}

					if(licenseNo!=null){
						queryString += "AND i.LICENCE_NO like '%"+licenseNo+"%' ";
					}

					if(invoiceNo!=null){
						queryString += "AND i.INVOICE_NO like '%"+invoiceNo+"%' ";
					}

					queryString += "ORDER BY i.LICENCE_ID asc, i.YEAR_TO desc )";

					Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
					return query.list();
				}
			});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}

		return invoiceList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String generateInvoiceNumber(final BigDecimal invoiceAmount, final Licenses license, final String user) {
		String invoiceNumber 	= null;
		List invoiceNumberList 	= null;
		try {
			invoiceNumberList = (List) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String queryString = "SELECT GET_INVOICE_FEE_TEST(" + invoiceAmount + ","
							+ "'" + license.getLicenceId() +"','"+
							new SimpleDateFormat("dd-MMM-yyyy").format((license.getPaymentDueDate())) +"','"+ user +"') FROM DUAL";
					Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
					return query.list();
				}
			});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}

		if(invoiceNumberList!=null && invoiceNumberList.size()>0){
			invoiceNumber = invoiceNumberList.get(0).toString();
		}else{
			invoiceNumber = "";
		}

		return invoiceNumber;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Invoices getDetailGeneratedInvoice(final String licenseNo, final String licenseId,
			final String invoiceNo, final String invoiceYear) {
		List<Invoices> invoiceList = null;

		try {
			invoiceList = (List<Invoices>) getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String queryString = "from Invoices where ";

					queryString += "licenceNo = '"+licenseNo+"' and ";
					queryString += "licenceId = '"+licenseId+"' and ";
					queryString += "invoiceNo = '"+invoiceNo+"' and ";
					queryString += "yearTo = '"+invoiceYear+"' ";

					Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
					return query.list();
				}
			});
		} catch (DataAccessException ex) {
			daoLog.error(ex);
			ex.printStackTrace();
			throw new ApplicationException(ex.getMessage());
		}
		return invoiceList.get(0);
	}

	@Override
	public void createFirstYearInvoice(Licenses license, String user) {
		Query query = getSession().createSQLQuery("call SP_CREATE_INVOICE_FEE('"+license.getLicenceId()+"','"+license.getYearTo()+"','"+user+"')");
		query.executeUpdate();
	}

}
