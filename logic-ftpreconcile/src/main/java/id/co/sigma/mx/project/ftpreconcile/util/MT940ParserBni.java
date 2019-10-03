package id.co.sigma.mx.project.ftpreconcile.util;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.MT940;
import id.co.sigma.mx.project.ftpreconcile.model.Transaction;
import id.co.sigma.mx.project.ftpreconcile.process.FTPManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ferdi
 * Date: Aug 22, 2006
 * Time: 11:42:29 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * this is MTI950 parser class, handle to parse MTI940 format transaction.
 */
public class MT940ParserBni {

    private static Logger logger = Logger.getLogger(MT940ParserBni.class);

    private SequenceUtil sequenceUtil;
    
    private PaymentUtilBni paymentUtil;
    
    private FTPManager ftpManager;
    
    public void setPaymentUtil(PaymentUtilBni paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
	}
	
	public void setFtpManager(FTPManager ftpManager) {
		this.ftpManager = ftpManager;
	}


    /**
     * used to parse record transaction and put to list of bean (Transaction)
     *
     * @param recordTransac -> record2 transaction MI940
     * @param mt940
     * @return flag -> true if valid, else false
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public boolean parseRecordToBean(List<String> recordTransac, MT940 mt940) throws SQLException, ClassNotFoundException {
    	logger.info("###Enter parseRecordToBean function###");
    	
        List<Transaction> listOfBean = new ArrayList<Transaction>(recordTransac.size());

        if (!recordTransac.isEmpty()) {
            String record = null;
            Transaction transaction = null;

            int midIdx;
            String firstCode = null;
            String secCode = null;
            boolean flag;
            boolean isValidFile=true;
            for (Iterator<String> i = recordTransac.iterator(); i.hasNext() && isValidFile ;) {

                record = i.next();

                transaction = new Transaction();

                /* set tambahan */
                try {
					transaction.setTransactionId(sequenceUtil.generateTransactionId());
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error(e);
					throw e;
				}

				transaction.setTransactionSrc(PostelConstant.FILE);

				if (!transaction.isTransactionStatusFlag())
                {
                    transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
                } else {
                    transaction.setTransactionStatus(PostelConstant.STATUS_READY);
                }

				transaction.setNeedReconcile("Y");

				/* set tambahan */

                midIdx = record.indexOf(":86:");

                firstCode = record.substring(4, midIdx).trim();
                secCode = record.substring(midIdx + 4).trim();

                //code 61 & 86 show all in raw_transaction_msg (update 27 nov'06)- for web request
                transaction.setRawTransactionMsg(firstCode + " | " + secCode);
                
                if(secCode.length() > 6){
	                String channelId = secCode.substring(secCode.indexOf("UBP") + 3,  secCode.indexOf("UBP") + 7);
	                String paymentChannel = paymentUtil.getChannelPayment(channelId);
	                if(paymentChannel != null){
	                	transaction.setPaymentChannel(paymentChannel);
	                }else{
	                	transaction.setPaymentChannel("Unknown : " + channelId);
	                }
                }else{
                	transaction.setPaymentChannel("unknown");
                }

                Calendar mt940Date=mt940.getTransactionDate();
                                
                flag=parseFirstRec(transaction, firstCode, mt940Date);

                //if parse true && not empty code 86, then continue to parse code 86
                if (flag) {
                    flag=parseSecondRec(transaction, secCode);
                }
                transaction.setTransactionStatusFlag(flag);
                transaction.setRawTransactionMsg(firstCode + " | " + secCode);

