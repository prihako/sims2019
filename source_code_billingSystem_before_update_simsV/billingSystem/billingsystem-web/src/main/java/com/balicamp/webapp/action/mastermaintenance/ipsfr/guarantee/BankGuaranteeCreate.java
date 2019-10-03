package com.balicamp.webapp.action.mastermaintenance.ipsfr.guarantee;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.Constants;
import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.UploadDokumen;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.UploadHelper;
import com.balicamp.webapp.action.operational.UpdateBG;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class BankGuaranteeCreate extends AdminBasePage implements
		PageBeginRenderListener {

	@InjectSpring("bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();

	public abstract void setBankGuarantee(BankGuarantee bankGuarantee);
	public abstract BankGuarantee getBankGuarantee();

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	// Radio button for received BG
	public abstract void setBgReceived(String bgReceived);
	public abstract String getBgReceived();

	public abstract void setBhpMethod(String bhpMethod);
	public abstract String getBhpMethod();

	public abstract void setIdKlien(String idKlien);
	public abstract String getIdKlien();

	public abstract void setBgRequired(String bgRequired);
	public abstract String getBgRequired();

	public abstract void setBgDueDate(String bgDueDate);
	public abstract String getBgDueDate();

	public abstract void setBgSubmitConfirmation(String bgSubmit);
	public abstract String getBgSubmitConfirmation();
	
	public abstract IUploadFile getFile();
	public abstract void setFile(IUploadFile value);

	public abstract BankGuarantee getRow();
	
	public abstract void setReceivedDate(String receivedDate);
	public abstract String getReceivedDate();

	public abstract void setBgPublishDate(String date);
	public abstract String getBgPublishDate();
	
	public abstract void setBgBeginDate(String date);
	public abstract String getBgBeginDate(); 
	
	public abstract void setBgEndDate(String date);
	public abstract String getBgEndDate(); 
	
	public abstract void setDueDate(String date);
	public abstract String getDueDate(); 
	
	public abstract void setSubmitDueDate(String date);
	public abstract String getSubmitDueDate();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
			}

			setBhpMethod(getText("label.bhpMethod."+getBankGuarantee().getLicense().getBhpMethod()));
			setIdKlien(getBankGuarantee().getLicense().getClientNo().toString());
			setBgRequired(getText("label.bgAvailable."+getBankGuarantee().getLicense().getBgAvailableStatus()));
			setSubmitDueDate(DateUtil.convertDateToString(getBankGuarantee().getSubmitDueDate(), "dd-MMM-yyyy"));
//			setBgDueDate(DateUtil.convertDateToString(getBankGuarantee().getDueDate(), "dd-MMM-yyyy"));

			if(getFields()!=null && getFields().get("CONFIRM")!=null){
//				setBgSubmitConfirmation(getText("operational.bg.info.updateConfirmation1", new Object[]{getUserLoginFromSession().getUsername()}));
				setBgSubmitConfirmation(getText("operational.bg.info.updateConfirmation2"));
			}
			
			//Tambahan format tanggal biar sama semua dd-MMM-yyyy
			if(getBankGuarantee().getSaveStatus().equalsIgnoreCase("S")){
				setReceivedDate(DateUtil.convertDateToString(getBankGuarantee().getReceivedDate(), "dd-MMM-yyyy"));
				setBgPublishDate(DateUtil.convertDateToString(getBankGuarantee().getBgPublishDate(), "dd-MMM-yyyy"));
				setBgBeginDate(DateUtil.convertDateToString(getBankGuarantee().getBgBeginDate(), "dd-MMM-yyyy"));
				setBgEndDate(DateUtil.convertDateToString(getBankGuarantee().getBgEndDate(), "dd-MMM-yyyy"));
				setDueDate(DateUtil.convertDateToString(getBankGuarantee().getDueDate(), "dd-MMM-yyyy"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void showTable() {
		if(getFields()!=null && getFields().get("BG_DETAIL")!=null){
			getFields().remove("BG_DETAIL");
		}else{
			List<BankGuarantee> bgList = getBankGuaranteeManager().findByLicenceNo(getBankGuarantee().getLicense().getLicenceNo(),"UPDATE");
			getFields().put("BG_DETAIL", bgList);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BankGuarantee> getDetailCalculateBG(){
		List<BankGuarantee> bgList = (List) getFields().get("BG_DETAIL");
		return bgList;
	}

	public void doDraft(IRequestCycle cycle) {
		
		String errorMessage = bankGuaranteeDateValidation();
		
		if(errorMessage != null){
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		BankGuarantee bg = getBankGuarantee();
		if(bg.getReceivedDate()!=null){
			bg.setReceivedStatus(Constants.Status.UNSUBMITTED);
		}
		bg.setSaveStatus(Constants.Status.DRAFT);
		getBankGuaranteeManager().save(bg);

		//1st option to construct status page
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("leftMenu.operational.bg.updateBG"));
		infoPageCommand.addMessage(getText("operational.bg.info.updateDraftSuccess"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "updateBG.html"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		saveDocument();		
		
		cycle.activate(infoPage);

		//2nd option to refresh page
//		BankGuaranteeCreate bgCreate = (BankGuaranteeCreate) cycle.getPage("bankGuaranteeCreate");
//		bgCreate.setBankGuarantee(bg);
//		cycle.activate(bgCreate);
	}

	@SuppressWarnings("unchecked")
	public void doConfirm(IRequestCycle cycle) {
		String errorMessage1 = bankGuaranteeGeneralValidation();
		if(errorMessage1 != null){
			addError(getDelegate(), "errorShadow",errorMessage1, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String errorMessage2 = bankGuaranteeDateValidation();
		if(errorMessage2 != null){
			addError(getDelegate(), "errorShadow",errorMessage2, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		BankGuaranteeCreate bgCreate 	= (BankGuaranteeCreate) cycle.getPage("bankGuaranteeCreate");
		BankGuarantee bg 				= getBankGuarantee();
		bg.setSaveStatus(Constants.Status.SUBMIT);
//		bg.setReceivedStatus(Constants.Status.SUBMITTED);

		getFields().put("CONFIRM", "CONFIRM");
		bgCreate.setBankGuarantee(bg);
		bgCreate.setFields(getFields());
		
		saveDocument();
		
		cycle.activate(bgCreate);
	}

	public void doSubmit(IRequestCycle cycle) {
		BankGuarantee bg = getBankGuarantee();
		if(bg.getReceivedDate()!=null){
			bg.setReceivedStatus(Constants.Status.SUBMITTED);
		}
		bg.setSaveStatus(Constants.Status.SUBMIT);
		
		//Added @26-02-2015, buat field bank garansi yang dicairkan di halaman 
		//bankGuaranteeCreatePayment supaya menampilkan jumlah seperti field bank garansi yang diserahkan
		bg.setClaimValue(bg.getBgValueSubmitted());
				
		getBankGuaranteeManager().save(bg);

		getFields().remove("CONFIRM");

		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("leftMenu.operational.bg.updateBG"));
		infoPageCommand.addMessage(getText("operational.bg.info.updateSuccess"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "updateBG.html"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		saveDocument();
		
		cycle.activate(infoPage);
	}

	@SuppressWarnings("unchecked")
	public void doCancel(IRequestCycle cycle) {
		if(getFields().get("CONFIRM")!=null){
			BankGuaranteeCreate bgCreate 	= (BankGuaranteeCreate) cycle.getPage("bankGuaranteeCreate");
			BankGuarantee bg 				= getBankGuarantee();
			bg.setSaveStatus(Constants.Status.UNSUBMITTED);
			
			getFields().remove("CONFIRM");
			bgCreate.setBankGuarantee(bg);
			bgCreate.setFields(getFields());

			cycle.activate(bgCreate);
		}else{
			UpdateBG bgCreate = (UpdateBG) cycle.getPage("updateBG");
			getFields().put("BACK", "BACK");
			bgCreate.setFields(getFields());
			cycle.activate(bgCreate);
		}
	}

	public String uploadDocumentBG(IRequestCycle cycle) {
		UploadDokumen uploadDokumen = (UploadDokumen) cycle.getPage("uploadDokumen");
		uploadDokumen.setActiveBG(true);
		uploadDokumen.setJenisDokumen("BG");
		
		return null;
	}

	public String viewDocumentBG() {
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(getBankGuarantee().getBgId()),
				String.valueOf(getBankGuarantee().getLicense().getLicenceNo()), "BG-3" + getBankGuarantee().getBgId());
		
		if(docUp != null){
			File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
			
			if(downloadFile.exists()){
				String url = "./document.svc?imageId=" + docUp.getFileName();
				return url;   
			}
		}
		
		String url = "./documentNotFound.html";
		return url; 
	}

	public String viewDocumentKM() {
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(getBankGuarantee().getInvoice().getInvoiceId()),
				String.valueOf(getBankGuarantee().getLicense().getLicenceNo()), "IN-1" + getBankGuarantee().getInvoice().getInvoiceId());
		
		String url = null;
		
		if(docUp != null){
			File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
			
			if(downloadFile.exists()){
				url = "./document.svc?imageId=" + docUp.getFileName();
				return url;   
			}
			
		}else{
			List<DocumentUpload> listDoc = getDocumentUploadManager().findDocument(String.valueOf(getBankGuarantee().getLicense().getLicenceNo()), "1");
			
			if(listDoc.size() > 0){
				int index = listDoc.size();
				DocumentUpload document = listDoc.get(index-1);
				File downloadFile2 = new File(document.getFileDir() + document.getFileName());

				if(downloadFile2.exists()){
					url = "./document.svc?imageId=" + document.getFileName();
					return url;   
				}
			}
		}
		
		url = "./documentNotFound.html";
		return url;
	}
	
	public void saveDocument(){
//		For saving file
		if(getFile() != null){
			
			String fileName = getFile().getFileName();
//			int length = fileName.length();
//			String subString = fileName.substring(length-4, length);
//			if( !(subString.equals(".pdf")) ){
//				String errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.notPdf");
//				addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
//			}
			
			DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(getBankGuarantee().getBgId()),
					String.valueOf(getBankGuarantee().getLicense().getLicenceNo()), "BG-3" + getBankGuarantee().getBgId());
			
			if(docUp != null){
				UploadHelper.deleteFile(docUp.getFileName(), docUp);
				getDocumentUploadManager().deleteDocument(docUp);
			}
			
			Date dateTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
			String timestamp = formatter.format(dateTime);
			fileName = fileName.replaceAll("\\s+","_");
			fileName = fileName.replace("\\", "_");
			fileName =  fileName.replace("/", "_");
			int index = fileName.indexOf("pdf") - 1;
			if(index > 0){
				fileName = fileName.substring(0, index) + timestamp + ".pdf";
			}
			UploadHelper.saveFile(fileName, getFile());
			DocumentUpload doc = new DocumentUpload();
			doc.setDocDesc("Upload From Manage Invoice-");
			doc.setCreatedOn(new Date());
			
			if(getUserLoginFromSession() != null){
				doc.setCreatedBy(getUserLoginFromSession().getUserName());
			}else if(getUserLoginFromDatabase() != null){
				doc.setCreatedBy(getUserLoginFromDatabase().getUserName());
			}
			
			doc.setFileName(fileName);
			doc.setYearTo(Integer.valueOf(getBankGuarantee().getInvoice().getYearTo().toString()));
			doc.setLicenseNo(getBankGuarantee().getLicense().getLicenceNo());
			doc.setReferenceId(String.valueOf(getBankGuarantee().getBgId()));
			doc.setFileDir("webapps/SKM/");
			doc.setDocType("1");
			doc.setvUploadId("BG-3" + getBankGuarantee().getBgId());
			getDocumentUploadManager().saveDocument(doc);
		
		}
		
	}
	
	public String bankGuaranteeDateValidation(){
		String errorMessage = null;
		
		if(getBankGuarantee().getSubmitDueDate() != null && getBankGuarantee().getReceivedDate() != null){
			if(getBankGuarantee().getSubmitDueDate().compareTo(new Date()) > 0){
				if( getBankGuarantee().getReceivedDate().compareTo(getBankGuarantee().getSubmitDueDate()) > 0 ){
					errorMessage = getText("operational.bg.field.bgReceivedDate.lessThan.bgSubmitDueDate");
				}
			}
		}
		
//		if(getBankGuarantee().getBgPublishDate() != null && getBankGuarantee().getSubmitDueDate() != null){
//			if(getBankGuarantee().getBgPublishDate().after(getBankGuarantee().getSubmitDueDate())){
//				if(errorMessage != null){
//					errorMessage = errorMessage + ", " + getText("operational.bg.field.bgPublishDate.moreThan.bgSubmitDueDate");
//				}else{
//					errorMessage = getText("operational.bg.field.bgPublishDate.moreThan.bgSubmitDueDate");
//				}
//			}
//		}
		
		if(getBankGuarantee().getBgPublishDate() != null && getBankGuarantee().getReceivedDate() != null){
			if(getBankGuarantee().getBgPublishDate().after(getBankGuarantee().getReceivedDate())){
				if(errorMessage != null){
					errorMessage = errorMessage + ", " + getText("operational.bg.field.bgPublishDate.moreThan.bgReceivedDate");
				}else{
					errorMessage = getText("operational.bg.field.bgPublishDate.moreThan.bgReceivedDate");
				}
			}
		}
		
		if(getBankGuarantee().getBgPublishDate() != null && getBankGuarantee().getBgBeginDate() != null){
			if(getBankGuarantee().getBgBeginDate().before(getBankGuarantee().getBgPublishDate())){
				if(errorMessage != null){
					errorMessage = errorMessage + ", " + getText("operational.bg.field.bgBeginDate.lessThan.bgPublishDate");
				}else{
					errorMessage = getText("operational.bg.field.bgBeginDate.lessThan.bgPublishDate");
				}
			}
		}
		
		if(getBankGuarantee().getBgEndDate() != null && getBankGuarantee().getDueDate() != null){
			if( getBankGuarantee().getBgEndDate().compareTo(getBankGuarantee().getDueDate()) != 0 ){
				if(errorMessage != null){
					errorMessage = errorMessage + ", " + getText("operational.bg.field.bgEndDate.notEquals.bgDueDate");
				}else{
					errorMessage = getText("operational.bg.field.bgEndDate.notEquals.bgDueDate");
				}
			}
		}
		
		if(getBankGuarantee().getBgBeginDate() != null && getBankGuarantee().getBgEndDate() != null){
			if(getBankGuarantee().getBgBeginDate().after(getBankGuarantee().getBgEndDate())){
				if(errorMessage != null){
					errorMessage = errorMessage + ", " + getText("operational.bg.field.bgBeginDate.moreThan.bgEndDate");
				}else{
					errorMessage = getText("operational.bg.field.bgBeginDate.moreThan.bgEndDate");
				}
			}
		}
		
		return errorMessage;
	}
	
	public String bankGuaranteeGeneralValidation(){
		String errorMessage = null;
		
		if(getBankGuarantee().getReceivedDate()==null){
			errorMessage = getText("operational.bg.field.bgReceivedDate.empty");
		}
		
		if(getBankGuarantee().getBgPublishDate()==null){
			errorMessage = getText("operational.bg.field.bgPublishDate.empty");
		}
		
		if(getBankGuarantee().getDueDate()==null){
			errorMessage = getText("operational.bg.field.bgDueDate.empty");
		}
		
		if(getBankGuarantee().getBgBeginDate()==null){
			errorMessage = getText("operational.bg.field.bgBeginDate.empty");
		}
		
		if(getBankGuarantee().getBgEndDate()==null){
			errorMessage = getText("operational.bg.field.bgEndDate.empty");
		}
		
		if(getBankGuarantee().getBgValueSubmitted()==null){
			errorMessage = getText("operational.bg.field.bgReceivedDate.lessThan.bgSubmitDueDate");
		}else if(getBankGuarantee().getBgValueSubmitted().compareTo(getBankGuarantee().getBgValue())!=0){
			errorMessage = getText("operational.bg.field.bgValueSubmitted.notEqual.bgValue");
		}
		
		return errorMessage;
	}
	
}
