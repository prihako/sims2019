package com.balicamp.webapp.action.operational;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.Constants;
import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.DocumentLetter;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.PdfService;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualPercentageBHPViewVariety;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualRateBHPView;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class ManageInvoiceVarietyRateView extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {
	
	public abstract void setKmDateStr(String s);
	
	public abstract String getKmDateStr();
	
	public abstract void setLicBeginDateStr(String s);
	
	public abstract String getLicBeginDateStr();
	
	public abstract void setLicEndDateStr(String s);
	
	public abstract String getLicEndDateStr();
	
	public abstract void setCurBeginDateStr(String s);
	
	public abstract String getCurBeginDateStr();
	
	public abstract void setCurEndDateStr(String s);
	
	public abstract String getCurEndDateStr();
	
	public abstract void setPaymentDueDateStr(String s);
	
	public abstract String getPaymentDueDateStr();
	
	public abstract void setInvCreatedDateStr(String s);
	
	public abstract String getInvCreatedDateStr();
	
	public abstract void setPaymentDateStr(String s);
	
	public abstract String getPaymentDateStr();
	
	public abstract void setBgSubmitDueDate(String s);
	
	public abstract String getBgSubmitDueDate();
	
	public abstract void setInvoiceCommentCancelInvoice(String s);
	public abstract String getInvoiceCommentCancelInvoice();
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	public abstract void setInvoiceCancelConfirmation(String bgSubmit);
	public abstract String getInvoiceCancelConfirmation();
	
	private PropertySelectionModel percentageBHPModel;
	
	private License license;
	
	private Invoice invoice;
	
	private ApplicationBandwidth appBandMan = null;
	
	public void setApplicationBandwidth(ApplicationBandwidth appBandMan){
		this.appBandMan = appBandMan;
	}
	
	public ApplicationBandwidth getApplicationBandwidth(){
		return appBandMan;
	}
	
	public void setLicense(License license){
		this.license = license;
	}
	
	public License getLicense(){
		return license;
	}
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public abstract void setBgNextYear(String bg);
	
	public abstract String getBgNextYear();
	
//For table
	public abstract Invoice getRow();
	
//	For Table
	public abstract BankGuarantee getBg();
	
	public abstract void setPercentageBHP(String bhp);
	
	public abstract String getPercentageBHP();
	
	public abstract void setBiRate(String bi);
	
	public abstract String getBiRate();
	
	public abstract void setBhpUpfrontFee(BigDecimal bhpUp);
	
	public abstract BigDecimal getBhpUpfrontFee();
	
	public abstract void setPaymentType(String type);
	
	public abstract String getPayementType();
	
	public abstract void setBhpMethod(String bhp);
	
	public abstract String getBhpMethod();
	
	public abstract void setInvoiceStatus(String status);
	
	public abstract String getInvoiceStatus();
	
	public abstract void setInvoiceType(String status);
	
	public abstract String getInvoiceType();
	
	public abstract void setNilaiBhp(String s);
	public abstract String getNilaiBhp();
	
	public abstract void setNilaiBhpEdit(String nilaiBhpEdit);
	public abstract String getNilaiBhpEdit();
	
	public abstract void setRemarks(String remarks);
	public abstract String getRemarks();
	
	public abstract void setRemarksEdit(String remarks);
	public abstract String getRemarksEdit();
	
	public abstract void setInvoiceComment(String invoiceComment);
	public abstract String getInvoiceComment();
	
	public abstract void setInvoiceCommentBhp(String invoiceCommentBhp);
	public abstract String getInvoiceCommentBhp();
	
	public abstract void setBhpTotalEditHasError(String bhpTotalEditHasError);
	public abstract String getBhpTotalEditHasError();
	
	public abstract void setInvBeginYear(String year);
	public abstract String getInvBeginYear();
	
//	public abstract void setHargaLelang(BigDecimal hl);
//	
//	public abstract BigDecimal getHargaLelang();
	
	public IPropertySelectionModel getPercentageBHPModel() {
		return percentageBHPModel;
	}
	
	public void setPercentageBHPModel(String year){
		HashMap<String, String> map = new HashMap<String, String>();
		
		if(year != null){
			map.put(year, year);
		}else{
			map.put("", "");
		}
		
		percentageBHPModel = new PropertySelectionModel(getLocale(), map, false, false);
	}
	
	@InjectObject("spring:bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();

	public abstract InitialInvoiceSearch getApplicationBandwidthSearch();

	@InjectObject("engine-service:page")
	public abstract IEngineService getDownloadService();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();
	
	public abstract void setInvoiceStatusCache(String stat);
	
	public abstract String getInvoiceStatusCache();
	
	public List<Invoice> getInvoiceList() {
		List<Invoice> bhpList = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		return bhpList;
	}
	
	public List<BankGuarantee> getBgList() {
		List bhpList = (List) getObjectfromSession("BG_LIST_VARIETY");
		return bhpList;
	}
	
	public ManageInvoiceVarietyRateView(){
		
	}
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		if(license.getKmDate() != null){
			setKmDateStr(dateFormat.format(license.getKmDate()));
		}
		
		if(license.getLicenceBeginDate() != null){
			setLicBeginDateStr(dateFormat.format(license.getLicenceBeginDate()));
		}
		
		if(license.getLicenceEndDate() != null){
			setLicEndDateStr(dateFormat.format(license.getLicenceEndDate()));
		}
		
		if(invoice.getInvBeginDate() != null){
			setCurBeginDateStr(dateFormat.format(invoice.getInvBeginDate()));
		}
		
		if(invoice.getInvEndDate() != null){
			setCurEndDateStr(dateFormat.format(invoice.getInvEndDate()));
		}

		if(invoice.getPaymentDueDate() != null){
			setPaymentDueDateStr(dateFormat.format(invoice.getPaymentDueDate()));
		}
		
		if(invoice.getInvCreatedDate() != null){
			setInvCreatedDateStr(dateFormat.format(invoice.getInvCreatedDate()));
		}
		
		if(invoice.getPaymentDate() != null){
			setPaymentDateStr(dateFormat.format(invoice.getPaymentDate()));
		}
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(license.getPaymentType().equalsIgnoreCase("FP")){
				setPaymentType("Full Payment");
			}
			
			if(license.getBhpMethod().equalsIgnoreCase("FR")){
				setBhpMethod("Flat BHP");
			}else if(license.getBhpMethod().equalsIgnoreCase("VR")){
				setBhpMethod("Variety Rate");
			}else{
				setBhpMethod("Conversion");
			}
			
			if(Integer.valueOf(invoice.getInvoiceType()) == 1){
				setInvoiceType("Pokok");
			}else if(Integer.valueOf(invoice.getInvoiceType()) == 2){
				setInvoiceType("Denda");
			}else if(Integer.valueOf(invoice.getInvoiceType()) == 3){
				setInvoiceType("Pokok BG");
			}else if(Integer.valueOf(invoice.getInvoiceType()) == 4){
				setInvoiceType("Pokok Sisa BG");
			} 
			
			if(invoice.getInvoiceStatus().equalsIgnoreCase("D")){
				setInvoiceStatus("Draft");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("U")){
				setInvoiceStatus("Unpaid");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("P")){
				setInvoiceStatus("Paid");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("C")){
				setInvoiceStatus("Cancel");
			}
		}
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(getFields()!=null && getFields().get("INVOICE_CANCEL")!=null){
				setInvoiceCancelConfirmation(getText("manageInvoice.invoice.cancel.confirmation"));
			}
		}
		
	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
	}
	
	public IPage doEdit(IRequestCycle cycle){
		
		ManageInvoiceVarietyRateEdit edit = (ManageInvoiceVarietyRateEdit) cycle.getPage("manageInvoiceVarietyRateEdit");
		edit.setLicense(license);
		edit.setInvoice(invoice);
		edit.setInvBeginYear(getInvBeginYear());
		VariableAnnualPercentage varAnnualBhp = null;
		if(license.getVariableAnnualPercent() != null){
			varAnnualBhp = getVariableManager().findByAnnualPercentId(license.getVariableAnnualPercent().getAnnualPercentId());
		}
		
		if(varAnnualBhp != null){
			edit.setPercentageBHPModel(String.valueOf(varAnnualBhp.getKmNo()));
		}else{
			edit.setPercentageBHPModel("belum diisi");
		}
		
//		For get Document, we must get in applicationBandwidth view, cause in license table, document not exist.
		edit.setApplicationBandwidth(appBandMan);
		edit.setBhpUpfrontFee(getBhpUpfrontFee());
		
//		For disable and enable input
		edit.setEnableInput("Y");
//		for show or hide table
		edit.setShowTable("N");
		
		edit.setBiRate(getBiRate());
		
		if(getBgNextYear() != null){
			edit.setBgYear2(getBgNextYear());
			edit.setBgSubmitDueDate(getBgSubmitDueDate());
		}
		
		getUserManager().viewManageInvoiceEdit(initUserLoginFromDatabase(), getRequest().getRemoteHost(), license.getBhpMethod(), license.getLicenceNo());
		
		//For back button 
		getFields().put("LICENSE_LIST_VR", (List) getFields().get("LICENSE_LIST_VR"));
		
		edit.setCriteria(getFields().get("criteria").toString());
		edit.setCriteriaSearch((String) getFields().get("criteriaSearch"));
		edit.setInvoiceStatusCache((String) getFields().get("invoiceStatus")); 
		
		return edit;

	}
	
	public void viewBiRate(IRequestCycle cycle){

		List<VariableAnnualRate> rates = getVariableAnnualRateManager().findByStatus(1);
		
		AnnualRateBHPView annualRateBHPEdit = (AnnualRateBHPView) cycle
				.getPage("annualRateBHPView");
		
		VariableAnnualRate rate = null;
		if(rates.size() > 0){
			rate = rates.get(0);

			annualRateBHPEdit.setAnnualRateId(rate.getAnnualRateId());
			annualRateBHPEdit.setActiveStatus(rate.getActiveStatus());
			annualRateBHPEdit.setSaveStatus(rate.getSaveStatus());
			annualRateBHPEdit.setRateValue(rate.getRateValue());
			annualRateBHPEdit.setRateYear(rate.getRateYear());
			annualRateBHPEdit.setBaseOnNote(rate.getKmNo());
			annualRateBHPEdit.setPageLocation("manageView");
			cycle.activate(annualRateBHPEdit);
		}
		
	}
	
