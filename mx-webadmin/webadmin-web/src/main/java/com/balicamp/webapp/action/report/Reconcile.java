package com.balicamp.webapp.action.report;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import test.Constants;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.InquiryReconcileDto;
import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.model.user.Role;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.armgmt.ArmgmtManager;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.sims.BillingManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class Reconcile extends AdminBasePage implements PageBeginRenderListener, PageEndRenderListener {

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getTransactionLogsManager();

	@InjectSpring("billingManagerImpl")
	public abstract BillingManager getBillingManager();

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameter();
	
	@InjectSpring("armgmtManagerImpl")
	public abstract ArmgmtManager getArmgmt();

	private XlstoStringConverter xlsFile;

	public Reconcile() {
		super();
		this.xlsFile = new XlstoStringConverter();
	}

	protected final Log log = LogFactory.getLog(Reconcile.class);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract String getChannelCode();

	public abstract void setChannelCode(String channelCode);

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getClientId();

	public abstract void setClientId(String clientId);

	public abstract String getReconcileStatus();

	public abstract void setReconcileStatus(String reconcileStatus);

	public abstract InquiryReconcileDto getInquiryReconcileDto();

	public abstract ReconcileDto getReconcileDto();

	public abstract Date getTrxDate();

	public abstract void setTrxDate(Date getTrxDate);
	
	public abstract Date getTrxEndDate();

	public abstract void setTrxEndDate(Date getTrxEndDate);

	public abstract Integer getSettled();

	public abstract void setSettled(Integer settled);

	public abstract Integer getNotSettled();

	public abstract void setNotSettled(Integer notSettled);

	public abstract Integer getUnconfirmed();

	public abstract void setUnconfirmed(Integer unconfirmed);

	public abstract Integer getSettledReport();

	public abstract void setSettledReport(Integer settled);

	public abstract Integer getNotSettledReport();

	public abstract void setNotSettledReport(Integer notSettled);

	public abstract Integer getUnconfirmedReport();

	public abstract void setUnconfirmedReport(Integer unconfirmed);

	public abstract Long getAmountSettled();

	public abstract void setAmountSettled(Long amountSettled);

	public abstract Long getAmountNotSettled();

	public abstract void setAmountNotSettled(Long amountNotSettled);

	public abstract Long getAmountUnconfirmed();

	public abstract void setAmountUnconfirmed(Long amountUnconfirmed);

	public abstract Long getAmountSettledReport();

	public abstract void setAmountSettledReport(Long amountSettled);

	public abstract Long getAmountNotSettledReport();

	public abstract void setAmountNotSettledReport(Long amountNotSettled);

	public abstract Long getAmountUnconfirmedReport();

	public abstract void setAmountUnconfirmedReport(Long amountUnconfirmed);

	public abstract Long getTotalAmount();

	public abstract void setTotalAmount(Long totalAmount);

	public abstract Long getTotalAmountReport();

	public abstract void setTotalAmountReport(Long totalAmount);

	public abstract Long getTotalAmountDenda();

	public abstract void setTotalAmountDenda(Long totalAmountDenda);

	public abstract Long getTotalAmountDendaReport();

	public abstract void setTotalAmountDendaReport(Long totalAmountDenda);

	public abstract String getDoReconcileStatus();

	public abstract void setDoReconcileStatus(String doReconcileStatus);

	public abstract void setTime(String date);

	public abstract String getTransactionType();

	public abstract void setTransactionType(String transactionType);

	public abstract String getTime();

	public abstract String getTrxType();

	private IUploadFile file;

	public IUploadFile getFile() {
		return file;
	}

	public void setFile(IUploadFile file) {
		this.file = file;
	}

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());

				} else {
					getFields().remove("TRANSACTION_LOGS_WEBADMIN");
				}
			}
		}
	}

	public IPropertySelectionModel getTransactionTypeModel() {
		HashMap<String, String> map = new HashMap<String, String>();

		List<Endpoints> endpointList = getEndpointsManager().getAllEndpointsByType("biller");
		for (Endpoints tempEndpoint : endpointList) {
			if (!tempEndpoint.getCode().equalsIgnoreCase("chws")
					&& !tempEndpoint.getCode().equalsIgnoreCase("chws2")) {
				map.put(tempEndpoint.getCode(), tempEndpoint.getName());
			}
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public String cekFileInFileSystem(String remoteFile, String trxType) {
		String errorMessage 	= null;

		String reportPath = (getSystemParameterManager().findParamValueByParamName("reconcileEod.mt940.path"));

		if (trxType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (trxType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}else{
			reportPath += "";
		}

		String filepath = reportPath + "/" + remoteFile;
		File file = new File(filepath);

		if (!file.exists()) {
			errorMessage = getText("leftMenu.reconcileEod.ftp.fileNotFound");
			return errorMessage;
		}

		return filepath;
	}

	public IPropertySelectionModel getChannelCodeModel() {
		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

//		map.put("nonbank", "Non Bank");
		List<Endpoints> endpointChannel = new ArrayList<Endpoints>();
		endpointChannel.addAll(getEndpointsManager().getAllEndpointsByType("channel"));

		for (Endpoints tempEndpoint : endpointChannel) {
			map.put(tempEndpoint.getName(), tempEndpoint.getName());
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public String channelNameToChannelCode(String channelName) {

		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		// map.put("all", "All");
		List<Endpoints> endpointChannel = new ArrayList<Endpoints>();
		endpointChannel.addAll(getEndpointsManager().getAllEndpointsByType("channel"));

		for (Endpoints tempEndpoint : endpointChannel) {
			map.put(tempEndpoint.getName(), tempEndpoint.getCode());
		}

		String channelCode = map.get(channelName);

		return channelCode;

	}

	public IPropertySelectionModel getReconcileStatusModel() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("All", "All");
		map.put("Unsettled", "Unsettled");
		map.put("Settled", "Settled");
		map.put("Need Confirmation/Manual Payment", "Need Confirmation/Manual Payment");

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGetReconcileListByMt940() {

		String errorMessage = validation();
		// validation
		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			return;
		}

		String trxDateNameString = DateUtil.convertDateToString(getTrxDate(), "yyyyMMdd");

		String channelCode 	= null;
		String trxCode 		= null;

		HashMap<String, Object[]> mt940Data = new HashMap<String, Object[]>();
		List<ReconcileDto> searchResult = new ArrayList<ReconcileDto>();

		Map<String, Integer> mapCount = new HashMap<String, Integer>();
		mapCount.put("settled", 0);
		mapCount.put("notSettled", 0);
		mapCount.put("unconfirmed", 0);
		Map<String, Long> mapCountAmount = new HashMap<String, Long>();
		mapCountAmount.put("amountSettled", (long) 0);
		mapCountAmount.put("amountNotSettled", (long) 0);
		mapCountAmount.put("amountUnconfirmed", (long) 0);
		mapCountAmount.put("totalAmount", (long) 0);
		mapCountAmount.put("totalAmountDenda", (long) 0);

		Set<String> listInvoiceCode = new HashSet<String>();
		
		Calendar reconcileCalendar 		= Calendar.getInstance();
		reconcileCalendar.setTime(new Date());

		Date trxDate 				= null;
		Date trxEndDate				= null;
		SimpleDateFormat formatter 	= new SimpleDateFormat("dd-MM-yyyy");
		try {
			if(getTrxDate()!=null){
				trxDate = formatter.parse(DateUtil.convertDateToString(getTrxDate(), "dd-MM-yyyy"));
			}else{
				trxDate = formatter.parse(DateUtil.convertDateToString(reconcileCalendar.getTime(), "dd-MM-yyyy"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(getTrxEndDate()!=null){
				trxEndDate = formatter.parse(DateUtil.convertDateToString(getTrxEndDate(), "dd-MM-yyyy"));
			}else{
				trxEndDate = formatter.parse(DateUtil.convertDateToString(reconcileCalendar.getTime(), "dd-MM-yyyy"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			String paymentType ="";

			if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
				paymentType = Constants.BillerNumberCode.BHP_CODE;
				trxCode = Constants.EndpointCode.BHP_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
				paymentType = Constants.BillerNumberCode.PERANGKAT_CODE;
				trxCode = Constants.EndpointCode.PERANGKAT_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
				paymentType = Constants.BillerNumberCode.SKOR_CODE;
				trxCode = Constants.EndpointCode.SKOR_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
				paymentType = Constants.BillerNumberCode.REOR_CODE;
				trxCode = Constants.EndpointCode.REOR_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
				paymentType = Constants.BillerNumberCode.IAR_CODE;
				trxCode = Constants.EndpointCode.IAR_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
				paymentType = Constants.BillerNumberCode.UNAR_CODE;
				trxCode = Constants.EndpointCode.UNAR_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
				paymentType = Constants.BillerNumberCode.IKRAP_CODE;
				trxCode = Constants.EndpointCode.IKRAP_CODE;
			} else if (getTransactionType().equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
				paymentType = Constants.BillerNumberCode.PAP_CODE;
				trxCode = Constants.EndpointCode.PAP_CODE;
			}

			trxCode += ".PAY";
			channelCode = channelNameToChannelCode(getChannelCode()).toLowerCase();


			if (getArmgmt().getMT940(trxDate, trxEndDate, paymentType, channelCodeToBankName(channelCode)).isEmpty()) {
				addError(getDelegate(), "errorShadow", getText("leftmenu.reconcile.errorMessage.emptySearchResult"), ValidationConstraint.CONSISTENCY);
				getFields().put("TRANSACTION_LOGS_WEBADMIN", new ArrayList());
				return;
			} else {
				mt940Data.putAll(getArmgmt().getMT940(trxDate, trxEndDate, paymentType, channelCodeToBankName(channelCode)));
			}

			System.out.println("listInvoiceCode : " + listInvoiceCode.size());

			if (!mt940Data.isEmpty()) {
				listInvoiceCode = mt940Data.keySet();
				searchResult.addAll(getTransactionLogsManager().findTransactionLogsWebadmin(mt940Data, channelCode, listInvoiceCode, getClientId(), trxCode, getTrxDate(), getReconcileStatus(), mapCount, mapCountAmount));
			}
//		}

		if (mapCount.get("settled") != null) {
			setSettled(mapCount.get("settled"));
			setSettledReport(mapCount.get("settled"));
		} else {
			setSettled(0);
			setSettledReport(0);
		}
		if (mapCount.get("notSettled") != null) {
			setNotSettled(mapCount.get("notSettled"));
			setNotSettledReport(mapCount.get("notSettled"));
		} else {
			setNotSettled(0);
			setNotSettledReport(0);
		}
		if (mapCount.get("unconfirmed") != null) {
			setUnconfirmed(mapCount.get("unconfirmed"));
			setUnconfirmedReport(mapCount.get("unconfirmed"));
		} else {
			setUnconfirmed(0);
			setUnconfirmedReport(0);
		}
		if (mapCountAmount.get("amountSettled") != null) {
			setAmountSettled(mapCountAmount.get("amountSettled"));
			setAmountSettledReport(mapCountAmount.get("amountSettled"));
		} else {
			setAmountSettled((long) 0);
			setAmountSettledReport((long) 0);
		}
		if (mapCountAmount.get("amountNotSettled") != null) {
			setAmountNotSettled(mapCountAmount.get("amountNotSettled"));
			setAmountNotSettledReport(mapCountAmount.get("amountNotSettled"));
		} else {
			setAmountNotSettled((long) 0);
			setAmountNotSettledReport((long) 0);
		}
		if (mapCountAmount.get("amountUnconfirmed") != null) {
			setAmountUnconfirmed(mapCountAmount.get("amountUnconfirmed"));
			setAmountUnconfirmedReport(mapCountAmount.get("amountUnconfirmed"));
		} else {
			setAmountUnconfirmed((long) 0);
			setAmountUnconfirmedReport((long) 0);
		}

		if (mapCountAmount.get("totalAmount") != null) {
			setTotalAmount(mapCountAmount.get("totalAmount"));
			setTotalAmountReport(mapCountAmount.get("totalAmount"));
		} else {
			setTotalAmount((long) 0);
			setTotalAmountReport((long) 0);
		}

		if (mapCountAmount.get("totalAmountDenda") != null) {
			setTotalAmountDenda(mapCountAmount.get("totalAmountDenda"));
			setTotalAmountDendaReport(mapCountAmount.get("totalAmountDenda"));
		} else {
			setTotalAmountDenda((long) 0);
			setTotalAmountDendaReport((long) 0);
		}

		if (searchResult == null || searchResult.size() < 1) {
			addError(getDelegate(), "errorShadow", getText("leftmenu.reconcile.errorMessage.emptySearchResult"), ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_LOGS_WEBADMIN", new ArrayList());
			return;
		}

		getFields().put("TRANSACTION_LOGS_WEBADMIN", searchResult);

	}

	public List<ReconcileDto> getReconcileList() {
		List<ReconcileDto> list = (List<ReconcileDto>) getFields().get("TRANSACTION_LOGS_WEBADMIN");
		return list;
	}

	public void exportToExcel(IRequestCycle cycle) throws JRException, IOException {

		ArrayList<ReconcileDto> listData = (ArrayList<ReconcileDto>) getFields().get("TRANSACTION_LOGS_WEBADMIN");
		ReportReconcileAction reportReconcileAction = new ReportReconcileAction();

		String xlsReportPath = reportReconcileAction.createReport(putParams(), listData, "xls", getServletContext(), getTransactionLogsManager().findParamValueByParamName());

		// download(xlsReportPath);

		String context = getBaseUrl();
		cycle.sendRedirect(context + "/download?fileName=" + xlsReportPath + "&mode=simple");

	}

	public void exportToPdf(IRequestCycle cycle) throws JRException, IOException {

		ArrayList<ReconcileDto> listData = (ArrayList<ReconcileDto>) getFields().get("TRANSACTION_LOGS_WEBADMIN");
		ReportReconcileAction reportReconcileAction = new ReportReconcileAction();
		String pdfReportPath = reportReconcileAction.createReport(putParams(), listData, "pdf", getServletContext(), getTransactionLogsManager().findParamValueByParamName());
		// download(pdfReportPath);

		String context = getBaseUrl();
		cycle.sendRedirect(context + "/download?fileName=" + pdfReportPath + "&mode=simple");

	}

	public Map<String, String> putParams() {

		DecimalFormat numFormat = new DecimalFormat("#,###,###.00");

		Map<String, String> params = new HashMap<String, String>();
		params.put("trxStartDate", DateUtil.convertDateToString(getTrxDate(), "dd-MM-yyyy"));
		params.put("trxEndDate", DateUtil.convertDateToString(getTrxEndDate(), "dd-MM-yyyy"));
		params.put("channelCode", channelNameToChannelCode(getChannelCode()));
		params.put("transactionType", getTransactionType());
		params.put("reconcileStatus", getReconcileStatus());

		params.put("settled", getSettled().toString());
		params.put("notSettled", getNotSettled().toString());
		params.put("unconfirmed", getUnconfirmed().toString());

		params.put("settledReport", getSettledReport().toString());
		params.put("notSettledReport", getNotSettledReport().toString());
		params.put("unconfirmedReport", getUnconfirmedReport().toString());

		params.put("amountSettled", getAmountSettled().toString());
		params.put("amountNotSettled", getAmountNotSettled().toString());
		params.put("amountUnconfirmed", getAmountUnconfirmed().toString());

		params.put("totalAmount", getTotalAmount().toString());
		params.put("totalAmountDenda", getTotalAmountDenda().toString());

		params.put("amountSettledReport", getAmountSettledReport().toString());
		params.put("amountNotSettledReport", getAmountNotSettledReport().toString());
		params.put("amountUnconfirmedReport", getAmountUnconfirmedReport().toString());

		params.put("totalAmountReport", getTotalAmountReport().toString());
		params.put("totalAmountDendaReport", getTotalAmountDendaReport().toString());

		params.put("bankName", channelCodeToBankName(channelNameToChannelCode(getChannelCode())));
		params.put("headerImg", getServletContext().getRealPath("/WEB-INF/report/"));
		return params;
	}

	public IPage detailInvoice(IRequestCycle cycle, ReconcileDto reconcileDto, Date trxDate, String channelCode, String transactionType, String reconcileStatus, Integer settled, Integer notSettled, Integer unconfirmed, Long amountSettled, Long amountNotSettled, Long amountUnconfirmed,
			Long totalAmount, Long totalAmountDenda, String doReconcileStatus) {
		DetailTransactionReconcile detailTransaction = (DetailTransactionReconcile) cycle.getPage("detailTransactionReconcile");
		detailTransaction.setReconcileDto(reconcileDto);
		detailTransaction.setRemarks(reconcileDto.getRemarks());

		DecimalFormat numFormat = new DecimalFormat("#,###,###.00");

		Map<Object, Object> mapParams = new HashMap<Object, Object>();

		mapParams.put("trxDate", DateUtil.convertDateToString(trxDate, "dd-MM-yyyy"));
		mapParams.put("channelCode", channelCode);
		mapParams.put("transactionType", transactionType);
		mapParams.put("reconcileStatus", reconcileStatus);

		mapParams.put("settled", settled);
		mapParams.put("notSettled", notSettled);
		mapParams.put("unconfirmed", unconfirmed);
		mapParams.put("amountSettled", amountSettled);
		mapParams.put("amountNotSettled", amountNotSettled);
		mapParams.put("amountUnconfirmed", amountUnconfirmed);
		mapParams.put("totalAmount", totalAmount);
		mapParams.put("totalAmountDenda", totalAmountDenda);

		mapParams.put("settledReport", settled);
		mapParams.put("notSettledReport", notSettled);
		mapParams.put("unconfirmedReport", unconfirmed);
		mapParams.put("amountSettledReport", amountSettled);
		mapParams.put("amountNotSettledReport", amountNotSettled);
		mapParams.put("amountUnconfirmedReport", amountUnconfirmed);
		mapParams.put("totalAmountReport", totalAmount);
		mapParams.put("totalAmountDendaReport", totalAmountDenda);

		mapParams.put("doReconcileStatus", doReconcileStatus);
		mapParams.put("bankName", channelCodeToBankName(channelCode));

		detailTransaction.setMapParams(mapParams);

		/*
		 * Map<Object, Object> mapParams = new HashMap<Object, Object>(); mapParams.put("settled", settle); mapParams.put("notSettled", notSettle); mapParams.put("time", time);
		 * detailTransaction.setMapParams(mapParams);
		 * 
		 * System.out.println(mapParams);
		 */

		return detailTransaction;

	}

	public List<String> getEndpointCodeList() {
		List<String> channelCodeList = new ArrayList<String>();
		Set<String> channelCodeSet = new HashSet<String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		if (endpoints.size() > 0) {
			for (Endpoints tempEnd : endpoints) {
				channelCodeList.add(tempEnd.getCode());
			}
		} else {
			List<Endpoints> listReady = new ArrayList<Endpoints>();
			listReady.addAll(getEndpointsManager().getAllEndpointsByState("ready"));
			for (int i = 0; i < listReady.size(); i++) {
				if (listReady.get(i).getState().equalsIgnoreCase("ready")) {
					// List<Endpoints> endpointList =
					// getEndpointsManager().getAllEndpointsByState("ready");
					List<Endpoints> endpointCh = new ArrayList<Endpoints>();
					endpointCh.addAll(getEndpointsManager().getAllEndpointsByType("channel"));

					for (Endpoints tempEndpoint : endpointCh) {
						// if (!tempEndpoint.getCode().equalsIgnoreCase("chws"))
						// {
						channelCodeSet.add(tempEndpoint.getCode());
						// }
					}
				}
			}

		}
		channelCodeList.addAll(channelCodeSet);
		return channelCodeList;
	}

	public IPropertySelectionModel getTransactionTypeModelBiller() {
		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		// Check if session is null
		if (getUserLoginFromSession() == null) {
			try {
				getResponse().sendRedirect(getBaseUrl() + "/main.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Set<Role> roles = getUserLoginFromSession().getRoleSet();
		for (Role tempRole : roles) {
			if (getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()) != null) {
				endpoints.add(getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()));
			}
		}

		if (endpoints.size() > 0) {
			for (Endpoints tempEnd : endpoints) {
				map.put(tempEnd.getCode(), tempEnd.getName());
			}
		} else {
			// map.put("All", "All");
			List<Endpoints> endpointList = getEndpointsManager().getAllEndpointsByType("biller");
			// List<Endpoints> endpointList =
			// getEndpointsManager().getAllEndpointsByState("ready");
			for (Endpoints tempEndpoint : endpointList) {
				if (!tempEndpoint.getCode().equalsIgnoreCase("chws") && !tempEndpoint.getCode().equalsIgnoreCase("chws2")) {
					map.put(tempEndpoint.getCode(), tempEndpoint.getName());
				}
			}

		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public String validation() {

		String errorMessage = null;

		if (getTrxDate() == null) {

			errorMessage = getText("leftmenu.reconcile.trxDate.empty");

		}

		return errorMessage;

	}

	public String channelCodeToBankName(String channelCode) {

		String bankName = null;
		if (channelCode.equals("chws")) {
			bankName = "mandiri";
		} else if (channelCode.equals("chws2")) {
			bankName = "bni";
		}
		else if (channelCode.equals("chws3")) {
			bankName = "bri";
		}
		return bankName;

	}
}
