/**
 * 
 */
package com.balicamp.util.mx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.balicamp.model.mx.MessageLogsDto;
import com.balicamp.util.zip.EasyZip;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * Util for Wrap List of Object Java to Word Document
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: WriteToWord.java 393 2013-03-26 07:30:54Z wayan.agustina $
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
			dirFile.mkdir();

			RtfWriter2 rtfw = RtfWriter2.getInstance(doc, new FileOutputStream(new File(fullPath + "/" + fileName)
					+ ".doc"));
			doc.open();

			int page = 0;
			for (StringBuffer str : tmps) {
				doc.add(new Paragraph(str.toString().replaceFirst("\\s+", "").replaceAll("\r+|\n+", "\r\n")));
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
	
	/*
	 * 	set null value to ""
	 */
	public static void checkNullField(MessageLogsDto dis) {
		try {
			for (Field f : dis.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.get(dis) == null) {
					f.set(dis, "");
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
