package com.balicamp.webapp.action.operational.invoice.invoiceMain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.model.operational.Invoices;
import com.balicamp.model.parameter.SystemParameter;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.operational.InvoiceManagerObsolete;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.LicenseSearch;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class InvoiceCreateDetail extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectSpring("subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	@InjectSpring("licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectSpring("invoiceManagerO")
	public abstract InvoiceManagerObsolete getInvoiceManager();

	@Override
	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	public abstract void setServices(Services services);
	public abstract Services getServices();

	public abstract void setSubServices(SubServices subServices);
	public abstract SubServices getSubServices();

	public abstract void setLicenses(Licenses licenses);
	public abstract Licenses getLicenses();

	public abstract void setInvoices(Invoices invoices);
	public abstract Invoices getInvoices();

	public abstract void setInvoiceType(String invoiceType);
	public abstract String getInvoiceType();

	public abstract void setInvoiceMainType(String invoiceMainType);
	public abstract String getInvoiceMainType();

	public abstract void setInvoiceNumber(String invoiceNumber);
	public abstract String getInvoiceNumber();

	public abstract String getLicenseBegin();
	public abstract void setLicenseBegin(String licenseBegin);

	public abstract String getLicenseEnd();
	public abstract void setLicenseEnd(String licenseEnd);

	public abstract String getInvoiceDueDate();
	public abstract void setInvoiceDueDate(String invoiceDueDate);

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract void setMethodBhp(String methodBhp);
	public abstract String getMethodBhp();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if(getFields()==null){
			setFields(new HashMap());
		}

//		if(!pageEvent.getRequestCycle().isRewinding()){
//			if(!isNotFirstLoad()){
//				setNotFirstLoad(true);
//				if(getFields()==null){
//					setFields(new HashMap());
//				}
//			}
//		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public IPropertySelectionModel getInvoiceTypeModel() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("INIT", "Initial");
		map.put("MANUAL", "Manual");
    	return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getInvoiceMainTypeModel() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		List<SystemParameter> sParamList = getSystemParameterManager().findListByParamGroup("invoicetype");
		for(int i=0;i<sParamList.size();i++){
			map.put(sParamList.get(i).getParamValue(), sParamList.get(i).getDescription());
		}
    	return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public void search(IRequestCycle cycle) {

		if(getInvoiceType()==null){
			addError(getDelegate(), "errorShadow", getText("invoice.error.invoiceType.empty"), ValidationConstraint.CONSISTENCY);
			return;
		}

		LicenseSearch licenseSearch = (LicenseSearch) cycle.getPage("licenseSearch");

		getFields().put("PAGE", "INVOICE");
		getFields().put("LICENSE_LIST", null);
		getFields().put("INVOICE_LIST", null);

		licenseSearch.setFields(getFields());
		licenseSearch.setInvoiceType(getInvoiceType());

		cycle.activate(licenseSearch);
	}

	public void createInvoice(IRequestCycle cycle){

		String invoiceNumber 	= getInvoiceManager().generateInvoiceNumber(getInvoices().getBaseInvAmount(), getLicenses(),
									getUserLoginFromSession().getUsername());

		String status			= getSystemParameterManager().findByParamNameAndParamGroup("invoicestatus", "invoicestatus.unpaid").getParamValue();

		if(invoiceNumber!=null){
			getInvoices().setInvoiceNo(invoiceNumber);
			getInvoices().setLicenceNo(getLicenses().getLicenceNo());
			getInvoices().setLicenceId(getLicenses().getLicenceId());
			getInvoices().setYearTo(getLicenses().getYearTo());
			getInvoices().setInvoiceDate(new Date());
			getInvoices().setCreatedOn(new Date());
			getInvoices().setCreatedBy(getUserLoginFromSession().getUsername());
			getInvoices().setInvoiceStatus(status);
			getInvoices().setBaseInvAmount(getLicenses().getBhpTotal());
			getInvoices().setTotalInvAmount(getLicenses().getBhpTotal());
			getInvoiceManager().saveOrUpdateInvoices(getInvoices());

			getFields().put("SUCCESS", "SUCCESS");

			addError(getDelegate(), "errorShadow", getText("invoice.success.generate"), ValidationConstraint.CONSISTENCY);

			InvoiceCreateDetail invoiceRefresh = (InvoiceCreateDetail) cycle.getPage("invoiceCreateDetail");
			invoiceRefresh.setServices(getServices());
			invoiceRefresh.setSubServices(getSubServices());
			invoiceRefresh.setLicenses(getLicenses());
			invoiceRefresh.setInvoices(getInvoices());
			invoiceRefresh.setFields(getFields());

			cycle.activate(invoiceRefresh);
		}else{
			addError(getDelegate(), "errorShadow", getText("invoice.error.fail.generate", new Object[]{"ERROR"}),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}

	public void cancelInvoice(IRequestCycle cycle){
		InvoiceCreate invoiceCreate = (InvoiceCreate)cycle.getPage("invoiceCreate");
		if(getFields()!= null){
			if(getFields().get("SUCCESS") != null){
				getFields().remove("SUCCESS");
			}
//			invoiceCreate.setFields(new HashMap());
			invoiceCreate.setNotFirstLoad(true);
		}
		cycle.activate(invoiceCreate);
	}

}
