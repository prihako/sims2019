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
 * 
 * <a href="mailto:prihako.nurukat@sigma.co.id">Prihako Nurukat</a>
 */
public abstract class SimulasiBHP extends AdminBasePage implements PageBeginRenderListener {
	private static final String LIST = "SIM_BHP_LIST";

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
		
		Licenses license1 = new Licenses();
		BigDecimal percent1 = new BigDecimal(annualPercentage1);
		BigDecimal annualIpsfr1 = bhpPhl.multiply(percent1).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total1 = bhpUpfrontFee.add(annualIpsfr1).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		System.out.println("Ipsfr1 " + annualIpsfr1);
		System.out.println("total1 " + total1);
		System.out.println();
		
		license1.setBhpUpfrontFee(new BigDecimal(getBhpUpfrontFee()));
		license1.setBhpRate(new BigDecimal(getBiRate1()));
		license1.setBhpCalcIndex(new BigDecimal(0).setScale(2,
				BigDecimal.ROUND_HALF_UP));
		license1.setBhpAnnualPercent(new BigDecimal(getAnnualRate1()));
		license1.setBhpAnnualValue(annualIpsfr1);
		license1.setBhpTotal(total1.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license1.setBhpNK(annualIpsfr1.setScale(0, BigDecimal.ROUND_HALF_UP));
		license1.setBhpPhl(new BigDecimal(getBhpPhl()));
		license1.setYearTo(new BigDecimal(1));
		bhpList.add(license1);
		
		Licenses license2 = new Licenses();
		double multiplyIndex1 = biRate2+ 1.0;
		BigDecimal percent2 = new BigDecimal(annualPercentage2);
		BigDecimal annualIpsfr2 = bhpPhl.multiply(percent2).multiply(new BigDecimal(multiplyIndex1))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total2 = annualIpsfr2;
		
		license2.setBhpUpfrontFee(new BigDecimal(0));
		license2.setBhpRate(new BigDecimal(getBiRate2()));
		license2.setBhpCalcIndex(new BigDecimal(multiplyIndex1).setScale(8, BigDecimal.ROUND_HALF_UP));
		license2.setBhpAnnualPercent(new BigDecimal(getAnnualRate2()));
		license2.setBhpAnnualValue(annualIpsfr2);
		license2.setBhpTotal(total2.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license2.setBhpNK(total2.setScale(0, BigDecimal.ROUND_HALF_UP));
		license2.setBhpPhl(new BigDecimal(getBhpPhl()));
		license2.setYearTo(new BigDecimal(2));
		license2.setBhpPhl(new BigDecimal(getBhpPhl()));
		bhpList.add(license2);
		
		
		Licenses license3 = new Licenses();
		double multiplyIndex2 = multiplyIndex1 * ( biRate3 + 1);
		BigDecimal percent3 = new BigDecimal(annualPercentage3);
		BigDecimal annualIpsfr3 = bhpPhl.multiply(percent3).multiply(new BigDecimal(multiplyIndex2))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total3 = annualIpsfr3;
		
		System.out.println("Ipsfr3 " + annualIpsfr3);
		System.out.println("total3 " + total3);
		System.out.println();
		
		license3.setBhpUpfrontFee(new BigDecimal(0));
		license3.setBhpRate(new BigDecimal(getBiRate3()));
		license3.setBhpCalcIndex(new BigDecimal(multiplyIndex2).setScale(8, BigDecimal.ROUND_HALF_UP));
		license3.setBhpAnnualPercent(new BigDecimal(getAnnualRate3()));
		license3.setBhpAnnualValue(annualIpsfr3);
		license3.setBhpTotal(total3.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license3.setBhpNK(total3.setScale(0, BigDecimal.ROUND_HALF_UP));
		license3.setBhpPhl(new BigDecimal(getBhpPhl()));
		license3.setYearTo(new BigDecimal(3));
		bhpList.add(license3);
		
		
		double multiplyIndex3 = multiplyIndex2 * ( biRate4 + 1);
		BigDecimal percent4 = new BigDecimal(annualPercentage4);
		BigDecimal annualIpsfr4 = bhpPhl.multiply(percent4).multiply(new BigDecimal(multiplyIndex3))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total4 = annualIpsfr4;
		
		System.out.println("Ipsfr4 " + annualIpsfr4);
		System.out.println("total4 " + total4);
		System.out.println();
		
		Licenses license4 = new Licenses();
		license4.setBhpUpfrontFee(new BigDecimal(0));
		license4.setBhpRate(new BigDecimal(getBiRate4()));
		license4.setBhpCalcIndex(new BigDecimal(multiplyIndex3).setScale(8, BigDecimal.ROUND_HALF_UP));
		license4.setBhpAnnualPercent(new BigDecimal(getAnnualRate4()));
		license4.setBhpAnnualValue(annualIpsfr4);
		license4.setBhpTotal(total4.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license4.setBhpNK(total4.setScale(0, BigDecimal.ROUND_HALF_UP));
		license4.setBhpPhl(new BigDecimal(getBhpPhl()));
		license4.setYearTo(new BigDecimal(4));
		bhpList.add(license4);
		
		double multiplyIndex4 = multiplyIndex3 * ( biRate5 + 1);
		BigDecimal percent5 = new BigDecimal(annualPercentage5);
		BigDecimal annualIpsfr5 = bhpPhl.multiply(percent5).multiply(new BigDecimal(multiplyIndex4))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total5 = annualIpsfr5;
		
		System.out.println("Ipsfr5 " + annualIpsfr5);
		System.out.println("total5 " + total5);
		System.out.println();
		
		Licenses license5 = new Licenses();
		license5.setBhpUpfrontFee(new BigDecimal(0));
		license5.setBhpRate(new BigDecimal(getBiRate5()));
		license5.setBhpCalcIndex(new BigDecimal(multiplyIndex4).setScale(8, BigDecimal.ROUND_HALF_UP));
		license5.setBhpAnnualPercent(new BigDecimal(getAnnualRate5()));
		license5.setBhpAnnualValue(annualIpsfr5);
		license5.setBhpTotal(total5.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license5.setBhpNK(total5.setScale(0, BigDecimal.ROUND_HALF_UP));
		license5.setBhpPhl(new BigDecimal(getBhpPhl()));
		license5.setYearTo(new BigDecimal(5));
		bhpList.add(license5);
		
		double multiplyIndex5 = multiplyIndex4 * ( biRate6 + 1);
		BigDecimal percent6 = new BigDecimal(annualPercentage6);
		BigDecimal annualIpsfr6 = bhpPhl.multiply(percent6).multiply(new BigDecimal(multiplyIndex5))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total6 = annualIpsfr6;
		
		System.out.println("Ipsfr6 " + annualIpsfr6);
		System.out.println("total6 " + total6);
		System.out.println();
		Licenses license6 = new Licenses();
		license6.setBhpUpfrontFee(new BigDecimal(0));
		license6.setBhpRate(new BigDecimal(getBiRate6()));
		license6.setBhpCalcIndex(new BigDecimal(multiplyIndex5).setScale(8, BigDecimal.ROUND_HALF_UP));
		license6.setBhpAnnualPercent(new BigDecimal(getAnnualRate6()));
		license6.setBhpAnnualValue(annualIpsfr6);
		license6.setBhpTotal(total6.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license6.setBhpNK(total6.setScale(0, BigDecimal.ROUND_HALF_UP));
		license6.setBhpPhl(new BigDecimal(getBhpPhl()));
		license6.setYearTo(new BigDecimal(6));
		bhpList.add(license6);
		
		
		double multiplyIndex6 = multiplyIndex5 * ( biRate7 + 1);
		BigDecimal percent7 = new BigDecimal(annualPercentage7);
		BigDecimal annualIpsfr7 = bhpPhl.multiply(percent7).multiply(new BigDecimal(multiplyIndex6))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total7 = annualIpsfr7;
		
		System.out.println("Ipsfr7 " + annualIpsfr7);
		System.out.println("total7 " + total7);
		System.out.println();
		Licenses license7 = new Licenses();
		license7.setBhpUpfrontFee(new BigDecimal(0));
		license7.setBhpRate(new BigDecimal(getBiRate7()));
		license7.setBhpCalcIndex(new BigDecimal(multiplyIndex6).setScale(8, BigDecimal.ROUND_HALF_UP));
		license7.setBhpAnnualPercent(new BigDecimal(getAnnualRate7()));
		license7.setBhpAnnualValue(annualIpsfr7);
		license7.setBhpTotal(total7.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license7.setBhpNK(total7.setScale(0, BigDecimal.ROUND_HALF_UP));
		license7.setBhpPhl(new BigDecimal(getBhpPhl()));
		license7.setYearTo(new BigDecimal(7));
		bhpList.add(license7);
		
		
		double multiplyIndex7 = multiplyIndex6 * ( biRate8 + 1);
		BigDecimal percent8 = new BigDecimal(annualPercentage8);
		BigDecimal annualIpsfr8 = bhpPhl.multiply(percent8).multiply(new BigDecimal(multiplyIndex7))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total8 = annualIpsfr8;
		
		System.out.println("Ipsfr8 " + annualIpsfr8);
		System.out.println("total8 " + total8);
		System.out.println();
		Licenses license8 = new Licenses();
		license8.setBhpUpfrontFee(new BigDecimal(0));
		license8.setBhpRate(new BigDecimal(getBiRate8()));
		license8.setBhpCalcIndex(new BigDecimal(multiplyIndex7).setScale(8, BigDecimal.ROUND_HALF_UP));
		license8.setBhpAnnualPercent(new BigDecimal(getAnnualRate8()));
		license8.setBhpAnnualValue(annualIpsfr8);
		license8.setBhpTotal(total8.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license8.setBhpNK(total8.setScale(0, BigDecimal.ROUND_HALF_UP));
		license8.setBhpPhl(new BigDecimal(getBhpPhl()));
		license8.setYearTo(new BigDecimal(8));
		bhpList.add(license8);
		
		double multiplyIndex8 = multiplyIndex7 * ( biRate9 + 1);
		BigDecimal percent9 = new BigDecimal(annualPercentage9);
		BigDecimal annualIpsfr9 = bhpPhl.multiply(percent9).multiply(new BigDecimal(multiplyIndex8))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total9 = annualIpsfr9;
		
		System.out.println("Ipsfr9 " + annualIpsfr9);
		System.out.println("total9 " + total9);
		System.out.println();
		Licenses license9 = new Licenses();
		license9.setBhpUpfrontFee(new BigDecimal(0));
		license9.setBhpRate(new BigDecimal(getBiRate9()));
		license9.setBhpCalcIndex(new BigDecimal(multiplyIndex8).setScale(8, BigDecimal.ROUND_HALF_UP));
		license9.setBhpAnnualPercent(new BigDecimal(getAnnualRate9()));
		license9.setBhpAnnualValue(annualIpsfr9);
		license9.setBhpTotal(total9.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license9.setBhpNK(total9.setScale(0, BigDecimal.ROUND_HALF_UP));
		license9.setBhpPhl(new BigDecimal(getBhpPhl()));
		license9.setYearTo(new BigDecimal(9));
		bhpList.add(license9);
		
		double multiplyIndex9 = multiplyIndex8 * ( biRate10 + 1);
		BigDecimal percent10 = new BigDecimal(annualPercentage10);
		BigDecimal annualIpsfr10 = bhpPhl.multiply(percent10).multiply(new BigDecimal(multiplyIndex9))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total10 = annualIpsfr10;
		
		System.out.println("Ipsfr10 " + annualIpsfr10);
		System.out.println("total10 " + total10);
		System.out.println();
		
		Licenses license10 = new Licenses();
		license10.setBhpUpfrontFee(new BigDecimal(0));
		license10.setBhpRate(new BigDecimal(getBiRate10()));
		license10.setBhpCalcIndex(new BigDecimal(multiplyIndex9).setScale(8, BigDecimal.ROUND_HALF_UP));
		license10.setBhpAnnualPercent(new BigDecimal(getAnnualRate10()));
		license10.setBhpAnnualValue(annualIpsfr10);
		license10.setBhpTotal(total10.setScale(0, BigDecimal.ROUND_HALF_UP));
		//ini tidak sesuai peruntukannnya hanya untuk menampung hasil round up saja dari total
		license10.setBhpNK(total10.setScale(0, BigDecimal.ROUND_HALF_UP));
		license10.setBhpPhl(new BigDecimal(getBhpPhl()));
		license10.setYearTo(new BigDecimal(10));
		bhpList.add(license10);
		
		
		getFields().put(LIST, bhpList);
		
		List<Licenses> bhpList2 = getBhpList();
		
		//For saving to audit log
		getUserManager().viewSimulasi(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "BHP Variety Rate");
		
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
	
	public String validationMore1000(){
		String errorMessage	= null;
		
		Double total = Double.valueOf(getAnnualRate1()) + Double.valueOf(getAnnualRate2()) + Double.valueOf(getAnnualRate3())
				+ Double.valueOf(getAnnualRate4()) + Double.valueOf(getAnnualRate5()) + Double.valueOf(getAnnualRate6())
				+ Double.valueOf(getAnnualRate7()) + Double.valueOf(getAnnualRate8()) + Double.valueOf(getAnnualRate9())
				+ Double.valueOf(getAnnualRate10());
		
		if(total > 1000){
			errorMessage = getText("mastermaintenance.ipsfr.variable.persentase.moreThan1000");
		}else if(total < 1000){
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
	
	public void refresh(IRequestCycle cycle) {
		getDelegate().clearErrors();
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
