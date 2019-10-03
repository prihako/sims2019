package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.InvoiceManagerObsolete;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class LicenseEdit extends AdminBasePage implements
		PageBeginRenderListener {

	public LicenseEdit() {
	}

	public abstract Licenses getLicenses();
	public abstract void  setLicenses(Licenses license);

	public abstract Services getServices();
	public abstract void  setServices(Services service);

	public abstract SubServices getSubServices();
	public abstract void  setSubServices(SubServices subService);

	public abstract Map getLicenseMap();
	public abstract void setLicenseMap(Map licenseMap);


	public abstract Long getBsLicenceId();
	public abstract void  setBsLicenceId(Long bsLicenceId);

	public abstract String getServiceName();
	public abstract void  setServiceName(String serviceName);

	public abstract BigDecimal getServiceId();
	public abstract void  setServiceId(BigDecimal serviceId);


	public abstract String getSubServicesName();
	public abstract void setSubServicesName(String subServicesName);

	public abstract BigDecimal getSubServiceId();
	public abstract void setSubServiceId(BigDecimal subServiceId);

	public abstract String getBhpMethod();
	public abstract void setBhpMethod(String bhpMethod);

	public abstract BigDecimal getClientID();
	public abstract void setClientID(BigDecimal clientID);

	public abstract String getClientName();
	public abstract void setClientName(String clientName);

	public abstract String getLicenceId();
	public abstract void setLicenceId(String licenceId);

	public abstract String getLicenceNo();
	public abstract void setLicenceNo(String licenceNo);

	public abstract Date getLicenceDate();
	public abstract void setLicenceDate(Date licenceDate);

	public abstract Date getLicenceBegin();
	public abstract void setLicenceBegin(Date licenceBegin);

	public abstract Date getLicenceEnd();
	public abstract void setLicenceEnd(Date licenceEnd);

	public abstract BigDecimal getYearTo();
	public abstract void setYearTo(BigDecimal yearTo);

	public abstract String getLicenceNoBirate();
	public abstract void setLicenceNoBirate(String licenceNoBirate);

	public abstract BigDecimal getTransmitMin();
	public abstract void setTransmitMin(BigDecimal transmitMin);

	public abstract BigDecimal getTransmitMax();
	public abstract void setTransmitMax(BigDecimal transmitMax);

	public abstract BigDecimal getReceiveMin();
	public abstract void setReceiveMin(BigDecimal receivetMin);

	public abstract BigDecimal getReceiveMax();
	public abstract void setReceiveMax(BigDecimal receiveMax);

	public abstract String getBhpPaymentType();
	public abstract void setBhpPaymentType(String bhpPaymentType);

	public abstract Date getPaymentDueDate();
	public abstract void setPaymentDueDate(Date paymentDueDate);

	public abstract BigDecimal getBhpTotal();
	public abstract void setBhpTotal(BigDecimal bhpTotal);

	public abstract String getBhpUpfrontFee();
	public abstract void setBhpUpfrontFee(String bhpUpfrontFee);

	public abstract String getBhpRate();
	public abstract void setBhpRate(String bhpRate);

	public abstract String getBhpCalcIndex();
	public abstract void setBhpCalcIndex(String bhpCalcIndex);

	public abstract String getBhpPhl();
	public abstract void setBhpPhl(String bhpPhl);

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract String getBhpPhlType();
	public abstract void setBhpPhlType(String bhpPhlType);

	public abstract BigDecimal getPercentageYearOne();

	public abstract void setPercentageYearOne(BigDecimal percentageYearOne);

	public abstract BigDecimal getPercentageYearTwo();

	public abstract void setPercentageYearTwo(BigDecimal percentageYearTwo);

	public abstract BigDecimal getPercentageYearThree();

	public abstract void setPercentageYearThree(BigDecimal percentageYearThree);

	public abstract BigDecimal getPercentageYearFour();

	public abstract void setPercentageYearFour(BigDecimal percentageYearFour);

	public abstract BigDecimal getPercentageYearFive();

	public abstract void setPercentageYearFive(BigDecimal percentageYearFive);

	public abstract BigDecimal getPercentageYearSix();

	public abstract void setPercentageYearSix(BigDecimal percentageYearSix);

	public abstract BigDecimal getPercentageYearSeven();

	public abstract void setPercentageYearSeven(BigDecimal percentageYearSeven);

	public abstract BigDecimal getPercentageYearEight();

	public abstract void setPercentageYearEight(BigDecimal percentageYearEight);

	public abstract BigDecimal getPercentageYearNine();

	public abstract void setPercentageYearNine(BigDecimal percentageYearNine);

	public abstract BigDecimal getPercentageYearTen();

	public abstract void setPercentageYearTen(BigDecimal percentageYearTen);

	public abstract String getStatusActive();

	public abstract void setStatusActive(String activeStatus);

	public abstract String getBaseOnNote();

	public abstract void setBaseOnNote(String baseOnNote);

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectSpring("invoiceManagerO")
	public abstract InvoiceManagerObsolete getInvoiceManager();

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();

	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();

	@InjectPage("licenseSearch")
	public abstract LicenseSearch getLicenseSearch();

	@InjectPage("licenseEntry")
	public abstract LicenseEntry getLicenseEntry();

	@InjectPage("licenseView")
	public abstract LicenseView getLicenseView();

	protected final Log log = LogFactory.getLog(LicenseEdit.class);

	public abstract Licenses getRow1();
	public abstract Licenses getRow2();

	public abstract String getMetodeBhp();
	public abstract void setMetodeBhp(String metodeBhp);

	public abstract String getMetodePembayaran();
	public abstract void setMetodePembayaran(String metodePembayaran);

	//
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if(!pageEvent.getRequestCycle().isRewinding()){

			if(getFields()==null){
				setFields(new HashMap());
			}

			if(getLicenses()!=null){

				getFields().put("UP_FRONT_FEE", getLicenses().getBhpUpfrontFee());
				getFields().put("HL", ((getLicenses().getBhpHl() == null) ? new BigDecimal(0) : getLicenses().getBhpHl()));
				getFields().put("PHL", ((getLicenses().getBhpPhl() == null) ? new BigDecimal(0) : getLicenses().getBhpPhl()));
				getFields().put("TOTAL", getLicenses().getBhpTotal());

				if(getLicenses().getBhpMethod().equals("FR")){
					setMetodeBhp(getText("method.bhp.flat"));
				}else if(getLicenses().getBhpMethod().equals("VR")){
					setMetodeBhp(getText("method.bhp.variated"));
					getFields().put("BHP_RATE", getLicenses().getBhpRate());
					getFields().put("CALC_INDEX", ((getLicenses().getBhpCalcIndex())));
					getFields().put("ANNUAL_PERCENTAGE", ((getLicenses().getBhpAnnualPercent())));
					getFields().put("ANNUAL_VALUE", getLicenses().getBhpAnnualValue());
				}else{
					setMetodeBhp(getText("method.bhp.conversion"));
				}

				if(getLicenses().getBhpPaymentType().equals("FP")){
					setMetodePembayaran(getText("method.payment.full"));
				}else{
					setMetodePembayaran(getLicenses().getBhpPaymentType());
				}

			} else {
				if(getFields()!=null){
					if(getFields().get("LICENSE")!=null){
						setLicenses((Licenses) getFields().get("LICENSE"));
					}
				}
			}
		}

		List<VariableAnnualRate> obj = getVariableAnnualRateManager()
				.findByStatus(1);

		List<VariableAnnualPercentage> variable = getVariableManager()
				.findByStatus(1);

		Long annualPercentID = variable.get(0).getAnnualPercentId();

		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentID, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentID, 2);
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentID, 3);
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentID, 4);
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentID, 5);
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentID, 6);
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentID, 7);
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentID, 8);
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentID, 9);
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentID, 10);

		setPercentageYearOne(percentageYearOne.getPercentage());
		setPercentageYearTwo(percentageYearTwo.getPercentage());
		setPercentageYearThree(percentageYearThree.getPercentage());
		setPercentageYearFour(percentageYearFour.getPercentage());
		setPercentageYearFive(percentageYearFive.getPercentage());
		setPercentageYearSix(percentageYearSix.getPercentage());
		setPercentageYearSeven(percentageYearSeven.getPercentage());
		setPercentageYearEight(percentageYearEight.getPercentage());
		setPercentageYearNine(percentageYearNine.getPercentage());
		setPercentageYearTen(percentageYearTen.getPercentage());

		setBhpRate(obj.get(0).getRateValue().toString());
