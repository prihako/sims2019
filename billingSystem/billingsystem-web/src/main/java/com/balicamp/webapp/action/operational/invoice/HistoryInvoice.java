package com.balicamp.webapp.action.operational.invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

/**
 * @author Prihako Nurukat<a href="mailto:prihako.nurukat@sigma.co.id">Prihako Nurukat</a>
 *  
 */
public abstract class HistoryInvoice extends AdminBasePage implements PageBeginRenderListener {
	private static final String LIST = "LICENSE_LIST_HISTORY_INVOICE";
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if(getFields()==null){
			setFields(new HashMap());
		}
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
				getFields().put(LIST, null);
			}
		}
	}
	
	public abstract Invoice getRow();
	
	public abstract String getSearchCriteria();
	
	public abstract String getCriteria();
	
	public abstract String getBhpMethod();
	
	public abstract void setBhpMethod(String bhpMethod);
	
	public IPropertySelectionModel getSearchCriteriaModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ClientName", "Nama Klien");
		map.put("ClientId", "ID Klien");
		map.put("BHPMethod", "Metode BHP");
		map.put("ApplicationNumber", "Nomor Aplikasi");
		map.put("InvoiceNumber", "Nomor Invoice");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public IPropertySelectionModel getMethodBHPModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FR", "Flat BHP");
		map.put("VR", "Variety Rate");
//		map.put("C", "Conversion");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public void doSearch(){
		
		String validationMsg 	= generalValidation();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		List<Invoice> invoiceList = new ArrayList<Invoice>();
		List<License> licenseList = null;
		
		if(getSearchCriteria().equalsIgnoreCase("ApplicationNumber")){
			
			String criteria = getCriteria().replaceAll("\\s+","");
			
			licenseList = getLicenseManager().findByLicenceNo(criteria);
			License license = null;
			if(licenseList.size() > 0){
				for (Iterator iterator = licenseList.iterator(); iterator.hasNext();) {
					license = (License) iterator.next();
					 
					if(license.getTInvoices().size() > 0){
//						invoiceList.addAll(license.getTInvoices());
						
//						test
						List<Invoice> invoiceTempList = license.getTInvoices();
						for(int i = 0; i < invoiceTempList.size(); i ++){
							Invoice invTemp = invoiceTempList.get(i);
							if( (invTemp.getInvoiceStatus().equalsIgnoreCase("C")) || (invTemp.getInvoiceStatus().equalsIgnoreCase("P")) || 
									 (invTemp.getInvoiceStatus().equalsIgnoreCase("BD")) ){
								invoiceList.add(invTemp);
							}
						}
						
					}
					
				}
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	
			        	int month1, month2;
			        	
			        	if(p1.getMonthTo() == null ){
			        		month1 = 0;
			        	}else{
			        		month1 = p1.getMonthTo().intValue();
			        	}
			        	
			        	if(p2.getMonthTo() == null ){
			        		month2 = 0;
			        	}else{
			        		month2 = p2.getMonthTo().intValue();
			        	}
			        	
			        	
			        	return (month1) - (month2);
			        }
			    });
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	return (p1.getYearTo().intValue()) - (p2.getYearTo().intValue());
			        }
			    });
				
			}
			
		}else if(getSearchCriteria().equalsIgnoreCase("InvoiceNumber")){
			String criteria = getCriteria().replaceAll("\\s+","");
			
			invoiceList = getInvoiceManager().findByInvoiceNo(criteria);
			
		}else if(getSearchCriteria().equalsIgnoreCase("ClientName")){

			licenseList = getLicenseManager().findByClientName(getCriteria());
			License license = null;
			if(licenseList.size() > 0){
				for (Iterator iterator = licenseList.iterator(); iterator.hasNext();) {
					license = (License) iterator.next();
					
					if(license.getTInvoices().size() > 0){
//						invoiceList.addAll(license.getTInvoices());
						
//						test
						List<Invoice> invoiceTempList = license.getTInvoices();
						for(int i = 0; i < invoiceTempList.size(); i ++){
							Invoice invTemp = invoiceTempList.get(i);
							if( (invTemp.getInvoiceStatus().equalsIgnoreCase("C")) || (invTemp.getInvoiceStatus().equalsIgnoreCase("P")) || 
									 (invTemp.getInvoiceStatus().equalsIgnoreCase("BD")) ){
								invoiceList.add(invTemp);
							}
						}
					}
					
				}
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	
			        	int month1, month2;
			        	
			        	if(p1.getMonthTo() == null ){
			        		month1 = 0;
			        	}else{
			        		month1 = p1.getMonthTo().intValue();
			        	}
			        	
			        	if(p2.getMonthTo() == null ){
			        		month2 = 0;
			        	}else{
			        		month2 = p2.getMonthTo().intValue();
			        	}
			        	
			        	
			        	return (month1) - (month2);
			        }
			    });
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	return (p1.getYearTo().intValue()) - (p2.getYearTo().intValue());
			        }
			    });
				
