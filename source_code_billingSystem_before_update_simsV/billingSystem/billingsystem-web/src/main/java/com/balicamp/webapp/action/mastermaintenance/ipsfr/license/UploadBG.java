package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.mastermaintenance.license.UploadView;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.License;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.license.UploadViewManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class UploadBG extends AdminBasePage implements PageBeginRenderListener {
	
	private static final String LIST_BG = "DATA_BG";
	
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";
	 
	private IUploadFile file;
	
	private UploadView uploadView;
	
	private License license;
	
	private String deskripsi;
	
	private BankGuarantee bg;
	
	public abstract void setLicenseNo(String licenseNo);
	
	public abstract String getLicenseNo();
	
	public abstract void setClientName(String clientName);
	
	public abstract String getClientName();
	
	public abstract void setClientID(String clientID);
	
	public abstract String getClientID();
	
	public abstract void setYearTo(String year);
	
	public abstract String getYearTo();
	
	public abstract String getYear();
	
	public abstract void setYear(String year);
	
	public abstract void setPercentYear(String percent);
	
	public abstract String getPercentYear();
	
	public abstract void setBhpMethod(String bhpMethod);
	
	public abstract String getBhpMethod();
	
	public abstract void setJenisDokumen(String doc);
	
	public abstract String getJenisDokumen();
	
	public void setDeskripsi(String deskripsi){
		this.deskripsi = deskripsi;
	}
	
	public String getDeskripsi(){
		return deskripsi;
	}
	
	public abstract void setReferenceId(String ref);
	
	public abstract String getReferenceId();
	
	public abstract void setDocType(String type);
	
	public abstract String getDocType();
	
	public abstract void setDueDate(Date date);
	
	public abstract Date getDueDate();
	
	public abstract void setDocumentNumber(String docNum);
	
	public abstract String getDocumentNumber();
	
	public void setLicense(License license){
		this.license = license;
	}
	
	public License getLicense(){
		return license;
	}
	
	public void setUploadView(UploadView uv){
		uploadView = uv;
	}
	
	public UploadView getUploadView(){
		return uploadView;
	}
	
	public void setBankGuarantee(BankGuarantee bg){
		this.bg = bg;
	}
	
	public BankGuarantee getBankGuarantee(){
		return bg;
	}
	
	public abstract void setNotification(boolean note);
	
	public abstract boolean getNotification();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
	@InjectObject("spring:uploadViewManager")
	public abstract UploadViewManager getUploadViewManager();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if(getUploadView()!=null){
			setUploadView(getUploadView());
		}else{
			setUploadView(new UploadView());
		}
	}
	
	public IUploadFile getFile() { 
	    return file; 
	}
	    
	public void setFile(IUploadFile value) {
	    file = value;
	}
	
	public String getFilename() { 
        if (file != null) {
            return file.getFileName();
        } else {
            return "";   
        }        
    }
	
	public void doSave(IRequestCycle cycle){
		
		String errorMessage = generalValidation();
		if(errorMessage != null){
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			setAllFields();
			return;
		}
		
		String fileName = file.getFileName();
		fileName = fileName.replaceAll("\\s+","_"); 
		fileName = fileName.replace("\\","_"); 
		fileName = fileName.replace("/","_"); 
		int index = fileName.indexOf("pdf") - 1;
		if(index > 0){
			fileName = fileName.substring(0, index) + getTimestampStr() + ".pdf";
		}
		
		boolean duplicateFile = UploadHelper.checkDuplicateFileName(fileName, file);
		if(duplicateFile){
			String errorDuplicate = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.duplicateFileName");
			addError(getDelegate(), "errorShadow", errorDuplicate, ValidationConstraint.CONSISTENCY);
			setAllFields();
			return;
		}
		
		boolean validationMsg = validation();
		if (validationMsg == true) {
			setAllFields();
//			save file temporary before move to destination folder when user agree to replace document
			UploadHelper.saveFileToServerTemp(fileName, file);
			
			return;
		}
		
		UploadHelper.saveFile(fileName, file);
		
		System.out.println("Get percentYear : " + getPercentYear());
		DocumentUpload doc = new DocumentUpload();
	    doc.setDocDesc(getDeskripsi());
	    doc.setCreatedOn(new Date());
	    doc.setCreatedBy(getUserLoginFromSession().getUserName());
		doc.setFileName(fileName);
		doc.setYearTo(Integer.valueOf(uploadView.getYearTo()));
		doc.setLicenseNo(uploadView.getLicenseNo());
		doc.setReferenceId(String.valueOf(uploadView.getReferenceId()));
		doc.setFileDir(DIRECTORY_DOCUMENT);
		doc.setDocType(String.valueOf(3));
		doc.setvUploadId(uploadView.getVUploadId());
		
		setAllFields();
		
		getDocumentUploadManager().saveDocument(doc);
		
		updateData();
		
		//For Save to audit log
		getUserManager().uploadDokumen(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Bank Garansi");
				
		doBack(cycle);
	}
	
	public void doReplace(IRequestCycle cycle, String licenseNo, BigDecimal referenceId, Integer yearTo) throws IOException{
		
		DocumentUpload docToDelete = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()),uploadView.getLicenseNo(), uploadView.getVUploadId());
		
		SimpleDateFormat format = new SimpleDateFormat("d-MMM-yy");
		Date date = new Date();
		String fileName = file.getFileName();
		fileName = fileName.replaceAll("\\s+","_"); 
		fileName = fileName.replace("\\","_"); 
		fileName = fileName.replace("/","_"); 
		int index = fileName.indexOf("pdf") - 1;
		if(index > 0){
			fileName = fileName.substring(0, index) + getTimestampStr() + ".pdf";
		}
		
