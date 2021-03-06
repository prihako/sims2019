package id.co.sigma.mx.project.ftpreconcile.process;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.FileTransactionReceive;
import id.co.sigma.mx.project.ftpreconcile.model.MT940;
import id.co.sigma.mx.project.ftpreconcile.model.Transaction;
import id.co.sigma.mx.project.ftpreconcile.util.MT940Parser;
import id.co.sigma.mx.project.ftpreconcile.util.MT940ParserBri;
import id.co.sigma.mx.project.ftpreconcile.util.PaymentUtil;
import id.co.sigma.mx.project.ftpreconcile.util.PaymentUtilBri;
import id.co.sigma.mx.project.ftpreconcile.util.SequenceUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ProcessorMessageBri {
	protected static transient Logger logger = Logger.getLogger(ProcessorMessageBri.class);

	private MT940ParserBri mt940Parser;
	
	private PaymentUtilBri paymentUtil;

	private SequenceUtil sequenceUtil;

	public void setMt940Parser(MT940ParserBri mt940Parser) {
		this.mt940Parser = mt940Parser;
	}

	public void setPaymentUtil(PaymentUtilBri paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}

    /**
     * used to parse file transaction, if valid then will process, else logging that file.
     *
     * @param fileTransac -> contains file from, file name, and content(byte)
     * @param isSpecialFolder -> true if from special folder, else false
     * @throws Exception
     */
    
    public synchronized void processTransaction(FileTransactionReceive fileTransac,boolean isSpecialFolder, String sameAccountNoFlag, 
    		String transactionCode, String bankName, String endpointCode) throws Exception {
    	
    	logger.info("bri, processTransaction, sameAccountNoFlag : " + sameAccountNoFlag);
    	logger.info("bri, processTransaction, bankName : " + bankName);
    	logger.info("bri, processTransaction, endpointCode : " + endpointCode);

    	String fileName=fileTransac.getFileNameTransaction();
        List<Transaction> listTransaction = new ArrayList<Transaction>();
        int totalSuccess=0;
        int totalFail=0;
        int totalSuccessHostToHost = 0;
        int totalSuccessNotHostToHost = 0;
        long seqTransactionFileId = 0;

        MT940 mt940=new MT940();
        mt940.setContentFile(new String(fileTransac.getContentFileTransaction()));
        
        String fileStatus = mt940Parser.getFileTransacStatus(mt940,fileName, sameAccountNoFlag, transactionCode, endpointCode);

        //return true if success parsing, false otherwise
        if (fileStatus.equals(PostelConstant.STATUS_VALID)) {

            //get transaction date from mt940 file
            fileTransac.setTransactionDate(mt940.getTransactionDate());

            //if not from special folder, then check processed file transaction, else by pass checking
            boolean isProcessed = false;
            if (!isSpecialFolder) {
                isProcessed = paymentUtil.isProcessedFileTransac(fileTransac.getTransactionDate(), fileName, bankName);
            }

            if (!isProcessed) { //not processed yet

                listTransaction = mt940.getRecordTransaction();

                //if true && list of model is not empty,
                if (!listTransaction.isEmpty()) {

                    logger.info("bri, File transaction (" + fileName + ") is parsed successfully.");

                    try {
                        seqTransactionFileId = paymentUtil.saveFileTransactionLog(fileTransac, PostelConstant.STATUS_VALID, bankName);

                        Transaction transaction = null;

                        for (Iterator<Transaction> i = listTransaction.iterator(); i.hasNext();) {

                            //get sequence of transaction
                            long seqTransactionId = sequenceUtil.generateTransactionId();

                            transaction = i.next();
                            transaction.setTransactionId(seqTransactionId);
                            transaction.setFileId(seqTransactionFileId);
                            transaction.setTransactionSrc(PostelConstant.FILE);

                            //update tgl 27 nov'06-uat request
                            //if (transaction.getRawTransactionMsg() != null && !transaction.getRawTransactionMsg().equalsIgnoreCase(""))
                            if (!transaction.isTransactionStatusFlag())
                            {
                                totalFail++;
                                transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
                            } else {
                                totalSuccess++;
                                if(transaction.getPaymentType() != null && transaction.getPaymentType().equalsIgnoreCase(PostelConstant.HOST_TO_HOST)){
                                	totalSuccessHostToHost++;
                                }else if(transaction.getPaymentType() != null && transaction.getPaymentType().equalsIgnoreCase(PostelConstant.NOT_HOST_TO_HOST)){
                                	totalSuccessNotHostToHost++;
                                }
                                transaction.setTransactionStatus(PostelConstant.STATUS_READY);
                            }

                            //insert record to transaction
                            paymentUtil.saveTransactions(transaction);
                        }
                        
                        paymentUtil.insertParserLog(seqTransactionFileId,totalFail,totalSuccess, totalSuccessHostToHost, totalSuccessNotHostToHost,
                        		listTransaction.size(),PostelConstant.STATUS_SUCCESS);
                    } catch (Exception e) {
                        logger.error("[bri, ProcessorMessage.processTransaction] Error process while inserting valid file transaction, ", e);
                        throw e;
                    }

                } else { //list of model is empty,
                    logger.info("bri, File transaction (" + fileName + ") is parsed successfully but no found transaction in file.");

                    try {
                    	seqTransactionFileId = paymentUtil.saveFileTransactionLog(fileTransac, PostelConstant.STATUS_VALID, bankName);
                    	paymentUtil.insertParserLog(seqTransactionFileId,totalFail,totalSuccess, totalSuccessHostToHost, totalSuccessNotHostToHost,listTransaction.size(),PostelConstant.STATUS_FAIL);
                    } catch (Exception e) {
                        logger.error("[bri, ProcessorMessage.processTransaction] Error process while inserting empty file transaction, ", e);
                        throw e;
                    }
                }
            } else {
                logger.info("bri, This file ( "+  fileName  +" ) has been processed, will be received only.");

                try {
                	paymentUtil.saveFileTransactionLog(fileTransac, PostelConstant.STATUS_IGNORED, bankName);
                } catch (Exception e) {
                    logger.error("[bri, ProcessorMessage.process] Error process while inserting ignored file transaction, ", e);
                    throw e;
                }
            }
        } else {
            logger.info("bri, Invalid format in file transaction [ " + fileName + " ]");

            //get transaction date from mt940 file
            fileTransac.setTransactionDate(mt940.getTransactionDate());

            try {
                seqTransactionFileId = paymentUtil.saveFileTransactionLog(fileTransac, fileStatus, bankName);
                paymentUtil.insertParserLog(seqTransactionFileId,totalFail,totalSuccess, totalSuccessHostToHost, totalSuccessNotHostToHost,listTransaction.size(),PostelConstant.STATUS_FAIL);
            } catch (Exception e) {
                logger.error("[bri, ProcessorMessage.processTransaction] Error process while inserting invalid file transaction, ", e);
                throw e;
            }
        }

        logger.info("bri, PROSESS FILE TRANSACTION IS COMPLETE.");

    }
}