                listOfBean.add(transaction);
            }

            if (isValidFile) mt940.setRecordTransaction(listOfBean);
            return isValidFile;
        } else {
            mt940.setRecordTransaction(listOfBean);
            return true; //valid but empty transaction
        }
    }
    
    public boolean parseRecordToBean(List<String> recordTransac, MT940 mt940, 
    		String sameAccountNoFlag, String transactionCodeParam, String endpointCode) 
    				throws SQLException, ClassNotFoundException {
    	logger.info("bni, Enter parseRecordToBean, sameAccountFlag : " + sameAccountNoFlag);
    	
        List<Transaction> listOfBean = new ArrayList<Transaction>(recordTransac.size());

        if (!recordTransac.isEmpty()) {
            String record = null;
            Transaction transaction = null;

            int midIdx;
            String firstCode = null;
            String secCode = null;
            boolean flag;
            boolean isValidFile=true;
            
            for (Iterator<String> i = recordTransac.iterator(); i.hasNext() && isValidFile ;) {

                record = i.next();

                transaction = new Transaction();

                /* set tambahan */
                try {
					transaction.setTransactionId(sequenceUtil.generateTransactionId());
				} catch (SQLException e) {
					logger.error("error " , e);
					throw e;
				}

				transaction.setTransactionSrc(PostelConstant.FILE);

				if (!transaction.isTransactionStatusFlag())
                {
                    transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
                } else {
                    transaction.setTransactionStatus(PostelConstant.STATUS_READY);
                }

				transaction.setNeedReconcile("Y");
				 
                midIdx = record.indexOf(":86:");
                
                try{
	                firstCode = record.substring(4, midIdx).trim();
	                secCode = record.substring(midIdx + 4).trim();
	
	                //code 61 & 86 show all in raw_transaction_msg (update 27 nov'06)- for web request
	                transaction.setRawTransactionMsg(firstCode + " | " + secCode);
	
	                Calendar mt940Date=mt940.getTransactionDate();
	                                
	                flag=parseFirstRec(transaction, firstCode, mt940Date);
	
	                //if parse true && not empty code 86, then continue to parse code 86, and if same account no check transaction code
	                if (flag) {
	                	flag=parseSecondRec(transaction, secCode, endpointCode);
	                }
	                
	                transaction.setTransactionStatusFlag(flag);
	
	                transaction.setRawTransactionMsg(firstCode + " | " + secCode);
	
	                listOfBean.add(transaction);
                }catch(Exception e){
                	transaction.setRawTransactionMsg(firstCode + " | " + secCode);
                	transaction.setTransactionStatusFlag(false);
                    transaction.setErrorDesc("Error Parsing tag 86");
                    transaction.setTransactionDate(new Timestamp(0));
                	transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
                	listOfBean.add(transaction);
                	logger.error("Error Parsing tag 86  : " + record , e);
                }
            }
           
            if (isValidFile) mt940.setRecordTransaction(listOfBean);
            return isValidFile;
        } else {
            mt940.setRecordTransaction(listOfBean);
            return true; //valid but empty transaction
        }
    }

    public boolean checkTransactionCode(Transaction transaction, String strCode, String transactionCodeParam){
    	String transactionCode = null;
    	int transactionCodeLength = transactionCodeParam.length();
		String accountNo = FileFormatUtilBni.getFilePattern();
		accountNo = accountNo.substring(0, accountNo.indexOf("MT940"));
		
    	if(strCode.length() > transactionCodeLength){
            transactionCode = strCode.substring(strCode.length() - transactionCodeLength, strCode.length());
            strCode = strCode.substring(0, strCode.length() - transactionCodeLength);    
    	}
		
		if(paymentUtil.getTransactionCode(accountNo, transactionCode)){
        	transaction.setTransactionCode(transactionCode);
        	return true;
        }else{
        	transaction.setTransactionCode(transactionCode);
        	transaction.setTransactionStatusFlag(false);
        	transaction.setErrorDesc(PostelConstant.UNKNOWN_TRANSACTION_CODE + ", code : " + transactionCode);
        	return false;
        }
		
    }

    /**
     * used to parse code 61 to bean, ex: 151215CR2151908,NTRF//
     * @param transaction -> model table
     * @param strFirst -> string of code 61
     * @return flag -> true if valid parse, else false
     */
    private static boolean parseFirstRec(Transaction transaction, String strFirst, Calendar mt940Date) {
        
        if(strFirst.length() > 11) {
        	String transacDateStr = strFirst.substring(0, 6);
        	try {
                java.util.Date trxDate = DateUtil.convertStringToDate(transacDateStr,DateUtil.YYMMDD_DatePattern,false);
                Calendar cal = Calendar.getInstance();
                cal.setTime(trxDate);
                transaction.setTransactionDateCal(cal);
                transaction.setTransactionDate(new Timestamp(cal.getTimeInMillis()));
            } catch (ParseException e) {
            	logger.error("bni, [MT940Parser.parseFirstRec] error parse, " , e);
                transaction.setErrorDesc("bni, Error parsing tanggal transaksi.");
                transaction.setTransactionDate(new Timestamp(0));
                e.printStackTrace();
                return false;
            }
            
        	//Untuk cek tanggal transaksi, skrng dinonaktifkan karena mungkin transaksi kemaren juga masuk
//            Calendar transacDate=transaction.getTransactionDateCal();
//            
//            if ( transacDate.compareTo(mt940Date) != 0 ) {
//            	transacDate.add(Calendar.DAY_OF_YEAR, 1);
//            	if(transacDate.compareTo(mt940Date) != 0) {
//            		logger.error("bni, [MT940Parser.parseFirstRec] error parse transaction date");
//                    transaction.setErrorDesc("bni, Error tanggal transaksi tidak valid.");
//                    return false;
//            	}
//            }

            String transacType = strFirst.substring(6, 7);
            if (PostelConstant.CREDIT.equalsIgnoreCase(transacType) || PostelConstant.DEBIT.equalsIgnoreCase(transacType)) {
                transaction.setTransactionType(transacType);
            }else{
            	transaction.setErrorDesc("Transaction type harus Credit(C) or Debit (D)");
                return false;
            }
            
            transaction.setTransactionType(transacType);

            String amount = strFirst.substring(7, strFirst.length() - 6);
            
            try {  
            	BigDecimal amountTransac=new BigDecimal(amount);
                transaction.setTransactionAmount(amountTransac);
            }  
            catch(NumberFormatException e) {  
            	logger.error("bni, [MT940Parser.parseFirstRec] error parse, amount=" + amount);
                transaction.setErrorDesc("bni, Error parsing jumlah transaksi.");
                return false;  
            }  

            String typeMsg = strFirst.substring(strFirst.length() - 6, strFirst.length() - 2);
            transaction.setMessageType(typeMsg);
        } else{
        	logger.error("bni, [MT940Parser.parseFirstRec] Information from tag 61 is not valid");
            transaction.setErrorDesc("bni, Informasi tag 61 tidak lengkap.");
            return false; 
        }
        
        return true;
    }
    
    private static boolean parseFirstRec(Transaction transaction, String strFirst, Calendar mt940Date, String sameAccountNoFlag, String transactionCodeParam) {
    	
        if(strFirst.length() > 11) {
        	String transacDateStr = strFirst.substring(0, 6);
        	try {
                java.util.Date trxDate = DateUtil.convertStringToDate(transacDateStr,DateUtil.YYMMDD_DatePattern,false);
                Calendar cal = Calendar.getInstance();
                cal.setTime(trxDate);
                transaction.setTransactionDateCal(cal);
                transaction.setTransactionDate(new Timestamp(cal.getTimeInMillis()));
            } catch (ParseException e) {
            	logger.error("[MT940Parser.parseFirstRec] error parse, " , e);
                transaction.setErrorDesc("Error parsing tanggal transaksi.");
                transaction.setTransactionDate(new Timestamp(0));
                e.printStackTrace();
                return false;
            }
            
            Calendar transacDate=transaction.getTransactionDateCal();
            
            if ( transacDate.compareTo(mt940Date) != 0 ) {
            	transacDate.add(Calendar.DAY_OF_YEAR, 1);
            	if(transacDate.compareTo(mt940Date) != 0) {
            		logger.error("[MT940Parser.parseFirstRec] error parse transaction date");
                    transaction.setErrorDesc("Error tanggal transaksi tidak valid.");
                    return false;
            	}
            }

            String transacType = strFirst.substring(6, 7);
            if (transacType==null || !PostelConstant.CREDIT.equalsIgnoreCase(transacType)) {
                transaction.setErrorDesc("Transaction type harus Credit(C)");
                return false;
            }
            transaction.setTransactionType(transacType);

            String amount = strFirst.substring(7, strFirst.length() - 4);
            
            try {  
            	BigDecimal amountTransac=new BigDecimal(amount);
                transaction.setTransactionAmount(amountTransac);
            }  
            catch(NumberFormatException e) {  
            	logger.error("[MT940Parser.parseFirstRec] error parse, amount=" + amount);
                transaction.setErrorDesc("Error parsing jumlah transaksi.");
                return false;  
            }  

            String typeMsg = strFirst.substring(strFirst.length() - 4);
            transaction.setMessageType(typeMsg);
        } else{
        	logger.error("[MT940Parser.parseFirstRec] Information from tag 61 is not valid");
            transaction.setErrorDesc("Informasi tag 61 tidak lengkap.");
            return false; 
        }
        
        return true;
    }
    
    /**
     * used to parse code 86 to bean, ex: SETOR TUNAI|547694|SDPPI KEMENKOMINFO 042281310|14:32:21
     * @param transaction -> model table
     * @param strSecond -> string of code 86
     * @return flag -> true if valid parse, else false
     */
    private static boolean parseSecondRec(Transaction transaction, String strSecond){
    	//split string by |
    	String [] strSeconds = strSecond.split("\\|");
    	if(strSeconds.length > 3){
    		//split string by space
    		String [] strDescriptions = strSeconds[2].split("\\s+");
    		if(strDescriptions.length > 2){
    			transaction.setInvoiceId(strDescriptions[2].substring(0,7));
        		transaction.setClientId(strDescriptions[2].substring(9));
        		return true;
    		}else{
    			transaction.setErrorDesc("format narasi pada code 86 salah");
        		return false;
    		}
    	}else{
    		transaction.setErrorDesc("Error parsing code 86, kurang dari 4 bagian");
    		return false;
    	}
    }
    
    /**
     * used to parse code 86 to bean, ex: SETOR TUNAI|547694|SDPPI KEMENKOMINFO 042281310|14:32:21
     * @param transaction -> model table
     * @param strSecond -> string of code 86
     * @return flag -> true if valid parse, else false
     */
    private static boolean parseSecondRec(Transaction transaction, String strSecond, String endpointCode){
    	//split string by |
    	String [] strSeconds = strSecond.split("\\|");
    	if(strSeconds.length > 3){
    		//split string by space
    		String [] strDescriptions = strSeconds[2].split("\\s+");
    		if(strDescriptions.length > 2 && strDescriptions[0].equalsIgnoreCase("SDPPI")){
    			String invoiceId = null;
    			String clientId = null;
    			String transactionCode = null;
    			if(endpointCode.equals(PostelConstant.BILS0)){
    				invoiceId = strDescriptions[2].substring(0,7);
    				transactionCode = strDescriptions[2].substring(7,9);
    				clientId = strDescriptions[2].substring(9);
    			}else if(endpointCode.equals(PostelConstant.PER01) || endpointCode.equals(PostelConstant.PAP)){
    				Date now = new Date();
    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    				String year = sdf.format(now);
    				Integer idx = strDescriptions[2].lastIndexOf(year);
    				invoiceId = null;
    				clientId = null;
    				transactionCode = null;
    				Boolean isError = false;
    				
    				List<Integer> listIdxOccurences = getIdxOccurences(strDescriptions[2], year);
    				if(listIdxOccurences.isEmpty()){
    					year = String.valueOf(Integer.valueOf(year) - 1);
    					listIdxOccurences = getIdxOccurences(strDescriptions[2], year);
    					
    					if(listIdxOccurences.isEmpty()){
    						isError = true;
    					}else if (listIdxOccurences.size() > 2){
    						idx = listIdxOccurences.get(listIdxOccurences.size() - 2);
    					}
    				}else if (listIdxOccurences.size() > 2){
    					idx = listIdxOccurences.get(listIdxOccurences.size() - 2);
    				}
    				
    				if(!isError){
    					invoiceId = strDescriptions[2].substring(0, idx + year.length());
    					transactionCode = strDescriptions[2].substring(invoiceId.length(), invoiceId.length()+2);
    					clientId = strDescriptions[2].substring(invoiceId.length()+transactionCode.length());
    				}else{
    					invoiceId = strDescriptions[2].substring(0,11);
    					transactionCode = strDescriptions[2].substring(11,13);
    					clientId = strDescriptions[2].substring(13);
    				}
    				
    			}else{
    				invoiceId = strDescriptions[2].substring(0,10);
    				transactionCode = strDescriptions[2].substring(10,12);
    				clientId = strDescriptions[2].substring(12);
    			}
    			transaction.setInvoiceId(invoiceId);
        		transaction.setClientId(clientId);
        		transaction.setTransactionCode(transactionCode);
        		transaction.setPaymentType(PostelConstant.HOST_TO_HOST);
        		
        		return true;
    		}else{
    			transaction.setErrorDesc("format narasi pada code 86 salah");
    			transaction.setPaymentType(PostelConstant.NOT_HOST_TO_HOST);
        		return false;
    		}
    	}else{
    		transaction.setErrorDesc("Error parsing code 86, kurang dari 4 bagian");
    		return false;
    	}
    }
    
    public static List<Integer> getIdxOccurences(String text, String textToSearch){
		int lastIndex = 0;
		List<Integer> listOccurences = new ArrayList<Integer>();
		while(lastIndex != -1){
		    lastIndex = text.indexOf(textToSearch,lastIndex);

		    if(lastIndex != -1){
		        listOccurences.add(lastIndex);
		        lastIndex += textToSearch.length();
		    }
		}
		
		return listOccurences;
	}
    
    /**
     * used to parse file transaction,
     *
     * @param mt940 -> model MT940
     * @return flag -> true if match, else false
     * @throws SQLException
     */
    public String getFileTransacStatus(MT940 mt940, String fileName) throws SQLException {

    	String message = PostelConstant.STATUS_VALID;
    	String transactionDate = "";
    	
    	try {
	        Calendar cal = Calendar.getInstance();
	        String fileDate = fileName.substring(fileName.indexOf("MT940")+5, fileName.length());
	        java.util.Date trxDate = DateUtil.convertStringToDate(fileDate,DateUtil.YYYYMMDD_DatePattern,false);
	        cal.setTime(trxDate);
	        mt940.setTransactionDate(cal);
	
	        String contentFile=mt940.getContentFile();
	        
	        if (!contentFile.equalsIgnoreCase("")) {
	
	            //(update : 24-01-07), if use '\\r', cant process first source file is string ( ex : from axis webservice )
	            // only can proses if source file is bytes, so remove '\\r' to solve this problem.
	//            String[] recordFile=contentFile.split("\\r\\n");
	            String[] recordFile=contentFile.split("\\n");
	            String[] code25=recordFile[2].split(":25:");
	            String[] code62=recordFile[recordFile.length-2].split(":62F:");
	
	//            if (recordFile.length > 2 && recordFile[0].indexOf("BANK MANDIRI") != -1 && recordFile[0].indexOf("PTABC") != -1) {
	            if(code62.length > 1) {
	            	if(code25.length > 1) {
		            	String filePattern = FileFormatUtilBni.getFilePattern();
		            	//cek rekening valid
		            	if(code25[1].trim().equals(filePattern.substring(0, filePattern.indexOf("MT940")))) {
		            		String[] code20=recordFile[1].split(":20:");
		
		                    if (code20.length>1) {
		                    	
		                    	if(code20[1].trim().length() >= 8){
		                    		//get body contain code 20 -> transaction date
			                        transactionDate=code20[1].trim().substring(0, 8);
			                        trxDate = DateUtil.convertStringToDate(transactionDate,DateUtil.YYYYMMDD_DatePattern,false);
		                    	}
		                    	
								if (transactionDate.equals(fileDate)) {
									// java.util.Date trxDate = DateUtil.convertStringToDate(transactionDate);
									// cal.setTime(trxDate);
									//
									// mt940.setTransactionDate(cal);

									List<String> recordTransac = new ArrayList<String>();

									// get body contains transaction {code 61 and 86}
									for (int i = 0; i < recordFile.length; i++) {
										if (recordFile[i].startsWith(":61:")) {
											StringBuilder recTransac = new StringBuilder(recordFile[i]);
											if (recordFile[++i].startsWith(":86:")) {
												recTransac.append(recordFile[i]);
												recordTransac.add(recTransac.toString());
											}
										}
									}

									try {
										// flag=parseRecordToBean(recordTransac,mt940);
										if (!parseRecordToBean(recordTransac, mt940)) {
											message = PostelConstant.STATUS_ERROR_PARSE;
										}
									} catch (Exception e) {
										logger.error(e);
									}

								} else{
		                        	 message = PostelConstant.STATUS_FILE_INVALID;
		                             logger.error("[MT940Parser.isValidFileTransac] transaction date between tag 20 and filname is different.");
		                        }
		                    } else {
		                    	message = PostelConstant.STATUS_DATE_NA;
		                        logger.error("[MT940Parser.isValidFileTransac] no found transaction date in code 20.");
		                    }
		            	}else {
		            		message = PostelConstant.STATUS_ACCOUNT_INVALID;
		            		logger.error("[MT940Parser.isValidFileTransac] invalid account number.");
		            	}
		            } else {
		            	message = PostelConstant.STATUS_ACCOUNT_NA;
		            	logger.error("[MT940Parser.isValidFileTransac] no found account number in code 25.");
		//                logger.error("[MT940Parser.isValidFileTransac] no found header MANDIRI and POSTEL in code 1 and 2.");
		            }
	            } else{
	            	message = PostelConstant.STATUS_INCOMPLETED_FILE;
            		logger.error("[MT940Parser.isValidFileTransac] invalid account number.");
	            }
	            
	        } else {
	        	message = PostelConstant.STATUS_CONTENT_NA;
	            logger.error("[MT940Parser.isValidFileTransac] empty file.");
	        }
    	} catch (ParseException e1) {
            message = PostelConstant.STATUS_DATE_INVALID;
            logger.error("[MT940Parser.isValidFileTransac] invalid transaction date in tag 20.");
            e1.printStackTrace();
        }

        return message;
    }
    
    /**
     * used to parse file transaction for multi biller,
     *
     * @param mt940 -> model MT940
     * @return flag -> true if match, else false
     * @throws SQLException
     */
    public String getFileTransacStatus(MT940 mt940, String fileName, String sameAccountNoFlag, 
    		String transactionCode, String endpointCode) throws SQLException {
    	logger.info("bni, getFileTransacStatus, sameAccountNoFlag : " + sameAccountNoFlag);
    	logger.info("bni, getFileTransacStatus, endpointCode : " + endpointCode);
    	logger.info("bni, getFileTransacStatus, fileName : " + fileName);

    	String message = PostelConstant.STATUS_VALID;
    	String transactionDate = "";
    	
    	try {
	        Calendar cal = Calendar.getInstance();
	        //Ini sudah disesuaikan dengan bni ex : 201509151715-SIGIT-MT940_H0-1210005737386.txt
	        //Masalahnya bni tanggal file-nya adalah hari ini bukan H-1 jadi harus dikurangi dulu 1 :(
	        String fileDate = fileName.substring(0, 8);
//	        String fileDate = fileName.substring(fileName.length() - 8, fileName.length());
	        java.util.Date trxDate = DateUtil.convertStringToDate(fileDate,DateUtil.YYYYMMDD_DatePattern,false);
	        cal.setTime(trxDate);
	        cal.add(Calendar.DAY_OF_MONTH, -1);
	        mt940.setTransactionDate(cal);
	
	        String contentFile=mt940.getContentFile();
	        
	        if (!contentFile.equalsIgnoreCase("")) {
	
	            String[] recordFile=contentFile.split("\\n");
	            String[] code25=recordFile[2].split(":25:");
	            String[] code62=recordFile[recordFile.length-3].split(":62F:");
            	logger.info("Code62 : " + code62.length);
            	
	            if(code62.length > 1) {
	            	if(code25.length > 1) {
	    	            String filePattern = FileFormatUtilBni.getFilePattern();
	    	            String accountNo = filePattern.substring(filePattern.lastIndexOf("_") + 1, filePattern.lastIndexOf("."));
		            	//cek rekening valid
		            	if(code25[1].trim().equals(accountNo)) {
		            		String[] code20=recordFile[1].split(":20:");
		
		            		//cek tanggal di nama file dan di code 20
		                    if (code20.length>1) {
		                    	
//		                    	if(code20[1].trim().length() >= 8){
//		                    		//get body contain code 20 -> transaction date
//			                        transactionDate=code20[1].trim().substring(0, 8);
//			                        trxDate = DateUtil.convertStringToDate(transactionDate,DateUtil.YYYYMMDD_DatePattern,false);
//		                    	}
		                    	
//								if (transactionDate.equals(fileDateMinusOne)) {

									List<String> recordTransac = new ArrayList<String>();

									// get body contains transaction {code 61 and 86}
									for (int i = 0; i < recordFile.length; i++) {
										if (recordFile[i].startsWith(":61:")) {
											StringBuilder recTransac = new StringBuilder(recordFile[i]);
											if (recordFile[++i].startsWith(":86:")) {
												recTransac.append(recordFile[i]);
												recordTransac.add(recTransac.toString());
											}
										}
									}

									try {
										if (!parseRecordToBean(recordTransac, mt940, sameAccountNoFlag, transactionCode, endpointCode)) {
											message = PostelConstant.STATUS_ERROR_PARSE;
										}
									} catch (Exception e) {
										logger.trace(e);
									}

		                    } else {
		                    	message = PostelConstant.STATUS_DATE_NA;
		                        logger.trace("bni, [MT940Parser.isValidFileTransac] no found transaction date in code 20.");
		                    }
		            	}else {
		            		message = PostelConstant.STATUS_ACCOUNT_INVALID;
		            		logger.trace("bni, [MT940Parser.isValidFileTransac] invalid account number.");
		            	}
		            } else {
		            	message = PostelConstant.STATUS_ACCOUNT_NA;
		            	logger.trace("bni, [MT940Parser.isValidFileTransac] no found account number in code 25.");
		//                logger.error("[MT940Parser.isValidFileTransac] no found header MANDIRI and POSTEL in code 1 and 2.");
		            }
	            } else{
	            	message = PostelConstant.STATUS_INCOMPLETED_FILE;
	            	logger.trace("bni, [MT940Parser.isValidFileTransac] invalid bit62");
	            }
	            
	        } else {
	        	message = PostelConstant.STATUS_CONTENT_NA;
	            logger.error("bni, [MT940Parser.isValidFileTransac] empty file.");
	        }
    	} catch (ParseException e1) {
            message = PostelConstant.STATUS_DATE_INVALID;
            logger.error("bni, [MT940Parser.isValidFileTransac] invalid transaction date in tag 20.");
            e1.printStackTrace();
        }

        return message;
    }

}
