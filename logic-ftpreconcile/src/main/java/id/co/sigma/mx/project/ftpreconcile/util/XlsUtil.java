package id.co.sigma.mx.project.ftpreconcile.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;


public class XlsUtil {
	public static final String XLS_SHEET_TITLE_COLUMN = "SHEET_TITLE_COLUMN";
	public static final String XLS_SHEET_NAME = "SHEET_NAME";
	public static final String XLS_DATA = "DATA";
	private static final String MONEY_FORMAT = "_([$Rp-421]* #,##0.00_);_([$Rp-421]* (#,##0.00);_([$Rp-421]* \"-\"??_);_(@_)";
	private static Logger logger = Logger.getLogger(XlsUtil.class);
	
	private static CellStyle moneyCells;
	private static CellStyle headerCells;
	private static CellStyle titleCells;
	
	public static void createXls(Map<Integer,Map<String,Object>> sheetsDetail, String directory, String fileName, Map<Integer, Integer> indexTransactionPaymentSheet) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Set<Integer> keyset = new TreeSet<Integer>(sheetsDetail.keySet());
		
		for (Integer xlsIndex : keyset) {
			Map<String,Object> sheetsVariable = sheetsDetail.get(xlsIndex);
			String sheetName = (String) sheetsVariable.get(XLS_SHEET_NAME);
			String sheetTitle = (String) sheetsVariable.get(XLS_SHEET_TITLE_COLUMN);
			List<Object[]> sheetsData = (List<Object[]>) sheetsVariable.get(XLS_DATA);
			if(indexTransactionPaymentSheet.get(xlsIndex) != null ){
				createSheet(workbook, sheetName, sheetTitle, sheetsData, 10);
			}else{
				createSheet(workbook, sheetName, sheetTitle, sheetsData, 4);
			}
		}
		
		try {
			FileOutputStream out = new FileOutputStream(new File(directory+ fileName));
			workbook.write(out);
			out.close();
			logger.info("Mandiri, Excel file "+ fileName +" written successfully, with directory : " + directory);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.fatal(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal(e);
		}
		
	}
	
	public static void createSheet(HSSFWorkbook wb, String sheetName, String sheetTitle, List<Object[]> data, int rownumHeader) {
		HSSFSheet sheet = wb.createSheet(sheetName);
		
	    createCellStylesAndEvaluator(wb);
	    	    
		int rownum = 3;
		int cellnum = 0;
		for (Object[] objArr : data) {
			Row row = sheet.createRow(rownum++);
			cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				
				if(rownumHeader > 4){
					if(rownumHeader == rownum){
						createHeaderStyleTransaction(cell, rownumHeader);
					}
				}else{
					createHeaderStyle(cell,rownum);
				}
				
				if (obj instanceof Date)
					cell.setCellValue((Date) obj);
				else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Double){
					cell.setCellStyle(moneyCells);
					cell.setCellValue((Double) obj);
				}
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
				else if (obj instanceof Long)
					cell.setCellValue((Long) obj);
			}
		}
		
		for(int i=0 ; i<cellnum ; i++){
			sheet.autoSizeColumn(i);
		}
		
		// set title column
		Row title = sheet.createRow(0);
	    Cell cellTitle = title.createCell(0);
	    cellTitle.setCellValue(sheetTitle);
	    cellTitle.setCellStyle(titleCells);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellnum-1));
	}
	
	private static void createCellStylesAndEvaluator(HSSFWorkbook wb) {
        // CreationHelper for create CellStyle
        CreationHelper createHelper = wb.getCreationHelper();

        moneyCells = wb.createCellStyle();
        moneyCells.setDataFormat(createHelper.createDataFormat().getFormat(MONEY_FORMAT));
        
        Font fontBold = wb.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerCells = wb.createCellStyle();
        headerCells.setFont(fontBold);
        headerCells.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerCells.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        
        titleCells = wb.createCellStyle();
        titleCells.setAlignment(CellStyle.ALIGN_CENTER);
        titleCells.setFont(fontBold);
    }
	
	private static void createHeaderStyle(Cell cell,int rownum) {
		if(rownum == 4) {
			cell.setCellStyle(headerCells);
		}
	}
	
	private static void createHeaderStyleTransaction(Cell cell,int rownum) {
		if(rownum == 10) {
			cell.setCellStyle(headerCells);
		}
	}

}
