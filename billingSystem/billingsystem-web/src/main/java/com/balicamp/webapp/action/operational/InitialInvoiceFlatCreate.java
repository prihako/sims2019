// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InitialInvoice.java

package com.balicamp.webapp.action.operational;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.BankGuarantee;
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

public abstract class InitialInvoiceFlatCreate extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	protected final Log log = LogFactory.getLog(InitialInvoiceFlatCreate.class);

	public InitialInvoiceFlatCreate() {
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

	public abstract void setMethodBHP(String s);

	public abstract String getMethodBHP();

	public abstract void setPaymentTypeMethod(String s);

	public abstract String getPaymentTypeMethod();

	public abstract String getClientName();

	public abstract void setClientName(String s);

	public abstract String getClientNO();

	public abstract void setClientNO(String s);

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

	public abstract BigDecimal getBhpAnnual();

	public abstract void setBhpAnnual(BigDecimal bigdecimal);

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

	@InjectPage("initialInvoiceSearch")
	public abstract InitialInvoiceSearch getInitialInvoiceSearch();

	@InjectObject("engine-service:page")
	public abstract IEngineService getDownloadService();

	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	
	public abstract boolean isNotFirstLoad();
	
	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();

	
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				}
			}
		}
		
	}

	public void pageEndRender(PageEvent pageevent) {
	}

	@SuppressWarnings({"unchecked" })
	public List<Invoice> getBhpList() {
		List<Invoice> bhpList = (List<Invoice>) getFields().get("BHP_LIST");
		return bhpList;
	}
	
