package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.Licenses;
import com.balicamp.webapp.action.AdminBasePage;

/**
 * 
 * @author <a href="mailto:prihako.nurukat@sigma.co.id">Prihako Nurukat</a>
 */
public abstract class SimulasiDenda extends AdminBasePage implements PageBeginRenderListener {
	
	private static final String LIST = "LIST_SIMULASI_DENDA";
	
	public abstract void setNilaiBhp(String s);
	public abstract String getNilaiBhp();
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	public abstract Licenses getRow();
	
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
	
	public void hitungDenda(){
		
		String validationMsg 	= generalValidation();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		//konstanta 2%
		final BigDecimal CONST_PERCENT = new BigDecimal("0.02");
		System.out.println("Nilai BHP : " + getNilaiBhp());
		
		//Untuk menyimpan objek yg akan ditampilkan di tabel
		List<Licenses> listDenda = new ArrayList<Licenses>();
		
		Licenses denda = null;
		BigDecimal nilaiBHP = new BigDecimal(getNilaiBhp());
		BigDecimal pokok = new BigDecimal(getNilaiBhp());
		BigDecimal akumDenda = new BigDecimal("0");
		BigDecimal pnbp = new BigDecimal(getNilaiBhp());
		BigDecimal tempDenda = new BigDecimal("1");
		

		
//		for(int i = 1; i <= 24; i++){
//
//			tempDenda = pokok.multiply(CONST_PERCENT).setScale(0, BigDecimal.ROUND_HALF_UP);
//			System.out.println("pokok : " + pokok);
//			akumDenda = akumDenda.add(tempDenda).setScale(0, BigDecimal.ROUND_HALF_UP);
//			pnbp = pnbp.add(tempDenda).setScale(0, BigDecimal.ROUND_HALF_UP);
//			pokok = pnbp; 
//			
//			denda = new Licenses();
//			denda.setYearTo(new BigDecimal(i));
//			denda.setBhpB(nilaiBHP.stripTrailingZeros());
//			denda.setBhpC(tempDenda.stripTrailingZeros());
//			denda.setBhpDelta(akumDenda.stripTrailingZeros());
//			denda.setBhpI(pnbp.stripTrailingZeros());
//			listDenda.add(denda);
//			
//		}
		
		for(int i = 1; i <= 24; i++){

			tempDenda = CONST_PERCENT.multiply(pokok.multiply(new BigDecimal("1").add(CONST_PERCENT).pow(i - 1))).setScale(0, BigDecimal.ROUND_HALF_UP);

			akumDenda = akumDenda.add(tempDenda).setScale(0, BigDecimal.ROUND_HALF_UP);
			
			pnbp = pnbp.add(tempDenda).setScale(0, BigDecimal.ROUND_HALF_UP);
			
			denda = new Licenses();
			denda.setYearTo(new BigDecimal(i));
			denda.setBhpB(nilaiBHP.stripTrailingZeros());
			denda.setBhpC(tempDenda.stripTrailingZeros());
			denda.setBhpDelta(akumDenda.stripTrailingZeros());
			denda.setBhpI(pnbp.stripTrailingZeros());
			listDenda.add(denda);
			
		}
		
		//For saving to audit log
		getUserManager().viewSimulasi(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Denda");
			
		getFields().put(LIST, listDenda);
		
	}
	
	public List getListDenda(){
		List<Licenses> listDenda = (List<Licenses>) getFields().get(LIST);
		
		return listDenda;
	}
	
	private String generalValidation(){
		Pattern beta = Pattern.compile("(^0)|[a-zA-Z]|([%*/-])");
		
		String errorMessage = null;
		
		if(getNilaiBhp() == null){
			errorMessage = getText("calculateBhp.error.empty.nilaiBhp");
		}else if(beta.matcher(getNilaiBhp()).find()){
			errorMessage = getText("calculateBhp.error.numerik.nilaiBhp");
		}
		
		return errorMessage;
	}
	
	public void reset(){
		getFields().put(LIST, null);
		setNilaiBhp(null);
	}
}
