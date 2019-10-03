package com.balicamp.webapp.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FTPManager {

	private static String ftpServerAddress;
	private static int ftpPort;
	private static String ftpUsername;
	private static String ftpPassword;
	private static String directoryServer;

	// private static Properties prop = new Properties();

	public FTPManager(String serverAddress, String port, String username, String password, String serverDirectory) {

		ftpServerAddress = serverAddress;
		ftpPort = Integer.valueOf(port);
		ftpUsername = username;
		ftpPassword = password;
		directoryServer = serverDirectory;
	}

	public String[] listFileNames(String directory) throws Exception {

		String[] fileNames = null;

		FTPClient ftpClient = new FTPClient();
		try {
			// initFTPConfig();
			ftpClient.connect(ftpServerAddress, ftpPort);
			ftpClient.login(ftpUsername, ftpPassword);
			// ftpClient.changeToParentDirectory();
			if (directory != null) {
				boolean test = ftpClient.changeWorkingDirectory(directory);
				if (!test) {
					System.out.println("***********Failed change to directory " + directory);
				}
			}
			fileNames = ftpClient.listNames();

		} catch (Exception e) {
			throw new Exception("could not list file, " + e);
		} finally {
			if (ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// do nothing
				}
		}

		return fileNames;
	}

	public static String getFtpServerAddress() {
		return ftpServerAddress;
	}

	public void setFtpServerAddress(String ftpServerAddress) {
		this.ftpServerAddress = ftpServerAddress;
	}

	public static int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public static String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public static String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public static String getDirectoryServer() {
		return directoryServer;
	}

	public void setDirectoryServer(String directoryServer) {
		this.directoryServer = directoryServer;
	}

}
