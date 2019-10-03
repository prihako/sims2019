package com.balicamp.webapp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import test.Constants;

import com.balicamp.dao.parameter.SystemParameterDao;
import com.balicamp.webapp.common.DownloadSession;
import com.balicamp.webapp.ftp.FTPManager;

public class DownloaderServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(DownloaderServlet.class.getName());

	private SystemParameterDao paramDao;

	private String reportPath = null;

	private DownloadSession session = null;

	@Override
	public void init() throws ServletException {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		File file = new File("");
		System.out.println(file.getAbsolutePath());
		if (null == session) {
			session = context.getBean(DownloadSession.class);
		}

		if (null == paramDao) {
			paramDao = context.getBean(SystemParameterDao.class);
		}

		reportPath = paramDao.findParamValueByParamName("webadmin.download.temporary.path");
		if (null == reportPath) {
			throw new RuntimeException("PARAMETER UNTUK REPORT PATH DG KEY webadmin.download.temporary.path BELUM DI SET");
		}

		if (!reportPath.startsWith("/"))
			reportPath = "/" + reportPath;

		// adding report path to absolute path
		// reportPath = file.getAbsolutePath() + reportPath;

		File reportDir = new File(reportPath);
		if (!reportDir.exists()) {
			if (!reportDir.mkdir()) {
				throw new ServletException("ADA KESALAHAN PADA PATH REPORT PATH " + reportPath);
			}
		}

		File tmpReport = new File(reportDir + "/" + "test.txt");
		try {
			if (!tmpReport.createNewFile()) {
				throw new RuntimeException("Tidak bisa menulis pada directory " + reportPath);
			} else {
				tmpReport.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getParameter("mode") != null)
			simpleDownloadReport(req, resp);
		else if (req.getParameter("transactionType") != null && req.getParameter("reconcileTypeReport") == null) {
			// downloadTransactionReport(req, resp);
			downloadReportParsingMT940(req, resp);
		} else if (req.getParameter("fileReportReconcile") != null && req.getParameter("reconcileTypeReport") == null) {
			downloadTransactionReport(req, resp);
		} else if (req.getParameter("transactionType") != null && req.getParameter("reconcileTypeReport") != null) {
			// downloadTransactionReport(req, resp);
			if (req.getParameter("reconcileTypeReport").equalsIgnoreCase("eod")) {
				simpleDownloadReportReconcileEod(req, resp);
			} else if (req.getParameter("reconcileTypeReport").equalsIgnoreCase("weekly")) {
				simpleDownloadReportReconcileWeekly(req, resp);
			} else if (req.getParameter("reconcileTypeReport").equalsIgnoreCase("monthly")) {
				simpleDownloadReportReconcileMonthly(req, resp);
			}
		} else {
			downloadReport(req, resp);
		}

	}

	private void downloadTransactionReport(HttpServletRequest req, HttpServletResponse resp) {

		String ftpServerAddress = FTPManager.getFtpServerAddress();
		int ftpPort = FTPManager.getFtpPort();
		String ftpUsername = FTPManager.getFtpUsername();
		String ftpPassword = FTPManager.getFtpPassword();
		String transactionType = req.getParameter("transactionType");
		String remoteFile = req.getParameter("fileName");

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
			// ftpClient.changeToParentDirectory();
			ftpClient.changeWorkingDirectory(directoryServer);

			binaryFile = ftpClient.retrieveFileStream(remoteFile);

			if (binaryFile != null) {
				byte[] bytes = IOUtils.toByteArray(binaryFile);

				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + remoteFile);

				OutputStream out = resp.getOutputStream();
				out.write(bytes);
			}

			ftpClient.logout();

		} catch (SocketException e) {

			logger.trace(e);

		} catch (IOException e) {

			logger.trace(e);

		} finally {

			if (ftpClient != null && ftpClient.isConnected())
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
				}

			if (binaryFile != null) {
				try {
					binaryFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getParameter("mode") != null)
			simpleDownloadReport(req, resp);
		else if (req.getParameter("transactionType") != null && req.getParameter("reconcileTypeReport") == null) {
			// downloadTransactionReport(req, resp);
			downloadReportParsingMT940(req, resp);
		} else if (req.getParameter("fileReportReconcile") != null && req.getParameter("reconcileTypeReport") == null) {
			downloadTransactionReport(req, resp);
		} else if (req.getParameter("transactionType") != null && req.getParameter("reconcileTypeReport") != null) {
			// downloadTransactionReport(req, resp);
			if (req.getParameter("reconcileTypeReport").equalsIgnoreCase("eod")) {
				simpleDownloadReportReconcileEod(req, resp);
			} else if (req.getParameter("reconcileTypeReport").equalsIgnoreCase("weekly")) {
				simpleDownloadReportReconcileEod(req, resp);
			}
		} else {
			downloadReport(req, resp);
		}
	}

	private void downloadReport(HttpServletRequest req, HttpServletResponse resp) {
		String fileName = null;
		String qrys = req.getQueryString();
		String[] arrQry = qrys.split(";");
		if (null != arrQry) {
			for (int i = 0; i < arrQry.length; i++) {
				String qry = arrQry[i];
				if (qry.contains("fileName")) {
					fileName = arrQry[i].substring((arrQry[i].indexOf("=")) + 1);
					break;
				}
			}
		}

		if (null != fileName && !session.exist(fileName)) {
			System.out.println("download : " + reportPath + "/" + fileName);
			File file = new File(reportPath + "/" + fileName);
			if (!file.exists()) {
				session.fileNotExist(fileName);
				return;
			}
			session.progress(fileName);
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
			boolean statusDownload = true;
			try {

				FileInputStream fis = new FileInputStream(file);
				OutputStream os = resp.getOutputStream();

				int bit = 256;
				int i = 0;

				while ((bit) >= 0) {
					bit = fis.read();
					os.write(bit);
				}
				os.flush();
				os.close();
				fis.close();

			} catch (IOException e) {
				session.failed(fileName);
				statusDownload = false;
			} catch (Exception e) {
				statusDownload = false;
				session.failed(fileName);
			}

			if (statusDownload) {
				session.success(fileName);
			}

		}
	}

	private void simpleDownloadReport(HttpServletRequest req, HttpServletResponse resp) {
		String fileName = req.getParameter("fileName");
		System.out.println("download : " + reportPath + "/" + fileName);

		if (null != fileName) {
			File file = new File(reportPath + "/" + fileName);

			if (file.exists()) {
				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
				try {

					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();

					int bit = 256;
					int i = 0;

					while ((bit) >= 0) {
						bit = fis.read();
						os.write(bit);
					}
					os.flush();
					os.close();
					fis.close();

				} catch (IOException e) {

				} catch (Exception e) {

				}
			}

		}
	}

	private void simpleDownloadReportReconcileEod(HttpServletRequest req, HttpServletResponse resp) {

		String fileName = req.getParameter("fileName");
		String transactionType = req.getParameter("transactionType");
		String bankName = req.getParameter("bankName").toLowerCase();

		String reconcileEodReportPath = (paramDao.findParamValueByParamName("reconcileEod.report.path"))+bankName + "/";

		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reconcileEodReportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		if (null != fileName && !fileName.contains("before")) {
			System.out.println("download : " + reconcileEodReportPath + "/" + fileName);
			File file = new File(reconcileEodReportPath + "/" + fileName.substring(17, 27) + "/" + fileName);

			if (file.exists()) {
				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
				try {

					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();

					int bit = 256;
					int i = 0;

					while ((bit) >= 0) {
						bit = fis.read();
						os.write(bit);
					}
					os.flush();
					os.close();
					fis.close();

				} catch (IOException e) {

				} catch (Exception e) {

				}
			}

		}
		if (null != fileName && fileName.contains("before")) {
			System.out.println("download : " + reconcileEodReportPath + "/" + fileName);
			// fileName.substring(17, 27)= Tanggal Generate Laporan
			File file = new File(reconcileEodReportPath + "/" + fileName.substring(17, 27) + "/" + fileName);

			if (file.exists()) {
				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
				try {

					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();

					int bit = 256;
					int i = 0;

					while ((bit) >= 0) {
						bit = fis.read();
						os.write(bit);
					}
					os.flush();
					os.close();
					fis.close();

				} catch (IOException e) {

				} catch (Exception e) {

				}
			}

		}
	}

	private void simpleDownloadReportReconcileWeekly(HttpServletRequest req, HttpServletResponse resp) {

		String fileName = req.getParameter("fileName");
		String transactionType = req.getParameter("transactionType");
		String bankName = req.getParameter("bankName").toLowerCase();

		String reconcileWeeklyReportPath = (paramDao.findParamValueByParamName("reconcileWeekly.report.path")+bankName + "/");

		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reconcileWeeklyReportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		if (null != fileName) {
			System.out.println("download : " + reconcileWeeklyReportPath + "/" + fileName);
			// fileName.substring(17, 39)= Range Laporan
			File file = new File(reconcileWeeklyReportPath + "/" + fileName.substring(17, 38) + "/" + fileName);

			if (file.exists()) {
				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
				try {

					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();

					int bit = 256;
					int i = 0;

					while ((bit) >= 0) {
						bit = fis.read();
						os.write(bit);
					}
					os.flush();
					os.close();
					fis.close();

				} catch (IOException e) {

				} catch (Exception e) {

				}
			}

		}
	}

	private void simpleDownloadReportReconcileMonthly(HttpServletRequest req, HttpServletResponse resp) {

		String fileName = req.getParameter("fileName");
		String transactionType = req.getParameter("transactionType");
		String bankName = req.getParameter("bankName").toLowerCase();

		String reconcileMonthlyReportPath = (paramDao.findParamValueByParamName("reconcileMonthly.report.path")+bankName + "/");

		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reconcileMonthlyReportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		if (null != fileName) {
			System.out.println("download : " + reconcileMonthlyReportPath + "/" + fileName);
			// fileName.substring(17, 39)= Range Laporan
			File file = new File(reconcileMonthlyReportPath + "/" + fileName.substring(17, 38) + "/" + fileName);

			if (file.exists()) {
				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
				try {

					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();

					int bit = 256;
					int i = 0;

					while ((bit) >= 0) {
						bit = fis.read();
						os.write(bit);
					}
					os.flush();
					os.close();
					fis.close();

				} catch (IOException e) {

				} catch (Exception e) {

				}
			}

		}
	}

	private void downloadReportParsingMT940(HttpServletRequest req, HttpServletResponse resp) {
		String fileName = req.getParameter("fileName");
		String transactionType = req.getParameter("transactionType");
		String bankName = req.getParameter("bankName").toLowerCase();

		String reportPath = paramDao.findParamValueByParamName("webadmin.transactionReport.download.path.parsing.mt940." + bankName);

		if (transactionType.equalsIgnoreCase(Constants.BillerConstants.BHP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.BHP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PERANGKAT_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PERANGKAT_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.SKOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.SKOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.REOR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.REOR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.UNAR_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.UNAR_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.IKRAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.IKRAP_DIR;
		} else if (transactionType.equalsIgnoreCase(Constants.BillerConstants.PAP_CODE)) {
			reportPath += Constants.DirectoryFTPConstants.PAP_DIR;
		}

		if (null != fileName) {
			System.out.println("download : " + reportPath + "/" + fileName);
			File file = new File(reportPath + "/" + fileName);

			if (file.exists()) {
				resp.setContentType("application/octet-stream");
				resp.setHeader("Content-disposition", "attachment;filename=" + fileName);
				try {

					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();

					int bit = 256;
					int i = 0;

					while ((bit) >= 0) {
						bit = fis.read();
						os.write(bit);
					}
					os.flush();
					os.close();
					fis.close();

				} catch (IOException e) {

				} catch (Exception e) {

				}
			}

		}
	}
}
