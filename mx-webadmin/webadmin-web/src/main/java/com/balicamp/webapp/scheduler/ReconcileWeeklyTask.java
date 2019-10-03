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
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.action.report.ReportReconcileAction;
import com.balicamp.webapp.action.report.XlstoStringConverter;
import com.balicamp.webapp.ftp.FTPManager;
import com.balicamp.webapp.util.SendMail;

@SuppressWarnings("serial")
public class ReconcileWeeklyTask extends HttpServlet {
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

	private XlstoStringConverter xlsFile;

	public ReconcileWeeklyTask() {
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

	public void reconcileWeekly() throws FileNotFoundException, JRException, IOException, ServletException, SQLException {
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
		System.out.println("RECONCILE WEEKLY START");
		Iterator<String> billerIterator = billerList.iterator();
		while (billerIterator.hasNext()) {
//			doGetReconcileListByMt940Weekly(channelList, billerIterator.next());
			doGetReconcileListByMt940WeeklyNew(billerIterator.next(), channelList);
		}
		System.out.println("RECONCILE WEEKLY FINISH");
	}
	
	public void doGetReconcileListByMt940WeeklyNew(String billerCode, List<String> channelList) throws FileNotFoundException, JRException, IOException, SQLException {

		String mt940BasePath = systemParameter.findParamValueByParamName("reconcileWeekly.mt940.path");
		String jrxmlRealPathSettled = systemParameter.findParamValueByParamName("reconcileWeekly.jrxml.settled.path");
		String jasperRealPathSettled = systemParameter.findParamValueByParamName("reconcileWeekly.jasper.settled.path");
		String jrxmlRealPathUnsettled = systemParameter.findParamValueByParamName("reconcileWeekly.jrxml.unsettled.path");
		String jasperRealPathUnsettled = systemParameter.findParamValueByParamName("reconcileWeekly.jasper.unsettled.path");
		
		String jrxmlRealPathAll = systemParameter.findParamValueByParamName("reconcileWeekly.jrxml.all.path");
		String jasperRealPathAll = systemParameter.findParamValueByParamName("reconcileWeekly.jasper.all.path");
		
		Map<String, String> paramsSettled = new HashMap<String, String>();
		Map<String, String> paramsUnsettled = new HashMap<String, String>();
		
		Map<String, String> params 		= new HashMap<String, String>();

		Calendar reconcileCalendar = Calendar.getInstance();
		reconcileCalendar.setTime(new Date());

		String trxCode 				= null;
		String trxDateNameString	= "";
		

		Map<String, String> listFile = new HashMap<String, String>();
		Integer settled = 0;
		Integer unSettled = 0;
		Integer needConfirmation = 0;
		Long paymentAmountSettled = 0L;
		Long paymentAmountUnSettled = 0L;
		Long payemntAmountNeedConfirmation = 0L;

		List<String> channelCodeList = channelList;
		Iterator<String> channelCodeListIterator = channelCodeList.iterator();

		while (channelCodeListIterator.hasNext()) {

			List<ReconcileDto> searchResultSettled = new ArrayList<ReconcileDto>();
			List<ReconcileDto> searchResultUnsettled = new ArrayList<ReconcileDto>();
			
//			untuk akomodir permintaan rekon dengan status ALL
			List<ReconcileDto> searchResultAll = new ArrayList<ReconcileDto>();

			Map<String, Integer> mapCountSettled = new HashMap<String, Integer>();
			mapCountSettled.put("settled", 0);
			mapCountSettled.put("notSettled", 0);
			mapCountSettled.put("unconfirmed", 0);
			Map<String, Long> mapCountAmountSettled = new HashMap<String, Long>();
			mapCountAmountSettled.put("amountSettled", (long) 0);
			mapCountAmountSettled.put("amountNotSettled", (long) 0);
			mapCountAmountSettled.put("amountUnconfirmed", (long) 0);
			mapCountAmountSettled.put("totalAmount", (long) 0);
			mapCountAmountSettled.put("totalAmountDenda", (long) 0);
			Map<String, Integer> mapCountUnsettled = new HashMap<String, Integer>();
			mapCountUnsettled.put("settled", 0);
			mapCountUnsettled.put("notSettled", 0);
			mapCountUnsettled.put("unconfirmed", 0);
			Map<String, Long> mapCountAmountUnsettled = new HashMap<String, Long>();
			mapCountAmountUnsettled.put("amountSettled", (long) 0);
			mapCountAmountUnsettled.put("amountNotSettled", (long) 0);
			mapCountAmountUnsettled.put("amountUnconfirmed", (long) 0);
			mapCountAmountUnsettled.put("totalAmount", (long) 0);
			mapCountAmountUnsettled.put("totalAmountDenda", (long) 0);
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

			String[] channelCode = channelCodeListIterator.next().split("-");
			reconcileCalendar.setTime(new Date());

			for (int x = 0; x < 7; x++) { // Mundur dari Hari Jumat sampai dengan Hari Senin

				HashMap<String, Object[]> mt940Data = new HashMap<String, Object[]>();
				Set<String> listInvoiceCode = new HashSet<String>();
				reconcileCalendar.add(Calendar.DATE, -1);
				trxDateNameString = DateUtil.convertDateToString(reconcileCalendar.getTime(), "yyyyMMdd");

//				int i = 0;
				String paymentType = "";

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

				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date trxDate = null;

				try {
					trxDate = formatter.parse(DateUtil.convertDateToString(reconcileCalendar.getTime(), "dd-MM-yyyy"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mt940Data.putAll(armgmtManagerImpl.getMT940(trxDate, null, paymentType, channelCode[0]));
				
				if (mt940Data.isEmpty()) {
					System.out.println("DATA IS EMPTY");
				} else {
					listInvoiceCode = mt940Data.keySet();

					searchResultSettled.addAll(trxLogManager.findTransactionLogsWebadmin(
							mt940Data, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "Settled", mapCountSettled, mapCountAmountSettled));
					searchResultUnsettled.addAll(trxLogManager.findTransactionLogsWebadmin(
							mt940Data, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "Unsettled/Need Confirmation", mapCountUnsettled, mapCountAmountUnsettled));

					searchResultAll.addAll(trxLogManager.findTransactionLogsWebadmin(
							mt940Data, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "All", mapCount, mapCountAmount));
					
				}
			}

			ReportReconcileAction reportReconcileAction = new ReportReconcileAction();

//			String reportDir = trxLogManager.findParamValueByParamName("reconcileWeekly.report.path") + billerCode;
			String reportDir = trxLogManager.findParamValueByParamName("reconcileWeekly.report.path") + channelCode[0].toLowerCase() + "/" + billerCode;

			// Date Laporan Reconcile Dilakukan

			Calendar calStart = Calendar.getInstance();
			calStart.setTime(new Date());
			calStart.add(Calendar.DATE, -7);
			Date startDate = calStart.getTime();

			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(new Date());
			calEnd.add(Calendar.DATE, -1);
			Date endDate = calEnd.getTime();

			putToParam(paramsSettled, mapCountSettled, mapCountAmountSettled, startDate, endDate, channelCode[1], channelCode[0], "Settled", systemParameter.findParamValueByParamName("reconcileWeekly.report.image.path"), "");
			putToParam(paramsUnsettled, mapCountUnsettled, mapCountAmountUnsettled, startDate, endDate, channelCode[1], channelCode[0], "Unsettled/Need Confirmation", systemParameter.findParamValueByParamName("reconcileWeekly.report.image.path"), "");
			
			putToParam(params, mapCount, mapCountAmount, startDate, endDate, channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileWeekly.report.image.path"), "");

			reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsSettled, searchResultSettled, jasperRealPathSettled, jrxmlRealPathSettled, reportDir, channelCode[0], "PDF", "settled");
			String reportPathSettled = reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsSettled, searchResultSettled, jasperRealPathSettled, jrxmlRealPathSettled, reportDir, channelCode[0], "XLS", "settled");
			saveToLogQuery(new Date(), reportPathSettled, "Settled", billerCode, channelCode[0]);

			ArrayList<ReconcileDto> reconcileUnsettled = new ArrayList<ReconcileDto>();
			ArrayList<ReconcileDto> reconcileUnconfirmed = new ArrayList<ReconcileDto>();
			for (ReconcileDto dataReconcile : searchResultUnsettled) {
				if (dataReconcile.getReconcileStatus().equalsIgnoreCase("Unsettled")) {
					reconcileUnsettled.add(dataReconcile);
				} else if (dataReconcile.getReconcileStatus().equalsIgnoreCase("Need Confirmation/Manual Payment")) {
					reconcileUnconfirmed.add(dataReconcile);
				}
			}
			searchResultUnsettled.clear();
			searchResultUnsettled.addAll(reconcileUnsettled);
			searchResultUnsettled.addAll(reconcileUnconfirmed);
			reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsUnsettled, searchResultUnsettled, jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir, channelCode[0], "PDF", "unsettled");
			String reportPathUnsettled = reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsUnsettled, searchResultUnsettled, jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir, channelCode[0], "XLS", "unsettled");
			saveToLogQuery(new Date(), reportPathUnsettled, "Unsettled/Need Confirmation", billerCode, channelCode[0]);

			reportReconcileAction.createReportWithoutContextByDataTypeWeekly(params, searchResultAll, jasperRealPathAll, jrxmlRealPathAll, reportDir, channelCode[0], "PDF", "all");
			String reportPathAll = reportReconcileAction.createReportWithoutContextByDataTypeWeekly(params, searchResultAll, jasperRealPathAll, jrxmlRealPathAll, reportDir, channelCode[0], "XLS", "all");
			saveToLogQuery(new Date(), reportPathAll, "All", billerCode, channelCode[0]);
			
			// for detail in email
			settled += mapCountSettled.get("settled");
			unSettled += mapCountUnsettled.get("notSettled");
			needConfirmation += mapCountUnsettled.get("unconfirmed");
			paymentAmountSettled += mapCountAmountSettled.get("amountSettled");
			paymentAmountUnSettled += mapCountAmountUnsettled.get("amountNotSettled");
			

		}

		if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
			sendMail(listFile, settled, unSettled, needConfirmation, paymentAmountSettled, paymentAmountUnSettled, payemntAmountNeedConfirmation, billerCode);
		}

	}
/*
	public void doGetReconcileListByMt940Weekly(List<String> channelList, String transactionCode) throws FileNotFoundException, JRException, IOException, SQLException {

		String mt940BasePath = systemParameter.findParamValueByParamName("reconcileWeekly.mt940.path");
		String jrxmlRealPathSettled = systemParameter.findParamValueByParamName("reconcileWeekly.jrxml.settled.path");
		String jasperRealPathSettled = systemParameter.findParamValueByParamName("reconcileWeekly.jasper.settled.path");
		String jrxmlRealPathUnsettled = systemParameter.findParamValueByParamName("reconcileWeekly.jrxml.unsettled.path");
		String jasperRealPathUnsettled = systemParameter.findParamValueByParamName("reconcileWeekly.jasper.unsettled.path");
		Map<String, String> paramsSettled = new HashMap<String, String>();
		Map<String, String> paramsUnsettled = new HashMap<String, String>();

		Calendar reconcileCalendar = Calendar.getInstance();
		reconcileCalendar.setTime(new Date());

		String trxDateNameString = "";
		// String trxDateNameString = "20151020";
		String filepath = null;
		String filename = null;
		String channelCode[] = null;
		String billerCodeNumber = null;
		String trxCode = null;

		Map<String, String> listFile = new HashMap<String, String>();
		Integer settled = 0;
		Integer unSettled = 0;
		Integer needConfirmation = 0;
		Long paymentAmountSettled = 0L;
		Long paymentAmountUnSettled = 0L;
		Long payemntAmountNeedConfirmation = 0L;

		List<String> channelCodeList = channelList;

		Iterator<String> channelCodeListIterator = channelCodeList.iterator();

		while (channelCodeListIterator.hasNext()) {

			List<ReconcileDto> searchResultSettled = new ArrayList<ReconcileDto>();
			List<ReconcileDto> searchResultUnsettled = new ArrayList<ReconcileDto>();

			Map<String, Integer> mapCountSettled = new HashMap<String, Integer>();
			mapCountSettled.put("settled", 0);
			mapCountSettled.put("notSettled", 0);
			mapCountSettled.put("unconfirmed", 0);
			Map<String, Long> mapCountAmountSettled = new HashMap<String, Long>();
			mapCountAmountSettled.put("amountSettled", (long) 0);
			mapCountAmountSettled.put("amountNotSettled", (long) 0);
			mapCountAmountSettled.put("amountUnconfirmed", (long) 0);
			mapCountAmountSettled.put("totalAmount", (long) 0);
			mapCountAmountSettled.put("totalAmountDenda", (long) 0);
			Map<String, Integer> mapCountUnsettled = new HashMap<String, Integer>();
			mapCountUnsettled.put("settled", 0);
			mapCountUnsettled.put("notSettled", 0);
			mapCountUnsettled.put("unconfirmed", 0);
			Map<String, Long> mapCountAmountUnsettled = new HashMap<String, Long>();
			mapCountAmountUnsettled.put("amountSettled", (long) 0);
			mapCountAmountUnsettled.put("amountNotSettled", (long) 0);
			mapCountAmountUnsettled.put("amountUnconfirmed", (long) 0);
			mapCountAmountUnsettled.put("totalAmount", (long) 0);
			mapCountAmountUnsettled.put("totalAmountDenda", (long) 0);

			channelCode = channelCodeListIterator.next().split("-");
			reconcileCalendar.setTime(new Date());

			for (int x = 0; x < 7; x++) { // Mundur dari Hari Jumat sampai dengan Hari Senin

				HashMap<String, String> mt940Data = new HashMap<String, String>();
				Set<String> listInvoiceCode = new HashSet<String>();
				reconcileCalendar.add(Calendar.DATE, -1);
				trxDateNameString = DateUtil.convertDateToString(reconcileCalendar.getTime(), "yyyyMMdd");

				int i = 0;
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

					searchResultSettled.addAll(trxLogManager.findTransactionLogsWebadmin(mt940Data, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "Settled", mapCountSettled, mapCountAmountSettled));
					searchResultUnsettled.addAll(trxLogManager.findTransactionLogsWebadmin(mt940Data, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "Unsettled/Need Confirmation", mapCountUnsettled, mapCountAmountUnsettled));
					
				}
			}

			ReportReconcileAction reportReconcileAction = new ReportReconcileAction();

			String reportDir = trxLogManager.findParamValueByParamName("reconcileWeekly.report.path") + channelCode[0].toLowerCase() + "/" + transactionCode;

			// Date Laporan Reconcile Dilakukan

			Calendar calStart = Calendar.getInstance();
			calStart.setTime(new Date());
			calStart.add(Calendar.DATE, -7);
			Date startDate = calStart.getTime();

			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(new Date());
			calEnd.add(Calendar.DATE, -1);
			Date endDate = calEnd.getTime();

			putToParam(paramsSettled, mapCountSettled, mapCountAmountSettled, startDate, endDate, channelCode[1], channelCode[0], "Settled", systemParameter.findParamValueByParamName("reconcileWeekly.report.image.path"), "");

			putToParam(paramsUnsettled, mapCountUnsettled, mapCountAmountUnsettled, startDate, endDate, channelCode[1], channelCode[0], "Unsettled/Need Confirmation", systemParameter.findParamValueByParamName("reconcileWeekly.report.image.path"), "");

			// result.put("params", params);
			// result.put("jrxmlPath", jrxmlRealPath);
			// result.put("jasperPath", jasperRealPath);
			// result.put("searchResult", searchResult);

			reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsSettled, searchResultSettled, jasperRealPathSettled, jrxmlRealPathSettled, reportDir, channelCode[0], "PDF", "settled");
			String reportPathSettled = reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsSettled, searchResultSettled, jasperRealPathSettled, jrxmlRealPathSettled, reportDir, channelCode[0], "XLS", "settled");

			saveToLogQuery(new Date(), reportPathSettled, "Settled", transactionCode, channelCode[0]);

			ArrayList<ReconcileDto> reconcileUnsettled = new ArrayList<ReconcileDto>();
			ArrayList<ReconcileDto> reconcileUnconfirmed = new ArrayList<ReconcileDto>();

			for (ReconcileDto dataReconcile : searchResultUnsettled) {
				if (dataReconcile.getReconcileStatus().equalsIgnoreCase("Unsettled")) {
					reconcileUnsettled.add(dataReconcile);
				} else if (dataReconcile.getReconcileStatus().equalsIgnoreCase("Need Confirmation/Manual Payment")) {
					reconcileUnconfirmed.add(dataReconcile);
				}
			}

			searchResultUnsettled.clear();
			searchResultUnsettled.addAll(reconcileUnsettled);
			searchResultUnsettled.addAll(reconcileUnconfirmed);

			reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsUnsettled, searchResultUnsettled, jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir, channelCode[0], "PDF", "unsettled");
			String reportPathUnsettled = reportReconcileAction.createReportWithoutContextByDataTypeWeekly(paramsUnsettled, searchResultUnsettled, jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir, channelCode[0], "XLS", "unsettled");

			saveToLogQuery(new Date(), reportPathUnsettled, "Unsettled/Need Confirmation", transactionCode, channelCode[0]);

			// for detail in email
			settled += mapCountSettled.get("settled");
			unSettled += mapCountUnsettled.get("notSettled");
			needConfirmation += mapCountUnsettled.get("unconfirmed");
			paymentAmountSettled += mapCountAmountSettled.get("amountSettled");
			paymentAmountUnSettled += mapCountAmountUnsettled.get("amountNotSettled");
			// payemntAmountNeedConfirmation += mapCountAmountUnsettled.get("amountUnconfirmed");

			// DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
			// Calendar calStartEmail = Calendar.getInstance();
			// calStartEmail.add(Calendar.DATE, -1);
			// calStartEmail.setTime(new Date());
			// String dateStartEmail = dateFormat.format(calEnd.getTime());

			// Calendar calEndEmail = Calendar.getInstance();
			// calEndEmail.setTime(new Date());
			// calEndEmail.add(Calendar.DATE, -6);
			// String dateEndEmail = dateFormat.format(calStart.getTime());

			// listFile.put(reportPathSettled + "_" + "settled" + "_" + channelCode[0] + ".pdf", reportDir + "/" + dateEndEmail + "_" + dateStartEmail);
			// listFile.put(reportPathSettled + "_" + "settled" + "_" + channelCode[0] + ".xls", reportDir + "/" + dateEndEmail + "_" + dateStartEmail);
			// listFile.put(reportPathUnsettled + "_" + "unsettled" + "_" + channelCode[0] + ".pdf", reportDir + "/" + dateEndEmail + "_" + dateStartEmail);
			// listFile.put(reportPathUnsettled + "_" + "unsettled" + "_" + channelCode[0] + ".xls", reportDir + "/" + dateEndEmail + "_" + dateStartEmail);

		}

		if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
			sendMail(listFile, settled, unSettled, needConfirmation, paymentAmountSettled, paymentAmountUnSettled, payemntAmountNeedConfirmation, transactionCode);
		}

	}*/

