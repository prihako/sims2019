package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.license.UploadViewManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.operational.InfoPageBankGuarantee;

public abstract class UploadBGInBgCreate extends AdminBasePage implements PageBeginRenderListener {
	
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";
	 
	private IUploadFile file;
	
	private String deskripsi;
	
	public String getJenisDokumen(){
		return "Bank Garansi";
	}
	
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
	
	public abstract void setDocumentNumber(String docNum);
	
	public abstract String getDocumentNumber();
	
	public BankGuarantee getBankGuarantee(){
		return (BankGuarantee) getFields().get("BankGuaranteeFromBGCreate");
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
			return;
		}
		
		boolean validationMsg = validation();
		if (validationMsg == true) {
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
		doc.setYearTo(Integer.valueOf(getBankGuarantee().getTInvoice().getYearTo().toString()));
		doc.setLicenseNo(getBankGuarantee().getLicense().getLicenceNo());
		doc.setReferenceId(String.valueOf(getBankGuarantee().getBgId()));
		doc.setFileDir(DIRECTORY_DOCUMENT);
		doc.setDocType(String.valueOf(3));
		doc.setvUploadId("BG-3" + getBankGuarantee().getBgId());
		
		getDocumentUploadManager().saveDocument(doc);
		
		//For Save to audit log
		getUserManager().uploadDokumen(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Bank Garansi");
				
		doConfirm(cycle);
	}
	
	public void doReplace(IRequestCycle cycle, String licenseNo, BigDecimal referenceId, Integer yearTo) throws IOException{
		
		DocumentUpload docToDelete = getDocumentUploadManager().findAndDownload(String.valueOf(getBankGuarantee().getBgId()), 
				getBankGuarantee().getLicense().getLicenceNo(), "BG-3" + getBankGuarantee().getBgId());
		
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
		
		getDocumentUploadManager().replaceDocument(licenseNo, Integer.valueOf(yearTo), String.valueOf(referenceId), "BG-3" + getBankGuarantee().getBgId(),
				DIRECTORY_DOCUMENT, fileName , getDeskripsi(), format.format(date), getUserLoginFromSession().getUsername());
		
		doConfirm(cycle);
	}
	
	public void doConfirm(IRequestCycle cycle){
		InfoPageBankGuarantee infoPage = (InfoPageBankGuarantee) cycle.getPage("infoPageBankGuarantee");
		infoPage.setMessage(getText("confirmation.document.bankGuarantee.save"));
		cycle.activate(infoPage);
	}
	
//	For check, if document exist, prompt user for replacement
	public boolean validation(){
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(getBankGuarantee().getBgId()),
				getBankGuarantee().getLicense().getLicenceNo(), "BG-3" + getBankGuarantee().getBgId());
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
	
	public static String getTimestampStr(){
		Date dateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String timestamp = formatter.format(dateTime);
		
		return timestamp;
	}
	
}