//		setBaseOnNote(obj.get(0).getBaseOnNote());
		setStatusActive("active");

		BigDecimal multiplyIndex = (new BigDecimal(obj.get(0).getRateValue()
				.toString()).divide(new BigDecimal(100)))
				.add(new BigDecimal(1));

		setBhpCalcIndex(multiplyIndex.toString());

	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
		if(getFields()!=null){
			getFields().clear();
		}
	}

	public IPage doSearch() {
		if (getDelegate().getHasErrors()){
			return null;
		}
		LicenseSearch licenseSearch = getLicenseSearch();
		return licenseSearch;
	}

	public void doDraft(IRequestCycle cycle) {

		String validationMsg 	= saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		Licenses licenses 			= null;
		List<Licenses> licenseList 	= null;

//		getLicenseManager().deleteByLicenseId(getLicenses().getLicenceId());

		if(getFields().get("BHP_LIST")!=null){
			licenseList = (List<Licenses>) getFields().get("BHP_LIST");
		}else if(getLicenses()!=null){
			licenseList = getLicenseManager().findByLicenseId(getLicenses().getLicenceId());
		}

		if(licenseList!=null && licenseList.size()>0){
			for(int i=0;i<licenseList.size();i++){
				licenses = licenseList.get(i);
				if(licenses.getBhpMethod().equals("VR") && !licenses.getYearTo().toString().equals("1")){
					licenses.setLicenceStatus(null);
				}else{
					licenses.setLicenceStatus("D");
				}
				licenses.setLicenceComment(getLicenses().getLicenceComment());
				licenses.setUpdatedOn(new Date());
				licenses.setUpdatedBy(getUserLoginFromSession().getUsername());
				getLicenseManager().save(licenses);
			}
		}

		getFields().clear();

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { licenses.getLicenceId(), licenses.getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "licenseEntry.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);
	}

	public void doSubmit(IRequestCycle cycle) {

		String validationMsg 	= saveValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		Licenses licenses 			= null;
		List<Licenses> licenseList 	= null;

		if(getFields().get("BHP_LIST")!=null){
			licenseList = (List<Licenses>) getFields().get("BHP_LIST");
		}else if(getLicenses()!=null){
			licenseList = getLicenseManager().findByLicenseId(getLicenses().getLicenceId());
		}

		if(getLicenses().getYearTo().toString().equals("1") && !getLicenses().getLicenceStatus().equals("S")){
			getInvoiceManager().createFirstYearInvoice(getLicenses(), getUserLoginFromSession().getUsername());
		}

		if(licenseList!=null && licenseList.size()>0){
			for(int i=0;i<licenseList.size();i++){
				licenses = licenseList.get(i);
				if(licenses.getBhpMethod().equals("VR")
						&& licenses.getYearTo().longValue()>getLicenses().getYearTo().longValue()){
					licenses.setLicenceStatus(null);
				}else{
					licenses.setLicenceStatus("S");
				}
				licenses.setLicenceComment(getLicenses().getLicenceComment());
				licenses.setUpdatedOn(new Date());
				licenses.setUpdatedBy(getUserLoginFromSession().getUsername());
				getLicenseManager().save(licenses);
			}
		}

		getFields().clear();

		InfoPageCommand infoPageCommand = new InfoPageCommand();

		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information",
				new Object[] { licenses.getLicenceId(), licenses.getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "licenseEntry.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);
	}


	public void  doCancel(IRequestCycle cycle) {
		LicenseView licenseView = (LicenseView) cycle.getPage("licenseView");
		licenseView.setFields(getFields());
		if(getFields().get("LICENSE")!=null){
			licenseView.setLicenses((Licenses) getFields().get("LICENSE"));
		}else{
			licenseView.setLicenses(getLicenses());
		}
		licenseView.setServices(getServices());
		licenseView.setSubServices(getSubServices());
		cycle.activate(licenseView);
	}

	private String generalValidation() {

		String errorMessage	= null;

		if(getLicenses().getLicenceNo()==null){
			errorMessage = getText("calculateBhp.error.licenseNumber");
		}else if((getFields().get("PHL")!=null && getFields().get("HL")!=null) &&
				(new BigDecimal(getFields().get("PHL").toString()).intValue()>0 && new BigDecimal(getFields().get("HL").toString()).intValue()>0)){
			errorMessage = getText("calculateBhp.error.phlBoth");
		}else{
			errorMessage = null;
		}
		return errorMessage;
	}

	private String saveValidation() {

		String errorMessage	= null;

		if(getLicenses().getLicenceNo()==null){
			errorMessage = getText("calculateBhp.error.licenseNumber");
		}else if(getLicenses().getBhpTotal()==null){
			errorMessage = getText("calculateBhp.error.calculateBhp");
		}else if(getLicenses().getYearTo().intValue()>1
				&& (getLicenses().getBhpPhl()!=null && getLicenses().getBhpPhl().intValue()>0)
				&& getLicenses().getLicenceComment()==null){
			errorMessage = getText("calculateBhp.error.emptyRemark");
		}else{
			errorMessage = null;
		}
		return errorMessage;
	}

	public List getBhpList() {
		List<Licenses> bhpList = (List<Licenses>) getFields().get("BHP_LIST");
		return bhpList;
	}

	public List getViewList() {
		List<Licenses> viewList = (List<Licenses>) getFields().get("VIEW_LIST");
		return viewList;
	}

	@SuppressWarnings("unchecked")
	public void hitung(IRequestCycle cycle){

		List<Licenses> licenseList 	= null;
		List<Licenses> viewList 	= new ArrayList<Licenses>();
		List<Licenses> bhpList 		= new ArrayList<Licenses>();

		String validationMsg 	= generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		if(getLicenses()!=null){
			licenseList = getLicenseManager().findByLicenseId(getLicenses().getLicenceId());
		}

		for(int i=0;i<licenseList.size();i++){
			Licenses license = licenseList.get(i);

			if(license.getYearTo().intValue()>(getLicenses().getYearTo().intValue()-1)){
				if(getFields().get("PHL")!=null){
					if(new BigDecimal(getFields().get("PHL").toString()).longValue()>0){
						license.setBhpPhl((BigDecimal) getFields().get("PHL"));
						license.setBhpHl(new BigDecimal(0));
					}else{
						license.setBhpPhl(new BigDecimal(0));
					}
				}else if(getFields().get("HL")!=null){
					if(new BigDecimal(getFields().get("HL").toString()).longValue()>0){
						license.setBhpHl((BigDecimal) getFields().get("HL"));
						license.setBhpPhl(new BigDecimal(0));
					}else{
						license.setBhpHl(new BigDecimal(0));
					}
				}else if(getFields().get("PHL")==null){
					license.setBhpPhl(new BigDecimal(0));
				}else if(getFields().get("HL")==null){
					license.setBhpHl(new BigDecimal(0));
				}

				if(getFields().get("COMMENT")!=null){
					license.setLicenceComment(getFields().get("COMMENT").toString());
				}

				if(getLicenses().getYearTo().toString().equals("1")
						&& license.getYearTo().toString().equals("1")
						&& getFields().get("UP_FRONT_FEE")!=null){
					license.setBhpUpfrontFee(new BigDecimal(getFields().get("UP_FRONT_FEE").toString()));
				}

				if(getLicenses().getBhpMethod().equals("FR")){
					license.setBhpAnnualValue(license.getBhpHl().add(license.getBhpPhl()));
					license.setBhpTotal(license.getBhpAnnualValue().add(license.getBhpUpfrontFee()));
				}

				if(getLicenses().getBhpMethod().equals("VR")){
					BigDecimal phl = license.getBhpHl().add(license.getBhpPhl());
					BigDecimal index = license.getBhpCalcIndex();
					BigDecimal bhpPercentage = license.getBhpAnnualPercent().divide(new BigDecimal(100));

					if(license.getYearTo().toString().equals("1")){
						license.setBhpAnnualValue(phl.multiply(bhpPercentage));
					}else{
						license.setBhpAnnualValue(phl.multiply(bhpPercentage).multiply(index));
					}

					license.setBhpTotal(license.getBhpUpfrontFee().add(license.getBhpAnnualValue()));

					if(license.getYearTo().toString().equals(getLicenses().getYearTo().toString())){
						viewList.add(license);
					}
				}
			}
			bhpList.add(license);
		}

		setLicenses(bhpList.get(getLicenses().getYearTo().intValue()-1));
		getFields().put("BHP_LIST", bhpList);
		getFields().put("VIEW_LIST", viewList);
		getFields().put("UP_FRONT_FEE", bhpList.get(0).getBhpUpfrontFee());
		getFields().put("HL", ((getLicenses().getBhpHl() == null) ? new BigDecimal(0) : getLicenses().getBhpHl()));
		getFields().put("PHL", ((getLicenses().getBhpPhl() == null) ? new BigDecimal(0) : getLicenses().getBhpPhl()));
		getFields().put("TOTAL", getLicenses().getBhpTotal());
		if(getLicenses().getBhpMethod().equals("VR")){
			getFields().put("BHP_RATE", getLicenses().getBhpRate());
			getFields().put("CALC_INDEX", ((getLicenses().getBhpCalcIndex())));
			getFields().put("ANNUAL_PERCENTAGE", ((getLicenses().getBhpAnnualPercent())));
			getFields().put("ANNUAL_VALUE", getLicenses().getBhpAnnualValue());
		}
	}
}
