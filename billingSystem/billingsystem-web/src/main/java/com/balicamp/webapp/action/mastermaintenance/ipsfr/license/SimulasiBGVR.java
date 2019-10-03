package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tapestry.IRequestCycle;
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
public abstract class SimulasiBGVR extends AdminBasePage implements PageBeginRenderListener {
	private static final String LIST = "SIM_BHP_LIST_BGVR";
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	public abstract Licenses getLicenses();
	public abstract void setLicenses(Licenses licenses);
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	public abstract Licenses getRow();
	
	public abstract void setBhpUpfrontFee(String upfront);
	public abstract String getBhpUpfrontFee();

	public abstract void setBhpPhl(String phl);
	public abstract String getBhpPhl();
	
	public abstract void setBiRate1(String rate);
	public abstract String getBiRate1();
	
	public abstract void setBiRate2(String s);
	public abstract String getBiRate2();
	
	public abstract void setBiRate3(String s);
	public abstract String getBiRate3();
	
	public abstract void setBiRate4(String s);
	public abstract String getBiRate4();
	
	public abstract void setBiRate5(String s);
	public abstract String getBiRate5();
	
	public abstract void setBiRate6(String s);
	public abstract String getBiRate6();
	
	public abstract void setBiRate7(String s);
	public abstract String getBiRate7();
	
	public abstract void setBiRate8(String s);
	public abstract String getBiRate8();
	
	public abstract void setBiRate9(String s);
	public abstract String getBiRate9();
	
	public abstract void setBiRate10(String s);
	public abstract String getBiRate10();
	
	public abstract void setAnnualRate1(String s);
	public abstract String getAnnualRate1();
	
	public abstract void setAnnualRate2(String s);
	public abstract String getAnnualRate2();
	
	public abstract void setAnnualRate3(String s);
	public abstract String getAnnualRate3();
	
	public abstract void setAnnualRate4(String s);
	public abstract String getAnnualRate4();
	
	public abstract void setAnnualRate5(String s);
	public abstract String getAnnualRate5();
	
	public abstract void setAnnualRate6(String s);
	public abstract String getAnnualRate6();
	
	public abstract void setAnnualRate7(String s);
	public abstract String getAnnualRate7();
	
	public abstract void setAnnualRate8(String s);
	public abstract String getAnnualRate8();
	
	public abstract void setAnnualRate9(String s);
	public abstract String getAnnualRate9();
	
	public abstract void setAnnualRate10(String s);
	public abstract String getAnnualRate10();

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

