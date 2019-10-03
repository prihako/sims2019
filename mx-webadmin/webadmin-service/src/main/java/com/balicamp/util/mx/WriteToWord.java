/**
 * 
 */
package com.balicamp.util.mx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.balicamp.model.user.User;
import com.balicamp.util.DateUtil;
import com.balicamp.util.zip.EasyZip;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Jpeg;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.Watermark;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * Util for Wrap List of Object Java to Word Document
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: WriteToWord.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public class WriteToWord {

	private static SimpleDateFormat formater = new SimpleDateFormat("dd_MMM_yyyy_mm_ss");

	private static String initFullPath(String path, String fName) {

		File tmpReport = new File(path + "/" + "test.txt");
		try {
			if (!tmpReport.createNewFile()) {
				throw new RuntimeException("Tidak bisa menulis pada directory " + path);
			} else {
				tmpReport.delete();
				String fullPath = path + "/" + fName;
				return fullPath;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String initFullPathNotWithDirectory(String path, String fName) {

		File tmpReport = new File(path + "/" + "test.txt");
		try {
			if (!tmpReport.createNewFile()) {
				throw new RuntimeException("Tidak bisa menulis pada directory " + path);
			} else {
				tmpReport.delete();
				String fullPath = path;
				return fullPath;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String initFileName(String fName) {
		return fName + "_" + formater.format(new Date());
	}

	/**
	 * Write to customize path as *.doc
	 * @param tmps List of StringBuffer to include in Word File
	 * @param path String path to write
	 * @param fName String file name destination *.doc
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public static String doWrite(List<StringBuffer> tmps, String path, String fName) {
		Document doc = new Document();

		try {

			String fileName = initFileName(fName);
			String fullPath = initFullPath(path, fileName);
			File dirFile = new File(fullPath);

			System.out.println("WriteToWord fileName " + fileName);
			System.out.println("WriteToWord fullPath " + fullPath);

			dirFile.mkdir();

			RtfWriter2 rtfw = RtfWriter2.getInstance(doc, new FileOutputStream(new File(fullPath + "/" + fileName)
					+ ".doc"));
			doc.open();

			int page = 0;
			for (StringBuffer str : tmps) {
				doc.add(new Paragraph(str.toString().replaceAll("\r+|\n+", "\n")));
				if (page != tmps.size() - 1)
					doc.newPage();
				page++;

			}

			doc.close();
			rtfw.close();

			// zip file Word
			String zipName = fileName + ".zip";
			EasyZip.zip(fullPath, path + "/" + zipName);
			System.out.println("ZIP successfully to " + fullPath + "/" + zipName);

			return zipName;
		} catch (FileNotFoundException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (IOException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write to customize path as *.doc
	 * @param tmps List of StringBuffer to include in Word File
	 * @param path String path to write
	 * @param fName String file name destination *.doc
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public static String doWriteNotCompress(List<StringBuffer> tmps, String path, String fName) {
		Document doc = new Document();

		try {

			String fileName = initFileName(fName);
			String fullPath = initFullPathNotWithDirectory(path, fileName);
			File dirFile = new File(fullPath);

			System.out.println("WriteToWord fileName " + fileName);
			System.out.println("WriteToWord fullPath " + fullPath);

			dirFile.mkdir();

			RtfWriter2 rtfw = RtfWriter2.getInstance(doc, new FileOutputStream(new File(fullPath + "/" + fileName)
					+ ".doc"));
			doc.open();

			int page = 0;
			for (StringBuffer str : tmps) {
				doc.add(new Paragraph(str.toString().replaceAll("\r+|\n+", "\n")));
				if (page != tmps.size() - 1)
					doc.newPage();
				page++;

			}

			doc.close();
			rtfw.close();

			String zipName = fileName + ".doc";

			return zipName;
		} catch (FileNotFoundException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (IOException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {
		String a = "";

		Document doc = new Document();

		try {

			// String fileName = initFileName("test");
			// String fullPath = initFullPath("D:/", "haloo");
			// File dirFile = new File(fullPath);
			// dirFile.mkdir();

			RtfWriter2 rtfw = RtfWriter2.getInstance(doc, new FileOutputStream(new File("D:\\haloo.doc")));
			// Image img = Image.getInstance("D:/SIGMA/arium.jpg");
			// img.setAlignment(1);
			// img.setAbsolutePosition(0, 0);

			doc.open();
			doc.setPageSize(PageSize.A4.rotate());
			Watermark watermark = new Watermark(Image.getInstance("D:/SIGMA/watermark.jpg"), 320, 200);
			doc.add(watermark);
			//
			// List<StringBuffer> tmps = new ArrayList<StringBuffer>();
			// tmps.add(new StringBuffer("Hei\nHehe"));
			// tmps.add(new StringBuffer("Hui\r"));
			//
			// int page = 0;
			// for (StringBuffer str : tmps) {
			// doc.add(new Paragraph(str.toString()));
			// Table table = new Table(2);
			// table.setWidth(100f);
			// table.setWidths(new float[] { 20f, 80f });
			// table.setBorderWidth(1);
			// table.setBorderColor(new Color(0, 0, 255));
			// table.setPadding(5);
			// table.setSpacing(5);
			//
			// Cell cell = new Cell("header");
			// cell.setHeader(true);
			// cell.setColspan(2);
			// table.addCell(cell);
			// table.endHeaders();
			//
			// Cell newCell = new Cell(new Phrase("Tes1",
			// FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD)));
			// Cell newCell2 = new Cell(new Phrase("Tes2",
			// FontFactory.getFont(FontFactory.TIMES, 14)));
			// table.addCell(newCell);
			// table.addCell(newCell2);
			// doc.add(table);
			// if (page != tmps.size() - 1)
			// doc.newPage();
			// page++;
			//
			// Table foot = new Table(3);
			// foot.setWidth(100f);
			// Cell kiri = new Cell("telkomsigma 2014");
			// Cell tengah = new Cell("Confidential");
			// Cell kanan = new Cell("Arium ESB");
			// kiri.setBorder(0);
			// kiri.setBorder(1);
			// kiri.setHorizontalAlignment(0);
			//
			// tengah.setBorder(0);
			// tengah.setHorizontalAlignment(1);
			// tengah.setBorder(1);
			//
			// kanan.setBorder(0);
			// kanan.setHorizontalAlignment(2);
			// kanan.setBorder(1);
			//
			// foot.addCell(kiri);
			// foot.addCell(tengah);
			// foot.addCell(kanan);
			// foot.disableBorderSide(0);
			// foot.setBorderWidth(0);
			// Phrase ab = new Phrase("");
			// ab.add(foot);
			//
			// MxFooter footer = new MxFooter(ab, true);
			//
			// String linkImage = "D:/SIGMA/telkomsigma.jpg";
			// Jpeg im = new Jpeg(linkImage);
			// im.scalePercent(20);
			//
			// Table head = new Table(4, 2);
			// head.setWidth(100f);
			// Cell headKiri = new Cell();
			// headKiri.setBorder(0);
			// headKiri.setBorder(2);
			// headKiri.setHorizontalAlignment(0);
			// headKiri.add(im);
			// Cell headKanan = new Cell();
			// headKanan.setBorder(0);
			// headKanan.setBorder(2);
			// headKanan.setHorizontalAlignment(2);
			//
			// head.addCell(headKiri);
			// head.addCell(headKanan);
			//
			// Phrase header = new Phrase("");
			// header.add(head);
			//
			// doc.setFooter(footer);
			// doc.setHeader(new HeaderFooter(header, false));
			// }

			doc.close();
			rtfw.flush();
			rtfw.close();

			// zip file Word
		} catch (FileNotFoundException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (IOException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static String doWriteTable(List<List<Cell>> tmps, String path, String fName, String pathFromServlet,
			User user) {
		Document doc = new Document();

		try {

			String fileName = initFileName(fName);

			if (!path.startsWith("/"))
				path = "/" + path;

			File dirFile = new File("");
			String docPath = dirFile.getAbsolutePath() + path + "/" + fileName;

			path = dirFile.getAbsolutePath() + path;

			new File(docPath).mkdir();

			RtfWriter2 rtfw = RtfWriter2.getInstance(doc, new FileOutputStream(new File(docPath + "/" + fileName)
					+ ".doc"));
			doc.open();

			int page = 0;

			for (List<Cell> str : tmps) {
				Table table = new Table(2);
				// table.setTableFitsPage(true);
				table.setWidth(100f);
				table.setWidths(new float[] { 40f, 60f });
				for (Cell cell : str) {
					table.addCell(cell);
				}
				doc.add(table);
				if (page != tmps.size() - 1)
					doc.newPage();
				page++;
			}

			// set header & footer
			setHeaderAndFooter(doc, pathFromServlet, user);

			// set watermark
			// Watermark wm = new Watermark(image, offsetX, offsetY);

			doc.close();
			rtfw.flush();
			rtfw.close();

			// zip file Word
			String zipName = fileName + ".zip";
			EasyZip.zip(docPath, path + "/" + zipName);

			return zipName;
		} catch (FileNotFoundException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (IOException e1) {
			// do nothing
			e1.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static void setHeaderAndFooter(Document doc, String pathFromServlet, User user) throws Exception {
		String telkomsigmaLogo = pathFromServlet + "images/telkomsigma.jpg";
		Jpeg imageTelkomsigma = new Jpeg(telkomsigmaLogo);
		imageTelkomsigma.scalePercent(15);

		String clientLogo = pathFromServlet + "images/client.jpg";
		Jpeg imageClient = new Jpeg(clientLogo);

		Table headTable = new Table(4, 2);
		headTable.setWidths(new float[] { 35f, 35f, 15f, 15f });
		headTable.setWidth(100f);
		Cell headKiri = new Cell();
		headKiri.setBorder(0);
		headKiri.setBorder(2);
		headKiri.setHorizontalAlignment(0);
		headKiri.setRowspan(2);
		headKiri.add(imageTelkomsigma);

		Cell headTengah = new Cell();
		headTengah.setBorder(0);
		headTengah.setBorder(2);
		headTengah.setRowspan(2);
		headTengah.setHorizontalAlignment(2);
		headTengah.add(imageClient);

		headTable.addCell(headKiri);
		headTable.addCell(headTengah);

		Cell generatedBy = new Cell("Generated By");
		generatedBy.setBorder(0);
		headTable.addCell(generatedBy);

		Cell generatedBy2 = new Cell(":" + user.getUserName());
		generatedBy2.setBorder(0);
		headTable.addCell(generatedBy2);

		Cell generatedDate = new Cell("Generated Date");
		generatedDate.setBorder(0);
		generatedDate.setBorder(2);
		headTable.addCell(generatedDate);

		Cell generatedDate2 = new Cell(":" + DateUtil.convertDateToString(new Date(), "dd-MM-yyyy HH:mm:ss"));
		generatedDate2.setBorder(0);
		generatedDate2.setBorder(2);
		headTable.addCell(generatedDate2);

		Phrase headerPhrase = new Phrase("");
		headerPhrase.add(headTable);

		Table footTable = new Table(3);
		footTable.setWidth(100f);
		Cell kiri = new Cell("telkomsigma 2014");
		Cell tengah = new Cell("Confidential");
		Cell kanan = new Cell("Auto Generated Logs Data");
		kiri.setBorder(0);
		kiri.setBorder(1);
		kiri.setHorizontalAlignment(0);

		tengah.setBorder(0);
		tengah.setHorizontalAlignment(1);
		tengah.setBorder(1);

		kanan.setBorder(0);
		kanan.setHorizontalAlignment(2);
		kanan.setBorder(1);

		footTable.addCell(kiri);
		footTable.addCell(tengah);
		footTable.addCell(kanan);
		footTable.disableBorderSide(0);
		footTable.setBorderWidth(0);
		Phrase footerPhrase = new Phrase("");
		footerPhrase.add(footTable);

		HeaderFooter footer = new HeaderFooter(footerPhrase, true);
		HeaderFooter header = new HeaderFooter(headerPhrase, false);

		doc.setHeader(header);
		doc.setFooter(footer);
	}
}
