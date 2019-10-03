package com.balicamp.webapp.action.operational;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.License;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualRateBHPCreate;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualRateBHPEdit;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class InitialInvoiceVarietyRateSearch extends AdminBasePage
		implements PageBeginRenderListener, PageEndRenderListener {
	
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
	
	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if (!pageEvent.getRequestCycle().isRewinding()) {
	
				if (getFields() == null) {
					setFields(new HashMap());
				}
			
		}
		
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}
	
	public List getApplicationList() {
		List<ApplicationBandwidth> list = (List<ApplicationBandwidth>) getFields()
				.get("APPLICATION_LIST");
		return list;
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
		map.put("C", "Conversion");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public IPropertySelectionModel getInvoiceStatusModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("P", "Paid");
		map.put("U", "Unpaid");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public IPage doCreate(IRequestCycle cycle, String licenceNumber, String clientNumber){
//		validation if license was created or not
		String validationMsg 	= licenseValidation(licenceNumber);
		if(validationMsg != null){
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return null;
		}

		List<VariableAnnualRate> rates = getVariableAnnualRateManager().findByStatus(1);
			
		InitialInvoiceVarietyRateCreate view = (InitialInvoiceVarietyRateCreate)cycle.getPage("initialInvoiceVarietyRateCreate");
			
		ApplicationBandwidth license = getApplicationBandwidthManager().findByLicenceNo(licenceNumber);
			
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date year = license.getCurrentBeginDate();
			
		view.setYear(new BigDecimal(format.format(year)));
		view.setApplicationBandwidth(license);
		if(rates.size() > 0){
			VariableAnnualRate rate = rates.get(0);
			view.setBiRate(String.valueOf(rate.getRateYear()));
		}
			
		return view;

	}
	
	public void doSearch(){
		List<ApplicationBandwidth> list;
		
		if (getClientName() != null){
			
			list = getApplicationBandwidthManager().findByClientName(getClientName());
			getFields().put("APPLICATION_LIST", list);
			
		}else if(getClientNo() != null){
			
			 list = getApplicationBandwidthManager().findByClientNo(getClientNo());
			 getFields().put("APPLICATION_LIST", list);
			
		}else if (getLicenceNumber() != null){
			list = getApplicationBandwidthManager().findByLicenceNumber(getLicenceNumber());
			getFields().put("APPLICATION_LIST", list);

		}else if (getBhpMethod() != null){
			
			list = getApplicationBandwidthManager().findByMethod(getBhpMethod());
			getFields().put("APPLICATION_LIST", list);
			
		}
		
	}
	
	public String licenseValidation(String licenseNumber){
		List<License> licenseSearch = getLicenseManager().findByLicenceNo(licenseNumber);
		
		String validationMsg 	= "License sudah dibuat, harap gunakan menu Manage Invoice";
		
		if(licenseSearch.size() > 0){
			return validationMsg;
		}else{
			return null;
		}
	}

}
