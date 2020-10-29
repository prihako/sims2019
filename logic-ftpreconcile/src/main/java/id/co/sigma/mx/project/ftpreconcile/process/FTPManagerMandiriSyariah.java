package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.util.PropertiesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;


/**
 * This class will handle upload and download file through ftp
 * @author vicky
 *
 */
public class FTPManagerMandiriSyariah {

	FTPClient ftpClient = null;
	Logger networkLogger = Logger.getLogger("logger");

	private Properties ftpProperties = null;
	private String ftpServerAddress = null;
	private String userName = null;
	private String userPassword = null;
	private Integer ftpServerPort = null;
	private String ftpServerDirAddress = null;
	private String ftpLocalAddress = null;
	private String ftpReportDirectory = null;
	private static String filePattern = null;
	
	/**
	 * Upload a file into ftp server
	 * @param file
	 * @param destFileName
	 * @param directory
	 * @return
	 * @throws Exception
	 */

	public FTPManagerMandiriSyariah(String ftpSettingFile) {
		ftpProperties = PropertiesUtil.fromFile(ftpSettingFile);
		ftpServerAddress = ftpProperties.getProperty("ftp.server.address");
		userName = ftpProperties.getProperty("ftp.username");
		userPassword = ftpProperties.getProperty("ftp.password");
		ftpServerPort = Integer.parseInt(ftpProperties.getProperty("ftp.server.port"));
		ftpLocalAddress = ftpProperties.getProperty("ftp.local.address");
		ftpReportDirectory = ftpProperties.getProperty("ftp.report.address");
		ftpServerDirAddress = ftpProperties.getProperty("ftp.server.directory.address");
		filePattern = ftpProperties.getProperty("file.pattern");
		
//		setFtpServerDirAddress(ftpProperties.getProperty("ftp.server.directory.address"));
	}

	public synchronized boolean upload(File file, String destFileName, String directory) throws Exception
	{
		boolean upload = false;
		if (file.exists())
		{
//			initFTPConfig();
			ftpClient = new FTPClient();
			try {
				InputStream is = new FileInputStream(file);
				ftpClient.connect(ftpServerAddress,ftpServerPort.intValue());
				ftpClient.login(userName,userPassword);

				if (directory != null)
					ftpClient.changeWorkingDirectory(directory);

				upload = ftpClient.storeFile(destFileName,is);
				ftpClient.logout();
			} catch (SocketException e) {
				throw new Exception("could not upload file, " + e.getMessage());
			} catch (IOException e) {
				throw new Exception("could not upload file, " + e.getMessage());
			} finally {
				if (ftpClient.isConnected())
					try {
						ftpClient.disconnect();
					} catch (IOException e) {
						// do nothing
					}
			}
		}
		else
		{
			throw new Exception("Could not upload file, file doesn't exist.");
		}
		return upload;
	}

