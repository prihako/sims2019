package com.balicamp.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class EasyZip {


	public static void zip(String pSrcFolder, String pZipFile)
			throws IOException {
		EasyZip.zip(pSrcFolder, pZipFile, new EasyZipParameters());
	}

	public static void zip(String pSrcFolder, String pZipFile,EasyZipParameters pEasyZipParameters) throws IOException {
		
		String basePath = new File(pSrcFolder).getCanonicalPath();
		
		if (pEasyZipParameters == null) {
			pEasyZipParameters = new EasyZipParameters();
		}
		
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(new File(pZipFile)));
			
			if (pEasyZipParameters.isZipFolderContentsNotFolder()) {
				FilesThenDirsIterator ftd = new FilesThenDirsIterator(new File(
						pSrcFolder));
				while (ftd.hasNext()) {
					File file = (File) ftd.next();
					if (!file.isDirectory()) {
						addFileToZip("", file.getPath(), zipOut, false);
					}
					if (file.isDirectory()) {
						addFolderToZip("", file.getPath(), zipOut);
					}
				}
			} else {
				addFolderToZip("", basePath, zipOut);
			}
		} finally {
			IOUtils.closeQuietly(zipOut);
		}
	}

	private static void addFileToZip(String path, String srcFile,ZipOutputStream zip, boolean pIsEmptyFolder) throws IOException {
		
		String prefix = path.equals("") ? "" : path + "/";
		File folder = new File(srcFile);

		String name = prefix + folder.getName();
		if (pIsEmptyFolder == true) {
			System.out.println("adding empty dir: " + name);
			zip.putNextEntry(new ZipEntry(name + "/"));
		} else {
			if (!folder.isDirectory()) {
				System.out.println("adding file: " + name);

				FileInputStream in = null;
				try {
					zip.putNextEntry(new ZipEntry(name));
					in = new FileInputStream(srcFile);
					IOUtils.copy(in, zip);
				} finally {
					IOUtils.closeQuietly(in);
				}
			} else {
				addFolderToZip(path, srcFile, zip);
			}
		}
	}

	private static void addFolderToZip(String path, String srcFolder,ZipOutputStream zip) throws IOException {
		
		File folder = new File(srcFolder);

		if (folder.list().length == 0) {
			addFileToZip(path, srcFolder, zip, true);
		} else {
			FilesThenDirsIterator ftd = new FilesThenDirsIterator(folder);
			while (ftd.hasNext()) {
				String fileName = ftd.next().getName();
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName,
							zip, false);
				} else {
					addFileToZip(path + "/" + folder.getName(), srcFolder + "/"
							+ fileName, zip, false);
				}
			}
		}
	}

	public static InputStream getInputStreamForEntry(String pZip,
			String pEntryName) throws IOException {
		ZipFile zipFile = new ZipFile(pZip);
		return zipFile.getInputStream(zipFile.getEntry(pEntryName));
	}

	public static String getStringForEntry(String pZip, String pEntryName)
			throws IOException {
		InputStream inputStreamForEntry = null;
		try {
			inputStreamForEntry = getInputStreamForEntry(pZip, pEntryName);
			return IOUtils.toString(inputStreamForEntry);
		} finally {
			IOUtils.closeQuietly(inputStreamForEntry);
		}
	}

}