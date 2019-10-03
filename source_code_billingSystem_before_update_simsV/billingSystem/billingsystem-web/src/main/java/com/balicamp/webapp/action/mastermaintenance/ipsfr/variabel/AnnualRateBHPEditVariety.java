package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.concurrent.SessionRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.operational.InitialInvoiceSearch;
import com.balicamp.webapp.action.operational.InitialInvoiceVarietyRateCreate;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateEdit;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateView;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class AnnualRateBHPEditVariety extends AdminBasePage implements
		PageBeginRenderListener {
	
//	Untuk mengetahui, page mana yg sedang menggunakan, nilainya initial, manageEdit atau manageView
	private String locationPage;
	
	public void setLocationPage(String locationPage){
		this.locationPage = locationPage;
	}
	
	public String getLocationPage(){
		return locationPage;
	}
	
	private BeanPropertySelectionModel variableStatusModel;

	protected final Log log = LogFactory.getLog(AnnualRateBHPEditVariety.class);

	public abstract void setRateYear(BigDecimal rateYear);
	
	public abstract Long getAnnualRateId();
	public abstract void setAnnualRateId(Long annualRateId);
	
	public abstract String getStatusActive();
	public abstract void setStatusActive(String activeStatus);


	public abstract int getActiveStatus();
	public abstract void setActiveStatus(int activeStatus);
	
	public abstract int getSaveStatus();
	public abstract void setSaveStatus(int saveStatus);

	public abstract String getBaseOnNote();

	public abstract void setBaseOnNote(String baseOnNote);

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String createdBy);

	public abstract Date getCreatedOn();

	public abstract void setCreatedOn(Date createdOn);

	public abstract BigDecimal getRateValue();

	public abstract void setRateValue(BigDecimal rateValue);

	public abstract  BigDecimal getRateYear();

	public abstract String getUpdatedBy();

	public abstract void setUpdatedBy(String updatedBy);

	public abstract Date getUpdatedOn();

	public abstract void setUpdatedOn(Date updatedOn);
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	public abstract VariableAnnualRate getRow();

	
	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectPage("annualRateBHPEntry")
	public abstract AnnualRateBHPEntry getAnnualRateBHPEntry();

	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}
	
	public IPropertySelectionModel getVariableStatusModel() {

		Map<String, String> mapActive = new LinkedHashMap<String, String>();
		mapActive.put("1", "Active");
		mapActive.put("0", "Inactive");
		
		Map<String, String> mapInactive = new LinkedHashMap<String, String>();
		mapInactive.put("0", "Inactive");
		mapInactive.put("1", "Active");
		
		if(getActiveStatus()==0){
			return new PropertySelectionModel(getLocale(), mapInactive, false, false);
		}else{
			return new PropertySelectionModel(getLocale(), mapActive, false, false);
		}
		
	}

	
	public void doDraft (IRequestCycle cycle) {

		String validationMsg = isActive();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		VariableAnnualRate rate = getVariableAnnualRateManager().findByAnnualRateId(getAnnualRateId());

		if (getStatusActive().equals("0")) {
			rate.setActiveStatus(0);
		} else if (getStatusActive().equals("1")){
			rate.setActiveStatus(1);
		}
		
		rate.setRateValue(getRateValue());
		rate.setSaveStatus(0);
		rate.setUpdatedOn(new Date());
		rate.setUpdatedBy(getUserLoginFromSession().getUsername());
		
		
		getVariableAnnualRateManager().save(rate);
		
		if(locationPage.equalsIgnoreCase("initial")){
			InitialInvoiceVarietyRateCreate initialInvoiceVarietyRateCreate = (InitialInvoiceVarietyRateCreate) cycle.getPage("initialInvoiceVarietyRateCreate");
			cycle.activate(initialInvoiceVarietyRateCreate);
		}else if(locationPage.equalsIgnoreCase("manageView")){
			ManageInvoiceVarietyRateView manageInvoiceVarietyRateView = (ManageInvoiceVarietyRateView) cycle.getPage("manageInvoiceVarietyRateView");
			cycle.activate(manageInvoiceVarietyRateView);
		}else if(locationPage.equalsIgnoreCase("manageEdit")){
			ManageInvoiceVarietyRateEdit manageInvoiceVarietyRateEdit = (ManageInvoiceVarietyRateEdit) cycle.getPage("manageInvoiceVarietyRateEdit");
			cycle.activate(manageInvoiceVarietyRateEdit);
		}

	}
	
	public void doSubmit (IRequestCycle cycle) {

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		VariableAnnualRate rate = getVariableAnnualRateManager().findByAnnualRateId(getAnnualRateId());
		
		if (getStatusActive().equals("0")) {
			rate.setActiveStatus(0);
			rate.setRateValue(getRateValue());
			rate.setSaveStatus(1);
		
			rate.setUpdatedOn(new Date());
			rate.setUpdatedBy(getUserLoginFromSession().getUsername());
		} else if (getStatusActive().equals("1")){
			rate.setActiveStatus(1);
			rate.setRateValue(getRateValue());
			rate.setSaveStatus(1);
		
			rate.setUpdatedOn(new Date());
			rate.setUpdatedBy(getUserLoginFromSession().getUsername());
			
		}

		getVariableAnnualRateManager().update(rate);
		
		if(locationPage.equalsIgnoreCase("initial")){
			InitialInvoiceVarietyRateCreate initialInvoiceVarietyRateCreate = (InitialInvoiceVarietyRateCreate) cycle.getPage("initialInvoiceVarietyRateCreate");
			cycle.activate(initialInvoiceVarietyRateCreate);
		}else if(locationPage.equalsIgnoreCase("manageView")){
			ManageInvoiceVarietyRateView manageInvoiceVarietyRateView = (ManageInvoiceVarietyRateView) cycle.getPage("manageInvoiceVarietyRateView");
			cycle.activate(manageInvoiceVarietyRateView);
		}else if(locationPage.equalsIgnoreCase("manageEdit")){
			ManageInvoiceVarietyRateEdit manageInvoiceVarietyRateEdit = (ManageInvoiceVarietyRateEdit) cycle.getPage("manageInvoiceVarietyRateEdit");
			cycle.activate(manageInvoiceVarietyRateEdit);
		}
	}

	public void doCancel(IRequestCycle cycle) {
		
		if(locationPage.equalsIgnoreCase("initial")){
			InitialInvoiceVarietyRateCreate initialInvoiceVarietyRateCreate = (InitialInvoiceVarietyRateCreate) cycle.getPage("initialInvoiceVarietyRateCreate");
			cycle.activate(initialInvoiceVarietyRateCreate);
		}else if(locationPage.equalsIgnoreCase("manageView")){
			ManageInvoiceVarietyRateView manageInvoiceVarietyRateView = (ManageInvoiceVarietyRateView) cycle.getPage("manageInvoiceVarietyRateView");
			cycle.activate(manageInvoiceVarietyRateView);
		}else if(locationPage.equalsIgnoreCase("manageEdit")){
			ManageInvoiceVarietyRateEdit manageInvoiceVarietyRateEdit = (ManageInvoiceVarietyRateEdit) cycle.getPage("manageInvoiceVarietyRateEdit");
			cycle.activate(manageInvoiceVarietyRateEdit);
		}
		
	}

	private String saveValidation() {

		String errorMessage = null;

		if (getRateValue() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.valueBIRate.error");
		} else if (getBaseOnNote() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.baseOnNote.error");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}
	
	private String isActive() {

		String errorMessage = null;

		if (getRateValue() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.valueBIRate.error");
		} else if (getBaseOnNote() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.baseOnNote.error");
		} else if (getStatusActive().equals("1")) {
			errorMessage = getText("variable.annualPercentageBHPCreate.error.statusVariable.active");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}

}