	/**
	 * Upload a file into ftp server
	 * @param file
	 * @param destFileName
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean append(File file, String destFileName, String directory) throws Exception
	{
		boolean upload = false;
		if (file.exists())
		{
//			initFTPConfig();
			ftpClient = new FTPClient();
			try {
				InputStream is = new FileInputStream(file);
				ftpClient.connect(ftpServerAddress,ftpServerPort.intValue());
				ftpClient.login(userName,userPassword);

				if (directory != null)
					ftpClient.changeWorkingDirectory(directory);

				upload = ftpClient.appendFile(destFileName,is);
				ftpClient.logout();
			} catch (SocketException e) {
				throw new Exception("could not upload file, " + e.getMessage());
			} catch (IOException e) {
				throw new Exception("could not upload file, " + e.getMessage());
			} finally {
				if (ftpClient.isConnected())
					try {
						ftpClient.disconnect();
					} catch (IOException e) {
						// do nothing
					}
			}
		}
		else
		{
			throw new Exception("Could not upload file, file doesn't exist.");
		}
		return upload;
	}

	/**
	 * Upload a file into ftp server
	 * @param file
	 * @param destFileName
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean rename(String filename, String destFileName, String directory) throws Exception
	{
		boolean upload = false;
		if (filename != null && destFileName != null)
		{
//			initFTPConfig();
			ftpClient = new FTPClient();
			try {

				ftpClient.connect(ftpServerAddress,ftpServerPort.intValue());
				ftpClient.login(userName,userPassword);

				if (directory != null)
					ftpClient.changeWorkingDirectory(directory);

				if(!ftpClient.rename(filename, destFileName))
					throw new Exception("Fail to rename file from "  + filename  + " to " + destFileName);

				ftpClient.logout();
			} catch (SocketException e) {
				throw new Exception("could not rename file, " + e.getMessage());
			} catch (IOException e) {
				throw new Exception("could not rename file, " + e.getMessage());
			} finally {
				if (ftpClient.isConnected())
					try {
						ftpClient.disconnect();
					} catch (IOException e) {
						// do nothing
					}
			}
		}
		else
		{
			throw new Exception("could not upload file, file doesn't exist");
		}
		return upload;
	}

	/**
	 * retrieve a file from ftp server and save into local file
	 * @param localFileName
	 * @param remoteFileName
	 * @param directory
	 * @throws Exception
	 */
	public synchronized void retrieve(String localFileName, String remoteFileName, String directory) throws Exception
	{
		File file = new File(localFileName);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			networkLogger.error("could not create file " + localFileName + ", " +e1, e1);
		}

		OutputStream os = null;

