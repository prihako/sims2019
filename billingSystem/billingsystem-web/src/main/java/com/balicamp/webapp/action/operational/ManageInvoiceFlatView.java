package com.balicamp.webapp.action.operational;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.Constants;
import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.DocumentLetter;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.PdfService;

public abstract class ManageInvoiceFlatView extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	protected final Log log = LogFactory.getLog(ManageInvoiceFlatView.class);

	public abstract Long getLicenceID();
	public abstract void setLicenceID(Long licenceID);

	public abstract Long getInvoiceID();
	public abstract void setInvoiceID(Long invoiceID);

	public abstract String getKmNo();
	public abstract void setKmNo(String s);

	public abstract String getLicenceNumber();
	public abstract void setLicenceNumber(String s);

	public abstract String getService();
	public abstract void setService(String s);

	public abstract String getSubService();
	public abstract void setSubService(String s);

	public abstract BigDecimal getServiceId();
	public abstract void setServiceId(BigDecimal bigdecimal);

	public abstract BigDecimal getSubServiceId();
	public abstract void setSubServiceId(BigDecimal bigdecimal);

	public abstract String getBhpMethod();
	public abstract void setBhpMethod(String s);

	public abstract String getClientName();
	public abstract void setClientName(String s);

	public abstract String getClientNO();
	public abstract void setClientNO(String clientNO);

	public abstract void setMethodBHP(String s);
	public abstract String getMethodBHP();

	public abstract void setPaymentTypeMethod(String s);
	public abstract String getPaymentTypeMethod();

	public abstract String getZoneName();
	public abstract void setZoneName(String s);

	public abstract BigDecimal getZoneNo();
	public abstract void setZoneNo(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqTMax();
	public abstract void setFreqTMax(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqTMin();
	public abstract void setFreqTMin(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqRMax();
	public abstract void setFreqRMax(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqRMin();
	public abstract void setFreqRMin(BigDecimal bigdecimal);

	public abstract String getKmDate();
	public abstract void setKmDate(String s);

	public abstract String getCurrentBeginDate();
	public abstract void setCurrentBeginDate(String s);

	public abstract String getCurrentEndDate();
	public abstract void setCurrentEndDate(String s);

	public abstract String getLicenceBeginDate();
	public abstract void setLicenceBeginDate(String s);

	public abstract String getLicenceEndDate();
	public abstract void setLicenceEndDate(String s);

	public abstract BigDecimal getYearTo();
	public abstract void setYearTo(BigDecimal bigdecimal);

	public abstract String getYear();
	public abstract void setYear(String bigdecimal);

	public abstract String getPaymentDueDate();
	public abstract void setPaymentDueDate(String s);

	public abstract String getPaymentType();
	public abstract void setPaymentType(String s);

	public abstract String getBgAvailableStatus();
	public abstract void setBgAvailableStatus(String s);

//	public abstract Date getBgDueDate();
//	public abstract void setBgDueDate(Date date);
	
	public abstract String getBgSubmitDueDate();
	public abstract void setBgSubmitDueDate(String date);

	public abstract BigDecimal getRateValue();
	public abstract void setRateValue(BigDecimal bigdecimal);

	public abstract Long getPercentYear();
	public abstract void setPercentYear(Long long1);

	public abstract BigDecimal getBhpCalcIndex();
	public abstract void setBhpCalcIndex(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpUpfrontFee();
	public abstract void setBhpUpfrontFee(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpHl();
	public abstract void setBhpHl(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpPhl();
	public abstract void setBhpPhl(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpAnnual();
	public abstract void setBhpAnnual(BigDecimal bigdecimal);

	public abstract BigDecimal getBgTotal();
	public abstract void setBgTotal(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpTotal();
	public abstract void setBhpTotal(BigDecimal bigdecimal);
	
	public abstract BigDecimal getBhpTotalOri();
	public abstract void setBhpTotalOri(BigDecimal bigdecimal);
	
	public abstract void setNilaiBhpEdit(String nilaiBhpEdit);
	public abstract String getNilaiBhpEdit();

	public abstract String getInvoiceNo();
	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getInvoiceStatus();
	public abstract void setInvoiceStatus(String invoiceStatus);

	public abstract String getInvoiceType();
	public abstract void setInvoiceType(String invoiceType);

	public abstract String getInvoiceStatusText();
	public abstract void setInvoiceStatusText(String invoiceStatusText);

	public abstract String getInvoiceComment();
	public abstract void setInvoiceComment(String invoiceComment);
	
	public abstract String getInvoiceCommentBhp();
	public abstract void setInvoiceCommentBhp(String invoiceCommentBhp);
	
	public abstract void setRemarksEdit(String remarks);
	public abstract String getRemarksEdit();

	public abstract String getInvoiceTypeText();
	public abstract void setInvoiceTypeText(String invoiceTypeText);

	public abstract String getSaveStatus();
	public abstract void setSaveStatus(String saveStatus);
	
	public abstract String getBhpTotalEditHasError();
	public abstract void setBhpTotalEditHasError(String bhpTotalEditHasError);

	public abstract String getInvoiceCreateDate();
	public abstract void setInvoiceCreateDate(String invoiceCreateDate);

	public abstract BigDecimal getMonthTo();
	public abstract void setMonthTo(BigDecimal monthTo);

	public abstract String getPaymentDate();
	public abstract void setPaymentDate(String paymentDate);

	public abstract Invoice getInvoice();
	public abstract void setInvoice(Invoice invoice);
	
	public abstract BankGuarantee getBankGuaranteeRow();
	public abstract void setBankGuaranteeRow(BankGuarantee bg);

	public abstract BigDecimal getLetterID();
	public abstract void setLetterID(BigDecimal letterID);

	public abstract List<Invoice> getInvoiceList();
	public abstract void setInvoiceList(List<Invoice> invoiceList);

	public abstract License getLicense();

	public abstract Object getData();

	public abstract byte[] getDocument();
	public abstract void setDocument(byte[] document);

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

	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();
	
	public abstract void setInvoiceStatusCache(String stat);
	
	public abstract String getInvoiceStatusCache();
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	public abstract void setInvoiceCommentCancelInvoice(String s);
	public abstract String getInvoiceCommentCancelInvoice();

	public abstract void setInvoiceCancelConfirmation(String bgSubmit);
	public abstract String getInvoiceCancelConfirmation();
	
	public ManageInvoiceFlatView() {

	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
	}

	public List<Invoice> getBhpList() {
		List<Invoice> bhpList = (List<Invoice>) getFields().get("INVOICE_LIST");

		List<Invoice> invoiceList = new ArrayList<Invoice>();

		for (Invoice invoice : bhpList) {

			if (invoice.getInvoiceType().equals("1")) {
				invoiceList.add(invoice);
			}

		}
		return invoiceList;
	}
	
	public List<BankGuarantee> getBankGuaranteeList() {
		List<BankGuarantee> bgList = (List<BankGuarantee>) getFields().get("BANK_GUARANTEE_LIST_FR");
		return bgList;
	}

	public Invoice getInvoiceRecord() {
		Invoice invoice = (Invoice) getFields().get("INVOICE_RECORD");
		return invoice;
	}

	 public String getIsBGAvailable() {
	
		 String str = null;
		 if (getBgAvailableStatus().equals("Y")) {
			 str = "Y";
		 } else {
			 str = null;
		 }
		 return str;
	 }

	public String showPdf() {

		// byte[] letterDocument=null;
		Object letterDocument = getInvoiceManager().printLetterDocument(
				getLetterID());

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(letterDocument);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] doc = b.toByteArray();

		System.out.println("dokumen letter " + doc);

		if (getLetterID() != null) {
			PdfService.setPdf(doc);

			String url = "./pdf.svc?imageId=" + getLetterID();

			return url;
		} else {
			String url = "./documentNotFound.html";
			return url;
		}
	}

	public String doViewKmIpsfrRate() {
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(
				String.valueOf(getInvoiceID()),
				String.valueOf(getLicenceNumber()), "IN-1" + getInvoiceID());

		if (docUp != null) {
			File downloadFile = new File(docUp.getFileDir()
					+ docUp.getFileName());

			if (downloadFile.exists()) {
				String url = "./document.svc?imageId=" + docUp.getFileName();
				return url;
			} else {
				String url = "./documentNotFound.html";
				return url;
			}
		} else {
			List<DocumentUpload> listDoc = getDocumentUploadManager()
					.findDocument(String.valueOf(getLicenceNumber()), "1");

			if (listDoc.size() > 0) {
				int index = listDoc.size();
				DocumentUpload document = listDoc.get(index - 1);
				File downloadFile2 = new File(document.getFileDir());

				if (downloadFile2.exists()) {
					String url = "./document.svc?imageId="
							+ document.getFileName();
					return url;
				} else {
					String url = "./documentNotFound.html";
					return url;
				}

			} else {
				String url = "./documentNotFound.html";
				return url;
			}
		}
	}

	public void doEdit(IRequestCycle cycle) {

		getFields().put("BHP_LIST", null);// clear session before redirect to
											// edit page

		ManageInvoiceFlatEdit edit = (ManageInvoiceFlatEdit) cycle
				.getPage("manageInvoiceFlatEdit");
		edit.setLicenceID(getLicenceID());
		edit.setInvoiceID(getInvoiceID());
		edit.setLicenceNumber(getLicenceNumber());
		edit.setClientName(getClientName());
		edit.setClientNO(getClientNO());
		edit.setBhpMethod(getBhpMethod());
		edit.setMethodBHP(getMethodBHP());
		edit.setKmNo(getKmNo());
		edit.setKmDate(getKmDate().toString());
		edit.setZoneNo(getZoneNo());
		edit.setZoneName(getZoneName());
		edit.setLicenceBeginDate(getLicenceBeginDate());
		edit.setLicenceEndDate(getLicenceEndDate());
		edit.setFreqTMin(getFreqTMin());
		edit.setFreqTMax(getFreqTMax());
		edit.setFreqRMin(getFreqRMin());
		edit.setFreqRMax(getFreqRMax());
		edit.setPaymentType(getPaymentType());
		edit.setPaymentTypeMethod(getPaymentTypeMethod());
		edit.setPaymentDueDate(getPaymentDueDate());
		edit.setCurrentBeginDate(getCurrentBeginDate());
		edit.setCurrentEndDate(getCurrentEndDate());
		edit.setYearTo(getYearTo());
		edit.setYear(getYear());
		edit.setBgAvailableStatus(getBgAvailableStatus());
		edit.setBgSubmitDueDate(getBgSubmitDueDate());
		edit.setBhpUpfrontFee(getBhpUpfrontFee());
		edit.setBhpHl(getBhpHl());
		edit.setBhpPhl(getBhpPhl());

		edit.setBhpAnnual(getBhpAnnual());
		edit.setBgTotal(getBgTotal());
		edit.setBhpTotal(getBhpTotal());
		edit.setInvoiceNo(getInvoiceNo());
		edit.setInvoiceStatus(getInvoiceStatus());
		edit.setInvoiceStatusText(getInvoiceStatusText());
		edit.setInvoiceComment(getInvoiceComment());
		edit.setInvoiceType(getInvoiceType());
		edit.setInvoiceTypeText(getInvoiceTypeText());
		edit.setInvoiceCreateDate(getInvoiceCreateDate());
		edit.setPaymentDate(getPaymentDate());
//		edit.setSaveStatus(getSaveStatus());
		edit.setFields(getFields());

		getUserManager().viewManageInvoiceEdit(initUserLoginFromDatabase(), getRequest().getRemoteHost(),getBhpMethod(), getLicenceNumber());
		
		//For back button 
//		edit.setApplicationList((List) getFields().get("LICENSE_LIST"));
		getFields().put("LICENSE_LIST", (List) getFields().get("LICENSE_LIST_FR"));
		
		edit.setCriteria(getFields().get("criteria").toString());
		edit.setCriteriaSearch((String) getFields().get("criteriaSearch"));
		edit.setInvoiceStatusCache((String) getFields().get("invoiceStatus"));  
		
		cycle.activate(edit);

	}
	
	public void doEditBhpTotal(IRequestCycle cycle) {

		if (validateBhpTotal(getNilaiBhpEdit())==true && validateRemarks(getRemarksEdit())==true) {		
			
			List<Invoice> invoices=getInvoiceManager().findByInvoiceNo(getInvoiceNo());
			for (Invoice invoice : invoices) {
				if (invoice.getBhpTotalOri()==null) {
					invoice.setBhpTotalOri(invoice.getBhpTotal());
				}
				invoice.setBhpTotal(new BigDecimal(Long.parseLong(getNilaiBhpEdit())));
				invoice.setInvoiceCommentBhp(getRemarksEdit());
				getInvoiceManager().save(invoice);
			}
			setBhpTotalOri(getBhpTotalOri());
			setInvoiceCommentBhp(getRemarksEdit());
			setBhpTotal(new BigDecimal(Long.parseLong(getNilaiBhpEdit())));
			setBhpTotalEditHasError(null);
			addError(getDelegate(), "errorShadow","Nilai BHP Frekuensi Baru Berhasil Tersimpan" , ValidationConstraint.CONSISTENCY);
		}else{
			setBhpTotalEditHasError("hasError");
		}

		getUserManager().viewManageInvoiceView(initUserLoginFromDatabase(), getRequest().getRemoteHost(),getBhpMethod(), getLicenceNumber(), true);
		
	}
	
	public boolean validateBhpTotal(String bhpTotal){
		String regex = "\\d+";
		boolean bhpTotalValidator=true;
		
		Pattern pattern = Pattern.compile(regex);
		
		if (bhpTotal==null || bhpTotal.equals("")) {
			addError(getDelegate(), "bhpTotalEditErrorNotif", getText("manageInvoice.invoice.editBhpTotal.notification.bhpValue.isNull"),
					ValidationConstraint.CONSISTENCY);
			bhpTotalValidator=false;
		}else{
			BigDecimal bhpTotalBD=new BigDecimal(Long.parseLong(bhpTotal));
			if (bhpTotalBD.longValue()<=0) {
				addError(getDelegate(), "bhpTotalEditErrorNotif", "Input Nilai Bhp Total Frekuensi Harus Lebih Besar Dari 0.",
						ValidationConstraint.CONSISTENCY);
				bhpTotalValidator=false;
			}else if(!pattern.matcher(bhpTotal.toString()).matches()){
				addError(getDelegate(), "bhpTotalEditErrorNotif", getText("manageInvoice.invoice.editBhpTotal.notification.bhpValue.notNumber"),
						ValidationConstraint.CONSISTENCY);
				bhpTotalValidator=false;
			}else if (getBhpTotal().equals(bhpTotalBD)) {
				addError(getDelegate(), "bhpTotalEditErrorNotif", getText("manageInvoice.invoice.editBhpTotal.notification.bhpValue.isNotChanged"),
						ValidationConstraint.CONSISTENCY);
				bhpTotalValidator=false;
			}
		}
		return bhpTotalValidator;
	}
	public boolean validateRemarks(String remarks){
		boolean bhpTotalValidator=true;
		if (remarks==null || remarks.equals("")) {
			addError(getDelegate(), "errorShadow", getText("manageInvoice.invoice.editBhpTotal.notification.remarks.isNull"),
					ValidationConstraint.CONSISTENCY);
			bhpTotalValidator=false;
		}
		return bhpTotalValidator;
	}

	public void doBack(IRequestCycle cycle) {

		ManageInvoiceSearch manageInvoiceSearch = (ManageInvoiceSearch) cycle
				.getPage("manageInvoiceSearch");
		getFields().remove("INVOICE_LIST");
		getFields().remove("INVOICE_RECORD");
		
		getFields().remove("INVOICE_CANCEL");
		
		getFields().put("LICENSE_LIST_FR", (List) getFields().get("LICENSE_LIST_FR"));
		
		getFields().put("criteria", getCriteria());
		getFields().put("criteriaSearch", getCriteriaSearch());
		getFields().put("invoiceStatus", getInvoiceStatusCache());
		getFields().put("managePage", "xxx");
		
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
		
		manageInvoiceSearch.doSearch();
		cycle.activate(manageInvoiceSearch);	

	}

	public String doPrint() {

		System.out.println("Letter ID for Print Letter = " + getLetterID());
		System.out.println("SAVE STATUS = " + getSaveStatus());

		if (getLetterID() == null) {

			String url = "./documentNotFound.html";
			System.out.println("Letter ID Null ");
			return url;
		}
		String url = null;
		if (getLetterID() != null) {
			DocumentLetter letterDocument = getInvoiceManager()
					.printLetterDocument(getLetterID());

			if (letterDocument != null && letterDocument.getDcitDoc() != null) {
				System.out.println("dokumen letter "
						+ letterDocument.getDcitDoc() + " and size = "
						+ letterDocument.getDcitDoc().length);
				PdfService.setPdf(letterDocument.getDcitDoc());

				url = "./pdf.svc?imageId=" + letterDocument.getLeId();
				System.out.println("dokumen letter URL OK " + url);

				return url;

			}else{
				url = "./documentNotFound.html";
				System.out.println("dokumen letter URL failed " + url);

				return url;
			}
		} else {
			url = "./documentNotFound.html";
			System.out.println("dokumen letter URL failed " + url);

			return url;

		}

	}
	
	
	public void doCancelInvoice(IRequestCycle cycle) {
		getFields().put("INVOICE_CANCEL", "INVOICE_CANCEL");
		
	}
	
	public void doCancelInvoice2(IRequestCycle cycle) {
		
		if(getInvoiceNo() != null){
			
			Invoice invoice = getInvoiceManager().findInvoiceByID(getInvoiceID());
			
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
