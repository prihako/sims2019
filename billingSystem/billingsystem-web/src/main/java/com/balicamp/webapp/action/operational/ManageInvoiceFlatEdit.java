package com.balicamp.webapp.action.operational;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.request.IUploadFile;
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
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.UploadHelper;

public abstract class ManageInvoiceFlatEdit extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	protected final Log log = LogFactory.getLog(ManageInvoiceFlatEdit.class);

	public ManageInvoiceFlatEdit() {

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

	public abstract String getClientNO();

	public abstract void setClientNO(String bigdecimal);

	public abstract void setMethodBHP(String s);

	public abstract String getMethodBHP();

	public abstract void setPaymentTypeMethod(String s);

	public abstract String getPaymentTypeMethod();

	public abstract String getZoneName();

	public abstract void setZoneName(String s);

	public abstract BigDecimal getZoneNo();

	public abstract void setZoneNo(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqRMax();

	public abstract void setFreqRMax(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqRMin();

	public abstract void setFreqRMin(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqTMax();

	public abstract void setFreqTMax(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqTMin();

	public abstract void setFreqTMin(BigDecimal bigdecimal);

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
//
//	public abstract void setBgDueDate(Date date);

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
	
	public abstract String getSaveStatus();
	public abstract void setSaveStatus(String invoiceStatus);

	public abstract String getInvoiceType();

	public abstract void setInvoiceType(String invoiceType);

	public abstract String getInvoiceStatusText();

	public abstract void setInvoiceStatusText(String invoiceStatusText);

	public abstract String getInvoiceTypeText();

	public abstract void setInvoiceTypeText(String invoiceTypeText);

	public abstract BigDecimal getMonthTo();

	public abstract void setMonthTo(BigDecimal monthTo);

	public abstract String getPaymentDate();

	public abstract void setPaymentDate(String paymentDate);

	public abstract String getInvoiceCreateDate();

	public abstract void setInvoiceCreateDate(String invoiceCreateDate);

	public abstract String getInvoiceComment();

	public abstract void setInvoiceComment(String invoiceComment);

	public abstract ApplicationBandwidth getApplication();

	public abstract Invoice getInvoice();

	public abstract List<Invoice> getInvoiceList();

	public abstract void setInvoiceList(List<Invoice> invoiceList);

	public abstract License getLicense();

	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	public abstract VariableManager getVariableManager();

	public abstract VariableDetailManager getVariableDetailManager();
	
	public abstract String getBgSubmitDueDate();

	public abstract void setBgSubmitDueDate(String date);
	
	public abstract BankGuarantee getBankGuaranteeRow();

	public abstract void setBankGuaranteeRow(BankGuarantee bg);

	private IUploadFile fileSkmIpsfr;
	private String filename;

	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";

	public IUploadFile getFile() {
		return fileSkmIpsfr;
	}

	public void setFile(IUploadFile fileSkmIpsfr) {
		this.fileSkmIpsfr = fileSkmIpsfr;
	}

	public String getFilename() {
		if (fileSkmIpsfr != null) {
			setFilename(fileSkmIpsfr.getFileName());
			return fileSkmIpsfr.getFileName();
		} else {
			return "";
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();

	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();
	
	public abstract void setInvoiceStatusCache(String stat);
	
	public abstract String getInvoiceStatusCache();

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		// setDocument(getDocument());

	}

	public void pageEndRender(PageEvent pageevent) {
		super.pageEndRender(pageevent);
	}

	public List<Invoice> getBhpList() {
		List bhpList = (List) getFields().get("BHP_LIST");
		return bhpList;
	}
	
	public List<BankGuarantee> getBankGuaranteeList() {
		List<BankGuarantee> bgList = (List<BankGuarantee>) getFields().get("BANK_GUARANTEE_LIST_FR_EDIT");
		return bgList;
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

	private static final String OUTPUT_FILE_NAME = "C:\\Users\\ALDERI\\Desktop\\SKM\\";

	public void hitung() {
		System.out.println("UpFrontFee = " + getBhpUpfrontFee());
		System.out.println("Harga Lelang = " + getBhpHl());
		System.out.println("Penyesuaian Harga Lelang = " + getBhpPhl());
		System.out.println("Klien ID = " + getClientNO());
		System.out.println("Invoice ID = " + getInvoiceID());
//		System.out.println("Save Status = " + getSaveStatus());
		System.out.println("getBgAvaliableStatus " + getBgAvailableStatus());


		BigDecimal yearto = new BigDecimal(1);
		if (getYearTo().intValue() == 1) {
			System.out.println("1.Tahun " +getYearTo());
			System.out.println("1.Tahun " +yearto);
			if (getBhpUpfrontFee() == null && getBhpHl() == null) {
				addError(getDelegate(), "errorShadow",
						"Upfrontfee dan Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			} 
			
			if (getBhpUpfrontFee() == null && getBhpHl() != null) {
				addError(getDelegate(), "errorShadow",
						"Upfrontfee harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			} 
			
			if (getBhpUpfrontFee() != null && getBhpHl() == null && getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang atau PHL  harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}
			
			
			
			if (getBhpHl() == null && getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang atau PHL harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}
			
			if (getBhpHl() == null && getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang atau PHL harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}
			
		} else if (getYearTo().intValue() != 1) {
			
			System.out.println("2.Tahun " +getYearTo());
			System.out.println("2.Tahun " +yearto);

			if (getBhpHl() == null && getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang atau PHL harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			} 
			
			if (getBhpHl() != null && getBhpPhl() == null) {

				addError(getDelegate(), "errorShadow", " PHL harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}

		}

		// Filename validation, because always pass, so i hardcode this
		if (getFile() != null) {
			String fileName = getFile().getFileName();
			int length = fileName.length();
			String subString = fileName.substring(length - 4, length);
			if (!(subString.equals(".pdf"))) {
				String errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.notPdf");
				addError(getDelegate(), "errorShadow", errorMessage,
						ValidationConstraint.CONSISTENCY);
				return;
			}
		}

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null && getBhpPhl() != null) {
			auctionPrice = getBhpPhl();
			System.out.println("auction price PHL "+getBhpPhl());
		} else if (getBhpPhl() == null && getBhpHl() != null) {
			auctionPrice = getBhpHl();
			System.out.println("auction price HL "+getBhpHl());

		} else if (getBhpPhl() != null && getBhpHl() != null
				&& getYearTo().intValue() != 1) {
			auctionPrice = getBhpPhl();
			System.out.println("auction price PHL "+getBhpPhl());

		}

		BigDecimal bhpTotal1 = null;

		if (getBhpUpfrontFee() != null && getYearTo().intValue() == 1) {
			bhpTotal1 = getBhpUpfrontFee().add(auctionPrice);
		} else if (getBhpUpfrontFee() != null && getYearTo().intValue() != 1) {
			bhpTotal1 = auctionPrice;
		}

		Invoice invoices = new Invoice();
		License license = new License();
		license.setLicenceNo(getLicenceNumber());
		license.setKmNo(getKmNo());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(getClientNO()));

		setBhpTotal(bhpTotal1);
		setBhpAnnual(auctionPrice);

		List<Invoice> bhpList = (List<Invoice>) getFields().get("INVOICE_LIST");

		List<Invoice> invoiceList = new ArrayList<Invoice>();

		for (Invoice invoice : bhpList) {

			if (invoice.getSaveStatus().equals("S") && invoice.getInvoiceType().equals("1")) {
				invoiceList.add(invoice);
			} else {
				if (invoice.getYearTo().intValue() >= getYearTo().intValue() && invoice.getInvoiceType().equals("1")) {

					if (invoice.getYearTo().intValue() == 1) {

						invoice.setBhpUpfrontFee(getBhpUpfrontFee());
						if (getBhpHl() == null) {
							invoice.setBhpHl(null);
							invoice.setBhpPhl(getBhpPhl());
							invoice.setBhpAnnual(getBhpPhl());
							invoice.setBhpTotal(getBhpUpfrontFee().add(
									getBhpPhl()));

						} else if (getBhpPhl() == null) {
							invoice.setBhpPhl(null);
							invoice.setBhpHl(getBhpHl());
							invoice.setBhpAnnual(getBhpHl());
							invoice.setBhpTotal(getBhpUpfrontFee().add(
									getBhpHl()));
						}

					} else if (invoice.getYearTo().intValue() != 1) {
						invoice.setBhpUpfrontFee(null);

						if (getBhpHl() == null && getBhpPhl() != null) {
							invoice.setBhpHl(null);
							invoice.setBhpPhl(getBhpPhl());
							invoice.setBhpAnnual(getBhpPhl());
							invoice.setBhpTotal(getBhpPhl());

						} else if (getBhpPhl() == null && getBhpHl() != null) {
							invoice.setBhpPhl(null);
							invoice.setBhpHl(getBhpHl());
							invoice.setBhpAnnual(getBhpHl());
							invoice.setBhpTotal(getBhpHl());
						} else if (getBhpPhl() != null && getBhpHl() != null ) {
							invoice.setBhpPhl(auctionPrice);
							invoice.setBhpHl(auctionPrice);
							invoice.setBhpAnnual(auctionPrice);
							invoice.setBhpTotal(auctionPrice);
						}

					}
					invoiceList.add(invoice);
				}

			}

		}
		
		//For Bank Guarantee
		if(getBgAvailableStatus() != null && getBgAvailableStatus().equalsIgnoreCase("Y")){
			double bgPercent = 0.02;
			BigDecimal decimalBGPercent = new BigDecimal(Double.toString(bgPercent));
			BigDecimal bgAmount = auctionPrice.multiply(decimalBGPercent);
			BigDecimal bgAmountTotal = auctionPrice.add(bgAmount);
			List<BankGuarantee> bgList = (List<BankGuarantee>) getFields().get("BANK_GUARANTEE_LIST_FR");
			setBgTotal(bgAmountTotal);
			for(BankGuarantee bankGuarantee : bgList){
				if(bankGuarantee.getSubmitYearTo().intValue() >= getYearTo().intValue()){
					bankGuarantee.setBgValue(bgAmountTotal);
					bankGuarantee.setBhpValue(auctionPrice);
				}
			}
			
			getFields().put("BANK_GUARANTEE_LIST_FR_EDIT", bgList);
		}
		

		license.setTInvoices(invoiceList);

		setInvoiceList(invoiceList);

		saveDocument();

		getFields().put("BHP_LIST", invoiceList);

	}

	public void reset(IRequestCycle cycle) {
		getFields().remove("BHP_LIST");
		
		if(getFields().get("BANK_GUARANTEE_LIST_FR_EDIT") != null){
			getFields().remove("BANK_GUARANTEE_LIST_FR_EDIT");
		}
		
		setBhpAnnual(null);
		setBhpTotal(null);
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

	@SuppressWarnings("deprecation")
	public void doDraft(IRequestCycle cycle) {

		String errorMsg = checkBgDueDate();
		
		if(errorMsg != null){
			addError(getDelegate(), "errorShadow",errorMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		if (getYearTo().intValue() == 1) {
			if (getBhpUpfrontFee() == null && getBhpHl() == null
					&& getBhpHl() == null) {
				addError(getDelegate(), "errorShadow",
						"Upfrontfee dan Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			} else if (getBhpUpfrontFee() != null && getBhpHl() == null
					&& getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang/PHL  harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			} else if (getYearTo().intValue() != 1 && getBhpHl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}
		} else {

			if (getBhpHl() == null && getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			} else if (getBhpUpfrontFee() == null && getBhpHl() == null
					&& getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Upfrontfee dan Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}

			if (getInvoiceComment() == null) {
				addError(getDelegate(), "errorShadow", "Catatan harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			}
		}

		if (getFields().get("BHP_LIST") == null) {
			addError(getDelegate(), "errorShadow", "Anda harus melakukan perhitungan",
					ValidationConstraint.CONSISTENCY);

			return;

		}

		List<Invoice> invoiceList = getInvoiceManager().findInvoiceByLicenceID(
				getLicenceID());

		License license = getLicenseManager().findLicenseByID(getLicenceID());

		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		license.setClientNo(new BigDecimal(getClientNO()));

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

		license.setFreqRMin(getFreqRMin());
		license.setFreqRMax(getFreqRMax());
		license.setFreqTMin(getFreqTMin());
		license.setFreqTMax(getFreqTMax());
		license.setPaymentType(getPaymentType());
		// license.setLicenceStatus("D");

		BigDecimal auctionPrice = null;

		if (getBhpHl() == null && getBhpPhl() != null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null && getBhpHl() != null) {
			auctionPrice = getBhpHl();
		} else if (getBhpPhl() != null && getBhpHl() != null
				&& getYearTo().intValue() != 1) {
			auctionPrice = getBhpPhl();
		}

		BigDecimal bhpTotal = null;

		if (getBhpUpfrontFee() != null) {
			bhpTotal = getBhpUpfrontFee().add(auctionPrice);
		} else {
			bhpTotal = auctionPrice;
		}

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());
		System.out.println("NEW BHP UPFRONT FEE : " + getBhpUpfrontFee());
		System.out.println("NEW BHP TOTAL  : " + bhpTotal);

		List<Invoice> bhpList = new ArrayList<Invoice>();

		for (Invoice invoice : invoiceList) {

			if (invoice.getSaveStatus().equals("S")) {

				bhpList.add(invoice);

			} else {

				if (invoice.getYearTo().intValue() >= getYearTo().intValue()) {

					if (invoice.getYearTo().intValue() == 1) {
						invoice.setBhpUpfrontFee(getBhpUpfrontFee());
						if (getBhpHl() == null) {
							invoice.setBhpHl(null);
							invoice.setBhpPhl(getBhpPhl());
							invoice.setBhpAnnual(getBhpPhl());
							invoice.setBhpTotal(getBhpUpfrontFee().add(
									getBhpPhl()));

						} else if (getBhpPhl() == null) {
							invoice.setBhpPhl(null);
							invoice.setBhpHl(getBhpHl());
							invoice.setBhpAnnual(getBhpHl());
							invoice.setBhpTotal(getBhpUpfrontFee().add(
									getBhpHl()));
						}
					} else if (invoice.getYearTo().intValue() != 1) {
						invoice.setBhpUpfrontFee(null);

						if (invoice.getYearTo().intValue() == getYearTo()
								.intValue()) {
							invoice.setInvoiceComment(getInvoiceComment());
						} else {
							invoice.setInvoiceComment(null);

						}

						if (getBhpHl() == null && getBhpPhl() != null) {
							invoice.setBhpHl(null);
							invoice.setBhpPhl(getBhpPhl());
							invoice.setBhpAnnual(getBhpPhl());
							invoice.setBhpTotal(getBhpPhl());

						} else if (getBhpPhl() == null && getBhpHl() != null) {
							invoice.setBhpPhl(null);
							invoice.setBhpHl(getBhpHl());
							invoice.setBhpAnnual(getBhpHl());
							invoice.setBhpTotal(getBhpHl());
						} else if (getBhpPhl() != null && getBhpHl() != null ) {
							invoice.setBhpPhl(auctionPrice);
							invoice.setBhpHl(auctionPrice);
							invoice.setBhpAnnual(auctionPrice);
							invoice.setBhpTotal(auctionPrice);
						}

					}
					bhpList.add(invoice);

				}

			}

			invoice.setUpdatedOn(new Date());
			invoice.setUpdatedBy(getUserLoginFromSession().getUsername());
		}

		getInvoiceManager().saveList(bhpList);

		 if (getBgAvailableStatus().equals("Y")) {
		
//			BankGuarantee bg = getBankGuaranteeManager().findByInvoiceID(getLicenceID(), getInvoiceID());
//			bg.setBhpValue(auctionPrice);
//		 	bg.setBgValue(getBgTotal());
//		 	// bg.setTInvoice(invoices);
//		 	bg.setTLicence(license);
//		 	bg.setUpdatedOn(new Date());
//		 	bg.setUpdatedBy(getUserLoginFromSession().getUsername());
//		 	getBankGuaranteeManager().save(bg);
			 
			List<BankGuarantee> bgListToSaved = getBankGuaranteeManager().findByLicenseId(getLicenceID());
			for(BankGuarantee bankGuarantee : bgListToSaved){
				if(bankGuarantee.getSubmitYearTo().intValue() >= getYearTo().intValue()){
					bankGuarantee.setBgValue(getBgTotal());
					bankGuarantee.setBhpValue(auctionPrice);
					bankGuarantee.setUpdatedOn(new Date());
					bankGuarantee.setUpdatedBy(getUserLoginFromSession().getUsername());
					getBankGuaranteeManager().save(bankGuarantee);
				}
			}
		 }

		license.setUpdatedOn(new Date());
		license.setUpdatedBy(getUserLoginFromSession().getUsername());
		
		//Save to audit log
		getUserManager().saveInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "draft", license.getLicenceNo(), null, license.getBhpMethod());

		getLicenseManager().save(license);

		getFields().remove("BHP_LIST");
		if(getFields().get("BANK_GUARANTEE_LIST_FR_EDIT") != null){
			getFields().remove("BANK_GUARANTEE_LIST_FR_EDIT");
		}

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

	public void doSubmit(IRequestCycle cycle) throws ParseException {
		
		String errorMsg = checkBgDueDate();
		
		if(errorMsg != null){
			addError(getDelegate(), "errorShadow",errorMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		System.out.println("doSubmit() InvoiceID : " + getInvoiceID());
		System.out.println("doSubmit() LicenceID : " + getLicenceID());

		if (getYearTo().intValue() == 1) {
			if (getBhpUpfrontFee() == null && getBhpHl() == null
					&& getBhpHl() == null) {
				addError(getDelegate(), "errorShadow",
						"Upfrontfee dan Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			} else if (getBhpUpfrontFee() != null && getBhpHl() == null
					&& getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang/PHL  harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			} else if (getYearTo().intValue() != 1 && getBhpHl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}
		} else {

			if (getBhpHl() == null && getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			} else if (getBhpUpfrontFee() == null && getBhpHl() == null
					&& getBhpPhl() == null) {
				addError(getDelegate(), "errorShadow",
						"Upfrontfee dan Harga Lelang harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;

			}

			if (getInvoiceComment() == null) {
				addError(getDelegate(), "errorShadow", "Catatan harus diisi",
						ValidationConstraint.CONSISTENCY);

				return;
			}
		}

		if (getFields().get("BHP_LIST") == null) {
			addError(getDelegate(), "errorShadow", "Anda harus melakukan perhitungan",
					ValidationConstraint.CONSISTENCY);

			return;

		}

		License license = getLicenseManager().findLicenseByID(getLicenceID());
//		BankGuarantee bg = getBankGuaranteeManager().findByInvoiceID(
//				getLicenceID(), getInvoiceID());
		List<Invoice> invoiceList = getInvoiceManager().findInvoiceByLicenceID(
				getLicenceID());

		license.setLicenceNo(getLicenceNumber());
		license.setBhpMethod(getBhpMethod());
		license.setClientName(getClientName());
		// license.setClientNo(new BigDecimal(getClientID()));

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

		BigDecimal bhpTotal = getBhpUpfrontFee().add(auctionPrice);

		System.out.println("BG AVAILABLE STATUS : " + getBgAvailableStatus());
		System.out.println("NEW BHP UPFRONT FEE : " + getBhpUpfrontFee());

		Map invoiceMap = getInvoiceManager().generateInvoice(
				getLicenceNumber(), bhpTotal, DateUtil.convertStringToDate(getPaymentDueDate()));
		
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

		BigDecimal letterID = new BigDecimal(letterId);
		
		String errorCodeMessage = checkErrorCode(errorCode);
		
		if(errorCodeMessage != null){
			addError(getDelegate(), "errorShadow", errorCodeMessage,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		for (int i = 0; i < invoiceList.size(); i++) {

			Invoice invoices = invoiceList.get(i);

			if (invoices.getYearTo().intValue() == 1) {
				invoices.setInvoiceNo(invoiceNO);
				invoices.setInvoiceStatus("U");
				invoices.setLetterID(letterID);
			}

			if (invoices.getBhpUpfrontFee() != null) {
				invoices.setBhpUpfrontFee(getBhpUpfrontFee());
				if (getBhpHl() == null) {
					auctionPrice = getBhpPhl();
					invoices.setBhpPhl(auctionPrice);

				} else if (getBhpPhl() == null) {
					auctionPrice = getBhpHl();
					invoices.setBhpHl(auctionPrice);
				}
				invoices.setBhpAnnual(auctionPrice);
				invoices.setBhpTotal(bhpTotal);

				 if (license.getBgAvailableStatus().equals("Y")) {
					 invoices.setBgTotal(getBgTotal());
				 }

				invoices.setSaveStatus("S");
				invoices.setInvoiceComment(getInvoiceComment());
				invoices.setUpdatedOn(new Date());
				invoices.setUpdatedBy(getUserLoginFromSession().getUsername());

			} else if (invoices.getBhpUpfrontFee() == null
					&& invoices.getYearTo().intValue() == 2) {

				invoices.setSaveStatus("D");
				if (getBhpHl() == null) {
					auctionPrice = getBhpPhl();
					invoices.setBhpPhl(auctionPrice);

				} else if (getBhpPhl() == null) {
					auctionPrice = getBhpHl();
					invoices.setBhpHl(auctionPrice);
				}

				invoices.setBhpAnnual(auctionPrice);
				invoices.setBhpTotal(auctionPrice);

				if (license.getBgAvailableStatus().equals("Y")) {
					invoices.setBgTotal(getBgTotal());
				}
				invoices.setInvoiceComment(getInvoiceComment());
				invoices.setUpdatedOn(new Date());
				invoices.setUpdatedBy(getUserLoginFromSession().getUsername());

			} else {

				invoices.setSaveStatus("C");

				if (getBhpHl() == null) {
					auctionPrice = getBhpPhl();
					invoices.setBhpPhl(auctionPrice);

				} else if (getBhpPhl() == null) {
					auctionPrice = getBhpHl();
					invoices.setBhpHl(auctionPrice);
				}

				invoices.setBhpAnnual(auctionPrice);
				invoices.setBhpTotal(auctionPrice);
				
				if (license.getBgAvailableStatus().equals("Y")) {
					invoices.setBgTotal(getBgTotal());
				}
				invoices.setInvoiceComment(getInvoiceComment());
				invoices.setUpdatedOn(new Date());
				invoices.setUpdatedBy(getUserLoginFromSession().getUsername());
			}

		}
		getInvoiceManager().saveList(invoiceList);

		 if (getBgAvailableStatus().equals("Y")) {
		
//			 bg.setBhpValue(auctionPrice);
//			 bg.setBgValue(getBgTotal());
//			 // bg.setTInvoice(invoices);
//			 bg.setTLicence(license);
//			 bg.setUpdatedOn(new Date());
//			 bg.setUpdatedBy(getUserLoginFromSession().getUsername());
//			 bg.setReceivedStatus("0");
//			 bg.setSaveStatus("D");
//			 getBankGuaranteeManager().save(bg);
			 
			List<BankGuarantee> bgListToSaved = getBankGuaranteeManager().findByLicenseId(getLicenceID());
			for(BankGuarantee bankGuarantee : bgListToSaved){
				if(bankGuarantee.getSubmitYearTo().intValue() == 1){
					bankGuarantee.setReceivedStatus("0");
					bankGuarantee.setSaveStatus("D");
				}
				bankGuarantee.setBgValue(getBgTotal());
				bankGuarantee.setBhpValue(auctionPrice);
				bankGuarantee.setUpdatedOn(new Date());
				bankGuarantee.setUpdatedBy(getUserLoginFromSession().getUsername());
				getBankGuaranteeManager().save(bankGuarantee);
			}
		 }

		license.setUpdatedOn(new Date());
		license.setUpdatedBy(getUserLoginFromSession().getUsername());
		
		//Save to audit log
		getUserManager().saveInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "submit", license.getLicenceNo(), invoiceNO, license.getBhpMethod());

		getLicenseManager().save(license);

		getFields().remove("BHP_LIST");
		if(getFields().get("BANK_GUARANTEE_LIST_FR_EDIT") != null){
			getFields().remove("BANK_GUARANTEE_LIST_FR_EDIT");
		}

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { getKmNo(), getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);
	}
	
	public String checkBgDueDate(){
		String error = null;
		
		if(getIsBGAvailable() != null && getIsBGAvailable().equalsIgnoreCase("Y")){
			if (getBgSubmitDueDate() == null) {
				error =  getText("initialInvoice.license.empty.bgDueDate");
			}
//			else if(getBgDueDate().before(new Date())){
//				error =  getText("initialInvoice.license.lessThanNow.bgDueDate");
//			}
		}
		
		return error;
	}

	public void doCancel(IRequestCycle cycle) {
		ManageInvoiceSearch manageInvoiceSearch = (ManageInvoiceSearch) cycle
				.getPage("manageInvoiceSearch");
		getFields().remove("BHP_LIST");
		if(getFields().get("BANK_GUARANTEE_LIST_FR_EDIT") != null){
			getFields().remove("BANK_GUARANTEE_LIST_FR_EDIT");
		}
		
		getFields().put("LICENSE_LIST", (List) getFields().get("LICENSE_LIST_FR"));
		
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

		cycle.activate(manageInvoiceSearch);
	}

	public void saveDocument() {
		// For saving file
		if (fileSkmIpsfr != null) {

			String fileName = fileSkmIpsfr.getFileName();
			// int length = fileName.length();
			// String subString = fileName.substring(length-4, length);
			// if( !(subString.equals(".pdf")) ){
			// String errorMessage =
			// getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.notPdf");
			// addError(getDelegate(), "errorShadow", errorMessage,
			// ValidationConstraint.CONSISTENCY);
			// }

			System.out.println("Save Document , LicenceNumber= "
					+ getLicenceNumber() + " , invoice ID = " + getInvoiceID());
			DocumentUpload docUp = getDocumentUploadManager()
					.findAndDownload(String.valueOf(getInvoiceID()),
							String.valueOf(getLicenceNumber()),
							"IN-1" + getInvoiceID());

			if (docUp != null) {
				UploadHelper.deleteFile(docUp.getFileName(), docUp);
				getDocumentUploadManager().deleteDocument(docUp);
			}

			// For saving file
			if (fileSkmIpsfr != null) {
				Date dateTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"MMddyyyy_hmmss");
				String timestamp = formatter.format(dateTime);
				fileName = fileSkmIpsfr.getFileName();
				fileName = fileName.replaceAll("\\s+", "_");
				int index = fileName.indexOf("pdf") - 1;
				if (index > 0) {
					fileName = fileName.substring(0, index) + timestamp
							+ ".pdf";
				}
				UploadHelper.saveFile(fileName, fileSkmIpsfr);
				DocumentUpload doc = new DocumentUpload();
				doc.setDocDesc("Upload From Manage Invoice- Flat BHP");
				doc.setCreatedOn(new Date());
				doc.setCreatedBy(getUserLoginFromSession().getUserName());
				doc.setFileName(fileName);
				doc.setYearTo(getYearTo().intValue());
				doc.setLicenseNo(getLicenceNumber());
				doc.setReferenceId(String.valueOf(getInvoiceID()));
				doc.setFileDir(DIRECTORY_DOCUMENT);
				doc.setDocType("1");
				doc.setvUploadId("IN-1" + getInvoiceID());
				getDocumentUploadManager().saveDocument(doc);
			}

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
