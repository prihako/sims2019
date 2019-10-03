package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.acegisecurity.concurrent.SessionRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.License;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class AnnualPercentageBHPEntry extends AdminBasePage implements
		PageBeginRenderListener {
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public AnnualPercentageBHPEntry() {

	}  

	private BeanPropertySelectionModel variableStatusModel = null;

	protected final Log log = LogFactory.getLog(AnnualPercentageBHPEntry.class);
	
	public abstract void setVariableStatus(String status);

	public abstract String getVariableStatus();

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String createdBy);

	public abstract Date getCreatedOn();

	public abstract void setCreatedOn(Date createdOn);

	public abstract String getUpdatedBy();

	public abstract void setUpdatedBy(String updatedBy);

	public abstract Date getUpdatedOn();

	public abstract void setUpdatedOn(Date updatedOn);

	public abstract VariableAnnualPercentage getRow();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();

	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	@InjectPage("annualPercentageBHPCreate")
	public abstract AnnualPercentageBHPCreate getAnnualPercentageBHPCreate();
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
				if(getObjectfromSession("bhpManage") == null){
					saveObjectToSession(null, "ANNUAL_PERCENTAGE_LIST");
				}else{
					saveObjectToSession(null, "bhpManage");
				}
			}
		}
		
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

	}

	public List getVariableList() {
		List<VariableAnnualPercentage> licenseList = (List<VariableAnnualPercentage>) getObjectfromSession("ANNUAL_PERCENTAGE_LIST");
		return licenseList;
	}

	public BeanPropertySelectionModel getVariableStatusModel() {
		
		if(variableStatusModel != null){
			return variableStatusModel;
		}else{
			List<LabelValue> variableStatusList = new ArrayList<LabelValue>();
			variableStatusList.add(new LabelValue("All", "2"));
			variableStatusList.add(new LabelValue("Active", "1"));
			variableStatusList.add(new LabelValue("Inactive", "0"));
			variableStatusModel = new BeanPropertySelectionModel(
					variableStatusList, "label");
			System.out.println(variableStatusModel);
		
			return variableStatusModel;
		}
	}
	
	public void setVariableStatusModel(String num){
		List<LabelValue> variableStatusList = new ArrayList<LabelValue>();
		if(num.equals("2")){
			variableStatusList.add(new LabelValue("All", "2"));
			variableStatusList.add(new LabelValue("Active", "1"));
			variableStatusList.add(new LabelValue("Inactive", "0"));
		}else if(num.equals("1")){
			variableStatusList.add(new LabelValue("Active", "1"));
			variableStatusList.add(new LabelValue("Inactive", "0"));
			variableStatusList.add(new LabelValue("All", "2"));
		}else{
			variableStatusList.add(new LabelValue("Inactive", "0"));
			variableStatusList.add(new LabelValue("All", "2"));
			variableStatusList.add(new LabelValue("Active", "1"));
		}
		variableStatusModel = new BeanPropertySelectionModel(variableStatusList, "label");
	}

	public void doSearch() {
		String validationMsg = generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		int status;

		if (getVariableStatus().equals("LabelValue[Inactive, 0]")) {
			setVariableStatus("0");
			status = 0;
			setVariableStatusModel("0");

		} else if (getVariableStatus().equals("LabelValue[Active, 1]")) {
			setVariableStatus("0");
			status = 1;
			setVariableStatusModel("1");

		} else {
			setVariableStatus("0");
			status = 2;
			setVariableStatusModel("2");
		}

		saveObjectToSession(getVariableManager().findByStatus(status),
				"ANNUAL_PERCENTAGE_LIST");
		
		//Save to audit log
		getUserManager().bhpRateAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Search", null);
	}

	@SuppressWarnings("unchecked")
	public IPage onCreate() {

		if (getDelegate().getHasErrors())
			return null;

		AnnualPercentageBHPCreate licenseCreate = getAnnualPercentageBHPCreate();
		licenseCreate.setPageLocation(null);
		
		//for back button
		licenseCreate.setCriteria(getVariableStatus());
				
		return licenseCreate;
	}

	public IPage doEdit(IRequestCycle cycle, Long annualPercentId) {

		VariableAnnualPercentage variable = getVariableManager()
				.findByAnnualPercentId(annualPercentId);           

		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentId, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentId, 2);
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentId, 3);
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentId, 4);
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentId, 5);
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentId, 6);
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentId, 7);
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentId, 8);
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentId, 9);
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentId, 10);

		AnnualPercentageBHPView annualPercentageBHPView = (AnnualPercentageBHPView) cycle
				.getPage("annualPercentageBHPView");
		
		if (variable.getVariableStatus() == 1) {
			annualPercentageBHPView.setVariableStatus("Active");

		} else {
			annualPercentageBHPView.setVariableStatus("Inactive");

		}
		
		annualPercentageBHPView.setAnnualPercentId(annualPercentId);
		
		annualPercentageBHPView.setBerdasarkanDokumen(variable.getKmNo());
	
		annualPercentageBHPView.setVariablePresentaseTahunan(String.valueOf(variable.getPercentYear()));
	 
		if(percentageYearOne != null){
			annualPercentageBHPView.setPercentageYearOne(String.valueOf(percentageYearOne.getPercentage()));
		}
	
		if(percentageYearTwo != null){
			annualPercentageBHPView.setPercentageYearTwo(String.valueOf(percentageYearTwo
					.getPercentage()));
		}
	
		if(percentageYearThree != null){
			annualPercentageBHPView.setPercentageYearThree(String.valueOf(percentageYearThree
					.getPercentage()));
		}
			
		if(percentageYearFour != null){
			annualPercentageBHPView.setPercentageYearFour(String.valueOf(percentageYearFour
					.getPercentage()));
		}
	
		if(percentageYearFive != null){
			annualPercentageBHPView.setPercentageYearFive(String.valueOf(percentageYearFive
					.getPercentage()));
		}
	
		if(percentageYearSix != null){
			annualPercentageBHPView.setPercentageYearSix(String.valueOf(percentageYearSix
					.getPercentage()));
		}
	
		if(percentageYearSeven != null){
			annualPercentageBHPView.setPercentageYearSeven(String.valueOf(percentageYearSeven
					.getPercentage()));
		}
	
		if(percentageYearEight != null){
			annualPercentageBHPView.setPercentageYearEight(String.valueOf(percentageYearEight
					.getPercentage()));
		}
		
		if(percentageYearNine != null){
			annualPercentageBHPView.setPercentageYearNine(String.valueOf(percentageYearNine
					.getPercentage()));
		}
			
		if(percentageYearTen != null){
			annualPercentageBHPView.setPercentageYearTen(String.valueOf(percentageYearTen
					.getPercentage()));
		}
		
		List<License> licenseList = getLicenseManager().findByAnnualPercentId(annualPercentId);
		if(licenseList.size() > 0){
			annualPercentageBHPView.setUsed(true);
		}else{
			annualPercentageBHPView.setUsed(false);
		}
		
		//for back button
		annualPercentageBHPView.setCriteria(getVariableStatus());
		
		return annualPercentageBHPView;

	}

	private String generalValidation() {
		String errorMessage = null;
		if (getVariableStatus() == "") {
			errorMessage = getText("license.error.licenseId.empty");
		} else {
			errorMessage = null;
		}
		return errorMessage;
		
		
	}
	
	public String closePopup(){
		
		return "close_popup()";
	}
	
	public String getPopupAnnual(){
		String str = (String) getObjectfromSession("POPUP_ANNUAL");
		
		return str;
	}
	
//	public String chooseKmNo(){
//		return "select_percentage(" + kmNo + ")";
//	}
//	
//	private String kmNo = null;
//	public void doSelect(IRequestCycle cycle, String kmNo){
//		this.kmNo = kmNo;
//	}

}
