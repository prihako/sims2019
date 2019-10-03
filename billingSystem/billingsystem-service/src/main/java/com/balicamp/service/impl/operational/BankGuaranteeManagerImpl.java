package com.balicamp.service.impl.operational;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.bankguarantee.BankGuaranteeDao;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.operational.BankGuaranteeManager;

@Service("bankGuaranteeManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class BankGuaranteeManagerImpl extends AbstractManager
				implements BankGuaranteeManager{

	private static final long serialVersionUID = 1L;
	private final BankGuaranteeDao bankGuaranteeDAO;

	@Autowired
	public BankGuaranteeManagerImpl (BankGuaranteeDao bankGuaranteeDAO){
		this.bankGuaranteeDAO = bankGuaranteeDAO;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BankGuarantee> findByLicenceNo(String licenceNo, String type) {
		return bankGuaranteeDAO.findByLicenceNo(licenceNo, type);
	}

	@Override
	public List<Object> searchByKeyword(String searchMethod,
			String searchKeyword, String extraQuery) {
		return bankGuaranteeDAO.searchByKeyword(searchMethod, searchKeyword, extraQuery);
	}

	@Override
	public BankGuarantee searchByLicenseAndYear(String licenseNo,
			String submitYearTo) {
		return bankGuaranteeDAO.searchByLicenseAndYear(licenseNo, submitYearTo);
	}

	@Override
	public BankGuarantee findByBgId(Long id) {
		return bankGuaranteeDAO.findByBgId(id);
	}

	@Override
	public void save(BankGuarantee bg) {
		bankGuaranteeDAO.saveBG(bg);
	}

	@Override
	public BankGuarantee findByInvoiceID(Long licenceID, Long invoiceID) {
		return bankGuaranteeDAO.findByInvoiceID(licenceID, invoiceID);
	}

	@Override
	public List<BankGuarantee> findByLicenseId(Long licenseId) {
		List<BankGuarantee> listBg = bankGuaranteeDAO.findByLicenseId(licenseId);
		return listBg;
	}

	@Override
	public BankGuarantee findBgByInvoiceId(Long invoiceId) {
		return bankGuaranteeDAO.findBgByInvoiceId(invoiceId);
	}

	@Override
	public BankGuarantee findBgNextYear(Long licenseId, BigDecimal submitYearTo) {
		return bankGuaranteeDAO.findBgNextYear(licenseId, submitYearTo);
	}

	@Override
	public Map<String, String> performPaymentBG(BankGuarantee bg, Invoice invoice, License license) {
		return bankGuaranteeDAO.performPaymentBG(bg, invoice, license);
	}

	@Override
	public BankGuarantee findByInvoiceNo(String invoiceNo) {
		return bankGuaranteeDAO.findByInvoiceNo(invoiceNo);
	}

}
