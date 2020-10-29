package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.FileTransactionReceive;
import id.co.sigma.mx.project.ftpreconcile.model.ListenerLog;
import id.co.sigma.mx.project.ftpreconcile.util.FileFormatUtil;
import id.co.sigma.mx.project.ftpreconcile.util.PaymentUtil;
import id.co.sigma.mx.project.ftpreconcile.util.SequenceUtil;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

public class ReceiverFTPMandiriSyariah {

	protected static transient Logger logger = Logger
			.getLogger(ReceiverFTPMandiriSyariah.class);

	private FTPManager ftpManager;

	public void setFtpManager(FTPManager ftpManager) {
		this.ftpManager = ftpManager;
	}

	private PaymentUtil paymentUtil;

	public void setPaymentUtil(PaymentUtil paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	private ProcessorMessage processorMessage;

	public void setProcessorMessage(ProcessorMessage processorMessage) {
		this.processorMessage = processorMessage;
	}

	private SequenceUtil sequenceUtil;

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}

	public void execute(int type, String sameAccountNoFlag, String filePattern, String transactionCode, String endpointCode, String bankName) {
		logger.info("mandiri, ReceiverFTP.execute() method, filePattern = " + filePattern);
		logger.info("mandiri, ReceiverFTP.execute() method, type = " + type);
		logger.info("mandiri, ReceiverFTP.execute() method, skorReorFlag = " + sameAccountNoFlag);
		logger.info("mandiri, ReceiverFTP.execute() method, bankName = " + bankName);
		logger.info("mandiri, ReceiverFTP.execute() method, endpointCode = " + endpointCode);

		try {
			String ftpLocalDir = ftpManager.getFtpLocalAddress();
			String ftpServerDir = ftpManager.getFtpServerDirAddress();
			String filename = FileFormatUtil.getFileName(-1, filePattern);
			
			logger.info("mandiri, ReceiverFTP filename : " + filename);

			Calendar transactionDate = FileFormatUtil.getTrasactionDate(
					filename, filePattern);

			File directoryFile = new File(ftpLocalDir);
			if (!directoryFile.exists())
				directoryFile.mkdir();

			// diubah sama hako
			boolean isProcessed = paymentUtil.isProcessedFileTransac(
					transactionDate, filename);

			if (isProcessed) {

				logger.info("--------------------------------------------------");
				logger.info("mandiri, File " + filename + " has been processed.");
				logger.info("--------------------------------------------------");

			} else {
				List<String> listFileTransac = new ArrayList<String>();
				listFileTransac.add(filename);

				searchPrevFileTransaction(filename, listFileTransac, filePattern);

				String file;
				for (int i = listFileTransac.size() - 1; i >= 0; i--) {
					file = listFileTransac.get(i);

					if (ftpManager.isEnableToAccess()) {
						logger.info("---------------------------------------------------------------");
						logger.info("Request file " + file
								+ " to download on Ftp server MANDIRI.");
						logger.info("---------------------------------------------------------------");

						if (isFileFound(file)) {
							logger.info("mandiri, Found " + file
									+ " file transaction in ftp server.");
							ftpManager.retrieve(ftpLocalDir + file, file, ftpServerDir);
							processingFileContent(file, sameAccountNoFlag, transactionCode, bankName, endpointCode);

						} else {
							logger.info("mandiri, No found " + file
									+ " file transaction in ftp server.");

							insertListenerLog(
									PostelConstant.STATUS_NOT_AVAILABLE,
									PostelConstant.FTP,
									FileFormatUtil.getTrasactionDate(file, filePattern),
									file, bankName);
						}
					} else {
						insertListenerLog(PostelConstant.STATUS_FTP_DOWN,
								PostelConstant.FTP,
								FileFormatUtil.getTrasactionDate(file, filePattern),
								filename, bankName);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		}

	}

	private void processingFileContent(String fileName, String sameAccountNoFlag, String transactionCode, 
			String bankName, String endpointCode) throws Exception {
		
		logger.info("mandiri, processingFileContent, sameAccountNoFlag : " + sameAccountNoFlag);
		logger.info("mandiri, processingFileContent, bankName : " + bankName);
		
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

	private boolean isFileFound(String file) throws Exception {
		boolean result = false;
		String[] filesNames = ftpManager.listFilesNames(ftpManager
				.getFtpServerDirAddress());
		
		String fileName = null;
		if (filesNames != null) {
			for (int i = 0; i < filesNames.length; i++) {
				fileName = filesNames[i];
				if (fileName.equals(file)) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}

	private void searchPrevFileTransaction(String fileName,
			List<String> listFileTransac) throws SQLException,
			ClassNotFoundException {

		String prevFileName = FileFormatUtil.getPrevFilename(fileName);

		// diubah sama hako
		boolean isProcessed = paymentUtil.isProcessedFileTransac(
				FileFormatUtil.getTrasactionDate(prevFileName), prevFileName);

		if (!isProcessed) {
			if (!paymentUtil.isEmptyTableFileTransac()) {
				listFileTransac.add(prevFileName);
				searchPrevFileTransaction(prevFileName, listFileTransac);
			} else {
				logger.info("Table file log is empty, so only retrieve current date file transaction");
			}
		}
	}
	
	private void searchPrevFileTransaction(String fileName,
			List<String> listFileTransac, String filePattern) throws SQLException,
			ClassNotFoundException {
		
		String prevFileName = FileFormatUtil.getPrevFilename(fileName, filePattern);

		// diubah sama hako
		boolean isProcessed = paymentUtil.isProcessedFileTransac(
				FileFormatUtil.getTrasactionDate(prevFileName, filePattern), prevFileName);

		if (!isProcessed) {
			if (!paymentUtil.isEmptyTableFileTransac()) {
				listFileTransac.add(prevFileName);
				searchPrevFileTransaction(prevFileName, listFileTransac, filePattern);
			} else {
				logger.info("Table file log is empty, so only retrieve current date file transaction");
			}
		}
	}
}