//		Delete file before we replace with the new one
		UploadHelper.deleteFile(docToDelete.getFileName(), docToDelete);
		
//		For copy file from temporary folder into destination folder
		UploadHelper.copyFile(fileName, file);
		
//		For set field after replace document
		setAllFields();
		
		getDocumentUploadManager().replaceDocument(licenseNo, Integer.valueOf(yearTo), String.valueOf(referenceId), uploadView.getVUploadId(),
				DIRECTORY_DOCUMENT, fileName , getDeskripsi(), format.format(date), getUserLoginFromSession().getUsername());
		
		updateData();
		doBack(cycle);
	}
	
//	To set file after page reload
	public void setAllFields(){

		if(license != null){
			setClientID(String.valueOf(license.getClientNo()));
		}
		
		if(bg != null){
			setDueDate(bg.getDueDate());
		}
		
		setLicenseNo(uploadView.getLicenseNo());
		setReferenceId(String.valueOf(uploadView.getReferenceId()));
		setClientName(uploadView.getClientName());
		setPercentYear(uploadView.getYear());
		setYearTo(String.valueOf(uploadView.getYearTo()));
		setYear(uploadView.getYear());
		setBhpMethod(uploadView.getBhp());
		setJenisDokumen(uploadView.getType());
	}
	
	public void doRemove(){
		setAllFields();
	}
	
	private String criteria;
	private String searchCriteria;
	private String jenisDokumen;
	
	public void setCriteriaSearch(String jenisDokumen, String crit, String searchCriteria){
		this.jenisDokumen = jenisDokumen;
		criteria = crit;
		this.searchCriteria = searchCriteria;
	}
	
	public void doBack(IRequestCycle cycle){
		UploadDokumen upDoc = (UploadDokumen) cycle.getPage("uploadDokumen");
		upDoc.setActiveBG(true);
		
		upDoc.setJenisBG(criteria);
		upDoc.setKriteriaBG(searchCriteria);
		upDoc.setJenisDokumen(jenisDokumen);
		
		cycle.activate(upDoc);
	}
	
//	For check, if document exist, prompt user for replacement
	public boolean validation(){
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()),uploadView.getLicenseNo(), uploadView.getVUploadId());
		boolean errorMessage = false;
		if(docUp != null){
			errorMessage = true;
			setNotification(true);
		}
		
		return errorMessage;
	}
	
	public String generalValidation(){
		
		String errorMessage = null;
		if(getDeskripsi() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.deksripsi.empty");
		}else if(file == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.empty");
		}else if(file.getFileName() != null){
			String fileName = file.getFileName();
			int length = fileName.length();
			String subString = fileName.substring(length-4, length);
			if( !(subString.equals(".pdf")) ){
				errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.notPdf");
			}
		}
		
		return errorMessage;
	}
	
	public void updateData(){
		List<UploadView> listData = getUploadViewManager().findDataByClientName("BG-3", uploadView.getClientName());
		
		saveObjectToSession(listData, LIST_BG);
	}
	
	public static String getTimestampStr(){
		Date dateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String timestamp = formatter.format(dateTime);
		
		return timestamp;
	}
	
}
