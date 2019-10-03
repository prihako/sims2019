package com.balicamp.webapp.action.operational;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.formula.functions.Goto;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.operational.ApplicationBandwidth;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.DocumentLetter;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.service.operational.ApplicationBandwidthManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.PdfService;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.license.UploadHelper;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualPercentageBHPViewVariety;
import com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel.AnnualRateBHPView;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class ManageInvoiceVarietyRateEdit extends AdminBasePage implements
		PageBeginRenderListener, PageEndRenderListener {
	
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
	
	public abstract void setInvCreatedDateStr(String s);
	
	public abstract String getInvCreatedDateStr();
	
	public abstract void setPaymentDateStr(String s);
	
	public abstract String getPaymentDateStr();
	
	public abstract void setBgSubmitDueDate(String s);
	
	public abstract String getBgSubmitDueDate();
	
	private static final String DIRECTORY_DOCUMENT = "webapps/SKM/";
	
	private PropertySelectionModel percentageBHPModel;
	
	private License license;
	
	private Invoice invoice;
	
	private ApplicationBandwidth appBandMan = null;
	
	private BigDecimal bhpUpfrontFee;
	
	private IUploadFile fileSkmIpsfr;
	
	private String filename;
	
	public void setApplicationBandwidth(ApplicationBandwidth appBandMan){
		this.appBandMan = appBandMan;
	}
	
	public ApplicationBandwidth getApplicationBandwidth(){
		return appBandMan;
	}
	
	public void setLicense(License license){
		this.license = license;
	}
	
	public License getLicense(){
		return license;
	}
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public IUploadFile getFile() { 
	    return fileSkmIpsfr; 
	}
	    
	public void setFile(IUploadFile value) {
		fileSkmIpsfr = value;
	}
	
	public void setFilename(String fileName){
		this.filename = filename;
	}
	
	public String getFilename() { 
        if (fileSkmIpsfr != null) {
        	setFilename(fileSkmIpsfr.getFileName());
            return fileSkmIpsfr.getFileName();
        } else {
            return "";   
        }        
    }
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
	@InjectObject("spring:variableManager")
	public abstract VariableManager getVariableManager();

	@InjectObject("spring:variableDetailManager")
	public abstract VariableDetailManager getVariableDetailManager();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();
	
	//For table
	public abstract Invoice getRow();
	
//	For Table
	public abstract BankGuarantee getBg();
	
	public abstract void setPercentageBHP(String bhp);
	
	public abstract String getPercentageBHP();
	
	public abstract void setInvBeginYear(String year);
	public abstract String getInvBeginYear();
	
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
	
	private String percentageAnnualBhp;
	
	public void setPercentageAnnualBhp(String annualBhp){
		percentageAnnualBhp = annualBhp;
	}
	
	public String getPercentageAnnualBhp(){
		return percentageAnnualBhp;
	}
	
	private String biRate;
	
	public void setBiRate(String biRate){
		this.biRate = biRate;
	}
	
	public String getBiRate(){
		return biRate;
	}
	
	public IPropertySelectionModel getPercentageBHPModel() {
		return percentageBHPModel;
	}
	
//	private BigDecimal hargaLelang;
//	
//	public void setHargaLelang(BigDecimal hl){
//		hargaLelang = hl;
//	}
//	
//	public BigDecimal getHargaLelang(){
//		return hargaLelang;
//	}
	
	public void setPercentageBHPModel(String year){
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		if( (String.valueOf(invoice.getYearTo()).equals("1")) && (invoice.getSaveStatus().equalsIgnoreCase("D")) ){
			List<VariableAnnualPercentage> list = getVariableManager().findAll();
			map.put(year, year);
			for (VariableAnnualPercentage variableAnnualPercentage : list) {
				if( !(variableAnnualPercentage.getKmNo().equalsIgnoreCase(year)) ){
					if(variableAnnualPercentage.getVariableStatus() == 1){
						map.put(variableAnnualPercentage.getKmNo(), variableAnnualPercentage.getKmNo());
					}
				}
			}
			setPercentageBHP(year);
		}else if(year != null){
			map.put(year, year);
		}else{
			map.put("", "");
		}
		
		percentageBHPModel = new PropertySelectionModel(getLocale(), map, false, false);
	}
	
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
	
	public List<Invoice> getInvoiceList() {
		List<Invoice> bhpList = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		return bhpList;
	}
	
	public List<BankGuarantee> getBgList() {
		List bhpList = (List) getObjectfromSession("BG_LIST_VARIETY");
		return bhpList;
	}
	
	public ManageInvoiceVarietyRateEdit(){
		
	}
	
	public abstract void setIpsfrYearOne(String ipsfr);
	
	public abstract String  getIpsfrYearOne();
	
	public abstract void setBhpValueRadioFreq(String value);
	
	public abstract String getBhpValueRadioFreq();
	
	public abstract void setBgYear2(String bg);
	
	public abstract String getBgYear2();
	
	public abstract void setIpsfrNo(String no);
	
	public abstract String getIpsfrNo();
	
	public abstract void setComment(String comment);
	
	public abstract String getComment();
	
	public void setBhpUpfrontFee(BigDecimal bhpUp){
		bhpUpfrontFee = bhpUp;
	}
	
	public BigDecimal getBhpUpfrontFee(){
		return bhpUpfrontFee;
	}
	
	public abstract void setPaymentType(String type);
	
	public abstract String getPaymentType();
	
	public abstract void setBhpMethod(String bhp);
	
	public abstract String getBhpMethod();
	
	public abstract void setInvoiceStatus(String status);
	
	public abstract String getInvoiceStatus();
	
	public abstract void setInvoiceType(String status);
	
	public abstract String getInvoiceType();

	public abstract void setCriteria(String crt);
	
	public abstract String getCriteria();
	
	public abstract void setCriteriaSearch(String crit);
	
	public abstract String getCriteriaSearch();
	
	public abstract void setInvoiceStatusCache(String stat);
	
	public abstract String getInvoiceStatusCache();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		if(license.getKmDate() != null){
			setKmDateStr(dateFormat.format(license.getKmDate()));
		}
		
		if(license.getLicenceBeginDate() != null){
			setLicBeginDateStr(dateFormat.format(license.getLicenceBeginDate()));
		}
		
		if(license.getLicenceEndDate() != null){
			setLicEndDateStr(dateFormat.format(license.getLicenceEndDate()));
		}
		
		if(invoice.getInvBeginDate() != null){
			setCurBeginDateStr(dateFormat.format(invoice.getInvBeginDate()));
		}
		
		if(invoice.getInvEndDate() != null){
			setCurEndDateStr(dateFormat.format(invoice.getInvEndDate()));
		}
		
		if(invoice.getPaymentDueDate() != null){
			setPaymentDueDateStr(dateFormat.format(invoice.getPaymentDueDate()));
		}
		
		if(invoice.getInvCreatedDate() != null){
			setInvCreatedDateStr(dateFormat.format(invoice.getInvCreatedDate()));
		}
		
		if(invoice.getPaymentDate() != null){
			setPaymentDateStr(dateFormat.format(invoice.getPaymentDate()));
		}
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(license.getPaymentType().equalsIgnoreCase("FP")){
				setPaymentType("Full Payment");
			}
			
			if(license.getBhpMethod().equalsIgnoreCase("FR")){
				setBhpMethod("Flat BHP");
			}else if(license.getBhpMethod().equalsIgnoreCase("VR")){
				setBhpMethod("Variety Rate");
			}else{
				setBhpMethod("Conversion");
			}
			
			if(Integer.valueOf(invoice.getInvoiceType()) == 1){
				setInvoiceType("Pokok");
			}else if(Integer.valueOf(invoice.getInvoiceType()) == 2){
				setInvoiceType("Denda");
			}
			
			if(invoice.getInvoiceStatus().equalsIgnoreCase("D")){
				setInvoiceStatus("Draft");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("U")){
				setInvoiceStatus("Unpaid");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("P")){
				setInvoiceStatus("Paid");
			}else if(invoice.getInvoiceStatus().equalsIgnoreCase("C")){
				setInvoiceStatus("Cancel");
			}
			
			if(getPercentageBHP() != null){
				getFields().put("percentage", getPercentageBHP());
			}else{
				getFields().put("percentage", null);
			}
			
		}
		
	}

	@Override
	public void pageEndRender(PageEvent pageEvent) {
		super.pageEndRender(pageEvent);
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(license.getPaymentType().equalsIgnoreCase("FP")){
				setPaymentType("Full Payment");
			}
			
			if(license.getBhpMethod().equalsIgnoreCase("FR")){
				setBhpMethod("Flat BHP");
			}else if(license.getBhpMethod().equalsIgnoreCase("VR")){
				setBhpMethod("Variety Rate");
			}else{
				setBhpMethod("Conversion");
			}
		}
		
	}
	
	public void hitung(){

//		set hidden fields
		setPercentageAnnualBhp(getPercentageBHP());
		
		String validationMsg 	= generalValidation();
		
//		validation input
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
//		Untuk cek, apakah bi rate tahun bersangkutan aktif atau tidak, namun untuk tahun pertama tidak perlu,
		if(invoice.getYearTo().intValue() != 1){
			String validationBiRate = checkBiRate();
	//		validation bi rate
			if(validationBiRate != null){
				addError(getDelegate(), "errorShadow", validationBiRate, ValidationConstraint.CONSISTENCY);
			}
		}
		
		//VAlidation for invoice comment, because always pass, so i hardcode this
		if(invoice.getYearTo().intValue() > 1){
			if( (invoice.getInvoiceComment() == null)){
				String errorMessage = getText("initial.invoice.empty.comment");
				if(errorMessage != null){
					addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
					return;
				}
			}
		}
		
		//Filename validation,  because always pass, so i hardcode this
		if(getFile() != null){
			String fileName = getFile().getFileName();
			int length = fileName.length();
			String subString = fileName.substring(length-4, length);
			if( !(subString.equals(".pdf")) ){
				String errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.notPdf");
				addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
				return;
			}
		}
		
//		Replace invoice
		List<Invoice> invoiceListFromTable = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		int indexToReplace = Integer.valueOf(String.valueOf(invoice.getYearTo().subtract(new BigDecimal("1"))));
		invoiceListFromTable.remove(indexToReplace);
		invoiceListFromTable.add(indexToReplace, invoice);
		
		for(Invoice inv: invoiceListFromTable){
			System.out.println(inv.getYearTo() + " = " + inv.getMonthTo() );
		}
		
		BigDecimal auctionPrice = null;
		
//		jika yearTo = 1, berarti bhpHL bisa d edit, tpi jika yearTo > 1, bhpHL tidak bisa dedit
		if( (String.valueOf(invoice.getYearTo())).equals("1") ) {
			//tambahan
			if(invoice.getBhpPhl() ==  null){
				auctionPrice = invoice.getBhpHl();
			}else{
				auctionPrice = invoice.getBhpPhl();
			}
//			auctionPrice = invoice.getBhpHl();
//			Kalo tahun 1, jika harga lelang (hl) diubah harus diset ke semua tahun
			for(int i = 0; i < 10; i++){
				Invoice invTemp = invoiceListFromTable.get(i);
				
				//tambahan
				if(invoice.getBhpPhl() ==  null){
					invTemp.setBhpHl(auctionPrice);
					invTemp.setBhpPhl(null);
				}else{
					invTemp.setBhpPhl(auctionPrice);
					invTemp.setBhpHl(null);
				}
//				invTemp.setBhpHl(auctionPrice);
			}
//			check Kalo persentase bhp tahunan berubah, set ke semua tahun
			if( !(license.getVariableAnnualPercent().getKmNo().equalsIgnoreCase(getPercentageBHP())) ){
				VariableAnnualPercentage variableAnnual = getVariableManager().findByKmNo(getPercentageBHP());
				List<VariableAnnualPercentageDetail> variableAnnualDetail = getVariableDetailManager().findByAnnualPercentId(variableAnnual.getAnnualPercentId());
				
				for(int i = 0; i < 10; i ++){
					Invoice invTemp = invoiceListFromTable.get(i);
					invTemp.setBhpAnnualPercent(variableAnnualDetail.get(i).getPercentage());
				}
				
//				cek, kalo pake bankGurantee, tabel bank guarantee harus d set juga annualPercentageBhp-nya, soalnya buat perhitungan bg di fungsi hitungBg()
				if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
//					Ingat bg hanya ada 9, yaitu dri tahun ke-2 sampai tahun ke-10, namun annualPercentage BHP-nya dri tahun 1 sampai 9
					List<BankGuarantee> listBg = (List<BankGuarantee>) getObjectfromSession("BG_LIST_VARIETY");
					for(int i = 0; i < 9; i++){
						BankGuarantee bgTemp = listBg.get(i);
						bgTemp.setBhpPercent(variableAnnualDetail.get(i+1).getPercentage());
					}
				}
				
				license.setVariableAnnualPercent(variableAnnual);
			}
			
//			Set bhp value, when there is change in bhpPhl or bhpHl
			if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
				List<BankGuarantee> listBg = (List<BankGuarantee>) getObjectfromSession("BG_LIST_VARIETY");
				for(int i = 0; i < 9; i++){
					BankGuarantee bgTemp = listBg.get(i);
					bgTemp.setBhpValue(auctionPrice);
				}
			}
			
		}else{
			//tambahan
			if(getSelectPayment().equalsIgnoreCase("HL")){
				auctionPrice = invoice.getBhpHl();
			}else{
				auctionPrice = invoice.getBhpPhl();
			}
			//End
//			auctionPrice = invoice.getBhpPhl();
			Integer index = (invoice.getYearTo().intValue()) - 1;
			for(int i = index; i < 10; i ++){
				Invoice invTemp = invoiceListFromTable.get(i);
//				if(getSelectPayment().equalsIgnoreCase("HL")){
//					invTemp.setBhpHl(auctionPrice);
//				}else{
//					invTemp.setBhpPhl(auctionPrice);
//				}
				
				if(getSelectPayment().equalsIgnoreCase("PHL")){
					invTemp.setBhpPhl(auctionPrice);
				}
			}
			
//			set bank guarantee bhpValue
			if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
//				Ingat bg hanya ada 9, yaitu dri tahun ke-2 sampai tahun ke-10, namun annualPercentage BHP-nya dri tahun 1 sampai 9
				List<BankGuarantee> listBg = (List<BankGuarantee>) getObjectfromSession("BG_LIST_VARIETY");
				Integer indexBg = (invoice.getYearTo().intValue()) - 1;
				for(int i = indexBg; i < 9; i++){
					BankGuarantee bgTemp = listBg.get(i);
					bgTemp.setBhpValue(auctionPrice);
				}
			}
			
		}
		
