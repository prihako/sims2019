package com.balicamp.webapp.action.mastermaintenance.ipsfr.license;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.mastermaintenance.license.UploadView;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.License;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.mastermaintenance.license.UploadViewManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;

public abstract class UploadDokumen extends AdminBasePage implements PageBeginRenderListener {
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	public abstract void setActiveKM(boolean active);
	
	public abstract boolean getActiveKM();
	
	public abstract void setActiveBI(boolean active);
	
	public abstract boolean getActiveBI();
	
	public abstract void setActiveBG(boolean active);
	
	public abstract boolean getActiveBG();
	
	public abstract void setActivePBG(boolean active);
	
	public abstract boolean getActivePBG();
	
	public abstract void setActiveVPBG(boolean active);
	
	public abstract boolean getActiveVPBG();
	
	public abstract void setActiveSKL(boolean active);
	
	public abstract boolean getActiveSKL();
	
	private static final String LIST = "DATA_IPSFR";

	private static final String LIST_BI = "DATA_BI_RATE";

	private static final String LIST_BG = "DATA_BG";

	private static final String LIST_PBG = "DATA_PBG";

	private static final String LIST_VPBG = "DATA_VPBG";

	private static final String LIST_SKL = "DATA_SKL";

	public abstract void setNotification(boolean note);

	public abstract boolean getNotification();

	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();

	@InjectObject("spring:uploadViewManager")
	public abstract UploadViewManager getUploadViewManager();
	
	@InjectObject("spring:bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();
	
	public abstract void setJenisDokumen(String jenis);
	
	public abstract String getJenisDokumen();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
//				setActiveKM(false);
//				setActiveBI(false);
//				setActiveBG(false);
//				setActivePBG(false);
//				setActiveVPBG(false);
//				setActiveSKL(false);
			}
		}
		
		//for disable button upload new bi rate if user has upload document for current year
		Date currentYear = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		DocumentUpload docUp = getDocumentUploadManager().findBiByYear(Integer.valueOf(formatter.format(currentYear)));
		if(docUp != null){
			setDisableButtonNewBi(true);
		}else{
			setDisableButtonNewBi(false);
		}
		
	}
	
	public IPropertySelectionModel getJenisDokumenModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("SKMIPSFR", "Surat Keputusan Menteri IPSFR");
		map.put("SKMBIRATE", "Surat Keputusan Menteri BI Rate");
		map.put("BG", "Bank Garansi");
		map.put("SBPBG", "Surat Bukti Pencairan Bank Garansi");
		map.put("SVPBG", "Surat Verifikasi Pencairan Bank Garansi");
		map.put("SKL", "Surat Keputusan Lainnya");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public String validationSearch(){
		
		String errorMessage = null;
		
		if(getJenisDokumen() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.general");
		}
		
		return errorMessage;
	}

	public void doSearch(){
		
		String validationMsg 	= validationSearch();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		setActiveKM(false);
		setActiveBI(false);
		setActiveBG(false);
		setActivePBG(false);
		setActiveVPBG(false);
		setActiveSKL(false);
		
		saveObjectToSession(null, LIST);
		saveObjectToSession(null, LIST_BI);
		saveObjectToSession(null, LIST_BG);
		saveObjectToSession(null, LIST_PBG);
		saveObjectToSession(null, LIST_VPBG);
		saveObjectToSession(null, LIST_SKL);
		
		if(getJenisDokumen().equalsIgnoreCase("SKMIPSFR")){
			setActiveKM(true);
		}else if(getJenisDokumen().equalsIgnoreCase("SKMBIRATE")){
			setActiveBI(true);
		}else if(getJenisDokumen().equalsIgnoreCase("BG")){
			setActiveBG(true);
		}else if(getJenisDokumen().equalsIgnoreCase("SBPBG")){
			setActivePBG(true);
		}else if(getJenisDokumen().equalsIgnoreCase("SVPBG")){
			setActiveVPBG(true);
		}else if(getJenisDokumen().equalsIgnoreCase("SKL")){
			setActiveSKL(true);
		}
		
		//For Save to audit log
		getUserManager().uploadDokumen(initUserLoginFromDatabase(), getRequest().getRemoteHost(), null);
		
	}

	public void doDownload(IRequestCycle cycle, BigDecimal referenceId, String licenseNo, String vUploadId, String active){
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(referenceId), licenseNo, vUploadId);
		
		String errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.fileNotFound");
		
		File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
		
		if(downloadFile.exists()){
			cycle.sendRedirect("./image.svc?imageId=" + docUp.getFileName());
			//Test
//			showDocument("./document.svc?imageId=" + docUp.getFileName());
		}else{
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			
			if(active.equalsIgnoreCase("KM")){
				setActiveKM(true);
			}else if(active.equalsIgnoreCase("BI")){
				setActiveBI(true);
			}else if(active.equalsIgnoreCase("BG")){
				setActiveBG(true);
			}else if(active.equalsIgnoreCase("PBG")){
				setActivePBG(true);
			}else if(active.equalsIgnoreCase("VPBG")){
				setActiveVPBG(true);
			}else if(active.equalsIgnoreCase("SKL")){
				setActiveSKL(true);
			}
			
			return;
		}
		
	}
	
