package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.operational.InitialInvoiceVarietyRateCreate;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateEdit;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateView;

public abstract class AnnualPercentageBHPViewVariety extends AdminBasePage implements
		PageBeginRenderListener {
	
	private String pageLocation;
	
	public AnnualPercentageBHPViewVariety(){
		
	}
	//
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}
	
	public void setPageLocation(String location){
		this.pageLocation = location;
	}
	
	public String getPageLocation(){
		return pageLocation;
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
	
	@InjectPage("annualPercentageBHPCreate")
	public abstract AnnualPercentageBHPCreate getAnnualPercentageBHPCreate();

	public IPage doEdit (IRequestCycle cycle) {
		
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
		variableEdit.setPageLocation(pageLocation);
		
		return variableEdit;
	}
	
	public IPage doNewInput(IRequestCycle cycle){
		AnnualPercentageBHPCreate licenseCreate = getAnnualPercentageBHPCreate();
		
		licenseCreate.setPageLocation(pageLocation);
		
		return licenseCreate;
	}
	
	
	public IPage doCancel (IRequestCycle cycle) {
//		To call back page that call this page
		if(getPageLocation().equalsIgnoreCase("initial")){
			InitialInvoiceVarietyRateCreate initial = (InitialInvoiceVarietyRateCreate) cycle.getPage("initialInvoiceVarietyRateCreate");
			return initial;
		}else if(getPageLocation().equalsIgnoreCase("manageView")){
			ManageInvoiceVarietyRateView view = (ManageInvoiceVarietyRateView) cycle.getPage("manageInvoiceVarietyRateView");
			return view;
		}else{
			ManageInvoiceVarietyRateEdit edit = (ManageInvoiceVarietyRateEdit) cycle.getPage("manageInvoiceVarietyRateEdit");
			return edit;
		}
	}
	
	
	

}
