package com.balicamp.webapp.action.report;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XlstoStringConverter {
	public HashMap<String, String> getMt940Data(String filepath) {
		String[][] data = new String[0][0];
		HashMap<String, String> data2 = new HashMap();
		try {
			InputStream input = new BufferedInputStream(new FileInputStream(filepath));
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(3);

			// int rowNum = sheet.getLastRowNum() + 1;

			HSSFSheet sheetProsesParser = wb.getSheetAt(2);

			HSSFRow rowTotalSukses = sheetProsesParser.getRow(4);
			if (rowTotalSukses == null || (rowTotalSukses != null && (rowTotalSukses.getCell((short) 5) == null || (int) rowTotalSukses.getCell((short) 5).getNumericCellValue() == 0))) {
				return data2;
			} else {
				HSSFCell totalSuksesCell = rowTotalSukses.getCell((short) 5);
				int rowNum = (int) totalSuksesCell.getNumericCellValue() + 8;

				int colNum = sheet.getRow(4).getLastCellNum();
				data = new String[rowNum][colNum];
				String invoiceNo = new String();

				// i=4 karena data berada pada row ke 5
				for (int i = 0; i < rowNum; i++) {
					if (sheet.getRow(i) != null && sheet.getRow(i).getCell((short) 0) != null && isNumeric(cellToString(sheet.getRow(i).getCell((short) 0)))) {
						HSSFRow row = sheet.getRow(i);
						String amount = new String();
						for (short j = 0; j < colNum; j++) {
							if (row.getCell(j) != null) {
								HSSFCell cell = row.getCell(j);
								String value = cellToString(cell);
								data[i][j] = value;
								// ambil invoice no as key
								if (j == 5)
									invoiceNo = value;
								if (j == 7)
									amount = value;
							} else {
								continue;
							}
						}

						data2.put(invoiceNo, amount);
					} else {
						continue;
					}
				}
				// System.out.println("the data value is " + data2);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return data2;
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

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}
}