//	public String showDocument(String url){
//		return url;
//	}

	//SKMIPSFR
	public IPropertySelectionModel getJenisKMModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NamaKlien", "Nama Klien");
		map.put("NoAplikasi", "No Aplikasi");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setJenisKM(String km);

	public abstract String getJenisKM();

	public abstract UploadView getRow();
	
	public abstract void setKriteriaKM(String km);

	public abstract String getKriteriaKM();

	public void doSearchKM(){
		
		setActiveKM(true);
		
		String validationMsg 	= validationKM();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<UploadView> listData = null;

		if(getJenisKM().equalsIgnoreCase("NamaKlien")){
			listData = getUploadViewManager().findDataByClientName("IN-1", getKriteriaKM());
		}else if(getJenisKM().equalsIgnoreCase("NoAplikasi")){
			listData = getUploadViewManager().findDataByLicenseNo("IN-1", getKriteriaKM());
		}

		saveObjectToSession(listData, LIST);

	}

	//Untuk tabel
	public List getDataIPSFR(){
		List<UploadView> listData 	= (List<UploadView>) getObjectfromSession(LIST);

		return listData;
	}

	public IPage doInputKM(IRequestCycle cycle, String viewUploadId, String licenseNo, String year, Integer yearTo){
		UploadView uploadView = getUploadViewManager().findDataByViewUploadId(viewUploadId, licenseNo, year, yearTo);

		UploadIPSFR uploadPage = (UploadIPSFR) cycle.getPage("uploadIPSFR");
		
		if(uploadView != null){
			DocumentUpload docUpload = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()), uploadView.getLicenseNo(), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
			}else{
				uploadPage.setDeskripsi(null);
			}
		}

		uploadPage.setLicenseNo(licenseNo);
		uploadPage.setClientName(uploadView.getClientName());
		uploadPage.setYearTo(String.valueOf(uploadView.getYearTo()));
		uploadPage.setPercentYear(String.valueOf(uploadView.getYear()));
		uploadPage.setYear(uploadView.getYear());
		uploadPage.setBhpMethod(uploadView.getBhp());
		uploadPage.setJenisDokumen(uploadView.getType());
		uploadPage.setDocType(uploadView.getDocType());

		uploadPage.setUploadView(uploadView);

		List<License> licenseList = getLicenseManager().findByLicenceNo(licenseNo);

		License license = null;
		if(licenseList.size() > 0){
			license = licenseList.get(0);
			uploadPage.setClientID(String.valueOf(license.getClientNo()));
			uploadPage.setLicense(license);
		}
		
		uploadPage.setCriteriaSearch(getJenisDokumen(), getJenisKM(), getKriteriaKM());

		return uploadPage;
	}
	
	public String validationKM(){
		
		String errorMessage = null;
		
		if(getJenisKM() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.empty");
		}else if(getKriteriaKM() ==  null){
			errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.keteranganKriteria.empty");
		}
		
		return errorMessage;
	}

	//SKMBIRate
	public IPropertySelectionModel getBiRateModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NoKMBIRate", "NO. KM Penetapan BI Rate");
		map.put("Tahun", "Tahun");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setBiRate(String rate);

	public abstract String getBiRate();
	
	public abstract void setKriteriaBi(String bi);
	
	public abstract String getKriteriaBi();

	public abstract UploadView getRowBi();
	
	public abstract void setDisableButtonNewBi(boolean b);
	
	public abstract boolean getDisableButtonNewBi();

	public void doSearchBi(){
		
		setActiveBI(true);
		
		String validationMsg 	= validationBI();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}
		
		if(getBiRate().equalsIgnoreCase("Tahun")){
			int biYearFromUser = Integer.valueOf(getKriteriaBi());
			int currentYear = Integer.valueOf( (new SimpleDateFormat("yyyy")).format(new Date()) );
			if(biYearFromUser != currentYear){
				String errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.biRateNotCurrentYear");
				addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			}
		}
		
		List<UploadView> listData = null;

		if(getBiRate().equalsIgnoreCase("NoKMBIRate")){
			listData = getUploadViewManager().findBiByDocName("BI-2", getKriteriaBi());
		}else if(getBiRate().equalsIgnoreCase("Tahun")){
			listData = getUploadViewManager().findDataBIByYear(getKriteriaBi(), "BI-2");
		}

		saveObjectToSession(listData, LIST_BI);
	}

	public List getDataBiRate(){
		List<UploadView> listData 	= (List<UploadView>) getObjectfromSession(LIST_BI);

		return listData;
	}

	public IPage doInputBI(IRequestCycle cycle, String viewUploadId, String licenseNo, String year, Integer yearTo){
		UploadView uploadView = getUploadViewManager().findDataByViewUploadId(viewUploadId, year);
 
		UploadBIRate uploadPage = (UploadBIRate) cycle.getPage("uploadBIRate");
		
		if(uploadView != null){
			DocumentUpload docUpload = getDocumentUploadManager().findBiRateDocument(String.valueOf(uploadView.getReferenceId()), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
			}else{
				uploadPage.setDeskripsi(null);
			}
				
		}

		uploadPage.setYear(uploadView.getYear());
		uploadPage.setJenisDokumen(uploadView.getType());
		uploadPage.setUploadView(uploadView);
		
		uploadPage.setCriteriaSearch(getJenisDokumen(), getBiRate(), getKriteriaBi());

		return uploadPage;
	}
	
	public IPage doUploadNewBi(IRequestCycle cycle){
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy");
		Date date = new Date();
		List<UploadView> listBiView = getUploadViewManager().findDataBIByYear(simpleDate.format(date), "BI-2");
		
		UploadView uploadView = null;
		if(listBiView.size() > 0){
			uploadView = listBiView.get(0);
		}
		
		if(uploadView != null){
			UploadBIRate uploadPage = (UploadBIRate) cycle.getPage("uploadBIRate");
			
			DocumentUpload docUpload = getDocumentUploadManager().findBiRateDocument(String.valueOf(uploadView.getReferenceId()), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
			}else{
				uploadPage.setDeskripsi(null);
			}
			
			uploadPage.setYear(simpleDate.format(date));
			uploadPage.setUploadView(uploadView);
			uploadPage.setJenisDokumen(uploadView.getType());
			return uploadPage;
		}else{
			String errorMess = getText("ftMenu.masterMaintenance.ipsfr.uploadDokumen.currentBIRateEmpty");
			addError(getDelegate(), "errorShadow", errorMess, ValidationConstraint.CONSISTENCY);
			setActiveBI(true);
			return null;
		}
	}
	
	public String validationBI(){
	
		String errorMessage = null;
		
		if(getBiRate() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.empty");
		}else if(getKriteriaBi() ==  null){
			errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.keteranganKriteria.empty");
		}
		
		return errorMessage;
	}
	
	public void doDownloadBi(IRequestCycle cycle, BigDecimal referenceId, String vUploadId, String active){
		DocumentUpload docUp = getDocumentUploadManager().findBiRateDocument(String.valueOf(referenceId), vUploadId);
		
		String errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.fileNotFound");
		
		File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
		
		if(downloadFile.exists()){
			cycle.sendRedirect("./image.svc?imageId=" + docUp.getFileName());
		}else{
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			
			if(active.equalsIgnoreCase("KM")){
				setActiveKM(true);
			}else if(active.equalsIgnoreCase("BI")){
				setActiveBI(true);
			}else if(active.equalsIgnoreCase("BG")){
				setActiveBG(true);
			}else if(active.equalsIgnoreCase("PBG")){
				setActivePBG(true);
			}else if(active.equalsIgnoreCase("VPBG")){
				setActiveVPBG(true);
			}else if(active.equalsIgnoreCase("SKL")){
				setActiveSKL(true);
			}
			
			return;
		}
		
	}


	//SKMBG
	public IPropertySelectionModel getJenisBGModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NamaKlien", "Nama Klien");
		map.put("NoAplikasi", "No Aplikasi");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setJenisBG(String bg);

	public abstract String getJenisBG();

	public abstract UploadView getRowBG();
	
	public abstract void setKriteriaBG(String bg);

	public abstract String getKriteriaBG();

	public void doSearchBG(){
		
		setActiveBG(true);
		
		String validationMsg 	= validationBG();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<UploadView> listData = null;

		if(getJenisBG().equalsIgnoreCase("NamaKlien")){
			String criteria = getKriteriaBG().toUpperCase();
			listData = getUploadViewManager().findDataByClientName("BG-3", criteria);
		}else if(getJenisBG().equalsIgnoreCase("NoAplikasi")){
			listData = getUploadViewManager().findDataByLicenseNo("BG-3", getKriteriaBG());
		}

		for (UploadView uploadView : listData) {
			System.out.println(uploadView.toString());
		}

		saveObjectToSession(listData, LIST_BG);

	}

	//Untuk tabel
	public List getDataBG(){
		List<UploadView> listData 	= (List<UploadView>) getObjectfromSession(LIST_BG);

		return listData;
	}

	public IPage doInputBG(IRequestCycle cycle, String viewUploadId, String licenseNo, String year, Integer yearTo, BigDecimal referenceId){
		if(year==null){
			year = "";
		}
		
		UploadView uploadView = getUploadViewManager().findDataByViewUploadId(viewUploadId, licenseNo, year, yearTo);

		BankGuarantee bg = (BankGuarantee) getBankGuaranteeManager().findByBgId(Long.valueOf(String.valueOf(referenceId)));

		UploadBG uploadPage = (UploadBG) cycle.getPage("uploadBG");

		if(bg != null){
			uploadPage.setDueDate(bg.getDueDate());
			uploadPage.setBankGuarantee(bg);
		}
		
		if(uploadView != null){
			DocumentUpload docUpload = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()), uploadView.getLicenseNo(), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
			}else{
				uploadPage.setDeskripsi(null);
			}
			
			uploadPage.setClientName(uploadView.getClientName());
			uploadPage.setYearTo(String.valueOf(uploadView.getYearTo()));
			uploadPage.setPercentYear(String.valueOf(uploadView.getYear()));
			uploadPage.setYear(uploadView.getYear());
			uploadPage.setBhpMethod(uploadView.getBhp());
			uploadPage.setJenisDokumen(uploadView.getType());
			uploadPage.setDocType(uploadView.getDocType());
			uploadPage.setUploadView(uploadView);
		}
		
		uploadPage.setLicenseNo(licenseNo);
		
		
		List<License> licenseList = getLicenseManager().findByLicenceNo(licenseNo);

		License license = null;
		if(licenseList.size() > 0){
			license = licenseList.get(0);
			uploadPage.setClientID(String.valueOf(license.getClientNo()));
			uploadPage.setLicense(license);
		}
		
		uploadPage.setCriteriaSearch(getJenisDokumen(), getJenisBG(), getKriteriaBG());

		return uploadPage;
	}
	
	public String validationBG(){
		
		String errorMessage = null;
		
		if(getJenisBG() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.empty");
		}else if(getKriteriaBG() ==  null){
			errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.keteranganKriteria.empty");
		}
		
		return errorMessage;
	}

	//SBPBG
	public IPropertySelectionModel getJenisPBGModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NamaKlien", "Nama Klien");
		map.put("NoAplikasi", "No Aplikasi");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setJenisPBG(String pbg);

	public abstract String getJenisPBG();

	public abstract UploadView getRowPBG();

	public abstract String getKriteriaPBG();
	
	public abstract void setKriteriaPBG(String pbgCrit);

	public void doSearchPBG(){
		
		setActivePBG(true);
		
		String validationMsg 	= validationPBG();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<UploadView> listData = null;

		if(getJenisPBG().equalsIgnoreCase("NamaKlien")){
			String criteria = getKriteriaPBG().toUpperCase();
			listData = getUploadViewManager().findDataByClientName("BG-4", criteria);
		}else if(getJenisPBG().equalsIgnoreCase("NoAplikasi")){
			listData = getUploadViewManager().findDataByLicenseNo("BG-4", getKriteriaPBG());
		}

		for (UploadView uploadView : listData) {
			System.out.println(uploadView.toString());
		}

		saveObjectToSession(listData, LIST_PBG);

	}

	//Untuk tabel
	public List getDataPBG(){
		List<UploadView> listData 	= (List<UploadView>) getObjectfromSession(LIST_PBG);

		return listData;
	}

	public IPage doInputPBG(IRequestCycle cycle, String viewUploadId, String licenseNo, String year, Integer yearTo){
		UploadView uploadView = getUploadViewManager().findDataByViewUploadId(viewUploadId, licenseNo, year, yearTo);

		System.out.println("getJenisDokumen() " + getJenisDokumen());

		UploadPencairanBG uploadPage = (UploadPencairanBG) cycle.getPage("uploadPencairanBG");
		
		if(uploadView != null){
			DocumentUpload docUpload = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()), uploadView.getLicenseNo(), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
			}else{
				uploadPage.setDeskripsi(null);
			}
		}

		uploadPage.setLicenseNo(licenseNo);
		uploadPage.setClientName(uploadView.getClientName());
		uploadPage.setYearTo(String.valueOf(uploadView.getYearTo()));
		uploadPage.setPercentYear(String.valueOf(uploadView.getYear()));
		uploadPage.setYear(uploadView.getYear());
		uploadPage.setBhpMethod(uploadView.getBhp());
		uploadPage.setJenisDokumen(uploadView.getType());
