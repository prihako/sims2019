// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InitialInvoice.java

package com.balicamp.webapp.action.operational;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.page.InfoPageCommand;
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

public abstract class InitialInvoiceCreate extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	public InitialInvoiceCreate() {
	}

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

	public abstract String getClientID();

	public abstract void setClientID(String bigdecimal);

	public abstract String getZoneName();

	public abstract void setZoneName(String s);

	public abstract BigDecimal getZoneNo();

	public abstract void setZoneNo(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqMax();

	public abstract void setFreqMax(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqMin();

	public abstract void setFreqMin(BigDecimal bigdecimal);

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

	public abstract String getPaymentDueDate();

	public abstract void setPaymentDueDate(String s);

	public abstract String getPaymentType();

	public abstract void setPaymentType(String s);

	public abstract String getBgAvailableStatus();

	public abstract void setBgAvailableStatus(String s);

	public abstract Date getBgDueDate();

	public abstract void setBgDueDate(Date date);

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

	public abstract BigDecimal getBgTotal();

	public abstract void setBgTotal(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpTotal();

	public abstract void setBhpTotal(BigDecimal bigdecimal);

	public abstract byte[] getDocument();

	public abstract void setDocument(byte[] document);

	public abstract ILink getDocumentLink();

	public abstract void setDocumentLink(ILink documentLink);

	public abstract String getDocumentName();

	public abstract void setDocumentName(String documentName);

	public abstract ApplicationBandwidth getApplication();

	public abstract Invoice getInvoice();

	public abstract List<Invoice> getInvoiceList();

	public abstract void setInvoiceList(List<Invoice> invoiceList);

	public abstract License getLicense();

	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	public abstract VariableManager getVariableManager();

	public abstract VariableDetailManager getVariableDetailManager();

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

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		// setDocument(getDocument());

	}

	public void pageEndRender(PageEvent pageevent) {
	}

	public List<Invoice> getBhpList() {
		List bhpList = (List) getFields().get("BHP_LIST");
		return bhpList;
	}

	private static final String OUTPUT_FILE_NAME = "C:\\Users\\ALDERI\\Desktop\\SKM\\";

	// public String getServiceURL()
	//
	// {
	// String km = getKmNo().replace("/", "_").replace(".", "-");
	// ILink link = this.getDownloadService().getLink(false, "SKM/" +
	// getDocumentName());
	//
	// String str = link.getAbsoluteURL().replace(".html", "")
	// .replace("/billingsystem", "");
	// // String file = link.getURL().replace("/billingsystem/", "")
	// // .replace("SKM/", "").replace(".html", ".pdf");
	//
	// FileOutputStream fileOuputStream;
	// try {
	// fileOuputStream = new FileOutputStream(OUTPUT_FILE_NAME +
	// getDocumentName());
	// System.out.println("document  = " + getDocument());
	// System.out.println("documentNAME  = " + getDocumentName());
	//
	// fileOuputStream.write(getDocument());
	// fileOuputStream.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println("ServiceURL = " + str);
	// // System.out.println("URL = " + file);
	//
	// return str;
	// }

	public void hitung() {
		System.out.println("UpFrontFee = " + getBhpUpfrontFee());
		System.out.println("Harga Lelang = " + getBhpHl());
		System.out.println("Penyesuaian Harga Lelang = " + getBhpPhl());
		System.out.println("Klien ID = " + getClientID());

		ApplicationBandwidth appLicense = getApplicationBandwidthManager()
				.findByLicenceNo(getLicenceNumber());

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}

		Calendar dateBegin = Calendar.getInstance();
		Calendar dateEnd = Calendar.getInstance();
		BigDecimal bhpTotal1 = getBhpUpfrontFee().add(auctionPrice);

		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setKmNo(getKmNo());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(appLicense.getClientNumber()));

		double bgPercent = 0.02;
		BigDecimal decimalBGPercent = new BigDecimal(Double.toString(bgPercent));
		BigDecimal bgAmount = auctionPrice.multiply(decimalBGPercent);
		BigDecimal bgAmountTotal = auctionPrice.add(bgAmount);

		setBhpTotal(bhpTotal1);
		setBgTotal(bgAmountTotal);

		System.out.println("BG TOTAL = " + bgAmount);
		
		List<Invoice> invoiceList = new ArrayList<Invoice>();

		for (int i = 0; i < 10; i++) {
			

			invoiceList.add(new Invoice());
			Invoice invoice = invoiceList.get(i);
			invoice.setTLicence(license);
			invoice.setYearTo(new BigDecimal(i + 1));
		
			invoice.setBhpHl(auctionPrice);
			invoice.setBhpAnnual(auctionPrice);
			if (i+1 == 1){
				invoice.setBhpTotal(bhpTotal1);
				invoice.setBhpUpfrontFee(getBhpUpfrontFee());
			}else{
				invoice.setBhpTotal(auctionPrice);
				invoice.setBhpUpfrontFee(null);
			}
			invoice.setBgTotal(bgAmountTotal);
			
		
		}
		
		license.setTInvoices(invoiceList);

		setInvoiceList(invoiceList);

		getFields().put("BHP_LIST", invoiceList);

	}

	public void reset() {
	}

	public void doDraft(IRequestCycle cycle) {
		ApplicationBandwidth appLicense = getApplicationBandwidthManager()
				.findByLicenceNo(getLicenceNumber());

		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(appLicense.getClientNumber()));

		license.setKmNo(getKmNo());
		license.setZoneNo(getZoneNo().toString());
		license.setZoneName(getZoneName());

		try {

			license.setKmDate(DateUtil.convertStringToDate(getKmDate()));
			license.setLicenceBeginDate(DateUtil
					.convertStringToDate(getLicenceBeginDate()));
			license.setLicenceEndDate(DateUtil
					.convertStringToDate(getLicenceEndDate()));
			license.setCurrentBeginDate(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
			license.setCurrentEndDate(DateUtil
					.convertStringToDate(getCurrentEndDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		license.setFreqRMin(getFreqMin());
		license.setFreqRMax(getFreqMax());
		license.setPaymentType(getPaymentType());
		license.setLicenceStatus("D");

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}

		List<Invoice> invoiceList = new ArrayList<Invoice>();
		;
		List<BankGuarantee> bankGuaranteeList = new ArrayList<BankGuarantee>();

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());
				
		String invoiceNumber = getInvoiceManager().generateInvoiceNumber(getLicenceNumber());
		
		System.out.println("GENERATED INVOICE_NO = "+invoiceNumber);

		for (int i = 0; i < 10; i++) {

			invoiceList.add(new Invoice());
			Invoice invoice = invoiceList.get(i);
			invoice.setInvoiceNo(invoiceNumber);
			invoice.setTLicence(license);
			invoice.setYearTo(new BigDecimal(i + 1));
			invoice.setBhpUpfrontFee(getBhpUpfrontFee());
			invoice.setBhpHl(auctionPrice);
			invoice.setBhpAnnual(auctionPrice);
			invoice.setBhpTotal(getBhpTotal());
			invoice.setBgTotal(getBgTotal());
			invoice.setCreatedOn(new Date());
			invoice.setCreatedBy(getUserLoginFromSession().getUsername());
			invoice.setInvoiceStatus("U");
			invoice.setSaveStatus("D");

			bankGuaranteeList.add(new BankGuarantee());

			BankGuarantee bg = bankGuaranteeList.get(i);
			bg.setInvoiceNoClaim(invoiceNumber);
			bg.setBhpValue(auctionPrice);
			bg.setBgValue(getBgTotal());
			bg.setTInvoice(invoiceList.get(i));
			bg.setTLicence(license);
			bg.setCreatedOn(new Date());
			bg.setCreatedBy(getUserLoginFromSession().getUsername());

		}

		license.setTInvoices(invoiceList);
		if (getBgAvailableStatus().equals("Ya")) {
			license.setBgAvailableStatus("Y");
			license.setBgDueDate(getBgDueDate());
			license.setTBankGuarantees(bankGuaranteeList);
		} else {
			license.setBgAvailableStatus("N");
			license.setTBankGuarantees(null);

		}
		license.setCreatedOn(new Date());
		license.setCreatedBy(getUserLoginFromSession().getUsername());
		getLicenseManager().save(license);

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { getKmNo(), getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		// addError(getDelegate(), "errorShadow",
		// getText("license.draft.success"), ValidationConstraint.CONSISTENCY);
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);

	}

	public void doSubmit(IRequestCycle cycle) {
		
		ApplicationBandwidth appLicense = getApplicationBandwidthManager()
				.findByLicenceNo(getLicenceNumber());

		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(appLicense.getClientNumber()));

		license.setKmNo(getKmNo());
		license.setZoneNo(getZoneNo().toString());
		license.setZoneName(getZoneName());

		try {

			license.setKmDate(DateUtil.convertStringToDate(getKmDate()));
			license.setLicenceBeginDate(DateUtil
					.convertStringToDate(getLicenceBeginDate()));
			license.setLicenceEndDate(DateUtil
					.convertStringToDate(getLicenceEndDate()));
			license.setCurrentBeginDate(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
			license.setCurrentEndDate(DateUtil
					.convertStringToDate(getCurrentEndDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		license.setFreqRMin(getFreqMin());
		license.setFreqRMax(getFreqMax());
		license.setPaymentType(getPaymentType());
		license.setLicenceStatus("S");

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}

		List<Invoice> invoiceList = new ArrayList<Invoice>();
		;
		List<BankGuarantee> bankGuaranteeList = new ArrayList<BankGuarantee>();

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());
		
		String invoiceNumber = getInvoiceManager().generateInvoiceNumber(getLicenceNumber());
		
		System.out.println("GENERATED INVOICE_NO = "+invoiceNumber);

		for (int i = 0; i < 10; i++) {

			invoiceList.add(new Invoice());
			
			Invoice invoice = invoiceList.get(i);
			invoice.setInvoiceNo(invoiceNumber);
			invoice.setTLicence(license);
			invoice.setYearTo(new BigDecimal(i + 1));
			invoice.setBhpUpfrontFee(getBhpUpfrontFee());
			invoice.setBhpHl(auctionPrice);
			invoice.setBhpAnnual(auctionPrice);
			invoice.setBhpTotal(getBhpTotal());
			invoice.setBgTotal(getBgTotal());
			invoice.setCreatedOn(new Date());
			invoice.setCreatedBy(getUserLoginFromSession().getUsername());
			invoice.setInvoiceStatus("U");
			
			if (i+1 == 1){
				invoice.setSaveStatus("S");

			}else if(i+1 == 2){
				invoice.setSaveStatus("D");

			}else{
				invoice.setSaveStatus("C");

			}


			bankGuaranteeList.add(new BankGuarantee());

			BankGuarantee bg = bankGuaranteeList.get(i);
			bg.setInvoiceNoClaim(invoiceNumber);
			bg.setBhpValue(auctionPrice);
			bg.setBgValue(getBgTotal());
			bg.setTInvoice(invoiceList.get(i));
			bg.setTLicence(license);
			bg.setCreatedOn(new Date());
			bg.setCreatedBy(getUserLoginFromSession().getUsername());

		}

		license.setTInvoices(invoiceList);
		if (getBgAvailableStatus().equals("Ya")) {
			license.setBgAvailableStatus("Y");
			license.setBgDueDate(getBgDueDate());
			license.setTBankGuarantees(bankGuaranteeList);
		} else {
			license.setBgAvailableStatus("N");
			license.setTBankGuarantees(null);

		}
		license.setCreatedOn(new Date());
		license.setCreatedBy(getUserLoginFromSession().getUsername());
		getLicenseManager().save(license);

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { getKmNo(), getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		// addError(getDelegate(), "errorShadow",
		// getText("license.draft.success"), ValidationConstraint.CONSISTENCY);
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);
		
		
	}

	public void doCancel() {
	}
}
