package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.FileTransactionReceive;
import id.co.sigma.mx.project.ftpreconcile.model.ListenerLog;
import id.co.sigma.mx.project.ftpreconcile.util.FileFormatUtilBni;
import id.co.sigma.mx.project.ftpreconcile.util.FileFormatUtilBri;
import id.co.sigma.mx.project.ftpreconcile.util.PaymentUtilBri;
import id.co.sigma.mx.project.ftpreconcile.util.SequenceUtil;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

public class ReceiverFTPBri {

	protected static transient Logger logger = Logger
			.getLogger(ReceiverFTPBri.class);

	private FTPManagerBri ftpManager;

	private PaymentUtilBri paymentUtil;

	private ProcessorMessageBri processorMessage;

	private SequenceUtil sequenceUtil;
	
	public void setFtpManager(FTPManagerBri ftpManager) {
		this.ftpManager = ftpManager;
	}
	
	public void setPaymentUtil(PaymentUtilBri paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	public void setProcessorMessage(ProcessorMessageBri processorMessage) {
		this.processorMessage = processorMessage;
	}

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}

	public void execute(int type, String sameAccountNoFlag, String filePattern, String transactionCode, String endpointCode, String bankName) {
		logger.info("bri, ReceiverFTP.execute() method, filePattern = " + filePattern);
		logger.info("bri, ReceiverFTP.execute() method, type = " + type);
		logger.info("bri, ReceiverFTP.execute() method, skorReorFlag = " + sameAccountNoFlag);
		logger.info("bri, ReceiverFTP.execute() method, bankName = " + bankName);
		logger.info("bri, ReceiverFTP.execute() method, endpointCode = " + endpointCode);

		try {
			String ftpLocalDir = ftpManager.getFtpLocalAddress();
			String ftpServerDir = ftpManager.getFtpServerDirAddress();
			String accountNoPlusDate = FileFormatUtilBri.getFileName(-1, filePattern);
			
			logger.info("bri, ReceiverFTP accountNoPlusDate : " + accountNoPlusDate);

			Calendar transactionDate = FileFormatUtilBri.getTrasactionDate(
					accountNoPlusDate, FileFormatUtilBri.getDatePattern(filePattern));

			logger.info("bri, ReceiverFTP transactionDate : " + transactionDate.getTime());
			
			File directoryFile = new File(ftpLocalDir);
			if (!directoryFile.exists())
				directoryFile.mkdir();
			
			boolean isProcessed = paymentUtil.isProcessedFileTransac(
					transactionDate, accountNoPlusDate, bankName);

			if (isProcessed) {

				logger.info("--------------------------------------------------");
				logger.info("bri, File " + accountNoPlusDate + " has been processed.");
				logger.info("--------------------------------------------------");

			} else {
				List<String> listFileTransac = new ArrayList<String>();
				listFileTransac.add(accountNoPlusDate);

				searchPrevFileTransaction(accountNoPlusDate, listFileTransac, filePattern, bankName);

				String file;
				for (int i = listFileTransac.size() - 1; i >= 0; i--) {
					file = listFileTransac.get(i);
					String [] accountNoAndDate = file.split("\\+");

					if (ftpManager.isEnableToAccess()) {
						logger.info("---------------------------------------------------------------");
						logger.info("bri, Request file which file name start " + file
								+ " to download on Ftp server BRI.");
						logger.info("---------------------------------------------------------------");

						if (isFileFound(accountNoAndDate)) {
							String fileNameFtp = getFileNameFromFtp(accountNoAndDate);
							logger.info("bri, Found " + fileNameFtp
									+ " file transaction in ftp server.");
							logger.info("bri, ftpLocalDir+fileNameFtp : " + ftpLocalDir+fileNameFtp);
							logger.info("bri, ftpServerDir+fileNameFtp : " + ftpServerDir+fileNameFtp);
							ftpManager.retrieve(ftpLocalDir+fileNameFtp, fileNameFtp, ftpServerDir);
							processingFileContent(file, fileNameFtp,  sameAccountNoFlag, transactionCode, bankName, endpointCode);

						} else {
							logger.info("bri, No found " + file
									+ " file transaction in ftp server.");

							insertListenerLog(
									PostelConstant.STATUS_NOT_AVAILABLE,
									PostelConstant.FTP,
									FileFormatUtilBri.getTrasactionDate(file, FileFormatUtilBri.getDatePattern(filePattern)),
									file, bankName);
						}
					} else {
						insertListenerLog(PostelConstant.STATUS_FTP_DOWN,
								PostelConstant.FTP,
								FileFormatUtilBri.getTrasactionDate(file, filePattern),
								accountNoPlusDate, bankName);
					}
				}
			}

		} catch (Exception e) {
			logger.error("ERROR : ", e);
		}

	}

