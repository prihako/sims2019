package com.balicamp.webapp.thread;

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
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.model.mx.ReconcileDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.armgmt.ArmgmtManagerImpl;
import com.balicamp.service.impl.ebs.ExternalBillingSystemManagerImpl;
import com.balicamp.service.impl.pengujian.SertifikasiManagerImpl;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.soap.ws.channel.IarChannel;
import com.balicamp.soap.ws.channel.IkrapChannel;
import com.balicamp.soap.ws.channel.KlbsiChannel;
import com.balicamp.soap.ws.channel.PengujianChannel;
import com.balicamp.soap.ws.channel.ReorChannel;
import com.balicamp.soap.ws.channel.UnarChannel;
import com.balicamp.util.DateUtil;
import com.balicamp.util.LogHelper;
import com.balicamp.webapp.action.report.ReportReconcileAction;
import com.balicamp.webapp.util.SendMail;

import net.sf.jasperreports.engine.JRException;
import test.Constants;

public class DoGetReconcileListByMt940NewMonthlyThread extends Thread{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private SystemParameterManager systemParameter;
	private TransactionLogsManager trxLogManager;
	private ArmgmtManagerImpl armgmtManagerImpl;
	private DataSource dataSourceWebapp;
	private DataSource dataSource;
	private String billerCode;
	private List<String> channelList;
	
	
	public DoGetReconcileListByMt940NewMonthlyThread(SystemParameterManager systemParameter,
			TransactionLogsManager trxLogManager,
			ArmgmtManagerImpl armgmtManagerImpl,
			DataSource dataSourceWebapp,
			DataSource dataSource,
			String billerCode, 
			List<String> channelList){
		super(billerCode + "-" + UUID.randomUUID().toString());
		this.systemParameter = systemParameter;
		this.trxLogManager = trxLogManager;
		this.armgmtManagerImpl = armgmtManagerImpl;
		this.dataSourceWebapp = dataSourceWebapp;
		this.dataSource=dataSource;
		this.billerCode = billerCode;
		this.channelList = channelList;
	}
	
	@Override
	public void run() {
		try {
			doGetReconcileListByMt940EodNew();
		} catch(Exception e) {
			log.error("error in thread DoGetReconcileListByMt940NewThread ", e);
		}
	}
	