//	public String viewBiRate(){
//		VariableAnnualRate rate = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
//		saveObjectToSession(rate, "BI_RATE_DETAIL_LIST");
//		return "bi_rate_view()";
//	}
	
	public IPage viewBhpAnnualRate(IRequestCycle cycle){
		
		if(getPercentageBHP() == null){
			String validationMsg = "Anda Harus Memilih Persentase BHP Tahunan";
			addError(getDelegate(), "errorShadow",validationMsg , ValidationConstraint.CONSISTENCY);
			return null;
		}

		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());    

		Long annualPercentId = variable.getAnnualPercentId();
		
		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentId, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentId, 2);
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentId, 3);
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentId, 4);
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentId, 5);
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentId, 6);
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentId, 7);
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentId, 8);
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentId, 9);
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentId, 10);
		
		AnnualPercentageBHPViewVariety variableView =  (AnnualPercentageBHPViewVariety) cycle
				.getPage("annualPercentageBHPViewVariety");
		
		if (variable.getVariableStatus() == 1) {
			variableView.setVariableStatus("Active");
		} else {
			variableView.setVariableStatus("Inactive");
		}
		variableView.setVariablePresentaseTahunan(String.valueOf(variable.getPercentYear()));
		variableView.setAnnualPercentId(variable.getAnnualPercentId());
		variableView.setBerdasarkanDokumen(variable.getKmNo());
		variableView.setPercentageYearOne(String.valueOf(percentageYearOne.getPercentage()));
		variableView.setPercentageYearTwo(String.valueOf(percentageYearTwo.getPercentage()));
		variableView.setPercentageYearThree(String.valueOf(percentageYearThree.getPercentage()));
		variableView.setPercentageYearFour(String.valueOf(percentageYearFour.getPercentage()));
		variableView.setPercentageYearFive(String.valueOf(percentageYearFive.getPercentage()));
		variableView.setPercentageYearSix(String.valueOf(percentageYearSix.getPercentage()));
		variableView.setPercentageYearSeven(String.valueOf(percentageYearSeven.getPercentage()));
		variableView.setPercentageYearEight(String.valueOf(percentageYearEight.getPercentage()));
		variableView.setPercentageYearNine(String.valueOf(percentageYearNine.getPercentage()));
		variableView.setPercentageYearTen(String.valueOf(percentageYearTen.getPercentage()));
		variableView.setPageLocation("initial");
		
		return variableView;
	}
	
	
