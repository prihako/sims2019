/**
 *
 */
package com.balicamp.dao.sims;


import java.util.Date;
import java.util.Map;
import java.util.Set;






import com.balicamp.dao.GenericDao;
import com.balicamp.model.admin.BaseAdminModel;

public interface BillingDao extends GenericDao<BaseAdminModel, String> {

	public Object findBillingByInvoiceNo(String invoiceNo);
	
	public Map<String, Object[]>  findBillingByInvoiceNo(Set<String> invoiceNo, String clientId);
	
	//bi type : comma separated
	public Object findBillingByInvoiceNo(String invoiceNo, String [] biTypes);
	
	public Map<String, Object[]> findBillingByInvoiceNo(Set<String> invoiceNo, String [] biTypes, String clientId);


	public Map<String, Object[]> findAllBillingPaidByInvoiceNoAndDate(
			Set<String> invoiceNo, Date trxDate, String[] biTypes);

	public Map<String, Object[]> findAllBillingUnpaidByInvoiceNoAndDate(
			Set<String> invoiceNo, Date trxDate, String[] biTypes);

	public Map<String, Object[]> findAllBillingPaidByInvoiceNoAndDateNotSettle(
			Set<String> invoiceNo, Date trxDate, String[] biTypes);

	public void addRemarks(String invoiceNo, Date trxDate,
			String[] biTypes, String remarks);

	public Map<String, Object[]> findAllBillingByInvoiceNoAndDate(
			Set<String> invoiceNo, Date trxDate);

	public Map<String, Object[]> findAllBillingDendaByInvoiceNo(Set<String> biId);

	public Map<String, Object[]> findAllBillingReconciledByInvoiceNo(Set<String> invoiceNo);

	


}