	public void doGetReconcileListByMt940EodNew() throws FileNotFoundException, JRException, IOException, SQLException {

		String mt940BasePath 				= systemParameter.findParamValueByParamName("reconcileMonthly.mt940.path");
		String jrxmlRealPathSettled 		= systemParameter.findParamValueByParamName("reconcileMonthly.jrxml.settled.path");
		String jasperRealPathSettled 		= systemParameter.findParamValueByParamName("reconcileMonthly.jasper.settled.path");
		String jrxmlRealPathUnsettled 		= systemParameter.findParamValueByParamName("reconcileMonthly.jrxml.unsettled.path");
		String jasperRealPathUnsettled 		= systemParameter.findParamValueByParamName("reconcileMonthly.jasper.unsettled.path");
		
		String jrxmlRealPathAll = systemParameter.findParamValueByParamName("reconcileMonthly.jrxml.all.path");
		String jasperRealPathAll = systemParameter.findParamValueByParamName("reconcileMonthly.jasper.all.path");
		
		Map<String, String> paramsSettled 	= new HashMap<String, String>();
		Map<String, String> paramsUnsettled = new HashMap<String, String>();
		
		Map<String, String> params 		= new HashMap<String, String>();

		Calendar reconcileCalendar = Calendar.getInstance();
		reconcileCalendar.setTime(DateUtil.substractMonth(new Date(), 1));
		int maxDay = 0;

		String trxDateNameString = "";
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

			Set<String> listInvoiceCode = null;

			int i = 0;
			reconcileCalendar.set(Calendar.MONTH, reconcileCalendar.get(Calendar.MONTH));
			reconcileCalendar.set(Calendar.DAY_OF_MONTH, 1);
			
			maxDay = reconcileCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			String[] channelCode = channelCodeListIterator.next().split("-");
			
			for (int j = 1; j <= maxDay; j++) {
				HashMap<String, Object[]> mxData 	= new HashMap<String, Object[]>();
				HashMap<String, Object[]> mt940Data = new HashMap<String, Object[]>();
				listInvoiceCode = new HashSet<String>();
				reconcileCalendar.set(Calendar.DAY_OF_MONTH, j);
				trxDateNameString = DateUtil.convertDateToString(reconcileCalendar.getTime(), "yyyyMMdd");

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
					trxDate = formatter.parse(DateUtil.convertDateToString(
							reconcileCalendar.getTime(), "dd-MM-yyyy"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mt940Data.putAll(armgmtManagerImpl.getMT940(trxDate, null, paymentType, channelCode[0]));
				mxData.putAll(trxLogManager.
						findAllTransactionLogsWebadminReconcileByDate(channelCode[1], trxCode, new String[] { "00" }, new String[] { "00" }, trxDate));

				if (mt940Data.isEmpty()) {
					System.out.println("DATA IS EMPTY");
				} else {
					listInvoiceCode = mt940Data.keySet();
					searchResultSettled.addAll(trxLogManager.
							findTransactionLogsWebadmin(mt940Data, mxData, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "Settled", mapCountSettled, mapCountAmountSettled));
					searchResultUnsettled.addAll(trxLogManager.findTransactionLogsWebadmin(
							mt940Data, mxData, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "Unsettled/Need Confirmation", mapCountUnsettled, mapCountAmountUnsettled));
					
					searchResultAll.addAll(trxLogManager.findTransactionLogsWebadmin(
							mt940Data, mxData, channelCode[1], listInvoiceCode, null, trxCode, trxDate, "All", mapCount, mapCountAmount));
				}
			}

			ReportReconcileAction reportReconcileAction = new ReportReconcileAction();
//			String reportDir = trxLogManager.findParamValueByParamName("reconcileMonthly.report.path") + billerCode;
			String reportDir = trxLogManager.findParamValueByParamName("reconcileMonthly.report.path") + channelCode[0].toLowerCase() + "/" + billerCode;

			// Date Laporan Reconcile Dilakukan

			Calendar cal = Calendar.getInstance();
			cal.setTime(DateUtil.substractMonth(new Date(), 1));
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date startDate = cal.getTime();
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date endDate = cal.getTime();

			putToParam(paramsSettled, mapCountSettled, mapCountAmountSettled, startDate, endDate, channelCode[1], channelCode[0], "Settled", systemParameter.findParamValueByParamName("reconcileMonthly.report.image.path"),"");
			putToParam(paramsUnsettled, mapCountUnsettled, mapCountAmountUnsettled, startDate, endDate, channelCode[1], channelCode[0], "Unsettled/Need Confirmation", systemParameter.findParamValueByParamName("reconcileMonthly.report.image.path"),"");

			putToParam(params, mapCount, mapCountAmount, startDate, endDate, channelCode[1], channelCode[0], "All", systemParameter.findParamValueByParamName("reconcileMonthly.report.image.path"),"");
			// result.put("params", params);
			// result.put("jrxmlPath", jrxmlRealPath);
			// result.put("jasperPath", jasperRealPath);
			// result.put("searchResult", searchResult);

			ArrayList<ReconcileDto> reconcileUnsettled = new ArrayList<ReconcileDto>();
			ArrayList<ReconcileDto> reconcileUnconfirmed = new ArrayList<ReconcileDto>();

			for (ReconcileDto dataReconcile : searchResultUnsettled) {
				if (dataReconcile.getReconcileStatus().equalsIgnoreCase(
						"Unsettled")) {
					reconcileUnsettled.add(dataReconcile);
				} else if (dataReconcile.getReconcileStatus().equalsIgnoreCase(
						"Need Confirmation/Manual Payment")) {
					reconcileUnconfirmed.add(dataReconcile);
				}
			}

			searchResultUnsettled.clear();
			searchResultUnsettled.addAll(reconcileUnsettled);
			searchResultUnsettled.addAll(reconcileUnconfirmed);

			synchronized (this){
				reportReconcileAction.createReportWithoutContextByDataTypeMonthly(paramsSettled, searchResultSettled, jasperRealPathSettled, jrxmlRealPathSettled, reportDir, channelCode[0], "PDF", "settled", reconcileCalendar.getTime());
				String reportPathSettled = reportReconcileAction.createReportWithoutContextByDataTypeMonthly(paramsSettled, searchResultSettled, jasperRealPathSettled, jrxmlRealPathSettled, reportDir, channelCode[0], "XLS", "settled", reconcileCalendar.getTime());
				saveToLogQuery(new Date(), reportPathSettled, "Settled", billerCode, channelCode[0]);
	
				reportReconcileAction.createReportWithoutContextByDataTypeMonthly(paramsUnsettled, searchResultUnsettled, jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir, channelCode[0], "PDF", "unsettled", reconcileCalendar.getTime());
				String reportPathUnsettled = reportReconcileAction.createReportWithoutContextByDataTypeMonthly(paramsUnsettled, searchResultUnsettled, jasperRealPathUnsettled, jrxmlRealPathUnsettled, reportDir, channelCode[0], "XLS", "unsettled", reconcileCalendar.getTime());
				saveToLogQuery(new Date(), reportPathUnsettled, "Unsettled/Need Confirmation", billerCode, channelCode[0]);
	
				
				reportReconcileAction.createReportWithoutContextByDataTypeMonthly(params, searchResultAll, jasperRealPathAll, jrxmlRealPathAll, reportDir, channelCode[0], "PDF", "all", reconcileCalendar.getTime());
				String reportPathAll = reportReconcileAction.createReportWithoutContextByDataTypeMonthly(params, searchResultAll, jasperRealPathAll, jrxmlRealPathAll, reportDir, channelCode[0], "XLS", "all", reconcileCalendar.getTime());
				saveToLogQuery(new Date(), reportPathAll, "All", billerCode, channelCode[0]);
				
				// for detail in email
				settled += mapCountSettled.get("settled");
				unSettled += mapCountUnsettled.get("notSettled");
				needConfirmation += mapCountUnsettled.get("unconfirmed");
				paymentAmountSettled += mapCountAmountSettled.get("amountSettled");
				paymentAmountUnSettled += mapCountAmountUnsettled.get("amountNotSettled");
			
			}

		}

		if (!(settled == 0 && unSettled == 0 && needConfirmation == 0)) {
			sendMail(listFile, settled, unSettled, needConfirmation,
					paymentAmountSettled, paymentAmountUnSettled,
					payemntAmountNeedConfirmation, billerCode);
		}

	}
	
	Map<String, String> putToParam(Map<String, String> param,
			Map<String, Integer> paramCount,
			Map<String, Long> mapCountAmountSettled, Date startDate,
			Date endDate, String channelCode, String bankName,
			String reconcileStatus, String headerImgPath, String reportTitle) {

		param.put("trxStartDate",
				DateUtil.convertDateToString(startDate, "dd-MM-yyyy"));
		param.put("trxEndDate",
				DateUtil.convertDateToString(endDate, "dd-MM-yyyy"));
		param.put("channelCode", channelCode);
		param.put("bankName", bankName);
		param.put("reconcileStatus", reconcileStatus);

		param.put("settled", paramCount.get("settled").toString());
		param.put("notSettled", paramCount.get("notSettled").toString());
		param.put("unconfirmed", paramCount.get("unconfirmed").toString());
		param.put("amountSettled", mapCountAmountSettled.get("amountSettled")
				.toString());
		param.put("amountNotSettled",
				mapCountAmountSettled.get("amountNotSettled").toString());
		param.put("amountUnconfirmed",
				mapCountAmountSettled.get("amountUnconfirmed").toString());
		param.put("totalAmount", mapCountAmountSettled.get("totalAmount")
				.toString());
		param.put("totalAmountDenda",
				mapCountAmountSettled.get("totalAmountDenda").toString());

		param.put("headerImg", headerImgPath);
		param.put("title", reportTitle);

		return param;
	}
	
	public void saveToLogQuery(Date reportTime, String reportDocument,
			String reportType, String trxType, String bankName)
			throws SQLException {

		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.substractMonth(reportTime, 1));
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		String sql = "INSERT INTO RECONCILE_REPORT_MONTHLY_LOG (REPORT_TIME,REPORT_DOCUMENT,REPORT_TYPE,TRANSACTION_TYPE,BANK_NAME) "
				+ "VALUES (?,?,?,?,?)";

		String sql2 = "SELECT * FROM RECONCILE_REPORT_MONTHLY_LOG WHERE BANK_NAME = ? AND REPORT_DOCUMENT = ? AND REPORT_TYPE = ? AND TRANSACTION_TYPE = ?";
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
				s.setDate(1, new java.sql.Date(cal.getTime().getTime()));
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
	
	public boolean sendMail(Map<String, String> listFile, Integer settled,
			Integer unSettled, Integer needConfirmation, Long paymentSettled,
			Long paymentUnSettled, Long paymentNeedConfirmation,
			String endpointCode) {
		DecimalFormat numFormat = new DecimalFormat("#,###,###.00");

		Integer totalInvoice = settled + unSettled + needConfirmation;
		Long totalAmount = paymentNeedConfirmation + paymentSettled
				+ paymentUnSettled;

		String emailList = systemParameter
				.findParamValueByParamName("webadmin.reconcileEod.emailList");
		String smtpHost = systemParameter
				.findParamValueByParamName("webadmin.reconcileEod.smtpHost");
		String smtpAuth = systemParameter
				.findParamValueByParamName("webadmin.reconcileEod.smtpAuth");
		String smptpPort = systemParameter
				.findParamValueByParamName("webadmin.reconcileEod.smtpPort");
		String mailServer = systemParameter
				.findParamValueByParamName("webadmin.reconcileEod.mailServer.mail");
		String passwordMailServer = systemParameter
				.findParamValueByParamName("webadmin.reconcileEod.mailServer.password");

		String endpointName = getEndpointName(endpointCode);

		SendMail mail = new SendMail();
		mail.setSubject("Reconcile Monthly Report "
				+ (endpointName != null ? endpointName : ""));
		mail.setUsername(mailServer);
		mail.setPassword(passwordMailServer);
		mail.setListFile(listFile);
		mail.setToEmail(emailList.split(","));
		mail.setSmtpHost(smtpHost);
		mail.setSmtpAuth(smtpAuth);
		mail.setSmtpPort(smptpPort);
		mail.setText("<h1>Reconcile Report</h1>" + "<table border='1'>"
				+ "<tr>" + "<th>Status</th>" + "<th>Total Invoice</th>"
				+ "<th>Total Amount</th>" + "</tr>" + "<tr>"
				+ "<td>Settled</td> " + "<td align='right'>"
				+ settled
				+ "</td> "
				+ "<td align='right'>"
				+ (paymentSettled != 0 ? numFormat.format(paymentSettled) : 0)
				+ "</td> "
				+ "</tr>"
				+ "<tr>"
				+ "<td>Unsettled</td>"
				+ "<td align='right'>"
				+ unSettled
				+ "</td>"
				+ "<td align='right'>"
				+ (paymentUnSettled != 0 ? numFormat.format(paymentUnSettled)
						: 0)
				+ "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Need Confirmation</td>"
				+ "<td align='right'>"
				+ needConfirmation
				+ "</td>"
				+ "<td align='right'>"
				+ (paymentNeedConfirmation != 0 ? numFormat
						.format(paymentNeedConfirmation) : 0)
				+ "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Total</td>"
				+ "<td align='right'>"
				+ totalInvoice
				+ "</td>"
				+ "<td align='right'>"
				+ (totalAmount != 0 ? numFormat.format(totalAmount) : 0)
				+ "</td>" + "</tr>" + "</table>");
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
}
