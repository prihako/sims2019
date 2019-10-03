package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class AnnualRateBHPEntry extends AdminBasePage implements
		PageBeginRenderListener {
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public AnnualRateBHPEntry() {

	}

	private BeanPropertySelectionModel variableStatusModel = null;

	protected final Log log = LogFactory.getLog(AnnualRateBHPEntry.class);

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

	public abstract BigDecimal getRateValue();

	public abstract void setRateValue(BigDecimal rateValue);

	public abstract BigDecimal getRateYear();

	public abstract void setRateYear(BigDecimal rateYear);

	public abstract String getUpdatedBy();

	public abstract void setUpdatedBy(String updatedBy);

	public abstract Date getUpdatedOn();

	public abstract void setUpdatedOn(Date updatedOn);

	public abstract VariableAnnualRate getRows();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectPage("annualRateBHPCreate")
	public abstract AnnualRateBHPCreate getAnnualRateBHPCreate();

	@InjectPage("annualRateBHPEdit")
	public abstract AnnualRateBHPEdit getAnnualRateBHPEdit();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if (!pageEvent.getRequestCycle().isRewinding()) {

			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else if (getFields() != null) {
					if(getFields().get("manageBi") == null){
						getFields().remove("ANNUAL_RATE_LIST");
					}else{
						getFields().remove("manageBi");
					}
				}
			}
			
		}
		
	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);

	}

	public List getVariableAnnualRateList() {
//		List<VariableAnnualRate> licenseList = (List<VariableAnnualRate>) getObjectfromSession("ANNUAL_RATE_LIST");
		List<VariableAnnualRate> licenseList = (List<VariableAnnualRate>) getFields().get("ANNUAL_RATE_LIST");
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

//		System.out.println("getStatusActive()  = " + getStatusActive()
//				+ ", getActiveStatus() = " + getActiveStatus()
//				+ ", getBaseOnNote = " + getBaseOnNote());

		int status;

		if (getStatusActive().equals("LabelValue[Inactive, 0]")) {
			setStatusActive("0");
			status = Integer.parseInt(getStatusActive());
			setVariableStatusModel("0");

		} else if (getStatusActive().equals("LabelValue[Active, 1]")) {
			setStatusActive("1");
			status = Integer.parseInt(getStatusActive());
			setVariableStatusModel("1");

		} else {
			setStatusActive("2");
			status = Integer.parseInt(getStatusActive());
			setVariableStatusModel("2");
		}

		if (getBaseOnNote() == null) {
//			System.out.println("1 Base On Note and Status = " + getBaseOnNote()
//					+ " / " + status);
//			saveObjectToSession(
//					getVariableAnnualRateManager().findByStatus(status),
//					"ANNUAL_RATE_LIST");
//			
			getFields().put("ANNUAL_RATE_LIST", getVariableAnnualRateManager().findByStatus(status));

		} else {
//			System.out.println("2 Base On Note and Status = " + getBaseOnNote()
//					+ " / " + status);

//			saveObjectToSession(getVariableAnnualRateManager()
//					.findByBaseOnNoteAndStatus(getBaseOnNote(), status),
//					"ANNUAL_RATE_LIST");
			
			getFields().put("ANNUAL_RATE_LIST", getVariableAnnualRateManager()
					.findByBaseOnNoteAndStatus(getBaseOnNote(), status));
		}
		
		//Save to audit log
		getUserManager().bhpRateAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Search", null);

	}

	public IPage onCreate() {
		if (getDelegate().getHasErrors())
			return null;
		AnnualRateBHPCreate create = getAnnualRateBHPCreate();
		create.setPageLocation(null);
		
		//for back button 
		create.setBaseOnNoteCriteria(getBaseOnNote());
		create.setStatusCriteria(getStatusActive());
				
		return create;
	}

	public IPage doEdit(IRequestCycle cycle, Long annualRateId) {

//		System.out.println("AnnualPercentageBHPEdit - doEdit = annualRateId = "
//				+ annualRateId);
		VariableAnnualRate rate = getVariableAnnualRateManager()
				.findByAnnualRateId(annualRateId);

		AnnualRateBHPEdit annualRateBHPEdit = (AnnualRateBHPEdit) cycle
				.getPage("annualRateBHPEdit");

		annualRateBHPEdit.setAnnualRateId(annualRateId);
		annualRateBHPEdit.setActiveStatus(rate.getActiveStatus());
		annualRateBHPEdit.setSaveStatus(rate.getSaveStatus());
		annualRateBHPEdit.setRateValue(String.valueOf(rate.getRateValue()));
		annualRateBHPEdit.setRateYear(String.valueOf(rate.getRateYear()));
		annualRateBHPEdit.setBaseOnNote(rate.getKmNo());
		annualRateBHPEdit.setVariableStatusModel(rate.getActiveStatus());
		annualRateBHPEdit.setPageLocation(null);
		
		//for back button 
		annualRateBHPEdit.setBaseOnNoteCriteria(getBaseOnNote());
		annualRateBHPEdit.setStatusCriteria(getStatusActive());
		
		return annualRateBHPEdit;

	}
	
	private String generalValidation() {
		String errorMessage = null;
//		if (getS() == "") {
//			errorMessage = getText("license.error.licenseId.empty");
//		} else {
//			errorMessage = null;
//		}
		return errorMessage;
	}

	public String closePopup(){
		List<VariableAnnualRate> rates = getVariableAnnualRateManager().findByStatus(1);
		if(rates.size() > 0 ){
			VariableAnnualRate rate = rates.get(0);
			return "changeparent(" + rate.getRateValue() + ")";
		}else{
			return "changeparent()";
		}
	}
	
	public String getPopupBi(){
		String str = (String) getObjectfromSession("POPUP_BI");
		
		return str;
	}
	
}
