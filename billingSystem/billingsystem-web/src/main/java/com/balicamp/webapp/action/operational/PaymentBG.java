package com.balicamp.webapp.action.operational;

import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.guarantee.BankGuaranteeCreatePayment;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class PaymentBG extends BasePageList implements PageBeginRenderListener {

	@InjectSpring("bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract void setSearchMethod(String searchMethod);
	public abstract String getSearchMethod();

	public abstract void setSearchKeyword(String searchKeyword);
	public abstract String getSearchKeyword();

	public abstract void setBankGuarantee(BankGuarantee bankGuarantee);
	public abstract BankGuarantee getBankGuarantee();

	@Override
	public abstract Object getRow();

	private static final String CLIENT_ID 	= "CLIENT_ID";
	private static final String CLIENT_NAME = "CLIENT_NAME";
	private static final String BHP_METHOD 	= "BHP_METHOD";
	private static final String LICENCE_NO 	= "LICENCE_NO";
	private static final String BG_DOCUMENT	= "BG_DOCUMENT_NO";
	private static final String SEARCH_BY_KEYWORD_PAYMENT_BG 	= "PAYMENT";

	private static final String FLAT 		= "FR";
	private static final String VARIETY 	= "VR";
	private static final String CONVERSION 	= "C";

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void pageBeginRender(PageEvent pageEvent) {
//		super.pageBeginRender(pageEvent);
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
				if(getFields()==null){
					setFields(new HashMap());
				}else if(getFields().get("BACK")!=null){
					getFields().remove("BACK");
				}else if(getFields().get("CONFIRM")!=null){
					getFields().remove("CONFIRM");
				}else{
					getFields().remove("BGP_LIST");
					getFields().remove("BG_CALCULATE");
					getFields().remove("BHP_CALCULATE");
				}
			}
		}
	}

	public IPropertySelectionModel getSearchModel() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put(CLIENT_NAME, "Nama Klien");
		map.put(CLIENT_ID, "ID Klien");
		map.put(BHP_METHOD, "Metode BHP");
		map.put(LICENCE_NO, "No. Aplikasi");
		map.put(BG_DOCUMENT, "No. Dokumen Bank Garansi");
    	return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public IPropertySelectionModel getBhpModel() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put(FLAT, "Flat BHP");
		map.put(VARIETY, "Variety Rate");
//		map.put(CONVERSION, "Conversion");
    	return new PropertySelectionModel(getLocale(), map, true, false);
	}

	public void refresh(IRequestCycle cycle){
		getDelegate().clearErrors();
		if(cycle.isRewinding()){
			
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doSearch(IRequestCycle cycle){
		setNotFirstLoad(true);

		if(getSearchMethod()==null){
			addError(getDelegate(), "errorShadow", getText("operational.bg.error.searchMethod"), ValidationConstraint.CONSISTENCY);
			return;
		}

		if(getSearchMethod().equals(BHP_METHOD) && getSearchKeyword()==null){
			addError(getDelegate(), "errorShadow", getText("operational.bg.error.searchKeyword"), ValidationConstraint.CONSISTENCY);
			return;
		}

		if(getSearchKeyword()==null){
			setSearchKeyword("");
		}

		List bgList = getBankGuaranteeManager().searchByKeyword(getSearchMethod(), getSearchKeyword().toUpperCase(), SEARCH_BY_KEYWORD_PAYMENT_BG);
		getFields().put("BGP_LIST", bgList);
	}

	@SuppressWarnings("rawtypes")
	public List getBgList(){
		List bgList = (List) getFields().get("BGP_LIST");
		return bgList;
	}

	public void bgPayment(IRequestCycle cycle) {
		BankGuarantee bg	= null;

		Object[] params = cycle.getListenerParameters();
		if(params!=null && params.length>0 && params[0]!=null){
			String bgId	= params[0].toString();
			bg			= getBankGuaranteeManager().findByBgId(Long.parseLong(bgId));
		}

		BankGuaranteeCreatePayment bgCreate = (BankGuaranteeCreatePayment) cycle.getPage("bankGuaranteeCreatePayment");
		bgCreate.setBankGuarantee(bg);
		cycle.activate(bgCreate);
	}
}