//		uploadPage.setReferenceId(String.valueOf(uploadView.getLicenseId()));
		uploadPage.setDocType(uploadView.getDocType());
		uploadPage.setUploadView(uploadView);
		
		List<License> licenseList = getLicenseManager().findByLicenceNo(licenseNo);

		License license = null;
		if(licenseList.size() > 0){
			license = licenseList.get(0);
			uploadPage.setClientID(String.valueOf(license.getClientNo()));
			uploadPage.setLicense(license);
		}
		
		uploadPage.setCriteriaSearch(getJenisDokumen(), getJenisPBG(), getKriteriaPBG());

		return uploadPage;
	}
	
	public String validationPBG(){
		
		String errorMessage = null;
		
		if(getJenisPBG() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.empty");
		}else if(getKriteriaPBG() ==  null){
			errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.keteranganKriteria.empty");
		}
		
		return errorMessage;
	}

	////SBVPBG
	public IPropertySelectionModel getJenisVPBGModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NamaKlien", "Nama Klien");
		map.put("NoAplikasi", "No Aplikasi");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setJenisVPBG(String vpbg);

	public abstract String getJenisVPBG();

	public abstract UploadView getRowVPBG();
	
	public abstract void setKriteriaVPBG(String vpbg2);

	public abstract String getKriteriaVPBG();

	public void doSearchVPBG(){
		
		setActiveVPBG(true);
		
		String validationMsg 	= validationVPBG();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<UploadView> listData = null;

		System.out.println(getJenisVPBG());

		if(getJenisVPBG().equalsIgnoreCase("NamaKlien")){
			String criteria = getKriteriaVPBG().toUpperCase();
			listData = getUploadViewManager().findDataByClientName("BG-5", criteria);
		}else if(getJenisVPBG().equalsIgnoreCase("NoAplikasi")){
			listData = getUploadViewManager().findDataByLicenseNo("BG-5", getKriteriaVPBG());
		}

		for (UploadView uploadView : listData) {
			System.out.println(uploadView.toString());
		}

		saveObjectToSession(listData, LIST_VPBG);

	}

	//Untuk tabel
	public List getDataVPBG(){
		List<UploadView> listData 	= (List<UploadView>) getObjectfromSession(LIST_VPBG);

		return listData;
	}

	public IPage doInputVPBG(IRequestCycle cycle, String viewUploadId, String licenseNo, String year, Integer yearTo){
		UploadView uploadView = getUploadViewManager().findDataByViewUploadId(viewUploadId, licenseNo, year, yearTo);

		System.out.println("getJenisDokumen() " + getJenisDokumen());

		UploadVerifikasiPencairanBankGaransi uploadPage = (UploadVerifikasiPencairanBankGaransi) cycle.getPage("uploadVerifikasiPencairanBankGaransi");
		
		if(uploadView != null){
			DocumentUpload docUpload = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()), uploadView.getLicenseNo(), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
			}else{
				uploadPage.setDeskripsi(null);
			}
		}
		
		uploadPage.setLicenseNo(licenseNo);
		uploadPage.setClientName(uploadView.getClientName());
		uploadPage.setYearTo(String.valueOf(uploadView.getYearTo()));
		uploadPage.setPercentYear(String.valueOf(uploadView.getYear()));
		uploadPage.setYear(uploadView.getYear());
		uploadPage.setBhpMethod(uploadView.getBhp());
		uploadPage.setJenisDokumen(uploadView.getType());
