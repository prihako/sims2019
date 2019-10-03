package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;
//import com.balicamp.model.mastermaintenance.service.Services;
//import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.ui.PropertySelectionData;


/**
 * @Author Prihako Nurukat 
 * The persistent class for the Document Upload.
 *
 */
@Entity
@Table(name="T_DOC_UPLOAD")
public class DocumentUpload extends BaseAdminModel implements ISequencesModel, PropertySelectionData, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="T_DOC_UPLOAD_ID")
	@SequenceGenerator(name="T_DOC_UPLOAD_SEQ", sequenceName="T_DOC_UPLOAD_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="T_DOC_UPLOAD_SEQ")
	private Long uploadId;
	
	@Column(name="REFERENCE_ID")
	private String referenceId;
	
	@Column(name="DOC_TYPE")
	private String docType;
	
	@Column(name="LICENCE_NO")
	private String licenseNo;
	
	@Column(name="YEAR")
	private int yearTo;
	
	@Column(name="DOC_DESC")
	private String docDesc;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="FILE_DIR")
	private String fileDir;
	
	@Column(name="CREATED_ON")
	private Date createdOn;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_ON")
	private Date updatedOn;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Column(name="V_UPLOAD_ID")
	private String vUploadId;
	
	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public int getYearTo() {
		return yearTo;
	}

	public void setYearTo(int yearTo) {
		this.yearTo = yearTo;
	}

	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getvUploadId() {
		return vUploadId;
	}

	public void setvUploadId(String vUploadId) {
		this.vUploadId = vUploadId;
	}

	@Override
	public String getPsdValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPsdLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPsdDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSequenceName() {
		// TODO Auto-generated method stub
		return "t_doc_upload_seq";
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

	
}