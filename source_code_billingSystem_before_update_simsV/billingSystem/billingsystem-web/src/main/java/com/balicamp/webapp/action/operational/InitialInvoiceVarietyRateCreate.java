
package com.balicamp.webapp.action.operational;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.PdfService;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualPercentageBHPViewVariety;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualRateBHPView;
import com.balicamp.webapp.tapestry.PropertySelectionModel;


public abstract class InitialInvoiceVarietyRateCreate extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {
	
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	private ApplicationBandwidth appBandMan = null;
	
	public void setApplicationBandwidth(ApplicationBandwidth appBandMan){
		this.appBandMan = appBandMan;
	}
	
	public ApplicationBandwidth getApplicationBandwidth(){
		return appBandMan;
	}
	
	public abstract void setKmDateStr(String s);
	
	public abstract String getKmDateStr();
	
	public abstract void setLicBeginDateStr(String s);
	
	public abstract String getLicBeginDateStr();
	
	public abstract void setLicEndDateStr(String s);
	
	public abstract String getLicEndDateStr();
	
	public abstract void setCurBeginDateStr(String s);
	
	public abstract String getCurBeginDateStr();
	
	public abstract void setCurEndDateStr(String s);
	
	public abstract String getCurEndDateStr();
	
	public abstract void setPaymentDueDateStr(String s);
	
	public abstract String getPaymentDueDateStr();

	public abstract Invoice getInvoice();
	
	public abstract BankGuarantee getBankGuarantee();
	
	public abstract BankGuarantee getBg();
	
	public abstract void setIpsfrNo(String ipsfrNo);
	
	public abstract String getIpsfrNo();
	
	@InitialValue("literal:1")
	public abstract String getYearTo();

	public abstract void setYearTo(String bigdecimal);
	
	public abstract BigDecimal getYear();

	public abstract void setYear(BigDecimal bigdecimal);

	public abstract Date getBgDueDate();

	public abstract void setBgDueDate(Date date);
	
	private String biRate;
	
	public void setBiRate(String bi){
		this.biRate = bi;
	}
	
	public String getBiRate(){
		return biRate;
	}
	
	private String percentageAnnualBhp;
	
	public void setPercentageAnnualBhp(String annualBhp){
		percentageAnnualBhp = annualBhp;
	}
	
	public String getPercentageAnnualBhp(){
		return percentageAnnualBhp;
	}
	
	public abstract void setPercentageBHP(String bhp);
	
	public abstract String getPercentageBHP();
	
	public IPropertySelectionModel getPercentageBHPModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		List<VariableAnnualPercentage> list = getVariableManager().findAll();
		for (VariableAnnualPercentage variableAnnualPercentage : list) {
			if(variableAnnualPercentage.getVariableStatus() == 1){
				map.put(variableAnnualPercentage.getKmNo(), variableAnnualPercentage.getKmNo());
			}
		}

		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setMultiplyIndex(String index);
	
	public abstract String getMultiplyIndex();
	
	public abstract BigDecimal getBhpUpfrontFee();

	public abstract void setBhpUpfrontFee(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpHl();

	public abstract void setBhpHl(BigDecimal bigdecimal);

	public abstract BigDecimal getBhpPhl();

	public abstract void setBhpPhl(BigDecimal bigdecimal);
	
	public abstract void setIpsfrYearOne(String ipsfr);
	
	public abstract String getIpsfrYearOne();
	
	public abstract void setBhpValueRadioFreq(String value);
	
	public abstract String getBhpValueRadioFreq();
	
	public abstract void setBgYear2(String bg);
	
	public abstract String getBgYear2();
	
	private String enableInput = "";
	
	public void setEnableInput(String enable){
		enableInput = enable;
	}

	public String getEnableInput(){
		return enableInput;
	}
	
	private String showTable = "";
	
	public void setShowTable(String show){
		showTable = show;
	}
	
	public String getShowTable(){
		return showTable;
	}
	
	public abstract void setPaymentType(String type);
	
	public abstract String getPayementType();
	
	public abstract void setBhpMethod(String bhp);
	
	public abstract String getBhpMethod();
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	@InjectObject("spring:invoiceManager")
	public abstract InvoiceManager getInvoiceManager();
	
	@InjectObject("spring:applicationBandwidthManager")
	public abstract ApplicationBandwidthManager getApplicationBandwidthManager();
	
	@InjectObject("engine-service:page")
	public abstract IEngineService getDownloadService();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
//	public abstract void setApplicationList(List<ApplicationBandwidth> list);
//	public abstract List<ApplicationBandwidth> getApplicationList();
	
//	private List<ApplicationBandwidth> applicationList;
//	public void setApplicationList(List<ApplicationBandwidth> list){
//		this.applicationList = list;
//	}
//	public List<ApplicationBandwidth> getApplicationList(){
//		return applicationList;
//	}
	
	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();
	
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		if(appBandMan.getKmDate() != null){
			setKmDateStr(dateFormat.format(appBandMan.getKmDate()));
		}
		
		if(appBandMan.getLiBeginDate() != null){
			setLicBeginDateStr(dateFormat.format(appBandMan.getLiBeginDate()));
		}
		
		if(appBandMan.getLiEndDate() != null){
			setLicEndDateStr(dateFormat.format(appBandMan.getLiEndDate()));
		}
		
		if(appBandMan.getCurrentBeginDate() != null){
			setCurBeginDateStr(dateFormat.format(appBandMan.getCurrentBeginDate()));
		}
		
		if(appBandMan.getCurrentEndDate() != null){
			setCurEndDateStr(dateFormat.format(appBandMan.getCurrentEndDate()));
		}
		
		if(appBandMan.getPaymentDueDate() != null){
			setPaymentDueDateStr(dateFormat.format(appBandMan.getPaymentDueDate()));
		}
		
		if(getPercentageAnnualBhp() != null){
			setPercentageBHP(getPercentageAnnualBhp());
		}
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			
			if(!isNotFirstLoad()){
				if(getFields() == null){
					setFields(new HashMap());
				}
				
				if(appBandMan.getBhpPaymentType().equalsIgnoreCase("FP")){
					setPaymentType("Full Payment");
				}
				
				if(appBandMan.getBhpMethod().equalsIgnoreCase("FR")){
					setBhpMethod("Flat BHP");
				}else if(appBandMan.getBhpMethod().equalsIgnoreCase("VR")){
					setBhpMethod("Variety Rate");
				}else{
					setBhpMethod("Conversion");
				}
				
				if(getPercentageBHP() != null){
					getFields().put("percentage", getPercentageBHP());
				}else{
					getFields().put("percentage", null);
				}
				
				setNotFirstLoad(true);
			}

		}
		
