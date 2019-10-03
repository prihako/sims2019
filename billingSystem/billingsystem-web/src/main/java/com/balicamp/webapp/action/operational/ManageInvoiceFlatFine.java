package com.balicamp.webapp.action.operational;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
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
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.PdfService;

public abstract class ManageInvoiceFlatFine extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	protected final Log log = LogFactory.getLog(ManageInvoiceFlatFine.class);

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

	public abstract String getPaymentFineDueDate();
	public abstract void setPaymentFineDueDate(String s);

	public abstract String getPaymentType();
	public abstract void setPaymentType(String s);

	public abstract String getBgAvailableStatus();
	public abstract void setBgAvailableStatus(String s);

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

	public abstract String getInvoiceNo();
	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getInvoiceStatus();
	public abstract void setInvoiceStatus(String invoiceStatus);

	public abstract String getInvoiceType();
	public abstract void setInvoiceType(String invoiceType);

	public abstract String getInvoiceStatusText();
	public abstract void setInvoiceStatusText(String invoiceStatusText);

	public abstract String getInvoiceTypeText();
	public abstract void setInvoiceTypeText(String invoiceTypeText);

	public abstract String getInvoiceCreateDate();
	public abstract void setInvoiceCreateDate(String invoiceCreateDate);

	public abstract String getInvoiceBeginDate();
	public abstract void setInvoiceBeginDate(String invoiceBeginDate);

	public abstract BigDecimal getMonthTo();
	public abstract void setMonthTo(BigDecimal monthTo);

	public abstract String getPaymentDate();
	public abstract void setPaymentDate(String paymentDate);

	public abstract BigDecimal getBhpFineAccumulate();
	public abstract void setBhpFineAccumulate(BigDecimal bhpFineAccumulate);

	public abstract BigDecimal getBhpFineCurrent();
	public abstract void setBhpFineCurrent(BigDecimal bhpFineCurrent);

	public abstract BigDecimal getBhpFinePercent();
	public abstract void setBhpFinePercent(BigDecimal bhpFinePercent);

	public abstract BigDecimal getPaymentAmount();
	public abstract void setPaymentAmount(BigDecimal paymentAmount);

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract Invoice getInvoice();
	public abstract void setInvoice(Invoice invoice);

	public abstract List<Invoice> getInvoiceList();
	public abstract void setInvoiceList(List<Invoice> invoiceList);

	public abstract License getLicense();
	public abstract Object getData();

	public abstract BigDecimal getLetterID();
	public abstract void setLetterID(BigDecimal letterID);

	public abstract String getSaveStatus();
	public abstract void setSaveStatus(String saveStatus);

	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();

	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();
	
	public abstract void setCriteria(String crt);
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	public abstract String getCriteriaSearch();
	
	public abstract void setInvoiceStatusCache(String stat);
	public abstract String getInvoiceStatusCache();
	
	public abstract void setBankGuarantee(BankGuarantee bankGuarantee);
	public abstract BankGuarantee getBankGuarantee();
	
	public abstract void setInvoiceCancelConfirmation(String bgSubmit);
	public abstract String getInvoiceCancelConfirmation();
	
	public abstract void setInvoiceCommentCancelInvoice(String s);
	public abstract String getInvoiceCommentCancelInvoice();
	
	public ManageInvoiceFlatFine() {

	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if(getBankGuarantee()==null){
			if(getFields()!=null && getFields().get("BG")!=null){
				setBankGuarantee((BankGuarantee) getFields().get("BG"));
			}
		}
		
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				}
				getFields().put("BG", getBankGuarantee());
			}
		}

	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);

	}

	public List<Invoice> getBhpList() {
		List<Invoice> list = (List<Invoice>) getFields().get("INVOICE_LIST");
		List<Invoice> invoicesFine = (List<Invoice>) getFields().get("INVOICE_FINE");

		List<Invoice> invoiceList = new ArrayList<Invoice>();

		for (Invoice invoice : list) {

			if (invoice.getYearTo().intValue() <= invoicesFine.get(0)
					.getYearTo().intValue()
					&& invoice.getInvoiceType().equals("1")) {
				invoiceList.add(invoice);
			}

		}
		return invoiceList;
	}

	public List<Invoice> getInvoiceFine() {

		List<Invoice> list = (List<Invoice>) getFields().get(
				"INVOICE_FINE_LIST");

		return list;

	}

	public String getIsBGAvailable() {

		String str = null;
		if(getBgAvailableStatus()!=null && getBgAvailableStatus().equals("Y")) {
			str = "Y";
		} else {
			str = null;
		}
		return str;
	}

	public void doBack(IRequestCycle cycle) {

		ManageInvoiceSearch manageInvoiceSearch = (ManageInvoiceSearch) cycle
				.getPage("manageInvoiceSearch");
		getFields().remove("BG");
		getFields().remove("INVOICE_LIST");
		getFields().remove("INVOICE_FINE");
		getFields().remove("INVOICE_FINE_LIST");
		
		getFields().put("LICENSE_LIST", (List) getFields().get("LICENSE_LIST_FR"));
		
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
			System.out.println("dokumen letter " + letterDocument.getDcitDoc()
					+ " and size = " + letterDocument.getDcitDoc().length);
			PdfService.setPdf(letterDocument.getDcitDoc());

			url = "./pdf.svc?imageId=" + letterDocument.getLeId();
			System.out.println("dokumen letter URL OK " + url);

			return url;
		} else {
			url = "./documentNotFound.html";
			System.out.println("dokumen letter URL failed " + url);

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
