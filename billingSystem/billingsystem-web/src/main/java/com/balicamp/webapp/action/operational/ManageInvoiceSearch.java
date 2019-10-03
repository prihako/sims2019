package com.balicamp.webapp.action.operational;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class ManageInvoiceSearch extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {

	protected final Log log = LogFactory.getLog(ManageInvoiceSearch.class);

	public abstract String getCriteria();

	public abstract void setCriteria(String criteria);

	public abstract String getBhpMethod();

	public abstract void setBhpMethod(String bhpMethod);

	public abstract String getClientName();

	public abstract void setClientName(String clientName);

	public abstract String getClientNo();

	public abstract void setClientNo(String clientNo);

	public abstract String getLicenceNumber();

	public abstract void setLicenceNumber(String licenceNumber);

	public abstract String getInvoiceStatus();

	public abstract void setInvoiceStatus(String invoiceStatus);

	public abstract License getLicense();

	public abstract Invoice getInvoice();

	public abstract Object getData();

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();

	@InjectObject("spring:bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();

	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (!pageEvent.getRequestCycle().isRewinding()) {
			
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else if (getFields() != null) {
					if(getFields().get("managePage") == null){
//						getFields().remove("LICENSE_LIST");
						getFields().clear();
					}else{
						getFields().remove("managePage");
					}
				}
			}
		}

	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public IPropertySelectionModel getCriteriaModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("clientName", "Nama Klien");
		map.put("clientNo", "Klien Id");
		map.put("bhpMethod", "Metode BHP");
		map.put("noApp", "No. Aplikasi");

		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getMethodBHPModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FR", "Flat BHP");
		map.put("VR", "Variety Rate");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getInvoiceStatusModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("A", "All");
		map.put("P", "Paid");
		map.put("U", "Unpaid");
		map.put("D", "Draft");
		map.put("C", "Cancel");
		map.put("BD", "Bad Debt");

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public List getLicenseList() {
		List list = (List) getFields().get("LICENSE_LIST");
		return list;
	}

	public void doSearch() {
		List<Object> obj;
		Pattern alfa 		= Pattern.compile("[a-zA-Z]");
		Pattern other 		= Pattern.compile("[']");

		String errorMessage	= null;
		
		System.out.println("Cari Invoice Status " + getInvoiceStatus());
		
		getFields().put("criteria", getCriteria());
		getFields().put("invoiceStatus", getInvoiceStatus());

		if (getClientName() != null) {
			System.out.println("Cari ClientName " + getClientName());

			if(other.matcher(getClientName()).find()){
				errorMessage = getText("manageInvoiceSearch.notWord");
				addError(getDelegate(), "errorShadow",errorMessage, ValidationConstraint.CONSISTENCY);
				return;
			}
			
			obj = getLicenseManager().findInvoiceByClientName(getClientName(), getInvoiceStatus());

			getFields().put("LICENSE_LIST", obj);
			getFields().put("criteriaSearch", getClientName());

		} else if (getClientNo() != null) {

			System.out.println("Client NO = " + getClientNo());

			if(other.matcher(getClientNo()).find()){
				errorMessage = getText("manageInvoiceSearch.notWord");
				addError(getDelegate(), "errorShadow",errorMessage, ValidationConstraint.CONSISTENCY);
				return;
			}
			
			if(alfa.matcher(getClientNo()).find()){
				errorMessage = getText("manageInvoiceSearch.clintId.notNumber");
				addError(getDelegate(), "errorShadow",errorMessage, ValidationConstraint.CONSISTENCY);
				return;
			}
			
			obj = getLicenseManager().findInvoiceByClientID(getClientNo(),
					getInvoiceStatus());

			getFields().put("LICENSE_LIST", obj);
			getFields().put("criteriaSearch",  getClientNo());

		} else if (getLicenceNumber() != null) {
			System.out.println("Cari LicenceNumber " + getLicenceNumber());

			if(other.matcher(getLicenceNumber()).find()){
				errorMessage = getText("manageInvoiceSearch.notWord");
				addError(getDelegate(), "errorShadow",errorMessage, ValidationConstraint.CONSISTENCY);
				return;
			}
			
			obj = getLicenseManager().findInvoiceByLicenceNo(
					getLicenceNumber(), getInvoiceStatus());
			getFields().put("LICENSE_LIST", obj);
			getFields().put("criteriaSearch", getLicenceNumber());

		} else if (getBhpMethod() != null) {
			System.out.println("Cari BHPMethod " + getBhpMethod());

			if(other.matcher(getBhpMethod()).find()){
				errorMessage = getText("manageInvoiceSearch.notWord");
				addError(getDelegate(), "errorShadow",errorMessage, ValidationConstraint.CONSISTENCY);
				return;
			}
			
			obj = getLicenseManager().findInvoiceByMethod(getBhpMethod(),
					getInvoiceStatus());

			getFields().put("LICENSE_LIST", obj);
			getFields().put("criteriaSearch", getBhpMethod());

		}
		
		getUserManager().viewManageInvoiceSearch(initUserLoginFromDatabase(), getRequest().getRemoteHost());
	}

	/**
	 * @param cycle
	 * @param licenceID
	 * @param invoiceID
	 * @param bhpMethod
	 * @param yearTo
	 * @param licenceNo
	 * @param invoiceNo
	 * @param invoiceType
	 */
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
				if (invoiceObject.getBhpTotalOri()==null) {
					view.setBhpTotalOri(invoiceObject.getBhpTotal());
				} else {
					view.setBhpTotalOri(invoiceObject.getBhpTotalOri());
				}
				
				view.setInvoiceNo(invoiceObject.getInvoiceNo());
				view.setInvoiceStatus(invoiceObject.getInvoiceStatus());
				view.setInvoiceComment(invoiceObject.getInvoiceComment());
				view.setInvoiceCommentBhp(invoiceObject.getInvoiceCommentBhp());
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

}
