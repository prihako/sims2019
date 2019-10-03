package com.balicamp.service.report;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ZipManager {
	public File zipFiles(List<String> files, File zipFile) throws IOException;

	public File zipFiles(List<String> files, String folder, String namaFile) throws IOException;

	public File zipFiles(List<String> files, String folder) throws IOException;

	public String generateFileName(List<String> files);
}
