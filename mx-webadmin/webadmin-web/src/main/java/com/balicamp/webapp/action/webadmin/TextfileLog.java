package com.balicamp.webapp.action.webadmin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.component.StringValuePropertySelectionModel;

public abstract class TextfileLog extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {

	public abstract String getLog();

	public abstract void setLog(String log);

	public abstract String getLogFile();

	public abstract void setLogFile(String logFile);

	List<String> fileLocationList = new ArrayList<String>();
	List<String> fileNameList = new ArrayList<String>();

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);

		StringBuffer buffer = new StringBuffer();
		String temp = new String();

		try {
			String fileLocation = getSystemParameterManager().findParamValueByParamName("log.file.location");
			FileReader file = new FileReader(fileLocation + "/fuse.log");
			BufferedReader br = new BufferedReader(file);

			LinkedList<String> a = new LinkedList<String>();
			int i = 0;
			while ((temp = br.readLine()) != null) {
				i++;
				a.push(temp);
				if (i > 100)
					a.removeLast();
			}

			while (!a.isEmpty()) {
				buffer.append(a.pop() + "\r");
			}

			br.close();
			setLog(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	public void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			// skip running log
			if (node.getName().endsWith(".log")) {
				String fileToZip = (node.getPath().toString());
				fileLocationList.add(fileToZip);
				fileNameList.add(node.getName());
			}
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	public StringValuePropertySelectionModel getLogFileModel() {
		String fileLocation = getSystemParameterManager().findParamValueByParamName("log.file.location");
		File directory = new File(fileLocation);
		fileLocationList = new ArrayList<String>();
		fileNameList = new ArrayList<String>();
		this.generateFileList(directory);

		return new StringValuePropertySelectionModel(fileNameList.toArray(new String[0]),
				fileLocationList.toArray(new String[0]));
	}

	public IPage onDownload() {
		this.downloadFile(getLogFile());
		return null;
	}

	protected void downloadFile(String file) {
		log.info("downloading : " + file);
		HttpServletResponse respon = getResponse();
		try {
			String context = getBaseUrl();
			respon.sendRedirect(context + "/download?mode=simple&fileName=" + file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
