package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;

/**
 * @author <a href="mailto:hendy.yusprasetya@sigma.co.id">Yusprasetya</a>
 * Modified <a href="mailto:prihako.nurukat@sigma.co.id">Prihako Nurukat</a>
 */
public abstract class SimulasiBGFR extends AdminBasePage implements PageBeginRenderListener {
	private static final String LIST = "SIM_BHP_LIST_BGFR";
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract Licenses getRow();
	
	public abstract Licenses getLicenses();
	public abstract void setLicenses(Licenses licenses);
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	public abstract String getBhpUpfrontFee();
	
	public abstract void setBhpUpfrontFee(String upfrontFee);
	
	public abstract String getBhpPhl();
	
	public abstract void setBhpPhl(String bhp);
	
	public abstract String getHargaLelang();
	
	public abstract void setHargaLelang(String harga);


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
	
	public void hitungBhp(){
		
		String validationMsg 	= generalValidation();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		//Untuk menyimpan objek yg akan ditampilkan di tabel
		List<Licenses> bhpList 	= new ArrayList<Licenses>();
		
		BigDecimal constPercent = new BigDecimal("1.02");
		
		BigDecimal total = null;
		
		if(getHargaLelang() != null){
			total = new BigDecimal(getHargaLelang()).multiply(constPercent);
		}else{
			total = new BigDecimal(getBhpPhl()).multiply(constPercent);
		}
		
		Licenses license1 = new Licenses();
		license1.setYearTo(new BigDecimal(1));
		license1.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license1);
		
		Licenses license2 = new Licenses();
		license2.setYearTo(new BigDecimal(2));
		license2.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license2);
		
		Licenses license3 = new Licenses();
		license3.setYearTo(new BigDecimal(3));
		license3.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license3);
		
		Licenses license4 = new Licenses();
		license4.setYearTo(new BigDecimal(4));
		license4.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license4);
		
		Licenses license5 = new Licenses();
		license5.setYearTo(new BigDecimal(5));
		license5.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license5);
		
		Licenses license6 = new Licenses();
		license6.setYearTo(new BigDecimal(6));
		license6.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license6);
		
		Licenses license7 = new Licenses();
		license7.setYearTo(new BigDecimal(7));
		license7.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license7);
		
		Licenses license8 = new Licenses();
		license8.setYearTo(new BigDecimal(8));
		license8.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license8);
		
		Licenses license9= new Licenses();
		license9.setYearTo(new BigDecimal(9));
		license9.setBhpTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP));
		bhpList.add(license9);
		
		getFields().put(LIST, bhpList);
		
		//For saving to audit log
		getUserManager().viewSimulasi(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Bank Garansi Flat Rate");
	}
	
	public List getBhpList(){
		List<Licenses> bhpList 	= (List<Licenses>) getFields().get(LIST);
		
		return bhpList;
	}
	
	private String generalValidation(){
		Pattern beta = Pattern.compile("(^0)|[a-zA-Z]|([%*/-])");
		
		String errorMessage = null;
		
		if(getUpfrontFee() != null){
			if(beta.matcher(getBhpUpfrontFee()).find()){
				errorMessage = getText("calculateBhp.error.numerik.upfrontFee");
			}
		}else if(getBhpPhl()==null && getHargaLelang() == null){
			errorMessage = getText("calculateBhp.error.empty.phl.both");
		}else if(getBhpPhl() == null && getHargaLelang() != null){
			if(beta.matcher(getHargaLelang()).find()){
				errorMessage = getText("calculateBhp.error.numerik.hargaLelang");
			}
		}else if(getBhpPhl() != null && getHargaLelang() == null){
			if(beta.matcher(getBhpPhl()).find()){
				errorMessage = getText("calculateBhp.error.numerik.phl");
			}
		}
		
		return errorMessage;
	}
	
	private Object getUpfrontFee() {
		// TODO Auto-generated method stub
		return null;
	}

	public void reset(){
		getFields().put(LIST, null);
		setBhpUpfrontFee(null);
		setBhpPhl(null);
		setHargaLelang(null);
	}
		
}