//		Find active bi rate
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		VariableAnnualRate rate = rateList.get(0);
		Integer yearActive = rate.getRateYear().intValue();
		Integer invYear = Integer.valueOf(invoice.getYears());
		BigDecimal biRate = null;
		
		if(invYear >= yearActive){
			biRate = rate.getRateValue();
		}else{
			VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
			biRate = rate2.getRateValue();
		}
		
		BigDecimal annualPercent = invoice.getBhpAnnualPercent().divide(new BigDecimal("100"));
		
		if(invoice.getYearTo().equals(new BigDecimal("1"))){
			BigDecimal bhpAnnual = auctionPrice.multiply(annualPercent);
			BigDecimal bhpTotal = invoice.getBhpUpfrontFee().add(bhpAnnual);
			invoice.setBhpAnnual(bhpAnnual.setScale(0,  BigDecimal.ROUND_HALF_UP));
			invoice.setBhpTotal(bhpTotal.setScale(0, BigDecimal.ROUND_HALF_UP));
			
//			calculate invoice next year with active bi Rate
			int indexNextYear = Integer.valueOf(String.valueOf(invoice.getYearTo()));
			Invoice invoiceNextYear = invoiceListFromTable.get(indexNextYear);
//			BigDecimal biRateActive = rate.getRateValue();
			BigDecimal biRateActive = null;
			if(invYear >= yearActive){
				biRateActive = rate.getRateValue();
			}else{
				VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()).add(new BigDecimal("1")));
				biRateActive = rate2.getRateValue();
			}
			BigDecimal multiplyNextYear = (biRateActive.divide(new BigDecimal("100"))).add(new BigDecimal("1"))
					.setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
			BigDecimal annualIpsfrNextYear =  auctionPrice.multiply(invoiceNextYear.getBhpAnnualPercent().divide(new BigDecimal("100"))).multiply(multiplyNextYear)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			invoiceNextYear.setBhpCalcIndex(multiplyNextYear);
			invoiceNextYear.setBhpRate(biRateActive);
			invoiceNextYear.setBhpAnnual(annualIpsfrNextYear.setScale(0, BigDecimal.ROUND_HALF_UP));
			invoiceNextYear.setBhpTotal(annualIpsfrNextYear.setScale(0, BigDecimal.ROUND_HALF_UP));
			
		}else{
			//-2 karena, tahun sekarang -1, jadi tahun sebelumnya adalah -2, karena dapat dari list, sedangkan argument adalah yearTo yg dimulai dri 1
			int indexPrevYear = Integer.valueOf(String.valueOf(invoice.getYearTo().subtract(new BigDecimal("2"))));
			Invoice invoicePrevYear = invoiceListFromTable.get(indexPrevYear);
			
			BigDecimal multiplyIndexPrevYear = invoicePrevYear.getBhpCalcIndex();
			if(multiplyIndexPrevYear == null){
				multiplyIndexPrevYear = new BigDecimal("1");
			}
			BigDecimal biRateNow = null;
			if(invYear >= yearActive){
				biRateNow = rate.getRateValue();
			}else{
				VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
				biRateNow = rate2.getRateValue();
			}
			BigDecimal multiplyIndexNow = (biRateNow.divide(new BigDecimal("100"))).add(new BigDecimal("1")).multiply(multiplyIndexPrevYear)
					.setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
			BigDecimal annualIpsfrNow =  auctionPrice.multiply(annualPercent).multiply(multiplyIndexNow).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Jika tahun kedua dst maka tidak usah di set upfront fee
			invoice.setBhpUpfrontFee(null);
			
			invoice.setBhpRate(biRateNow);
			invoice.setBhpCalcIndex(multiplyIndexNow);
			invoice.setBhpAnnual(annualIpsfrNow.setScale(0,  BigDecimal.ROUND_HALF_UP));
			invoice.setBhpTotal(annualIpsfrNow.setScale(0, BigDecimal.ROUND_HALF_UP));
			
			if(invoice.getYearTo().intValue() < 10){
	//			calculate invoice next year with active bi Rate
				int indexNextYear = Integer.valueOf(String.valueOf(invoice.getYearTo()));
				Invoice invoiceNextYear = invoiceListFromTable.get(indexNextYear);
//				BigDecimal biRateActive = rate.getRateValue();
				BigDecimal biRateActive = null;
				if(invYear >= yearActive){
					biRateActive = rate.getRateValue();
				}else{
					VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()).add(new BigDecimal("1")));
					biRateActive = rate2.getRateValue();
				}
				BigDecimal multiplyNextYear = ((biRateActive.divide(new BigDecimal("100"))).add(new BigDecimal("1"))).multiply(multiplyIndexNow)
						.setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
				BigDecimal annualIpsfrNextYear =  auctionPrice.multiply(invoiceNextYear.getBhpAnnualPercent().divide(new BigDecimal("100"))).multiply(multiplyNextYear)
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				invoiceNextYear.setBhpCalcIndex(multiplyNextYear);
				invoiceNextYear.setBhpRate(biRateActive);
				invoiceNextYear.setBhpAnnual(annualIpsfrNextYear.setScale(0, BigDecimal.ROUND_HALF_UP));
				invoiceNextYear.setBhpTotal(annualIpsfrNextYear.setScale(0, BigDecimal.ROUND_HALF_UP));
			}
			
		}

		saveObjectToSession(invoiceListFromTable, "INVOICE_LIST_VARIETY");
		
		setEnableInput("N");
		setShowTable("Y");
		
