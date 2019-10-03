package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.operational.InitialInvoiceVarietyRateCreate;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateEdit;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateView;

public abstract class AnnualPercentageBHPCreate extends AdminBasePage implements
		PageBeginRenderListener {
	
	private String pageLocation;
	
	public void setPageLocation(String location){
		this.pageLocation = location;
	}
	
	public String getPageLocation(){
		return pageLocation;
	}
	
	public abstract String getVariableStatus();

	public abstract String getBerdasarkanDokumen();
	
	public abstract void setVariablePersentaseTahunan(String year);
	
	public abstract String getVariablePersentaseTahunan();
	
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
	
	private BeanPropertySelectionModel variableStatusModel;

	@InjectPage("annualPercentageBHPEntry")
	public abstract AnnualPercentageBHPEntry getAnnualPercentageBHPEntry();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();
	
	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();
	
	public abstract void setCriteria(String criteria);
	
	public abstract String getCriteria();
	
	public BeanPropertySelectionModel getVariableStatusModel() {

		List<LabelValue> variableStatusList = new ArrayList<LabelValue>();
		variableStatusList.add(new LabelValue("Active", "1"));
		variableStatusList.add(new LabelValue("Inactive", "0"));
		variableStatusModel = new BeanPropertySelectionModel(
				variableStatusList, "label");
		System.out.println(variableStatusModel);

		return variableStatusModel;
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date date = new Date();
		setVariablePersentaseTahunan(format.format(date));
	}

	public IPage doSave (IRequestCycle cycle) {

		String validationMsg = saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		
		String validationMsg2 = validationMore1000();
		if (validationMsg2 != null) {
			addError(getDelegate(), "errorShadow", validationMsg2,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		
		String duplicateData = validationDuplicateKmNo();
		if (duplicateData != null) {
			addError(getDelegate(), "errorShadow", duplicateData,
					ValidationConstraint.CONSISTENCY);
			return null;
		}
		
		
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
		
		addError(getDelegate(), "errorShadow",
			getText("license.submit.success"),
			ValidationConstraint.CONSISTENCY);
		InfoPageCommand infoPageCommand = new InfoPageCommand();
	
		infoPageCommand.setTitle(getText("variableInformation.title"));
		infoPageCommand.addMessage(getText("variableInformation.information"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
					getText("button.finish"), "annualPercentageBHPEntry.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
			
		//Ini digunakan untuk mengupdate tampilan tabel d halaman entry search
		saveObjectToSession(getVariableManager().findByStatus(2),
			"ANNUAL_PERCENTAGE_LIST");
		saveObjectToSession("xxx", "bhpManage");
		
		//Save to audit log
		getUserManager().bhpRateAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Save", getVariablePersentaseTahunan());
			
		return infoPage;
	}

	public IPage doCancel(IRequestCycle cycle) {
		AnnualPercentageBHPEntry entry = getAnnualPercentageBHPEntry();
		
		entry.setVariableStatus(getCriteria());
		saveDataToSession(getCriteria());
		
		return entry;
	}

	private String saveValidation() {
		
		String errorMessage	= null;
		Pattern alfa 		= Pattern.compile("[a-zA-Z,]");
		
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
	
	public String validationMore1000(){
		String errorMessage	= null;
		
		Double total = Double.valueOf(getPercentageYearOne()) + Double.valueOf(getPercentageYearTwo()) + Double.valueOf(getPercentageYearThree())
				+ Double.valueOf(getPercentageYearFour()) + Double.valueOf(getPercentageYearFive()) + Double.valueOf(getPercentageYearSix())
				+ Double.valueOf(getPercentageYearSeven()) + Double.valueOf(getPercentageYearEight()) + Double.valueOf(getPercentageYearNine())
				+ Double.valueOf(getPercentageYearTen());
		
		if(total > 1000){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.moreThan1000");
		}else if(total < 1000){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.lessThan1000");
		}else if(Double.valueOf(getVariablePersentaseTahunan()) > 9999){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentaseTahunan.moreThan4Digit");
		}else if( (Double.valueOf(getPercentageYearOne()) <= 0) || (Double.valueOf(getPercentageYearTwo()) <= 0) || (Double.valueOf(getPercentageYearThree()) <= 0)
				|| (Double.valueOf(getPercentageYearFour()) <= 0)|| (Double.valueOf(getPercentageYearFive()) <= 0)|| (Double.valueOf(getPercentageYearSix()) <= 0)
				|| (Double.valueOf(getPercentageYearSeven()) <= 0)|| (Double.valueOf(getPercentageYearEight()) <= 0)|| (Double.valueOf(getPercentageYearNine()) <= 0)
				|| (Double.valueOf(getPercentageYearTen()) <= 0)){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentaseTahunanDetail.lessThanZero");
		}else if( (Double.valueOf(getPercentageYearOne()) > 999) || (Double.valueOf(getPercentageYearTwo()) > 999) || (Double.valueOf(getPercentageYearThree()) > 999)
				|| (Double.valueOf(getPercentageYearFour()) > 999)|| (Double.valueOf(getPercentageYearFive()) > 999)|| (Double.valueOf(getPercentageYearSix()) > 999)
				|| (Double.valueOf(getPercentageYearSeven()) > 999)|| (Double.valueOf(getPercentageYearEight()) > 999)|| (Double.valueOf(getPercentageYearNine()) > 999)
				|| (Double.valueOf(getPercentageYearTen()) > 999)){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentaseTahunanDetail.moreThan3Digit");
		}
		
		return errorMessage;
	}
	
	public String validationDuplicateKmNo(){
		String errorMessage	= null;
		VariableAnnualPercentage var = getVariableManager().findByKmNo(getBerdasarkanDokumen());
		
		if(var != null){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.duplicateKmNo");
		}
		
		return errorMessage;
	}
	
	public void saveDataToSession(String crit){
		int status = 2;
		if (crit.equals("LabelValue[Inactive, 0]")) {
			status = 0;
		} else if (crit.equals("LabelValue[Active, 1]")) {
			status = 1;
		} else {
			status = 2;
		}

		saveObjectToSession(getVariableManager().findByStatus(status),
				"ANNUAL_PERCENTAGE_LIST");
		saveObjectToSession("xxx", "bhpManage");
	}

}
