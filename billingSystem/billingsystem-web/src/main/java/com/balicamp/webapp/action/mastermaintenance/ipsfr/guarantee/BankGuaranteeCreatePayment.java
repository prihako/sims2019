package com.balicamp.webapp.action.mastermaintenance.ipsfr.guarantee;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.Constants;
import com.balicamp.model.mastermaintenance.license.DocumentUpload;
import com.balicamp.model.operational.BankGuarantee;
import com.balicamp.model.operational.Invoice;
import com.balicamp.model.operational.License;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.license.DocumentUploadManager;
import com.balicamp.service.operational.BankGuaranteeManager;
import com.balicamp.service.operational.InvoiceManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.operational.PaymentBG;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class BankGuaranteeCreatePayment extends AdminBasePage implements
		PageBeginRenderListener {

	@InjectSpring("licenseManager")
	public abstract LicenseManager getLicenseManager();

	@InjectSpring("invoiceManager")
	public abstract InvoiceManager getInvoiceManager();

	@InjectSpring("bankGuaranteeManager")
	public abstract BankGuaranteeManager getBankGuaranteeManager();
	
	@InjectObject("spring:documentUploadManager")
	public abstract DocumentUploadManager getDocumentUploadManager();

	public abstract void setLicense(License license);
	public abstract License getLicense();

	public abstract void setInvoice(Invoice invoice);
	public abstract Invoice getInvoice();

	public abstract void setBankGuarantee(BankGuarantee bankGuarantee);
	public abstract BankGuarantee getBankGuarantee();

	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();

	// Radio button for received BG
	public abstract void setBgReceived(String bgReceived);
	public abstract String getBgReceived();

	//check box for paid BG
	public abstract void setBgPaid(boolean bgPaid);
	public abstract boolean isBgPaid();

	public abstract void setBhpMethod(String bhpMethod);
	public abstract String getBhpMethod();

	public abstract void setIdKlien(String idKlien);
	public abstract String getIdKlien();

	public abstract void setBgRequired(String bgRequired);
	public abstract String getBgRequired();

	public abstract void setBgDueDate(String bgDueDate);
	public abstract String getBgDueDate();
	
	public abstract void setReceivedDate(String bgDueDate);
	public abstract String getReceivedDate();
	
	public abstract void setBgPublishDate(String bgDueDate);
	public abstract String getBgPublishDate();
	
	public abstract void setDueDate(String bgDueDate);
	public abstract String getDueDate();
	
	public abstract void setClaimRequestDate(String claimRequestDate);
	public abstract String getClaimRequestDate();

	public abstract void setBgSubmitConfirmation(String bgSubmit);
	public abstract String getBgSubmitConfirmation();

	public abstract BankGuarantee getRowBG();
	public abstract Invoice getRowBHP();
	
	public abstract void setClaimDate(String bgDueDate);
	public abstract String getClaimDate();

	@SuppressWarnings("unchecked")
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
			}

			setBhpMethod(getText("label.bhpMethod."+getBankGuarantee().getLicense().getBhpMethod()));
			setIdKlien(getBankGuarantee().getLicense().getClientNo().toString());
			setBgRequired(getText("label.bgAvailable."+getBankGuarantee().getLicense().getBgAvailableStatus()));
			setBgDueDate(DateUtil.convertDateToString(getBankGuarantee().getDueDate(), "dd MMM yyyy"));
			setReceivedDate(DateUtil.convertDateToString(getBankGuarantee().getReceivedDate(), "dd MMM yyyy"));
			setBgPublishDate(DateUtil.convertDateToString(getBankGuarantee().getBgPublishDate(), "dd MMM yyyy"));
			setDueDate(DateUtil.convertDateToString(getBankGuarantee().getDueDate(), "dd MMM yyyy"));
			if(getBankGuarantee().getClaimRequestDate() != null){
				setClaimRequestDate(DateUtil.convertDateToString(getBankGuarantee().getClaimRequestDate(), "dd MMM yyyy"));
			}
			if(getBankGuarantee().getClaimDate() != null){
				setClaimDate(DateUtil.convertDateToString(getBankGuarantee().getClaimDate(), "dd MMM yyyy"));
			}

			List<BankGuarantee> bgList = getBankGuaranteeManager().findByLicenceNo(getBankGuarantee().getLicense().getLicenceNo(),"PAYMENT");
			getFields().put("BG_CALCULATE", bgList);

