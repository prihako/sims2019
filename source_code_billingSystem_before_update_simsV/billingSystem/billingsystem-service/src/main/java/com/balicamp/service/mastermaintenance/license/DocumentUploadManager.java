package com.balicamp.service.mastermaintenance.license;

import java.util.List;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.service.IManager;
 
public interface DocumentUploadManager extends IManager {
	
	public void saveDocument(DocumentUpload document);
	
	public List<DocumentUpload> findAllDocuments();    
	
	public List<DocumentUpload> findDocumentByDescription(String desc);
	
	public List findDataForTable(String kmNo, Long annualPercentId, Long referenceId, int yearTo);
	
	public DocumentUpload findDocument(String licenseNo, Integer year);
	
	public void deleteDocument(String licenseNo, Integer year);
	
	public void replaceDocument(String licenseNo, Integer year, String referenceId, String vUploadId, String fileDir, String fileName, String docDesc, String updatedOn, String updatedBy);
	
	public DocumentUpload findAndDownload(String referenceId, String licenseNo, String vUploadId);
	
	public DocumentUpload findAndDownload(String referenceId, String licenseNo, String vUploadId, String docType);
	
	public DocumentUpload findBiRateDocument(String referenceId, String vUploadId);
	
	public void replaceBiRateDocument(Integer year, String referenceId, String vUploadId, String fileDir, String fileName, String docDesc, String updatedOn, String updatedBy);
	
	public List<DocumentUpload> findDocument(String licenseNo, String docType);
	
	public void deleteDocument(DocumentUpload documentUpload);
	
	public DocumentUpload findBiByYear(Integer year);
	
}
