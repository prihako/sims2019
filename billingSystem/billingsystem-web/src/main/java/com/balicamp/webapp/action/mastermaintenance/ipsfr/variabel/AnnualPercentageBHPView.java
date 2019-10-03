package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class AnnualPercentageBHPView extends AdminBasePage implements
		PageBeginRenderListener {
	
	private String pageLocation;
	
	public void setPageLocation(String location){
		this.pageLocation = location;
	}
	
	public String getPageLocation(){
		return pageLocation;
	}
	
	public AnnualPercentageBHPView(){
		
	}
	//
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}
	
	public abstract void setVariablePresentaseTahunan(String tahun);
	
	public abstract String getVariablePresentaseTahunan();
	
	public abstract  void setBerdasarkanDokumen(String skmNo);
	
	public abstract String getBerdasarkanDokumen();
	
	public abstract Long getAnnualPercentId();

	public abstract void setAnnualPercentId(Long annualPercentId);
	
	public abstract String getVariableStatus();
	
	public abstract void setVariableStatus(String variableStatus);

	public abstract String getPercentageYearOne();

	public abstract void setPercentageYearOne(String percentageYearOne);

	public abstract String getPercentageYearTwo();

	public abstract void setPercentageYearTwo(String percentageYearTwo);

	public abstract String getPercentageYearThree();

	public abstract void setPercentageYearThree(String percentageYearThree);

	public abstract String getPercentageYearFour();

	public abstract void setPercentageYearFour(String percentageYearFour);

	public abstract String getPercentageYearFive();

	public abstract void setPercentageYearFive(String percentageYearFive);

	public abstract String getPercentageYearSix();

	public abstract void setPercentageYearSix(String percentageYearSix);

	public abstract String getPercentageYearSeven();

	public abstract void setPercentageYearSeven(String percentageYearSeven);

	public abstract String getPercentageYearEight();

	public abstract void setPercentageYearEight(String percentageYearEight);

	public abstract String getPercentageYearNine();

	public abstract void setPercentageYearNine(String percentageYearNine);
	
	public abstract String getPercentageYearTen();

	public abstract void setPercentageYearTen(String percentageYearTen);

	public abstract String getPercentage();

	public abstract void setPercentage(String percentage);
	
	private  String used;
	public abstract void setUsed(boolean used);
	
	public abstract boolean getUsed();
	
	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();
	
	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();
	
	@InjectPage("annualPercentageBHPEdit")
	public abstract AnnualPercentageBHPEdit getAnnualPercentageBHPEdit();
	
	@InjectPage("annualPercentageBHPEntry")
	public abstract AnnualPercentageBHPEntry getAnnualPercentageBHPEntry();
	
	public abstract void setCriteria(String criteria);
	
	public abstract String getCriteria();

	public IPage doEdit (IRequestCycle cycle) {
		if(getUsed() == true){
			String validationMsg = "Variable Presentase BHP Tahunan Sudah Digunakan";
			
			if (validationMsg != null) {
				addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
				return null;
			}
		}
		
		AnnualPercentageBHPEdit variableEdit =  (AnnualPercentageBHPEdit) cycle
				.getPage("annualPercentageBHPEdit");
		
		int status = 0;
		if (getVariableStatus().equalsIgnoreCase("active")) {
			status = 1;
		} else {
			status = 0;
		}
		
		variableEdit.setAnnualPercentId(getAnnualPercentId());
		variableEdit.setVariableStatus(getVariableStatus());
		variableEdit.setVariableStatusModel(status);
		variableEdit.setBerdasarkanDokumen(getBerdasarkanDokumen());
		variableEdit.setVariablePersentaseTahunan(getVariablePresentaseTahunan());
		variableEdit.setPercentageYearOne(getPercentageYearOne());
		variableEdit.setPercentageYearTwo(getPercentageYearTwo());
		variableEdit.setPercentageYearThree(getPercentageYearThree());
		variableEdit.setPercentageYearFour(getPercentageYearFour());
		variableEdit.setPercentageYearFive(getPercentageYearFive());
		variableEdit.setPercentageYearSix(getPercentageYearSix());
		variableEdit.setPercentageYearSeven(getPercentageYearSeven());
		variableEdit.setPercentageYearEight(getPercentageYearEight());
		variableEdit.setPercentageYearNine(getPercentageYearNine());
		variableEdit.setPercentageYearTen(getPercentageYearTen());
		variableEdit.setPageLocation(null);
		
		//for back button
		variableEdit.setCriteria(getCriteria());
		
		return variableEdit;
	}
	
	
	public void doCancel (IRequestCycle cycle) {
		
		AnnualPercentageBHPEntry entry = getAnnualPercentageBHPEntry();
		saveObjectToSession("xxx", "bhpManage");
		entry.setVariableStatus(getCriteria());
		
		int status = 2;
		if (getCriteria().equals("LabelValue[Inactive, 0]")) {
			status = 0;
		} else if (getCriteria().equals("LabelValue[Active, 1]")) {
			status = 1;
		} else {
			status = 2;
		}

		saveObjectToSession(getVariableManager().findByStatus(status),
				"ANNUAL_PERCENTAGE_LIST");
		saveObjectToSession("xxx", "bhpManage");
		
		cycle.activate(entry);
	}
	
}
