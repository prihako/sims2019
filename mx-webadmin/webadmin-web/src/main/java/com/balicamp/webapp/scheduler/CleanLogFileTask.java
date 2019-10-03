package com.balicamp.webapp.scheduler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.dao.parameter.SystemParameterDao;

public class CleanLogFileTask {

	private final Log log = LogFactory.getLog(CleanLogFileTask.class);

	@Autowired
	private SystemParameterDao paramDao;

	private String filePath;
	private String archivedPath;

	public void runCleaner() {
		log.info("Running housekeeping log file");
		if (null == filePath) {
			filePath = paramDao.findParamValueByParamName("scheduler.log.path.running");
		}

		if (archivedPath == null) {
			archivedPath = paramDao.findParamValueByParamName("scheduler.log.path.archived");
		}

		File file = new File(filePath);
		if (!file.exists()) {
			log.warn("@@@@@@@ Temporari File Tidak ditemukan..... ");
			return;
		}

		if (file.isDirectory()) {
			File[] lstFile = file.listFiles();
			for (File f : lstFile) {
				if (f.getName().endsWith(".zip")) {
					File copy = new File(archivedPath + f.getName());
					try {
						FileUtils.copyFile(f, copy);
					} catch (IOException e) {
						log.error(e);
					}
					this.delete(f);
				}
			}
		}
	}

	public void delete(File file) {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();
				System.out.println("Directory is deleted : " + file.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					log.info("Directory is deleted : " + file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
			log.info("File is deleted : " + file.getAbsolutePath());
		}
	}
}
