package com.balicamp.service.operational;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.service.IManager;

public interface BankGuaranteeManager extends IManager{

	List<BankGuarantee> findByLicenceNo(String licenceNo, String type);
	List<Object> searchByKeyword(String searchMethod, String searchKeyword, String extraQuery);
	
	BankGuarantee searchByLicenseAndYear(String licenseNo, String submitYearTo);
	BankGuarantee findByBgId(Long id);
	BankGuarantee findByInvoiceID (Long licenceID, Long invoiceID);
	BankGuarantee findBgByInvoiceId(Long invoiceId);
	BankGuarantee findBgNextYear(Long licenseId, BigDecimal submitYearTo);
	
	List<BankGuarantee> findByLicenseId(Long licenseId);
	
	Map<String, String> performPaymentBG (BankGuarantee bg, Invoice invoice, License license);
	
	void save(BankGuarantee bg);
	
	BankGuarantee findByInvoiceNo(String invoiceNo);
	
}