//	@SuppressWarnings({"unchecked" })
//	public List<BankGuarantee> getBgList() {
//		List<BankGuarantee> bgList = (List<BankGuarantee>) getFields().get("BANK_GUARANTEE_LIST");
//		return bgList;
//	}

	public String getIsBGAvailable() {

		String str = null;

		if (getBgAvailableStatus() == null) {
			str = "N";
		} else if (getBgAvailableStatus().equals("Y")) {
			str = "Y";
		} else if (getBgAvailableStatus().equals("N")) {
			str = "N";
		}
		return str;
	}

	// private static final String DIRECTORY_FILE =
	// "C:\\Program Files\\JBoss.org\\JBoss Web 2.1\\webapps\\SKM\\";
	private static final String DIRECTORY_FILE = "\\webapps\\SKM\\";
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";

	public String showPdf() {
		if (getDocument() != null) {
			PdfService.setPdf(getDocument());

			String url = "./pdf.svc?imageId=" + getDocumentName();

			return url;
		} else {
			String url = "./documentNotFound.html";
			return url;
		}
	}

	public void hitung() {
		System.out.println("UpFrontFee = " + getBhpUpfrontFee());
		System.out.println("Harga Lelang = " + getBhpHl());
		System.out.println("Penyesuaian Harga Lelang = " + getBhpPhl());
		System.out.println("Klien ID = " + getClientNO());

		// String validationMsg = saveValidation();
		if (getBhpUpfrontFee() == null && getBhpHl() == null) {
			addError(getDelegate(), "errorShadow",
					"Up Front Fee dan Harga Lelang harus diisi",
					ValidationConstraint.CONSISTENCY);
			return;
		} else if (getBhpUpfrontFee() == null && getBhpHl() != null) {
			addError(getDelegate(), "errorShadow",
					"Nilai Up Front Fee Harus Diisi",
					ValidationConstraint.CONSISTENCY);
			return;

		} else if (getBhpUpfrontFee() != null && getBhpHl() == null
				&& getBhpPhl() == null) {
			addError(
					getDelegate(),
					"errorShadow",
					"Anda Harus Mengisi Nilai Penyesuain Harga Lelang Atau Harga Lelang",
					ValidationConstraint.CONSISTENCY);

			return;
		}

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}

		Calendar dateBegin = Calendar.getInstance();
		try {
			dateBegin.setTime(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BigDecimal bhpTotal1 = getBhpUpfrontFee().add(auctionPrice);

		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setKmNo(getKmNo());
		license.setClientName(getClientName());
		// license.setClientNo(new BigDecimal(appLicense.getClientNumber()));

		BigDecimal bgAmountTotal = null;
		if (getBgAvailableStatus() == null) {

		} else if (getBgAvailableStatus().equals("Y")) {
			double bgPercent = 0.02;
			BigDecimal decimalBGPercent = new BigDecimal(
					Double.toString(bgPercent));
			BigDecimal bgAmount = auctionPrice.multiply(decimalBGPercent);
			bgAmountTotal = auctionPrice.add(bgAmount);
			setBgTotal(bgAmountTotal);

			System.out.println("BG TOTAL = " + bgAmount);

		}
		
//		for Bank Guarantee
//		List<BankGuarantee> bgList = new ArrayList<BankGuarantee>();
//		if (getBgAvailableStatus().equals("Y")) {
//			double bgPercent = 0.02;
//			BigDecimal decimalBGPercent = new BigDecimal(
//					Double.toString(bgPercent));
//			BigDecimal bgAmount = auctionPrice.multiply(decimalBGPercent);
//			bgAmountTotal = auctionPrice.add(bgAmount);
//			setBgTotal(bgAmountTotal);
//			Calendar bgSubmitDueDate = Calendar.getInstance();
//			try {
//				bgSubmitDueDate.setTime(DateUtil
//						.convertStringToDate(getCurrentBeginDate()));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			
//			for(int i = 0; i < 9; i++){
//				BankGuarantee bg = new BankGuarantee();
//				bg.setSubmitYearTo(new BigDecimal(String.valueOf(i+1)));
//				bg.setBankAddress(String.valueOf(bgSubmitDueDate.get(Calendar.YEAR)));
//				bg.setBhpValue(auctionPrice);
//				bg.setBgValue(bgAmountTotal);
//				
//				bgSubmitDueDate.add(Calendar.YEAR, 1);
//				bgList.add(bg);
//			}
//			
//			getFields().put("BANK_GUARANTEE_LIST", bgList);
//
//		}

		setBhpTotal(bhpTotal1);
		setBhpAnnual(auctionPrice);

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
				invoice.setInvBeginDate(dateBegin.getTime());

			} else {
				dateBegin.add(Calendar.YEAR, 1);
				invoice.setInvBeginDate(dateBegin.getTime());
				invoice.setBhpTotal(auctionPrice);
				invoice.setBhpUpfrontFee(null);
			}
			invoice.setBgTotal(bgAmountTotal);
		}

		license.setTInvoices(invoiceList);

		setInvoiceList(invoiceList);

		getFields().put("BHP_LIST", invoiceList);

	}

	public void reset(IRequestCycle cycle) {
		getFields().remove("BHP_LIST");
	
		setBhpAnnual(null);
		setBgTotal(null);
		setBhpTotal(null);
	}

	public void doDraft(IRequestCycle cycle) {
		
		ApplicationBandwidth appLicense = getApplicationBandwidthManager()
				.findByLicenceNo(getLicenceNumber());
		
		String checkBgDueDate = checkBgDueDate();
		
		if(checkBgDueDate != null){
			addError(getDelegate(), "errorShadow", checkBgDueDate, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String checkLicense = checkLicense(appLicense);
		
		if (checkLicense != null) {
			addError(getDelegate(), "errorShadow", checkLicense, ValidationConstraint.CONSISTENCY);
			System.out.println(checkLicense);
			return;
		}

		if (getFields().get("BHP_LIST") == null) {
			addError(getDelegate(), "errorShadow", "BHP harus dihitung",
					ValidationConstraint.CONSISTENCY);

			return;

		}

		if (getBhpUpfrontFee() == null && getBhpHl() == null) {
			
			addError(getDelegate(), "errorShadow",
					"Upfrontfee dan Harga Lelang harus diisi",
					ValidationConstraint.CONSISTENCY);

			return;
		} else if (getBhpUpfrontFee() == null && getBhpHl() != null) {
			
			addError(getDelegate(), "errorShadow", "Upfrontfee harus diisi",
					ValidationConstraint.CONSISTENCY);

			return;

		} else if (getBhpUpfrontFee() != null && getBhpHl() == null
				&& getBhpPhl() == null) {
		
			addError(getDelegate(), "errorShadow", "Harga Lelang harus diisi",
					ValidationConstraint.CONSISTENCY);

			return;

		} 

		Calendar dateBegin = Calendar.getInstance();
		Calendar dateEnd = Calendar.getInstance();
		Calendar datePayment = Calendar.getInstance();

		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(appLicense.getClientNumber()));
		license.setServiceId(getServiceId());
		license.setSubserviceId(getSubServiceId());

		license.setKmNo(getKmNo());
		if(getZoneNo() != null){
			license.setZoneNo(getZoneNo().toString());
		}
		license.setZoneName(getZoneName());

		try {

			if(getKmDate() != null){
				license.setKmDate(DateUtil.convertStringToDate(getKmDate()));
			}
			license.setLicenceBeginDate(DateUtil
					.convertStringToDate(getLicenceBeginDate()));
			license.setLicenceEndDate(DateUtil
					.convertStringToDate(getLicenceEndDate()));
			license.setCurrentBeginDate(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
			license.setCurrentEndDate(DateUtil
					.convertStringToDate(getCurrentEndDate()));
			license.setPaymentDueDate(DateUtil
					.convertStringToDate(getPaymentDueDate()));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		license.setFreqRMin(getFreqRMin());
		license.setFreqRMax(getFreqRMax());
		license.setFreqTMin(getFreqTMin());
		license.setFreqTMax(getFreqTMax());
		license.setPaymentType(getPaymentType());

		license.setLicenceStatus("D");

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();
		}

		try {
			dateBegin.setTime(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
			dateEnd.setTime(DateUtil.convertStringToDate(getCurrentEndDate()));
			datePayment.setTime(DateUtil
					.convertStringToDate(getPaymentDueDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Invoice> invoiceList = new ArrayList<Invoice>();

		List<BankGuarantee> bankGuaranteeList = new ArrayList<BankGuarantee>();

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());

		for (int i = 0; i < 10; i++) {

			invoiceList.add(new Invoice());
			Invoice invoice = invoiceList.get(i);
			invoice.setInvoiceNo(null);
			invoice.setTLicence(license);
			invoice.setYearTo(new BigDecimal(i + 1));
			invoice.setInvCreatedDate(new Date());
			if (getBhpHl() == null) {
				invoice.setBhpPhl(auctionPrice);
			} else if (getBhpPhl() == null) {
				invoice.setBhpHl(auctionPrice);

			}

			invoice.setBhpAnnual(auctionPrice);

			if (i + 1 == 1) {

				invoice.setPaymentDueDate(datePayment.getTime());
				invoice.setInvBeginDate(dateBegin.getTime());
				invoice.setInvEndDate(dateEnd.getTime());
				invoice.setBhpUpfrontFee(getBhpUpfrontFee());
				invoice.setBhpTotal(getBhpTotal());

				invoice.setSaveStatus("D");

			} else {
				dateBegin.add(Calendar.YEAR, 1);
				dateEnd.add(Calendar.YEAR, 1);
				datePayment.add(Calendar.YEAR, 1);
				invoice.setInvBeginDate(dateBegin.getTime());
				invoice.setInvEndDate(dateEnd.getTime());
				invoice.setPaymentDueDate(datePayment.getTime());
				invoice.setBhpTotal(auctionPrice);

				invoice.setSaveStatus("C");

			}
			if (getBgAvailableStatus() == null) {
				invoice.setBgTotal(null);

			} else if (getBgAvailableStatus().equals("Y") && i > 0) {
				Calendar bgDueDate = Calendar.getInstance();
				bgDueDate.setTime(getBgDueDate());
				bgDueDate.add(Calendar.YEAR,i - 1);
				
				invoice.setBgTotal(getBgTotal());
				
				bankGuaranteeList.add(new BankGuarantee());

				BankGuarantee bgToSaved = bankGuaranteeList.get(i - 1);
				bgToSaved.setBhpValue(auctionPrice);
				bgToSaved.setBgValue(getBgTotal());
				bgToSaved.setTInvoice(invoiceList.get(i));
				bgToSaved.setTLicence(license);
				bgToSaved.setCreatedOn(new Date());
				bgToSaved.setSaveStatus("C");
				bgToSaved.setReceivedStatus("3");
				bgToSaved.setSubmitYearTo(new BigDecimal(i));
				bgToSaved.setSubmitDueDate(bgDueDate.getTime());
				
				if(getUserLoginFromSession() != null){
					bgToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
				}else if(getUserLoginFromDatabase() != null){
					bgToSaved.setCreatedBy(getUserLoginFromDatabase().getUsername());
				}

			} else {
				invoice.setBgTotal(null);

			}
			invoice.setInvoiceType("1");
			invoice.setCreatedOn(new Date());
//			invoice.setCreatedBy(getUserLoginFromSession().getUsername());
			if(getUserLoginFromSession() != null){
				invoice.setCreatedBy(getUserLoginFromSession().getUsername());
			}else if(getUserLoginFromDatabase() != null){
				invoice.setCreatedBy(getUserLoginFromDatabase().getUsername());
			}
			invoice.setInvoiceStatus("D");

		}

		license.setTInvoices(invoiceList);
		if (getBgAvailableStatus() == null) {
			license.setBgAvailableStatus("N");
			license.setBgDueDate(null);
			license.setTBankGuarantees(null);
		} else if (getBgAvailableStatus().equals("Y")) {
			license.setBgAvailableStatus("Y");
			license.setBgDueDate(getBgDueDate());
			license.setTBankGuarantees(bankGuaranteeList);
		} else {
			license.setBgAvailableStatus("N");
			license.setTBankGuarantees(null);
			license.setBgDueDate(null);

		}
		license.setCreatedOn(new Date());
//		license.setCreatedBy(getUserLoginFromSession().getUsername());
		if(getUserLoginFromSession() != null){
			license.setCreatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			license.setCreatedBy(getUserLoginFromDatabase().getUsername());
		}
		getLicenseManager().save(license);

		Long invID = null;
		for (Invoice invobj : invoiceList) {

			if (invobj.getYearTo().intValue() == 1) {
				invID = invobj.getInvoiceId();
			}
		}

		System.out.println("InvoiceID Tahun Pertama = " + invID);
		System.out.println("KM File Name Tahun Pertama = " + getDocumentName());

		Date dateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
		String timestamp = formatter.format(dateTime);
		String fileName = getDocumentName();
		fileName = fileName.replaceAll("\\s+", "_");
		int index = fileName.indexOf("pdf") - 1;
		if (index > 0) {
			fileName = fileName.substring(0, index) + timestamp + ".pdf";
		}
		FileOutputStream fileOuputStream;

		File dirTest = new File("webapps");
		if (!dirTest.exists()) {
			dirTest.mkdir();
		}

		File dirTest2 = new File("webapps/SKM");
		if (!dirTest2.exists()) {
			dirTest2.mkdir();
		}

		if (getDocument() != null) {

			try {
				fileOuputStream = new FileOutputStream(DIRECTORY_DOCUMENT
						+ fileName);

				fileOuputStream.write(getDocument());
				fileOuputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		DocumentUpload doc = new DocumentUpload();
		doc.setDocDesc("Upload From Initial Invoice Flat BHP");
		doc.setCreatedOn(new Date());
//		doc.setCreatedBy(getUserLoginFromSession().getUserName());
		if(getUserLoginFromSession() != null){
			doc.setCreatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			doc.setCreatedBy(getUserLoginFromDatabase().getUsername());
		}
		doc.setFileName(fileName);
		doc.setYearTo(getYearTo().intValue());
		doc.setLicenseNo(license.getLicenceNo());
		doc.setReferenceId(String.valueOf(invID));
		doc.setFileDir(DIRECTORY_DOCUMENT);
		doc.setDocType("1");
		doc.setvUploadId("IN-1" + invID);
		getDocumentUploadManager().saveDocument(doc);

		getFields().remove("BHP_LIST");
		getFields().clear();

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { getKmNo(), getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		//Save to audit log
		getUserManager().saveInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "draft", appLicense.getLicenceNumber(), null, appLicense.getBhpMethod());

		cycle.activate(infoPage);

	}

	public void doSubmit(IRequestCycle cycle) {
		
		ApplicationBandwidth appLicense = getApplicationBandwidthManager()
				.findByLicenceNo(getLicenceNumber());
		
		String checkBgDueDate = checkBgDueDate();
		
		if(checkBgDueDate != null){
			addError(getDelegate(), "errorShadow", checkBgDueDate, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String checkLicense = checkLicense(appLicense);
		
		if (checkLicense != null) {
			addError(getDelegate(), "errorShadow", checkLicense, ValidationConstraint.CONSISTENCY);
			System.out.println(checkLicense);
			return;
		}

		if (getFields().get("BHP_LIST") == null) {
			addError(getDelegate(), "errorShadow", "BHP harus dihitung",
					ValidationConstraint.CONSISTENCY);
			return;
		}

		if (getBhpUpfrontFee() == null && getBhpHl() == null) {
			
			addError(getDelegate(), "errorShadow",
					"Upfrontfee dan Harga Lelang harus diisi",
					ValidationConstraint.CONSISTENCY);

			return;
		} else if (getBhpUpfrontFee() == null && getBhpHl() != null) {
		
			addError(getDelegate(), "errorShadow", "Upfrontfee harus diisi",
					ValidationConstraint.CONSISTENCY);

			return;

		} else if (getBhpUpfrontFee() != null && getBhpHl() == null
				&& getBhpPhl() == null) {
			
			addError(getDelegate(), "errorShadow", "Harga Lelang harus diisi",
					ValidationConstraint.CONSISTENCY);

			return;
		}
		

		Calendar dateBegin = Calendar.getInstance();
		Calendar dateEnd = Calendar.getInstance();
		Calendar datePayment = Calendar.getInstance();

		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(appLicense.getClientNumber()));
		license.setServiceId(getServiceId());
		license.setSubserviceId(getSubServiceId());

		license.setKmNo(getKmNo());
		if(getZoneNo() != null){
			license.setZoneNo(getZoneNo().toString());
		}
		license.setZoneName(getZoneName());

		try {

			if(getKmDate() != null){
				license.setKmDate(DateUtil.convertStringToDate(getKmDate()));
			}
			license.setLicenceBeginDate(DateUtil
					.convertStringToDate(getLicenceBeginDate()));
			license.setLicenceEndDate(DateUtil
					.convertStringToDate(getLicenceEndDate()));
			license.setCurrentBeginDate(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
			license.setCurrentEndDate(DateUtil
					.convertStringToDate(getCurrentEndDate()));
			license.setPaymentDueDate(DateUtil
					.convertStringToDate(getPaymentDueDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		license.setFreqRMin(getFreqRMin());
		license.setFreqRMax(getFreqRMax());
		license.setFreqTMin(getFreqTMin());
		license.setFreqTMax(getFreqTMax());
		license.setPaymentType(getPaymentType());
		license.setLicenceStatus("S");

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}

		try {
			dateBegin.setTime(DateUtil
					.convertStringToDate(getCurrentBeginDate()));
			dateEnd.setTime(DateUtil.convertStringToDate(getCurrentEndDate()));
			datePayment.setTime(DateUtil
					.convertStringToDate(getPaymentDueDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Invoice> invoiceList = new ArrayList<Invoice>();

		List<BankGuarantee> bankGuaranteeList = new ArrayList<BankGuarantee>();

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());


		Map invoiceMap = getInvoiceManager().generateInvoice(
				getLicenceNumber(), getBhpTotal(), datePayment.getTime());
		
		if (invoiceMap.get("invoiceNo") == null ||invoiceMap.get("letterID") == null || invoiceMap.get("invoiceErrorCode") == null ){
			
			addError(getDelegate(), "errorShadow", getText("initialInvoice.license.callStoredProchedure.failed", new Object[] {invoiceMap.get("invoiceNo"),
					invoiceMap.get("letterID"), invoiceMap.get("invoiceErrorCode")}) , ValidationConstraint.CONSISTENCY);
			return;
		}
		String invoiceNO = invoiceMap.get("invoiceNo").toString();
		String letterId = invoiceMap.get("letterID").toString();
		String errorCode = invoiceMap.get("invoiceErrorCode").toString();

		System.out.println("GENERATED INVOICE NUMBER = " + invoiceNO);
		System.out.println("GENERATED  LETTER ID = " + letterId);
		System.out.println("GENERATED ERROR CODE = " + errorCode);
		
		String errorCodeMessage = checkErrorCode(errorCode);
		
		if(errorCodeMessage != null){
			addError(getDelegate(), "errorShadow", errorCodeMessage,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		BigDecimal letterID = new BigDecimal(letterId);

		for (int i = 0; i < 10; i++) {

			invoiceList.add(new Invoice());
			Invoice invoice = invoiceList.get(i);
			invoice.setInvoiceNo(null);
			invoice.setTLicence(license);
			invoice.setYearTo(new BigDecimal(i + 1));
			invoice.setInvCreatedDate(new Date());
			if (getBhpHl() == null) {
				invoice.setBhpPhl(auctionPrice);
			} else if (getBhpPhl() == null) {
				invoice.setBhpHl(auctionPrice);

			}

			invoice.setBhpAnnual(auctionPrice);

			if (i + 1 == 1) {
				invoice.setInvoiceNo(invoiceNO);
				invoice.setPaymentDueDate(datePayment.getTime());
				invoice.setInvBeginDate(dateBegin.getTime());
				invoice.setInvEndDate(dateEnd.getTime());
				invoice.setBhpUpfrontFee(getBhpUpfrontFee());
				invoice.setBhpTotal(getBhpTotal());
				invoice.setSaveStatus("S");
				invoice.setInvoiceStatus("U");
				invoice.setLetterID(letterID);

			} else if (i + 1 == 2) {
				dateBegin.add(Calendar.YEAR, 1);
				dateEnd.add(Calendar.YEAR, 1);
				datePayment.add(Calendar.YEAR, 1);

				invoice.setInvoiceNo(null);
				invoice.setInvoiceStatus("D");
				invoice.setInvBeginDate(dateBegin.getTime());
				invoice.setInvEndDate(dateEnd.getTime());
				invoice.setPaymentDueDate(datePayment.getTime());
				invoice.setBhpTotal(auctionPrice);
				invoice.setSaveStatus("D");

			} else {
				invoice.setInvoiceNo(null);

				dateBegin.add(Calendar.YEAR, 1);
				dateEnd.add(Calendar.YEAR, 1);
				datePayment.add(Calendar.YEAR, 1);
				invoice.setInvBeginDate(dateBegin.getTime());
				invoice.setInvEndDate(dateEnd.getTime());
				invoice.setPaymentDueDate(datePayment.getTime());
				invoice.setBhpTotal(auctionPrice);
				invoice.setInvoiceStatus("D");
				invoice.setSaveStatus("C");

			}

			if (getBgAvailableStatus() == null) {
				invoice.setBgTotal(null);

			} else if (getBgAvailableStatus().equals("Y") && i > 0) {
				Calendar bgDueDate = Calendar.getInstance();
				bgDueDate.setTime(getBgDueDate());
				bgDueDate.add(Calendar.YEAR,i-1);
				
				invoice.setBgTotal(getBgTotal());
				
				bankGuaranteeList.add(new BankGuarantee());

				BankGuarantee bgToSaved = bankGuaranteeList.get(i-1);
				bgToSaved.setBhpValue(auctionPrice);
				if(i == 1){
//					bgToSaved.setInvoiceNo(invoiceNO);
					bgToSaved.setSaveStatus("D");
					bgToSaved.setReceivedStatus("0");
				}else{
					bgToSaved.setSaveStatus("C");
					bgToSaved.setReceivedStatus("3");
				}
				bgToSaved.setBgValue(getBgTotal());
				bgToSaved.setTInvoice(invoiceList.get(i));
				bgToSaved.setTLicence(license);
				bgToSaved.setCreatedOn(new Date());
				
				bgToSaved.setSubmitYearTo(new BigDecimal(i));
				bgToSaved.setSubmitDueDate(bgDueDate.getTime());
				
				bgToSaved.setBgValueDiff(new BigDecimal(0));
				bgToSaved.setBgValueSubmitted(new BigDecimal(0));
				bgToSaved.setClaimValue(new BigDecimal(0));

			} else {
				invoice.setBgTotal(null);

			}

			invoice.setInvoiceType("1");
			invoice.setCreatedOn(new Date());
			invoice.setCreatedBy(getUserLoginFromSession().getUsername());
			
			if(getUserLoginFromSession() != null){
				invoice.setCreatedBy(getUserLoginFromSession().getUsername());
			}else if(getUserLoginFromDatabase() != null){
				invoice.setCreatedBy(getUserLoginFromDatabase().getUsername());
			}

		}

		license.setTInvoices(invoiceList);
		if (getBgAvailableStatus() == null) {
			license.setBgAvailableStatus("N");
			license.setBgDueDate(null);
			license.setTBankGuarantees(null);

		} else if (getBgAvailableStatus().equals("Y")) {
			license.setBgAvailableStatus("Y");
			license.setBgDueDate(getBgDueDate());
			license.setTBankGuarantees(bankGuaranteeList);
		} else {
			license.setBgAvailableStatus("N");
			license.setBgDueDate(null);
			license.setTBankGuarantees(null);

		}

		license.setCreatedOn(new Date());
//		license.setCreatedBy(getUserLoginFromSession().getUsername());
		
		if(getUserLoginFromSession() != null){
			license.setCreatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			license.setCreatedBy(getUserLoginFromDatabase().getUsername());
		}
		
		getLicenseManager().save(license);

		Long invID = null;
		for (Invoice invobj : invoiceList) {

			if (invobj.getYearTo().intValue() == 1) {
				invID = invobj.getInvoiceId();
			}
		}

		System.out.println("InvoiceID Tahun Pertama = " + invID);
		System.out.println("KM File Name Tahun Pertama = " + getDocumentName());

		Date dateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
		String timestamp = formatter.format(dateTime);
		String fileName = getDocumentName();
		fileName = fileName.replaceAll("\\s+", "_");
		int index = fileName.indexOf("pdf") - 1;
		if (index > 0) {
			fileName = fileName.substring(0, index) + timestamp + ".pdf";
		}
		FileOutputStream fileOuputStream;

		File dirTest = new File("webapps");
		if (!dirTest.exists()) {
			dirTest.mkdir();
		}

		File dirTest2 = new File("webapps/SKM");
		if (!dirTest2.exists()) {
			dirTest2.mkdir();
		}

		if (getDocument() != null) {

			try {
				fileOuputStream = new FileOutputStream(DIRECTORY_DOCUMENT
						+ fileName);

				fileOuputStream.write(getDocument());
				fileOuputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		DocumentUpload doc = new DocumentUpload();
		doc.setDocDesc("Upload From Initial Invoice Flat BHP");
		doc.setCreatedOn(new Date());
//		doc.setCreatedBy(getUserLoginFromSession().getUserName());
		if(getUserLoginFromSession() != null){
			doc.setCreatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			doc.setCreatedBy(getUserLoginFromDatabase().getUsername());
		}
		
		doc.setFileName(fileName);
		doc.setYearTo(getYearTo().intValue());
		doc.setLicenseNo(license.getLicenceNo());
		doc.setReferenceId(String.valueOf(invID));
		doc.setFileDir(DIRECTORY_DOCUMENT);
		doc.setDocType("1");
		doc.setvUploadId("IN-1" + invID);
		getDocumentUploadManager().saveDocument(doc);

		getFields().remove("BHP_LIST");
		getFields().clear();
		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		// addError(getDelegate(), "errorShadow",
		// getText("license.draft.success"), ValidationConstraint.CONSISTENCY);
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		//Save to audit log
		getUserManager().saveInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "submit", appLicense.getLicenceNumber(), invoiceNO, appLicense.getBhpMethod());

		cycle.activate(infoPage);

	}
	
	public String checkBgDueDate(){
		String error = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		if(getIsBGAvailable() != null && getIsBGAvailable().equalsIgnoreCase("Y")){
			if (getBgDueDate() == null) {
				error =  getText("initialInvoice.license.empty.bgDueDate");
			}
			
			else if(getBgDueDate().before(cal.getTime())){
				error =  getText("initialInvoice.license.lessThanNow.bgDueDate");
			}
		}
		
		return error;
	}

	public void doCancel(IRequestCycle cycle) {
		InitialInvoiceSearch initialinvoiceSearch = (InitialInvoiceSearch) cycle
				.getPage("initialInvoiceSearch");
		

		getFields().put("APPLICATION_LIST", (List<ApplicationBandwidth>) getFields().get("APPLICATION_LIST_IN_INITIAL_FR"));
		
		getFields().put("criteria", getCriteria());
		getFields().put("criteriaSearch", getCriteriaSearch());
		getFields().put("initialPage", "yes");
		initialinvoiceSearch.setCriteria(getCriteria());
		
		if(getCriteria().equalsIgnoreCase("clientName")){
			initialinvoiceSearch.setClientName(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("clientNo")){
			initialinvoiceSearch.setClientNo(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("bhpMethod")){
			initialinvoiceSearch.setBhpMethod(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("noApp")){
			initialinvoiceSearch.setLicenceNumber(getCriteriaSearch());
		}
		
		getFields().remove("BHP_LIST");

		cycle.activate(initialinvoiceSearch);
	}

	public void doPrint(IRequestCycle cycle) {
		InitialInvoiceSearch initialinvoiceSearch = (InitialInvoiceSearch) cycle
				.getPage("initialInvoiceSearch");
		getFields().remove("BHP_LIST");
		getFields().clear();

		cycle.activate(initialinvoiceSearch);
	}

	private String saveValidation() {

		String errorMessage = null;

		if (getBhpUpfrontFee() == null) {
			errorMessage = getText("initial.invoice.upfrontfee.error");
		} else if (getBhpHl() == null) {
			errorMessage = getText("initial.invoice.auction.error");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}
	
	public String checkLicense(ApplicationBandwidth appBandMan){
		String errorMessage = null;
		
		/*Berdasarkan perubahan request pada tanggal 13-april-2018
		 * validasi :
		 * 1. dokumen keputusan menteri kosong
		 * 2. Nama keputusan menteri kosong
		 * 3. Tanggal keputusan menteri kosong 
		 * 4. Nomor Keputusan menteri kosong
		 * dihapus
		 */
		
		if(appBandMan.getLicenceNumber() == null){
			String error =  getText("initialInvoice.license.empty.licenseNumber");
			System.out.println("Test" + error);
			errorMessage = error;
			
		}
		
		/*if(appBandMan.getKmNo() == null){
			String error =  getText("initialInvoice.license.empty.kmNo");
			System.out.println("Test" +error);
			
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " +  error;
			}
		}*/
		
		if(appBandMan.getBhpMethod()== null){
			String error =  getText("initialInvoice.license.empty.bhpMethod");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getClientNumber() == null){
			String error =  getText("initialInvoice.license.empty.clientNumber");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getCurrentBeginDate() == null){
			
			String error =  getText("initialInvoice.license.empty.currentBeginDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getCurrentEndDate() == null){
			String error =  getText("initialInvoice.license.empty.currentEndDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getLiBeginDate() == null){
			String error =  getText("initialInvoice.license.empty.licBeginDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getLiEndDate() == null){
			String error =  getText("initialInvoice.license.empty.licEndDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getPaymentDueDate() == null){
			String error =  getText("initialInvoice.license.empty.paymentDueDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getIsBgAvailable() == null){
			String error =   getText("initialInvoice.license.empty.isBgAvailable");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getSvId() == null){
			String error =   getText("initialInvoice.license.empty.svid");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getSsId() == null){
			String error =  getText("initialInvoice.license.empty.ssid");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		/*if(appBandMan.getKmDoc() == null){
			String error =   getText("initialInvoice.license.empty.kmDoc");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}*/
		
		/*if(appBandMan.getKmFileName() ==  null){
			String error =  getText("initialInvoice.license.empty.kmFileName");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}*/
		
		/*if(appBandMan.getKmDate() == null){
			String error =  getText("initialInvoice.license.empty.kmDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}*/
		
		if(appBandMan.getBhpPaymentType() == null){
			String error =  getText("initialInvoice.license.empty.paymentType");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getStatusDb().intValue() == 0){
			String error =  getText("initialInvoice.license.empty.statusDb");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getClientCompany() == null){
			String error =  getText("initialInvoice.license.empty.clientCompany");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getIsBgAvailable() != null && appBandMan.getIsBgAvailable().equalsIgnoreCase("Y")){
			if (getBgDueDate() == null) {
				String error =  getText("initialInvoice.license.empty.bgDueDate");
				System.out.println("Test" +error);
				if(errorMessage == null){
					errorMessage = error;
				}else{
					errorMessage =  errorMessage + ", " + error;
				}
			}
		}
		
		if(errorMessage != null){
			String opening = getText("initialInvoice.license.opening");
			String closing = getText("initialInvoice.license.closing");
			return opening + " " + errorMessage + closing;
		}else{
			return errorMessage;
		}
	}
	
	public String checkErrorCode(String errorCode){
		String errorMessage = null;
		
		if (errorCode == null) {
			errorMessage = getText("initialInvoice.license.errorCode.empty");
			
		} else if (errorCode.equals("P1")) {
			errorMessage = getText("initialInvoice.license.errorCode.p1");
			
		} else if (errorCode.equals("P2")) {
			errorMessage = getText("initialInvoice.license.errorCode.p2");

		} else if (errorCode.equals("P3")) {
			errorMessage = getText("initialInvoice.license.errorCode.p3");

		} else if (errorCode.equals("P4")) {
			errorMessage = getText("initialInvoice.license.errorCode.p4");

		} else if (errorCode.equals("P5")) {
			errorMessage = getText("initialInvoice.license.errorCode.p5");

		} else if (errorCode.equals("P6")) {
			errorMessage = getText("initialInvoice.license.errorCode.p6");

		} else if (errorCode.equals("P7")) {
			errorMessage = getText("initialInvoice.license.errorCode.p7");

		} else if (errorCode.equals("P8")) {
			errorMessage = getText("initialInvoice.license.errorCode.p8");

		} else if (errorCode.equals("P9")) {
			errorMessage = getText("initialInvoice.license.errorCode.p9");

		} else if (errorCode.equals("P10")) {
			errorMessage = getText("initialInvoice.license.errorCode.p10");

		} else if (errorCode.equals("P11")) {
			errorMessage = getText("initialInvoice.license.errorCode.p11");

		} else if (errorCode.equals("P12")) {
			errorMessage = getText("initialInvoice.license.errorCode.p12");

		}
		
		if(errorMessage != null){
			String opening = getText("initialInvoice.license.opening");
			String closing = getText("initialInvoice.license.closing");
			return opening + " " + errorMessage + closing;
		}else{
			return errorMessage;
		}
	}
}
