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

public abstract class ManageInvoiceEdit extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	public ManageInvoiceEdit() {

	}

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

	public abstract String getBgDueDate();

	public abstract void setBgDueDate(String date);

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

	public abstract BigDecimal getBhpAnnual();

	public abstract void setBhpAnnual(BigDecimal bigdecimal);

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

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getInvoiceStatus();

	public abstract void setInvoiceStatus(String invoiceStatus);

	public abstract String getInvoiceType();

	public abstract void setInvoiceType(String invoiceType);

	public abstract BigDecimal getMonthTo();

	public abstract void setMonthTo(BigDecimal monthTo);

	public abstract Date getPaymentDate();

	public abstract void setPaymentDate(Date paymentDate);

	public abstract String getInvoiceCreateDate();

	public abstract void setInvoiceCreateDate(String invoiceCreateDate);

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

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		// setDocument(getDocument());

	}

	@Override
	public void pageEndRender(PageEvent pageevent) {
		super.pageEndRender(pageevent);
	}

	public List<Invoice> getBhpList() {
		List bhpList = (List) getFields().get("BHP_LIST");
		return bhpList;
	}

	private static final String OUTPUT_FILE_NAME = "C:\\Users\\ALDERI\\Desktop\\SKM\\";

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

		List<License> licenseList = new ArrayList<License>();

		Calendar dateBegin = Calendar.getInstance();
		Calendar dateEnd = Calendar.getInstance();
		BigDecimal bhpTotal1 = getBhpUpfrontFee().add(auctionPrice);

		Invoice invoices = new Invoice();
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
			if (i + 1 == 1) {
				invoice.setBhpTotal(bhpTotal1);
				invoice.setBhpUpfrontFee(getBhpUpfrontFee());

			} else {
				invoice.setBhpTotal(auctionPrice);
				invoice.setBhpUpfrontFee(null);

			}
			invoice.setBgTotal(bgAmountTotal);
			invoice.setUpdatedOn(new Date());
			invoice.setUpdatedBy(getUserLoginFromSession().getUsername());

		}

		license.setTInvoices(invoiceList);

		setInvoiceList(invoiceList);

		getFields().put("BHP_LIST", invoiceList);

	}

	public void reset() {
	}

	public void doDraft(IRequestCycle cycle) {
		Invoice invoice = getInvoiceManager().findInvoiceByID(getInvoiceID());
		License license = getLicenseManager().findLicenseByID(getLicenceID());
		BankGuarantee bg = getBankGuaranteeManager().findByInvoiceID(
				getLicenceID(), getInvoiceID());

		// License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(getClientID()));

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

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());


		invoice.setTLicence(license);
		invoice.setBhpUpfrontFee(getBhpUpfrontFee());
		invoice.setBhpHl(auctionPrice);
		invoice.setBhpAnnual(auctionPrice);
		invoice.setBhpTotal(getBhpTotal());
		invoice.setBgTotal(getBgTotal());
		invoice.setCreatedOn(new Date());
		invoice.setCreatedBy(getUserLoginFromSession().getUsername());


		bg.setBhpValue(auctionPrice);
		bg.setBgValue(getBgTotal());
		bg.setTInvoice(invoice);
		bg.setTLicence(license);
		bg.setUpdatedOn(new Date());
		bg.setUpdatedBy(getUserLoginFromSession().getUsername());
		getBankGuaranteeManager().save(bg);


		license.setUpdatedOn(new Date());
		license.setUpdatedBy(getUserLoginFromSession().getUsername());
		getInvoiceManager().save(invoice);


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

		Invoice invoice = getInvoiceManager().findInvoiceByID(getInvoiceID());
		License license = getLicenseManager().findLicenseByID(getLicenceID());
		BankGuarantee bg = getBankGuaranteeManager().findByInvoiceID(
				getLicenceID(), getInvoiceID());

		// License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(getClientID()));

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

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());


		invoice.setTLicence(license);
		invoice.setBhpUpfrontFee(getBhpUpfrontFee());
		invoice.setBhpHl(auctionPrice);
		invoice.setBhpAnnual(auctionPrice);
		invoice.setBhpTotal(getBhpTotal());
		invoice.setBgTotal(getBgTotal());
		invoice.setCreatedOn(new Date());
		invoice.setCreatedBy(getUserLoginFromSession().getUsername());


		bg.setBhpValue(auctionPrice);
		bg.setBgValue(getBgTotal());
		bg.setTInvoice(invoice);
		bg.setTLicence(license);
		bg.setUpdatedOn(new Date());
		bg.setUpdatedBy(getUserLoginFromSession().getUsername());
		getBankGuaranteeManager().save(bg);


		license.setUpdatedOn(new Date());
		license.setUpdatedBy(getUserLoginFromSession().getUsername());
		getInvoiceManager().save(invoice);


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
