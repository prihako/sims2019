package com.balicamp.webapp.action.webadmin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.service.TransactionLogsManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.action.report.ReportAnalisaTransactionLogsAction;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version
 */
/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version
 */
/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version 
 */
public abstract class AnalisaTransactionLogsViaUploadInvoice extends BasePageList implements PageBeginRenderListener {

	protected final Log log = LogFactory.getLog(AnalisaTransactionLogsViaUploadInvoice.class);

	@InjectSpring("systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getTransactionLogsManager();

	private IUploadFile file;

	public IUploadFile getFile() {
		return file;
	}

	public void setFile(IUploadFile file) {
		this.file = file;
	}

	public abstract AnalisaTransactionLogsDto getTransactionReport();

	public abstract void setNotFirstLoad(boolean firsloadFlag);

	public abstract boolean isNotFirstLoad();

	public List getTransactionLogs() {
		List<AnalisaTransactionLogsDto> list = (List<AnalisaTransactionLogsDto>) getFields().get(
				"TRANSACTION_LOGS_VIA_UPLOAD_INVOICE");
		return list;
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
					if (getFields().get("viewLogs") == null) {
						getFields().remove("TRANSACTION_LOGS_VIA_UPLOAD_INVOICE");
					} else {
						getFields().remove("viewLogs");
					}
				}
			}
		}

	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public void doSearch() {
		String errorMessage = validation();

		if (errorMessage != null) {
			addError(getDelegate(), "errorShadow", errorMessage, ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_LOGS_VIA_UPLOAD_INVOICE", new ArrayList<AnalisaTransactionLogsDto>());
			return;
		}

		Set<String> invoiceNo = null;
		if (getExtensionFile(file.getFileName()).equalsIgnoreCase("txt")) {
			invoiceNo = findInvoiceByTxt(file);
		} else if (getExtensionFile(file.getFileName()).equalsIgnoreCase("xls")) {
			invoiceNo = getInvoiceFromExcel(file);
		}

		String validateInvoiceNo = validationInvoiceNo(invoiceNo);

		if (validateInvoiceNo != null) {
			addError(getDelegate(), "errorShadow", validateInvoiceNo, ValidationConstraint.CONSISTENCY);
			getFields().put("TRANSACTION_LOGS_VIA_UPLOAD_INVOICE", new ArrayList<AnalisaTransactionLogsDto>());
			return;
		}

		List<AnalisaTransactionLogsDto> dataTransaction = null;
		if (invoiceNo != null) {
			dataTransaction = getTransactionLogsManager().findTransactionsOrderByTimeAndInvoiceNo(null, null, null,
					invoiceNo, null, null, null, null, null);

			getFields().put("TRANSACTION_LOGS_VIA_UPLOAD_INVOICE", dataTransaction);
		}

		if (dataTransaction == null || dataTransaction.size() < 1) {
			addError(getDelegate(), "errorShadow",
					getText("leftmenu.analisaTransactionLogsViaUploadInvocie.emptySearchResult"),
					ValidationConstraint.CONSISTENCY);
		}

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

	public IPage additionalLinkListener(IRequestCycle cycle, String trxId) {

		MessageLogsListViaUpload messageLogsList = (MessageLogsListViaUpload) cycle.getPage("messageLogsListViaUpload");
		messageLogsList.setEditorState("visible");

		messageLogsList.setTransactionLogList((List<AnalisaTransactionLogsDto>) getFields().get(
				"TRANSACTION_LOGS_VIA_UPLOAD_INVOICE"));

		messageLogsList.setTransactionId(trxId);

		getFields().put("transactionLogListViaUpload",
				(List<AnalisaTransactionLogsDto>) getFields().get("TRANSACTION_LOGS_VIA_UPLOAD_INVOICE"));
		getFields().put("transactionIdViaUpload", trxId);

		return messageLogsList;
	}

	public String validation() {

		String errorMessage = null;

		if ((getFile() == null)) {

			errorMessage = getText("leftmenu.analisaTransactionLogsViaUploadInvocie.fileIsNull");

		}

		if (getFile() != null) {
			String fileName = getFile().getFileName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			if ((!ext.equalsIgnoreCase("txt") && !ext.equalsIgnoreCase("xls")) || fileName.lastIndexOf(".") < 0) {
				errorMessage = getText("leftmenu.analisaTransactionLogsViaUploadInvocie.errorMessage.invalidFile");
			}
		}

		return errorMessage;

	}

	public void exportToExcel(IRequestCycle cycle) throws JRException, IOException {

		List<AnalisaTransactionLogsDto> listData = getTransactionLogs();
		ReportAnalisaTransactionLogsAction reportAnalisaTransactionLogsAction = new ReportAnalisaTransactionLogsAction();
		String xlsReportPath = reportAnalisaTransactionLogsAction.createReport(putParams(), listData, "xls",
				getServletContext(),
				getSystemParameterManager().findParamValueByParamName("webadmin.download.temporary.path"));
		// download(xlsReportPath);
		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + xlsReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + xlsReportPath + "&mode=simple");

	}

	public void exportToPdf(IRequestCycle cycle) throws JRException, IOException {

		List<AnalisaTransactionLogsDto> listData = getTransactionLogs();
		ReportAnalisaTransactionLogsAction reportAnalisaTransactionLogsAction = new ReportAnalisaTransactionLogsAction();
		String pdfReportPath = reportAnalisaTransactionLogsAction.createReport(putParams(), listData, "pdf",
				getServletContext(),
				getSystemParameterManager().findParamValueByParamName("webadmin.download.temporary.path"));
		// download(pdfReportPath);
		String context = getBaseUrl();
		System.out.println("DOWNLAOD --> " + context + "/download?fileName=" + pdfReportPath + "&mode=simple");
		cycle.sendRedirect(context + "/download?fileName=" + pdfReportPath + "&mode=simple");

	}

	public Map putParams() {
		Map params = new HashMap<String, String>();

		params.put("klienId", "");
		params.put("noInvoice", "From File");
		params.put("startDate", "");
		params.put("startHour", "");
		params.put("startMinute", "");
		params.put("startSecond", "");
		params.put("endDate", "");
		params.put("endHour", "");
		params.put("endMinute", "");
		params.put("endSecond", "");
		params.put("trxType", "");
		params.put("respCodeMandiri", "");
		params.put("trxTypeBiller", "");
		params.put("respBillerCode", "");
		params.put("trxCode", "");
		// ngambil gambar
		InputStream imgStream = getServletContext().getResourceAsStream("/WEB-INF/report/kominfo_2.png");
		params.put("realPath", imgStream);
		// params.put("realPath",
		// getServletContext().getRealPath("/WEB-INF/report/"));
		return params;
	}

	private boolean validateInvoiceNumberFormat(String invoiceNo) {
		// TODO Auto-generated method stub
		String pattern = "\\d*";

		return invoiceNo.matches(pattern);
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

	public String validationInvoiceNo(Set<String> invoiceNo) {
		String errorMessage = null;
		for (String tempInvoice : invoiceNo) {
			if (validateInvoiceNumberFormat(tempInvoice) != true) {
				errorMessage = getText("leftmenu.analisaTransactionLogsViaUploadInvocie.invoiceNumberWrongFormat");
			}
		}
		return errorMessage;
	}

	public String getExtensionFile(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
}
