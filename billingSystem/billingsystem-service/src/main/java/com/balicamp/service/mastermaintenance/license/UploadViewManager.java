package com.balicamp.service.mastermaintenance.license;

import java.math.BigDecimal;
import java.util.List;

import com.balicamp.model.mastermaintenance.license.UploadView;
import com.balicamp.service.IManager;

public interface UploadViewManager extends IManager {

	public List<UploadView> findAllView();

	public List<UploadView> findBI(String source);

	public List<UploadView> findBG(String source);

	public List<UploadView> findIN(String source);

	public List<UploadView> findDataByClientName(String source, String clientName);

	public List<UploadView> findDataByClientName(String source, String clientName, String docType);
	
	public UploadView findUploadViewByLicenseNoUnique(String licenseNo);

	public List<UploadView> findDataByLicenseNo(String source, String licenseNo, String docType);
	
	public UploadView findUploadView(BigDecimal referenceId, String licenseNo, String year);
	
	public List<UploadView> findDataByLicenseNo(String source, String licenseNo);

	public List<UploadView> findDataBIByYear(String year);

	public List<UploadView> findDataBIByYear(String year, String tSource);
	
	public List<UploadView> findDataBIByNoKM(String licenseNo);

	public List<UploadView> findDataBIByNoKM(String licenseNo, String tSource);
	
	public UploadView findDataBIByYearUnique(String year);
	
	public UploadView findUploadViewUnique(String tSource, BigDecimal referenceId, String licenseNo, String year, Integer yearTo);
	
	public UploadView findDataByViewUploadId(String viewUploadId, String licenseNo, String year, Integer yearTo);
	
	public List<UploadView> findBiByDocName(String tSource, String docName);
	
	public UploadView findDataByViewUploadId(String viewUploadId, String year);
}
