package com.balicamp.service.impl.mastermaintenance.license;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.license.UploadViewDao;
import com.balicamp.model.mastermaintenance.license.UploadView;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.license.UploadViewManager;

@Service("uploadViewManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class UploadViewManagerImpl extends AbstractManager implements UploadViewManager, InitializingBean{
 
	private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(UploadViewManagerImpl.class);
	
	private final UploadViewDao uploadViewDao;

	@Autowired
	public UploadViewManagerImpl(UploadViewDao uploadViewDao){
		this.uploadViewDao = uploadViewDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UploadView> findAllView() {

		List list = uploadViewDao.findAllView();
		
		return list;
	}

	@Override
	public List<UploadView> findBI(String source) {
		List list = uploadViewDao.findBI(source);
		
		return list;
	}

	@Override
	public List<UploadView> findBG(String source) {
		List list = uploadViewDao.findBG(source);
		
		return list;
	}

	@Override
	public List<UploadView> findIN(String source) {
		List list = uploadViewDao.findIN(source);
		
		return list;
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UploadView> findDataByClientName(String source,
			String clientName) {
		List list = uploadViewDao.findDataByClientName(source, clientName);
		
		return list;
	}
	
	@Override
	public List<UploadView> findDataByClientName(String source,
			String clientName, String docType) {
		List list = uploadViewDao.findDataByClientName(source, clientName, docType);
		
		return list;
	}
 
	@Override
	public List<UploadView> findDataByLicenseNo(String source, String licenseNo) {
		List list = uploadViewDao.findDataByLicenseNo(source, licenseNo);
		
		return list;
	}
	
	@Override
	public List<UploadView> findDataByLicenseNo(String source, String licenseNo, String docType) {
		List list = uploadViewDao.findDataByLicenseNo(source, licenseNo, docType);
		
		return list;
	}

	@Override
	public UploadView findUploadViewByLicenseNoUnique(String licenseNo) {
		UploadView uploadView = uploadViewDao.findUploadViewByLicenseNoUnique(licenseNo);
		
		return uploadView;
	}

	@Override
	public List<UploadView> findDataBIByYear(String year) {
		List listData = uploadViewDao.findDataBIByYear(year);
		return listData;
	}

	@Override
	public List<UploadView> findDataBIByNoKM(String licenseNo) {
		List listData = uploadViewDao.findDataBIByNoKM(licenseNo);
		return listData;
	}

	@Override
	public UploadView findDataBIByYearUnique(String year) {
		UploadView upView = uploadViewDao.findDataBIByYearUnique(year);
		return upView;
	}

	@Override
	public UploadView findUploadView(BigDecimal referenceId, String licenseNo,
			String year) {
		UploadView upView = uploadViewDao.findUploadView(referenceId, licenseNo, year);
		return upView;
	}

	@Override
	public UploadView findUploadViewUnique(String tSource,
			BigDecimal referenceId, String licenseNo, String year,
			Integer yearTo) {
		UploadView upView = uploadViewDao.findUploadViewUnique(tSource, referenceId, licenseNo, year, yearTo);
		return upView;
	}

	@Override
	public UploadView findDataByViewUploadId(String viewUploadId, String licenseNo, String year,
			Integer yearTo) {
		UploadView upView = uploadViewDao.findDataByViewUploadId(viewUploadId, licenseNo, year, yearTo);
		return upView;
	}

	@Override
	public List<UploadView> findDataBIByYear(String year, String tSource) {
		List listData = uploadViewDao.findDataBIByYear(year, tSource);
		return listData;
	}

	@Override
	public List<UploadView> findDataBIByNoKM(String licenseNo, String tSource) {
		List listData = uploadViewDao.findDataBIByNoKM(licenseNo, tSource);
		return listData;
	}

	@Override
	public List<UploadView> findBiByDocName(String tSource, String docName) {
		List listData = uploadViewDao.findBiByDocName(tSource, docName);
		return listData;
	}

	@Override
	public UploadView findDataByViewUploadId(String viewUploadId, String year) {
		UploadView upView = uploadViewDao.findDataByViewUploadId(viewUploadId, year);
		return upView;
	}

}
