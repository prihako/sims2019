package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.balicamp.model.operational.Invoices;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.operational.InvoiceManagerObsolete;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class LicenseView extends AdminBasePage implements
		PageBeginRenderListener {

	public LicenseView() {

	}

	public abstract Licenses getLicenses();
	public abstract void  setLicenses(Licenses license);

	public abstract Services getServices();
	public abstract void  setServices(Services service);

	public abstract SubServices getSubServices();
	public abstract void  setSubServices(SubServices subService);

	public abstract Long getBsLicenceId();
	public abstract void  setBsLicenceId(Long bsLicenceId);

	public abstract String getServiceName();
	public abstract void  setServiceName(String serviceName);

	public abstract BigDecimal getServiceId();
	public abstract void  setServiceId(BigDecimal serviceId);

	public abstract String getSubServiceName();
	public abstract void setSubServiceName(String subServicesName);

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

	public abstract String getLicenceDate();
	public abstract void setLicenceDate(String licenceDate);

	public abstract String getLicenceBegin();
	public abstract void setLicenceBegin(String licenceBegin);

	public abstract String getLicenceEnd();
	public abstract void setLicenceEnd(String licenceEnd);

	public abstract String getLicenceExpireDate();
	public abstract void setLicenceExpireDate(String licenceExpireDate);

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

	public abstract String getPaymentDueDate();
	public abstract void setPaymentDueDate(String paymentDueDate);

	public abstract BigDecimal getBhpTotal();
	public abstract void setBhpTotal(BigDecimal bhpTotal);


	public abstract String getLicenceStatus();
	public abstract void setLicenceStatus(String licenceStatus);

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	@InjectSpring("invoiceManagerO")
	public abstract InvoiceManagerObsolete getInvoiceManager();

	@InjectPage("licenseCreate")
	public abstract LicenseCreate getLicenseCreate();

	@InjectPage("licenseEdit")
	public abstract LicenseEdit getLicenseEdit();

	@InjectPage("licenseEntry")
	public abstract LicenseEntry getLicenseEntry();

	protected final Log log = LogFactory.getLog(LicenseView.class);

	public abstract Licenses getRow1();

	//
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if(getFields()==null){
			setFields(new HashMap());
		}

//		List<Licenses> licenseList 	= getLicenseManager().findByLicenseId(getLicenses().getLicenceId());
//		if(licenseList!=null && licenseList.size()>0){
//			getFields().put("BHP_LIST", licenseList);
//		}

		final String DATE_FORMAT = "dd-MMM-yyyy";

		if(getLicenses()!=null){
			setLicenceBegin(new SimpleDateFormat(DATE_FORMAT).format((getLicenses().getLicenceBegin())));
			setLicenceEnd(new SimpleDateFormat(DATE_FORMAT).format((getLicenses().getLicenceEnd())));
			setLicenceDate(new SimpleDateFormat(DATE_FORMAT).format((getLicenses().getLicenceDate())));
			setLicenceExpireDate(new SimpleDateFormat(DATE_FORMAT).format((getLicenses().getLicenceExpireDate())));
			setPaymentDueDate(new SimpleDateFormat(DATE_FORMAT).format((getLicenses().getPaymentDueDate())));
		}

		List<Licenses> licenseList 	= new ArrayList<Licenses>();

		if(getLicenses()==null){
			setLicenses((Licenses) getFields().get("LICENSE"));
		}

		licenseList.add(getLicenses());
		getFields().put("BHP_LIST", licenseList);

	}

	//
	public List getBhpList() {
		List<Licenses> bhpList = (List<Licenses>) getFields().get("BHP_LIST");
		return bhpList;
	}

	public IPage doEdit(IRequestCycle cycle) {

		if (getDelegate().getHasErrors()){
			return null;
		}

		List<Licenses> licenseList	= null;
		if(getFields().get("BHP_LIST")!=null){
			licenseList = (List<Licenses>) getFields().get("BHP_LIST");
		}

		Licenses license = null;

		if(licenseList!=null && licenseList.get(0)!=null){
			license = licenseList.get(0);
		}

		if(getLicenses()!=null){
			license = getLicenses();
		}

		Services service 			= getServicesManager().findServicesById(license.getServiceId().longValue());
		SubServices subService		= getSubServicesManager().findSubServicesById(license.getSubServiceId().longValue());
		LicenseEdit licenseEdit 	= (LicenseEdit) cycle.getPage("licenseEdit");

		licenseEdit.setLicenses(license);
		licenseEdit.setServices(service);
		licenseEdit.setSubServices(subService);

		getFields().remove("BHP_LIST");
		getFields().put("LICENSE", license);

		licenseEdit.setFields(getFields());

		return licenseEdit;
	}

	public void doCancel(IRequestCycle cycle) {
		Licenses license 			= getLicenses();
		LicenseEntry licenseEntry 	= (LicenseEntry) cycle.getPage("licenseEntry");

		getFields().put("VIEW", true);

		if(getFields()!=null && getFields().get("SEARCH")!=null){
			getFields().remove("SEARCH");
		}

		licenseEntry.setFields(getFields());
		licenseEntry.setService(getServicesManager().findServicesById(license.getServiceId().longValue()).getServiceName());
		licenseEntry.setSubServices(getSubServicesManager().findSubServicesById(license.getSubServiceId().longValue()).getSubserviceName());
		licenseEntry.setBhpMethod(license.getBhpMethod());
		licenseEntry.setClientName(license.getClientName());
		licenseEntry.setLicenceNo(license.getLicenceNo());

		cycle.activate(licenseEntry);
	}

	public void doDelete(IRequestCycle cycle) {

		String validationMsg 	= deleteValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<Licenses> licenses = getLicenseManager().findByLicenseId(getLicenses().getLicenceId());

		for (Licenses obj : licenses){
			obj.setLicenceStatus("C");
			getLicenseManager().save(obj);
		}

		getFields().clear();

		InfoPageCommand infoPageCommand = new InfoPageCommand();
		// contruct status page
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.delete.information",
				new Object[] { licenses.get(0).getLicenceId(), licenses.get(0).getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "licenseEntry.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);
	}

	@SuppressWarnings("unchecked")
	private String deleteValidation() {

		String errorMessage	= null;
		List<Invoices> invoiceList = getInvoiceManager().findGeneratedInvoiceByLicense(
										getLicenses().getLicenceNo(), getLicenses().getLicenceId(), getLicenses().getYearTo().toString());
		if(invoiceList!=null && invoiceList.size()>0){
			errorMessage = getText("license.error.delete");
		}
		return errorMessage;
	}

}
