package com.balicamp.webapp.scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import test.Constants;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.armgmt.ArmgmtManagerImpl;
import com.balicamp.service.impl.ebs.ExternalBillingSystemManagerImpl;
import com.balicamp.service.impl.pengujian.PengujianManagerImpl;
import com.balicamp.service.impl.pengujian.SertifikasiManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.report.ReportReconcileAction;
import com.balicamp.webapp.action.report.XlstoStringConverter;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.util.SendMail;

@SuppressWarnings("serial")
public class ReconcileEodTask extends HttpServlet {
	protected final Log log = LogFactory.getLog(getClass());
	private FTPManager ftpManager;
	private DataSource dataSource;
	private DataSource dataSourceWebapp;
	@Autowired
	public SystemParameterManager systemParameter;

	@Autowired
	public TransactionLogsManager trxLogManager;

	@Autowired
	public ExternalBillingSystemManagerImpl externalBillingSystem;
	
	@Autowired
	public ArmgmtManagerImpl armgmtManagerImpl;
	
	@Autowired
	public SertifikasiManagerImpl sertifikasiManagerImpl;
	
	@Autowired
	public PengujianManagerImpl pengujianManagerImpl;

	@SuppressWarnings("unused")
	private XlstoStringConverter xlsFile;

	public ReconcileEodTask() {
		this.xlsFile = new XlstoStringConverter();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDataSourceWebapp(DataSource dataSourceWebapp) {
		this.dataSourceWebapp = dataSourceWebapp;

	}

	public void setFtpManager(FTPManager ftpManager) {
		this.ftpManager = ftpManager;
	}

	public void listAllFiles() throws Exception {
		String[] fileNames = ftpManager.listFileNames(null);
		log.info("---------List Of Files-----------");
		for (int i = 0; i < fileNames.length; i++) {
			System.out.println("-----------" + fileNames[i] + "---------");
			log.info(fileNames[i]);
		}
		log.info("---------End Of List Of Files-----------");
	}

	public void init() throws ServletException {
		super.init();
	}

	public void reconcileEod() throws FileNotFoundException, JRException, IOException, ServletException, SQLException {
		// Get Today Date
		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Calendar cal = Calendar.getInstance();
		// String todayTimeStamp =
		// (dateFormat.format(cal.getTime())).toString();

		// Get All Channel
		init();
		String sql = "SELECT * FROM endpoints WHERE type = 'channel'";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		// boolean flag = false;
		List<String> channelList = new ArrayList<String>();

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			rs = stat.executeQuery();
			while (rs.next()) {
				String chnl[] = rs.getString("name").split(" ");
				channelList.add(chnl[1] + "-" + rs.getString("code").trim());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			con.close();
		}

		// get Biller List
		sql = "SELECT * FROM endpoints WHERE type = 'biller'";
		con = null;
		stat = null;
		rs = null;
		// boolean flag = false;
		List<String> billerList = new ArrayList<String>();

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			rs = stat.executeQuery();
			while (rs.next()) {
				billerList.add(rs.getString("code"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			con.close();
		}
		System.out.println("RECONCILE EOD START");
		Iterator<String> billerIterator = billerList.iterator();
		while (billerIterator.hasNext()) {
			doGetReconcileListByMt940EodNew(billerIterator.next(), channelList);
		}
		System.out.println("RECONCILE EOD FINISH");
	}

/*	public void doGetReconcileListByMt940Eod(List<String> channelList, String transactionCode) throws FileNotFoundException, JRException, IOException, SQLException {

		String mt940BasePath = systemParameter.findParamValueByParamName("reconcileEod.mt940.path");
		String jrxmlRealPath = systemParameter.findParamValueByParamName("reconcileEod.jrxml.path");
		String jasperRealPath = systemParameter.findParamValueByParamName("reconcileEod.jasper.path");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> finalParams = new HashMap<String, String>();

		Calendar reconcileCalendar = Calendar.getInstance();
		reconcileCalendar.setTime(new Date());
		reconcileCalendar.add(Calendar.DATE, -1);

		Map<String, String> listFile = new HashMap<String, String>();
		Integer settled = 0;
		Integer unSettled = 0;
		Integer needConfirmation = 0;
		Long paymentAmountSettled = 0L;
		Long paymentAmountUnSettled = 0L;
		Long payemntAmountNeedConfirmation = 0L;

		String trxDateNameString = DateUtil.convertDateToString(reconcileCalendar.getTime(), "yyyyMMdd");
		// String trxDateNameString = "20151020";
		String filepath = null;
		String filename = null;
		String channelCode[] = null;
		String billerCodeNumber = null;
		String trxCode = null;

		List<String> channelCodeList = channelList;

		Iterator<String> channelCodeListIterator = channelCodeList.iterator();

		while (channelCodeListIterator.hasNext()) {
			List<ReconcileDto> searchResult = new ArrayList<ReconcileDto>();
			List<ReconcileDto> finalSearchResult = new ArrayList<ReconcileDto>();

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

			HashMap<String, String> mt940Data = new HashMap<String, String>();
			Set<String> listInvoiceCode = new HashSet<String>();

			int i = 0;
			channelCode = channelCodeListIterator.next().split("-"); // 0= nama
																		// bank,
																		// 1=nama
																		// channel
																		// di mx
			filepath = mt940BasePath + channelCode[0].toLowerCase() + "/";

			if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
				filepath += Constants.DirectoryFTPConstants.BHP_DIR;
				billerCodeNumber = Constants.BillerNumberCode.BHP_CODE;
				trxCode = Constants.EndpointCode.BHP_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
				filepath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
				billerCodeNumber = Constants.BillerNumberCode.PERANGKAT_CODE;
				trxCode = Constants.EndpointCode.PERANGKAT_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
				filepath += Constants.DirectoryFTPConstants.SKOR_DIR;
				billerCodeNumber = Constants.BillerNumberCode.SKOR_CODE;
				trxCode = Constants.EndpointCode.SKOR_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
				filepath += Constants.DirectoryFTPConstants.REOR_DIR;
				billerCodeNumber = Constants.BillerNumberCode.REOR_CODE;
				trxCode = Constants.EndpointCode.REOR_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
				filepath += Constants.DirectoryFTPConstants.IAR_DIR;
				billerCodeNumber = Constants.BillerNumberCode.IAR_CODE;
				trxCode = Constants.EndpointCode.IAR_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
				filepath += Constants.DirectoryFTPConstants.UNAR_DIR;
				billerCodeNumber = Constants.BillerNumberCode.UNAR_CODE;
				trxCode = Constants.EndpointCode.UNAR_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
				filepath += Constants.DirectoryFTPConstants.IKRAP_DIR;
				billerCodeNumber = Constants.BillerNumberCode.IKRAP_CODE;
				trxCode = Constants.EndpointCode.IKRAP_CODE;
			} else if (transactionCode.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
				filepath += Constants.DirectoryFTPConstants.PAP_DIR;
				billerCodeNumber = Constants.BillerNumberCode.PAP_CODE;
				trxCode = Constants.EndpointCode.PAP_CODE;
			}

			trxCode += ".PAY";
			filepath += "/MT940" + trxDateNameString + billerCodeNumber + ".xls";

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date trxDate = null;

			try {
				trxDate = formatter.parse(DateUtil.convertDateToString(reconcileCalendar.getTime(), "dd-MM-yyyy"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (cekFileInFileSystem(filepath, transactionCode, channelCode[0]) == true) {
				mt940Data.putAll(xlsFile.getMt940Data(filepath));
			} else {
				continue;
			}

			if (mt940Data.isEmpty()) {
				System.out.println("DATA IS EMPTY");
			} else {
				listInvoiceCode = mt940Data.keySet();
				searchResult.addAll(trxLogManager.findTransactionLogsWebadmin(mt940Data, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "all", mapCount, mapCountAmount));
			}

			ReportReconcileAction reportReconcileAction = new ReportReconcileAction();
			String reportDir = trxLogManager.findParamValueByParamName("reconcileEod.report.path") + channelCode[0].toLowerCase() + "/" + transactionCode;

			// Date Laporan Reconcile Dilakukan
			putToParam(params, mapCount, mapCountAmount, new Date(), channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"), "");

			params.put("trxDate", DateUtil.convertDateToString(new Date(), "dd-MM-yyyy"));
			params.put("channelCode", channelCode[1]);
			params.put("bankName", channelCode[0]);
			params.put("reconcileStatus", "All");

			params.put("settled", mapCount.get("settled").toString());
			params.put("notSettled", mapCount.get("notSettled").toString());
			params.put("unconfirmed", mapCount.get("unconfirmed").toString());
			params.put("amountSettled", mapCountAmount.get("amountSettled").toString());
			params.put("amountNotSettled", mapCountAmount.get("amountNotSettled").toString());
			params.put("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed").toString());
			params.put("totalAmount", mapCountAmount.get("totalAmount").toString());

			params.put("headerImg", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"));
			params.put("title", "");
			// result.put("params", params);
			// result.put("jrxmlPath", jrxmlRealPath);
			// result.put("jasperPath", jasperRealPath);
			// result.put("searchResult", searchResult);

			reportReconcileAction.createReportWithoutContextByDataType(params, searchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "PDF", "original");
			reportReconcileAction.createReportWithoutContextByDataType(params, searchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "XLS", "original");

			finalSearchResult.addAll(AutoReconcileToPaid(searchResult, mapCount, mapCountAmount));
			// finalSearchResult.addAll(searchResult);

			putToParam(finalParams, mapCount, mapCountAmount, new Date(), channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"), "Hasil Rekonsiliasi Otomatis");

			finalParams.put("trxDate", DateUtil.convertDateToString(new Date(), "dd-MM-yyyy"));
			finalParams.put("channelCode", channelCode[1]);
			finalParams.put("bankName", channelCode[0]);
			finalParams.put("reconcileStatus", "All");

			finalParams.put("settled", mapCount.get("settled").toString());
			finalParams.put("notSettled", mapCount.get("notSettled").toString());
			finalParams.put("unconfirmed", mapCount.get("unconfirmed").toString());
			finalParams.put("amountSettled", mapCountAmount.get("amountSettled").toString());
			finalParams.put("amountNotSettled", mapCountAmount.get("amountNotSettled").toString());
			finalParams.put("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed").toString());
			finalParams.put("totalAmount", mapCountAmount.get("totalAmount").toString());

			finalParams.put("headerImg", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"));
			finalParams.put("title", "Hasil Rekonsiliasi Otomatis");

			reportReconcileAction.createReportWithoutContextByDataType(finalParams, finalSearchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "PDF", "reconciled");
			String reportPath = reportReconcileAction.createReportWithoutContextByDataType(finalParams, finalSearchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "XLS", "reconciled");

			saveToLogQuery(new Date(), reportPath, transactionCode, channelCode[0]);

			// for detail in email
			settled += mapCount.get("settled");
			unSettled += mapCount.get("notSettled");
			needConfirmation += mapCount.get("unconfirmed");
			paymentAmountSettled += mapCountAmount.get("amountSettled");
			paymentAmountUnSettled += mapCountAmount.get("amountNotSettled");
			payemntAmountNeedConfirmation += mapCountAmount.get("amountUnconfirmed");

			// DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
			// Calendar cal = Calendar.getInstance();
			// String date = dateFormat.format(cal.getTime());
			// listFile.put(reportPath + "_" + "original" + "_" + channelCode[0] + ".pdf", reportDir + "/" + date);
			// listFile.put(reportPath + "_" + "original" + "_" + channelCode[0] + ".xls", reportDir + "/" + date);
			// listFile.put(reportPath + "_" + "reconciled" + "_" + channelCode[0] + ".pdf", reportDir + "/" + date);
			// listFile.put(reportPath + "_" + "reconciled" + "_" + channelCode[0] + ".xls", reportDir + "/" + date);

		}

		if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
			sendMail(listFile, settled, unSettled, needConfirmation, paymentAmountSettled, paymentAmountUnSettled, payemntAmountNeedConfirmation, transactionCode);
		}
	}*/
	
	public void doGetReconcileListByMt940EodNew(String billerCode, List<String> channelList) throws FileNotFoundException, JRException, IOException, SQLException {

		ReportReconcileAction reportReconcileAction = new ReportReconcileAction();
		
		String mt940BasePath 			= systemParameter.findParamValueByParamName("reconcileEod.mt940.path");
		String jrxmlRealPath 			= systemParameter.findParamValueByParamName("reconcileEod.jrxml.path");
		String jasperRealPath 			= systemParameter.findParamValueByParamName("reconcileEod.jasper.path");
		Map<String, String> params 		= new HashMap<String, String>();
		Map<String, String> finalParams = new HashMap<String, String>();
		
		String trxCode 		= "";
		String paymentType 	= "";
		
		String filepath 	= null;
		String filename 	= null;
		String channelCode[]= null;
		
		Map<String, String> listFile = new HashMap<String, String>();
		Integer settled 					= 0;
		Integer unSettled 					= 0;
		Integer needConfirmation 			= 0;
		Long paymentAmountSettled 			= 0L;
		Long paymentAmountUnSettled 		= 0L;
		Long paymentAmountNeedConfirmation 	= 0L;

		Calendar reconcileCalendar 		= Calendar.getInstance();
		reconcileCalendar.setTime(new Date());
		reconcileCalendar.add(Calendar.DATE, -1);
		
		Iterator<String> channelCodeListIterator = channelList.iterator();
		while (channelCodeListIterator.hasNext()) {
			List<ReconcileDto> searchResult 		= new ArrayList<ReconcileDto>();
			List<ReconcileDto> finalSearchResult 	= new ArrayList<ReconcileDto>();
			
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
			
			channelCode = channelCodeListIterator.next().split("-");
			
			if (billerCode.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
				trxCode = Constants.EndpointCode.BHP_CODE;
				paymentType = Constants.BillerNumberCode.BHP_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
				trxCode = Constants.EndpointCode.PERANGKAT_CODE;
				paymentType = Constants.BillerNumberCode.PERANGKAT_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
				trxCode = Constants.EndpointCode.SKOR_CODE;
				paymentType = Constants.BillerNumberCode.SKOR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
				trxCode = Constants.EndpointCode.REOR_CODE;
				paymentType = Constants.BillerNumberCode.REOR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
				trxCode = Constants.EndpointCode.IAR_CODE;
				paymentType = Constants.BillerNumberCode.IAR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
				trxCode = Constants.EndpointCode.UNAR_CODE;
				paymentType = Constants.BillerNumberCode.UNAR_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
				trxCode = Constants.EndpointCode.IKRAP_CODE;
				paymentType = Constants.BillerNumberCode.IKRAP_CODE;
			} else if (billerCode.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
				trxCode = Constants.EndpointCode.PAP_CODE;
				paymentType = Constants.BillerNumberCode.PAP_CODE;
			}
			
			trxCode += ".PAY";
	
			HashMap<String, Object[]> mxData 	= new HashMap<String, Object[]>();
			HashMap<String, Object[]> mt940Data = new HashMap<String, Object[]>();
			Set<String> listInvoiceCode 		= new HashSet<String>();
	
			Date trxDate 				= null;
			SimpleDateFormat formatter 	= new SimpleDateFormat("dd-MM-yyyy");
			try {
				trxDate = formatter.parse(DateUtil.convertDateToString(reconcileCalendar.getTime(), "dd-MM-yyyy"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mxData.putAll(trxLogManager.
					findAllTransactionLogsWebadminReconcileByDate(channelCode[1], trxCode, new String[] { "00" }, new String[] { "00" }, trxDate));
			mt940Data.putAll(armgmtManagerImpl.getMT940(trxDate, null, paymentType, channelCode[0]));
			System.out.println("trxDate : " + trxDate);
			System.out.println("paymentType : " + paymentType);
			System.out.println("channelCode[0] : " + channelCode[0]);
			
			if(mt940Data.isEmpty() && mxData.isEmpty()) {
				System.out.println("DATA IS EMPTY");
			}else{
				if(!mt940Data.isEmpty()){
					listInvoiceCode = mt940Data.keySet();
				}else if(!mxData.isEmpty()){
					listInvoiceCode = mxData.keySet();
				}
				searchResult.addAll(trxLogManager.findTransactionLogsWebadmin(mt940Data, mxData, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "all", mapCount, mapCountAmount));
			}
	
//			String reportDir = trxLogManager.findParamValueByParamName("reconcileEod.report.path") + billerCode;
			String reportDir = trxLogManager.findParamValueByParamName("reconcileEod.report.path") + channelCode[0].toLowerCase() + "/" + billerCode;
			putToParam(params, mapCount, mapCountAmount, new Date(), channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"), "");
	
			reportReconcileAction.createReportWithoutContextByDataType(params, searchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "PDF", "original");
			reportReconcileAction.createReportWithoutContextByDataType(params, searchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "XLS", "original");
	
			finalSearchResult.addAll(autoReconcileToPaid(searchResult, mapCount, mapCountAmount, billerCode));
	
			putToParam(finalParams, mapCount, mapCountAmount, new Date(), channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileEod.report.image.path"), "Hasil Rekonsiliasi Otomatis");
	
			reportReconcileAction.createReportWithoutContextByDataType(finalParams, finalSearchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "PDF", "reconciled");
			String reportPath = reportReconcileAction.createReportWithoutContextByDataType(finalParams, finalSearchResult, jasperRealPath, jrxmlRealPath, reportDir, channelCode[0], "XLS", "reconciled");
	
			saveToLogQuery(new Date(), reportPath, billerCode, channelCode[0]);
	
			// for detail in email
			settled += mapCount.get("settled");
			unSettled += mapCount.get("notSettled");
			needConfirmation += mapCount.get("unconfirmed");
			paymentAmountSettled += mapCountAmount.get("amountSettled");
			paymentAmountUnSettled += mapCountAmount.get("amountNotSettled");
			paymentAmountNeedConfirmation += mapCountAmount.get("amountUnconfirmed");
	
			if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
				sendMail(listFile, settled, unSettled, needConfirmation, paymentAmountSettled, paymentAmountUnSettled, paymentAmountNeedConfirmation, billerCode);
			}
		}
	}
	
	private List<ReconcileDto> autoReconcileToPaid(List<ReconcileDto> list, Map<String, Integer> mapCount, Map<String, Long> mapCountAmount, String billerCode) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);

		List<ReconcileDto> reconciledList = new ArrayList<ReconcileDto>();
		reconciledList.addAll(list);

		String trxDate = DateUtil.convertDateToString(cal.getTime(), "dd-MM-yyyy");

		Date paymentDateSims = null;
		Date invoiceDueDate = null;
		Integer newSettled = 0;
		long amountNewSettled = 0;
		Integer newUnconfirmed = 0;
		long amountNewUnconfirmed = 0;

		for (int i = 0; i < reconciledList.size(); i++) {

			if (reconciledList.get(i).getMt940Status().equalsIgnoreCase("Paid") && (reconciledList.get(i).getSimsStatus().equalsIgnoreCase("Unpaid") || reconciledList.get(i).getSimsStatus().equalsIgnoreCase("Cancelled"))
					&& (reconciledList.get(i).getTrxId() != null && reconciledList.get(i).getTrxId().equalsIgnoreCase("-"))) {

				System.out.println("Auto Reconcile Invoice No : " + reconciledList.get(i).getInvoiceNo());
				
				if(billerCode.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)){
					externalBillingSystem.updateInvoiceEod(reconciledList.get(i).getInvoiceNo(), cal.getTime(), "Auto Reconcile By WebAdmin");
				}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)){
					sertifikasiManagerImpl.updateInvoiceEodSertifikasi(reconciledList.get(i), cal.getTime(), "Auto Reconcile By WebAdmin");
				}else if(billerCode.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)){
					pengujianManagerImpl.updateInvoiceEodPengujian(reconciledList.get(i), cal.getTime(), "Auto Reconcile By WebAdmin");
				}

				if (reconciledList.get(i).getInvoiceDendaNo() != null && !reconciledList.get(i).getInvoiceDendaNo().equalsIgnoreCase("-")) {
					reconciledList.get(i).setStatusDenda("Cancelled");
				}

				reconciledList.get(i).setSimsStatus("Paid");
				reconciledList.get(i).setPaymentDateSims(trxDate);

				if (reconciledList.get(i).getInvoiceDueDate() != null && !reconciledList.get(i).getInvoiceDueDate().equals("-")) {

					try {
						paymentDateSims = formatter.parse(reconciledList.get(i).getPaymentDateSims());
						invoiceDueDate 	= formatter.parse(reconciledList.get(i).getInvoiceDueDate());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (invoiceDueDate.compareTo(paymentDateSims) >= 0) {
						reconciledList.get(i).setReconcileStatus("Settled");
						if (reconciledList.get(i).getInvoiceDendaNo() != null && reconciledList.get(i).getInvoiceDendaNo().equalsIgnoreCase("-") && reconciledList.get(i).getStatusDenda().equalsIgnoreCase("Paid")) {
							amountNewSettled = amountNewSettled + Long.parseLong(reconciledList.get(i).getTrxAmountDenda().replaceAll("(\\D)", ""));
						} else {
							amountNewSettled = amountNewSettled + Long.parseLong(reconciledList.get(i).getTrxAmount().replaceAll("(\\D)", ""));
						}
						newSettled++;
					} else {
						reconciledList.get(i).setReconcileStatus("Need Confirmation/Manual Payment");
						if (reconciledList.get(i).getInvoiceDendaNo() != null && reconciledList.get(i).getInvoiceDendaNo().equalsIgnoreCase("-") && reconciledList.get(i).getStatusDenda().equalsIgnoreCase("Paid")) {
							amountNewUnconfirmed = amountNewUnconfirmed + Long.parseLong(reconciledList.get(i).getTrxAmountDenda().replaceAll("(\\D)", ""));
						} else {
							amountNewUnconfirmed = amountNewUnconfirmed + Long.parseLong(reconciledList.get(i).getTrxAmount().replaceAll("(\\D)", ""));
						}
						newUnconfirmed++;
					}
				}

			}
		}

		System.out.println("Auto Reconcile SUCCESS !");
		mapCount.put("settled", mapCount.get("settled") + newSettled);
		mapCount.put("notSettled", mapCount.get("notSettled") - newSettled);
		mapCount.put("settledReport", mapCount.get("settled") + newSettled);
		mapCount.put("notSettledReport", mapCount.get("notSettled") - newSettled);

		mapCount.put("unconfirmed", mapCount.get("unconfirmed") + newUnconfirmed);
		mapCount.put("notSettled", mapCount.get("notSettled") - newUnconfirmed);
		mapCount.put("unconfirmedReport", mapCount.get("unconfirmed") + newUnconfirmed);
		mapCount.put("notSettledReport", mapCount.get("notSettled") - newUnconfirmed);

		mapCountAmount.put("amountSettled", mapCountAmount.get("amountSettled") + amountNewSettled);
		mapCountAmount.put("amountNotSettled", mapCountAmount.get("amountNotSettled") - amountNewSettled);
		mapCountAmount.put("amountSettledReport", mapCountAmount.get("amountSettled") + amountNewSettled);
		mapCountAmount.put("amountNotSettledReport", mapCountAmount.get("amountNotSettled") - amountNewSettled);

		mapCountAmount.put("amountUnconfirmed", mapCountAmount.get("amountUnconfirmed") + amountNewUnconfirmed);
		mapCountAmount.put("amountNotSettled", mapCountAmount.get("amountNotSettled") - amountNewUnconfirmed);
		mapCountAmount.put("amountUnconfirmedReport", mapCountAmount.get("amountUnconfirmed") + amountNewUnconfirmed);
		mapCountAmount.put("amountNotSettledReport", mapCountAmount.get("amountNotSettled") - amountNewUnconfirmed);

		System.out.println("Settled: " + mapCount.get("settled"));
		System.out.println("Unsettled: " + mapCount.get("notSettled"));
		System.out.println("Need Confirmation: " + mapCount.get("unconfirmed"));

		System.out.println("Amount Settled: Rp " + mapCountAmount.get("amountSettled"));
		System.out.println("Amount Unsettled: Rp " + mapCountAmount.get("amountNotSettled"));
		System.out.println("Amount Need Confirmation: Rp " + mapCountAmount.get("amountUnconfirmed"));

		return reconciledList;
	}
	
	public void saveToLogQuery(Date reportTime, String reportDocument, String trxType, String bankName) throws SQLException {

		String sql = "INSERT INTO RECONCILE_REPORT_LOG (REPORT_TIME,REPORT_DOCUMENT,TRANSACTION_TYPE,BANK_NAME) " + "VALUES (?,?,?,?)";

		String sql2 = "SELECT * FROM "
				    + "RECONCILE_REPORT_LOG "
				    + "WHERE BANK_NAME = ? "
				    + "AND REPORT_DOCUMENT = ? "
				    + "AND TRANSACTION_TYPE = ? ";
		Connection con = null;
		PreparedStatement ps, s = null;
		ResultSet rs = null;
		
		try {
			con = dataSourceWebapp.getConnection();
			ps = con.prepareStatement(sql2);
			ps.setString(1, bankName);
			ps.setString(2, reportDocument);
			ps.setString(3, trxType);
			rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println(new Date () + "Data already exist");
				log.info("Data already exist");
			} else {
				s = con.prepareStatement(sql);
				s.setDate(1, new java.sql.Date(reportTime.getTime()));
				s.setString(2, reportDocument);
				s.setString(3, trxType);
				s.setString(4, bankName);
				s.executeUpdate();
				s.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.trace(e);
			throw e;
		} finally {

			con.close();
		}
	}

	public Boolean cekFileInFileSystem(String filepath, String transactionType, String bankName) {
		String errorMessage = null;
		File file 			= new File(filepath);

		if (!file.exists()) {
			errorMessage = "File MT940 " + transactionType + " pada bank " + bankName + " Tidak Ditemukan Pada Path : " + filepath;
			System.out.println(errorMessage);
			return false;
		}

		return true;
	}

	public boolean sendMail(Map<String, String> listFile, Integer settled, Integer unSettled, Integer needConfirmation, Long paymentSettled, Long paymentUnSettled, Long paymentNeedConfirmation, String endpointCode) {
		DecimalFormat numFormat = new DecimalFormat("#,###,###.00");

		Integer totalInvoice = settled + unSettled + needConfirmation;
		Long totalAmount = paymentNeedConfirmation + paymentSettled + paymentUnSettled;

		String emailList = systemParameter.findParamValueByParamName("webadmin.reconcileEod.emailList");
		String smtpHost = systemParameter.findParamValueByParamName("webadmin.reconcileEod.smtpHost");
		String smtpAuth = systemParameter.findParamValueByParamName("webadmin.reconcileEod.smtpAuth");
		String smptpPort = systemParameter.findParamValueByParamName("webadmin.reconcileEod.smtpPort");
		String mailServer = systemParameter.findParamValueByParamName("webadmin.reconcileEod.mailServer.mail");
		String passwordMailServer = systemParameter.findParamValueByParamName("webadmin.reconcileEod.mailServer.password");

		String endpointName = getEndpointName(endpointCode);

		SendMail mail = new SendMail();
		mail.setSubject("Reconcile Daily Report " + (endpointName != null ? endpointName : ""));
		mail.setUsername(mailServer);
		mail.setPassword(passwordMailServer);
		mail.setListFile(listFile);
		mail.setToEmail(emailList.split(","));
		mail.setSmtpHost(smtpHost);
		mail.setSmtpAuth(smtpAuth);
		mail.setSmtpPort(smptpPort);
		mail.setText("<h1>Reconcile Report</h1>" + "<table border='1'>" + "<tr>" + "<th>Status</th>" + "<th>Total Invoice</th>" + "<th>Total Amount</th>" + "</tr>" + "<tr>" + "<td>Settled</td> " + "<td align='right'>" + settled + "</td> " + "<td align='right'>"
				+ (paymentSettled != 0 ? numFormat.format(paymentSettled) : 0) + "</td> " + "</tr>" + "<tr>" + "<td>Unsettled</td>" + "<td align='right'>" + unSettled + "</td>" + "<td align='right'>" + (paymentUnSettled != 0 ? numFormat.format(paymentUnSettled) : 0) + "</td>" + "</tr>" + "<tr>"
				+ "<td>Need Confirmation</td>" + "<td align='right'>" + needConfirmation + "</td>" + "<td align='right'>" + (paymentNeedConfirmation != 0 ? numFormat.format(paymentNeedConfirmation) : 0) + "</td>" + "</tr>" + "<tr>" + "<td>Total</td>" + "<td align='right'>" + totalInvoice + "</td>"
				+ "<td align='right'>" + (totalAmount != 0 ? numFormat.format(totalAmount) : 0) + "</td>" + "</tr>" + "</table>");
		mail.setHtml(true);

		return mail.send();
	}

	public String getEndpointName(String endpointCode) {
		String sql = "SELECT name FROM endpoints WHERE code = ? ";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		String endpointName = null;

		try {
			con = dataSource.getConnection();
			stat = con.prepareStatement(sql);
			stat.setString(1, endpointCode);
			rs = stat.executeQuery();
			while (rs.next()) {
				endpointName = rs.getString("name");
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return endpointName;
	}

	Map<String, String> putToParam(Map<String, String> param, Map<String, Integer> paramCount, Map<String, Long> mapCountAmountSettled, Date trxDate, String channelCode, String bankName, String reconcileStatus, String headerImgPath, String reportTitle) {

		param.put("trxDate", DateUtil.convertDateToString(trxDate, "dd-MM-yyyy"));
		param.put("bankName", bankName);
		param.put("channelCode", channelCode);
		param.put("reconcileStatus", reconcileStatus);

		param.put("settled", paramCount.get("settled").toString());
		param.put("notSettled", paramCount.get("notSettled").toString());
		param.put("unconfirmed", paramCount.get("unconfirmed").toString());
		param.put("amountSettled", mapCountAmountSettled.get("amountSettled").toString());
		param.put("amountNotSettled", mapCountAmountSettled.get("amountNotSettled").toString());
		param.put("amountUnconfirmed", mapCountAmountSettled.get("amountUnconfirmed").toString());
		param.put("totalAmount", mapCountAmountSettled.get("totalAmount").toString());
		param.put("totalAmountDenda", mapCountAmountSettled.get("totalAmountDenda").toString());

		param.put("headerImg", headerImgPath);
		param.put("title", reportTitle);

		return param;
	}
}