//		save document, hanya tahun ke-2 dan seterusnya yg bisa save document
		if(invoice.getYearTo().intValue() > 1){
			saveDocument();
		}
		
		if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
			hitungBg(auctionPrice);
		}
		
	}
	
	public void hitungBg(BigDecimal auctionPrice){
//		Find active bi rate
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		VariableAnnualRate rate = rateList.get(0);
//		BigDecimal biRate = rate.getRateValue().divide(new BigDecimal("100"));
		
		Integer yearActive = rate.getRateYear().intValue();
		Integer invYear = Integer.valueOf(invoice.getYears());
		BigDecimal biRate = null;
		if(invYear >= yearActive){
			biRate = rate.getRateValue();
		}else{
			VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
			biRate = rate2.getRateValue();
		}
		
		List<BankGuarantee> listBg = (List<BankGuarantee>) getObjectfromSession("BG_LIST_VARIETY");
		List<Invoice> invoiceList = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		
//		BigDecimal auctionPrice = null;
		BankGuarantee bgToEdit = null;
		BankGuarantee bgNextYear = null;
		BigDecimal multiplyIndexPrevYear = null;

//		Ingat, tahun pertama bg, tahun kesepulih tidak memiliki bg 
		if(invoice.getYearTo().intValue() == 1){
			bgToEdit = listBg.get(0);
			bgNextYear = listBg.get(1);
			multiplyIndexPrevYear = new BigDecimal("1");
//			auctionPrice = invoice.getBhpHl();
		}else{
			bgToEdit = listBg.get( (invoice.getYearTo().intValue()) - 1 );
			bgNextYear = listBg.get(invoice.getYearTo().intValue());
			BankGuarantee bgPrevYear = listBg.get( (invoice.getYearTo().intValue()) - 2 );
			multiplyIndexPrevYear = bgPrevYear.getCalcIndex();
//			auctionPrice = invoice.getBhpPhl();
		}
		
		BigDecimal biRateNow = null;
		if(invYear >= yearActive){
			biRateNow = rate.getRateValue().divide(new BigDecimal("100"));
		}else{
			VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
			biRateNow = rate2.getRateValue().divide(new BigDecimal("100"));;
		}
		
		BigDecimal multiplyIndexNow = (biRateNow.add(new BigDecimal("1"))).multiply(multiplyIndexPrevYear).setScale(10, BigDecimal.ROUND_HALF_UP)
				.stripTrailingZeros();
		BigDecimal annualPercent = bgToEdit.getBhpPercent().divide(new BigDecimal("100"));
		BigDecimal constPercent = new BigDecimal("1.02");
		BigDecimal bgValue = constPercent.multiply(annualPercent).multiply(multiplyIndexNow).multiply(auctionPrice);
		bgToEdit.setBgValue(bgValue.setScale(0,  BigDecimal.ROUND_HALF_UP));
		bgToEdit.setBhpValue(auctionPrice);
		bgToEdit.setBiRate(biRateNow.multiply(new BigDecimal("100")).stripTrailingZeros());
		bgToEdit.setCalcIndex(multiplyIndexNow);
		
		if(invoice.getYearTo().intValue() < 9){
			BigDecimal biRateNext = null;
			if(invYear >= yearActive){
				biRateNext = rate.getRateValue();
			}else{
				VariableAnnualRate rate2 = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()).add(new BigDecimal("1")));
				biRateNext = rate2.getRateValue();
			}
			BigDecimal biRateNextDivBy100 = biRateNext.divide(new BigDecimal("100"));
			BigDecimal multiplyIndexNextYear = (biRateNextDivBy100.add(new BigDecimal("1"))).multiply(multiplyIndexNow).setScale(10, BigDecimal.ROUND_HALF_UP)
					.stripTrailingZeros();
			BigDecimal annualPercentNextYear = bgNextYear.getBhpPercent().divide(new BigDecimal("100"));
			BigDecimal bgValueNextYear = constPercent.multiply(annualPercentNextYear).multiply(multiplyIndexNow).multiply(auctionPrice);
			bgNextYear.setBgValue(bgValueNextYear.setScale(0,  BigDecimal.ROUND_HALF_UP));
			bgNextYear.setBiRate(biRateNext);
			bgNextYear.setBhpValue(auctionPrice);
			bgNextYear.setCalcIndex(multiplyIndexNextYear);
		}
	}
	
	public void doDraft(IRequestCycle cycle){
		
		String validationMsg = calculateValidation();
		
		if(validationMsg != null){
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			addError(getDelegate(), "errorShadowHitung", "Anda belum melakukan perhitungan", ValidationConstraint.CONSISTENCY);
			return;
		}
		
		List<Invoice> invoiceListFromTable = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		List<BankGuarantee> listBgFromTable = (List<BankGuarantee>) getObjectfromSession("BG_LIST_VARIETY");
		String invoiceTarget = String.valueOf(invoice.getYearTo().subtract(new BigDecimal(1)));
		Invoice invoiceFromTable = invoiceListFromTable.get(Integer.valueOf(invoiceTarget));

		license.setUpdatedOn(new Date());
		if(getUserLoginFromSession() != null){
			license.setUpdatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			license.setUpdatedBy(getUserLoginFromDatabase().getUsername());
		}
		
		if(getUserLoginFromSession() != null){
			invoice.setUpdatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			invoice.setUpdatedBy(getUserLoginFromDatabase().getUsername());
		}
		invoice.setUpdatedOn(new Date());
		invoice.setInvoiceComment(invoice.getInvoiceComment());
		
		if( String.valueOf(invoice.getYearTo()).equals("1") ){
			license.setTInvoices(invoiceListFromTable);
//			This is cause we edit all the bankGurantee
			if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
				license.setTBankGuarantees(listBgFromTable);
			}
			getLicenseManager().save(license);
		}else{
			license.setTInvoices(invoiceListFromTable);
//			Cause we just edit one bankGuarantee, we just save one
			if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
//				int indexBgTarget = invoice.getYearTo().intValue() - 1;
//				getBankGuaranteeManager().save(listBgFromTable.get(indexBgTarget));
				license.setTBankGuarantees(listBgFromTable);
			}
			getLicenseManager().save(license);
		}
		
