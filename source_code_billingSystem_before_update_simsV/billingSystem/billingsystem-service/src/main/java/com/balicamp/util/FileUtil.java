package com.balicamp.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;

import com.balicamp.Constants;
import com.balicamp.exception.ApplicationException;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */

public class FileUtil {
	private static final Log log = LogFactory.getLog(FileUtil.class);// NOPMD

	public static String getDirPath(String completeFilePath) {
		return (new File(completeFilePath)).getParent();
	}

	/**
	 * Copy file and create destination dir if not exist
	 * @param inputStream
	 * @param destination
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public final static void copy(InputStream inputStream, File destination) throws FileNotFoundException, IOException {
		if (!destination.getParentFile().exists())
			destination.getParentFile().mkdirs();
		FileCopyUtils.copy(inputStream, new FileOutputStream(destination));
	}

	/**
	 * 
	 * @param data
	 * @param destination
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public final static void copy(byte[] data, File destination) throws FileNotFoundException, IOException {
		if (!destination.getParentFile().exists())
			destination.getParentFile().mkdirs();
		FileCopyUtils.copy(data, new FileOutputStream(destination));
	}

	public final static void mkdirs(File dirFile) throws FileNotFoundException, IOException {
		if (!dirFile.exists())
			dirFile.mkdirs();
	}

	public final static void mkdirs(String dirString) throws FileNotFoundException, IOException {
		mkdirs(new File(dirString));
	}

	/**
	 * extrack file extension
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		return fileName.replaceAll(fileExtPattern, "").toLowerCase();
	}

	private static String fileExtPattern = ".+[.]";

	/**
	 * @param fileName
	 * @return
	 */
	public static String getFileNameName(String fileName) {
		return fileName.replaceAll(fileNamePattern, "");
	}

	private static String fileNamePattern = "[.].+";

	/**
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName) {
		return new File(fileName).getName();
	}

	/**
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String inputUrl) {
		try {
			URL url = new URL(inputUrl);
			return getFileName(url.getPath());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param fileName
	 * @return
	 */
	public static boolean isImageFile(String fileName) {
		return fileName.matches(imageFilePattern.toLowerCase());
	}

	private static String imageFilePattern = ".+[.]jpg|.+[.]jpeg|.+[.]gif|.+[.]png";

	public static String readFile(final String location) {
		final StringBuffer contents = new StringBuffer();

		BufferedReader input = null;
		try {

			input = new BufferedReader(new FileReader(location));
			String line = null;

			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException ex) {
			log.error(ex.getMessage());
		} catch (IOException ex) {
			log.error(ex.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}
		}

		return contents.toString();
	}

	/**
	 * read input stream return byte array
	 * @param inputStream
	 * @return
	 */
	public static byte[] streamToByteArray(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			byte[] buffer = new byte[1000];
			int readLength = -1;
			while ((readLength = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
		} catch (Exception e) { // NOPMD
			// ignore just return available byte
		}

		return outputStream.toByteArray();
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String streamToString(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			byte[] buffer = new byte[1000];
			int readLength = -1;
			while ((readLength = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
		} catch (Exception e) { // NOPMD
			// ignore just return available byte
		}

		return outputStream.toString();
	}

	private final static Map<String, String> contentTypeMap;
	static {
		contentTypeMap = new HashMap<String, String>();
		contentTypeMap.put("pdf", "application/pdf");
		contentTypeMap.put("rtf", "application/rtf");
		contentTypeMap.put("xls", "application/xls");
		contentTypeMap.put("csv", "application/csv");
		contentTypeMap.put("odt", "application/vnd.oasis.opendocument.text");
		contentTypeMap.put("doc", "application/msword");
		contentTypeMap.put("pps", "application/vnd.ms-pps");
		contentTypeMap.put("txt", "text/plain");
		contentTypeMap.put("log", "text/plain");

		contentTypeMap.put("jpg", "image/jpeg");
		contentTypeMap.put("jpeg", "text/plain");
		contentTypeMap.put("gif", "image/gif");
		contentTypeMap.put("png", "image/png");

	}

	/**
	 * get content type from file extendsion
	 * @param fileExt
	 * @return
	 */
	public static String getContentType(String fileExt) {
		if (fileExt == null)
			return null;

		fileExt = fileExt.toLowerCase();
		String contentType = contentTypeMap.get(fileExt);
		return contentType;
	}

	public static String createZipFile(String outFilename, File dir) throws IOException {
		if (outFilename == null)
			outFilename = dir.getAbsolutePath() + ".zip";
		File[] fileList = dir.listFiles();
		String[] fileNameList = new String[fileList.length];
		for (int i = 0; i < fileList.length; i++) {
			fileNameList[i] = fileList[i].getAbsolutePath();
		}
		createZipFile(outFilename, fileNameList);
		return outFilename;
	}

	public static void createZipFile(String outFilename, String[] filenames) throws IOException {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		// Create the ZIP file
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

		// Compress the files
		for (int i = 0; i < filenames.length; i++) {
			FileInputStream in = new FileInputStream(filenames[i]);

			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(FileUtil.getFileName(filenames[i])));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Complete the entry
			out.closeEntry();
			in.close();
		}

		// Complete the ZIP file
		out.close();
	}

	public static List<String> unzipFile(String baseExtractDir, String inFilename) throws IOException {
		if (CommonUtil.isEmpty(baseExtractDir))
			baseExtractDir = getDirPath(inFilename);
		String subExtractDir = getFileNameName(getFileName(inFilename));
		String extractDir = baseExtractDir + Constants.FILE_SEP + subExtractDir;
		mkdirs(extractDir);

		// Open the ZIP file
		ZipInputStream in = new ZipInputStream(new FileInputStream(inFilename));
		try {
			List<String> result = new ArrayList<String>();

			// Get the first entry
			ZipEntry entry = in.getNextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					throw new ApplicationException("backup file is not valid, tidak boleh mengandung sub directory");
				} else {
					// Open the output file
					String outFilename = entry.getName();
					String outFilenameComplete = extractDir + Constants.FILE_SEP + Constants.FILE_SEP + outFilename;
					OutputStream out = new FileOutputStream(outFilenameComplete);
					try {
						// Transfer bytes from the ZIP file to the output file
						byte[] buf = new byte[1024];
						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						// Close the streams
					} finally {
						out.close();
					}

					result.add(outFilenameComplete);
				}
				entry = in.getNextEntry();
			}
			return result;
		} finally {
			in.close();
		}
	}

	public static boolean deleteRecursive(String path) {
		File file = new File(path);

		if (!file.exists())
			return false;

		if (file.isDirectory()) {
			File[] subFileList = file.listFiles();
			for (File subFile : subFileList) {
				deleteRecursive(subFile.getAbsolutePath());
			}
		}

		boolean status = file.delete();
		return status;
	}

}
