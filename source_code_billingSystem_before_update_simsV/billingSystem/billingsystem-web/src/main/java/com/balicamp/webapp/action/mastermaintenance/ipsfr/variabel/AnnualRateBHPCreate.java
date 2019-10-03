package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
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
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;

public abstract class AnnualRateBHPCreate extends AdminBasePage implements
		PageBeginRenderListener {

	private BeanPropertySelectionModel variableStatusModel;
	
	private String pageLocation;
	
	public void setPageLocation(String location){
		pageLocation = location;
	}
	
	public String getPageLocation(){
		return pageLocation;
	}
	protected final Log log = LogFactory.getLog(AnnualRateBHPCreate.class);

	public abstract String getStatusActive();

	public abstract void setStatusActive(String activeStatus);
	
	public abstract int getActiveStatus();

	public abstract void setActiveStatus(int activeStatus);

	public abstract String getBaseOnNote();

	public abstract void setBaseOnNote(String baseOnNote);

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String createdBy);

	public abstract Date getCreatedOn();

	public abstract void setCreatedOn(Date createdOn);

	public abstract String getRateValue();

	public abstract void setRateValue(String rateValue);

	public abstract String getRateYear();

	public abstract void setRateYear(String rateYear);

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

	public BeanPropertySelectionModel getVariableStatusModel() {

		List<LabelValue> variableStatusList = new ArrayList<LabelValue>();
		variableStatusList.add(new LabelValue("Inactive", "0"));
		variableStatusList.add(new LabelValue("Active", "1"));
		variableStatusModel = new BeanPropertySelectionModel(
				variableStatusList, "label");
		System.out.println(variableStatusModel);

		return variableStatusModel;
	}

	public IPage doDraft(IRequestCycle cycle) {
		
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

		VariableAnnualRate rate = new VariableAnnualRate();

		System.out.println("Active Status = " + getActiveStatus());
		System.out.println("Rate Value/BI Rate = " + getRateValue());

		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			rate.setActiveStatus(0);
		} else if (getStatusActive().equals("LabelValue[Active, 1]")) {
			rate.setActiveStatus(1);
		}
		
		rate.setSaveStatus(0);

		rate.setRateValue(new BigDecimal(getRateValue()));
		rate.setRateYear(new BigDecimal(getRateYear()));
		
		rate.setKmNo(getBaseOnNote());
		Date date = new Date();
		String strDate = DateUtil.convertDateToString(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			rate.setCreatedOn(new Timestamp(dateFormat.parse(strDate).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		rate.setCreatedOn(new Date());
		rate.setCreatedBy(getUserLoginFromSession().getUsername());

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
			
		//Save to audit log
		getUserManager().bhpRateAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Draft", getRateYear());
		
		//SUpaya tampilan tabel uptodate 
		getFields().put("ANNUAL_RATE_LIST", getVariableAnnualRateManager().findByStatus(2));
		getFields().put("manageBi", "xxx");
	
		return infoPage;
	}

	public IPage doSubmit(IRequestCycle cycle) {

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

		VariableAnnualRate rate = new VariableAnnualRate();

		System.out.println("Active Status = " + getActiveStatus());
		System.out.println("Rate Value/BI Rate = " + getRateValue());

		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			rate.setActiveStatus(0);
		} else if (getStatusActive().equals("LabelValue[Active, 1]")) {
			rate.setActiveStatus(1);

		}
		
		rate.setRateValue(new BigDecimal(getRateValue()));
		rate.setRateYear(new BigDecimal(getRateYear()));
		
		rate.setSaveStatus(1);
		rate.setKmNo(getBaseOnNote());
		rate.setCreatedOn(new Date());
		rate.setCreatedBy(getUserLoginFromSession().getUsername());

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
		getFields().put("ANNUAL_RATE_LIST", getVariableAnnualRateManager().findByStatus(2));
		getFields().put("manageBi", "xxx");
			
		//Save to audit log
		getUserManager().bhpRateAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Submit", getRateYear());
	
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

		if (getBaseOnNote() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.baseOnNote.error");
		}else if (getRateYear() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.null");
		}else if (getRateValue() == null) {
			errorMessage = getText("variable.annualRateBHPEntry.valueBIRate.error");
		}else if(alfa.matcher(getRateYear()).find()){
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.alpha");
		}else if(alfa.matcher(getRateValue()).find()){
			errorMessage = getText("variable.annualRateBHPEntry.rateValue.alpha");
		}else if( (Double.valueOf(getRateValue())) < 0){
			errorMessage = getText("variable.annualRateBHPEntry.rateValue.lessThan0");
		}else if( (Double.valueOf(getRateValue())) > 99){
			errorMessage = getText("variable.annualRateBHPEntry.rateValue.moreThan100");
		}else if( (Double.valueOf(getRateYear())) > 9999){
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
		}else {
			errorMessage = null;
		}
		return errorMessage;
	}
	
	public String yearValidation(){
		VariableAnnualRate rate = getVariableAnnualRateManager().findByYear(new BigDecimal(getRateYear()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year = format.format(date);
		
		Integer yearInput = Integer.parseInt(getRateYear());
		Integer yearNow = Integer.parseInt(year);
		
		String errorMessage = null;
		if(rate != null){
			errorMessage = getText("variable.annualRateBHPEntry.rateYear.duplicate");
		}else if(yearInput < yearNow){
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
			System.out.println(((List<VariableAnnualRate>) getFields().get("ANNUAL_RATE_LIST")).size());
		}
		
	}
}
