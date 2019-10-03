package com.balicamp.webapp.action.operational;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class ManageInvoiceView extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {
	
	public abstract Long getLicenceID();
	public abstract void setLicenceID(Long licenceID);
	
	public abstract Long getInvoiceID();
	public abstract void setInvoiceID(Long invoiceID);
	
	
	public abstract String getKmNo();

	public abstract void setKmNo(String s);

	public abstract String getLicenceNumber();

	public abstract void setLicenceNumber(String s);

	public abstract String getService();

	public abstract void setService(String s);

	public abstract String getSubService();

	public abstract void setSubService(String s);

	public abstract BigDecimal getServiceId();

	public abstract void setServiceId(BigDecimal bigdecimal);

	public abstract BigDecimal getSubServiceId();

	public abstract void setSubServiceId(BigDecimal bigdecimal);

	public abstract String getBhpMethod();

	public abstract void setBhpMethod(String s);

	public abstract String getClientName();

	public abstract void setClientName(String s);

	public abstract String getClientID();

	public abstract void setClientID(String clientID);

	public abstract String getZoneName();

	public abstract void setZoneName(String s);

	public abstract BigDecimal getZoneNo();

	public abstract void setZoneNo(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqMax();

	public abstract void setFreqMax(BigDecimal bigdecimal);

	public abstract BigDecimal getFreqMin();

	public abstract void setFreqMin(BigDecimal bigdecimal);

	public abstract String getKmDate();

	public abstract void setKmDate(String s);

	public abstract String getCurrentBeginDate();

	public abstract void setCurrentBeginDate(String s);

	public abstract String getCurrentEndDate();

	public abstract void setCurrentEndDate(String s);

	public abstract String getLicenceBeginDate();

	public abstract void setLicenceBeginDate(String s);

	public abstract String getLicenceEndDate();

	public abstract void setLicenceEndDate(String s);

	public abstract BigDecimal getYearTo();

	public abstract void setYearTo(BigDecimal bigdecimal);

	public abstract String getPaymentDueDate();

	public abstract void setPaymentDueDate(String s);

	public abstract String getPaymentType();

	public abstract void setPaymentType(String s);

	public abstract String getBgAvailableStatus();

	public abstract void setBgAvailableStatus(String s);

	public abstract String getBgDueDate();

	public abstract void setBgDueDate(String date);

	public abstract BigDecimal getRateValue();

	public abstract void setRateValue(BigDecimal bigdecimal);

	public abstract Long getPercentYear();

	public abstract void setPercentYear(Long long1);

	public abstract BigDecimal getBhpCalcIndex();

	public abstract void setBhpCalcIndex(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpUpfrontFee();

	public abstract void setBhpUpfrontFee(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpHl();

	public abstract void setBhpHl(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpPhl();

	public abstract void setBhpPhl(BigDecimal bigdecimal);
	
	public abstract BigDecimal getBhpAnnual();

	public abstract void setBhpAnnual(BigDecimal bigdecimal);

	public abstract BigDecimal getBgTotal();

	public abstract void setBgTotal(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpTotal();

	public abstract void setBhpTotal(BigDecimal bigdecimal);
	
	public abstract String getInvoiceNo();
	public abstract void setInvoiceNo(String invoiceNo);
	

	public abstract String getInvoiceStatus();
	public abstract void setInvoiceStatus(String invoiceStatus);

	public abstract String getInvoiceType();
	public abstract void setInvoiceType(String invoiceType);
	
	public abstract String getInvoiceCreateDate();
	public abstract void setInvoiceCreateDate(String invoiceCreateDate);

	public abstract BigDecimal getMonthTo();
	public abstract void setMonthTo(BigDecimal monthTo);

	public abstract  Date getPaymentDate();
	public abstract void setPaymentDate(Date paymentDate);
	
	public abstract Object getInvoice();

	public abstract List<Invoice> getInvoiceList();

	public abstract void setInvoiceList(List<Invoice> invoiceList);

	public abstract License getLicense();
	
	public abstract Object getData();
	
	@InjectObject("spring:bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();

	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();

	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();

	public abstract InitialInvoiceSearch getApplicationBandwidthSearch();

	@InjectObject("engine-service:page")
	public abstract IEngineService getDownloadService();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	public ManageInvoiceView(){
		
	}
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		List<Invoice> invoiceList = (List<Invoice>) getInvoiceList();
		
		getFields().put(invoiceList, "INVOICE_LIST");
		
	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
	}
	
	public List<Invoice> getBhpList() {
		List bhpList = (List) getFields().get("INVOICE_LIST");
		return bhpList;
	}
	
	public IPage doEdit(IRequestCycle cycle){
		ManageInvoiceEdit edit = (ManageInvoiceEdit) cycle.getPage("manageInvoiceEdit");
		edit.setLicenceID(getLicenceID());
		edit.setInvoiceID(getInvoiceID());
		edit.setLicenceNumber(getLicenceNumber());
		edit.setClientName(getClientName());
		edit.setClientID(getClientID());
		edit.setBhpMethod(getBhpMethod());
		edit.setKmNo(getKmNo());
		edit.setKmDate(getKmDate().toString());
		edit.setZoneNo(getZoneNo());
		edit.setZoneName(getZoneName());
		edit.setLicenceBeginDate(getLicenceBeginDate());
		edit.setLicenceEndDate(getLicenceEndDate());
		edit.setFreqMin(getFreqMin());
		edit.setFreqMax(getFreqMax());
		edit.setPaymentType(getPaymentType());
		edit.setPaymentDueDate(getPaymentDueDate());
		edit.setCurrentBeginDate(getCurrentBeginDate());
		edit.setCurrentEndDate(getCurrentEndDate());
		edit.setYearTo(getYearTo());
		edit.setBgAvailableStatus(getBgAvailableStatus());
		edit.setBgDueDate(getBgDueDate());
		edit.setBhpUpfrontFee(getBhpUpfrontFee());
		edit.setBhpHl(getBhpHl());
		edit.setBhpAnnual(getBhpAnnual());
		edit.setBgTotal(getBgTotal());
		edit.setBhpTotal(getBhpTotal());
		edit.setInvoiceNo(getInvoiceNo());
		edit.setInvoiceStatus(getInvoiceStatus());
		edit.setInvoiceType(getInvoiceType());
		//edit.setInvoiceCreateDate(getInvoiceCreateDate());
		edit.setPaymentDate(getPaymentDate());
		
		return edit;

	}
	
	public void doBack(){
		
	}
	
	public void doPrint(){
		
	}
}
