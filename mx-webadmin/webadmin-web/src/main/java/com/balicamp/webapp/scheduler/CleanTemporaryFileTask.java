package com.balicamp.webapp.scheduler;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.balicamp.dao.parameter.SystemParameterDao;

public class CleanTemporaryFileTask {

	private final Log log = LogFactory.getLog(CleanTemporaryFileTask.class);

	@Autowired
	private SystemParameterDao paramDao;

	private String filePath;

	public void runCleaner() {
		log.info("@@@@@@@ Cleaning temporary file task Run...... ");
		if (null == filePath) {
			filePath = paramDao.findParamValueByParamName("webadmin.download.temporary.path");
		}

		File file = new File(filePath);
		if (!file.exists()) {
			log.warn("@@@@@@@ Temporari File Tidak ditemukan..... ");
			return;
		}

		if (file.isFile()) {
			log.info("@@@@@@@ Delete file  " + filePath);
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] lstFile = file.listFiles();
			log.info("@@@@@@@ Delete all file in directory " + filePath);
			for (File f : lstFile) {
				this.delete(f);
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