	public void hitungBhp(IRequestCycle cycle){
		
		String validationMsg 	= generalValidation();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String validationMsg2 = validationMore1000();
		if (validationMsg2 != null) {
			addError(getDelegate(), "errorShadow", validationMsg2,
					ValidationConstraint.CONSISTENCY);
			return;
		}
		
		List<Licenses> bhpList 	= new ArrayList<Licenses>();
		
		double biRate1, biRate2, biRate3, biRate4, biRate5, biRate6, biRate7, biRate8, biRate9, biRate10;
		
		double annualPercentage1,annualPercentage2,annualPercentage3,annualPercentage4,annualPercentage5,
			annualPercentage6,annualPercentage7,annualPercentage8,annualPercentage9,annualPercentage10;
		
		BigDecimal bhpUpfrontFee, bhpPhl;
		
		//Untuk mengecek jika karakter yang dimasukkan hanya numerik dan bukan karakter lain seperti %#&*
		try{
			bhpUpfrontFee = new BigDecimal(getBhpUpfrontFee()); 
			bhpPhl = new BigDecimal(getBhpPhl());
		
			biRate1 = Double.valueOf(getBiRate1())/100.0;
			biRate2 = Double.valueOf(getBiRate2())/100.0;
			biRate3 = Double.valueOf(getBiRate3())/100.0;
			biRate4 = Double.valueOf(getBiRate4())/100.0;
			biRate5 = Double.valueOf(getBiRate5())/100.0;
			biRate6 = Double.valueOf(getBiRate6())/100.0;
			biRate7 = Double.valueOf(getBiRate7())/100.0;
			biRate8 = Double.valueOf(getBiRate8())/100.0;
			biRate9 = Double.valueOf(getBiRate9())/100.0;
			biRate10 = Double.valueOf(getBiRate10())/100.0;
		
			annualPercentage1 = Double.valueOf(getAnnualRate1())/100.0;
			annualPercentage2 = Double.valueOf(getAnnualRate2())/100.0;
			annualPercentage3 = Double.valueOf(getAnnualRate3())/100.0;
			annualPercentage4 = Double.valueOf(getAnnualRate4())/100.0;
			annualPercentage5 = Double.valueOf(getAnnualRate5())/100.0;
			annualPercentage6 = Double.valueOf(getAnnualRate6())/100.0;
			annualPercentage7 = Double.valueOf(getAnnualRate7())/100.0;
			annualPercentage8 = Double.valueOf(getAnnualRate8())/100.0;
			annualPercentage9 = Double.valueOf(getAnnualRate9())/100.0;
			annualPercentage10 = Double.valueOf(getAnnualRate10())/100.0;
			
		}catch(Exception e){
			addError(getDelegate(), "errorShadow", "Anda salah memasukkan input", ValidationConstraint.CONSISTENCY);
			return;
		}
		
		BigDecimal constPercent = new BigDecimal("1.02");
		
		Licenses license1 = new Licenses();
		double index1 = biRate1 + 1;
		BigDecimal percent1 = new BigDecimal(annualPercentage2);
		BigDecimal total1 = constPercent.multiply(percent1).multiply(new BigDecimal(index1)).multiply(bhpPhl); 
		
		license1.setBhpRate(new BigDecimal(getBiRate1()));
		license1.setBhpCalcIndex(new BigDecimal(index1).setScale(8,BigDecimal.ROUND_HALF_UP));
		license1.setBhpAnnualPercent(new BigDecimal(getAnnualRate2()));
		license1.setBhpTotal(total1.setScale(2, BigDecimal.ROUND_HALF_UP));
		license1.setYearTo(new BigDecimal(1));
		bhpList.add(license1);
		
		Licenses license2 = new Licenses();
		double index2 = index1 * (biRate2 + 1);
		BigDecimal percent2 = new BigDecimal(annualPercentage3);
		BigDecimal total2 = constPercent.multiply(percent2).multiply(new BigDecimal(index2)).multiply(bhpPhl); 
		
		license2.setBhpRate(new BigDecimal(getBiRate2()));
		license2.setBhpCalcIndex(new BigDecimal(index2).setScale(8,BigDecimal.ROUND_HALF_UP));
		license2.setBhpAnnualPercent(new BigDecimal(getAnnualRate3()));
		license2.setBhpTotal(total2.setScale(2, BigDecimal.ROUND_HALF_UP));
		license2.setYearTo(new BigDecimal(2));
		bhpList.add(license2);
		
		Licenses license3 = new Licenses();
		double index3 = index2 * (biRate3 + 1);
		BigDecimal percent3 = new BigDecimal(annualPercentage4);
		BigDecimal total3 = constPercent.multiply(percent3).multiply(new BigDecimal(index3)).multiply(bhpPhl); 
		
		license3.setBhpRate(new BigDecimal(getBiRate3()));
		license3.setBhpCalcIndex(new BigDecimal(index3).setScale(8,BigDecimal.ROUND_HALF_UP));
		license3.setBhpAnnualPercent(new BigDecimal(getAnnualRate4()));
		license3.setBhpTotal(total3.setScale(2, BigDecimal.ROUND_HALF_UP));
		license3.setYearTo(new BigDecimal(3));
		bhpList.add(license3);
		
		Licenses license4 = new Licenses();
		double index4 = index3 * (biRate4 + 1);
		BigDecimal percent4 = new BigDecimal(annualPercentage5);
		BigDecimal total4 = constPercent.multiply(percent4).multiply(new BigDecimal(index4)).multiply(bhpPhl); 
		
		license4.setBhpRate(new BigDecimal(getBiRate4()));
		license4.setBhpCalcIndex(new BigDecimal(index4).setScale(8,BigDecimal.ROUND_HALF_UP));
		license4.setBhpAnnualPercent(new BigDecimal(getAnnualRate5()));
		license4.setBhpTotal(total4.setScale(2, BigDecimal.ROUND_HALF_UP));
		license4.setYearTo(new BigDecimal(4));
		bhpList.add(license4);
		
		Licenses license5 = new Licenses();
		double index5 = index4 * (biRate5 + 1);
		BigDecimal percent5 = new BigDecimal(annualPercentage6);
		BigDecimal total5 = constPercent.multiply(percent5).multiply(new BigDecimal(index5)).multiply(bhpPhl); 
		
		license5.setBhpRate(new BigDecimal(getBiRate5()));
		license5.setBhpCalcIndex(new BigDecimal(index5).setScale(8,BigDecimal.ROUND_HALF_UP));
		license5.setBhpAnnualPercent(new BigDecimal(getAnnualRate6()));
		license5.setBhpTotal(total5.setScale(2, BigDecimal.ROUND_HALF_UP));
		license5.setYearTo(new BigDecimal(5));
		bhpList.add(license5);
		
		Licenses license6 = new Licenses();
		double index6 = index5 * (biRate6 + 1);
		BigDecimal percent6 = new BigDecimal(annualPercentage7);
		BigDecimal total6 = constPercent.multiply(percent6).multiply(new BigDecimal(index6)).multiply(bhpPhl); 
		
		license6.setBhpRate(new BigDecimal(getBiRate6()));
		license6.setBhpCalcIndex(new BigDecimal(index6).setScale(8,BigDecimal.ROUND_HALF_UP));
		license6.setBhpAnnualPercent(new BigDecimal(getAnnualRate7()));
		license6.setBhpTotal(total6.setScale(2, BigDecimal.ROUND_HALF_UP));
		license6.setYearTo(new BigDecimal(6));
		bhpList.add(license6);
		
		Licenses license7 = new Licenses();
		double index7 = index6 * (biRate7 + 1);
		BigDecimal percent7 = new BigDecimal(annualPercentage8);
		BigDecimal total7 = constPercent.multiply(percent7).multiply(new BigDecimal(index7)).multiply(bhpPhl); 
		
		license7.setBhpRate(new BigDecimal(getBiRate7()));
		license7.setBhpCalcIndex(new BigDecimal(index7).setScale(8,BigDecimal.ROUND_HALF_UP));
		license7.setBhpAnnualPercent(new BigDecimal(getAnnualRate8()));
		license7.setBhpTotal(total7.setScale(2, BigDecimal.ROUND_HALF_UP));
		license7.setYearTo(new BigDecimal(7));
		bhpList.add(license7);
		
		Licenses license8 = new Licenses();
		double index8 = index7 * (biRate8 + 1);
		BigDecimal percent8 = new BigDecimal(annualPercentage9);
		BigDecimal total8 = constPercent.multiply(percent8).multiply(new BigDecimal(index8)).multiply(bhpPhl); 
		
		license8.setBhpRate(new BigDecimal(getBiRate8()));
		license8.setBhpCalcIndex(new BigDecimal(index8).setScale(8,BigDecimal.ROUND_HALF_UP));
		license8.setBhpAnnualPercent(new BigDecimal(getAnnualRate9()));
		license8.setBhpTotal(total8.setScale(2, BigDecimal.ROUND_HALF_UP));
		license8.setYearTo(new BigDecimal(8));
		bhpList.add(license8);
		
		Licenses license9 = new Licenses();
		double index9 = index8 * (biRate9 + 1);
		BigDecimal percent9 = new BigDecimal(annualPercentage10);
		BigDecimal total9 = constPercent.multiply(percent9).multiply(new BigDecimal(index9)).multiply(bhpPhl); 
		
		license9.setBhpRate(new BigDecimal(getBiRate9()));
		license9.setBhpCalcIndex(new BigDecimal(index9).setScale(8,BigDecimal.ROUND_HALF_UP));
		license9.setBhpAnnualPercent(new BigDecimal(getAnnualRate10()));
		license9.setBhpTotal(total9.setScale(2, BigDecimal.ROUND_HALF_UP));
		license9.setYearTo(new BigDecimal(9));
		bhpList.add(license9);
		
		getFields().put(LIST, bhpList);
		
		List<Licenses> bhpList2 = getBhpList();
		
		//For saving to audit log
		getUserManager().viewSimulasi(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "Bank Garansi Variety Rate");
		
		for (Licenses licenses : bhpList2) {
			System.out.println("Bhp total : " + licenses.getBhpTotal());
			System.out.println();
		}
	}
	
