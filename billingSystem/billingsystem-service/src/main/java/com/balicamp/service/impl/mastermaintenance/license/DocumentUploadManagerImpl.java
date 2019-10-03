package com.balicamp.service.impl.mastermaintenance.license;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.hibernate.mastermaintenance.license.DocumentUploadDao;
import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;

@Service("documentUploadManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class DocumentUploadManagerImpl extends AbstractManager implements DocumentUploadManager, InitializingBean{

	private static final long serialVersionUID = 1L; 
	 
	private static final Log log = LogFactory.getLog(DocumentUploadManagerImpl.class);
	
	private final DocumentUploadDao documentUploadDao;

	@Autowired
	public DocumentUploadManagerImpl(DocumentUploadDao documentUploadDao){ 
		this.documentUploadDao = documentUploadDao;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDocument(DocumentUpload document) {
		documentUploadDao.save(document);
	}

	@Override
	public List<DocumentUpload> findAllDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentUpload> findDocumentByDescription(String desc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDefaultDao() {
		
		return documentUploadDao;
	}

	@Override
	public List findDataForTable(String kmNo, Long annualPercentId,
			Long referenceId, int yearTo) {
		
		List list = documentUploadDao.findDataForTable("", 1L, 1L, 0);
		
		return list;
	}

	@Override
	public DocumentUpload findDocument(String licenseNo, Integer year) {
		DocumentUpload docUp = documentUploadDao.findDocument(licenseNo, year);
		return docUp;
	}                

	@Override
	public void deleteDocument(String licenseNo, Integer year) {
		documentUploadDao.deleteDocument(licenseNo, year);
		documentUploadDao.flush();
	}

	@Override
	public void replaceDocument(String licenseNo, Integer year,
			String referenceId, String vUploadId, String fileDir, String fileName, String docDesc, String updatedOn, String updatedBy) {
		documentUploadDao.replaceDocument(licenseNo, year, referenceId, vUploadId, fileDir, fileName, docDesc, updatedOn, updatedBy);
		documentUploadDao.flush();
	}

	@Override
	public DocumentUpload findAndDownload(String referenceId, String licenseNo, String vUploadId) {
		DocumentUpload docUp = documentUploadDao.findAndDownload(referenceId, licenseNo, vUploadId);
		return docUp;
	}

	@Override
	public DocumentUpload findAndDownload(String referenceId, String licenseNo,
			String vUploadId, String docType) {
		DocumentUpload docUp = documentUploadDao.findAndDownload(referenceId, licenseNo, vUploadId, docType);
		return docUp;
	}

	@Override
	public DocumentUpload findBiRateDocument(String referenceId,
			String vUploadId) {
		DocumentUpload docUp = documentUploadDao.findBiRateDocument(referenceId, vUploadId);
		return docUp;
	}

	@Override
	public void replaceBiRateDocument(Integer year, String referenceId,
			String vUploadId, String fileDir, String fileName, String docDesc,
			String updatedOn, String updatedBy) {
		documentUploadDao.replaceBiRateDocument(year, referenceId, vUploadId, fileDir, fileName, docDesc, updatedOn, updatedBy);
		documentUploadDao.flush();
	}

	@Override
	public List<DocumentUpload> findDocument(String licenseNo, String docType) {
		List<DocumentUpload> result = documentUploadDao.findDocument(licenseNo, docType);
		return result;
	}

	@Override
	public void deleteDocument(DocumentUpload documentUpload) {
		documentUploadDao.delete(documentUpload);
	}

	@Override
	public DocumentUpload findBiByYear(Integer year) {
		DocumentUpload docUp = documentUploadDao.findBiByYear(year);
		return docUp;
	}

}
