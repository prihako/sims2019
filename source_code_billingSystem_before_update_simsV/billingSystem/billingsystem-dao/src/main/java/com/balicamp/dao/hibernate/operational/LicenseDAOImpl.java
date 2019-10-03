package com.balicamp.dao.hibernate.operational;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.operational.LicenseDAO;
import com.balicamp.model.mastermaintenance.license.UploadView;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;

@Repository
public class LicenseDAOImpl extends AdminGenericDaoImpl<License, Long>
		implements LicenseDAO {

	private static final String QUERY_LIST_LICENSE_FROM_EBS 		= " from License where ";
	private static final String QUERY_INVOICE_FROM_EBS 				= " from Invoice where ";

	private static final String QUERY_INNER_JOIN_LICENSE_INVOICE 	
		= "license.clientName, license.clientNo, license.bhpMethod, "
				+ "license.licenceNo, invoice.invoiceStatus "
				+ "from License license, Invoice invoice where license.tLicenceId = invoice.License.tLicenceId and ";
	private static final String QUERY
		= "select t_licence.t_licence_id, t_invoice.invoice_id, "
				+ "t_bank_guarantee.bg_id, t_licence.client_name, t_licence.client_id, "
				+ "t_licence.bhp_method, t_invoice.year_to, t_licence.licence_no, "
				+ "t_invoice.invoice_no, t_invoice.invoice_type, t_invoice.payment_due_date, "
				+ "t_invoice.payment_date, t_invoice.invoice_status, t_invoice.bhp_total "
				+ "from t_licence, t_invoice, t_bank_guarantee where t_licence.t_licence_id = t_invoice.t_licence_id and "
				+ "t_invoice.invoice_id = t_bank_guarantee.invoice_id and t_bank_guarantee.t_licence_id = t_licence.t_licence_id and ";
	
	private static final String EXTRA_QUERY 
		= "((t_invoice.MONTH_TO is null or t_invoice.INVOICE_TYPE='3' OR T_INVOICE.IS_MANUAL_CANCEL='Y') "
	 		+ "or (t_invoice.invoice_status != 'C' and t_invoice.INVOICE_TYPE in ('2', '3', '4', '5', '6'))"
//	 		+ "or (t_invoice.invoice_status = 'C' and t_invoice.INVOICE_TYPE in ('3', '4', '5', '6'))"
	 		+ ") "
	 		+ "order by t_licence_id, client_name, year_to, invoice_type, month_to, invoice_id";
	
	private static final String NATIVE_QUERY = "select "
			+ "t_licence.t_licence_id, "
			+ "t_invoice.invoice_id, "
			+ "t_licence.client_name, "
			+ "t_licence.client_id, "
			+ "t_licence.bhp_method, "
			+ "t_invoice.year_to, "
			+ "to_char(t_invoice.inv_begin_date, 'YYYY'), "
			+ "t_licence.licence_no, t_invoice.invoice_no, "
			+ "t_invoice.invoice_type, "
			+ "t_invoice.month_to, "
			+ "to_char(t_invoice.payment_due_date, 'dd-MM-yyyy'), "
			+ "to_char(t_invoice.payment_date,'dd-MM-yyyy'), "
			+ "t_invoice.invoice_status, "
			+ "t_invoice.bhp_total, "
			+ "t_invoice.save_status, "
			+ "t_licence.km_no, "
			+ "t_invoice.bg_total "
			+ "from t_licence, t_invoice "
			+ "where t_licence.t_licence_id = t_invoice.t_licence_id and ";

	private static final String QUERY_DUE_DATE = 
			"select "
					+ "l.client_name, "
					+ "l.client_id, "
					+ "l.bhp_method, "
					+ "i.year_to, "
					+ "to_char(i.inv_begin_date, 'YYYY'), "
					+ "i.invoice_no, "
					+ "i.invoice_type, "
					+ "i.month_to, "
					+ "to_char(i.payment_due_date, 'dd-MM-yyyy'), "
					+ "i.bhp_total, "
					+ "i.invoice_status "
			+ "from t_invoice i left join t_licence l on i.t_licence_id=l.t_licence_id "
			+ "where i.invoice_status='U' "
			+ "AND i.payment_due_date between "
			+ "sysdate "
			+ "AND "
			+ "sysdate+30 "
			+ "order by i.payment_due_date, i.t_licence_id";
	
	public LicenseDAOImpl() {
		super(License.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public License searchLicenseById(String tLicenceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findInvoiceByClientName(String clientName,
			String invoiceStatus) {
		List<Object> list 	= null;
		String extraQuery	= null;
		String extraQuery2	= null;
		
		if (invoiceStatus == "A"){
			extraQuery = "regexp_like (t_licence.client_name, '"+clientName+"', 'i') "
					+ "and t_invoice.save_status!='C' and ";
		}else{
			extraQuery = "regexp_like (t_licence.client_name, '"+clientName+"', 'i') "
					+ "and t_invoice.invoice_status ='" + invoiceStatus + "' and t_invoice.save_status!='C' and ";
		}

		extraQuery2 = EXTRA_QUERY;
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery + extraQuery2);
		list = query.list();
		return list;
	}

	@Override
	public List<Object> findInvoiceByClientID(String clientID,
			String invoiceStatus) {
		List<Object> list 	= null;
		String extraQuery	= null;
		String extraQuery2	= null;
		
		if (invoiceStatus == "A"){
			 extraQuery = "t_licence.client_id = '" + clientID
					 		+ "' and t_invoice.save_status!='C' and ";
		}else{
			extraQuery = "t_licence.client_id = '" + clientID
							+ "' and t_invoice.invoice_status ='" + invoiceStatus + "' and t_invoice.save_status!='C' and ";
		}

		extraQuery2 = EXTRA_QUERY;
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery + extraQuery2);
		list = query.list();
		return list;
	}

	@Override
	public List<Object> findInvoiceByMethod(String bhpMethod,
			String invoiceStatus) {
		List<Object> list 	= null;
		String extraQuery	= null;
		String extraQuery2	= null;
		
		if (invoiceStatus == "A"){
			 extraQuery = "t_licence.bhp_method ='" + bhpMethod
					+ "' and t_invoice.save_status!='C' and ";
		}else{
			extraQuery = "t_licence.bhp_method = '" + bhpMethod
					+ "' and t_invoice.invoice_status ='" + invoiceStatus + "' and t_invoice.save_status!='C' and ";
		}

		extraQuery2 = EXTRA_QUERY;
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery + extraQuery2);
		list = query.list();
		return list;
	}

	@Override
	public List<Object> findInvoiceByLicenceNo(String licenceNo,
			String invoiceStatus) {
		List<Object> list 	= null;
		String extraQuery	= null;
		String extraQuery2	= null;
		
		if (invoiceStatus == "A"){
			 extraQuery = "t_licence.licence_no = '" + licenceNo
					+ "' and t_invoice.save_status!='C' and ";
		}else{
			extraQuery = "t_licence.licence_no = '" + licenceNo
					+ "' and t_invoice.invoice_status ='" + invoiceStatus + "'and t_invoice.save_status!='C' and ";
		}

		extraQuery2 = EXTRA_QUERY;
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(
				NATIVE_QUERY + extraQuery + extraQuery2);
		list = query.list();

		return list;
	}
	
	@Override
	public List<License> findByClientName(String clientName) {
		List<License> list = null;

//		String extraQuery = "clientName like '%" + clientName + "%'";
//		Query query = getSessionFactory().getCurrentSession().createQuery(
//				QUERY_LIST_LICENSE_FROM_EBS + extraQuery);
//		list = query.list();
//		return list;
		
		Criteria crit = getSession().createCriteria(License.class);
		crit.add(Restrictions.ilike("clientName", "%" + clientName + "%"));
		crit.addOrder(Order.asc("clientName"));

		List<License> results = crit.list();
		
		return  results;
	}

	@Override
	public List<License> findByClientNo(String clientNumber) {
		List<License> list = null;
		String extraQuery = "clientNo ='" + clientNumber + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_LICENSE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<License> findByMethod(String bhpMethod) {
		List<License> list = null;
		String extraQuery = "bhpMethod ='" + bhpMethod + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_LICENSE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public List<License> findByLicenceNo(String licenceNo) {
		List<License> list = null;

		String extraQuery = "licenceNo ='" + licenceNo + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_LICENSE_FROM_EBS + extraQuery);
		list = query.list();
		return list;
	}

	@Override
	public License findLicenseByID(Object licenceID) {
		String extraQuery = "tLicenceId ='" + licenceID + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(
				QUERY_LIST_LICENSE_FROM_EBS + extraQuery);
		License license = (License) query.uniqueResult();
		return license;
	}

	@Override
	public List<License> findByAnnualPercentId(Long annualPercentId) {
		String criteria =  " from License l where l.variableAnnualPercent = " + annualPercentId; 
		Query query = getSessionFactory().getCurrentSession().createQuery(criteria);
		List<License> result = query.list();
		return result;
	}
	
	@Override
	public List<Object> findInvoiceDueDate() {
		List<Object> list = null;

		Query query = getSessionFactory().getCurrentSession().createSQLQuery(QUERY_DUE_DATE);
		list = query.list();
		return list;
	}

}