		Date dates = appBandMan.getKmDate();
		
	}
	
	public void viewBiRate(IRequestCycle cycle){

		List<VariableAnnualRate> rates = getVariableAnnualRateManager().findByStatus(1);
		
		AnnualRateBHPView annualRateBHPEdit = (AnnualRateBHPView) cycle
				.getPage("annualRateBHPView");
		
		VariableAnnualRate rate = null;
		if(rates.size() > 0){
			rate = rates.get(0);

			annualRateBHPEdit.setAnnualRateId(rate.getAnnualRateId());
			annualRateBHPEdit.setActiveStatus(rate.getActiveStatus());
			annualRateBHPEdit.setSaveStatus(rate.getSaveStatus());
			annualRateBHPEdit.setRateValue(rate.getRateValue());
			annualRateBHPEdit.setRateYear(rate.getRateYear());
			annualRateBHPEdit.setBaseOnNote(rate.getKmNo());
			annualRateBHPEdit.setPageLocation("initial");
			cycle.activate(annualRateBHPEdit);
		}
		
	}
	public String popupBiRate(){
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		if(rateList.size() > 0){
			int yearActiveBiRate = rateList.get(0).getRateYear().intValue();
			int invYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(appBandMan.getLiBeginDate()));
			if(invYear < yearActiveBiRate){
				VariableAnnualRate rateTemp = getVariableAnnualRateManager().findByYear( new BigDecimal(String.valueOf(invYear)) );
				saveObjectToSession(rateTemp, "BI_RATE_DETAIL_LIST");
				return "bi_rate_view()";
			}else{
				saveObjectToSession("popupBi", "POPUP_BI");
				return "bi_search()";
			}
		}else{
			saveObjectToSession("popupBi", "POPUP_BI");
			return "bi_search()";
		}
	}
	
	public IPage viewBhpAnnualRate(IRequestCycle cycle){
		
		if(getPercentageBHP() == null){
			String validationMsg = "Anda Harus Memilih Persentase BHP Tahunan";
			addError(getDelegate(), "errorShadow",validationMsg , ValidationConstraint.CONSISTENCY);
			return null;
		}

		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());    

		Long annualPercentId = variable.getAnnualPercentId();
		
		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentId, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentId, 2);
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentId, 3);
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentId, 4);
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentId, 5);
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentId, 6);
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentId, 7);
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentId, 8);
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentId, 9);
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentId, 10);
		
		AnnualPercentageBHPViewVariety variableView =  (AnnualPercentageBHPViewVariety) cycle
				.getPage("annualPercentageBHPViewVariety");
		
		if (variable.getVariableStatus() == 1) {
			variableView.setVariableStatus("Active");
		} else {
			variableView.setVariableStatus("Inactive");
		}
		variableView.setVariablePresentaseTahunan(String.valueOf(variable.getPercentYear()));
		variableView.setAnnualPercentId(variable.getAnnualPercentId());
		variableView.setBerdasarkanDokumen(variable.getKmNo());
		variableView.setPercentageYearOne(String.valueOf(percentageYearOne.getPercentage()));
		variableView.setPercentageYearTwo(String.valueOf(percentageYearTwo.getPercentage()));
		variableView.setPercentageYearThree(String.valueOf(percentageYearThree.getPercentage()));
		variableView.setPercentageYearFour(String.valueOf(percentageYearFour.getPercentage()));
		variableView.setPercentageYearFive(String.valueOf(percentageYearFive.getPercentage()));
		variableView.setPercentageYearSix(String.valueOf(percentageYearSix.getPercentage()));
		variableView.setPercentageYearSeven(String.valueOf(percentageYearSeven.getPercentage()));
		variableView.setPercentageYearEight(String.valueOf(percentageYearEight.getPercentage()));
		variableView.setPercentageYearNine(String.valueOf(percentageYearNine.getPercentage()));
		variableView.setPercentageYearTen(String.valueOf(percentageYearTen.getPercentage()));
		variableView.setPageLocation("initial");
		
		return variableView;
	}
	
	public String annualRatePopup(){
		if( (getFields() != null) && (getFields().get("percentage") != null) ){
			VariableAnnualPercentage variable = getVariableManager().findByKmNo(getFields().get("percentage").toString()); 
			List<VariableAnnualPercentageDetail> variableDetail = getVariableDetailManager().findByAnnualPercentId(variable.getAnnualPercentId());
			saveObjectToSession(variable, "ANNUAL_PERCENTAGE");
			saveObjectToSession(variableDetail, "ANNUAL_PERCETAGE_DETAIL_LIST");
			return "annual_percentage_view()";
			
		}else{
			saveObjectToSession("popupAnnualRate", "POPUP_ANNUAL");

			return "percentage_annual()";
		}

	}
	
	public void hitung(){
		
		if((appBandMan.getCurrentBeginDate() == null)){
			addError(getDelegate(), "errorShadow", "Tidak Dapat dihitung karena Tanggal Mulai lisensi kosong", ValidationConstraint.CONSISTENCY);
			return; 
		}
		
//		set hidden fields
		setPercentageAnnualBhp(getPercentageBHP());
		
		Calendar dateBegin = Calendar.getInstance();
		dateBegin.setTime(appBandMan.getCurrentBeginDate());
		
		String validationMsg 	= generalValidation();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String adaBiRateYangAktif = isThereIsActiveBiRate();
		if(adaBiRateYangAktif != null){
			addError(getDelegate(), "errorShadow", adaBiRateYangAktif, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		List<Invoice> invoiceList = new ArrayList<Invoice>();
		
		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());           

		Long annualPercentId = variable.getAnnualPercentId();
		
		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentId, 1);
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentId, 2);
		BigDecimal percent2 = percentageYearTwo.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentId, 3);
		BigDecimal percent3 = percentageYearThree.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentId, 4);
		BigDecimal percent4 = percentageYearFour.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentId, 5);
		BigDecimal percent5 = percentageYearFive.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentId, 6);
		BigDecimal percent6 = percentageYearSix.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentId, 7);
		BigDecimal percent7 = percentageYearSeven.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentId, 8);
		BigDecimal percent8 = percentageYearEight.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentId, 9);
		BigDecimal percent9 = percentageYearNine.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentId, 10);
		BigDecimal percent10 = percentageYearTen.getPercentage().divide(new BigDecimal("100"));
		
//		Search bi Rate based on active status, it will return list, but it just consist one object, 
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		VariableAnnualRate rate1 = rateList.get(0);
		BigDecimal biRate1 = rate1.getRateValue();
		
		//cek apakah tahun yg digunakan lebih kecil dari tahun bi rate
//		Integer yearActive = rate1.getRateYear().intValue();
//		Date appBandManLicBeginDate = appBandMan.getCurrentBeginDate(); 
//		SimpleDateFormat format = new SimpleDateFormat("yyyy");
//		Integer invYear = Integer.valueOf(format.format(appBandManLicBeginDate));
//		if(invYear >= yearActive){
//			biRate1 = rate1.getRateValue();
//		}else{
//			VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(format.format(appBandManLicBeginDate)));
//			biRate1 = rate2.getRateValue();
//		}
		
//		Ini berdasarkan tahun biRAte
//		int intDate = Integer.valueOf(String.valueOf(rate1.getRateYear()));
		
//		Ini berdasarkan license currentBeginDate
		int intDate = dateBegin.get(Calendar.YEAR);
				
		VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 1)));
		BigDecimal biRate2 = new BigDecimal("0");
		if(rate2 != null){
			biRate2 = rate2.getRateValue().divide(new BigDecimal("100"));
		}
		//cek apakah tahun yg digunakan lebih kecil dari tahun bi rate
		Integer yearActive = rate1.getRateYear().intValue();
		Date appBandManLicBeginDate = appBandMan.getCurrentBeginDate(); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Integer invYear = Integer.valueOf(format.format(appBandManLicBeginDate));
		if(invYear >= yearActive){
			biRate2 = rate1.getRateValue().divide(new BigDecimal("100"));
		}else{
			VariableAnnualRate rateTemp = getVariableAnnualRateManager().findByYear( (new BigDecimal(format.format(appBandManLicBeginDate))).add(new BigDecimal("1")) );
			if(rateTemp == null){
				addError(getDelegate(), "errorShadow", getText("initial.invoice.notActive.biRate"), ValidationConstraint.CONSISTENCY);
				return;
			}
			
			biRate2 = rateTemp.getRateValue().divide(new BigDecimal("100"));
		}
		
		VariableAnnualRate rate3 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 2)));
		BigDecimal biRate3 = new BigDecimal("0");
		if(rate3 != null){
			biRate3 = rate3.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate4 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 3)));
		BigDecimal biRate4 = new BigDecimal("0");
		if(rate4 != null){
			biRate4 = rate4.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate5 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 4)));
		BigDecimal biRate5 = new BigDecimal("0");
		if(rate5 != null){
			biRate5 = rate5.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate6 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 5)));
		BigDecimal biRate6 = new BigDecimal("0");
		if(rate6 != null){
			biRate6 = rate6.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate7 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 6)));
		BigDecimal biRate7 = new BigDecimal("0");
		if(rate7 != null){
			biRate7 = rate7.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate8 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 7)));
		BigDecimal biRate8 = new BigDecimal("0");
		if(rate8 != null){
			biRate8 = rate8.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate9 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 8)));
		BigDecimal biRate9 = new BigDecimal("0");
		if(rate9 != null){
			biRate9 = rate9.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate10 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 9)));
		BigDecimal biRate10 = new BigDecimal("0");
		if(rate10 != null){
			biRate10 = rate10.getRateValue().divide(new BigDecimal("100"));
		}
		//end
		
		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();
		}
		
		BigDecimal ipsfrYearOne = auctionPrice.multiply(percentageYearOne.getPercentage().divide(new BigDecimal("100")));
		setIpsfrYearOne(String.valueOf(ipsfrYearOne));
		BigDecimal bhpRadioFreq = getBhpUpfrontFee().add(ipsfrYearOne);
		setBhpValueRadioFreq(String.valueOf(bhpRadioFreq));
		
		Invoice invoice1 = new Invoice();
		invoice1.setYearTo(new BigDecimal(1));
		invoice1.setInvBeginDate(dateBegin.getTime());
		invoice1.setBhpUpfrontFee(getBhpUpfrontFee());
