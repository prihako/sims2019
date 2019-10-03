package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.operational.InitialInvoiceVarietyRateCreate;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateEdit;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateView;

public abstract class AnnualRateBHPView extends AdminBasePage implements
		PageBeginRenderListener {
	
	private BeanPropertySelectionModel variableStatusModel;
	
	private String pageLocation;
	
	public void setPageLocation(String location){
		pageLocation = location;
	}
	
	public String getPageLocation(){
		return pageLocation;
	}

	protected final Log log = LogFactory.getLog(AnnualRateBHPView.class);

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
	
	@InjectPage("annualRateBHPCreate")
	public abstract AnnualRateBHPCreate getAnnualRateBHPCreate();
	
	@InjectPage("annualRateBHPEdit")
	public abstract AnnualRateBHPEdit getAnnualRateBHPEdit();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}
	
	public BeanPropertySelectionModel getVariableStatusModel() {

		List<LabelValue> variableStatusActiveList = new ArrayList<LabelValue>();
		variableStatusActiveList.add(new LabelValue("Active", "1"));
		variableStatusActiveList.add(new LabelValue("Inactive", "0"));
		
		List<LabelValue> variableStatusInactiveList = new ArrayList<LabelValue>();
		variableStatusInactiveList.add(new LabelValue("Inactive", "0"));
		variableStatusInactiveList.add(new LabelValue("Active", "1"));
		
		
		variableStatusModel = new BeanPropertySelectionModel(
				variableStatusActiveList, "label");
		
		if(getActiveStatus()==0){
			variableStatusModel = new BeanPropertySelectionModel(
					variableStatusInactiveList, "label");
			System.out.println(variableStatusModel);

		}else{
			variableStatusModel = new BeanPropertySelectionModel(
					variableStatusActiveList, "label");
			System.out.println(variableStatusModel);

		}


		return variableStatusModel;
	}

	
	public IPage doDraft (IRequestCycle cycle) {

		String validationMsg = isActive();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		System.out.println("draft AnnualRateId = "+getAnnualRateId());

		VariableAnnualRate rate = getVariableAnnualRateManager().findByAnnualRateId(getAnnualRateId());
		
		
		System.out.println("Active Status = "+getActiveStatus());
		System.out.println("Rate Value/BI Rate = "+getRateValue());
		System.out.println("Status Active = "+getStatusActive());
		System.out.println("Base On Note = "+getBaseOnNote());
		System.out.println("Save Status = "+getSaveStatus());


		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			rate.setActiveStatus(0);
		} else if (getStatusActive().equals("LabelValue[Active, 1]")){
			rate.setActiveStatus(1);

			
		}
		rate.setRateValue(getRateValue());
		rate.setSaveStatus(0);
		rate.setUpdatedOn(new Date());
		rate.setUpdatedBy(getUserLoginFromSession().getUsername());
		
		getVariableAnnualRateManager().save(rate);
		
		addError(getDelegate(), "errorShadow",
				getText("license.submit.success"),
				ValidationConstraint.CONSISTENCY);
		InfoPageCommand infoPageCommand = new InfoPageCommand();

		infoPageCommand.setTitle(getText("variableInformation.title"));
		infoPageCommand.addMessage(getText("variableInformation.information"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "annualRateBHPEntry.html"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.welcome"), "main.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		//SUpaya tampilan tabel uptodate
		saveObjectToSession(
				getVariableAnnualRateManager().findByStatus(2),
				"ANNUAL_RATE_LIST");
		
		return infoPage;
	}
	
	public IPage doSubmit (IRequestCycle cycle) {

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		System.out.println("submit AnnualRateId = "+getAnnualRateId());

		VariableAnnualRate rate = getVariableAnnualRateManager().findByAnnualRateId(getAnnualRateId());
		
		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			rate.setActiveStatus(0);
			rate.setRateValue(getRateValue());
			rate.setSaveStatus(1);
		
			rate.setUpdatedOn(new Date());
			rate.setUpdatedBy(getUserLoginFromSession().getUsername());
		} else if (getStatusActive().equals("LabelValue[Active, 1]")){
			rate.setActiveStatus(1);
			rate.setRateValue(getRateValue());
			rate.setSaveStatus(1);
		
			rate.setUpdatedOn(new Date());
			rate.setUpdatedBy(getUserLoginFromSession().getUsername());

			
		}
		
		getVariableAnnualRateManager().update(rate);
		
		addError(getDelegate(), "errorShadow",
				getText("license.submit.success"),
				ValidationConstraint.CONSISTENCY);
		InfoPageCommand infoPageCommand = new InfoPageCommand();

		infoPageCommand.setTitle(getText("variableInformation.title"));
		infoPageCommand.addMessage(getText("variableInformation.information"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "annualRateBHPEntry.html"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.welcome"), "main.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		//SUpaya tampilan tabel uptodate
		saveObjectToSession(
				getVariableAnnualRateManager().findByStatus(2),
				"ANNUAL_RATE_LIST");
		
		return infoPage;
	}
	
	public IPage doEdit(IRequestCycle cycle){

		VariableAnnualRate rate = getVariableAnnualRateManager()
				.findByAnnualRateId(getAnnualRateId());
		
		System.out.println("AnnualPercentageBHPEdit - doEdit = annualRateId = "
				+ rate.getAnnualRateId());

		AnnualRateBHPEdit annualRateBHPEdit = getAnnualRateBHPEdit();

		annualRateBHPEdit.setAnnualRateId(rate.getAnnualRateId());
		annualRateBHPEdit.setActiveStatus(rate.getActiveStatus());
		annualRateBHPEdit.setSaveStatus(rate.getSaveStatus());
		annualRateBHPEdit.setRateValue(String.valueOf(rate.getRateValue()));
		annualRateBHPEdit.setRateYear(String.valueOf(rate.getRateYear()));
		annualRateBHPEdit.setBaseOnNote(rate.getKmNo());
		annualRateBHPEdit.setPageLocation(pageLocation);
		annualRateBHPEdit.setVariableStatusModel(rate.getActiveStatus());

		return annualRateBHPEdit;
	}
	
	public IPage doNewInput(IRequestCycle cycle){
		AnnualRateBHPCreate create = getAnnualRateBHPCreate();
		create.setPageLocation(pageLocation);
		return create;
	}

	public IPage doCancel(IRequestCycle cycle) {
		
//		To call back page that call this page
		if(getPageLocation().equalsIgnoreCase("initial")){
			InitialInvoiceVarietyRateCreate initial = (InitialInvoiceVarietyRateCreate) cycle.getPage("initialInvoiceVarietyRateCreate");
			return initial;
		}else if(getPageLocation().equalsIgnoreCase("manageView")){
			ManageInvoiceVarietyRateView view = (ManageInvoiceVarietyRateView) cycle.getPage("manageInvoiceVarietyRateView");
			return view;
		}else {
			ManageInvoiceVarietyRateEdit edit = (ManageInvoiceVarietyRateEdit) cycle.getPage("manageInvoiceVarietyRateEdit");
			return edit;
		}
		
	}

	private String saveValidation() {

		String errorMessage = null;

		System.out.println("getRateValue = " +getRateValue());
		System.out.println("getBaseOnNote = " +getBaseOnNote());
		System.out.println("getSaveStatus = " +getSaveStatus());
		System.out.println("getStatusActive = " +getStatusActive());

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
		} else if (getStatusActive().equals("LabelValue[Active, 1]")) {
			errorMessage = getText("variable.annualPercentageBHPCreate.error.statusVariable.active");
		} else {
			errorMessage = null;
		}
		return errorMessage;
	}

}
