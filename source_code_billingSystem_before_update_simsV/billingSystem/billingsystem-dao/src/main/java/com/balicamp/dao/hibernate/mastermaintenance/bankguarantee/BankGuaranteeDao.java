package com.balicamp.dao.hibernate.mastermaintenance.bankguarantee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.balicamp.dao.admin.AdminGenericDao;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;

/**
 * DAO for BankGuarantee
 * @author <a href="mailto:christian.soewoeh@sigma.co.id">Christian Suwuh</a>
 * @author <a href="mailto:hendy.yusprasetya@sigma.co.id">Hendy</a> - edited
 */
public interface BankGuaranteeDao extends AdminGenericDao<BankGuarantee, Long>{

	List<BankGuarantee> findByLicenceNo(String licenceNo, String type);
	List<Object> searchByKeyword(String searchMethod, String searchKeyword, String extraQuery);
	
	BankGuarantee searchByLicenseAndYear(String licenseNo, String submitYearTo);
	BankGuarantee searchById(Long bgId);
	BankGuarantee findByBgId(Long id);
	BankGuarantee findByInvoiceID (Long licenceID, Long invoiceID);
	BankGuarantee findBgByInvoiceId(Long invoiceId);
	BankGuarantee findBgNextYear(Long licenseId, BigDecimal submitYearTo);
	
	List<BankGuarantee> findByLicenseId(Long licenseId);

	Map<String, String> performPaymentBG (BankGuarantee bg, Invoice invoice, License license);
	
	void saveBG(BankGuarantee bg);
	
	BankGuarantee findByInvoiceNo(String invoiceNo);
		
}