//		invoice1.setBhpRate(new BigDecimal("0"));
//		invoice1.setInvoiceStatus("-");
		invoice1.setBhpAnnualPercent(percentageYearOne.getPercentage());
		invoice1.setBhpAnnual(ipsfrYearOne.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice1.setBhpTotal(bhpRadioFreq.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice1);
		
		//added at 12-05-2014
		BigDecimal multiplyIndex1 = (biRate2).add(new BigDecimal("1")).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr2 =  auctionPrice.multiply(percent2).multiply(multiplyIndex1)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice2 = new Invoice();
		invoice2.setYearTo(new BigDecimal(2));
		dateBegin.add(Calendar.YEAR,  1);
		invoice2.setInvBeginDate(dateBegin.getTime());
//		if(rate2 != null){
//			invoice2.setBhpRate(rate2.getRateValue());
//		}
		invoice2.setBhpRate(biRate2.multiply(new BigDecimal("100")).stripTrailingZeros());
		invoice2.setBhpCalcIndex(multiplyIndex1);
		invoice2.setBhpAnnualPercent(percentageYearTwo.getPercentage());
		invoice2.setBhpAnnual(annualIpsfr2.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice2.setBhpTotal(annualIpsfr2.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice2);
		
		BigDecimal multiplyIndex2 = (biRate3.add(new BigDecimal("1"))).multiply(multiplyIndex1).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr3 =  auctionPrice.multiply(percent3).multiply(multiplyIndex2)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice3 = new Invoice();
		invoice3.setYearTo(new BigDecimal(3));
		dateBegin.add(Calendar.YEAR,  1);
		invoice3.setInvBeginDate(dateBegin.getTime());
		if(rate3 != null){
			invoice3.setBhpRate(rate3.getRateValue());
		}
		invoice3.setBhpCalcIndex(multiplyIndex2);
		invoice3.setBhpAnnualPercent(percentageYearThree.getPercentage());
		invoice3.setBhpAnnual(annualIpsfr3.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice3.setBhpTotal(annualIpsfr3.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice3);
		
		BigDecimal multiplyIndex3 = (biRate4.add(new BigDecimal("1"))).multiply(multiplyIndex2).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr4 =  auctionPrice.multiply(percent4).multiply(multiplyIndex3)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice4 = new Invoice();
		invoice4.setYearTo(new BigDecimal(4));
		dateBegin.add(Calendar.YEAR,  1);
		invoice4.setInvBeginDate(dateBegin.getTime());
		if(rate4 != null){
			invoice4.setBhpRate(rate4.getRateValue());
		}
		invoice4.setBhpCalcIndex(multiplyIndex3);
		invoice4.setBhpAnnualPercent(percentageYearFour.getPercentage());
		invoice4.setBhpAnnual(annualIpsfr4.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice4.setBhpTotal(annualIpsfr4.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice4);
		
		BigDecimal multiplyIndex4 = (biRate5.add(new BigDecimal("1"))).multiply(multiplyIndex3).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr5 =  auctionPrice.multiply(percent5).multiply(multiplyIndex4)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice5 = new Invoice();
		invoice5.setYearTo(new BigDecimal(5));
		dateBegin.add(Calendar.YEAR,  1);
		invoice5.setInvBeginDate(dateBegin.getTime());
		if(rate5 != null){
			invoice5.setBhpRate(rate5.getRateValue());
		}
		invoice5.setBhpCalcIndex(multiplyIndex4);
		invoice5.setBhpAnnualPercent(percentageYearFive.getPercentage());
		invoice5.setBhpAnnual(annualIpsfr5.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice5.setBhpTotal(annualIpsfr5.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice5);
		
		BigDecimal multiplyIndex5 = (biRate6.add(new BigDecimal("1"))).multiply(multiplyIndex4).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr6 =  auctionPrice.multiply(percent6).multiply(multiplyIndex5)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice6 = new Invoice();
		invoice6.setYearTo(new BigDecimal(6));
		dateBegin.add(Calendar.YEAR,  1);
		invoice6.setInvBeginDate(dateBegin.getTime());
		if(rate6 != null){
			invoice6.setBhpRate(rate6.getRateValue());
		}
		invoice6.setBhpCalcIndex(multiplyIndex5);
		invoice6.setBhpAnnualPercent(percentageYearSix.getPercentage());
		invoice6.setBhpAnnual(annualIpsfr6.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice6.setBhpTotal(annualIpsfr6.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice6);
		
		BigDecimal multiplyIndex6 = (biRate7.add(new BigDecimal("1"))).multiply(multiplyIndex5).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr7 =  auctionPrice.multiply(percent7).multiply(multiplyIndex6)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice7 = new Invoice();
		invoice7.setYearTo(new BigDecimal(7));
		dateBegin.add(Calendar.YEAR,  1);
		invoice7.setInvBeginDate(dateBegin.getTime());
		if(rate7 != null){
			invoice7.setBhpRate(rate7.getRateValue());
		}
		invoice7.setBhpCalcIndex(multiplyIndex6);
		invoice7.setBhpAnnualPercent(percentageYearSeven.getPercentage());
		invoice7.setBhpAnnual(annualIpsfr7.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice7.setBhpTotal(annualIpsfr7.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice7);
		
		BigDecimal multiplyIndex7 = (biRate8.add(new BigDecimal("1"))).multiply(multiplyIndex6).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr8 =  auctionPrice.multiply(percent8).multiply(multiplyIndex7)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice8 = new Invoice();
		invoice8.setYearTo(new BigDecimal(8));
		dateBegin.add(Calendar.YEAR,  1);
		invoice8.setInvBeginDate(dateBegin.getTime());
		if(rate8 != null){
			invoice8.setBhpRate(rate8.getRateValue());
		}
		invoice8.setBhpCalcIndex(multiplyIndex7);
		invoice8.setBhpAnnualPercent(percentageYearEight.getPercentage());
		invoice8.setBhpAnnual(annualIpsfr8.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice8.setBhpTotal(annualIpsfr8.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice8);
		
		BigDecimal multiplyIndex8 = (biRate9.add(new BigDecimal("1"))).multiply(multiplyIndex7).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr9 =  auctionPrice.multiply(percent9).multiply(multiplyIndex8)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice9 = new Invoice();
		invoice9.setYearTo(new BigDecimal(9));
		dateBegin.add(Calendar.YEAR,  1);
		invoice9.setInvBeginDate(dateBegin.getTime());
		if(rate9 != null){
			invoice9.setBhpRate(rate9.getRateValue());
		}
		invoice9.setBhpCalcIndex(multiplyIndex8);
		invoice9.setBhpAnnualPercent(percentageYearNine.getPercentage());
		invoice9.setBhpAnnual(annualIpsfr9.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice9.setBhpTotal(annualIpsfr9.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice9);
		
		BigDecimal multiplyIndex9 = (biRate10.add(new BigDecimal("1"))).multiply(multiplyIndex8).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal annualIpsfr10 =  auctionPrice.multiply(percent10).multiply(multiplyIndex9)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		Invoice invoice10 = new Invoice();
		invoice10.setYearTo(new BigDecimal(10));
		dateBegin.add(Calendar.YEAR,  1);
		invoice10.setInvBeginDate(dateBegin.getTime());
		if(rate10 != null){
			invoice10.setBhpRate(rate10.getRateValue());
		}
		invoice10.setBhpCalcIndex(multiplyIndex9);
		invoice10.setBhpAnnualPercent(percentageYearTen.getPercentage());
		invoice10.setBhpAnnual(annualIpsfr10.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoice10.setBhpTotal(annualIpsfr10.setScale(0,  BigDecimal.ROUND_HALF_UP));
		invoiceList.add(invoice10);
		//end
		
		
		getFields().put("INVOICE_LIST_VARIETY", invoiceList);
		
		setEnableInput("N");
		setShowTable("Y");
		
		if(appBandMan.getIsBgAvailable() != null){
			if(appBandMan.getIsBgAvailable().equalsIgnoreCase("Y")){
				hitungBg();
			}
		}
	}
	
	public List<Invoice> getInvoiceList() {
		List bhpList = (List) getFields().get("INVOICE_LIST_VARIETY");
		return bhpList;
	}
	
	public void hitungBg(){
		BigDecimal constPercent = new BigDecimal("1.02");
		
		List<BankGuarantee> bgList = new ArrayList<BankGuarantee>();
		
		//BI Rate
//		Search bi Rate based on active status, it will return list, but it just consist one object, 
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		VariableAnnualRate rate1 = rateList.get(0);
		
		BigDecimal biRate1 = new BigDecimal("0");
		
		//cek apakah tahun yg digunakan lebih kecil dari tahun bi rate
		Integer yearActive = rate1.getRateYear().intValue();
		Date appBandManLicBeginDate = appBandMan.getCurrentBeginDate(); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Integer invYear = Integer.valueOf(format.format(appBandManLicBeginDate));
		if(invYear >= yearActive){
			biRate1 = rate1.getRateValue().divide(new BigDecimal("100"));
		}else{
			VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(format.format(appBandManLicBeginDate)));
			biRate1 = rate2.getRateValue().divide(new BigDecimal("100"));
			rate1 = rate2;
		}
		
		int intDate = Integer.valueOf(String.valueOf(rate1.getRateYear()));
	
		
//		if(rate1 != null){
//			biRate1 = rate1.getRateValue().divide(new BigDecimal("100"));
//		}
		VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 1)));
		BigDecimal biRate2 = new BigDecimal("0");
		if(rate2 != null){
			biRate2 = rate2.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate3 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 2)));
		BigDecimal biRate3 = new BigDecimal("0");
		if(rate3 != null){
			biRate3 = rate3.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate4 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 3)));
		BigDecimal biRate4 = new BigDecimal("0");
		if(rate4 != null){
			biRate4 = rate4.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate5 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 4)));
		BigDecimal biRate5 = new BigDecimal("0");
		if(rate5 != null){
			biRate5 = rate5.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate6 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 5)));
		BigDecimal biRate6 = new BigDecimal("0");
		if(rate6 != null){
			biRate6 = rate6.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate7 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 6)));
		BigDecimal biRate7 = new BigDecimal("0");
		if(rate7 != null){
			biRate7 = rate7.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate8 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 7)));
		BigDecimal biRate8 = new BigDecimal("0");
		if(rate8 != null){
			biRate8 = rate8.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate9 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 8)));
		BigDecimal biRate9 = new BigDecimal("0");
		if(rate9 != null){
			biRate9 = rate9.getRateValue().divide(new BigDecimal("100"));
		}
		VariableAnnualRate rate10 = getVariableAnnualRateManager().findByYear(new BigDecimal((intDate + 9)));
		BigDecimal biRate10 = new BigDecimal("0");
		if(rate10 != null){
			biRate10 = rate10.getRateValue().divide(new BigDecimal("100"));
		}
		
		//BHP percentage
		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());           
		Long annualPercentId = variable.getAnnualPercentId();
		
		VariableAnnualPercentageDetail percentageYearOne = getVariableDetailManager()
				.findByYear(annualPercentId, 1);
		BigDecimal percent1 = percentageYearOne.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearTwo = getVariableDetailManager()
				.findByYear(annualPercentId, 2);
		BigDecimal percent2 = percentageYearTwo.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearThree = getVariableDetailManager()
				.findByYear(annualPercentId, 3);
		BigDecimal percent3 = percentageYearThree.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearFour = getVariableDetailManager()
				.findByYear(annualPercentId, 4);
		BigDecimal percent4 = percentageYearFour.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearFive = getVariableDetailManager()
				.findByYear(annualPercentId, 5);
		BigDecimal percent5 = percentageYearFive.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearSix = getVariableDetailManager()
				.findByYear(annualPercentId, 6);
		BigDecimal percent6 = percentageYearSix.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearSeven = getVariableDetailManager()
				.findByYear(annualPercentId, 7);
		BigDecimal percent7 = percentageYearSeven.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearEight = getVariableDetailManager()
				.findByYear(annualPercentId, 8);
		BigDecimal percent8 = percentageYearEight.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearNine = getVariableDetailManager()
				.findByYear(annualPercentId, 9);
		BigDecimal percent9 = percentageYearNine.getPercentage().divide(new BigDecimal("100"));
		VariableAnnualPercentageDetail percentageYearTen = getVariableDetailManager()
				.findByYear(annualPercentId, 10);
		BigDecimal percent10 = percentageYearTen.getPercentage().divide(new BigDecimal("100"));
		
		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();
		}
		
		BigDecimal multiplyIndex1 = biRate1.add(new BigDecimal("1")).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		BigDecimal total1 = constPercent.multiply(percent2).multiply(multiplyIndex1).multiply(auctionPrice);
		BankGuarantee bg1 = new BankGuarantee();
		bg1.setSubmitYearTo(new BigDecimal(1));
		bg1.setBankAddress(String.valueOf(intDate));
		if(rate1 != null){
			bg1.setBiRate(rate1.getRateValue());
		}
		bg1.setCalcIndex(multiplyIndex1);
		bg1.setBhpPercent(percentageYearTwo.getPercentage());
		bg1.setBgValue(total1.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg1);
		
		//For set textfield
		setBgYear2(String.valueOf(total1));
		
		
		BigDecimal multiplyIndex2 = (biRate2.add(new BigDecimal("1"))).multiply(multiplyIndex1).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total2 = constPercent.multiply(percent3).multiply(multiplyIndex2).multiply(auctionPrice);
		BankGuarantee bg2 = new BankGuarantee();
		bg2.setSubmitYearTo(new BigDecimal(2));
		bg2.setBankAddress(String.valueOf(intDate + 1));
		if(rate2 != null){
			bg2.setBiRate(rate2.getRateValue());
		}
		bg2.setCalcIndex(multiplyIndex2);
		bg2.setBhpPercent(percentageYearThree.getPercentage());
		bg2.setBgValue(total2.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg2);
		
		BigDecimal multiplyIndex3 = (biRate3.add(new BigDecimal("1"))).multiply(multiplyIndex2).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total3 = constPercent.multiply(percent4).multiply(multiplyIndex3).multiply(auctionPrice);
		BankGuarantee bg3 = new BankGuarantee();
		bg3.setSubmitYearTo(new BigDecimal(3));
		bg3.setBankAddress(String.valueOf(intDate + 2));
		if(rate3 != null){
			bg3.setBiRate(rate3.getRateValue());
		}
		bg3.setCalcIndex(multiplyIndex3);
		bg3.setBhpPercent(percentageYearFour.getPercentage());
		bg3.setBgValue(total3.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg3);
		
		BigDecimal multiplyIndex4 = (biRate4.add(new BigDecimal("1"))).multiply(multiplyIndex3).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total4 = constPercent.multiply(percent5).multiply(multiplyIndex4).multiply(auctionPrice);
		BankGuarantee bg4 = new BankGuarantee();
		bg4.setSubmitYearTo(new BigDecimal(4));
		bg4.setBankAddress(String.valueOf(intDate + 3));
		if(rate4 != null){
			bg4.setBiRate(rate4.getRateValue());
		}
		bg4.setCalcIndex(multiplyIndex4);
		bg4.setBhpPercent(percentageYearFive.getPercentage());
		bg4.setBgValue(total4.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg4);
		
		BigDecimal multiplyIndex5 = (biRate5.add(new BigDecimal("1"))).multiply(multiplyIndex4).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total5 = constPercent.multiply(percent6).multiply(multiplyIndex5).multiply(auctionPrice);
		BankGuarantee bg5 = new BankGuarantee();
		bg5.setSubmitYearTo(new BigDecimal(5));
		bg5.setBankAddress(String.valueOf(intDate + 4));
		if(rate5 != null){
			bg5.setBiRate(rate5.getRateValue());
		}
		bg5.setCalcIndex(multiplyIndex5);
		bg5.setBhpPercent(percentageYearSix.getPercentage());
		bg5.setBgValue(total5.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg5);
		
		BigDecimal multiplyIndex6 = (biRate6.add(new BigDecimal("1"))).multiply(multiplyIndex5).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total6 = constPercent.multiply(percent7).multiply(multiplyIndex6).multiply(auctionPrice);
		BankGuarantee bg6 = new BankGuarantee();
		bg6.setSubmitYearTo(new BigDecimal(6));
		bg6.setBankAddress(String.valueOf(intDate + 5));
		if(rate6 != null){
			bg6.setBiRate(rate6.getRateValue());
		}
		bg6.setCalcIndex(multiplyIndex6);
		bg6.setBhpPercent(percentageYearSeven.getPercentage());
		bg6.setBgValue(total6.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg6);
		
		BigDecimal multiplyIndex7 = (biRate7.add(new BigDecimal("1"))).multiply(multiplyIndex6).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total7 = constPercent.multiply(percent8).multiply(multiplyIndex7).multiply(auctionPrice);
		BankGuarantee bg7 = new BankGuarantee();
		bg7.setSubmitYearTo(new BigDecimal(7));
		bg7.setBankAddress(String.valueOf(intDate + 6));
		if(rate7 != null){
			bg7.setBiRate(rate7.getRateValue());
		}
		bg7.setCalcIndex(multiplyIndex7);
		bg7.setBhpPercent(percentageYearEight.getPercentage());
		bg7.setBgValue(total7.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg7);
		
		BigDecimal multiplyIndex8 = (biRate8.add(new BigDecimal("1"))).multiply(multiplyIndex7).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total8 = constPercent.multiply(percent9).multiply(multiplyIndex8).multiply(auctionPrice);
		BankGuarantee bg8 = new BankGuarantee();
		bg8.setSubmitYearTo(new BigDecimal(8));
		bg8.setBankAddress(String.valueOf(intDate + 7));
		if(rate8 != null){
			bg8.setBiRate(rate8.getRateValue());
		}
		bg8.setCalcIndex(multiplyIndex8);
		bg8.setBhpPercent(percentageYearNine.getPercentage());
		bg8.setBgValue(total8.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg8);
		
		BigDecimal multiplyIndex9 = (biRate9.add(new BigDecimal("1"))).multiply(multiplyIndex8).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();;
		BigDecimal total9 = constPercent.multiply(percent10).multiply(multiplyIndex9).multiply(auctionPrice);
		BankGuarantee bg9 = new BankGuarantee();
		bg9.setSubmitYearTo(new BigDecimal(9));
		bg9.setBankAddress(String.valueOf(intDate + 9));
		if(rate9 != null){
			bg9.setBiRate(rate9.getRateValue());
		}
		bg9.setCalcIndex(multiplyIndex9);
		bg9.setBhpPercent(percentageYearTen.getPercentage());
		bg9.setBgValue(total9.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgList.add(bg9);
		
		getFields().put("BG_LIST", bgList);
	}
	
	public List<BankGuarantee> getBgList() {
		List bhpList = (List) getFields().get("BG_LIST");
		return bhpList;
	}
	
	public void doDraft(IRequestCycle cycle){
		String validationMsg = calculateValidation();
		
		if(validationMsg != null){
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String checkLicense = checkLicense();
		if(checkLicense != null){
			addError(getDelegate(), "errorShadow", checkLicense, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		Calendar dateBegin = Calendar.getInstance();
		dateBegin.setTime(appBandMan.getCurrentBeginDate());
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.setTime(appBandMan.getCurrentEndDate());
		Calendar datePayment = Calendar.getInstance();
		datePayment.setTime(appBandMan.getPaymentDueDate());
		
		List<Invoice> invoices = (List) getFields().get("INVOICE_LIST_VARIETY");
		List<BankGuarantee> bgList = (List) getFields().get("BG_LIST");
		
		List<Invoice> invoiceListToSaved = new ArrayList<Invoice>();
		List<BankGuarantee> bgListToSaved = new ArrayList<BankGuarantee>();
		
		License license = new License();
		license.setLicenceNo(appBandMan.getLicenceNumber());
		license.setBhpMethod(appBandMan.getBhpMethod());
		license.setClientName(appBandMan.getClientCompany());
		license.setClientNo(new BigDecimal(appBandMan.getClientNumber()));
		license.setKmNo(appBandMan.getKmNo());
		if(appBandMan.getZoneNo() != null){
			license.setZoneNo(appBandMan.getZoneNo().toString());
		}
		license.setZoneName(appBandMan.getZoneName());
		license.setKmDate(appBandMan.getKmDate());
		license.setLicenceBeginDate(appBandMan.getLiBeginDate());
		license.setLicenceEndDate(appBandMan.getLiEndDate());
		license.setCurrentBeginDate(appBandMan.getCurrentBeginDate());
		license.setCurrentEndDate(appBandMan.getCurrentEndDate());
		license.setFreqTMin(appBandMan.getFreqMin());
		license.setFreqTMax(appBandMan.getFreqMax());
		license.setFreqRMin(appBandMan.getFreqMinR());
		license.setFreqRMax(appBandMan.getFreqMaxR());
		license.setPaymentType(appBandMan.getBhpPaymentType());
		license.setLicenceStatus("D");
		license.setCreatedOn(new Date());
//		license.setCreatedBy(getUserLoginFromSession().getUsername());
		if(getUserLoginFromSession() != null){
			license.setCreatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			license.setCreatedBy(getUserLoginFromDatabase().getUsername());
		}
		license.setPaymentDueDate(appBandMan.getPaymentDueDate());
		license.setServiceId(appBandMan.getSvId());
		license.setSubserviceId(appBandMan.getSsId());

		
		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}
		
//		String invoiceNumber = getInvoiceManager().generateInvoiceNumber(appBandMan.getLicenceNumber());
		
		for(int i = 0; i < 10; i++){
			Invoice invoice = invoices.get(i);
			Invoice invoiceToSaved = new Invoice();
			
			invoice.setTLicence(license);
			invoiceToSaved.setTLicence(license);
			invoiceToSaved.setYearTo(new BigDecimal(1+i));
			invoiceToSaved.setBhpRate(invoice.getBhpRate());
			invoiceToSaved.setBhpAnnual(invoice.getBhpAnnual());
			invoiceToSaved.setBhpTotal(invoice.getBhpTotal());
			invoiceToSaved.setInvoiceType("1");
			if (getBhpHl() == null) {
				invoiceToSaved.setBhpPhl(auctionPrice);
			} else if (getBhpPhl() == null) {
				invoiceToSaved.setBhpHl(auctionPrice);

			}
//			invoiceToSaved.setBhpHl(auctionPrice);
			invoiceToSaved.setCreatedOn(new Date());
			
			invoiceToSaved.setInvCreatedDate(new Date());
			
			invoiceToSaved.setBhpAnnualPercent(invoice.getBhpAnnualPercent());
//			invoiceToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
			if(getUserLoginFromSession() != null){
				invoiceToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
			}else if(getUserLoginFromDatabase() != null){
				invoiceToSaved.setCreatedBy(getUserLoginFromDatabase().getUsername());
			}
			invoiceToSaved.setBhpCalcIndex(invoice.getBhpCalcIndex());
			invoiceToSaved.setPaymentDueDate(appBandMan.getPaymentDueDate());
			invoiceToSaved.setInvoiceStatus("D");
			
			if (i+1 == 1){
				invoiceToSaved.setSaveStatus("D");
				
				invoiceToSaved.setPaymentDueDate(datePayment.getTime());
				invoiceToSaved.setInvBeginDate(dateBegin.getTime());
				invoiceToSaved.setInvEndDate(dateEnd.getTime());
				invoiceToSaved.setBhpUpfrontFee(getBhpUpfrontFee());

			}else{
				invoiceToSaved.setSaveStatus("C");
				
				dateBegin.add(Calendar.YEAR,  1);
				dateEnd.add(Calendar.YEAR,  1);
				datePayment.add(Calendar.YEAR,  1);
				invoiceToSaved.setInvBeginDate(dateBegin.getTime());
				invoiceToSaved.setInvEndDate(dateEnd.getTime());
				invoiceToSaved.setPaymentDueDate(datePayment.getTime());

			}

			if( (i > 0) && (appBandMan.getIsBgAvailable().equals("Y")) ){
				Calendar bgDueDate = Calendar.getInstance();
				bgDueDate.setTime(getBgDueDate());
				bgDueDate.add(Calendar.YEAR, i - 1);
				
				BankGuarantee bg = bgList.get(i -1);
				BankGuarantee bgToSaved = new BankGuarantee();
				
				invoice.setBgTotal(bg.getBgValue());
				
				bgToSaved.setSaveStatus("C");
				bgToSaved.setReceivedStatus("3");
				
				bgToSaved.setBhpValue(invoice.getBhpAnnual());
				bgToSaved.setBgValue(bg.getBgValue());
				bgToSaved.setTInvoice(invoiceToSaved);
				bgToSaved.setTLicence(license);
				bgToSaved.setSubmitYearTo(bg.getSubmitYearTo());
				bgToSaved.setCalcIndex(bg.getCalcIndex());
				bgToSaved.setBhpPercent(bg.getBhpPercent());
				bgToSaved.setCreatedOn(new Date());
				bgToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
				
				bgToSaved.setSubmitDueDate(bgDueDate.getTime());
				bgToSaved.setBiRate(bg.getBiRate());
				bgListToSaved.add(bgToSaved);
			}
			
			invoiceListToSaved.add(invoiceToSaved);

		}

		license.setTInvoices(invoiceListToSaved);
		if (appBandMan.getIsBgAvailable().equals("Y")) {
			license.setBgAvailableStatus("Y");
			license.setBgDueDate(getBgDueDate());
			license.setTBankGuarantees(bgListToSaved);
		} else {
			license.setBgAvailableStatus("N");
			license.setTBankGuarantees(null);
		}
		
//		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());
		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageAnnualBhp());
		
		license.setVariableAnnualPercent(variable);
		
		getLicenseManager().save(license);
		
//		For saving file
		if ( (appBandMan.getKmDoc() != null) && (appBandMan.getKmFileName() != null)) {
			Date dateTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
			String timestamp = formatter.format(dateTime);
			Invoice invRef = invoiceListToSaved.get(0);
			String fileName = appBandMan.getKmFileName();
			fileName = fileName.replaceAll("\\s+","_");
			fileName = fileName.replace("\\","_"); 
			fileName = fileName.replace("/","_");
			if(fileName.indexOf(".pdf") < 0){
				fileName = fileName + "pdf";
			}
			int index = fileName.indexOf("pdf") - 1;
			if(index > 0){
				fileName = fileName.substring(0, index) + timestamp + ".pdf";
			}
			FileOutputStream fileOuputStream;
			
			File dirTest = new File("webapps");
			if(!dirTest.exists()){
				dirTest.mkdir();
			}
			
			File dirTest2 = new File("webapps/SKM");
			if(!dirTest2.exists()){
				dirTest2.mkdir();
			}
			
			if (appBandMan.getKmDoc() != null) {
	
				try {
					fileOuputStream = new FileOutputStream(DIRECTORY_DOCUMENT
							+ fileName);
	
					fileOuputStream.write(appBandMan.getKmDoc());
					fileOuputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			DocumentUpload doc = new DocumentUpload();
		    doc.setDocDesc("Upload From Initial Invoice");
		    doc.setCreatedOn(new Date());
//		    doc.setCreatedBy(getUserLoginFromSession().getUserName());
		    if(getUserLoginFromSession() != null){
		    	doc.setCreatedBy(getUserLoginFromSession().getUserName());
			}else if(getUserLoginFromDatabase() != null){
				doc.setCreatedBy(getUserLoginFromDatabase().getUserName());
			}
			doc.setFileName(fileName);
			doc.setYearTo(invRef.getYearTo().intValue());
			doc.setLicenseNo(license.getLicenceNo());
			doc.setReferenceId(String.valueOf(invRef.getInvoiceId()));
			doc.setFileDir(DIRECTORY_DOCUMENT);
			doc.setDocType("1");
			doc.setvUploadId("IN-1" + invRef.getInvoiceId());
			getDocumentUploadManager().saveDocument(doc);
		}
		
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information"));
//		infoPageCommand.addMessage(getText("licenseInformation.information",
//				new Object[] { license.getTLicenceId(), license.getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		//Save to audit log
		getUserManager().saveInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "draft", appBandMan.getLicenceNumber(), null, appBandMan.getBhpMethod());

		cycle.activate(infoPage);
		
	}
	
	public void doSubmit(IRequestCycle cycle){
		
		String validationMsg = calculateValidation();
		
		if(validationMsg != null){
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String checkLicense = checkLicense();
		if(checkLicense != null){
			addError(getDelegate(), "errorShadow", checkLicense, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		Calendar dateBegin = Calendar.getInstance();
		dateBegin.setTime(appBandMan.getCurrentBeginDate());
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.setTime(appBandMan.getCurrentEndDate());
		Calendar datePayment = Calendar.getInstance();
		datePayment.setTime(appBandMan.getPaymentDueDate());

		List<Invoice> invoices = (List) getFields().get("INVOICE_LIST_VARIETY");
		
		List<BankGuarantee> bgList = (List) getFields().get("BG_LIST");
		
		List<Invoice> invoiceListToSaved = new ArrayList<Invoice>();
		List<BankGuarantee> bgListToSaved = new ArrayList<BankGuarantee>();
		
		License license = new License();
		license.setLicenceNo(appBandMan.getLicenceNumber());
		license.setBhpMethod(appBandMan.getBhpMethod());
		license.setClientName(appBandMan.getClientCompany());
		license.setClientNo(new BigDecimal(appBandMan.getClientNumber()));
		license.setKmNo(appBandMan.getKmNo());
		if(appBandMan.getZoneNo() != null){
			license.setZoneNo(appBandMan.getZoneNo().toString());
		}
		license.setZoneName(appBandMan.getZoneName());
		license.setKmDate(appBandMan.getKmDate());
		license.setLicenceBeginDate(appBandMan.getLiBeginDate());
		license.setLicenceEndDate(appBandMan.getLiEndDate());
		license.setCurrentBeginDate(appBandMan.getCurrentBeginDate());
		license.setCurrentEndDate(appBandMan.getCurrentEndDate());
		license.setFreqTMin(appBandMan.getFreqMin());
		license.setFreqTMax(appBandMan.getFreqMax());
		license.setFreqRMin(appBandMan.getFreqMinR());
		license.setFreqRMax(appBandMan.getFreqMaxR());
		license.setPaymentType(appBandMan.getBhpPaymentType());
		license.setServiceId(appBandMan.getSvId());
		license.setSubserviceId(appBandMan.getSsId());
		
		license.setLicenceStatus("S");
		
		license.setCreatedOn(new Date());
//		license.setCreatedBy(getUserLoginFromSession().getUsername());
		if(getUserLoginFromSession() != null){
			license.setCreatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			license.setCreatedBy(getUserLoginFromSession().getUsername());
		}
		license.setPaymentDueDate(appBandMan.getPaymentDueDate());
		
		BigDecimal auctionPrice = null;

		if (getBhpHl() == null) {
			auctionPrice = getBhpPhl();
		} else if (getBhpPhl() == null) {
			auctionPrice = getBhpHl();

		}
		
//		String invoiceNumber = getInvoiceManager().generateInvoiceNumber(appBandMan.getLicenceNumber());
		
		Map invoiceMap = getInvoiceManager().generateInvoice(appBandMan.getLicenceNumber(), new BigDecimal(getBhpValueRadioFreq()), appBandMan.getPaymentDueDate());
		
		if( (invoiceMap.get("invoiceNo") == null) || (invoiceMap.get("letterID") == null) || (invoiceMap.get("invoiceErrorCode") == null)){
			addError(getDelegate(), "errorShadow",  getText("initialInvoice.license.callStoredProchedure.failed", new Object[] {invoiceMap.get("invoiceNo"),
					invoiceMap.get("letterID"), invoiceMap.get("invoiceErrorCode")}), ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String invoiceNO = invoiceMap.get("invoiceNo").toString();
		String letterId = invoiceMap.get("letterID").toString();
		String errorCode = invoiceMap.get("invoiceErrorCode").toString();
		
		System.out.println("GENERATED INVOICE NUMBER = "+ invoiceNO);
		System.out.println("GENERATED  LETTER ID = "+ letterId);
		System.out.println("GENERATED ERROR CODE = "+ errorCode);
		
		if (letterId == null){
			addError(getDelegate(), "errorShadow", getText("initialInvoice.license.letterCode.empty"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String errorCodeMessage = checkErrorCode(errorCode);
		
		if(errorCodeMessage != null){
			addError(getDelegate(), "errorShadow", errorCodeMessage,
					ValidationConstraint.CONSISTENCY);
			return;
		}
		
		BigDecimal letterID = new BigDecimal(letterId);
				
		for(int i = 0; i < 10; i++){
			Invoice invoice = invoices.get(i);
			Invoice invoiceToSaved = new Invoice();
			
			invoice.setTLicence(license);
			invoiceToSaved.setTLicence(license);
			invoiceToSaved.setYearTo(new BigDecimal(1+i));
			invoiceToSaved.setInvoiceType("1");
			invoiceToSaved.setBhpRate(invoice.getBhpRate());
			invoiceToSaved.setBhpCalcIndex(invoice.getBhpAnnualPercent());
			invoiceToSaved.setBhpAnnual(invoice.getBhpAnnual());
			invoiceToSaved.setInvCreatedDate(new Date());
			invoiceToSaved.setBhpTotal(invoice.getBhpTotal());
			if (getBhpHl() == null) {
				invoiceToSaved.setBhpPhl(auctionPrice);
			} else if (getBhpPhl() == null) {
				invoiceToSaved.setBhpHl(auctionPrice);
			}
//			invoiceToSaved.setBhpHl(auctionPrice);
			invoiceToSaved.setCreatedOn(new Date());
			invoiceToSaved.setBhpAnnualPercent(invoice.getBhpAnnualPercent());
//			invoiceToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
			if(getUserLoginFromSession() != null){
				invoiceToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
			}else if(getUserLoginFromDatabase() != null){
				invoiceToSaved.setCreatedBy(getUserLoginFromDatabase().getUsername());
			}
			invoiceToSaved.setBhpCalcIndex(invoice.getBhpCalcIndex());
			invoiceToSaved.setPaymentDueDate(appBandMan.getPaymentDueDate());
			
			if (i+1 == 1){
				invoiceToSaved.setSaveStatus("S");
				invoiceToSaved.setInvoiceNo(invoiceNO);
				invoiceToSaved.setPaymentDueDate(datePayment.getTime());
				invoiceToSaved.setInvBeginDate(dateBegin.getTime());
				invoiceToSaved.setInvEndDate(dateEnd.getTime());
				invoiceToSaved.setBhpUpfrontFee(getBhpUpfrontFee());
				invoiceToSaved.setInvoiceStatus("U");
				invoiceToSaved.setLetterID(letterID);
			}else if(i+1 == 2){
				dateBegin.add(Calendar.YEAR,  1);
				dateEnd.add(Calendar.YEAR,  1);
				datePayment.add(Calendar.YEAR,  1);
				
				invoiceToSaved.setInvBeginDate(dateBegin.getTime());
				invoiceToSaved.setInvEndDate(dateEnd.getTime());
				invoiceToSaved.setPaymentDueDate(datePayment.getTime());
				invoiceToSaved.setInvoiceStatus("D");
				invoiceToSaved.setSaveStatus("D");

			}else{
				dateBegin.add(Calendar.YEAR,  1);
				dateEnd.add(Calendar.YEAR,  1);
				datePayment.add(Calendar.YEAR,  1);
				
				invoiceToSaved.setInvBeginDate(dateBegin.getTime());
				invoiceToSaved.setInvEndDate(dateEnd.getTime());
				invoiceToSaved.setPaymentDueDate(datePayment.getTime());
				invoiceToSaved.setInvoiceStatus("D");
				invoiceToSaved.setSaveStatus("C");

			}

			if( (i > 0) && (appBandMan.getIsBgAvailable().equals("Y")) ){
				Calendar bgDueDate = Calendar.getInstance();
				bgDueDate.setTime(getBgDueDate());
				bgDueDate.add(Calendar.YEAR, i - 1);
				
				BankGuarantee bg = bgList.get(i - 1);
				BankGuarantee bgToSaved = new BankGuarantee();
				
				invoice.setBgTotal(bg.getBgValue());
				
				bgToSaved.setBhpValue(invoice.getBhpAnnual());
				bgToSaved.setBgValue(bg.getBgValue());
				bgToSaved.setTLicence(license);
				bgToSaved.setSubmitYearTo(bg.getSubmitYearTo());
				bgToSaved.setCreatedOn(new Date());
				bgToSaved.setCreatedBy(getUserLoginFromSession().getUsername());
				bgToSaved.setSubmitDueDate(bgDueDate.getTime());
				
				bgToSaved.setBgValueDiff(new BigDecimal(0));
				bgToSaved.setBgValueSubmitted(new BigDecimal(0));
				bgToSaved.setClaimValue(new BigDecimal(0));

				if(i==1){
					bgToSaved.setSaveStatus("D");
					bgToSaved.setReceivedStatus("0");
//					bgToSaved.setInvoiceNo(invoiceNO);
					bgToSaved.setTInvoice(invoiceToSaved);
					bgToSaved.setBiRate(bg.getBiRate());
					bgToSaved.setCalcIndex(bg.getCalcIndex());
					bgToSaved.setBhpPercent(bg.getBhpPercent());
				}else{
					bgToSaved.setSaveStatus("C");
					bgToSaved.setReceivedStatus("3");
					bgToSaved.setTInvoice(invoiceToSaved);
					bgToSaved.setBhpPercent(bg.getBhpPercent());
					bgToSaved.setBiRate(bg.getBiRate());
					bgToSaved.setCalcIndex(bg.getCalcIndex());
				}
				bgListToSaved.add(bgToSaved);
			}
			
			invoiceListToSaved.add(invoiceToSaved);

		}

		license.setTInvoices(invoiceListToSaved);
		if (appBandMan.getIsBgAvailable().equals("Y")) {
			license.setBgAvailableStatus("Y");
			license.setBgDueDate(getBgDueDate());
			license.setTBankGuarantees(bgListToSaved);
		} else {
			license.setBgAvailableStatus("N");
			license.setTBankGuarantees(null);
		}
		
		VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageAnnualBhp());
		license.setVariableAnnualPercent(variable);
		
		getLicenseManager().save(license);
		
//		For saving file
		if ( (appBandMan.getKmDoc() != null) && (appBandMan.getKmFileName() != null)) {
			Date dateTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
			String timestamp = formatter.format(dateTime);
			Invoice invRef = invoiceListToSaved.get(0);
			String fileName = appBandMan.getKmFileName();
			fileName = fileName.replaceAll("\\s+","_");
			if(fileName.indexOf("pdf") < 0){
				fileName = fileName + ".pdf";
			}
			int index = fileName.indexOf("pdf") - 1;
			if(index > 0){
				fileName = fileName.substring(0, index) + timestamp + ".pdf";
			}
			FileOutputStream fileOuputStream;
			
			File dirTest = new File("webapps");
			if(!dirTest.exists()){
				dirTest.mkdir();
			}
			
			File dirTest2 = new File("webapps/SKM");
			if(!dirTest2.exists()){
				dirTest2.mkdir();
			}
			
			if (appBandMan.getKmDoc() != null) {
	
				try {
					fileOuputStream = new FileOutputStream(DIRECTORY_DOCUMENT
							+ fileName);
	
					fileOuputStream.write(appBandMan.getKmDoc());
					fileOuputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			DocumentUpload doc = new DocumentUpload();
		    doc.setDocDesc("Upload From Initial Invoice");
		    doc.setCreatedOn(new Date());
//		    doc.setCreatedBy(getUserLoginFromSession().getUserName());
		    if(getUserLoginFromSession() != null){
		    	doc.setCreatedBy(getUserLoginFromSession().getUserName());
			}else if(getUserLoginFromDatabase() != null){
				doc.setCreatedBy(getUserLoginFromDatabase().getUserName());
			}
			doc.setFileName(fileName);
			doc.setYearTo(invRef.getYearTo().intValue());
			doc.setLicenseNo(license.getLicenceNo());
			doc.setReferenceId(String.valueOf(invRef.getInvoiceId()));
			doc.setFileDir(DIRECTORY_DOCUMENT);
			doc.setDocType("1");
			doc.setvUploadId("IN-1" + invRef.getInvoiceId());
			getDocumentUploadManager().saveDocument(doc);
		}
		
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information"));
//		infoPageCommand.addMessage(getText("licenseInformation.information",
//				new Object[] { license.getTLicenceId(), license.getClientName() }));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		
		//Save to audit log
		getUserManager().saveInvoice(initUserLoginFromDatabase(), getRequest().getRemoteHost(), "submit", appBandMan.getLicenceNumber(), invoiceNO, appBandMan.getBhpMethod());

		cycle.activate(infoPage);

	}
	
	private String generalValidation(){
		
		String errorMessage = null;
		
		if(getPercentageBHP() == null){
			errorMessage = getText("initial.invoice.empty.persentaseBhp");
		}else if(getBhpHl() == null && getBhpPhl() == null){
			errorMessage = getText("initial.invoice.empty.nilaiLelang");
		}else if(getBhpUpfrontFee() == null){
			errorMessage = getText("initial.invoice.empty.upFrontFee");
		}
		
		return errorMessage;
	}
	
	public void viewSkm(IRequestCycle cycle){
		if(appBandMan.getKmDoc() != null){
			PdfService.setPdf(appBandMan.getKmDoc());
			cycle.sendRedirect("./pdf.svc?imageId=" + appBandMan.getKmFileName());
		}else{
			cycle.sendRedirect("./documentNotFound.html");
		}
	}
	
	public String showPdf(){
		if(appBandMan.getKmDoc() != null){
			PdfService.setPdf(appBandMan.getKmDoc());
			
			String url = "./pdf.svc?imageId=" + appBandMan.getKmFileName();
			
			return url;
		}else{
			String url = "./documentNotFound.html";
			return url;
		}
	}
	
	public String doViewKmBiRate(){

		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		
		File dir = new File("testPRihakoNurukat");
		if(!dir.exists()){
			dir.mkdir();
		}
		
//		check if there's no active bi rate
		if(rateList.size() > 0){
			int yearActiveBiRate = rateList.get(0).getRateYear().intValue();
			int invYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(appBandMan.getLiBeginDate()));
//			VariableAnnualRate biRate = rateList.get(0);
			VariableAnnualRate biRate = null;
			if(invYear < yearActiveBiRate){
				VariableAnnualRate rateTemp = getVariableAnnualRateManager().findByYear( new BigDecimal(String.valueOf(invYear)) );
				biRate = rateTemp;
			}else{
				biRate = rateList.get(0);
			}
			
			DocumentUpload docUp = getDocumentUploadManager().findBiRateDocument(String.valueOf(biRate.getAnnualRateId()), "BI-2" + String.valueOf(biRate.getAnnualRateId()));
			if(docUp != null){
				File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
				if(downloadFile.exists()){
					String url = "./document.svc?imageId=" + docUp.getFileName();
					return url;   
				}else{
					String url = "./documentNotFound.html";
					return url; 
				} 
			}else{
				String url = "./documentNotFound.html";
				return url; 
			}
		}else{
			String url = "./documentNotFound.html";
			return url; 
		}
		
	}
	
	public void doCancel(IRequestCycle cycle) {
		InitialInvoiceSearch initialinvoiceSearch = (InitialInvoiceSearch) cycle
				.getPage("initialInvoiceSearch");
		getFields().remove("INVOICE_LIST_VARIETY");
		
		getFields().put("APPLICATION_LIST", (List<ApplicationBandwidth>) getFields().get("APPLICATION_LIST_IN_INITIAL_VR"));
		
		getFields().put("criteria", getCriteria());
		getFields().put("criteriaSearch", getCriteriaSearch());
		getFields().put("initialPage", "yes");
		initialinvoiceSearch.setCriteria(getCriteria());
		
		if(getCriteria().equalsIgnoreCase("clientName")){
			initialinvoiceSearch.setClientName(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("clientNo")){
			initialinvoiceSearch.setClientNo(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("bhpMethod")){
			initialinvoiceSearch.setBhpMethod(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("noApp")){
			initialinvoiceSearch.setLicenceNumber(getCriteriaSearch());
		}

		cycle.activate(initialinvoiceSearch);
	}
	
	public void doReset(){
		setEnableInput("Y");
		setShowTable("N");
		setIpsfrYearOne(null);
		setBhpValueRadioFreq(null);
		if(appBandMan.getIsBgAvailable().equalsIgnoreCase("Y")){
			setBgYear2(null);
		}
	}
	
	private String calculateValidation(){
		String errorMessage = null;
		
		if(getEnableInput().equalsIgnoreCase("Y")){
			errorMessage = getText("initial.invoice.calculate.notYet");
		}
		
		if(errorMessage == null){
			errorMessage = checkBgDueDate();
		}else{
			if(checkBgDueDate() != null)
				errorMessage += ", " + checkBgDueDate();
		}
		
		return errorMessage;
	}
	
	public String isThereIsActiveBiRate(){
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		String errorMessage = null;
		if(rateList.size() < 1){
			errorMessage = getText("initial.invoice.notActive.biRate");
		}

		return errorMessage;
	}
	
	public String checkBgDueDate(){
		String error = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		if(appBandMan.getIsBgAvailable() != null && appBandMan.getIsBgAvailable().equalsIgnoreCase("Y")){
			if (getBgDueDate() == null) {
				error =  getText("initialInvoice.license.empty.bgDueDate");
				System.out.println("Test" +error);
			}
			
			else if(getBgDueDate().before(cal.getTime())){
				error =  getText("initialInvoice.license.lessThanNow.bgDueDate");
			}
		}
		
		return error;
	}
	
	public String checkLicense(){
		String errorMessage = null;
		
		if(appBandMan.getLicenceNumber() == null){
			String error =  getText("initialInvoice.license.empty.licenseNumber");
			System.out.println("Test" + error);
			errorMessage = error;
			
		}
		
		if(appBandMan.getKmNo() == null){
			String error =  getText("initialInvoice.license.empty.kmNo");
			System.out.println("Test" +error);
			
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getBhpMethod()== null){
			String error =  getText("initialInvoice.license.empty.bhpMethod");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getClientNumber() == null){
			String error =  getText("initialInvoice.license.empty.clientNumber");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getCurrentBeginDate() == null){
			
			String error =  getText("initialInvoice.license.empty.currentBeginDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getCurrentEndDate() == null){
			String error =  getText("initialInvoice.license.empty.currentEndDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getLiBeginDate() == null){
			String error =  getText("initialInvoice.license.empty.licBeginDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getLiEndDate() == null){
			String error =  getText("initialInvoice.license.empty.licEndDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getPaymentDueDate() == null){
			String error =  getText("initialInvoice.license.empty.paymentDueDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getIsBgAvailable() == null){
			String error =   getText("initialInvoice.license.empty.isBgAvailable");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getSvId() == null){
			String error =   getText("initialInvoice.license.empty.svid");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getSsId() == null){
			String error =  getText("initialInvoice.license.empty.ssid");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getKmDoc() == null){
			String error =   getText("initialInvoice.license.empty.kmDoc");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getKmFileName() ==  null){
			String error =  getText("initialInvoice.license.empty.kmFileName");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getKmDate() == null){
			String error =  getText("initialInvoice.license.empty.kmDate");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getBhpPaymentType() == null){
			String error =  getText("initialInvoice.license.empty.paymentType");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getStatusDb().intValue() == 0){
			String error =  getText("initialInvoice.license.empty.statusDb");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(appBandMan.getClientCompany() == null){
			String error =  getText("initialInvoice.license.empty.clientCompany");
			System.out.println("Test" +error);
			if(errorMessage == null){
				errorMessage = error;
			}else{
				errorMessage =  errorMessage + ", " + error;
			}
		}
		
		if(errorMessage != null){
			String opening = getText("initialInvoice.license.opening");
			String closing = getText("initialInvoice.license.closing");
			return opening + " " + errorMessage + closing;
		}else{
			return errorMessage;
		}
	}
	
	public String checkErrorCode(String errorCode){
		String errorMessage = null;
		
		if (errorCode == null) {
			errorMessage = getText("initialInvoice.license.errorCode.empty");
			
		} else if (errorCode.equals("P1")) {
			errorMessage = getText("initialInvoice.license.errorCode.p1");
			
		} else if (errorCode.equals("P2")) {
			errorMessage = getText("initialInvoice.license.errorCode.p2");

		} else if (errorCode.equals("P3")) {
			errorMessage = getText("initialInvoice.license.errorCode.p3");

		} else if (errorCode.equals("P4")) {
			errorMessage = getText("initialInvoice.license.errorCode.p4");

		} else if (errorCode.equals("P5")) {
			errorMessage = getText("initialInvoice.license.errorCode.p5");

		} else if (errorCode.equals("P6")) {
			errorMessage = getText("initialInvoice.license.errorCode.p6");

		} else if (errorCode.equals("P7")) {
			errorMessage = getText("initialInvoice.license.errorCode.p7");

		} else if (errorCode.equals("P8")) {
			errorMessage = getText("initialInvoice.license.errorCode.p8");

		} else if (errorCode.equals("P9")) {
			errorMessage = getText("initialInvoice.license.errorCode.p9");

		} else if (errorCode.equals("P10")) {
			errorMessage = getText("initialInvoice.license.errorCode.p10");

		} else if (errorCode.equals("P11")) {
			errorMessage = getText("initialInvoice.license.errorCode.p11");

		} else if (errorCode.equals("P12")) {
			errorMessage = getText("initialInvoice.license.errorCode.p12");

		}
		
		if(errorMessage != null){
			String opening = getText("initialInvoice.license.opening");
			String closing = getText("initialInvoice.license.closing");
			return opening + " " + errorMessage + closing;
		}else{
			return errorMessage;
		}
	}
	
}
