package id.co.sigma.mx.project.ftpreconcile.process;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.FileTransactionReceive;
import id.co.sigma.mx.project.ftpreconcile.model.ListenerLog;
import id.co.sigma.mx.project.ftpreconcile.util.FileFormatUtilBni;
import id.co.sigma.mx.project.ftpreconcile.util.PaymentUtilBniSyariah;
import id.co.sigma.mx.project.ftpreconcile.util.SequenceUtil;

public class ReceiverFTPBniSyariah {

	protected static transient Logger logger = Logger
			.getLogger(ReceiverFTPBniSyariah.class);

	private FTPManagerBniSyariah ftpManager;

	public void setFtpManager(FTPManagerBniSyariah ftpManager) {
		this.ftpManager = ftpManager;
	}

	private PaymentUtilBniSyariah paymentUtil;

	public void setPaymentUtil(PaymentUtilBniSyariah paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	private ProcessorMessageBniSyariah processorMessage;

	public void setProcessorMessage(ProcessorMessageBniSyariah processorMessage) {
		this.processorMessage = processorMessage;
	}

	private SequenceUtil sequenceUtil;

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}

	public void execute(int type, String sameAccountNoFlag, String filePattern, String transactionCode, String endpointCode, String bankName) {
		logger.info("bni, ReceiverFTP.execute() method, filePattern = " + filePattern);
		logger.info("bni, ReceiverFTP.execute() method, type = " + type);
		logger.info("bni, ReceiverFTP.execute() method, skorReorFlag = " + sameAccountNoFlag);
		logger.info("bni, ReceiverFTP.execute() method, transactionCode = " + transactionCode);
		logger.info("bni, ReceiverFTP.execute() method, bankName = " + bankName);
		logger.info("bni, ReceiverFTP.execute() method, endpointCode = " + endpointCode);

		try {
			String ftpLocalDir = ftpManager.getFtpLocalAddress();
			String ftpServerDir = ftpManager.getFtpServerDirAddress();
			String fileDatePlusAccountNo = FileFormatUtilBni.getFileNameDatePlusAccountNo(0, filePattern);
			
			logger.info("bni, fileDatePlusAccountNo : " + fileDatePlusAccountNo);

			Calendar transactionDate = FileFormatUtilBni.getTrasactionDate(fileDatePlusAccountNo, FileFormatUtilBni.getDatePattern(filePattern), -1); 

			logger.info("bni, transactionDate : " + transactionDate.getTime());
			
			File directoryFile = new File(ftpLocalDir);
			if (!directoryFile.exists())
				directoryFile.mkdir();

			boolean isProcessed = paymentUtil.isProcessedFileTransac(
					transactionDate, fileDatePlusAccountNo, bankName);

			if (isProcessed) {

				logger.info("-----------------bni,-------------------------");
				logger.info("bni, File with header + no rekening " + fileDatePlusAccountNo + " has been processed.");
				logger.info("-----------------bni,-------------------------");

			} else {
				List<String> listFileTransac = new ArrayList<String>();
				listFileTransac.add(fileDatePlusAccountNo);

				searchPrevFileTransaction(fileDatePlusAccountNo, listFileTransac, filePattern, bankName);

				String file;
				for (int i = listFileTransac.size() - 1; i >= 0; i--) {
					file = listFileTransac.get(i);
					String [] filenameAndAccountNo = file.split("\\+");

					if (ftpManager.isEnableToAccess()) {
						logger.info("------------------------bni,-----------------------------------");
						logger.info("bni, Request file with header + no rekening " + file
								+ " to download on Ftp server BNI.");
						logger.info("------------------------bni,-----------------------------------");

						if (isFileFound(filenameAndAccountNo)) {
							String fileNameFtp = getFileNameFromFtp(filenameAndAccountNo);
							logger.info("bni,Found file with filename " + fileNameFtp
									+ " file transaction in ftp server.");
							ftpManager.retrieve(ftpLocalDir + fileNameFtp, fileNameFtp,
									ftpServerDir);
							processingFileContent(file, fileNameFtp, sameAccountNoFlag, transactionCode, 
									bankName, endpointCode);

						} else {
							logger.info("bni, No found " + file
									+ " file transaction in ftp server.");

							insertListenerLog(
									PostelConstant.STATUS_NOT_AVAILABLE,
									PostelConstant.FTP,
									FileFormatUtilBni.getTrasactionDate(file, FileFormatUtilBni.getDatePattern(filePattern), -1),
									file, bankName);
						}
					} else {
						insertListenerLog(PostelConstant.STATUS_FTP_DOWN,
								PostelConstant.FTP,
								FileFormatUtilBni.getTrasactionDate(file, FileFormatUtilBni.getDatePattern(filePattern), -1),
								fileDatePlusAccountNo, bankName);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		}

	}

	private void processingFileContent(String fileDatePlusAccountNo, String fileName, String sameAccountNoFlag,
			String transactionCode, String bankName, String endpointCode) throws Exception {
		
		logger.info("bni, processingFileContent, sameAccountNoFlag : " + sameAccountNoFlag);
		logger.info("bni, processingFileContent, endpointCode : " + endpointCode);
		
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
		
		processorMessage.processTransaction(ftr, false, sameAccountNoFlag, transactionCode, 
				bankName, endpointCode);
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

	private boolean isFileFound(String [] filenameAndAccountNo) throws Exception {
		logger.info("bni, server Directory " + ftpManager.getFtpServerDirAddress());
		boolean result = false;
		String[] filesNames = ftpManager.listFilesNames(ftpManager
				.getFtpServerDirAddress());
		
		String fileName = null;
		if (filesNames != null) {
			for (int i = 0; i < filesNames.length; i++) {
				
				fileName = filesNames[i];
				if (fileName.startsWith(filenameAndAccountNo[0]) && fileName.contains(filenameAndAccountNo[1])) {
					result = true;
					break;
				}
			}
		}
		
		return result;
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
	
	private void searchPrevFileTransaction(String filenameDatePlusAccountNo,
			List<String> listFileTransac, String filePattern, String bankName) throws SQLException,
			ClassNotFoundException {
		
		logger.info("bni, searchPrevFileTransaction filenameDatePlusAccountNo : " + filenameDatePlusAccountNo);
		
		String prevFileNameDatePlusAccountNo = FileFormatUtilBni.getPrevFilenameDate(filenameDatePlusAccountNo, FileFormatUtilBni.getDatePattern(filePattern));

		logger.info("bni, searchPrevFileTransaction prevFileNameDatePlusAccountNo : " + prevFileNameDatePlusAccountNo);
		
		boolean isProcessed = paymentUtil.isProcessedFileTransac(
				FileFormatUtilBni.getTrasactionDate(prevFileNameDatePlusAccountNo,  FileFormatUtilBni.getDatePattern(filePattern), -1),prevFileNameDatePlusAccountNo, bankName);

		if (!isProcessed) {
			if (!paymentUtil.isEmptyTableFileTransac()) {
				listFileTransac.add(prevFileNameDatePlusAccountNo);
				searchPrevFileTransaction(prevFileNameDatePlusAccountNo, listFileTransac, filePattern, bankName);
			} else {
				logger.info("bni, Table file log is empty, so only retrieve current date file transaction");
			}
		}
	}
}
