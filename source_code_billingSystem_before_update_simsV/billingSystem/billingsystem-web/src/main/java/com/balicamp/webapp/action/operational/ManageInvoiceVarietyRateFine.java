package com.balicamp.webapp.action.operational;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
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
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.PdfService;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualPercentageBHPViewVariety;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualRateBHPView;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class ManageInvoiceVarietyRateFine extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
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
	
	public abstract void setInvCreatedDateFineStr(String s);
	public abstract String getInvCreatedDateFineStr();
	
	public abstract void setPaymentDateFineStr(String s);
	public abstract String getPaymentDateFineStr();
	
	public abstract void setBgSubmitDueDate(String s);
	public abstract String getBgSubmitDueDate();
	
	public abstract void setInvoiceCancelConfirmation(String bgSubmit);
	public abstract String getInvoiceCancelConfirmation();
	
	public abstract void setInvoiceCommentCancelInvoice(String s);
	public abstract String getInvoiceCommentCancelInvoice();
	
	public BankGuarantee getBankGuarantee(){
		return (BankGuarantee) getFields().get("BANK_GUARANTEE_FINE");
	}
	
	public List<BankGuarantee> getBankGuaranteeList(){
		List<BankGuarantee> bgList = null;
		if(getFields().get("BANK_GUARANTEE_FINE") != null){
			bgList = new ArrayList<BankGuarantee>();
			bgList.add((BankGuarantee) getFields().get("BANK_GUARANTEE_FINE"));
		}
		return bgList;
	}
	
	private PropertySelectionModel percentageBHPModel;
	
	private License license;
	
	private Invoice invoice;
	
	private ApplicationBandwidth appBandMan = null;
	
	private BigDecimal bhpUpfrontFee;
	
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
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
	//For table
	public abstract Invoice getRow();
	
	//For table
	public abstract Invoice getFineRow();
	
	public abstract BankGuarantee getBankGuaranteeRow();
	
//	For Table
	public abstract BankGuarantee getBg();
	
	public abstract void setPercentageBHP(String bhp);
	
	public abstract String getPercentageBHP();
	
	public IPropertySelectionModel getPercentageBHPModel() {
		return percentageBHPModel;
	}
	
	public void setPercentageBHPModel(String year){
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		if(year != null){
			map.put(year, year);
		}else{
			map.put("", "");
		}
		
		percentageBHPModel = new PropertySelectionModel(getLocale(), map, false, false);
	}
	
	@InjectObject("spring:bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();

	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();

	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();

	public abstract InitialInvoiceSearch getApplicationBandwidthSearch();

	@InjectObject("engine-service:page")
	public abstract IEngineService getDownloadService();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	public List<Invoice> getInvoiceList() {
		List<Invoice> bhpList = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		return bhpList;
	}
	
	public List<Invoice> getInvoiceListFine() {
		List<Invoice> bhpList = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY_FINE");
		return bhpList;
	}
	
	public ManageInvoiceVarietyRateFine(){
		
	}
	
	public abstract void setIpsfrYearOne(String ipsfr);
	
	public abstract String  getIpsfrYearOne();
	
	public abstract void setBhpValueRadioFreq(String value);
	
	public abstract String getBhpValueRadioFreq();
	
	public abstract void setBgYear2(String bg);
	
	public abstract String getBgYear2();
	
	public abstract void setIpsfrNo(String no);
	
	public abstract String getIpsfrNo();
	
	public void setBhpUpfrontFee(BigDecimal bhpUp){
		bhpUpfrontFee = bhpUp;
	}
	
	public BigDecimal getBhpUpfrontFee(){
		return bhpUpfrontFee;
	}
	
	public abstract void setPaymentType(String type);
	
	public abstract String getPayementType();
	
	public abstract void setBhpMethod(String bhp);
	
	public abstract String getBhpMethod();
	
	public abstract void setInvoiceStatus(String status);
	
	public abstract String getInvoiceStatus();
	
	public abstract void setInvoiceType(String status);
	
	public abstract String getInvoiceType();
	
	public abstract void setInvoicePokok(Invoice inv);
	
	public abstract Invoice getInvoicePokok();
	
	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();
	
	public abstract void setInvoiceStatusCache(String stat);
	
	public abstract String getInvoiceStatusCache();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		if(getFields()==null){
			setFields(new HashMap());
		}
		
		if(license.getKmDate() != null){
			setKmDateStr(dateFormat.format(license.getKmDate()));
		}
		
		if(license.getLicenceBeginDate() != null){
			setLicBeginDateStr(dateFormat.format(license.getLicenceBeginDate()));
		}
		
		if(license.getLicenceEndDate() != null){
			setLicEndDateStr(dateFormat.format(license.getLicenceEndDate()));
		}
		
		if(getInvoicePokok() != null){
			if(getInvoicePokok().getInvBeginDate() != null){
				setCurBeginDateStr(dateFormat.format(getInvoicePokok().getInvBeginDate()));
			}
		}
		
		if(getInvoicePokok() != null){
			if(getInvoicePokok().getInvEndDate() != null){
				setCurEndDateStr(dateFormat.format(getInvoicePokok().getInvEndDate()));
			}
		}
		
		if(getInvoicePokok() != null){
			if(getInvoicePokok().getPaymentDueDate() != null){
				setPaymentDueDateStr(dateFormat.format(getInvoicePokok().getPaymentDueDate()));
			}
		}
		
		if(getInvoicePokok() != null){
			if(getInvoicePokok().getInvCreatedDate() != null){
				setInvCreatedDateStr(dateFormat.format(getInvoicePokok().getInvCreatedDate()));
			}
		}
		
		if(getInvoicePokok() != null){
			if(getInvoicePokok().getPaymentDate() != null){
				setPaymentDateStr(dateFormat.format(getInvoicePokok().getPaymentDate()));
			}
		}
		
		if(invoice.getInvBeginDate() != null){
			setInvCreatedDateFineStr(dateFormat.format(invoice.getInvBeginDate()));
		}
		
		if(invoice.getPaymentDueDate() != null){
			setPaymentDateFineStr(dateFormat.format(invoice.getPaymentDueDate()));
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
				setInvoiceType("Denda");
			}else if(Integer.valueOf(invoice.getInvoiceType()) == 5){
				setInvoiceType("Pokok Selisih BG");
			}else if(Integer.valueOf(invoice.getInvoiceType()) == 6){
				setInvoiceType("Denda Selisih BG");
			} 
			
			if(invoice.getInvoiceStatus().equalsIgnoreCase("D")){
				setInvoiceStatus("Draft");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("U")){
				setInvoiceStatus("Unpaid");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("P")){
				setInvoiceStatus("Paid");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("C")){
				setInvoiceStatus("Cancel");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("BD")){
				setInvoiceStatus("Bad Debt");
			}
		}
		
		if(getInvoicePokok() == null){
			setInvoicePokok(new Invoice());
		}
		
	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
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
			annualRateBHPEdit.setPageLocation("manageEdit");
			cycle.activate(annualRateBHPEdit);
		}
		
	}
	
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
	
	public String viewAnnualPercentageBhp(){
		if(getPercentageBHP() != null){
			VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());
			List<VariableAnnualPercentageDetail> variableDetail = getVariableDetailManager().findByAnnualPercentId(variable.getAnnualPercentId());
			saveObjectToSession(variable, "ANNUAL_PERCENTAGE");
			saveObjectToSession(variableDetail, "ANNUAL_PERCETAGE_DETAIL_LIST");
		}
		
		return "annual_percentage_view()";
	}
	
