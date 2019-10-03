package com.balicamp.model.mastermaintenance.license;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.admin.BaseAdminModel;

/**
 * 
 * @author Prihako Nurukat
 * 
 */
@Entity
@Table(name="V_UPLOAD")
public class UploadView extends BaseAdminModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="V_UPLOAD_ID")
	private String vUploadId;

	@Column(name="TSOURCE")
	private String tSource;

	@Column(name="REFERENCE_ID")
	private BigDecimal referenceId;
	
//	@EmbeddedId
//	private UploadViewKey uploadViewKey;
//	
//	public UploadViewKey getUploadViewKey() {
//		return uploadViewKey;
//	}
//
//	public void setUploadViewKey(UploadViewKey uploadViewKey) {
//		this.uploadViewKey = uploadViewKey;
//	}

	@Column(name="T_LICENCE_ID")
	private BigDecimal licenseId;
	
	@Column(name="LICENCE_NO")
	private String licenseNo;
	
	@Column(name="YEAR_TO")
	private Integer yearTo;
	
	@Column(name="YEAR")
	private String year;
	
	@Column(name="BHP_METHOD")
	private String bhpMethod;
	
	@Column(name="CLIENT_ID")
	private BigDecimal clientID;
	
	@Column(name="CLIENT_NAME")
	private String clientName;
	
	@Column(name="DOC_TYPE")
	private String docType;
	
	@Column(name="DOC_NAME")
	private String docName;
	
	@Column(name="FILE_NAME")
	private String fileName;

	public String gettSource() {
		return tSource;
	}

	public void settSource(String tSource) {
		this.tSource = tSource;
	}

	public BigDecimal getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(BigDecimal referenceId) {
		this.referenceId = referenceId;
	}

	public BigDecimal getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(BigDecimal licenseId) {
		this.licenseId = licenseId;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Integer getYearTo() {
		return yearTo;
	}

	public void setYearTo(Integer yearTo) {
		this.yearTo = yearTo;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBhpMethod() {
		return bhpMethod;
	}

	public void setBhpMethod(String bhpMethod) {
		this.bhpMethod = bhpMethod;
	}

	public BigDecimal getClientID() {
		return clientID;
	}

	public void setClientID(BigDecimal clientID) {
		this.clientID = clientID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	public String getType(){
		String type = null;
		if(getDocType() != null){
			if(getDocType().equals("1")){
				type = "Keputusan Menteri IPSFR";
			}else if(getDocType().equals("2")){
				type = "Keputusan Menteri BI Rate";
			}else if(getDocType().equals("3")){
				type = "Bank Garansi";
			}else if(getDocType().equals("4")){
				type = "Surat Bukti Pencairan Bank Garansi";
			}else if(getDocType().equals("5")){
				type = "Surat Verifikasi Pencairan Bank Garansi";
			}else if(getDocType().equals("6")){
				type = "Surat Lainnya";
			}
		}else{
			type="--";
		}
		return type;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getBhp(){
		String str = null;
		
		if(getBhpMethod().equalsIgnoreCase("FR")){
			str = "Flat Rate";
		}else if(getBhpMethod().equalsIgnoreCase("VR")){
			str = "Variety Rate";
		}
		
		return str;
	}
	
	public String getVUploadId() {
		return vUploadId;
	}

	public void setVUploadId(String vUploadId) {
		this.vUploadId = vUploadId;
	}

//	@Override
//	public String toString() {
//		return "UploadView [tSource=" + tSource + ", referenceId="
//				+ referenceId + ", licenseId=" + licenseId + ", licenseNo="
//				+ licenseNo + ", yearTo=" + yearTo + ", year=" + year
//				+ ", bhpMethod=" + bhpMethod + ", clientID=" + clientID
//				+ ", clientName=" + clientName + ", docType=" + docType
//				+ ", docName=" + docName + ", fileName=" + fileName + "]";
//	}

	

	@Override
	public Object getPKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