//	public String doViewKmBiRate(){
//
//		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
//		VariableAnnualRate biRate = rateList.get(0);
//		
//		DocumentUpload docUp = getDocumentUploadManager().findBiRateDocument(String.valueOf(biRate.getAnnualRateId()), "BI-2" + String.valueOf(biRate.getAnnualRateId()));
//		
//		if(docUp != null){
//			File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
//			
//			if(downloadFile.exists()){
//				String url = "./document.svc?imageId=" + docUp.getFileName();
//				return url;   
//			}else{
//				String url = "./documentNotFound.html";
//				return url; 
//			} 
//		}else{
//			String url = "./documentNotFound.html";
//			return url; 
//		}
//	}
	
	public String doViewKmBiRate(){

		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		
//		check if there's no active bi rate
		if(rateList.size() > 0){
			int yearActiveBiRate = rateList.get(0).getRateYear().intValue();
			int invYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(invoice.getInvBeginDate()));
//			VariableAnnualRate biRate = rateList.get(0);
			VariableAnnualRate biRate = null;
			if(invYear < yearActiveBiRate){
				VariableAnnualRate rateTemp = getVariableAnnualRateManager().findByYear( new BigDecimal(String.valueOf(invYear)) );
				biRate = rateTemp;
			}else{
				biRate = rateList.get(0);
			}
			
			DocumentUpload docUp = getDocumentUploadManager().findBiRateDocument(String.valueOf(biRate.getAnnualRateId()), "BI-2" + String.valueOf(biRate.getAnnualRateId()));
			if(docUp != null){
				File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
				if(downloadFile.exists()){
					String url = "./document.svc?imageId=" + docUp.getFileName();
					return url;   
				}else{
					String url = "./documentNotFound.html";
					return url; 
				} 
			}else{
				String url = "./documentNotFound.html";
				return url; 
			}
		}else{
			String url = "./documentNotFound.html";
			return url; 
		}
		
	}
	
	public String doViewKmIpsfrRate(){
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(invoice.getInvoiceId()),
				String.valueOf(license.getLicenceNo()), "IN-1" + invoice.getInvoiceId());
		
		if(docUp != null){
			File downloadFile = new File(docUp.getFileDir()  + docUp.getFileName());
			
			if(downloadFile.exists()){
				String url = "./document.svc?imageId=" + docUp.getFileName();
				return url;   
			}else{
				String url = "./documentNotFound.html";
				return url; 
			} 
		}else{
			List<DocumentUpload> listDoc = getDocumentUploadManager().findDocument(String.valueOf(license.getLicenceNo()), "1");
			
			if(listDoc.size() > 0){
				int index = listDoc.size();
				DocumentUpload document = listDoc.get(index-1);
				File downloadFile2 = new File(document.getFileDir()  + document.getFileName());
				
				if(downloadFile2.exists()){
					String url = "./document.svc?imageId=" + document.getFileName();
					return url;   
				}else{
					String url = "./documentNotFound.html";
					return url; 
				} 
				
			}else{
				String url = "./documentNotFound.html";
				return url;
			}
		}
	}
	
	public String doPrint() throws IOException{
		if ( (invoice.getLetterID() != null)) {
			DocumentLetter letterDocument = getInvoiceManager().printLetterDocument(invoice.getLetterID());
			if(letterDocument != null){
				if(letterDocument.getDcitDoc() != null){
					PdfService.setPdf(letterDocument.getDcitDoc());
		
					String url = "./pdf.svc?imageId=" + invoice.getLetterID();
					System.out.println("dokumen letter URL OK "+url);
		
					return url;
				}
			}
		}
		
		String url = "./documentNotFound.html";
		System.out.println("dokumen letter URL failed "+url);

		return url;
	}
	
	public String doPrint2(){
		return null;
	}
	
	public void doCancel(IRequestCycle cycle) {
		ManageInvoiceSearch manageInvoiceSearch = (ManageInvoiceSearch) cycle
				.getPage("manageInvoiceSearch");
		
		saveObjectToSession(null, "INVOICE_LIST_VARIETY");
		
		getFields().put("LICENSE_LIST", (List) getFields().get("LICENSE_LIST_VR"));
		
		getFields().put("criteria", getCriteria());
		getFields().put("criteriaSearch", getCriteriaSearch());
		getFields().put("invoiceStatus", getInvoiceStatusCache());
		getFields().put("managePage", "test");
		
		manageInvoiceSearch.setCriteria(getCriteria());
		manageInvoiceSearch.setInvoiceStatus(getInvoiceStatusCache());
		
		if(getCriteria().equalsIgnoreCase("clientName")){
			manageInvoiceSearch.setClientName(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("clientNo")){
			manageInvoiceSearch.setClientNo(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("bhpMethod")){
			manageInvoiceSearch.setBhpMethod(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("noApp")){
			manageInvoiceSearch.setLicenceNumber(getCriteriaSearch());
		}
		
		//For cancel Invoice
		getFields().remove("INVOICE_CANCEL");

		manageInvoiceSearch.doSearch();
		cycle.activate(manageInvoiceSearch);
	}
	
	public void doCancelInvoice(IRequestCycle cycle) {
		getFields().put("INVOICE_CANCEL", "INVOICE_CANCEL");
		
	}
	
	public void doCancelInvoice2(IRequestCycle cycle) {
		
		if(invoice.getInvoiceNo() != null){
			getInvoiceManager().cancelInvoice(invoice, getInvoiceCommentCancelInvoice());
			
			invoice.setInvoiceComment(getInvoiceCommentCancelInvoice());
			invoice.setInvoiceStatus(Constants.InvoiceStatus.CANCEL);
			invoice.setIsManualCancel("Y");
			getInvoiceManager().save(invoice);
			
			getFields().remove("INVOICE_CANCEL");
			
			InfoPageCommand infoPageCommand = new InfoPageCommand();
			
			infoPageCommand.setTitle(getText("manageInvoice.invoice.cancel.confirmation.title"));
			infoPageCommand.addMessage(getText("manageInvoice.invoice.cancel.confirmation.message"));
			infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
					getText("button.finish"), "manageInvoiceSearch.html"));

			InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
			infoPage.setInfoPageCommand(infoPageCommand);

			cycle.activate(infoPage);
		}else{
			addError(getDelegate(), "errorShadow",getText("manageInvoice.invoice.cancel.invoiceNo.null") , ValidationConstraint.CONSISTENCY);
			return;
		}
		
	}
	
	public void doCancel2(IRequestCycle cycle) {
		getFields().remove("INVOICE_CANCEL");
		
	}
	
//	@EventListener(targets = "trueSaveNilaiBHPButton", events = "onclick", submitForm = "form-license") 
//	public void saveNilaiBhpFrekuensi(IRequestCycle cycle) {
//
//		// System.out.println("transactionTypeBiller called");
//		// System.out.println("Transaction Type Biller: " + getBillerCode());
//		// setResponseCodeBillerModel(getBillerCode());
//		// setTransactionCodeModel(getBillerCode());
//		// cycle.getResponseBuilder().updateComponent("transactionCodeField");
//		// cycle.getResponseBuilder().updateComponent("responseBillerCodeField");
//          
//        if (getNilaiBhpEdit()!=null){
//			System.out.println("Invoice Id = "+getInvoice().getInvoiceId()+" ;Nilai BHP = "+getNilaiBhpEdit()+" ;Invoice Type = "+getInvoiceType());
//			System.out.println("Saving Nilai Bhp Frekuensi");
//			getInvoiceManager().updateNilaiBhpFrekuensi(Long.parseLong(getNilaiBhpEdit()), getInvoice().getInvoiceId());
//			System.out.println("Saved");
//			
//        }
//	}
	
	
	public void doView(IRequestCycle cycle, Object licenceID, Object invoiceID,
			Object bhpMethod, Object yearTo, Object licenceNo,
			Object invoiceNo, Object invoiceType, Object bhptotal) {

		System.out.println("T_LICENCE_ID = " + licenceID);
		System.out.println("INVOICE_ID = " + invoiceID);
		System.out.println("BHP_METHOD = " + bhpMethod);
		System.out.println("YEAR_TO = " + yearTo);
		System.out.println("LICENCE_NO = " + licenceNo);
		System.out.println("INVOICE_NO = " + invoiceNo);
		System.out.println("INVOICE_TYPE = " + invoiceType);
		System.out.println("BHP TOTAL = " + bhptotal);

		Long invoiceId = new Long(invoiceID.toString());
		Long licenceid = new Long(licenceID.toString());
		BigDecimal invoicetipe = new BigDecimal(invoiceType.toString());

		License license = getLicenseManager().findLicenseByID(licenceID);

		List<Invoice> invoices = getInvoiceManager().findInvoiceListByID(
				invoiceId); // get an invoice using entity hibernate
		
		Invoice invoiceObject = invoices.get(0);

		List<Invoice> invoicesList = getInvoiceManager()
				.findInvoiceByLicenceID(licenceid);
		
		//Added @28-11-2014 for remove duplicate invoice fine 24
		invoicesList = getInvoiceManager().removeDuplicateInvoiceFine24(invoicesList);

		List<Invoice> invoiceFineList = new ArrayList<Invoice>();
		List<Invoice> invoiceFeeList = new ArrayList<Invoice>();

		for (Invoice inv : invoicesList) {

			if (inv.getInvoiceType().equals("1")) {
				invoiceFeeList.add(inv);
			} else if (inv.getInvoiceType().equals("2") || inv.getInvoiceType().equals("3") || inv.getInvoiceType().equals("4") 
						|| inv.getInvoiceType().equals("6")) {
				if(inv.getYearTo().intValue() == invoiceObject.getYearTo().intValue()){
					if(invoiceObject.getInvoiceType().equals("5")){
						if(inv.getInvoiceType().equals("3")){
							invoiceFineList.add(inv);
						}
					}else{
						invoiceFineList.add(inv);
					}
				}
			}
		}

		getFields().put("INVOICE_LIST", invoicesList);// data source for Table
														// BHP
		getFields().put("INVOICE_FINE", invoices);// data source for Table Fine
		getFields().put("INVOICE_FINE_LIST", invoiceFineList);// data source for
																// Table Fine

		/* Check BHP Method if Flat BHP */
		if (bhpMethod.toString().equals("FR")) {

			/* Check Invoice Type if have a Fine */
			if (invoicetipe.intValue() == 2 || (invoicetipe.intValue() == 3) || 
					(invoicetipe.intValue() == 4) || (invoicetipe.intValue() == 5)|| (invoicetipe.intValue() == 6)) {
				System.out.println("HALAMAN DENDA FLAT BHP = ");

				ManageInvoiceFlatFine fine = (ManageInvoiceFlatFine) cycle
						.getPage("manageInvoiceFlatFine");
				fine.setLicenceID(licenceid);
				fine.setInvoiceID(invoiceId);
				fine.setLicenceNumber(licenceNo.toString());
				fine.setClientName(license.getClientName());
				fine.setClientNO(license.getClientNo().toString());
				fine.setBhpMethod(bhpMethod.toString());
				fine.setMethodBHP("Flat BHP");
				fine.setKmNo(license.getKmNo());
				fine.setKmDate(DateUtil.convertDateToString(
						license.getKmDate(), "dd-MMM-yyyy"));
				if(license.getZoneNo() != null){
					fine.setZoneNo(new BigDecimal(license.getZoneNo()));
				}
				fine.setZoneName(license.getZoneName());
				if(license.getLicenceBeginDate() != null){
					fine.setLicenceBeginDate(DateUtil.convertDateToString(
							license.getLicenceBeginDate(), "dd-MMM-yyyy"));
				}
				if(license.getLicenceEndDate() != null){
					fine.setLicenceEndDate(DateUtil.convertDateToString(
							license.getLicenceEndDate(), "dd-MMM-yyyy"));
				}
				fine.setFreqRMin(license.getFreqRMin());
				fine.setFreqRMax(license.getFreqRMax());
				fine.setFreqTMin(license.getFreqTMin());
				fine.setFreqTMax(license.getFreqTMax());
				fine.setPaymentType(license.getPaymentType());
				
				if(invoiceObject.getPaymentDueDate() != null){
					fine.setPaymentDueDate(DateUtil.convertDateToString(
							invoiceObject.getPaymentDueDate(), "dd-MMM-yyyy"));
				}
				
				fine.setYearTo((BigDecimal) yearTo);
				fine.setMonthTo(invoiceObject.getMonthTo());
				fine.setYear(DateUtil.convertDateToString(
						invoiceObject.getInvBeginDate(), "yyyy"));
				if (license.getPaymentType().equals("FP")) {
					fine.setPaymentTypeMethod("Full Payment");

				} else if (license.getPaymentType().equals("SP")) {
					fine.setPaymentTypeMethod("Stage Payment");

				}

				fine.setBgAvailableStatus(license.getBgAvailableStatus());
				if (license.getBgAvailableStatus() == null) {
					fine.setBgSubmitDueDate(null);
				} else if (license.getBgAvailableStatus().equalsIgnoreCase("Y")) {
					BankGuarantee bg = getBankGuaranteeManager().findByInvoiceNo(invoiceNo.toString());
//					BankGuarantee bg = getBankGuaranteeManager().findBgNextYear(license.getTLicenceId(), invoiceObject.getYearTo());
					if(bg==null){
						bg = getBankGuaranteeManager().findBgNextYear(license.getTLicenceId(), invoiceObject.getYearTo().subtract(new BigDecimal("1")));
					}
					fine.setBgSubmitDueDate(DateUtil.convertDateToString(bg.getSubmitDueDate(), "dd-MMM-yyyy"));
					fine.setBankGuarantee(bg);
				}

				for (Invoice invoice : invoicesList) {

					if (invoice.getYearTo().intValue() == 1) {
						fine.setBhpUpfrontFee(invoice.getBhpUpfrontFee());
						fine.setBhpTotal(invoice.getBhpAnnual());
						fine.setBhpHl(invoice.getBhpHl());
					}else{
						fine.setBhpTotal(invoiceObject.getBhpAnnual());
					}
					
				}
				
				fine.setBhpPhl(invoiceObject.getBhpPhl());
				fine.setBhpAnnual(invoiceObject.getBhpAnnual());
				fine.setBgTotal(invoiceObject.getBgTotal());
				fine.setInvoiceNo(invoiceObject.getInvoiceNo());
				fine.setInvoiceStatus(invoiceObject.getInvoiceStatus());
				if (invoiceObject.getInvoiceStatus().equals("D")) {
					fine.setInvoiceStatusText("Draft");
				} else if (invoiceObject.getInvoiceStatus().equals("U")) {
					fine.setInvoiceStatusText("Unpaid");

				} else if (invoiceObject.getInvoiceStatus().equals("P")) {
					fine.setInvoiceStatusText("Paid");

				} else if (invoiceObject.getInvoiceStatus().equals("C")) {
					fine.setInvoiceStatusText("Cancel");

				} else if (invoiceObject.getInvoiceStatus().equals("BD")) {
					fine.setInvoiceStatusText("Bad Debt");

				}
				if(invoiceObject.getInvBeginDate() != null){
					fine.setInvoiceBeginDate(DateUtil.convertDateToString(
							invoiceObject.getInvBeginDate(), "dd-MMM-yyyy"));
				}
				if(invoiceObject.getInvCreatedDate() != null){
					fine.setInvoiceCreateDate(DateUtil.convertDateToString(
							invoiceObject.getInvCreatedDate(), "dd-MMM-yyyy"));
				}
				fine.setInvoiceType(invoiceObject.getInvoiceType());
				if (invoiceObject.getInvoiceType().equals("1")) {
					fine.setInvoiceTypeText("Pokok");
				} else if (invoiceObject.getInvoiceType().equals("2")) {
					fine.setInvoiceTypeText("Pokok + Denda");
				} else if (invoiceObject.getInvoiceType().equals("3")) {
					fine.setInvoiceTypeText("Pokok BG");
				} else if (invoiceObject.getInvoiceType().equals("4")) {
					fine.setInvoiceTypeText("Pokok + Denda");
				} else if (invoiceObject.getInvoiceType().equals("5")) {
					fine.setInvoiceTypeText("Pokok Selisih BG");
				} else if (invoiceObject.getInvoiceType().equals("6")) {
					fine.setInvoiceTypeText("Pokok + Denda Selisih BG");
				}

				for (Invoice obj : invoiceFeeList) {

					if (obj.getYearTo().equals(invoiceObject.getYearTo())) {
						if(obj.getInvBeginDate() != null){
							fine.setCurrentBeginDate(DateUtil.convertDateToString(
									obj.getInvBeginDate(), "dd-MMM-yyyy"));
						}
						if(obj.getInvEndDate() != null){
							fine.setCurrentEndDate(DateUtil.convertDateToString(
									obj.getInvEndDate(), "dd-MMM-yyyy"));
						}
						if(obj.getPaymentDueDate() != null){
							fine.setPaymentFineDueDate(DateUtil
									.convertDateToString(obj.getPaymentDueDate(),
											"dd-MMM-yyyy")); 
						}
					}
				}

				if (invoiceObject.getBhpTotal() == null) {
					fine.setPaymentAmount(null);

				} else {
					if(invoiceObject.getInvoiceType().equals("5")){
						fine.setPaymentAmount(invoiceFineList.get(0).getBhpTotal());
					}else{
						fine.setPaymentAmount(invoiceObject.getBhpTotal());
					}
				}
				
				fine.setLetterID(invoiceObject.getLetterID());
				fine.setSaveStatus(invoiceObject.getSaveStatus().toString());
				fine.setFields(getFields());
				
				fine.setInvoice(invoiceObject);
				
				getUserManager().viewManageInvoiceView(initUserLoginFromDatabase(), getRequest().getRemoteHost(), license.getBhpMethod(), license.getLicenceNo(), true);

//				getFields().remove("LICENSE_LIST");
				
				//For back button  
//				fine.setApplicationList((List) getFields().get("LICENSE_LIST"));
				getFields().put("LICENSE_LIST_FR", (List) getFields().get("LICENSE_LIST"));
			
				fine.setCriteria(getFields().get("criteria").toString());
				fine.setCriteriaSearch((String) getFields().get("criteriaSearch"));
				fine.setInvoiceStatusCache((String) getFields().get("invoiceStatus"));

				cycle.activate(fine);

			} else if(invoicetipe.intValue() == 1) {
				System.out.println("HALAMAN POKOK FLAT BHP = ");

				ManageInvoiceFlatView view = (ManageInvoiceFlatView) cycle
						.getPage("manageInvoiceFlatView");
				view.setLicenceID(licenceid);
				view.setInvoiceID(invoiceId);
				view.setLicenceNumber(licenceNo.toString());
				view.setClientName(license.getClientName());
				view.setClientNO(license.getClientNo().toString());
				view.setBhpMethod(bhpMethod.toString());
				view.setMethodBHP("Flat BHP");
				view.setKmNo(license.getKmNo());
				if(license.getKmDate() != null){
					view.setKmDate(DateUtil.convertDateToString(
							license.getKmDate(), "dd-MMM-yyyy"));
				}
				if(license.getZoneNo() != null){
					view.setZoneNo(new BigDecimal(license.getZoneNo()));
				}
				view.setZoneName(license.getZoneName());
				if(license.getLicenceBeginDate() != null){
					view.setLicenceBeginDate(DateUtil.convertDateToString(
							license.getLicenceBeginDate(), "dd-MMM-yyyy"));
				}
				if(license.getLicenceEndDate() != null){
					view.setLicenceEndDate(DateUtil.convertDateToString(
							license.getLicenceEndDate(), "dd-MMM-yyyy"));
				}
				view.setFreqRMin(license.getFreqRMin());
				view.setFreqRMax(license.getFreqRMax());
				view.setFreqTMin(license.getFreqTMin());
				view.setFreqTMax(license.getFreqTMax());
				view.setPaymentType(license.getPaymentType());
				if(invoiceObject.getPaymentDueDate() != null){
					view.setPaymentDueDate(DateUtil.convertDateToString(
							invoiceObject.getPaymentDueDate(), "dd-MMM-yyyy"));
				}
				if(invoiceObject.getInvBeginDate() != null){
					view.setCurrentBeginDate(DateUtil.convertDateToString(
							invoiceObject.getInvBeginDate(), "dd-MMM-yyyy"));
				}
				if(invoiceObject.getInvEndDate() != null){
					view.setCurrentEndDate(DateUtil.convertDateToString(
							invoiceObject.getInvEndDate(), "dd-MMM-yyyy"));
				}
				view.setYearTo((BigDecimal) yearTo);
				if(invoiceObject.getInvBeginDate() != null){
					view.setYear(DateUtil.convertDateToString(
							invoiceObject.getInvBeginDate(), "yyyy"));
				}
				if (license.getPaymentType().equals("FP")) {
					view.setPaymentTypeMethod("Full Payment");

				} else if (license.getPaymentType().equals("SP")) {
					view.setPaymentTypeMethod("Stage Payment");

				}
				view.setBgAvailableStatus(license.getBgAvailableStatus());
				 if (license.getBgDueDate() == null) {
//					 view.setBgDueDate(null);
					 view.setBgSubmitDueDate(null);
				 } else {
//					 view.setBgDueDate(license.getBgDueDate());
					 BankGuarantee bg = getBankGuaranteeManager().findBgNextYear(license.getTLicenceId(), invoiceObject.getYearTo());
					 view.setBgSubmitDueDate(DateUtil.convertDateToString(bg.getSubmitDueDate(), "dd-MMM-yyyy"));
					 view.setBgTotal(bg.getBgValue());
					 List<BankGuarantee> ListBgFR = getBankGuaranteeManager().findByLicenseId(licenceid);
					 getFields().put("BANK_GUARANTEE_LIST_FR", ListBgFR);
				 }

				BigDecimal upfrontfee = null;
				BigDecimal auctionFee = null;

				List<Invoice> listInvoices = getInvoiceManager()
						.findInvoiceByLicenceID(licenceid); // get an invoice
															// using entity
															// hibernate
				for (Invoice obj : listInvoices) {
					if (obj.getYearTo().intValue() == 1) {
						upfrontfee = obj.getBhpUpfrontFee();
						auctionFee = obj.getBhpHl();
					}
				}

				System.out.println("UPFRONT FEE TAHUN PERTAMA : " + upfrontfee);
				System.out.println("HARGA LELANG TAHUN PERTAMA : " + auctionFee);

				view.setBhpUpfrontFee(upfrontfee);
				view.setBhpHl(auctionFee);
				view.setBhpPhl(invoiceObject.getBhpPhl());
				view.setBhpAnnual(invoiceObject.getBhpAnnual());
				// view.setBgTotal(invoiceObject.getBgTotal());
				view.setBhpTotal(invoiceObject.getBhpTotal());
				view.setInvoiceNo(invoiceObject.getInvoiceNo());
				view.setInvoiceStatus(invoiceObject.getInvoiceStatus());
				view.setInvoiceComment(invoiceObject.getInvoiceComment());
				if (invoiceObject.getInvoiceStatus().equals("D")) {
					view.setInvoiceStatusText("Draft");
				} else if (invoiceObject.getInvoiceStatus().equals("U")) {
					view.setInvoiceStatusText("Unpaid");

				} else if (invoiceObject.getInvoiceStatus().equals("P")) {
					view.setInvoiceStatusText("Paid");

				} else if (invoiceObject.getInvoiceStatus().equals("C")) {
					view.setInvoiceStatusText("Cancel");

				} else if (invoiceObject.getInvoiceStatus().equals("BD")) {
					view.setInvoiceStatusText("Bad Debt");

				}
				
				view.setInvoiceType(invoiceObject.getInvoiceType());
				if (invoiceObject.getInvoiceType().equals("1")) {
					view.setInvoiceTypeText("Pokok");
				} else if (invoiceObject.getInvoiceType().equals("2")) {
					view.setInvoiceTypeText("Pokok + Denda");
				} else if (invoiceObject.getInvoiceType().equals("3")) {
					view.setInvoiceTypeText("Pokok BG");
				} else if (invoiceObject.getInvoiceType().equals("4")) {
					view.setInvoiceTypeText("Pokok + Denda");
				} else if (invoiceObject.getInvoiceType().equals("5")) {
					view.setInvoiceTypeText("Pokok Selisih BG");
				} else if (invoiceObject.getInvoiceType().equals("6")) {
					view.setInvoiceTypeText("Pokok + Denda Selisih BG");
				}

				view.setInvoiceCreateDate(DateUtil.convertDateToString(
						invoiceObject.getInvCreatedDate(), "dd-MMM-yyyy"));
				if(invoiceObject.getPaymentDate() != null){
					view.setPaymentDate(DateUtil.convertDateToString(
							invoiceObject.getPaymentDate(), "dd-MMM-yyyy"));
				}
				view.setSaveStatus(invoiceObject.getSaveStatus().toString());
				view.setLetterID(invoiceObject.getLetterID());
				view.setFields(getFields());
				
				getUserManager().viewManageInvoiceView(initUserLoginFromDatabase(), getRequest().getRemoteHost(), license.getBhpMethod(), license.getLicenceNo(), false);

				//For back button 
				getFields().put("LICENSE_LIST_FR", (List) getFields().get("LICENSE_LIST"));
				
				view.setCriteria(getFields().get("criteria").toString());
				view.setCriteriaSearch((String) getFields().get("criteriaSearch"));
				view.setInvoiceStatusCache((String) getFields().get("invoiceStatus"));

				cycle.activate(view);
			}

		} else if (bhpMethod.toString().equals("VR")) {

			Long yearto = new Long(yearTo.toString());
			
			License licenseLocal = getLicenseManager().findLicenseByID(licenceID);
			Invoice invoice = getInvoiceManager().findInvoiceByID(invoiceId);
			
//			check if invoiceBeginDate <= bi rate active
//			List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
//			VariableAnnualRate rate = rateList.get(0);
//			Integer biYear = rate.getRateYear().intValue();
//			SimpleDateFormat format = new SimpleDateFormat("yyyy");
//			Integer invYear = Integer.valueOf(format.format(invoice.getInvBeginDate()));
//			if(invYear > biYear){
//				String errorMessage = getText("initial.invoice.invYear.biRate.notActive");
//				addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
//				return;
//			}else{
//				VariableAnnualRate rateCurrent = getVariableAnnualRateManager().findByYear(new BigDecimal(String.valueOf(invYear)));
//				if(rateCurrent == null){
//					addError(getDelegate(), "errorShadow", "Bi Rate Untuk Tahun Invoice Berjalan Tidak Ada", ValidationConstraint.CONSISTENCY);
//					return;
//				}
//			}
			
			List<Invoice> invoiceVariety = getInvoiceManager().findInvoiceByLicenceID(licenceid);
			
			//Added @28-11-2014 for remove duplicate invoice fine 24
			invoiceVariety = getInvoiceManager().removeDuplicateInvoiceFine24(invoiceVariety);
			
			List<Invoice> invoicePokokVariety = new ArrayList<Invoice>();
			List<Invoice> invoiceDendaVariety = new ArrayList<Invoice>();
			for (Invoice inv3 : invoiceVariety){
				if (inv3.getInvoiceType().equals("1")){
					invoicePokokVariety.add(inv3);
				}else if (inv3.getInvoiceType().equals("2") || inv3.getInvoiceType().equals("3") || inv3.getInvoiceType().equals("4")){
					if((inv3.getYearTo().intValue()) == (invoice.getYearTo().intValue())){
						invoiceDendaVariety.add(inv3);
					}
				}
			}
			saveObjectToSession(invoicePokokVariety, "INVOICE_LIST_VARIETY");
			
			List<BankGuarantee> bgList = getBankGuaranteeManager().findByLicenseId(licenceid);
			saveObjectToSession(bgList, "BG_LIST_VARIETY");
			
//			BankGuarantee bg = getBankGuaranteeManager().findBgByInvoiceId(invoiceId);
			
			BankGuarantee bgNextYear = null;
			if(licenseLocal.getBgAvailableStatus().equalsIgnoreCase("Y")){
				bgNextYear = getBankGuaranteeManager().findBgNextYear(licenseLocal.getTLicenceId(), invoice.getYearTo());
			}
			
//			get invoice type 
			Integer tipeInvoice = Integer.valueOf(invoice.getInvoiceType());
			
			if(tipeInvoice == 1){
				ManageInvoiceVarietyRateView view = (ManageInvoiceVarietyRateView) cycle.getPage("manageInvoiceVarietyRateView");
				view.setLicense(licenseLocal);
				view.setInvoice(invoice);
				
//				VariableAnnualRate rate = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
//				if(rate != null){
//					view.setBiRate(String.valueOf(rate.getRateValue()));
//					saveObjectToSession(rate, "BI_RATE_DETAIL_LIST");
//				}
 
//				Find active bi rate 14-07-2014
				List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
				if(rateList.size() > 0){
					VariableAnnualRate rate = rateList.get(0);
					Integer yearActive = rate.getRateYear().intValue();
					Integer invYear = Integer.valueOf(invoice.getYears());
					
					if(invYear >= yearActive){
						view.setBiRate(String.valueOf(rate.getRateValue()));
						saveObjectToSession(rate, "BI_RATE_DETAIL_LIST");
					}else{
						VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
						view.setBiRate(String.valueOf(rate2.getRateValue()));
						saveObjectToSession(rate2, "BI_RATE_DETAIL_LIST");
					}
					
				}
				
				if(bgNextYear != null){
					view.setBgNextYear(String.valueOf(bgNextYear.getBgValue()));
					view.setBgSubmitDueDate(DateUtil.convertDateToString(bgNextYear.getSubmitDueDate(), "dd-MMM-yyyy"));
				}
				
				VariableAnnualPercentage varAnnualBhp = null;
				if(licenseLocal.getVariableAnnualPercent() != null){
					varAnnualBhp = getVariableManager().findByAnnualPercentId(licenseLocal.getVariableAnnualPercent().getAnnualPercentId());
				}
				
				if(varAnnualBhp != null){
					view.setPercentageBHPModel(String.valueOf(varAnnualBhp.getKmNo()));
				}else{
					view.setPercentageBHPModel("belum diisi");
				}
				
				Date invDate = invoice.getInvBeginDate();
				view.setInvBeginYear(new SimpleDateFormat("yyyy").format(invDate));
				
	//			setUpfrontFee
				Invoice invUpFrontFee = getInvoiceManager().searchByYearTo(invoice.getTLicence().getTLicenceId(), new BigDecimal("1"));
				view.setBhpUpfrontFee(invUpFrontFee.getBhpUpfrontFee());
				 
	//			For get Document, we must get in applicationBandwidth table, cause in license table, document not exist.
				ApplicationBandwidth appBandMan = getApplicationBandwidthManager().findByLicenceNo(licenceNo.toString());
				
				view.setApplicationBandwidth(appBandMan);
				
				//Penambahan 03-06-2014
				Long annualPercentId = licenseLocal.getVariableAnnualPercent().getAnnualPercentId();
				VariableAnnualPercentage variableAnnual = getVariableManager().findByAnnualPercentId(annualPercentId);
				List<VariableAnnualPercentageDetail> variableDetail = getVariableDetailManager().findByAnnualPercentId(annualPercentId);
				
//				if( ((invoice.getYearTo().intValue() == 1) && (!invoice.getInvoiceStatus().equalsIgnoreCase("S"))) || (invoice.getYearTo().intValue() > 1) ){
//					view.setHargaLelang(invoice.getBhpHl());
//				}
				
				saveObjectToSession(variableAnnual, "ANNUAL_PERCENTAGE");
				saveObjectToSession(variableDetail, "ANNUAL_PERCETAGE_DETAIL_LIST");
				
				getUserManager().viewManageInvoiceView(initUserLoginFromDatabase(), getRequest().getRemoteHost(), licenseLocal.getBhpMethod(), licenseLocal.getLicenceNo(), false);
				
				//For back button 
//				view.setApplicationList((List) getFields().get("LICENSE_LIST"));
				getFields().put("LICENSE_LIST_VR", (List) getFields().get("LICENSE_LIST"));
			
				view.setCriteria(getFields().get("criteria").toString());
				view.setCriteriaSearch((String) getFields().get("criteriaSearch"));
				view.setInvoiceStatusCache((String) getFields().get("invoiceStatus"));
				
				cycle.activate(view);
				
			}else if( (tipeInvoice == 2) || (tipeInvoice == 3) || (tipeInvoice == 4) || (tipeInvoice == 5) || (tipeInvoice == 6) ){
				saveObjectToSession(invoiceDendaVariety, "INVOICE_LIST_VARIETY_FINE");
				ManageInvoiceVarietyRateFine view = (ManageInvoiceVarietyRateFine) cycle.getPage("manageInvoiceVarietyRateFine");
				view.setLicense(licenseLocal);
				view.setInvoice(invoice);
				
				//FInd invoice Pokok
				Invoice invoicePokok = getInvoiceManager().getInvoicePokok(invoice.getTLicence().getTLicenceId(), "1", invoice.getYearTo());
				if(invoicePokok != null){
					view.setInvoicePokok(invoicePokok);
					System.out.println("TEst Invoice pokok : " + invoice.getInvoiceType());
				}else{
					addError(getDelegate(), "errorShadow", getText("manageInvoiceSearch.invoiceFine.notGenerated"), ValidationConstraint.CONSISTENCY);
					return;
				}
				
				VariableAnnualPercentage varAnnualBhp = null;
				if(licenseLocal.getVariableAnnualPercent() != null){
					varAnnualBhp = getVariableManager().findByAnnualPercentId(licenseLocal.getVariableAnnualPercent().getAnnualPercentId());
				}
				
				if(varAnnualBhp != null){
					view.setPercentageBHPModel(String.valueOf(varAnnualBhp.getKmNo()));
				}else{
					view.setPercentageBHPModel("belum diisi");
				}
				
	//			setUpfrontFee
				Invoice invUpFrontFee = getInvoiceManager().searchByYearTo(invoice.getTLicence().getTLicenceId(), new BigDecimal("1"));
				view.setBhpUpfrontFee(invUpFrontFee.getBhpUpfrontFee());
				 
	//			For get Document, we must get in applicationBandwidth table, cause in license table, document not exist.
				ApplicationBandwidth appBandMan = getApplicationBandwidthManager().findByLicenceNo(licenceNo.toString());
				view.setApplicationBandwidth(appBandMan);
				
				//Penambahan 03-06-2014
				Long annualPercentId = licenseLocal.getVariableAnnualPercent().getAnnualPercentId();
				VariableAnnualPercentage variableAnnual = getVariableManager().findByAnnualPercentId(annualPercentId);
				List<VariableAnnualPercentageDetail> variableDetail = getVariableDetailManager().findByAnnualPercentId(annualPercentId);
				saveObjectToSession(variableAnnual, "ANNUAL_PERCENTAGE");
				saveObjectToSession(variableDetail, "ANNUAL_PERCETAGE_DETAIL_LIST");
				
				getUserManager().viewManageInvoiceView(initUserLoginFromDatabase(), getRequest().getRemoteHost(), licenseLocal.getBhpMethod(), licenseLocal.getLicenceNo(), true);
				
				//For back button 
				getFields().put("LICENSE_LIST_VR", (List) getFields().get("LICENSE_LIST"));
				
				view.setCriteria(getFields().get("criteria").toString());
				view.setCriteriaSearch((String) getFields().get("criteriaSearch"));
				view.setInvoiceStatusCache((String) getFields().get("invoiceStatus")); 
				
				if(licenseLocal.getBgAvailableStatus().equalsIgnoreCase("Y")){
					BankGuarantee currentBankGuarantee = getBankGuaranteeManager().findBgNextYear(licenseLocal.getTLicenceId(), invoice.getYearTo().add(new BigDecimal("-1")));
					view.setBgSubmitDueDate(DateUtil.convertDateToString(currentBankGuarantee.getSubmitDueDate(), "dd-MMM-yyyy"));
					getFields().put("BANK_GUARANTEE_FINE", currentBankGuarantee);
				}
				
				cycle.activate(view);
			}
		}

	}
	
public void doSaveBhpTotal(IRequestCycle cycle) {
		
//		if (getNilaiBhpEdit()!=null){
//					System.out.println("Invoice Id = "+getInvoice().getInvoiceId()+" ;Nilai BHP = "+getNilaiBhpEdit()+" ;Invoice Type = "+getInvoiceType());
//					System.out.println("Saving Nilai Bhp Frekuensi");
//					if(getInvoiceManager().updateNilaiBhpFrekuensi(Long.parseLong(getNilaiBhpEdit()), getInvoice().getInvoiceId())>0){
//						getInvoice().setBhpTotal(new BigDecimal(getNilaiBhpEdit()));
//					}
//					
//					System.out.println("Saved");
//					
//		 }
		
		if (validasiBhpTotalEdit(getNilaiBhpEdit())==0&&validasiRemarks(getRemarksEdit())==0){
			setBhpTotalEditHasError(null);
			getInvoiceManager().updateNilaiBhpFrekuensi(Long.parseLong(getNilaiBhpEdit()), getInvoice().getInvoiceId(), getRemarksEdit());
			getInvoice().setBhpTotal(new BigDecimal(Long.parseLong(getNilaiBhpEdit())));
			getInvoice().setInvoiceCommentBhp(getRemarksEdit());
			addError(getDelegate(), "bhpTotalEditHasError","Nilai BHP Frekuensi Baru Berhasil Tersimpan" , ValidationConstraint.CONSISTENCY);
		}else{
			setBhpTotalEditHasError("ERROR");
		}
		getUserManager().viewManageInvoiceView(initUserLoginFromDatabase(), getRequest().getRemoteHost(),getBhpMethod(), getLicense().getLicenceNo(), true);
	}
	
	
	public int validasiBhpTotalEdit(String bhpTotalEdit) {
		int errorTotal = 0;
		if (bhpTotalEdit==null){
			addError(getDelegate(), "bhpTotalEditErrorNotif",getText("manageInvoice.invoice.editBhpTotal.notification.bhpValue.isNull") , ValidationConstraint.CONSISTENCY);
			return 1;
		}else
			if (!NumberUtils.isNumber(bhpTotalEdit)){
				addError(getDelegate(), "bhpTotalEditErrorNotif",getText("manageInvoice.invoice.editBhpTotal.notification.bhpValue.notNumber") , ValidationConstraint.CONSISTENCY);
				errorTotal++;
			}else
			
			if (new BigDecimal(Long.parseLong(bhpTotalEdit)).equals(getInvoice().getBhpTotal())){
				addError(getDelegate(), "bhpTotalEditErrorNotif",getText("manageInvoice.invoice.editBhpTotal.notification.bhpValue.isNotChanged") , ValidationConstraint.CONSISTENCY);
				errorTotal++;
			}
		return errorTotal;
					
	}
	
	public int validasiRemarks(String remarks) {
		int errorTotal = 0;
		if (remarks==null){
			addError(getDelegate(), "bhpTotalEditErrorNotif",getText("manageInvoice.invoice.editBhpTotal.notification.remarks.isNull") , ValidationConstraint.CONSISTENCY);
			errorTotal++;
		}
		return errorTotal;
	}

}