//				Collections.sort(invoiceList, new Comparator<Invoice>() {
//			        @Override public int compare(Invoice p1, Invoice p2) {
//			        	if( (p1.getMonthTo() != null) && (p2.getMonthTo() != null) ){
//			        		return (p1.getMonthTo().intValue()) - (p2.getMonthTo().intValue());
//			        	}else{
//			        		return (p1.getYearTo().intValue()) - (p2.getYearTo().intValue());
//			        	}
//			        }
//			    });
			}
			
		}else if(getSearchCriteria().equalsIgnoreCase("BHPMethod")){
			
			licenseList = getLicenseManager().findByMethod(getBhpMethod());
			License license = null;
			if(licenseList.size() > 0){
				for (Iterator iterator = licenseList.iterator(); iterator.hasNext();) {
					license = (License) iterator.next();
					
					if(license.getTInvoices().size() > 0){
//						invoiceList.addAll(license.getTInvoices());
						
//						test
						List<Invoice> invoiceTempList = license.getTInvoices();
						for(int i = 0; i < invoiceTempList.size(); i ++){
							Invoice invTemp = invoiceTempList.get(i);
							if( (invTemp.getInvoiceStatus().equalsIgnoreCase("C")) || (invTemp.getInvoiceStatus().equalsIgnoreCase("P")) || 
									 (invTemp.getInvoiceStatus().equalsIgnoreCase("BD")) ){
								invoiceList.add(invTemp);
							}
						}
						
					}
					
				}
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	
			        	int month1, month2;
			        	
			        	if(p1.getMonthTo() == null ){
			        		month1 = 0;
			        	}else{
			        		month1 = p1.getMonthTo().intValue();
			        	}
			        	
			        	if(p2.getMonthTo() == null ){
			        		month2 = 0;
			        	}else{
			        		month2 = p2.getMonthTo().intValue();
			        	}
			        	
			        	
			        	return (month1) - (month2);
			        }
			    });
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	return (p1.getYearTo().intValue()) - (p2.getYearTo().intValue());
			        }
			    });
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	return( p1.getTLicence().getClientName().compareToIgnoreCase(p2.getTLicence().getClientName()) );
			        }
			    });
				
			}
			
		}else if(getSearchCriteria().equalsIgnoreCase("ClientId")){
			
			String criteria = getCriteria().replaceAll("\\s+","");
			
			licenseList = getLicenseManager().findByClientNo(criteria);
			License license = null;
			if(licenseList.size() > 0){
				for (Iterator iterator = licenseList.iterator(); iterator.hasNext();) {
					license = (License) iterator.next();
					
					if(license.getTInvoices().size() > 0){
//						invoiceList.addAll(license.getTInvoices());
						
//						test
						List<Invoice> invoiceTempList = license.getTInvoices();
						for(int i = 0; i < invoiceTempList.size(); i ++){
							Invoice invTemp = invoiceTempList.get(i);
							if( (invTemp.getInvoiceStatus().equalsIgnoreCase("C")) || (invTemp.getInvoiceStatus().equalsIgnoreCase("P")) || 
									 (invTemp.getInvoiceStatus().equalsIgnoreCase("BD")) ){
								invoiceList.add(invTemp);
							}
						}
						
					}
					
				}
				
//				Collections.sort(invoiceList, new Comparator<Invoice>() {
//			        @Override public int compare(Invoice p1, Invoice p2) {
//			        	return ( (p1.getYearTo()).compareTo(p2.getYearTo()) );
//			        }
//			    });
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	
			        	int month1, month2;
			        	
			        	if(p1.getMonthTo() == null ){
			        		month1 = 0;
			        	}else{
			        		month1 = p1.getMonthTo().intValue();
			        	}
			        	
			        	if(p2.getMonthTo() == null ){
			        		month2 = 0;
			        	}else{
			        		month2 = p2.getMonthTo().intValue();
			        	}
			        	
			        	
			        	return (month1) - (month2);
			        }
			    });
				
//				Collections.sort(invoiceList, new Comparator<Invoice>() {
//			        @Override public int compare(Invoice p1, Invoice p2) {
//			        	if( (p1.getMonthTo() != null) && (p2.getMonthTo() != null) ){
//			        		return (p1.getMonthTo().intValue()) - (p2.getMonthTo().intValue());
//			        	}else{
//			        		return (p1.getYearTo().intValue()) - (p2.getYearTo().intValue());
//			        	}
//			        }
//			    });
				
				Collections.sort(invoiceList, new Comparator<Invoice>() {
			        @Override public int compare(Invoice p1, Invoice p2) {
			        	return (p1.getYearTo().intValue()) - (p2.getYearTo().intValue());
			        }
			    });
				
			}
			
		}
		
		
		getFields().put(LIST, invoiceList);
		
		getUserManager().viewHistoryInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost());
	}
	
	public List getInvoiceList(){
		List<Licenses> licenseList 	= (List<Licenses>) getFields().get(LIST);
		
		return licenseList;
	}
	
	private String generalValidation(){
		Pattern alfa 		= Pattern.compile("[a-zA-Z]");
		
		String errorMessage = null;
		
		if(getSearchCriteria() == null){
			errorMessage = getText("invoice.historyInvoice.searchCriteria.empty");
		}else if(getCriteria() == null){
			if(getBhpMethod() == null){
				errorMessage = getText("invoice.historyInvoice.criteria.empty");
			}
		}else if(getSearchCriteria().equalsIgnoreCase("ClientId")){
			if(alfa.matcher(getCriteria()).find()){
				errorMessage = getText("invoice.historyInvoice.criteria.clientId.alfa");
			}
		}
		
		return errorMessage;
		
	}
		
}
