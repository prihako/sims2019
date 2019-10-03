
package com.balicamp.dao.hibernate.mastermaintenance.license;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.model.mastermaintenance.license.DocumentUpload;

@Repository
public class DocumentUploadDaoHibernate extends AdminGenericDaoImpl<DocumentUpload, Long>
		implements DocumentUploadDao {

	public DocumentUploadDaoHibernate() {
		super(DocumentUpload.class);
		
	}

	@Override
	public void saveDocument(DocumentUpload document) {
		// TODO Auto-generated method stub
		
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
	public List findDataForTable(String kmNo, Long annualPercentId,
			Long referenceId, int yearTo) {
		
		Query query = getSession().createSQLQuery("SELECT T_LICENCE.CLIENT_NAME, T_LICENCE.LICENCE_NO, T_VAR_ANN_PERCENT_DTL.YEAR_TO, T_VAR_ANNUAL_PERCENT.PERCENT_YEAR, T_LICENCE.BHP_METHOD," 
													+ "T_DOC_UPLOAD.DOC_TYPE, T_DOC_UPLOAD.FILE_NAME "
													+ "FROM T_LICENCE, T_VAR_ANNUAL_PERCENT, T_VAR_ANN_PERCENT_DTL, T_DOC_UPLOAD "
													+ "WHERE T_LICENCE.KM_NO = T_VAR_ANNUAL_PERCENT.KM_NO AND T_VAR_ANNUAL_PERCENT.ANNUAL_PERCENT_ID = T_VAR_ANN_PERCENT_DTL.ANNUAL_PERCENT_ID "
													+ "AND T_LICENCE.T_LICENCE_ID = T_DOC_UPLOAD.REFERENCE_ID AND  T_VAR_ANN_PERCENT_DTL.YEAR_TO = T_DOC_UPLOAD.YEAR_TO");
		List result = query.list();
		return result;
	}

	@Override
	public DocumentUpload findDocument(String licenseNo, Integer year) {
		Query query = getSession().createQuery("SELECT d FROM DocumentUpload d WHERE d.licenseNo = '" + licenseNo + "' AND d.yearTo = " + year);
		DocumentUpload result = (DocumentUpload) query.uniqueResult();
		return result;
	}

	@Override
	public void deleteDocument(String licenseNo, Integer year) {
		Query query = getSession().createQuery(
				"delete DocumentUpload as d where d.licenseNo = '" + licenseNo + "' and  d.yearTo = " + year);
		query.executeUpdate();
	}

	@Override
	public void replaceDocument(String licenseNo, Integer year, 
			String referenceId, String vUploadId, String fileDir, String fileName, String docDesc, String updatedOn, String updatedBy) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"update  DocumentUpload  set fileDir = '"
						+ fileDir + "', fileName = '" + fileName + "', docDesc = '" + docDesc + 
						"', updatedOn = '" + updatedOn + "', updatedBy = '" + updatedBy +
						"' where licenseNo = '" +  licenseNo + "' and yearTo = " + year + 
						" and referenceId = '" + referenceId + "' AND vUploadId = '" + vUploadId + "'");
		query.executeUpdate();
	}

	@Override
	public DocumentUpload findAndDownload(String referenceId, String licenseNo, String vUploadId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("SELECT d FROM DocumentUpload d WHERE d.referenceId = '" 
				+ referenceId + "' AND d.licenseNo = '" + licenseNo + "' AND d.vUploadId = '" + vUploadId + "'");
		DocumentUpload result = (DocumentUpload) query.uniqueResult();
		return result;
	}

	@Override
	public DocumentUpload findAndDownload(String referenceId, String licenseNo,
			String vUploadId, String docType) {
		Query query = getSessionFactory().getCurrentSession().createQuery("SELECT d FROM DocumentUpload d WHERE d.referenceId = '" 
				+ referenceId + "' AND d.licenseNo = '" + licenseNo + "' AND d.vUploadId = '" + vUploadId + "' AND d.docType = '" + docType + "'");
		DocumentUpload result = (DocumentUpload) query.uniqueResult();
		return result;
	}

	@Override
	public DocumentUpload findBiRateDocument(String referenceId,
			String vUploadId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("SELECT d FROM DocumentUpload d WHERE d.referenceId = '" 
				+ referenceId + "' AND d.vUploadId = '" + vUploadId + "'");
		DocumentUpload result = (DocumentUpload) query.uniqueResult();
		return result;
	}

	@Override
	public void replaceBiRateDocument(Integer year, String referenceId,
			String vUploadId, String fileDir, String fileName, String docDesc,
			String updatedOn, String updatedBy) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"update  DocumentUpload  set fileDir = '"
						+ fileDir + "', fileName = '" + fileName + "', docDesc = '" + docDesc + 
						"', updatedOn = '" + updatedOn + "', updatedBy = '" + updatedBy +
						"' where yearTo = " + year + 
						" and referenceId = '" + referenceId + "' AND vUploadId = '" + vUploadId + "'");
		query.executeUpdate();
	}

	@Override
	public List<DocumentUpload> findDocument(String licenseNo, String docType) {
		Query query = getSession().createQuery("SELECT d FROM DocumentUpload d WHERE d.licenseNo = '" + licenseNo + "' AND d.docType = " + docType);
		List<DocumentUpload> result = query.list();
		return result;
	}

	@Override
	public DocumentUpload findBiByYear(Integer year) {
		Query query = getSessionFactory().getCurrentSession().createQuery("SELECT d FROM DocumentUpload d WHERE d.yearTo = " + year + " and d.docType = '2'"); 
		DocumentUpload result = (DocumentUpload) query.uniqueResult();
		return result;
	}

}
