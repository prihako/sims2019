package com.balicamp.webapp.action.operational;

import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class ApplicationBandwidthSearch extends AdminBasePage
		implements PageBeginRenderListener, PageEndRenderListener {
	
	public abstract String getCriteria();
	public abstract void setCriteria(String criteria);
	
	public abstract String getBhpMethod();
	public abstract void setBhpMethod(String bhpMethod);
	
	public abstract String getClientName();
	public abstract void setClientName(String clientName);
	
	public abstract String getClientNo();
	public abstract void setClientNo(String clientNo);
	
	public abstract String getLicenceNumber();
	public abstract void setLicenceNumber(String licenceNumber);
	
	public abstract ApplicationBandwidth getApplication();
	
	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();
	

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
//		if(getFields()==null){
//			setFields(new HashMap());
//			saveObjectToSession(null, "APPLICATION_LIST");
//		}
		
		if (!pageEvent.getRequestCycle().isRewinding()) {
	
				if (getFields() == null) {
					setFields(new HashMap());
				}
			
		}
		
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}
	
	public List getApplicationList() {
		List<ApplicationBandwidth> list = (List<ApplicationBandwidth>) getFields()
				.get("APPLICATION_LIST");
		return list;
	}
	
	
	public IPropertySelectionModel getCriteriaModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("clientName", "Nama Klien");
		map.put("clientNo", "Klien Id");
		map.put("bhpMethod", "Metode BHP");
		map.put("noApp", "No. Aplikasi");

		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public IPropertySelectionModel getMethodBHPModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FR", "Flat BHP");
		map.put("VR", "Variety Rate");
		map.put("C", "Conversion");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public void doSearch(){
		List<ApplicationBandwidth> list;
		
		if (getClientName() != null){
			 list = getApplicationBandwidthManager().findByClientName(getClientName());
			getFields().put("APPLICATION_LIST", list);

			
		}else if(getClientNo() != null){
			
			System.out.println("Client NO = "+getClientNo());
			 list = getApplicationBandwidthManager().findByClientNo(getClientNo());
				getFields().put("APPLICATION_LIST", list);

			
		}else if (getLicenceNumber() != null){
			 list = getApplicationBandwidthManager().findByLicenceNumber(getLicenceNumber());
			getFields().put("APPLICATION_LIST", list);

			
		}else if (getBhpMethod() != null){
			 list = getApplicationBandwidthManager().findByMethod(getBhpMethod());
			getFields().put("APPLICATION_LIST", list);

			
		}
		
	}

}
