package com.balicamp.service.impl.mastermaintenance.license;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.hibernate.mastermaintenance.license.LicenseSpectraDao;
import com.balicamp.model.mastermaintenance.license.LicenseSpectra;
import com.balicamp.model.mastermaintenance.license.LicenseSpectraDisplay;
//import com.balicamp.model.mastermaintenance.license.MmLicence;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.license.LicenseSpectraManager;

@Service("licenseSpectraManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class LicenseSpectraManagerImpl extends AbstractManager implements
		LicenseSpectraManager, InitializingBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final LicenseSpectraDao licenseSpectraDao;

	private static final Log log = LogFactory
			.getLog(LicenseSpectraManagerImpl.class);

	@Autowired
	public LicenseSpectraManagerImpl(LicenseSpectraDao licenseSpectraDao) {
		this.licenseSpectraDao = licenseSpectraDao;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LicenseSpectraDisplay> findLicenses(
			SearchCriteria searchCriteria, int firstResult, int maxResult) {
		List<LicenseSpectra> licenceList = licenseSpectraDao.findByCriteria(
				searchCriteria, firstResult, maxResult);

		List<LicenseSpectraDisplay> licenseSpectraDisplayList = new ArrayList<LicenseSpectraDisplay>();
		for (LicenseSpectra tmpLicence : licenceList) {

			LicenseSpectraDisplay toAdd = new LicenseSpectraDisplay(
					tmpLicence.getLicenceNumber(), tmpLicence.getApRefNumber(),
					tmpLicence.getEqFreqRangeMinEtx(),
					tmpLicence.getEqFreqRangeMaxEtx(),
					tmpLicence.getEqFreqRangeMinErx(),
					tmpLicence.getEqFreqRangeMaxErx());

			System.out.println("LicenceNumber = "
					+ tmpLicence.getLicenceNumber()
					+ ", Ap Ref Number = " + tmpLicence.getApRefNumber()
					+ ", Address Company =" + tmpLicence.getAddressCompany()
					+ ", Address Number =" + tmpLicence.getAddressNumber()
					+ ", TransmitMin =" + tmpLicence.getEqFreqRangeMinEtx()
					+ ", TransmitMax =" + tmpLicence.getEqFreqRangeMaxEtx()
					+ ", ReceiveMin =" + tmpLicence.getEqFreqRangeMinErx()
					+ ", ReceiveMax =" + tmpLicence.getEqFreqRangeMaxErx()
					);

			licenseSpectraDisplayList.add(toAdd);
		}

		return licenseSpectraDisplayList;
	}

	@Override
	public LicenseSpectra findLicenseByClient(String addressNumber,
			String addressCompany, String serviceId, String service,
			String subserviceId, String subservice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LicenseSpectra findLicenseByClientAndLicenceNumberID(
			String addressNumber, String addressCompany, String licenceId,
			String licenceNo, String serviceId, String service,
			String subserviceId, String subservice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<LicenseSpectra> findAllLicense(String addressCompany,
			String addressNumber, String licenceId, String licenceNo,
			String serviceId, String subserviceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {
		return licenseSpectraDao.findByCriteriaCount(searchCriteria);
	}

	//-------------------------------------------------------------------------------------------------------------------------------------hendy

	@Override
	public List<LicenseSpectra> searchLicenseForCreate(final String serviceName,
			final String subServiceName, final String clientName, final String licenseId,
			final String licenseNumber) {
		List<LicenseSpectra> licenseList = new ArrayList<LicenseSpectra>();
		licenseList = licenseSpectraDao.searchLicenseForCreate(serviceName, subServiceName, clientName, licenseId, licenseNumber);
		return licenseList;
	}

	@Override
	public List<LicenseSpectra> searchLicenseByNumber(final String licenseNumber) {
		List<LicenseSpectra> licenseList = new ArrayList<LicenseSpectra>();
		licenseList = licenseSpectraDao.searchLicenseByNumber(licenseNumber);
		return licenseList;
	}

}
