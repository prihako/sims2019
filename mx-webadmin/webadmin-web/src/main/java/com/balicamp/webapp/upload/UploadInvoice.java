package com.balicamp.webapp.upload;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.request.IUploadFile;

import com.balicamp.service.TransactionLogsManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.upload.dto.UploadInvoiceDto;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

public abstract class UploadInvoice extends AdminBasePage implements PageBeginRenderListener {

	@InjectSpring("transactionLogsManagerImpl")
	public abstract TransactionLogsManager getTransactionLogsManager();

	protected final Log log = LogFactory.getLog(UploadInvoice.class);

	private IUploadFile file;

	public abstract void setNotFirstLoad(boolean firstloadFlag);

	public abstract boolean isNotFirstLoad();

	public abstract UploadInvoiceDto getUploadInvoiceDto();

	public abstract void setDate1(Date date);

	public abstract Date getDate1();

	public abstract void setDate2(Date date);

	public abstract Date getDate2();

	// @EventListener(targets = "date1", events = "onValueChanged", submitForm =
	// "uploadInvoice")
	// public void changeTrxCode(IRequestCycle cycle) {
	//
	// System.out.println("Called");
	//
	// if (getDate1() != null) {
	// System.out.println("Date1 : " + getDate1());
	// setDate2(getDate1());
	// cycle.getResponseBuilder().updateComponent("date2");
	// } else {
	// System.out.println("Date1 : " + getDate1());
	// }
	// }

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (!pageEvent.getRequestCycle().isRewinding()) {
			if (!isNotFirstLoad()) {
				setNotFirstLoad(true);
				if (getFields() == null) {
					setFields(new HashMap());
				} else {
					getFields().remove("UPLOAD_INVOICE_DTO_LIST");
				}
			}
		}

	}

	public IUploadFile getFile() {
		return file;
	}

	public void setFile(IUploadFile file) {
		this.file = file;
	}

	public List getUploadInvoiceDtoList() {
		List<UploadInvoiceDto> list = (List<UploadInvoiceDto>) getFields().get("UPLOAD_INVOICE_DTO_LIST");
		return list;
	}

	/*
	 * doUpload and readExcel are marked for a while due poi version that used
	 * in jasperreports 2.0.4 is 3.0.1-FINAL.
	 * azis.suparman@sigma.co.id
	 */

	// public void doUpload() {
	//
	// cekFile(getFile());
	//
	// List<Object[]> listObject = readExcel(getFile());
	//
	// // Remove header if exist
	// Object[] header = listObject.get(0);
	// for (int i = 0; i < header.length; i++) {
	// if (header[i].toString().matches("[a-zA-Z ]+")) {
	// System.out.println("I am here");
	// listObject.remove(header);
	// break;
	// }
	// }
	//
	// // parsing content of excel file and search in table transaction_logs in
	// // database mx2
	// List<UploadInvoiceDto> listUploadInvoiceDto = new
	// ArrayList<UploadInvoiceDto>();
	// for (Object[] objects : listObject) {
	// if (objects.length == 2) {
	//
	// if
	// (getTransactionLogsManager().findTransactionLogByInvoiceNo((objects[0]).toString().trim(),
	// (objects[1]).toString().trim())) {
	// listUploadInvoiceDto.add(new
	// UploadInvoiceDto((objects[0]).toString().trim(), (objects[1])
	// .toString().trim(), getText("leftmenu.uploadInvoice.status.found")));
	// } else {
	// listUploadInvoiceDto.add(new
	// UploadInvoiceDto((objects[0]).toString().trim(), (objects[1])
	// .toString().trim(), getText("leftmenu.uploadInvoice.status.notFound")));
	// }
	//
	// } else {
	// System.out.println("Error, column expected 2, actual " + objects.length);
	// listUploadInvoiceDto.add(new
	// UploadInvoiceDto((objects[0]).toString().trim(), (objects[1]).toString()
	// .trim(), getText("leftmenu.uploadInvoice.status.failed", new Object[] {
	// 2, objects.length })));
	// }
	// }
	//
	// getFields().put("UPLOAD_INVOICE_DTO_LIST", listUploadInvoiceDto);
	//
	// }

	// public List<Object[]> readExcel(IUploadFile excelFile) {
	// List<Object[]> listObject = new ArrayList<Object[]>();
	//
	// Workbook workbook;
	//
	// try {
	// InputStream file = getFile().getStream();
	//
	// int length = excelFile.getFileName().length();
	//
	// if (excelFile.getFileName().substring(length - 4 - 1, length -
	// 1).equalsIgnoreCase(".xls")) {
	// // Create Workbook instance holding reference to .xlsx file
	// workbook = new XSSFWorkbook(file);
	// } else {
	// // Create Workbook instance holding reference to .xls file
	// workbook = new HSSFWorkbook(file);
	// }
	//
	// // Get first/desired sheet from the workbook
	// Sheet sheet = workbook.getSheetAt(0);
	//
	// // Iterate through each rows one by on
	// Iterator<Row> rowIterator = sheet.iterator();
	// while (rowIterator.hasNext()) {
	// Row row = rowIterator.next();
	// // For each row, iterate through all the columns
	// Iterator<Cell> cellIterator = row.cellIterator();
	//
	// StringBuffer bufUploadInvoice = new StringBuffer();
	//
	// while (cellIterator.hasNext()) {
	// Cell cell = cellIterator.next();
	// // Check the cell type and format accordingly
	// switch (cell.getCellType()) {
	// case Cell.CELL_TYPE_NUMERIC:
	// System.out.print(cell.getNumericCellValue());
	// bufUploadInvoice.append((Double.valueOf(cell.getNumericCellValue())).intValue());
	// break;
	// case Cell.CELL_TYPE_STRING:
	// System.out.print(cell.getStringCellValue());
	// bufUploadInvoice.append(cell.getStringCellValue());
	// break;
	// }
	//
	// bufUploadInvoice.append(",");
	// }
	//
	// System.out.println("");
	//
	// listObject.add(bufUploadInvoice.toString().split(","));
	//
	// }
	// file.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return listObject;
	// }

	public String cekFile(IUploadFile file) {
		String errorMessage = null;

		if (file != null) {
			String fileName = file.getFileName();
			int length = file.getFileName().length();

			if ((fileName.substring(length - 4 - 1, length - 1).equalsIgnoreCase(".xls"))
					|| (fileName.substring(length - 5 - 1, length - 1).equalsIgnoreCase(".xlsx"))) {

			} else {
				errorMessage = getText("leftmenu.uploadInvoice.file.unknownFileFormat",
						new Object[] { fileName.substring(length - 4 - 1, length - 1) });
			}
		} else {
			errorMessage = getText("leftmenu.uploadInvoice.file.null");
		}

		return errorMessage;
	}
}