//		uploadPage.setReferenceId(String.valueOf(uploadView.getLicenseId()));
		uploadPage.setDocType(uploadView.getDocType());
		uploadPage.setUploadView(uploadView);
		
		List<License> licenseList = getLicenseManager().findByLicenceNo(licenseNo);

		License license = null;
		if(licenseList.size() > 0){
			license = licenseList.get(0);
			uploadPage.setClientID(String.valueOf(license.getClientNo()));
			uploadPage.setLicense(license);
		}
		
		uploadPage.setCriteriaSearch(getJenisDokumen(), getJenisVPBG(), getKriteriaVPBG());

		return uploadPage;
	}
	
	public String validationVPBG(){
		
		String errorMessage = null;
		
		if(getJenisVPBG() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.empty");
		}else if(getKriteriaVPBG() ==  null){
			errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.keteranganKriteria.empty");
		}
		
		return errorMessage;
	}
	
	//SKL
	public IPropertySelectionModel getJenisSKLModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NamaKlien", "Nama Klien");
		map.put("NoAplikasi", "No Aplikasi");
		return new PropertySelectionModel(getLocale(), map, true, false);
	}
	
	public abstract void setJenisSKL(String skl);

	public abstract String getJenisSKL();

	public abstract UploadView getRowSKL();
	
	public abstract void setKriteriaSKL(String SKL2);

	public abstract String getKriteriaSKL();

	public void doSearchSKL(){
		
		setActiveSKL(true);
		
		String validationMsg 	= validationSKL();
		
		if (validationMsg != null) {
			addError(getDelegate(), "errorShadow", validationMsg, ValidationConstraint.CONSISTENCY);
			return;
		}

		List<UploadView> listData = null;

		System.out.println(getJenisSKL());

		if(getJenisSKL().equalsIgnoreCase("NamaKlien")){
			String criteria = getKriteriaSKL().toUpperCase();
			listData = getUploadViewManager().findDataByClientName("IN-6", criteria);
		}else if(getJenisSKL().equalsIgnoreCase("NoAplikasi")){
			listData = getUploadViewManager().findDataByLicenseNo("IN-6", getKriteriaSKL());
		}

		for (UploadView uploadView : listData) {
			System.out.println(uploadView.toString());
		}

		saveObjectToSession(listData, LIST_SKL);

	}

	//Untuk tabel
	public List getDataSKL(){
		List<UploadView> listData 	= (List<UploadView>) getObjectfromSession(LIST_SKL);

		return listData;
	}

	public IPage doInputSKL(IRequestCycle cycle, String viewUploadId, String licenseNo, String year, Integer yearTo){
		UploadView uploadView = getUploadViewManager().findDataByViewUploadId(viewUploadId, licenseNo, year, yearTo);

		UploadSuratLainnya uploadPage = (UploadSuratLainnya) cycle.getPage("uploadSuratLainnya");

		if(uploadView != null){
			DocumentUpload docUpload = getDocumentUploadManager().findAndDownload(String.valueOf(uploadView.getReferenceId()), uploadView.getLicenseNo(), uploadView.getVUploadId());
			if(docUpload != null){
				uploadPage.setDeskripsi(docUpload.getDocDesc());
				uploadPage.setFileName(uploadView.getFileName());
			}else{
				uploadPage.setDeskripsi(null);
			}
		}
		
		uploadPage.setLicenseNo(licenseNo);
		uploadPage.setClientName(uploadView.getClientName());
		uploadPage.setYearTo(String.valueOf(uploadView.getYearTo()));
		uploadPage.setPercentYear(String.valueOf(uploadView.getYear()));
		uploadPage.setYear(uploadView.getYear());
		uploadPage.setBhpMethod(uploadView.getBhp());
		uploadPage.setJenisDokumen(uploadView.getType());
//		uploadPage.setReferenceId(String.valueOf(uploadView.getLicenseId()));
		uploadPage.setDocType(uploadView.getDocType());
		uploadPage.setUploadView(uploadView);
		
		List<License> licenseList = getLicenseManager().findByLicenceNo(licenseNo);

		License license = null;
		if(licenseList.size() > 0){
			license = licenseList.get(0);
			uploadPage.setClientID(String.valueOf(license.getClientNo()));
			uploadPage.setLicense(license);
		}
		
		uploadPage.setCriteriaSearch(getJenisDokumen(), getJenisSKL(), getKriteriaSKL());

		return uploadPage;
	}
	
	public String validationSKL(){
		
		String errorMessage = null;
		
		if(getJenisSKL() == null){
			errorMessage = getText("leftMenu.masterMaintenance.ipsfr.uploadDokumen.jenisDokumen.empty");
		}else if(getKriteriaSKL() ==  null){
			errorMessage = getText("masterMaintenance.ipsfr.uploadDokumen.keteranganKriteria.empty");
		}
		
		return errorMessage;
	}

}
