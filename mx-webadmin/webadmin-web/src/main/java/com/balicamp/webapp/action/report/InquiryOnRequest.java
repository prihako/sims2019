package com.balicamp.webapp.action.report;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.InquiryReconcileDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.impl.mx.EndpointsManagerImpl;
import com.balicamp.service.sims.BillingManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.webadmin.AnalisaTransactionLogs;
import com.balicamp.webapp.tapestry.PropertySelectionModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class InquiryOnRequest extends AdminBasePage implements PageBeginRenderListener, PageEndRenderListener {

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManagerImpl getEndpointsManager();

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getTransactionLogsManager();

	@InjectSpring("billingManagerImpl")
	public abstract BillingManager getBillingManager();

	protected final Log log = LogFactory.getLog(AnalisaTransactionLogs.class);

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract String getChannelCode();

	public abstract void setChannelCode(String channelCode);

	public abstract String getInvoiceNo();

	public abstract void setInvoiceNo(String invoiceNo);

	public abstract String getClientId();

	public abstract void setClientId(String clientId);

	public abstract String getReconcileStatus();

	public abstract void setReconcileStatus(String status);

	public abstract InquiryReconcileDto getInquiryReconcileDto();

	public abstract Integer getSettled();

	public abstract void setSettled(Integer settled);

	public abstract Integer getNotSettled();

	public abstract void setNotSettled(Integer notSettled);

	public abstract Integer getSettledReport();

	public abstract void setSettledReport(Integer settled);

	public abstract Integer getNotSettledReport();

	public abstract void setNotSettledReport(Integer notSettled);

	public abstract void setTime(String date);

	public abstract String getTime();

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

	public IPropertySelectionModel getChannelCodeModel() {
		Map<String, String> map = new HashMap<String, String>();
		List<Endpoints> endpoints = new ArrayList<Endpoints>();

		map.put("all", "All");
		List<Endpoints> endpointChannel = new ArrayList<Endpoints>();
		endpointChannel.addAll(getEndpointsManager().getAllEndpointsByType("channel"));

		for (Endpoints tempEndpoint : endpointChannel) {
			map.put(tempEndpoint.getCode(), tempEndpoint.getName());
		}

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public IPropertySelectionModel getReconcileStatusModel() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("All", "All");
		map.put("Settled", "Settled");
		map.put("Unsettled", "Not Settled");

		return new PropertySelectionModel(getLocale(), map, false, false);
	}

	public void doSearch() {

		String errorMessage = validation();
		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);

			getFields().put("TRANSACTION_LOGS_WEBADMIN", new ArrayList<InquiryReconcileDto>());

			return;
		}

		Set<String> invoiceNo = null;

		if (getInvoiceNo() != null) {
			invoiceNo = new HashSet<String>();
			invoiceNo.add(getInvoiceNo());
		} else {
			if (getExtensionFile(file.getFileName()).equalsIgnoreCase("txt")) {
				invoiceNo = findInvoiceByTxt(file);
			} else if (getExtensionFile(file.getFileName()).equalsIgnoreCase("xls")) {
				invoiceNo = getInvoiceFromExcel(file);
			}
		}

		Map<String, Integer> mapCount = new HashMap<String, Integer>();

		Set<String> transactionCode = new HashSet<String>();
		transactionCode.add("BILL.PAY");
		transactionCode.add("BILL.REV");

		List<InquiryReconcileDto> searchResult = getTransactionLogsManager().findTransactionLogsWebadminWithMap(
				getChannelCode(), invoiceNo, getClientId(), transactionCode, getReconcileStatus(), "00", mapCount);

		setSettled(mapCount.get("settled"));
		setNotSettled(mapCount.get("notSettled"));
		setSettledReport(mapCount.get("settled"));
		setNotSettledReport(mapCount.get("notSettled"));

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		setTime(dateFormat.format(Calendar.getInstance().getTime()));

		getFields().put("TRANSACTION_LOGS_WEBADMIN", searchResult);

		if (searchResult == null || searchResult.size() < 1) {
			addError(getDelegate(), "errorShadow", getText("leftmenu.inquiryOnRequest.errorMessage.emptySearchResult"),
					ValidationConstraint.CONSISTENCY);
		}

	}

	public String validation() {
		String errorMessage = null;
		if (getInvoiceNo() == null && getFile() == null) {
			errorMessage = getText("leftmenu.inquiryOnRequest.errorMessage.both.invoiceIsNull");
		}

		if (getFile() != null) {
			String fileName = getFile().getFileName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			if ((!ext.equalsIgnoreCase("txt") && !ext.equalsIgnoreCase("xls")) || fileName.lastIndexOf(".") < 0) {
				errorMessage = getText("leftmenu.inquiryOnRequest.errorMessage.illegalFileName");
			}
		}

		return errorMessage;
	}

	private boolean validateInvoiceNumberFormat(String invoiceNo) {
		// TODO Auto-generated method stub
		String pattern = "\\d*";

		return invoiceNo.matches(pattern);
	}

	public List<InquiryReconcileDto> getInquiryReconcileList() {
		List<InquiryReconcileDto> list = (List<InquiryReconcileDto>) getFields().get("TRANSACTION_LOGS_WEBADMIN");
		return list;
	}

	public void exportToExcel(IRequestCycle cycle) throws JRException, IOException {
		ReportInquiryOnRequestAction reportInqOnReq = new ReportInquiryOnRequestAction();
		String inqXlsReportPath = reportInqOnReq.createReport(putParams(), getInquiryReconcileList(), "xls",
				getServletContext(), getTransactionLogsManager().findParamValueByParamName());
		// download(inqXlsReportPath);

		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + inqXlsReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + inqXlsReportPath + "&mode=simple");
	}

	public void exportToPdf(IRequestCycle cycle) throws JRException, IOException {
		ReportInquiryOnRequestAction reportInqOnReq = new ReportInquiryOnRequestAction();
		String inqPdfReportPath = reportInqOnReq.createReport(putParams(), getInquiryReconcileList(), "pdf",
				getServletContext(), getTransactionLogsManager().findParamValueByParamName());
		// download(inqPdfReportPath);

		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + inqPdfReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + inqPdfReportPath + "&mode=simple");
	}

	public Map<String, String> putParams() {
		Map<String, String> params = new HashMap<String, String>();

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());

		params.put("reconcileTime", date);
		if (getChannelCode().equalsIgnoreCase("all")) {
			params.put("channelCode", "");
		} else if (getChannelCode().equalsIgnoreCase("chws")) {
			params.put("channelCode", "BANK MANDIRI");
		} else if (getChannelCode().equalsIgnoreCase("chws2")) {
			params.put("channelCode", "BANK BNI");
		}

		if (getSettledReport() == null || getSettledReport() == 0) {
			params.put("totalMatch", "0");
		} else {
			params.put("totalMatch", getSettledReport().toString());
		}
		if (getNotSettledReport() == null || getNotSettledReport() == 0) {
			params.put("totalUnmatch", "0");
		} else {
			params.put("totalUnmatch", getNotSettledReport().toString());
		}
		params.put("realPath", getServletContext().getRealPath("/WEB-INF/report/"));
		return params;
	}

	public IPage detailInvoice(IRequestCycle cycle, InquiryReconcileDto inquiryReconcileDto, String time,
			Integer settle, Integer notSettle, String channelCode, String invoiceNo, String clientId,
			String reconcileStatus) {
		DetailTransactionInquiryOnRequest detailTransaction = (DetailTransactionInquiryOnRequest) cycle
				.getPage("detailTransactionInquiryOnRequest");
		detailTransaction.setReconcileDto(inquiryReconcileDto);

		cycle.getParameter("");

		Map<Object, Object> mapParams = new HashMap<Object, Object>();
		mapParams.put("settled", settle);
		mapParams.put("notSettled", notSettle);
		mapParams.put("time", time);
		detailTransaction.setMapParams(mapParams);
		detailTransaction.setChannelCode(channelCode);
		detailTransaction.setInvoiceNo(invoiceNo);
		detailTransaction.setClientId(clientId);
		detailTransaction.setReconcileStatus(reconcileStatus);

		System.out.println(mapParams);

		return detailTransaction;

	}

	public String getExtensionFile(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	public Set<String> findInvoiceByTxt(IUploadFile invoiceFile) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		Set<String> invoiceNotDuplicate = new HashSet<String>();

		try {

			br = new BufferedReader(new InputStreamReader(invoiceFile.getStream(), "UTF-8"));

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] invoiceNo = line.split(cvsSplitBy);
				for (int i = 0; i < invoiceNo.length; i++) {
					invoiceNotDuplicate.add(invoiceNo[i]);
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return invoiceNotDuplicate;

	}

	public Set<String> getInvoiceFromExcel(IUploadFile file) {
		Set<String> result = new HashSet<String>();
		try {
			InputStream input = file.getStream();
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			String invoiceNo = new String();

			// Start ngambil datanya dari bari ke 1
			int rowNum = 1;

			// Start ngambil datan invoice dari kolom ke 0
			short colNum = 0;
			while (true) {
				HSSFRow row = sheet.getRow(rowNum);
				invoiceNo = cellToString(row.getCell(colNum));
				result.add(invoiceNo);

				if (sheet.getRow(rowNum + 1) == null) {
					break;
				} else if (sheet.getRow(rowNum + 1).getCell(colNum) == null) {
					break;
				} else if (cellToString(sheet.getRow(rowNum + 1).getCell(colNum)).equals("")) {
					break;
				}

				rowNum++;
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("result is : " + result);
		return result;
	}

	public static String cellToString(HSSFCell cell) {
		int type;
		Object result = null;
		type = cell.getCellType();
		Double y;
		BigDecimal x, z;

		switch (type) {

		case HSSFCell.CELL_TYPE_NUMERIC: // numeric value in Excel
		case HSSFCell.CELL_TYPE_FORMULA: // precomputed value based on formula
			result = cell.getNumericCellValue();
			if (result.toString().contains("E")) {
				String[] result1 = result.toString().split("E");
				x = new BigDecimal(result1[0]);
				y = new Double(result1[1]);
				z = new BigDecimal(Math.pow(10, y)).multiply(x).stripTrailingZeros();
				result = z.toPlainString();
				break;
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // String Value in Excel
			result = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
		case HSSFCell.CELL_TYPE_BOOLEAN: // boolean value
			result: cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
		default:
			throw new RuntimeException("There is no support for this type of cell");
		}
		return result.toString();
	}

	/**
	public void exportToPdfEndpoints(IRequestCycle cycle) throws JRException, IOException {
		ReportEndpoints reportInqOnReq = new ReportEndpoints();
		List<Endpoints> endpoints = getEndpointsManager().getAll();

		String inqPdfReportPath = reportInqOnReq.createReport(putParamsEndpoints(), endpoints, "pdf",
				getServletContext(), getTransactionLogsManager().findParamValueByParamName());

		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + inqPdfReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + inqPdfReportPath + "&mode=simple");
	}

	public void exportToExcelEndpoints(IRequestCycle cycle) throws JRException, IOException {
		ReportEndpoints reportInqOnReq = new ReportEndpoints();
		List<Endpoints> endpoints = getEndpointsManager().getAll();

		String inqXlsReportPath = reportInqOnReq.createReport(putParamsEndpoints(), endpoints, "xls",
				getServletContext(), getTransactionLogsManager().findParamValueByParamName());

		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + inqXlsReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + inqXlsReportPath + "&mode=simple");
	}

	public Map<String, String> putParamsEndpoints() {
		Map<String, String> params = new HashMap<String, String>();

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());

		params.put("date", date);

		return params;
	}
	
	*/
}
