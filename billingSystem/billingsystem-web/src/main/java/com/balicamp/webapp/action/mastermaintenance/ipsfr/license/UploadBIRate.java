package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.File;
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
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.license.UploadViewManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class UploadBIRate extends AdminBasePage implements PageBeginRenderListener {
	
	private static final String LIST_BI = "DATA_BI_RATE";
	
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";
	 
	private IUploadFile file;
	
	private UploadView uploadView;
	
	private String deskripsi;
	
	private String year;
	
	public String getYear(){
		return year;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public abstract void setJenisDokumen(String doc);
	
	public abstract String getJenisDokumen();
	
	public void setDeskripsi(String deskripsi){
		this.deskripsi = deskripsi;
	}
	
	public String getDeskripsi(){
		return deskripsi;
	}
	
	public abstract void setNotification(boolean note);
	
	public abstract boolean getNotification();
	
	public void setUploadView(UploadView uv){
		uploadView = uv;
	}
	
	public UploadView getUploadView(){
		return uploadView;
	}
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
	@InjectObject("spring:uploadViewManager")
	public abstract UploadViewManager getUploadViewManager();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
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
		
//		Check if fields is empty
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
		if (validationMsg == true){
			
			setAllFields();
			
//			save file temporary before move to destination folder when user agree to replace document
			UploadHelper.saveFileToServerTemp(fileName, file);
			
			return;
		}
		
		UploadHelper.saveFile(fileName, file);
		
		DocumentUpload doc = new DocumentUpload();
	    doc.setDocDesc(getDeskripsi());
	    doc.setCreatedOn(new Date());
	    doc.setCreatedBy(getUserLoginFromSession().getUserName());
		doc.setFileName(fileName);
		doc.setYearTo(Integer.valueOf(uploadView.getYear()));
		doc.setLicenseNo(uploadView.getLicenseNo());
		doc.setReferenceId(String.valueOf(uploadView.getReferenceId()));
		doc.setFileDir(DIRECTORY_DOCUMENT);
		doc.setDocType(String.valueOf(2));
		doc.setvUploadId(uploadView.getVUploadId());
		
		getDocumentUploadManager().saveDocument(doc);
		
		setAllFields();
		
		updateData();
		
		//For Save to audit log
		getUserManager().uploadDokumen(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Penetapan BI Rate");
				
		doBack(cycle);
	}
	
	public void doReplace(IRequestCycle cycle, String licenseNo, BigDecimal referenceId, String year) throws IOException{
		
		DocumentUpload docToDelete = getDocumentUploadManager().findBiRateDocument(String.valueOf(uploadView.getReferenceId()), uploadView.getVUploadId());  
		
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
		
		//For set field after replace document
		setAllFields();
		
		SimpleDateFormat format = new SimpleDateFormat("d-MMM-yy");
		Date date = new Date();
		 
		getDocumentUploadManager().replaceBiRateDocument(Integer.valueOf(year), String.valueOf(referenceId), uploadView.getVUploadId(),
				DIRECTORY_DOCUMENT, fileName, getDeskripsi(), format.format(date), getUserLoginFromSession().getUsername());
		
		updateData();
		doBack(cycle);
	}
	
	public void setAllFields(){
		setYear(uploadView.getYear());
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
		upDoc.setActiveBI(true);
		
		upDoc.setKriteriaBi(searchCriteria);
		upDoc.setBiRate(criteria);
		upDoc.setJenisDokumen(jenisDokumen);
		
		cycle.activate(upDoc);
	}

	//For check, if document exist, prompt user for replacement
	public boolean validation(){
		DocumentUpload docUp = getDocumentUploadManager().findBiRateDocument(String.valueOf(uploadView.getReferenceId()), uploadView.getVUploadId());  
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
		List<UploadView> listData = getUploadViewManager().findDataBIByYear(uploadView.getYear(), "BI-2");
		
		saveObjectToSession(listData, LIST_BI);
	}
	
	public static String getTimestampStr(){
		Date dateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String timestamp = formatter.format(dateTime);
		
		return timestamp;
	}
	
}
