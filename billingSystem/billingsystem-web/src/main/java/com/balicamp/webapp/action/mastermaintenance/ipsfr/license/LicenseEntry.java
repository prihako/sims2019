package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.acegisecurity.concurrent.SessionRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.LicenseDisplay;
import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.mastermaintenance.service.Services;
import com.balicamp.model.mastermaintenance.service.SubServices;
import com.balicamp.service.mastermaintenance.license.LicenseManager;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

// Referenced classes of package com.balicamp.webapp.action.mastermaintenance.ipsfr.license:
//            LicenseConfirm

public abstract class LicenseEntry extends BasePageList implements
		PageBeginRenderListener, PageEndRenderListener {
	
	private IPropertySelectionModel subServiceModel = null;

	public LicenseEntry() {

		methodBHPModel = null;
	}
	
	public void setSubServicesModel(String serviceName){
		HashMap<String, String> map = new HashMap<String, String>();
		Services service = getServicesManager().findServicesByName(serviceName);
		List<SubServices> subServiceList = getSubServicesManager().findSubServicesByServiceId(service.getServiceId());
		
		for (int i = 0; i < subServiceList.size(); i++) {
			map.put(subServiceList.get(i).getSubserviceName(), subServiceList
					.get(i).getSubserviceName());
		}
		
		subServiceModel = new PropertySelectionModel(getLocale(), map, true, false);;
		
	}
	
	@EventListener(targets = "service", events = "onchange", submitForm = "myForm")
	public void watchText(IRequestCycle cycle)
	{
		
		if(getService() != null){
			setSubServicesModel(getService());
			cycle.getResponseBuilder().updateComponent("myTarget");
		}else{
			//Ini buat set subServiceModel jadi pas tak dipilih, akan tampil semua
			subServiceModel = null;
			cycle.getResponseBuilder().updateComponent("myTarget");
		}
	}

	protected final Log log = LogFactory.getLog(LicenseEntry.class);

	private BeanPropertySelectionModel methodBHPModel = null;

	public abstract BigDecimal getServiceId();

	public abstract void setServiceId(BigDecimal serviceId);

	public abstract String getService();

	public abstract void setService(String service);

	public abstract BigDecimal getSubServiceId();

	public abstract void setSubServiceId(BigDecimal subServiceId);

	public abstract String getSubServices();

	public abstract void setSubServices(String subServices);

	public abstract String getBhpMethod();

	public abstract void setBhpMethod(String bhpMethod);

	public abstract BigDecimal getClientID();

	public abstract void setClientID(BigDecimal clientID);

	public abstract String getClientName();

	public abstract void setClientName(String clientName);

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

	public abstract void setNotFirstLoad(boolean firstLoadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract Licenses getLicensesRow();


	@InjectPage("licenseCreate")
	public abstract LicenseCreate getLicenseCreate();

	@InjectObject("spring:servicesManager")
	public abstract ServicesManager getServicesManager();

	@InjectObject("spring:subServicesManager")
	public abstract SubServicesManager getSubServicesManager();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectPage("licenseView")
	public abstract LicenseView getLicenseView();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	@Persist("client")
	public abstract String getLicenceId();

	public abstract void setLicenseId(String licenceId);

	public IPropertySelectionModel getServiceModel() {

		HashMap<String, String> map = new HashMap<String, String>();
		List<Services> serviceList = getServicesManager().findAllServices();
		for (int i = 0; i < serviceList.size(); i++) {
			map.put(serviceList.get(i).getServiceName(), serviceList.get(i)
					.getServiceName());
		}
		return new PropertySelectionModel(getLocale(), map, true, false);

	}

	public IPropertySelectionModel getSubServicesModel() {
		if(subServiceModel != null){
			return subServiceModel;
		}else{
			HashMap<String, String> map = new HashMap<String, String>();
			List<SubServices> subServiceList = getSubServicesManager()
					.findAllSubServices();
			for (int i = 0; i < subServiceList.size(); i++) {
				map.put(subServiceList.get(i).getSubserviceName(), subServiceList
						.get(i).getSubserviceName());
			}
			return new PropertySelectionModel(getLocale(), map, true, false);
		}

	}

	public IPropertySelectionModel getMethodBHPModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FR", "Flat Rate");
		map.put("VR", "Variety Rate");
		map.put("C", "Conversion");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if(getFields()==null){
			setFields(new HashMap());
			saveObjectToSession(null, "LICENSE_LIST");
		}else if(getFields().get("VIEW")==null && getFields().get("SEARCH")==null){
			setFields(new HashMap());
			saveObjectToSession(null, "LICENSE_LIST");
		}
	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
	}


	public abstract void setUniqueSet(Set<LicenseDisplay> set);

	public abstract Set<LicenseDisplay> getUniqueSet();

	@Override
	public abstract Object getLoopObject();

	public List getLicenseList() {
		List<Licenses> licenseList = (List<Licenses>) getObjectfromSession("LICENSE_LIST");
		return licenseList;
	}

	public IPage onSearch() {

		setRealRender(true);
		if (getTableModel().getRowCount() >= 0) {

		}

		return null;
	}

	public void doSearch() {
		String validationMsg = generalValidation();
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		if(getService()!=null){
			Services service = getServicesManager().findServicesByName(getService());
			if (getServiceId() == null) {
				setServiceId(service.getServiceId());
			}
		}

		if(getSubServices()!=null){
			SubServices subServices = getSubServicesManager().findSubServicesByName(getSubServices());
			if (getSubServiceId() == null) {
				setSubServiceId(subServices.getSubserviceId());
			}
		}

		if (getBhpMethod() == null) {
			setBhpMethod("");
		}
		if (getClientName() == null) {
			setClientName("");
		}
		if (getLicenceNo() == null) {
			setLicenceNo("");
		}

		System.out.println("Service Id = " + getServiceId() + " Service = "
				+ getService() + ", SubServices Id = "+getSubServiceId()+" SubServices = " + getSubServices()
				+ ", BHP Method= " + getBhpMethod() + ", Client Name = "
				+ getClientName() + ", LicenceNo = " + getLicenceNo());

		saveObjectToSession(
				getLicenseManager().
				searchLicense(getServiceId(), getSubServiceId(), getBhpMethod(), getClientName(),getLicenceNo().trim()), "LICENSE_LIST");

		if(getObjectfromSession("LICENSE_LIST")!=null){
			List<Licenses> licenseList 	= (List<Licenses>) getObjectfromSession("LICENSE_LIST");
			if(getLicenceNo()!=null && !getLicenceNo().equals("") && licenseList.size()>0){
				Licenses license = (Licenses) getLicenseList().get(0);
				setService(getServicesManager().findServicesById(license.getServiceId().longValue()).getServiceName());
				setSubServices(getSubServicesManager().findSubServicesById(license.getSubServiceId().longValue()).getSubserviceName());
				setBhpMethod(license.getBhpMethod());
				setClientName(license.getClientName());
			}
		}

		getFields().put("SEARCH", true);

		if(getFields()!=null && getFields().get("VIEW")!=null){
			getFields().remove("VIEW");
		}
	}

	private String generalValidation() {
		String errorMessage = null;
		if ( getLicenceNo() == null){
			if (getService() == null ) {
				errorMessage = getText("license.error.service.empty");
			}else if ( getSubServices() == null){
				errorMessage = getText("license.error.subservice.empty");
			}else if (  getBhpMethod() == null){
				errorMessage = getText("license.error.bhpmethod.empty");
			}else if ( getClientName() == null){
				errorMessage = getText("license.error.client.empty");
			}
		}else{
			errorMessage=null;
		}
		return errorMessage;
	}

	public IPage doEdit(IRequestCycle cycle, Long bsLicenceId, String licenceId) {

		Licenses license 		= getLicenseManager().findByLicenseId(bsLicenceId, licenceId);
		Services service 		= getServicesManager().findServicesById(license.getServiceId().longValue());
		SubServices subServices = getSubServicesManager().findSubServicesById(license.getSubServiceId().longValue());
		LicenseView licenseView = (LicenseView) cycle.getPage("licenseView");

		if(getFields()!=null && getFields().get("SEARCH")!=null){
			getFields().remove("SEARCH");
		}

		licenseView.setFields(getFields());
		licenseView.setLicenses(license);
		licenseView.setServices(service);
		licenseView.setSubServices(subServices);

		return licenseView;
	}

	public IPage onCreate() {

		serverValidate();

		if (getDelegate().getHasErrors()){
			return null;
		}

		LicenseCreate licenseCreate = getLicenseCreate();
		return licenseCreate;
	}

	public void serverValidate() {

	}

}