	private List<ReconcileDto> AutoReconcileToPaid(List<ReconcileDto> list, Map<String, Integer> mapCount, Map<String, Long> mapCountAmount) {

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

			if (reconciledList.get(i).getMt940Status().equalsIgnoreCase("Paid") && reconciledList.get(i).getSimsStatus().equalsIgnoreCase("Unpaid") && reconciledList.get(i).getTrxId() != "-") {

				System.out.println("Auto Reconcile Invoice No : " + reconciledList.get(i).getInvoiceNo());
				externalBillingSystem.updateInvoiceEod(reconciledList.get(i).getInvoiceNo(), cal.getTime(), "Auto Reconcile By WebAdmin");

				reconciledList.get(i).setSimsStatus("Paid");
				reconciledList.get(i).setPaymentDateSims(trxDate);

				if (reconciledList.get(i).getInvoiceDueDate() != null && !reconciledList.get(i).getInvoiceDueDate().equals("-")) {

					try {
						paymentDateSims = formatter.parse(reconciledList.get(i).getPaymentDateSims());
						invoiceDueDate = formatter.parse(reconciledList.get(i).getInvoiceDueDate());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (invoiceDueDate.compareTo(paymentDateSims) >= 0) {
						reconciledList.get(i).setReconcileStatus("Settled");
						amountNewSettled = amountNewSettled + Long.parseLong(reconciledList.get(i).getTrxAmount());
						newSettled++;
					} else {
						reconciledList.get(i).setReconcileStatus("Need Confirmation/Manual Payment");
						amountNewUnconfirmed = amountNewUnconfirmed + Long.parseLong(reconciledList.get(i).getTrxAmount());
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

	public void saveToLogQuery(Date reportTime, String reportDocument, String reportType, String trxType, String bankName) throws SQLException {

		String sql = "INSERT INTO RECONCILE_REPORT_WEEKLY_LOG (REPORT_TIME,REPORT_DOCUMENT,REPORT_TYPE,TRANSACTION_TYPE,BANK_NAME) " + "VALUES (?,?,?,?,?)";

		String sql2 = "SELECT * FROM RECONCILE_REPORT_WEEKLY_LOG WHERE BANK_NAME = ? AND REPORT_DOCUMENT = ? AND REPORT_TYPE = ? AND TRANSACTION_TYPE = ?";
		Connection con = null;
		PreparedStatement ps, s = null;
		ResultSet rs = null;

		try {
			con = dataSourceWebapp.getConnection();
			ps = con.prepareStatement(sql2);
			ps.setString(1, bankName);
			ps.setString(2, reportDocument);
			ps.setString(3, reportType);
			ps.setString(4, trxType);
			rs = ps.executeQuery();
			List<String> nameList = new ArrayList<String>();
			if (rs.next()) {
				System.out.println("Data already exist");
				log.info("Data already exist");
			} else {
				s = con.prepareStatement(sql);
				s.setDate(1, new java.sql.Date(reportTime.getTime()));
				s.setString(2, reportDocument);
				s.setString(3, reportType);
				s.setString(4, trxType);
				s.setString(5, bankName);
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

		File file = new File(filepath);

		if (!file.exists()) {
			System.out.println("File MT940 " + transactionType + " pada bank " + bankName + " Tidak Ditemukan Pada Path : " + filepath);
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
		mail.setSubject("Reconcile Weekly Report " + (endpointName != null ? endpointName : ""));
		mail.setUsername(mailServer);
		mail.setPassword(passwordMailServer);
		mail.setListFile(listFile);
		mail.setToEmail(emailList.split(","));
		mail.setSmtpHost(smtpHost);
		mail.setSmtpAuth(smtpAuth);
		mail.setSmtpPort(smptpPort);
		mail.setText("<h1>Reconcile Report</h1>" + "<table border='1'>" + "<tr>" + "<th>Status</th>" + "<th>Total Invoice</th>" + "<th>Total Amount</th>" + "</tr>" + "<tr>" + "<td>Settled</td> " + "<td align='right'>" + settled + "</td> " + "<td align='right'>"
				+ (paymentSettled != 0 ? numFormat.format(paymentSettled) : 0) + "</td> " + "</tr>" + "<tr>" + "<td>Unsettled</td>" + "<td align='right'>" + (unSettled + needConfirmation) + "</td>" + "<td align='right'>"
				+ (paymentUnSettled != 0 ? numFormat.format(paymentUnSettled) + numFormat.format(paymentNeedConfirmation) : 0) + "</td>" + "</tr>" + "<tr>" + "<td>Total</td>" + "<td align='right'>" + totalInvoice + "</td>" + "<td align='right'>"
				+ (totalAmount != 0 ? numFormat.format(totalAmount) : 0) + "</td>" + "</tr>" + "</table>");
		mail.setHtml(true);

		return mail.send();
	}

	Map<String, String> putToParam(Map<String, String> param, Map<String, Integer> paramCount, Map<String, Long> mapCountAmountSettled, Date startDate, Date endDate, String channelCode, String bankName, String reconcileStatus, String headerImgPath, String reportTitle) {

		param.put("trxStartDate", DateUtil.convertDateToString(startDate, "dd-MM-yyyy"));
		param.put("trxEndDate", DateUtil.convertDateToString(endDate, "dd-MM-yyyy"));
		param.put("channelCode", channelCode);
		param.put("bankName", bankName);
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
}
