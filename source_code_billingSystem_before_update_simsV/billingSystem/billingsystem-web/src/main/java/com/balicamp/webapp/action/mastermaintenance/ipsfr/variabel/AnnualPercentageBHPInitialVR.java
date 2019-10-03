package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.helpers.IntializationUtil;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.operational.InitialInvoiceVarietyRateCreate;

public abstract class AnnualPercentageBHPInitialVR extends AdminBasePage implements
		PageBeginRenderListener {
	
	public abstract void setBerdasarkanDokumen(String kmNO);

	public abstract String getBerdasarkanDokumen();
	
	public abstract void setVariablePersentaseTahunan(String tahun);
	
	public abstract String getVariablePersentaseTahunan();

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

	@InjectPage("annualPercentageBHPEntry")
	public abstract AnnualPercentageBHPEntry getAnnualPercentageBHPEntry();

	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();

	private BeanPropertySelectionModel variableStatusModel =  null;
	
	public void setVariableStatusModel(int num){
		List<LabelValue> variableStatusList = new ArrayList<LabelValue>();
		if(num == 1){
			variableStatusList.add(new LabelValue("Active", "1"));
			variableStatusList.add(new LabelValue("Inactive", "0"));
			variableStatusModel = new BeanPropertySelectionModel(variableStatusList, "label");
		}else if(num == 0){
			variableStatusList.add(new LabelValue("Inactive", "0"));
			variableStatusList.add(new LabelValue("Active", "1"));
			variableStatusModel = new BeanPropertySelectionModel(variableStatusList, "label");
		}
		
	}

	public BeanPropertySelectionModel getVariableStatusModel() {
		if(variableStatusModel == null){
			List<LabelValue> variableStatusList = new ArrayList<LabelValue>();
			variableStatusList.add(new LabelValue("Active", "1"));
			variableStatusList.add(new LabelValue("Inactive", "0"));
			variableStatusModel = new BeanPropertySelectionModel(variableStatusList, "label");
			
			return variableStatusModel;
		}else{
			return variableStatusModel;
		}

		
	}

	public IPage doSave(IRequestCycle cycle) {
		
		int status = 2;

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return null;
		}

		VariableAnnualPercentage var = getVariableManager()
				.findByAnnualPercentId(getAnnualPercentId());
		
		VariableAnnualPercentage varYear = getVariableManager().findByYear(Integer.valueOf(getVariablePersentaseTahunan()));
		
		if(varYear != null){
			
			var.setKmNo(getBerdasarkanDokumen());
			var.setPercentYear(Integer.valueOf(getVariablePersentaseTahunan()));
			var.setUpdatedOn(new Date());
			if (getVariableStatus().equals("LabelValue[Inactive, 0]")) {
				var.setVariableStatus(0);
			} else if (getVariableStatus().equals("LabelValue[Active, 1]")){
				var.setVariableStatus(1);
			}
			var.setUpdatedBy(getUserLoginFromSession().getUsername());
	
			getVariableManager().save(var);
	
			List<VariableAnnualPercentageDetail> variablesDetailList = getVariableDetailManager()
					.findByAnnualPercentId(getAnnualPercentId());
	
			for (VariableAnnualPercentageDetail temp : variablesDetailList) {
	
				if (temp.getYearTo() == 1) {
					temp.setPercentage(new BigDecimal(getPercentageYearOne()));
				} else if (temp.getYearTo() == 2) {
					temp.setPercentage(new BigDecimal(getPercentageYearTwo()));
	
				} else if (temp.getYearTo() == 3) {
					temp.setPercentage(new BigDecimal(getPercentageYearThree()));
	
				} else if (temp.getYearTo() == 4) {
					temp.setPercentage(new BigDecimal(getPercentageYearFour()));
	
				} else if (temp.getYearTo() == 5) {
					temp.setPercentage(new BigDecimal(getPercentageYearFive()));
	
				} else if (temp.getYearTo() == 6) {
					temp.setPercentage(new BigDecimal(getPercentageYearSix()));
	
				} else if (temp.getYearTo() == 7) {
					temp.setPercentage(new BigDecimal(getPercentageYearSeven()));
	
				} else if (temp.getYearTo() == 8) {
					temp.setPercentage(new BigDecimal(getPercentageYearEight()));
	
				} else if (temp.getYearTo() == 9) {
					temp.setPercentage(new BigDecimal(getPercentageYearNine()));
	
				} else if (temp.getYearTo() == 10) {
					temp.setPercentage(new BigDecimal(getPercentageYearTen()));
	
				}
	
			}
	
			getVariableDetailManager().saveCollection(variablesDetailList);
			
		}else{
			
			VariableAnnualPercentage variable = new VariableAnnualPercentage();
			
			if (getVariableStatus().equals("LabelValue[Inactive, 0]")) {
				variable.setVariableStatus(0);
			} else if (getVariableStatus().equals("LabelValue[Active, 1]")){
				variable.setVariableStatus(1);
			}
		
			variable.setKmNo(getBerdasarkanDokumen());
			variable.setPercentYear(Integer.valueOf(getVariablePersentaseTahunan()));
			variable.setCreatedOn(new Date());
			variable.setCreatedBy(getUserLoginFromSession().getUsername());
			variable.setUpdatedOn(new Date());
			variable.setUpdatedBy(getUserLoginFromSession().getUsername());
			
			getVariableManager().save(variable);
			
			List <VariableAnnualPercentageDetail> listVariableDetail = new ArrayList<VariableAnnualPercentageDetail>();
			
			VariableAnnualPercentageDetail variablesYearOne = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearTwo = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearThree = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearFour = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearFive = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearSix = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearSeven = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearEight = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearNine = new VariableAnnualPercentageDetail();
			VariableAnnualPercentageDetail variablesYearTen = new VariableAnnualPercentageDetail();
			
			variablesYearOne.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearOne.setYearTo(1);
			variablesYearOne.setPercentage(new BigDecimal(getPercentageYearOne()));
			listVariableDetail.add(variablesYearOne);
			
			variablesYearTwo.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearTwo.setYearTo(2);
			variablesYearTwo.setPercentage(new BigDecimal(getPercentageYearTwo()));
			listVariableDetail.add(variablesYearTwo);
			
			variablesYearThree.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearThree.setYearTo(3);
			variablesYearThree.setPercentage(new BigDecimal(getPercentageYearThree()));
			listVariableDetail.add(variablesYearThree);
			
			variablesYearFour.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearFour.setYearTo(4);
			variablesYearFour.setPercentage(new BigDecimal(getPercentageYearFour()));
			listVariableDetail.add(variablesYearFour);
			
			variablesYearFive.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearFive.setYearTo(5);
			variablesYearFive.setPercentage(new BigDecimal(getPercentageYearFive()));
			listVariableDetail.add(variablesYearFive);
			
			variablesYearSix.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearSix.setYearTo(6);
			variablesYearSix.setPercentage(new BigDecimal(getPercentageYearSix()));
			listVariableDetail.add(variablesYearSix);
			
			variablesYearSeven.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearSeven.setYearTo(7);
			variablesYearSeven.setPercentage(new BigDecimal(getPercentageYearSeven()));
			listVariableDetail.add(variablesYearSeven);
			
			variablesYearEight.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearEight.setYearTo(8);
			variablesYearEight.setPercentage(new BigDecimal(getPercentageYearEight()));
			listVariableDetail.add(variablesYearEight);
			
			variablesYearNine.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearNine.setYearTo(9);
			variablesYearNine.setPercentage(new BigDecimal(getPercentageYearNine()));
			listVariableDetail.add(variablesYearNine);
			
			variablesYearTen.setAnnualPercentId(variable.getAnnualPercentId());
			variablesYearTen.setYearTo(10);
			variablesYearTen.setPercentage(new BigDecimal(getPercentageYearTen()));
			listVariableDetail.add(variablesYearTen);
			
			getVariableDetailManager().saveCollection(listVariableDetail);
			
		}
		
		InitialInvoiceVarietyRateCreate initialInvoice = (InitialInvoiceVarietyRateCreate) cycle 
				.getPage("initialInvoiceVarietyRateCreate");
		
		return initialInvoice;
		
	}

	public IPage doCancel() {
		AnnualPercentageBHPEntry entry = getAnnualPercentageBHPEntry();
		return entry;
	}

	private String saveValidation() {

		String errorMessage	= null;
		Pattern alfa 		= Pattern.compile("[a-zA-Z]");
		
		if(getVariableStatus() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.status.null");
		}else if(getBerdasarkanDokumen() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.berdasarkanDokumen.null");
		}else if(getVariablePersentaseTahunan() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentaseTahunan.null");
		}else if(getPercentageYearOne() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearTwo() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearThree() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearFour() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearFive() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearSix() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearSeven() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearEight() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearNine() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(getPercentageYearTen() == null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.null");
		}else if(alfa.matcher(getVariablePersentaseTahunan()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.berdasarkanDokumen.numerik");
		}else if(alfa.matcher(getPercentageYearOne()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearTwo()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearThree()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearFour()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearFive()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearSix()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearSeven()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearEight()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearNine()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}else if(alfa.matcher(getPercentageYearTen()).find()){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.numerik");
		}
		
		return errorMessage;
	}

}