//	private static final String DIRECTORY_FILE = "C:\\Program Files\\JBoss.org\\JBoss Web 2.1\\webapps\\SKM\\";
//	
//	public String getServiceURL(){
//
//		if(appBandMan != null){
//			ILink link = this.getDownloadService().getLink(false, "SKM/" + appBandMan.getKmFileName());
//	
//			String km = appBandMan.getKmNo().replace("/", "_").replace(".", "-");
//	
//			String str = link.getAbsoluteURL().replace(".html", "")
//					.replace("/billingsystem", "");
//			
//			FileOutputStream fileOuputStream;
//			if (appBandMan.getKmDoc() != null) {
//	
//				try {
//					fileOuputStream = new FileOutputStream(DIRECTORY_FILE
//							+ appBandMan.getKmFileName());
//	
//					fileOuputStream.write(appBandMan.getKmDoc());
//					fileOuputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//	
//			return str;
//		}else{
//			return new String("none");
//		}
//	}
	
	public void doCancel(IRequestCycle cycle) {
		ManageInvoiceSearch manageInvoiceSearch = (ManageInvoiceSearch) cycle
				.getPage("manageInvoiceSearch");

		saveObjectToSession(null, "INVOICE_LIST_VARIETY");
		saveObjectToSession(null, "INVOICE_LIST_VARIETY_FINE");
		
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
		
		if(getFields().get("BANK_GUARANTEE_FINE") != null){
			getFields().remove("BANK_GUARANTEE_FINE");
		}
		
		//For cancel Invoice
		getFields().remove("INVOICE_CANCEL");
		
		cycle.activate(manageInvoiceSearch);
	}
	
	public String doViewKmIpsfrRate(){
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(invoice.getInvoiceId()),
				String.valueOf(license.getLicenceNo()), "IN-1" + invoice.getInvoiceId());

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
			List<DocumentUpload> listDoc = getDocumentUploadManager().findDocument(String.valueOf(license.getLicenceNo()), "1");
			
			if(listDoc.size() > 0){
				int index = listDoc.size();
				DocumentUpload document = listDoc.get(index-1);
				File downloadFile2 = new File(document.getFileDir() + document.getFileName());
				
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
		
		File dir = new File("testPRihakoNurukat");
		if(!dir.exists()){
			dir.mkdir();
		}
		
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
			
			DocumentUpload docUp = null;
			if(biRate!=null){
				docUp = getDocumentUploadManager().findBiRateDocument(String.valueOf(biRate.getAnnualRateId()), "BI-2" + String.valueOf(biRate.getAnnualRateId()));
			}
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
	
	public String doPrint() throws IOException{
		if (invoice.getLetterID() != null && (invoice.getLetterID().intValue() > 0)) {
			DocumentLetter letterDocument = getInvoiceManager().printLetterDocument(invoice.getLetterID());
			if(letterDocument != null && (letterDocument.getDcitDoc() != null)){
				PdfService.setPdf(letterDocument.getDcitDoc());
	
				String url = "./pdf.svc?imageId=" + invoice.getLetterID();
				System.out.println("dokumen letter URL OK "+url);
	
				return url;
			}else{
				String url = "./documentNotFound.html";
				System.out.println("dokumen letter URL failed "+url);

				return url;
			}
		}
		
		String url = "./documentNotFound.html";
		System.out.println("dokumen letter URL failed "+url);

		return url;
	}
	
	public String doPrint2(){
		return null;
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
}