	public List getBhpList(){
		List<Licenses> bhpList 	= (List<Licenses>) getFields().get(LIST);
		
		return bhpList;
	}
	
	private String generalValidation() {
		
		//Ini untuk validasi inputan. 
		
		String errorMessage	= null;
		Pattern alfa 		= Pattern.compile("[a-zA-Z]");
		Pattern nullInFront = Pattern.compile("^0");

//		if(getBhpUpfrontFee()==null){
//			errorMessage = getText("calculateBhp.error.empty.upfrontFee");
//		}else if(alfa.matcher(getBhpUpfrontFee()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.upfrontFee");
//		}else if(getBhpPhl()==null){
//			errorMessage = getText("calculateBhp.error.empty.phl");
//		}else if(alfa.matcher(getBhpUpfrontFee()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.upfrontFee");
//		}else if(getBiRate1()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate1()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate1()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate2()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate2()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate2()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate3()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate3()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate3()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate4()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate4()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate4()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate5()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate5()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate5()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate6()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate6()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate6()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate7()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate7()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate7()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate8()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate8()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate8()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate9()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate9()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate9()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getBiRate10()==null){
//			errorMessage = getText("calculateBhp.error.empty.biRate");
//		}else if(alfa.matcher(getBiRate10()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.biRate");
//		}else if(nullInFront.matcher(getBiRate10()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getAnnualRate1()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(alfa.matcher(getAnnualRate1()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate1()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(alfa.matcher(getAnnualRate1()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate1()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(alfa.matcher(getAnnualRate1()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate1()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(getAnnualRate2()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate2()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate2()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate3()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate3()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate3()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate4()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate4()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate4()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate5()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate5()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate5()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate6()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate6()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate6()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate7()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate7()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate7()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate8()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate8()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate8()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate9()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate9()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}else if(alfa.matcher(getAnnualRate9()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(getAnnualRate10()==null){
//			errorMessage = getText("calculateBhp.error.empty.annualRate");
//		}else if(alfa.matcher(getAnnualRate10()).find()){
//			errorMessage = getText("calculateBhp.error.numerik.annualRate");
//		}else if(nullInFront.matcher(getAnnualRate10()).find()){
//			errorMessage = getText("calculateBhp.error.nullInFront");
//		}
		
		if(getBhpUpfrontFee()==null){
			errorMessage = getText("calculateBhp.error.empty.upfrontFee");
		}else if(alfa.matcher(getBhpUpfrontFee()).find()){
			errorMessage = getText("calculateBhp.error.numerik.upfrontFee");
		}else if(getBhpPhl()==null){
			errorMessage = getText("calculateBhp.error.empty.phl");
		}else if(alfa.matcher(getBhpUpfrontFee()).find()){
			errorMessage = getText("calculateBhp.error.numerik.upfrontFee");
		}else if(getBiRate1()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate1()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate1()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate2()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate2()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate2()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate3()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate3()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate3()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate4()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate4()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate4()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate5()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate5()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(getBiRate6()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(Double.valueOf(getBiRate6()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(alfa.matcher(getBiRate6()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(getBiRate7()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate7()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate7()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate8()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate8()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate8()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate9()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate9()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate9()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getBiRate10()==null){
			errorMessage = getText("calculateBhp.error.empty.biRate");
		}else if(alfa.matcher(getBiRate10()).find()){
			errorMessage = getText("calculateBhp.error.numerik.biRate");
		}else if(Double.valueOf(getBiRate10()) > 99){
			errorMessage = getText("calculateBhp.error.numerik.biMoreThan99");
		}else if(getAnnualRate1()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate1()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate1()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate1()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate1()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate1()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate2()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate2()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate3()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate3()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate4()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate4()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate5()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate5()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate6()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate6()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate7()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(getAnnualRate8()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate8()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate9()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate9()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}else if(getAnnualRate10()==null){
			errorMessage = getText("calculateBhp.error.empty.annualRate");
		}else if(alfa.matcher(getAnnualRate10()).find()){
			errorMessage = getText("calculateBhp.error.numerik.annualRate");
		}
		
		return errorMessage;
	}
	
	public void refresh(IRequestCycle cycle) {
		getDelegate().clearErrors();
	}
	
	public String validationMore1000(){
		String errorMessage	= null;
		
		Double total = Double.valueOf(getAnnualRate1()) + Double.valueOf(getAnnualRate2()) + Double.valueOf(getAnnualRate3())
				+ Double.valueOf(getAnnualRate4()) + Double.valueOf(getAnnualRate5()) + Double.valueOf(getAnnualRate6())
				+ Double.valueOf(getAnnualRate7()) + Double.valueOf(getAnnualRate8()) + Double.valueOf(getAnnualRate9())
				+ Double.valueOf(getAnnualRate10());
		
		if(total > 1000){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.moreThan1000");
		}
		else if(total < 1000){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.lessThan1000");
		}else if( (Double.valueOf(getAnnualRate1()) <= 0) || (Double.valueOf(getAnnualRate2()) <= 0) || (Double.valueOf(getAnnualRate3()) <= 0)
				|| (Double.valueOf(getAnnualRate4()) <= 0)|| (Double.valueOf(getAnnualRate5()) <= 0)|| (Double.valueOf(getAnnualRate6()) <= 0)
				|| (Double.valueOf(getAnnualRate7()) <= 0)|| (Double.valueOf(getAnnualRate8()) <= 0)|| (Double.valueOf(getAnnualRate9()) <= 0)
				|| (Double.valueOf(getAnnualRate10()) <= 0)){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentaseTahunanDetail.lessThanZero");
		}else if( (Double.valueOf(getAnnualRate1()) > 999) || (Double.valueOf(getAnnualRate2()) > 999) || (Double.valueOf(getAnnualRate3()) > 999)
				|| (Double.valueOf(getAnnualRate4()) > 999)|| (Double.valueOf(getAnnualRate5()) > 999)|| (Double.valueOf(getAnnualRate6()) > 999)
				|| (Double.valueOf(getAnnualRate7()) > 999)|| (Double.valueOf(getAnnualRate8()) > 999)|| (Double.valueOf(getAnnualRate9()) > 999)
				|| (Double.valueOf(getAnnualRate10()) > 999)){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentaseTahunanDetail.moreThan3Digit");
		}
		
		return errorMessage;
	}
	
	public void reset(){
		getFields().put(LIST, null);
		setBhpUpfrontFee(null);
		setBhpPhl(null);
		
		setBiRate1(null);
		setBiRate2(null);
		setBiRate3(null);
		setBiRate4(null);
		setBiRate5(null);
		setBiRate6(null);
		setBiRate7(null);
		setBiRate8(null);
		setBiRate9(null);
		setBiRate10(null);
		
		setAnnualRate1(null);
		setAnnualRate2(null);
		setAnnualRate3(null);
		setAnnualRate4(null);
		setAnnualRate5(null);
		setAnnualRate6(null);
		setAnnualRate7(null);
		setAnnualRate8(null);
		setAnnualRate9(null);
		setAnnualRate10(null);
	}
}
