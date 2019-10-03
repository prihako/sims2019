package com.balicamp.webapp.action.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;

import test.Constants;

import com.balicamp.webapp.ftp.FTPManager;

public class FileService implements IEngineService {
	private HttpServletResponse response;
	private LinkFactory linkFactory;

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setLinkFactory(LinkFactory linkFactory) {
		this.linkFactory = linkFactory;
	}

	@Override
	public ILink getLink(boolean post, Object parameter) {
		String fileName = (String) ((Object[]) parameter)[0];
		String transactionType = (String) ((Object[]) parameter)[1];
		Map parameters = new HashMap();
		parameters.put("fileName", fileName);
		parameters.put("transactionType", transactionType);
		return linkFactory.constructLink(this, post, parameters, false);
	}

	@Override
	public String getName() {
		return "file";
	}

	@Override
	public void service(IRequestCycle cycle) throws IOException {
		String fileName = (cycle.getParameter("fileName"));
		String transactionType = (cycle.getParameter("transactionType"));
		byte imageData[] = getBytes(fileName, transactionType);

		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/xls");

		try {
			OutputStream out = response.getOutputStream();
			out.write(imageData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getBytes(String remoteFile, String transactionType) {

		System.out.println("filename : " + remoteFile);
		System.out.println("transactionType : " + transactionType);

		String ftpServerAddress = FTPManager.getFtpServerAddress();
		int ftpPort = FTPManager.getFtpPort();
		String ftpUsername = FTPManager.getFtpUsername();
		String ftpPassword = FTPManager.getFtpPassword();

		// ditambahkan pd tgl 04-09-2014, untuk mengakomodir multiple file
		// reconcile
		String directoryServer = FTPManager.getDirectoryServer();
		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			directoryServer += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		FTPClient ftpClient = null;
		InputStream binaryFile = null;

		ftpClient = new FTPClient();
		try {

			ftpClient.connect(ftpServerAddress, ftpPort);
			ftpClient.login(ftpUsername, ftpPassword);
			ftpClient.changeWorkingDirectory(directoryServer);

			binaryFile = ftpClient.retrieveFileStream(remoteFile);

			if (binaryFile != null) {
				byte[] bytes = IOUtils.toByteArray(binaryFile);
				return bytes;
			}

			ftpClient.logout();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (ftpClient != null && ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {}

			if (binaryFile != null) {
				try {
					binaryFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

}
