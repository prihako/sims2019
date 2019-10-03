package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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

public abstract class AnnualRateBHPEdit extends AdminBasePage implements
		PageBeginRenderListener {
	
	private BeanPropertySelectionModel variableStatusModel;
	
	private String pageLocation;
	
	public void setPageLocation(String location){
		pageLocation = location;
	}
	
	public String getPageLocation(){
		return pageLocation;
	}

	protected final Log log = LogFactory.getLog(AnnualRateBHPEdit.class);

	public abstract void setRateYear(String rateYear);
	
	public abstract Long getAnnualRateId();
	public abstract void setAnnualRateId(Long annualRateId);
	
	public abstract String getStatusActive();
	public abstract void setStatusActive(String activeStatus);

	private int activeStatus;
	
	private int saveStatus;
	
	public int getActiveStatus(){
		return activeStatus;
	}
	
	public void setActiveStatus(int activeStatus){
		this.activeStatus = activeStatus;
	}
	
	public int getSaveStatus(){
		return saveStatus;
	}
	
	public void setSaveStatus(int saveStatus){
		this.saveStatus = saveStatus;
	}

	public abstract String getBaseOnNote();

	public abstract void setBaseOnNote(String baseOnNote);

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String createdBy);

	public abstract Date getCreatedOn();

	public abstract void setCreatedOn(Date createdOn);

	public abstract String getRateValue();

	public abstract void setRateValue(String rateValue);

	public abstract  String getRateYear();

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

	public abstract void setBaseOnNoteCriteria(String crit);
	
	public abstract String getBaseOnNoteCriteria();
	
	public abstract void setStatusCriteria(String crit);
	
	public abstract String getStatusCriteria();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}
	
	public void setVariableStatusModel(Integer status){
		
		List<LabelValue> variableStatusActiveList = new ArrayList<LabelValue>();
		variableStatusActiveList.add(new LabelValue("Active", "1"));
		variableStatusActiveList.add(new LabelValue("Inactive", "0"));
		
		List<LabelValue> variableStatusInactiveList = new ArrayList<LabelValue>();
		variableStatusInactiveList.add(new LabelValue("Inactive", "0"));
		variableStatusInactiveList.add(new LabelValue("Active", "1"));
		
		if(status == 1){
			variableStatusModel = new BeanPropertySelectionModel( variableStatusActiveList, "label");
		}else{
			variableStatusModel = new BeanPropertySelectionModel( variableStatusInactiveList, "label");
		}
		
	}
	
	public BeanPropertySelectionModel getVariableStatusModel() {

		return variableStatusModel;
	}

	
	public IPage doDraft (IRequestCycle cycle) {
		
		String validationMsg2 = saveValidation();
		if (validationMsg2 != null) {
			addError(getDelegate(), "errorShadow", validationMsg2,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		
		String yearValidation = yearValidation();
		if (yearValidation != null) {
			addError(getDelegate(), "errorShadow", yearValidation,
					ValidationConstraint.CONSISTENCY);
			return null;
		}

		String validationMsg = isActive();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		System.out.println("draft AnnualRateId = "+getAnnualRateId());

		VariableAnnualRate rate = getVariableAnnualRateManager().findByAnnualRateId(getAnnualRateId());
		
		
//		System.out.println("Active Status = "+getActiveStatus());
//		System.out.println("Rate Value/BI Rate = "+getRateValue());
//		System.out.println("Status Active = "+getStatusActive());
//		System.out.println("Base On Note = "+getBaseOnNote());
//		System.out.println("Save Status = "+getSaveStatus());


		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			rate.setActiveStatus(0);
		} else if (getStatusActive().equals("LabelValue[Active, 1]")){
			rate.setActiveStatus(1);

			
		}
		rate.setRateValue(new BigDecimal(getRateValue()));
		rate.setSaveStatus(0);
		rate.setRateYear(new BigDecimal(getRateYear()));
		rate.setKmNo(getBaseOnNote());
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
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
			
		//SUpaya tampilan tabel uptodate
		saveObjectToSession(
				getVariableAnnualRateManager().findByStatus(2),
					"ANNUAL_RATE_LIST");
		//for back button
		getFields().put("manageBi", "xxx");
		saveDataToSession(getBaseOnNoteCriteria(), getStatusCriteria());
			
		return infoPage;
		
	}
	
	public IPage doSubmit (IRequestCycle cycle) {

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		
		String yearValidation = yearValidation();
		if (yearValidation != null) {
			addError(getDelegate(), "errorShadow", yearValidation,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		
		System.out.println("submit AnnualRateId = "+getAnnualRateId());

		VariableAnnualRate rate = getVariableAnnualRateManager().findByAnnualRateId(getAnnualRateId());
		
		
//		System.out.println("Active Status = "+getActiveStatus());
//		System.out.println("Rate Value/BI Rate = "+getRateValue());
//		System.out.println("Status Active = "+getStatusActive());
//		System.out.println("Base On Note = "+getBaseOnNote());
//		System.out.println("Save Status = "+getSaveStatus());
		
		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			rate.setActiveStatus(0);
			rate.setRateValue(new BigDecimal(getRateValue()));
			rate.setSaveStatus(1);
			rate.setRateYear(new BigDecimal(getRateYear()));
			rate.setUpdatedOn(new Date());
			rate.setUpdatedBy(getUserLoginFromSession().getUsername());
		} else if (getStatusActive().equals("LabelValue[Active, 1]")){
			rate.setActiveStatus(1);
			rate.setRateValue(new BigDecimal(getRateValue()));
			rate.setSaveStatus(1);
			rate.setRateYear(new BigDecimal(getRateYear()));
			rate.setUpdatedOn(new Date());
			rate.setUpdatedBy(getUserLoginFromSession().getUsername());
			
		}
		rate.setKmNo(getBaseOnNote());
		
		getVariableAnnualRateManager().update(rate);
		
		addError(getDelegate(), "errorShadow",
					getText("license.submit.success"),
					ValidationConstraint.CONSISTENCY);
		InfoPageCommand infoPageCommand = new InfoPageCommand();
	
		infoPageCommand.setTitle(getText("variableInformation.title"));
		infoPageCommand.addMessage(getText("variableInformation.information"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
					getText("button.finish"), "annualRateBHPEntry.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
			
		//for back button
		getFields().put("manageBi", "xxx");
		saveDataToSession(getBaseOnNoteCriteria(), getStatusCriteria());
			
		return infoPage;
	}

	public IPage doCancel(IRequestCycle cycle) {
		
		AnnualRateBHPEntry entry = getAnnualRateBHPEntry();
		entry.setStatusActive(getStatusCriteria());
		entry.setBaseOnNote(getBaseOnNoteCriteria());
		getFields().put("manageBi", "xxx");
		saveDataToSession(getBaseOnNoteCriteria(), getStatusCriteria());
			
		return entry;
	}

	private String saveValidation() {

		String errorMessage = null;
		Pattern alfa 		= Pattern.compile("[a-zA-Z,]");

//		System.out.println("getRateValue = " +getRateValue());
//		System.out.println("getBaseOnNote = " +getBaseOnNote());
//		System.out.println("getSaveStatus = " +getSaveStatus());
//		System.out.println("getStatusActive = " +getStatusActive());

		if (getRateValue() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.valueBIRate.error");
		}else if (getBaseOnNote() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.baseOnNote.error");
		}else if (getRateYear() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.null");
		}else if(alfa.matcher((getRateYear())).find()){
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.alpha");
		}else if(alfa.matcher((getRateValue())).find()){
			errorMessage = getText("variable.annualRateBHPEntry.rateValue.alpha");
		}else if( Double.valueOf(getRateValue()) < 0){
			errorMessage = getText("variable.annualRateBHPEntry.rateValue.lessThan0");
		}else if( Double.valueOf(getRateValue()) > 99){
			errorMessage = getText("variable.annualRateBHPEntry.rateValue.moreThan100");
		}else if( Double.valueOf(getRateYear()) > 9999){
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.moreThan4Digit");
		}else if( Double.valueOf(getRateYear()) < 1000){
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.lessThan4Digit");
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
	
	public String yearValidation(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year = format.format(date);
		
		Integer yearInput = Integer.parseInt(getRateYear());
		Integer yearNow = Integer.parseInt(year);
		
		String errorMessage = null;
		if(yearInput < yearNow){
			if(getStatusActive().equals("LabelValue[Active, 1]")){
				errorMessage = getText("variable.annualRateBHPEntry.rateYear.canNotActive");
			}
		}
		
		return errorMessage;
	}
	
	public void saveDataToSession(String baseOnNote, String statusCriteria){
		
		int status = 2;
		if (statusCriteria.equals("LabelValue[Inactive, 0]")) {
			status = 0;
		} else if (statusCriteria.equals("LabelValue[Active, 1]")) {
			status = 1;
		} else {
			status = 2;
		}
		
		if (baseOnNote == null) {		
			getFields().put("ANNUAL_RATE_LIST", getVariableAnnualRateManager().findByStatus(Integer.valueOf(status)));
		} else {			
			getFields().put("ANNUAL_RATE_LIST", getVariableAnnualRateManager()
					.findByBaseOnNoteAndStatus(getBaseOnNote(), Integer.valueOf(status)));
		}
		
	}

}
