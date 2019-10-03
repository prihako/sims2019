package com.balicamp.service.impl.mastermaintenance.license;

import java.math.BigDecimal;
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
import com.balicamp.dao.hibernate.mastermaintenance.license.LicenseDao;
import com.balicamp.model.mastermaintenance.license.LicenseDisplay;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.License;
//import com.balicamp.model.mastermaintenance.license.MmLicence;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.license.LicenseManager;

@Service("licenseManagerObsolete")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class LicenseManagerImpl extends AbstractManager implements
		LicenseManager, InitializingBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final LicenseDao licenseDao;

	private static final Log log = LogFactory.getLog(LicenseManagerImpl.class);

	@Autowired
	public LicenseManagerImpl(LicenseDao licenseDao) {
		this.licenseDao = licenseDao;
	}

	@Override
	public List<LicenseDisplay> findLicense(SearchCriteria searchCriteria,
			int firstResult, int maxResult) {
		// TODO Auto-generated method stub

		List<Licenses> licenceList = licenseDao.findByCriteria(searchCriteria,
				firstResult, maxResult);

		List<LicenseDisplay> licenseDisplayList = new ArrayList<LicenseDisplay>();
		for (Licenses tmpLicence : licenceList) {

			LicenseDisplay toAdd = new LicenseDisplay(
					tmpLicence.getBsLicenceId(), tmpLicence.getLicenceId(),
					tmpLicence.getLicenceNo(), tmpLicence.getYearTo(),
					tmpLicence.getLicenceDate(), tmpLicence.getLicenceBegin(),
					tmpLicence.getLicenceEnd(), tmpLicence.getTransmitMin(),
					tmpLicence.getTransmitMax(), tmpLicence.getReceiveMin(),
					tmpLicence.getReceiveMax(), tmpLicence.getServiceId(),
					tmpLicence.getSubServiceId(), tmpLicence.getBhpMethod(),
					tmpLicence.getClientName(), tmpLicence.getClientId(),
					tmpLicence.getBhpTotal(), tmpLicence.getBhpPaymentType(),
					tmpLicence.getLicenceStatus());

			System.out.println("LicenceId = " + tmpLicence.getLicenceId()
					+ ", LicenceNo = " + tmpLicence.getLicenceNo()
					+ ", YearTo =" + tmpLicence.getYearTo()
					+ ", Licence date =" + tmpLicence.getLicenceDate()
					+ ", LicenceBegin =" + tmpLicence.getLicenceBegin()
					+ ", LicenceEnd =" + tmpLicence.getLicenceEnd());

			licenseDisplayList.add(toAdd);
		}

		return licenseDisplayList;
		// return null;
	}

	@Override
	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {
		return licenseDao.findByCriteriaCount(searchCriteria);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return licenseDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(Licenses license) {
		licenseDao.saveOrUpdate(license);
	}

	@Override
	public void saveLicense(String service, String subService,
			String bhpMethod, String clientName, String licenceId,
			String licenceNo, String licenceDate, String licenceBegin,
			String licenceEnd) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer findBhpAnnualRate(Integer year) {
		Integer bhpRate = new Integer(0);
		bhpRate = licenseDao.findBhpAnnualRate(year);
		return bhpRate;
	}



	@Override
	public List<Licenses> findListByLicenseNo(String licenceNo) {
		List<Licenses> licenseList = licenseDao.findListByLicenseNo(licenceNo);
		return licenseList;
	}

	@Override
	public Licenses findByLicenseId(Long bsLicenceId, String licenceId) {
		// TODO Auto-generated method stub
		return licenseDao.findLicenseById(bsLicenceId, licenceId);
	}

	@Override
	public List<Licenses> findByLicenseId(String licenceId) {
		return licenseDao.findLicenseById(licenceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(Long bsLicenceId, String licenceId) {
		// TODO Auto-generated method stub
		 licenseDao.deleteLicenseById(bsLicenceId, licenceId);

	}

	@Override
	public List<Licenses> findLicenseForCreateInvoice(String licenseId, String yearTo) {
		List<Licenses> licenseList = licenseDao.findLicenseForCreateInvoice(licenseId, yearTo);
		return licenseList;
	}

	@Override
	public List findLicenseIdList(String serviceId, String subServiceId,
			String metodeBhp, String clientName, String skmNumber,
			String licenseId) {
		List licenseIdList = licenseDao.findLicenseIdList(serviceId, subServiceId, metodeBhp, clientName, skmNumber, licenseId);
		return licenseIdList;
	}

	@Override
	public String findLicenseYear(String licenseId) {
		String year = licenseDao.findLicenseYear(licenseId);
		return year;
	}

	@Override
	public Licenses getDetailLicense(String licenseNumber, String licenceId,
			String licenseYear) {
		Licenses licenses = licenseDao.getDetailLicense(licenseNumber, licenceId, licenseYear);
		return licenses;
	}

	@Override
	public List<Licenses> searchLicense(final BigDecimal serviceId, final BigDecimal subServiceId,
			final String methodBhp, final String clientName, final String licenceNo) {
		List<Licenses> licenseList = new ArrayList<Licenses>();
		licenseList = licenseDao.searchLicense(serviceId, subServiceId, methodBhp,clientName, licenceNo);
		return licenseList;
	}

	@Override
	public List<Licenses> findLicense(Licenses license) {
		List<Licenses> licenseList = new ArrayList<Licenses>();
		licenseList= licenseDao.findLicense(license);
		return licenseList;
	}

	@Override
	public List<Licenses> findLicenceByNo(String licenceNo) {
		List<Licenses> licenseList = new ArrayList<Licenses>();
		licenseList= licenseDao.findLicenseByNo(licenceNo);
		return licenseList;
	}

	@Override
	public void deleteByLicenseId(String licenceId) {
		licenseDao.deleteByLicenseId(licenceId);
	}

	@Override
	public License searchLicenseById(String tLicenceId) {
		// TODO Auto-generated method stub
		return null;
	}
}
