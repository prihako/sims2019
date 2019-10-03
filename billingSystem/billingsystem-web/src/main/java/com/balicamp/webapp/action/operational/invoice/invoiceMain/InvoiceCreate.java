package com.balicamp.webapp.action.operational.invoice.invoiceMain;

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
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.operational.InvoiceManagerObsolete;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class InvoiceCreate extends BasePageList implements PageBeginRenderListener {

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

	public abstract void setServiceId(String serviceId);
	public abstract String getServiceId();

	public abstract void setSubServiceId(String subServiceId);
	public abstract String getSubServiceId();

	public abstract void setMetodeBhp(String metodeBhp);
	public abstract String getMetodeBhp();

	public abstract void setClientName(String clientName);
	public abstract String getClientName();

	@Override
	public abstract Object getRow();

	public abstract void setInvoiceNumber(String invoiceNumber);
	public abstract String getInvoiceNumber(); //mandatory

	public abstract void setApRefNumber(String apRefNumber);
	public abstract String getApRefNumber();

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if(!pageEvent.getRequestCycle().isRewinding()){
			setNotFirstLoad(true);
			if(getFields()==null){
				setFields(new HashMap());
				getFields().put("INVOICE_LIST", null);
			}
		}
	}

	public IPropertySelectionModel getServiceSelectionModel() {
    	List<Services> serviceList = getServicesManager().findAllServices();
    	HashMap map = new HashMap();
    	for(int i=0;i<serviceList.size();i++){
    		map.put(serviceList.get(i).getServiceId().toString(), serviceList.get(i).getServiceName());
    	}
    	return new PropertySelectionModel(getLocale(), map, true, false);
    }

	public IPropertySelectionModel getSubServiceSelectionModel() {
		List<SubServices> subServiceList = getSubServicesManager().findAllSubServices();
    	HashMap map = new HashMap();
    	for(int i=0;i<subServiceList.size();i++){
    		map.put(subServiceList.get(i).getSubserviceId().toString(), subServiceList.get(i).getSubserviceName());
    	}
    	return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getMetodeBhpSelectionModel() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("FR", "Flat Rate");
		map.put("VR", "Variated Rate");
		map.put("C", "Conversion");
    	return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public void search(IRequestCycle cycle){

		List invoiceList = null;

		if(getServiceId()==null || getServiceId().isEmpty()){
			addError(getDelegate(), "errorShadow", getText("license.error.service.empty"), ValidationConstraint.CONSISTENCY);
			return;
		}

		if(getSubServiceId()==null || getSubServiceId().isEmpty()){
			addError(getDelegate(), "errorShadow", getText("license.error.subService.empty"), ValidationConstraint.CONSISTENCY);
			return;
		}

		if(getMetodeBhp()==null || getMetodeBhp().isEmpty()){
			addError(getDelegate(), "errorShadow", getText("license.error.bhpMethod.empty"), ValidationConstraint.CONSISTENCY);
			return;
		}

		setNotFirstLoad(true);
		invoiceList = getInvoiceManager().findGeneratedInvoice(getServiceId(),
				getSubServiceId(), getMetodeBhp(), getClientName(), getApRefNumber(), getInvoiceNumber());
		getFields().put("INVOICE_LIST", invoiceList);
	}

	public List getInvoiceList(){
		List invoiceList = (List) getFields().get("INVOICE_LIST");
		return invoiceList;
	}

	public void create(IRequestCycle cycle) {

		Invoices invoices		= new Invoices();
		Licenses licenses		= new Licenses();
		Services services		= new Services();
		SubServices subServices	= new SubServices();
		InvoiceCreateDetail invoiceDetail = (InvoiceCreateDetail)cycle.getPage("invoiceCreateDetail");

		invoiceDetail.setServices(services);
		invoiceDetail.setSubServices(subServices);
		invoiceDetail.setLicenses(licenses);
		invoiceDetail.setInvoices(invoices);

		if(getFields()!=null){
			invoiceDetail.setFields(getFields());
		}

		cycle.activate(invoiceDetail);
	}
}