	private void processingFileContent(String fileDatePlusAccountNo, String fileName, String sameAccountNoFlag, String transactionCode, 
			String bankName, String endpointCode) throws Exception {
		
		logger.info("bri, processingFileContent, sameAccountNoFlag : " + sameAccountNoFlag);
		logger.info("bri, processingFileContent, bankName : " + bankName);
			
		java.io.FileInputStream fis = new java.io.FileInputStream(
				new java.io.File(ftpManager.getFtpLocalAddress() + fileName));
		byte[] content = new byte[fis.available()];
		fis.read(content);
		fis.close();

		FileTransactionReceive ftr = new FileTransactionReceive();
		ftr.setContentFileTransaction(content);
		ftr.setFileNameTransaction(fileName);
		ftr.setUserName("");
		ftr.setKoreksiFlag("N");
		ftr.setTypeListener(PostelConstant.FTP);
		ftr.setFileDatePlusAccountNo(fileDatePlusAccountNo);
		
		processorMessage.processTransaction(ftr, false, sameAccountNoFlag, transactionCode, bankName, endpointCode);
	}

	private void insertListenerLog(String status, String source,
			Calendar transDate, String filenameTransaction, String bankName)
			throws SQLException, ClassNotFoundException {
		long seqListenerLogId = sequenceUtil.generateListenerLogId();

		ListenerLog listenerLog = new ListenerLog();
		listenerLog.setListenerLogId(seqListenerLogId);
		listenerLog.setListenerStatus(status);
		listenerLog.setSourceType(source);
		listenerLog.setTransactionDate(new Timestamp(transDate
				.getTimeInMillis()));
		listenerLog.setFilenameTransaction(filenameTransaction);
		listenerLog.setBankName(bankName);
		paymentUtil.saveListenerLog(listenerLog);
	}

	private boolean isFileFound(String [] accountNoAndDate) throws Exception {
		logger.info("bri, server Directory " + ftpManager.getFtpServerDirAddress());
		boolean result = false;
		String[] filesNames = ftpManager.listFilesNames(ftpManager
				.getFtpServerDirAddress());
		
		String fileName = null;
		if (filesNames != null) {
			for (int i = 0; i < filesNames.length; i++) {
				fileName = filesNames[i];
				if (fileName.startsWith(accountNoAndDate[0]) && fileName.contains(accountNoAndDate[1])) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	private void searchPrevFileTransaction(String fileName,
			List<String> listFileTransac, String filePattern, String bankName) throws SQLException,
			ClassNotFoundException {
		
		String prevFileNameAccountNoPlusDate = FileFormatUtilBri.getPrevFilenameDate(fileName, FileFormatUtilBri.getDatePattern(filePattern));

		logger.info("bri, searchPrevFileTransaction prevFileNameAccountNoPlusDate : " + prevFileNameAccountNoPlusDate);
		
		boolean isProcessed = paymentUtil.isProcessedFileTransac(
				FileFormatUtilBri.getTrasactionDate(prevFileNameAccountNoPlusDate, FileFormatUtilBni.getDatePattern(filePattern)), prevFileNameAccountNoPlusDate, bankName);

		if (!isProcessed) {
			if (!paymentUtil.isEmptyTableFileTransac()) {
				listFileTransac.add(prevFileNameAccountNoPlusDate);
				searchPrevFileTransaction(prevFileNameAccountNoPlusDate, listFileTransac, filePattern, bankName);
			} else {
				logger.info("bri, Table file log is empty, so only retrieve current date file transaction");
			}
		}
	}
	
	private String getFileNameFromFtp(String [] filenameAndAccountNo) throws Exception {
		logger.info("bni, server Directory " + ftpManager.getFtpServerDirAddress());
		String result = null;;
		String[] filesNames = ftpManager.listFilesNames(ftpManager
				.getFtpServerDirAddress());
		
		String fileName = null;
		if (filesNames != null) {
			for (int i = 0; i < filesNames.length; i++) {
				
				fileName = filesNames[i];
				if (fileName.startsWith(filenameAndAccountNo[0]) && fileName.contains(filenameAndAccountNo[1])) {
					result = fileName;
					break;
				}
			}
		}
		
		return result;
	}
	
}