		if (file.exists())
		{
//			initFTPConfig();
			ftpClient = new FTPClient();
			try {

				os = new FileOutputStream(file);
				ftpClient.connect(ftpServerAddress,ftpServerPort.intValue());
				ftpClient.login(userName,userPassword);
//				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.changeToParentDirectory();

				if (directory != null){
					ftpClient.changeWorkingDirectory(directory);
				}


				if(!ftpClient.retrieveFile(remoteFileName,os)){
					throw new RuntimeException("Fail when try to retrieve file .... ");
				}


				ftpClient.logout();

			} catch (SocketException e) {
				networkLogger.error("could not connect to " + ftpServerAddress + ", " + e.getMessage(), e);
			} catch (IOException e) {
				networkLogger.error("could not connect to " + ftpServerAddress + ", " + e.getMessage(), e);
			} finally {

				if(os!=null)
					try {os.close();} catch (Exception e) {}

				if(file.exists() && file.canRead())
					networkLogger.info("The file exist & can be read : successfully download .... " );
				else
					networkLogger.info("The file not exist or cannot be read : download Failed .... " );

				if (ftpClient!=null && ftpClient.isConnected())
					try {ftpClient.disconnect();} catch (IOException e) {}
			}
		}
	}

	/**
	 * delete a file at ftp server
	 * @param fileName
	 * @param directory
	 */
	public synchronized void delete(String fileName, String directory)
	{
		ftpClient = new FTPClient();
		try {
//			initFTPConfig();
			ftpClient.connect(ftpServerAddress,ftpServerPort.intValue());
			ftpClient.login(userName,userPassword);
			if (directory != null)
				ftpClient.changeWorkingDirectory(directory);

			if(!ftpClient.deleteFile(fileName))
				throw new RuntimeException("Fail when Try to delete ... " + fileName);

			ftpClient.logout();

		} catch (SocketException e) {
			networkLogger.error(e);
		} catch (IOException e) {
			networkLogger.error(e);
		} finally {
			if (ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// do nothing
				}
		}
	}

	/**
	 * list all file in specified directory at ftp server
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	public FTPFile[] listFiles(String directory) throws Exception
	{
		FTPFile[] files = null;

		ftpClient = new FTPClient();
		try {
//			initFTPConfig();
			ftpClient.connect(ftpServerAddress, ftpServerPort);
			ftpClient.login(userName, userPassword);
			ftpClient.changeToParentDirectory();
			if (directory != null)
				ftpClient.changeWorkingDirectory(directory);
			String[] a = ftpClient.listNames();
			files = ftpClient.listFiles();

			if (a != null) {
				for (int i = 0; i < a.length; i++) {
					System.out.println(a[i]);
				}
			}
		} catch (Exception e) {
			throw new Exception("could not list file, " + e);
		}finally {
			if (ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// do nothing
				}
		}

		return files;
	}

	/**
	 * list all filenames in specified directory at ftp server
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	public String[] listFilesNames(String directory) throws Exception
	{
		String[] names = null;

		ftpClient = new FTPClient();
		try {
//			initFTPConfig();
			ftpClient.connect(ftpServerAddress, ftpServerPort);
			ftpClient.login(userName, userPassword);
			ftpClient.changeToParentDirectory();
			if (directory != null)
				ftpClient.changeWorkingDirectory(directory);
			names = ftpClient.listNames();
		} catch (Exception e) {
			throw new Exception("could not list file, " + e);
		}finally {
			if (ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// do nothing
				}
		}

		return names;
	}
	
	public boolean isEnableToAccess() {
		boolean result = true;
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpServerAddress, ftpServerPort);
			ftpClient.login(userName, userPassword);
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			networkLogger.fatal(e);
		}finally {
			if (ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// do nothing
				}
		}
		return result;
	}

	/**
	 * @return the ftpServerAddress
	 */
	public String getFtpServerAddress() {
		return ftpServerAddress;
	}

	/**
	 * @param ftpServerAddress the ftpServerAddress to set
	 */
	public void setFtpServerAddress(String ftpServerAddress) {
		this.ftpServerAddress = ftpServerAddress;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the ftpServerPort
	 */
	public Integer getFtpServerPort() {
		return ftpServerPort;
	}

	/**
	 * @param ftpServerPort the ftpServerPort to set
	 */
	public void setFtpServerPort(Integer ftpServerPort) {
		this.ftpServerPort = ftpServerPort;
	}

	/**
	 * @return the ftpLocalAddress
	 */
	public String getFtpLocalAddress() {
		return ftpLocalAddress;
	}

	/**
	 * @param ftpLocalAddress the ftpLocalAddress to set
	 */
	public void setFtpLocalAddress(String ftpLocalAddress) {
		this.ftpLocalAddress = ftpLocalAddress;
	}
	/**
	 * @return the ftpProperties
	 */
	public Properties getFtpProperties() {
		return ftpProperties;
	}

	/**
	 * @param ftpProperties the ftpProperties to set
	 */
	public void setFtpProperties(Properties ftpProperties) {
		this.ftpProperties = ftpProperties;
	}

	public String getFtpReportDirectory() {
		return ftpReportDirectory;
	}

	public void setFtpReportDirectory(String ftpReportDirectory) {
		this.ftpReportDirectory = ftpReportDirectory;
	}

	public String getFtpServerDirAddress() {
		return ftpServerDirAddress;
	}

	public void setFtpServerDirAddress(String ftpServerDirAddress) {
		this.ftpServerDirAddress = ftpServerDirAddress;
	}

	
	
//	private void initFTPConfig()
//	{
//		paymentProperties = new Properties(System.getProperties());
//
//		try {
//			FileInputStream propFile = new FileInputStream("src/main/resources/ftp.properties");
//			paymentProperties.load(propFile);
//
//			ftpServerAddress = paymentProperties.getProperty("ftp.server.address");
//			userName = paymentProperties.getProperty("ftp.username");
//			userPassword = paymentProperties.getProperty("ftp.password");
//			ftpServerPort = Integer.parseInt(paymentProperties.getProperty("ftp.server.port"));
//		} catch (Exception e) {
//			networkLogger.error("could not init ftp configuration, " + e.getMessage());
//		}
//	}
	
	/**
	 * @return the filePattern
	 */
	public static String getFilePattern() {
		return filePattern;
	}

	/**
	 * @param filePattern the filePattern to set
	 */
	public void setFilePattern(String filePattern) {
		FTPManagerMandiriSyariah.filePattern = filePattern;
	}

}