//			List<Invoice> invList = new ArrayList<Invoice>();
//			invList.add(getBankGuarantee().getInvoice());
			List<Invoice> invList = getInvoiceManager().findByInvoiceNo(getBankGuarantee().getInvoiceNoClaim());
			getFields().put("BHP_CALCULATE", invList);
			if(invList.size()>0){
				setInvoice(invList.get(0));
			}

			if(getFields()!=null && getFields().get("CONFIRM")!=null){
//				setBgSubmitConfirmation(getText("operational.bg.info.paymentConfirmation1", new Object[]{getUserLoginFromSession().getUsername()}));
				setBgSubmitConfirmation(getText("operational.bg.info.paymentConfirmation2"));
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BankGuarantee> getDetailCalculateBG(){
		List<BankGuarantee> bgList = (List) getFields().get("BG_CALCULATE");
		return bgList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Invoice> getDetailCalculateBHP(){
		List<Invoice> invList = (List) getFields().get("BHP_CALCULATE");
		return invList;
	}

	public void doDraft(IRequestCycle cycle) {
		BankGuarantee bg = getBankGuarantee();
		bg.setClaimStatus(Constants.Status.NO);
		getBankGuaranteeManager().save(bg);
		
		Invoice invoice  = getInvoiceManager().findByInvoiceNo(getBankGuarantee().getInvoiceNoClaim()).get(0);
		
		if(bg.getBgValue()!=null && bg.getBgValue().compareTo(invoice.getBhpTotal())==1){
			bg.setBgValueDiff(bg.getBgValue().subtract(invoice.getBhpTotal()));
		}else if(bg.getBgValue().compareTo(invoice.getBhpTotal())==-1){
			bg.setBgValueDiff(invoice.getBhpTotal().subtract(bg.getBgValue()));
		}else{
			bg.setBgValueDiff(new BigDecimal("0"));
		}

		//1st option to construct status page
		InfoPageCommand infoPageCommand = new InfoPageCommand();
		infoPageCommand.setTitle(getText("leftMenu.operational.bg.paymentBG"));
		infoPageCommand.addMessage(getText("operational.bg.info.paymentDraftSuccess"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "paymentBG.html"));
		infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));
		InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
		infoPage.setInfoPageCommand(infoPageCommand);
		cycle.activate(infoPage);

		//2nd option to refresh page
//		BankGuaranteeCreate bgCreate = (BankGuaranteeCreate) cycle.getPage("bankGuaranteeCreate");
//		bgCreate.setBankGuarantee(bg);
//		cycle.activate(bgCreate);
	}

	@SuppressWarnings("unchecked")
	public void doConfirm(IRequestCycle cycle) {
		BankGuaranteeCreatePayment bgCreate = (BankGuaranteeCreatePayment) cycle.getPage("bankGuaranteeCreatePayment");
		BankGuarantee bg 					= getBankGuarantee();
		bg.setClaimStatus(Constants.Status.YES);

		getFields().put("CONFIRM", "CONFIRM");
		bgCreate.setBankGuarantee(bg);
		bgCreate.setFields(getFields());

		cycle.activate(bgCreate);
	}

	public void doSubmit(IRequestCycle cycle) {
		Invoice invoice		= getInvoiceManager().findByInvoiceNo(getBankGuarantee().getInvoiceNoClaim()).get(0);
		BankGuarantee bg 	= getBankGuarantee();
		bg.setClaimStatus(Constants.Status.SUBMITTED);
		
//		PAYMENT PROCESS
		Map<String, String> invoiceBgMap = getBankGuaranteeManager().performPaymentBG(bg, invoice, bg.getLicense());
		
		if(invoiceBgMap.get("ERROR_CODE")!= null){
			if(invoiceBgMap.get("ERROR_CODE").toString().equalsIgnoreCase("00")){
				getBankGuaranteeManager().save(bg);
				getFields().remove("CONFIRM");
				InfoPageCommand infoPageCommand = new InfoPageCommand();
				infoPageCommand.setTitle(getText("leftMenu.operational.bg.paymentBG"));
				infoPageCommand.addMessage(getText("operational.bg.info.paymentSuccess"));
				infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.finish"), "paymentBG.html"));
				infoPageCommand.addInfoPageButton(new InfoPageCommand.InfoPageButton(getText("button.welcome"), "wellcome.html"));
				InfoPage infoPage = (InfoPage) cycle.getPage("infoPage");
				infoPage.setInfoPageCommand(infoPageCommand);
				cycle.activate(infoPage);
			}else{
				addError(getDelegate(), "errorShadow", getText("payment.error."+invoiceBgMap.get("ERROR_CODE").toString()), 
						ValidationConstraint.CONSISTENCY);
				return;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void doCancel(IRequestCycle cycle) {
		if(getFields().get("CONFIRM")!=null){
			BankGuaranteeCreatePayment bgCreate = (BankGuaranteeCreatePayment) cycle.getPage("bankGuaranteeCreatePayment");
			BankGuarantee bg 					= getBankGuarantee();
			bg.setClaimStatus(Constants.Status.NO);

			getFields().remove("CONFIRM");
			bgCreate.setBankGuarantee(bg);
			bgCreate.setFields(getFields());

			cycle.activate(bgCreate);
		}else{
			PaymentBG bgCreate = (PaymentBG) cycle.getPage("paymentBG");
			getFields().put("BACK", "BACK");
			bgCreate.setFields(getFields());
			cycle.activate(bgCreate);
		}
	}

	public String viewDocumentBG() {
		DocumentUpload docUp = getDocumentUploadManager().findAndDownload(String.valueOf(getBankGuarantee().getBgId()),
				String.valueOf(getBankGuarantee().getLicense().getLicenceNo()), "BG-3" + getBankGuarantee().getBgId());
		
		if(docUp != null){
			File downloadFile = new File(docUp.getFileDir() + docUp.getFileName());
			
			if(downloadFile.exists()){
				String url = "./document.svc?imageId=" + docUp.getFileName();
				return url;   
			}
		}
		
		String url = "./documentNotFound.html";
		return url; 
	}
}
