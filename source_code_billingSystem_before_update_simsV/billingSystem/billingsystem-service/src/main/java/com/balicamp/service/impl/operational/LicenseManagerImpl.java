package com.balicamp.service.impl.operational;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.operational.LicenseDAO;
import com.balicamp.model.operational.License;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.operational.LicenseManager;

@Service("licenseManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class LicenseManagerImpl extends AbstractManager implements
		LicenseManager {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final LicenseDAO licenseDAO;

	@Autowired
	public LicenseManagerImpl(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public License searchLicenseById(String tLicenceId) {
		License license = licenseDAO.searchLicenseById(tLicenceId);
		return license;
	}

	@Override
	public void save(License license) {
		// TODO Auto-generated method stub
		licenseDAO.saveOrUpdate(license);

	}

	@Override
	public List<Object> findInvoiceByClientName(String clientName,
			String invoiceStatus) {
		// TODO Auto-generated method stub
		List<Object> list = licenseDAO.findInvoiceByClientName(clientName,
				invoiceStatus);

		return removeDuplicateInvoiceFine24(list);
	}

	@Override
	public List<Object> findInvoiceByClientID(String clientID,
			String invoiceStatus) {
		List<Object> list = licenseDAO.findInvoiceByClientID(clientID,
				invoiceStatus);

		return removeDuplicateInvoiceFine24(list);
	}

	@Override
	public List<Object> findInvoiceByMethod(String bhpMethod,
			String invoiceStatus) {
		List<Object> list = licenseDAO.findInvoiceByMethod(bhpMethod,
				invoiceStatus);

		return removeDuplicateInvoiceFine24(list);
	}

	@Override
	public List<Object> findInvoiceByLicenceNo(String licenceNo,
			String invoiceStatus) {
		List<Object> list = licenseDAO.findInvoiceByLicenceNo(licenceNo,
				invoiceStatus);

		return removeDuplicateInvoiceFine24(list);
	}
	
	
	// Special for case fine 24, cause fine 24 have 3 invoice(1 for
	// remainder, 1 for djkn, 1 for somasi)
	public List<Object> removeDuplicateInvoiceFine24(List<Object> list){
		
		List<Integer> indexToRemoved = new ArrayList<Integer>();
		
		for (int i = 0; i < list.size(); i++) {
			Object[] row = (Object[]) list.get(i);

			if (i < (list.size() - 1)) {
				Object[] nextRow = (Object[]) list.get(i + 1);
				if ((row[0].toString()).equalsIgnoreCase(nextRow[0].toString())) {
					if ((row[10] != null) && (nextRow[10] != null)) {
						if (row[10].toString().equalsIgnoreCase("24") 
								&& (row[10]).toString().equalsIgnoreCase(nextRow[10].toString())) {
							if( Integer.valueOf(row[1].toString()) < Integer.valueOf(nextRow[1].toString())){
								indexToRemoved.add(i);
							}else{
								indexToRemoved.add(i+1);
							}
						}else if ((row[9].toString().equals("4") || row[9].toString().equals("6"))
								&& row[10].toString().equalsIgnoreCase("12") 
								&& row[10].toString().equalsIgnoreCase(nextRow[10].toString())) {
							if( Integer.valueOf(row[1].toString()) < Integer.valueOf(nextRow[1].toString())){
								indexToRemoved.add(i);
							}else{
								indexToRemoved.add(i+1);
							}
						}
					}
				}
			}
		}
		
		for(int i = indexToRemoved.size() - 1; i >= 0; i--){
			Object row = list.get(indexToRemoved.get(i));
			list.remove(row);
		}
		
		return list;
	}
	
	public void removeDuplicate(List<Object> list){
		
	}

	@Override
	public List<License> findByClientName(String clientName) {
		// TODO Auto-generated method stub
		List<License> list = licenseDAO.findByClientName(clientName);
		return list;
	}

	@Override
	public List<License> findByClientNo(String clientNumber) {
		// TODO Auto-generated method stub
		List<License> list = licenseDAO.findByClientNo(clientNumber);
		return list;
	}

	@Override
	public List<License> findByMethod(String bhpMethod) {
		// TODO Auto-generated method stub
		List<License> list = licenseDAO.findByMethod(bhpMethod);
		return list;
	}

	@Override
	public List<License> findByLicenceNo(String licenceNo) {
		List<License> list = licenseDAO.findByLicenceNo(licenceNo);
		return list;
	}

	@Override
	public License findLicenseByID(Object licenceID) {
		License license = licenseDAO.findLicenseByID(licenceID);
		return license;
	}

	@Override
	public List<License> findByAnnualPercentId(Long annualPercentId) {
		List<License> list = licenseDAO.findByAnnualPercentId(annualPercentId);
		return list;
	}

	@Override
	public List<Object> findInvoiceDueDate() {
		List<Object> invoiceList = licenseDAO.findInvoiceDueDate();
		return invoiceList;
	}

}