//		For Clear table in manageInvoiceSearch
		getFields().put("LICENSE_LIST", null);
		
		toManageInvoiceSearch(cycle, license);
	}
	
	public void doSubmit(IRequestCycle cycle){
		
		String validationMsg = calculateValidation();
		
		if(validationMsg != null){
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			addError(getDelegate(), "errorShadowHitung", "Anda belum melakukan perhitungan", ValidationConstraint.CONSISTENCY);
			return;
		}
		
		List<Invoice> invoiceListFromTable = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
		List<BankGuarantee> listBgFromTable = (List<BankGuarantee>) getObjectfromSession("BG_LIST_VARIETY");
		String invoiceTarget = String.valueOf(invoice.getYearTo().subtract(new BigDecimal(1)));
		Invoice invoiceFromTable = invoiceListFromTable.get(Integer.valueOf(invoiceTarget));

//		String invoiceNumber = getInvoiceManager().generateInvoiceNumber(license.getLicenceNo());
		
		Map invoiceMap = getInvoiceManager().generateInvoice(
				license.getLicenceNo(), invoice.getBhpTotal(), invoice.getPaymentDueDate());
		
		if( (invoiceMap.get("invoiceNo") == null) || (invoiceMap.get("letterID") == null) || (invoiceMap.get("invoiceErrorCode") == null)){
			addError(getDelegate(), "errorShadow", "Aplikasi yang dibuat tidak valid. Failed Call SPECTRA PLUS prochedure", ValidationConstraint.CONSISTENCY);
			return;
		}
		
		String invoiceNO = invoiceMap.get("invoiceNo").toString();
		String letterId = invoiceMap.get("letterID").toString();
		String errorCode = invoiceMap.get("invoiceErrorCode").toString();
		
		String errorCodeMessage = checkErrorCode(errorCode);
		
		if(errorCodeMessage != null){
			addError(getDelegate(), "errorShadow", errorCodeMessage,
					ValidationConstraint.CONSISTENCY);
			return;
		}

		System.out.println("GENERATED INVOICE NUMBER = "+ invoiceNO);
		System.out.println("GENERATED  LETTER ID = "+ letterId);
		System.out.println("GENERATED ERROR CODE = "+ errorCode);
		
		if(letterId == null){
			addError(getDelegate(), "errorShadow", "Letter ID Null",
					ValidationConstraint.CONSISTENCY);
			return;
		}
		 
		BigDecimal letterID = new BigDecimal(letterId);
		
		license.setUpdatedOn(new Date());
		if(getUserLoginFromSession() != null){
			license.setUpdatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			license.setUpdatedBy(getUserLoginFromDatabase().getUsername());
		}
		
		invoice.setSaveStatus("S");
		invoice.setInvoiceStatus("U");
		invoice.setInvoiceNo(invoiceNO);
		invoice.setLetterID(letterID);
		if(getUserLoginFromSession() != null){
			invoice.setUpdatedBy(getUserLoginFromSession().getUsername());
		}else if(getUserLoginFromDatabase() != null){
			invoice.setUpdatedBy(getUserLoginFromDatabase().getUsername());
		}
		invoice.setUpdatedOn(new Date());
		
		//tahun sekarang -1, jadi tahun berikutnya adalah 0 (tanpa dikurang), karena dapat dari list, sedangkan argument adalah yearTo yg dimulai dri 0
		int indexNextYear = Integer.valueOf(String.valueOf(invoice.getYearTo()));
		Invoice invoiceNextYear = invoiceListFromTable.get(indexNextYear);
		invoiceNextYear.setSaveStatus("D");
		
		if( String.valueOf(invoice.getYearTo()).equals("1") ){
			license.setTInvoices(invoiceListFromTable);
			license.setLicenceStatus("S");
//			This is cause we edit all the bankGurantee
			if(license.getBgAvailableStatus().equalsIgnoreCase("Y")){
				//Set save status and received date for first bank guarantee
				listBgFromTable.get(0).setSaveStatus("D");
				listBgFromTable.get(0).setReceivedStatus("0");
				license.setTBankGuarantees(listBgFromTable);
			}
			getLicenseManager().save(license);
		}else{
			getInvoiceManager().save(invoice);
//			Cause we just edit one bankGuarantee, we just save one
			int indexBgTarget = invoice.getYearTo().intValue() - 1;
			getBankGuaranteeManager().save(listBgFromTable.get(indexBgTarget));
		}
		
//		For Clear table in manageInvoiceSearch
		getFields().put("LICENSE_LIST", null);
		
		toManageInvoiceSearch(cycle, license);
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
			annualRateBHPEdit.setPageLocation("manageEdit");
			cycle.activate(annualRateBHPEdit);
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
	
	public String viewAnnualPercentageBhp(){
		if(getPercentageBHP() != null){
			VariableAnnualPercentage variable = getVariableManager().findByKmNo(getPercentageBHP());
			List<VariableAnnualPercentageDetail> variableDetail = getVariableDetailManager().findByAnnualPercentId(variable.getAnnualPercentId());
			saveObjectToSession(null, "ANNUAL_PERCENTAGE");
			saveObjectToSession(variable, "ANNUAL_PERCENTAGE");
			saveObjectToSession(null, "ANNUAL_PERCETAGE_DETAIL_LIST");
			saveObjectToSession(variableDetail, "ANNUAL_PERCETAGE_DETAIL_LIST");
		}
		
		return "annual_percentage_view()";
	}
	
//	public String viewBiRate(){
//		VariableAnnualRate rate = getVariableAnnualRateManager().findByYear(new BigDecimal(invoice.getYears()));
//		saveObjectToSession(rate, "BI_RATE_DETAIL_LIST");
//		return "bi_rate_view()";
//	}
	
	public void doBack(){
		
	}
	
//	public String doPrint() throws IOException{
//		if (invoice.getLetterID() != null) {
//			DocumentLetter letterDocument = getInvoiceManager().printLetterDocument(invoice.getLetterID());
//			PdfService.setPdf(letterDocument.getDcitDoc());
//
//			String url = "./pdf.svc?imageId=" + invoice.getLetterID();
//			System.out.println("dokumen letter URL OK "+url);
//
//			return url;
//		} else {
//			String url = "./documentNotFound.html";
//			System.out.println("dokumen letter URL failed "+url);
//
//			return url;
//		}
//	}
	
	public String doPrint() throws IOException{
		if (invoice.getLetterID() != null && (invoice.getLetterID().intValue() > 0)) {
			DocumentLetter letterDocument = getInvoiceManager().printLetterDocument(invoice.getLetterID());
			if(letterDocument != null && (letterDocument.getDcitDoc() != null)){
				PdfService.setPdf(letterDocument.getDcitDoc());
	
				String url = "./pdf.svc?imageId=" + invoice.getLetterID();
				System.out.println("dokumen letter URL OK "+url);
	
				return url;
			}else{
				String url = "./documentNotFound.html";
				System.out.println("dokumen letter URL failed "+url);

				return url;
			}
		}
		
		String url = "./documentNotFound.html";
		System.out.println("dokumen letter URL failed "+url);

		return url;
	}

	public void toManageInvoiceSearch(IRequestCycle cycle, License license){
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		
		infoPageCommand.setTitle(getText("licenseInformation.title"));
		infoPageCommand.addMessage(getText("licenseInformation.information"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(
				getText("button.finish"), "manageInvoiceSearch.html"));

		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);

		cycle.activate(infoPage);
	}
	
	private String generalValidation(){
		
		String errorMessage = null;
		
		if(getPercentageBHP() == null){
			errorMessage = getText("initial.invoice.empty.persentaseBhp");
		}
//		else if(invoice.getBhpHl() == null && invoice.getBhpPhl() == null){
//			errorMessage = getText("initial.invoice.empty.nilaiLelang");
//		}

		else if( (invoice.getYearTo().intValue()) == 1){
			if(invoice.getBhpUpfrontFee() == null){
				errorMessage = getText("initial.invoice.empty.upFrontFee");
			}else if(invoice.getBhpHl() == null && invoice.getBhpPhl() == null){
				errorMessage = getText("initial.invoice.empty.nilaiLelang");
			}
			
//			else if(invoice.getBhpHl() == null){
//				errorMessage = getText("initial.invoice.empty.hl");
//			}
		}else if( (invoice.getYearTo().intValue() > 1) ){
			
			if(getSelectPayment().equalsIgnoreCase("PHL")){
				if(invoice.getBhpPhl() == null){
					errorMessage = getText("initial.invoice.empty.phl");
				}
			}else if(getSelectPayment().equalsIgnoreCase("HL")){
				if(invoice.getBhpHl() == null){
					errorMessage = getText("initial.invoice.notFill.hl");
				}
			}
		}
		
		return errorMessage;
	}
	
	public String checkBgDueDate(){
		String error = null;
		
		if(license.getBgAvailableStatus() != null && license.getBgAvailableStatus().equalsIgnoreCase("Y")){
			if (getBgSubmitDueDate() == null) {
				error =  getText("initialInvoice.license.empty.bgDueDate");
			}
			
//			else if(license.getBgDueDate().before(new Date())){
//				error =  getText("initialInvoice.license.lessThanNow.bgDueDate");
//			}
		}
		
		return error;
	}
	
	private String calculateValidation(){
		String errorMessage = null;
		
		if(getEnableInput().equalsIgnoreCase("Y")){
			errorMessage = getText("initial.invoice.calculate.notYet");
		}
		else if(invoice.getBhpPhl() == null){
			
			if(invoice.getYearTo().intValue() > 1){
				List<Invoice> invoiceListFromTable = (List<Invoice>) getObjectfromSession("INVOICE_LIST_VARIETY");
				Invoice invFromDatabase = invoiceListFromTable.get(invoice.getYearTo().intValue() - 2);
				BigDecimal phlFromDatabase = invFromDatabase.getBhpPhl();
				if(phlFromDatabase != null){
					errorMessage = getText("initial.invoice.changeToPhl.phl");
				}
			}
			
		}
		
		if(errorMessage == null){
			errorMessage = checkBgDueDate();
		}else{
			if(checkBgDueDate() != null)
				errorMessage += ", " + checkBgDueDate();
		}
		
		return errorMessage;
	}
	
//	To check current bi rate, active or not
	public String checkBiRate(){
		List<VariableAnnualRate> rates = getVariableAnnualRateManager().findByStatus(1);
		VariableAnnualRate biRate = rates.get(0);
		String errorMessage = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date now = new Date();
		String year = formatter.format(now);
		
//		to check if current bi rate is active or not
//		if(!(year.equalsIgnoreCase(String.valueOf(biRate.getRateYear())))){
//			errorMessage = getText("initial.invoice.biRate.notActive");
//		}
//		
		//untuk mengecek apakah tahun bi rate lebih besar dari tahun bi rate aktif
//		if( !((Integer.valueOf(year)) <= (biRate.getRateYear().intValue())) ){
//			if( !(invoice.getYearTo().intValue() == 1) ){
//				errorMessage = getText("initial.invoice.biRate.notActive");
//			}
//		}
			
		Integer invYear = Integer.valueOf(invoice.getYears());
		
		if(invYear > ((biRate.getRateYear().intValue()))){
			errorMessage = getText("initial.invoice.biRate.notActive");
		}
		
		return errorMessage;
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
			int invYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(invoice.getInvBeginDate()));
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
	
	public String doViewKmIpsfrRate(){
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(invoice.getInvoiceId()),
				String.valueOf(license.getLicenceNo()), "IN-1" + invoice.getInvoiceId());
		
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
			List<DocumentUpload> listDoc = getDocumentUploadManager().findDocument(String.valueOf(license.getLicenceNo()), "1");
			
			if(listDoc.size() > 0){
				int index = listDoc.size();
				DocumentUpload document = listDoc.get(index-1);
				File downloadFile2 = new File(document.getFileDir() + document.getFileName());
				
				if(downloadFile2.exists()){
					String url = "./document.svc?imageId=" + document.getFileName();
					return url;   
				}else{
					String url = "./documentNotFound.html";
					return url; 
				} 
				
			}else{
				String url = "./documentNotFound.html";
				return url;
			}
		}
	}
	
	public void doCancel(IRequestCycle cycle) {
		ManageInvoiceSearch manageInvoiceSearch = (ManageInvoiceSearch) cycle
				.getPage("manageInvoiceSearch");
		
		saveObjectToSession(null, "INVOICE_LIST_VARIETY");
		
		getFields().put("LICENSE_LIST", (List) getFields().get("LICENSE_LIST_VR"));
		
		getFields().put("criteria", getCriteria());
		getFields().put("criteriaSearch", getCriteriaSearch());
		getFields().put("invoiceStatus", getInvoiceStatusCache());
		getFields().put("managePage", "test");
		
		manageInvoiceSearch.setCriteria(getCriteria());
		manageInvoiceSearch.setInvoiceStatus(getInvoiceStatusCache());
		
		if(getCriteria().equalsIgnoreCase("clientName")){
			manageInvoiceSearch.setClientName(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("clientNo")){
			manageInvoiceSearch.setClientNo(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("bhpMethod")){
			manageInvoiceSearch.setBhpMethod(getCriteriaSearch());
		}else if(getCriteria().equalsIgnoreCase("noApp")){
			manageInvoiceSearch.setLicenceNumber(getCriteriaSearch());
		}

		cycle.activate(manageInvoiceSearch);
	}

	public void doReset(){
		setEnableInput("Y");
		setShowTable("N");
		setPercentageBHPModel(getPercentageAnnualBhp());
	}
	
	public String annualRatePopup(){
//		saveObjectToSession("popupAnnualRate", "POPUP_ANNUAL");
//
//		return "percentage_annual()";
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
	
	public abstract String getSelectPayment();
	
//	@EventListener(elements = "myFavoriteDiv", events = "onchange")
//	public void watchText(IRequestCycle cycle)
//	{
//		System.out.println("---------------------------Change-------------------");
//		cycle.getResponseBuilder().updateComponent("myFavoriteDiv");
//		saveDocument();
//	}
	
	public void saveDocument(){
//		For saving file
		if(fileSkmIpsfr != null){
			
			String fileName = fileSkmIpsfr.getFileName();
//			int length = fileName.length();
//			String subString = fileName.substring(length-4, length);
//			if( !(subString.equals(".pdf")) ){
//				String errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.file.notPdf");
//				addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
//			}
			
			DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(invoice.getInvoiceId()),
					String.valueOf(license.getLicenceNo()), "IN-1" + invoice.getInvoiceId());
			
			if(docUp != null){
				UploadHelper.deleteFile(docUp.getFileName(), docUp);
				getDocumentUploadManager().deleteDocument(docUp);
			}
			
			Date dateTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hmmss");
			String timestamp = formatter.format(dateTime);
			fileName = fileName.replaceAll("\\s+","_");
			fileName = fileName.replace("\\", "_");
			fileName =  fileName.replace("/", "_");
			int index = fileName.indexOf("pdf") - 1;
			if(index > 0){
				fileName = fileName.substring(0, index) + timestamp + ".pdf";
			}
			UploadHelper.saveFile(fileName, fileSkmIpsfr);
			DocumentUpload doc = new DocumentUpload();
			doc.setDocDesc("Upload From Manage Invoice-");
			doc.setCreatedOn(new Date());
			
			if(getUserLoginFromSession() != null){
				doc.setCreatedBy(getUserLoginFromSession().getUserName());
			}else if(getUserLoginFromDatabase() != null){
				doc.setCreatedBy(getUserLoginFromDatabase().getUserName());
			}
			
			doc.setFileName(fileName);
			doc.setYearTo(invoice.getYearTo().intValue());
			doc.setLicenseNo(license.getLicenceNo());
			doc.setReferenceId(String.valueOf(invoice.getInvoiceId()));
			doc.setFileDir(DIRECTORY_DOCUMENT);
			doc.setDocType("1");
			doc.setvUploadId("IN-1" + invoice.getInvoiceId());
			getDocumentUploadManager().saveDocument(doc);
		
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
