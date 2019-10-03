package com.balicamp.webapp.action.operational;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.License;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class InitialInvoiceSearch extends AdminBasePage
		implements PageBeginRenderListener, PageEndRenderListener {
	
	protected final Log log = LogFactory.getLog(InitialInvoiceSearch.class);
	
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
	
	public abstract ApplicationBandwidth getApplication();
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();
	

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if (!pageEvent.getRequestCycle().isRewinding()) {

			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else if (getFields() != null ) {
					if(getFields().get("initialPage") == null){
						getFields().clear();
//						getFields().remove("APPLICATION_LIST");
					}else{
						getFields().remove("initialPage");
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
	
//	public abstract List<ApplicationBandwidth> getApplicationList2();
//	public abstract void setApplicationList2(List<ApplicationBandwidth> li);
	
	public List getApplicationList() {
		List<ApplicationBandwidth> list = (List<ApplicationBandwidth>) getFields()
				.get("APPLICATION_LIST");
		return list;
	}
	
	public void doSearch(IRequestCycle cycle){
		List<ApplicationBandwidth> list;
		
		getFields().put("criteria", getCriteria());
		
		if (getClientName() != null){
			list = getApplicationBandwidthManager().findByClientName(getClientName());
			getFields().put("APPLICATION_LIST", list);
			getFields().put("criteriaSearch", getClientName());
		}else if(getClientNo() != null){

			list = getApplicationBandwidthManager().findByClientNo(getClientNo());
			getFields().put("APPLICATION_LIST", list);
				
			getFields().put("criteriaSearch", getClientNo());
			
		}else if (getLicenceNumber() != null){
			list = getApplicationBandwidthManager().findByLicenceNumber(getLicenceNumber());
			getFields().put("APPLICATION_LIST", list);
			getFields().put("criteriaSearch", getLicenceNumber());
		}else if (getBhpMethod() != null){
			list = getApplicationBandwidthManager().findByMethod(getBhpMethod());
			getFields().put("APPLICATION_LIST", list);
			getFields().put("criteriaSearch", getBhpMethod());
		}
		
		if(getRequest() != null){
			getUserManager().viewInitialInvoiceSearch(initUserLoginFromDatabase(), getRequest().getRemoteHost());
		}else{
			addError(getDelegate(), "errorShadow", getText("initialInvoice.session.timeout"), ValidationConstraint.CONSISTENCY);
			return;
		}
		
	}
	
	
	
	public void doCreate(IRequestCycle cycle, String licenceNumber, String clientNumber){
		
		String cekLicenseNumberAndClientNumber = cekLicenceNumberAndClientNumber(licenceNumber, clientNumber);
		
		if (cekLicenseNumberAndClientNumber != null ) {
			addError(getDelegate(), "errorShadow", cekLicenseNumberAndClientNumber, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		List<License> licenseList = getLicenseManager().findByLicenceNo(licenceNumber);
		
		for (License licenseObj : licenseList){
			if (licenseObj.getLicenceNo().equals(licenceNumber)){
				addError(getDelegate(), "errorShadow", "No Aplikasi telah tersedia.", ValidationConstraint.CONSISTENCY);
				return;
			}
		}
		
		InitialInvoiceFlatCreate view = (InitialInvoiceFlatCreate)cycle.getPage("initialInvoiceFlatCreate");

		ApplicationBandwidth license = null;
		
		//Check if license number is not unique (there is 2 license with sama license number)
		try{
			license = getApplicationBandwidthManager().findByLicenceNo(licenceNumber);
		}catch(Exception e){
			e.printStackTrace();
			String duplicateLicenseNumber = getText("initialInvoice.license.licenseNumber.duplicate");
			addError(getDelegate(), "errorShadow", duplicateLicenseNumber, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String validationLicense = validationLicense(license);
		
		if (validationLicense != null ) {
			addError(getDelegate(), "errorShadow", validationLicense, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		if (license.getBhpMethod().equals("FR")) {
			view.setLicenceNumber(license.getLicenceNumber());
			view.setClientName(license.getClientCompany());
			view.setClientNO(clientNumber);
			view.setServiceId(license.getSvId());
			view.setSubServiceId(license.getSsId());
			view.setBhpMethod(license.getBhpMethod());
			view.setMethodBHP("Flat BHP");
			view.setKmNo(license.getKmNo());
			if(license.getKmDate() != null){
				view.setKmDate(DateUtil.convertDateToString(license.getKmDate()));
			}
			view.setZoneNo(license.getZoneNo());
			view.setZoneName(license.getZoneName());
			if(license.getCurrentBeginDate() != null){
				view.setCurrentBeginDate(DateUtil.convertDateToString(license.getCurrentBeginDate()));
			}
			if(license.getCurrentEndDate() != null){
				view.setCurrentEndDate(DateUtil.convertDateToString(license.getCurrentEndDate()));
			}
			view.setFreqTMin(license.getFreqMin());
			view.setFreqTMax(license.getFreqMax());
			view.setFreqRMin(license.getFreqMinR());
			view.setFreqRMax(license.getFreqMaxR());
			if(license.getLiBeginDate() != null){
				view.setLicenceBeginDate(DateUtil.convertDateToString(license.getLiBeginDate()));
			}
			if(license.getLiEndDate() != null){
				view.setLicenceEndDate(DateUtil.convertDateToString(license.getLiEndDate()));
			}
			view.setYearTo(new BigDecimal(1));
			if(license.getCurrentBeginDate() != null){
				view.setYear(DateUtil.convertDateToString(license.getCurrentBeginDate(), "yyyy"));
			}
			view.setPaymentType(license.getBhpPaymentType());
			
			if (license.getBhpPaymentType().equals("FP")){
				view.setPaymentTypeMethod("Full Payment");

			}else{
				view.setPaymentTypeMethod("Installment");
			}
			if(license.getPaymentDueDate() != null){
				view.setPaymentDueDate(DateUtil.convertDateToString(license.getPaymentDueDate()));
			}
			view.setBgAvailableStatus(license.getIsBgAvailable());
			view.setDocument(license.getKmDoc());
			view.setDocumentName(license.getKmFileName());
			
//			getFields().remove("APPLICATION_LIST");
//			getFields().clear();
			
			//Save to audit log
			getUserManager().viewInitialInvoiceCreate(initUserLoginFromDatabase(), getRequest().getRemoteHost(), license.getBhpMethod(), license.getLicenceNumber());

			//For back button
			getFields().put("APPLICATION_LIST_IN_INITIAL_FR", (List<ApplicationBandwidth>) getFields().get("APPLICATION_LIST"));

			view.setCriteria(getFields().get("criteria").toString());
			view.setCriteriaSearch((String) getFields().get("criteriaSearch"));
			
			
			cycle.activate(view);

		}else if (license.getBhpMethod().equals("VR")) {

			InitialInvoiceVarietyRateCreate viewVr = (InitialInvoiceVarietyRateCreate)cycle.getPage("initialInvoiceVarietyRateCreate");
				
			ApplicationBandwidth licenseVr = getApplicationBandwidthManager().findByLicenceNo(licenceNumber);
			
			String validationLicenseBeginAndEndDate = checkLicenseBeginAndEndDate(licenseVr);
			
			if (validationLicenseBeginAndEndDate != null ) {
				addError(getDelegate(), "errorShadow", validationLicenseBeginAndEndDate, ValidationConstraint.CONSISTENCY);
				return;
			}
				
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			Date year = licenseVr.getCurrentBeginDate();
			viewVr.setYear(new BigDecimal(format.format(year)));
			viewVr.setApplicationBandwidth(licenseVr);
			BigDecimal yearAppBandwidth = new BigDecimal(format.format(licenseVr.getLiBeginDate()));
			VariableAnnualRate annualRate = getVariableAnnualRateManager().findByYear(yearAppBandwidth);
			if(annualRate != null){
				viewVr.setBiRate(String.valueOf(annualRate.getRateValue()));
			}
			
			viewVr.setPercentageAnnualBhp(null);
			
//			For disable and enable input
			viewVr.setEnableInput("Y");
//			for show or hide table
			viewVr.setShowTable("N");
			
			//Save to audit log
			getUserManager().viewInitialInvoiceCreate(initUserLoginFromDatabase(), getRequest().getRemoteHost(), license.getBhpMethod(), license.getLicenceNumber());
			
			//For back button 
			//Use this way, if you want to store more than one record into next page, in the next page you must save it in real variable not abstract one like tapestry did
			//if not, this will cause stale link exception
//			viewVr.setApplicationList((List<ApplicationBandwidth>) getFields().get("APPLICATION_LIST"));
			getFields().put("APPLICATION_LIST_IN_INITIAL_VR", (List<ApplicationBandwidth>) getFields().get("APPLICATION_LIST"));
			
			viewVr.setCriteria(getFields().get("criteria").toString());
			viewVr.setCriteriaSearch((String) getFields().get("criteriaSearch"));
			
			cycle.activate(viewVr);
		}
					
	}
	
	public String validationLicense(ApplicationBandwidth license){
		
		String errorMessage = null;
		
		if(license.getBhpPaymentType() == null){
			errorMessage = getText("initialInvoice.license.empty.paymentType");
		}
		
		if(license.getBhpMethod() == null){
			if(errorMessage == null){
				errorMessage = getText("initialInvoice.license.empty.bhpMethod");
			}else{
				errorMessage = errorMessage + ", " + getText("initialInvoice.license.empty.bhpMethod");
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
	
	public String cekLicenceNumberAndClientNumber(String licenseNumber, String clientNumber){
		
		String errorMessage = null;
		
		if(licenseNumber == null){
			errorMessage = getText("initialInvoice.license.empty.licenseNumber");
		}
		
		if(clientNumber == null){
			if(errorMessage == null){
				errorMessage = getText("initialInvoice.license.empty.clientNumber");
			}else{
				errorMessage = errorMessage + ", " + getText("initialInvoice.license.empty.clientNumber");
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
	
	public String checkLicenseBeginAndEndDate(ApplicationBandwidth lic){
		String errorMessage = null;
		
		if(lic.getLiBeginDate() == null){
			errorMessage = getText("initialInvoice.license.empty.licBeginDate");
		}
		
		if(lic.getLiEndDate() == null){
			if(errorMessage == null){
				errorMessage = getText("initialInvoice.license.empty.licEndDate");
			}else{
				errorMessage = errorMessage + ", " + getText("initialInvoice.license.empty.licEndDate");
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
	
}