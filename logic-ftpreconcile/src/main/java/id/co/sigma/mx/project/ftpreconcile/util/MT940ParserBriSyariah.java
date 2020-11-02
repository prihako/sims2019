package id.co.sigma.mx.project.ftpreconcile.util;

import id.co.sigma.mx.project.ftpreconcile.constant.PostelConstant;
import id.co.sigma.mx.project.ftpreconcile.model.MT940;
import id.co.sigma.mx.project.ftpreconcile.model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
public class MT940ParserBriSyariah {

	private static Logger logger = Logger.getLogger(MT940ParserBriSyariah.class);

	private SequenceUtil sequenceUtil;

	private PaymentUtilBriSyariah paymentUtil;

	public void setPaymentUtil(PaymentUtilBriSyariah paymentUtil) {
		this.paymentUtil = paymentUtil;
	}

	public void setSequenceUtil(SequenceUtil sequenceUtil) {
		this.sequenceUtil = sequenceUtil;
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
	public boolean parseRecordToBean(List<String> recordTransac, MT940 mt940)
			throws SQLException, ClassNotFoundException {
		logger.info("###Enter parseRecordToBean function###");

		List<Transaction> listOfBean = new ArrayList<Transaction>(recordTransac.size());

		if (!recordTransac.isEmpty()) {
			String record = null;
			Transaction transaction = null;

			int midIdx;
			String firstCode = null;
			String secCode = null;
			boolean flag;
			boolean isValidFile = true;
			for (Iterator<String> i = recordTransac.iterator(); i.hasNext() && isValidFile;) {

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

				if (!transaction.isTransactionStatusFlag()) {
					transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
				} else {
					transaction.setTransactionStatus(PostelConstant.STATUS_READY);
				}

				transaction.setNeedReconcile("Y");

				/* set tambahan */
				// :86:UBP60105000002FFFFFF083437610060410
				midIdx = record.indexOf(":86:");

				firstCode = record.substring(4, midIdx).trim();
				secCode = record.substring(midIdx + 4).trim();

				// code 61 & 86 show all in raw_transaction_msg (update 27 nov'06)- for web
				// request
				transaction.setRawTransactionMsg(firstCode + " | " + secCode);

				if (secCode.length() > 6) {
					String channelId = secCode.substring(secCode.indexOf("UBP") + 3, secCode.indexOf("UBP") + 7);
					String paymentChannel = paymentUtil.getChannelPayment(channelId);
					if (paymentChannel != null) {
						transaction.setPaymentChannel(paymentChannel);
					} else {
						transaction.setPaymentChannel("Unknown : " + channelId);
					}
				} else {
					transaction.setPaymentChannel("unknown");
				}

				Calendar mt940Date = mt940.getTransactionDate();

				flag = parseFirstRec(transaction, firstCode, mt940Date);

				// if parse true && not empty code 86, then continue to parse code 86
				if (flag) {
					flag = parseSecondRec(transaction, secCode);
				}
				transaction.setTransactionStatusFlag(flag);
				transaction.setRawTransactionMsg(firstCode + " | " + secCode);

				listOfBean.add(transaction);
			}

			if (isValidFile)
				mt940.setRecordTransaction(listOfBean);
			return isValidFile;
		} else {
			mt940.setRecordTransaction(listOfBean);
			return true; // valid but empty transaction
		}
	}

	/**
	 * used only to parse record transaction and put to list of bean (Transaction)
	 * for reor and skor
	 *
	 * @param recordTransac -> record2 transaction MI940
	 * @param mt940
	 * @return flag -> true if valid, else false
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean parseRecordToBeanSkorReor(List<String> recordTransac, MT940 mt940)
			throws SQLException, ClassNotFoundException {

		logger.info("###Enter parseRecordToBeanSkorReor function###");
		List<Transaction> listOfBean = new ArrayList<Transaction>(recordTransac.size());

		if (!recordTransac.isEmpty()) {
			String record = null;
			Transaction transaction = null;

			int midIdx;
			String firstCode = null;
			String secCode = null;
			boolean flag;
			boolean isValidFile = true;

			// use for skor and reor
			String transactionCode = null;

			for (Iterator<String> i = recordTransac.iterator(); i.hasNext() && isValidFile;) {

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

				if (!transaction.isTransactionStatusFlag()) {
					transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
				} else {
					transaction.setTransactionStatus(PostelConstant.STATUS_READY);
				}

				transaction.setNeedReconcile("Y");

				/* set tambahan */

				midIdx = record.indexOf(":86:");

				firstCode = record.substring(4, midIdx).trim();
				secCode = record.substring(midIdx + 4).trim();

				// Only use if same account no
				transactionCode = secCode.substring(secCode.length() - 2, secCode.length());
				secCode = secCode.substring(0, secCode.length() - 2);
				transaction.setTransactionCode(transactionCode);

				// code 61 & 86 show all in raw_transaction_msg (update 27 nov'06)- for web
				// request
				transaction.setRawTransactionMsg(firstCode + " | " + secCode);

				// Tambahan untuk set payment channel 22-10-2014
				if (secCode.length() > 6) {
					String channelId = secCode.substring(secCode.indexOf("UBP") + 3, secCode.indexOf("UBP") + 7);
					String paymentChannel = paymentUtil.getChannelPayment(channelId);
					logger.info("=====paymentChannel " + paymentChannel);
					if (paymentChannel != null) {
						transaction.setPaymentChannel(paymentChannel);
					} else {
						transaction.setPaymentChannel("Unknown : " + channelId);
					}
				} else {
					transaction.setPaymentChannel("unknown");
				}

				Calendar mt940Date = mt940.getTransactionDate();

				flag = parseFirstRec(transaction, firstCode, mt940Date);

				// if parse true && not empty code 86, then continue to parse code 86
				if (flag) {
					flag = parseSecondRec(transaction, secCode);
				}
				transaction.setTransactionStatusFlag(flag);
				transaction.setRawTransactionMsg(firstCode + " | " + secCode);

				listOfBean.add(transaction);
			}

			if (isValidFile)
				mt940.setRecordTransaction(listOfBean);
			return isValidFile;
		} else {
			mt940.setRecordTransaction(listOfBean);
			return true; // valid but empty transaction
		}
	}

	public boolean parseRecordToBean(List<String> recordTransac, MT940 mt940, String sameAccountNoFlag,
			String transactionCodeParam, String endpointCode) throws SQLException, ClassNotFoundException {
		logger.info("bri, Enter parseRecordToBean, sameAccountFlag : " + sameAccountNoFlag);
		logger.info("bri, Enter parseRecordToBean, endpointCode : " + endpointCode);

		List<Transaction> listOfBean = new ArrayList<Transaction>(recordTransac.size());

		if (!recordTransac.isEmpty()) {
			String record = null;
			Transaction transaction = null;

			int midIdx;
			String firstCode = null;
			String secCode = null;
			boolean flag;
			boolean isValidFile = true;

			for (Iterator<String> i = recordTransac.iterator(); i.hasNext() && isValidFile;) {
				
				record = i.next();

				transaction = new Transaction();

				/* set tambahan */
				try {
					transaction.setTransactionId(sequenceUtil.generateTransactionId());

					transaction.setTransactionSrc(PostelConstant.FILE);

					if (!transaction.isTransactionStatusFlag()) {
						transaction.setTransactionStatus(PostelConstant.STATUS_ERROR);
					} else {
						transaction.setTransactionStatus(PostelConstant.STATUS_READY);
					}

					transaction.setNeedReconcile("Y");

					/* set tambahan */

					midIdx = record.indexOf(":86:");

					firstCode = record.substring(4, midIdx).trim();
					secCode = record.substring(midIdx + 4).trim();
					
					// Only use if same account no
					boolean isErrorTransactionCode = false;
					if (sameAccountNoFlag != null) {

						if (sameAccountNoFlag.equalsIgnoreCase("y") || sameAccountNoFlag.equalsIgnoreCase("yes")
								|| sameAccountNoFlag.equalsIgnoreCase("1")) {
							String transactionCode = null;
							int transactionCodeLength = transactionCodeParam.length();
							String accountNo = FileFormatUtilBri.getFilePattern();
							accountNo = accountNo.substring(0, accountNo.indexOf("+"));
							String flagBit = paymentUtil.getBitFlag(accountNo, transactionCodeParam);

							if (flagBit.equalsIgnoreCase("86")) {
								if (secCode.length() > transactionCodeLength) {
									transactionCode = secCode.substring(secCode.length() - transactionCodeLength,
											secCode.length());
									secCode = secCode.substring(0, secCode.length() - transactionCodeLength);
								}
							} else if (flagBit.equalsIgnoreCase("61")) {
								if (firstCode.length() > transactionCodeLength) {
									transactionCode = firstCode.substring(firstCode.length() - transactionCodeLength,
											firstCode.length());
									firstCode = firstCode.substring(0, firstCode.length() - transactionCodeLength);
								}
							}

							if (paymentUtil.getTransactionCode(accountNo, transactionCode)) {
								transaction.setTransactionCode(transactionCode);
							} else {
								isErrorTransactionCode = true;
								transaction.setTransactionCode(transactionCode);
								transaction.setTransactionStatusFlag(false);
								transaction.setErrorDesc(
										PostelConstant.UNKNOWN_TRANSACTION_CODE + ", code : " + transactionCode);
							}

						}
					}

					// code 61 & 86 show all in raw_transaction_msg (update 27 nov'06)- for web
					// request
					transaction.setRawTransactionMsg(firstCode + " | " + secCode);

					// For set payment channel, ex: ATM, IB, Branch, etc.
//                if(secCode.length() > 6){
//	                String channelId = secCode.substring(4, 10);
//	                String paymentChannel = paymentUtil.getChannelPayment(channelId);
//	                if(paymentChannel != null){
//	                	transaction.setPaymentChannel(paymentChannel);
//	                }else{
//	                	transaction.setPaymentChannel("bri, Unknown : " + channelId);
//	                }
//                }else{
//                	transaction.setPaymentChannel("bri, unknown");
//                }

					Calendar mt940Date = mt940.getTransactionDate();

					flag = parseFirstRec(transaction, firstCode, mt940Date);

					// if parse true && not empty code 86, then continue to parse code 86, and if
					// same account no check transaction code
					if (flag) {
						flag = parseSecondRec(transaction, secCode, endpointCode);
					}

					// This if use to check is there any error in transactionCode that have same
					// account no,
					if (!isErrorTransactionCode) {
						transaction.setTransactionStatusFlag(flag);
					}

				} catch (SQLException e) {
					e.printStackTrace();
					logger.error(e);
					throw e;
				}

				listOfBean.add(transaction);
			}

			if (isValidFile)
				mt940.setRecordTransaction(listOfBean);
			return isValidFile;
		} else {
			mt940.setRecordTransaction(listOfBean);
			return true; // valid but empty transaction
		}
	}

	public boolean checkTransactionCode(Transaction transaction, String strCode, String transactionCodeParam) {
		String transactionCode = null;
		int transactionCodeLength = transactionCodeParam.length();
		String accountNo = FileFormatUtilBri.getFilePattern();
		accountNo = accountNo.substring(0, accountNo.indexOf("MT940"));

		if (strCode.length() > transactionCodeLength) {
			transactionCode = strCode.substring(strCode.length() - transactionCodeLength, strCode.length());
			strCode = strCode.substring(0, strCode.length() - transactionCodeLength);
		}

		if (paymentUtil.getTransactionCode(accountNo, transactionCode)) {
			transaction.setTransactionCode(transactionCode);
			return true;
		} else {
			transaction.setTransactionCode(transactionCode);
			transaction.setTransactionStatusFlag(false);
			transaction.setErrorDesc(PostelConstant.UNKNOWN_TRANSACTION_CODE + ", code : " + transactionCode);
			return false;
		}

	}

	/**
	 * used to parse code 61 to bean, ex: 61:060630D6000NCHG,
	 * 
	 * @param transaction -> model table
	 * @param strFirst    -> string of code 61
	 * @return flag -> true if valid parse, else false
	 */
	private static boolean parseFirstRec(Transaction transaction, String strFirst, Calendar mt940Date) {

		if (strFirst.length() > 11) {
			String transacDateStr = strFirst.substring(0, 6);
			try {
				java.util.Date trxDate = DateUtil.convertStringToDate(transacDateStr, DateUtil.YYMMDD_DatePattern,
						false);
				Calendar cal = Calendar.getInstance();
				cal.setTime(trxDate);
				transaction.setTransactionDateCal(cal);
				transaction.setTransactionDate(new Timestamp(cal.getTimeInMillis()));
			} catch (ParseException e) {
				logger.error("[MT940Parser.parseFirstRec] error parse, ", e);
				transaction.setErrorDesc("Error parsing tanggal transaksi.");
				transaction.setTransactionDate(new Timestamp(0));
				e.printStackTrace();
				return false;
			}

			// Untuk cek tanggal transaksi, skrng dinonaktifkan karena mungkin transaksi
			// kemaren juga masuk
//            Calendar transacDate=transaction.getTransactionDateCal();
//            
//            if ( transacDate.compareTo(mt940Date) != 0 ) {
//            	transacDate.add(Calendar.DAY_OF_YEAR, 1);
//            	if(transacDate.compareTo(mt940Date) != 0) {
//            		logger.error("[MT940Parser.parseFirstRec] error parse transaction date");
//                    transaction.setErrorDesc("Error tanggal transaksi tidak valid.");
//                    return false;
//            	}
//            }

			String transacType = strFirst.substring(6, 7);
			if (PostelConstant.CREDIT.equalsIgnoreCase(transacType)
					|| PostelConstant.DEBIT.equalsIgnoreCase(transacType)) {
				transaction.setTransactionType(transacType);
			} else {
				transaction.setErrorDesc("Transaction type harus Credit(C) or Debit (D)");
				return false;
			}

			String amount = strFirst.substring(7, strFirst.length() - 4);

			try {
				BigDecimal amountTransac = new BigDecimal(amount);
				transaction.setTransactionAmount(amountTransac);
			} catch (NumberFormatException e) {
				logger.error("[MT940Parser.parseFirstRec] error parse, amount=" + amount);
				transaction.setErrorDesc("Error parsing jumlah transaksi.");
				return false;
			}

			String typeMsg = strFirst.substring(strFirst.length() - 4);
			transaction.setMessageType(typeMsg);
		} else {
			logger.error("[MT940Parser.parseFirstRec] Information from tag 61 is not valid");
			transaction.setErrorDesc("Informasi tag 61 tidak lengkap.");
			return false;
		}

		return true;
	}

	private static boolean parseFirstRec(Transaction transaction, String strFirst, Calendar mt940Date,
			String sameAccountNoFlag, String transactionCodeParam) {

		if (strFirst.length() > 11) {
			String transacDateStr = strFirst.substring(0, 6);
			try {
				java.util.Date trxDate = DateUtil.convertStringToDate(transacDateStr, DateUtil.YYMMDD_DatePattern,
						false);
				Calendar cal = Calendar.getInstance();
				cal.setTime(trxDate);
				transaction.setTransactionDateCal(cal);
				transaction.setTransactionDate(new Timestamp(cal.getTimeInMillis()));
			} catch (ParseException e) {
				logger.error("[MT940Parser.parseFirstRec] error parse, ", e);
				transaction.setErrorDesc("Error parsing tanggal transaksi.");
				transaction.setTransactionDate(new Timestamp(0));
				e.printStackTrace();
				return false;
			}

			Calendar transacDate = transaction.getTransactionDateCal();

			if (transacDate.compareTo(mt940Date) != 0) {
				transacDate.add(Calendar.DAY_OF_YEAR, 1);
				if (transacDate.compareTo(mt940Date) != 0) {
					logger.error("[MT940Parser.parseFirstRec] error parse transaction date");
					transaction.setErrorDesc("Error tanggal transaksi tidak valid.");
					return false;
				}
			}

			String transacType = strFirst.substring(6, 7);
			if (transacType == null || !PostelConstant.CREDIT.equalsIgnoreCase(transacType)) {
				transaction.setErrorDesc("Transaction type harus Credit(C)");
				return false;
			}
			transaction.setTransactionType(transacType);

			String amount = strFirst.substring(7, strFirst.length() - 4);

			try {
				BigDecimal amountTransac = new BigDecimal(amount);
				transaction.setTransactionAmount(amountTransac);
			} catch (NumberFormatException e) {
				logger.error("[MT940Parser.parseFirstRec] error parse, amount=" + amount);
				transaction.setErrorDesc("Error parsing jumlah transaksi.");
				return false;
			}

			String typeMsg = strFirst.substring(strFirst.length() - 4);
			transaction.setMessageType(typeMsg);
		} else {
			logger.error("[MT940Parser.parseFirstRec] Information from tag 61 is not valid");
			transaction.setErrorDesc("Informasi tag 61 tidak lengkap.");
			return false;
		}

		return true;
	}

	/**
	 * used to parse code 86 to bean, ex: 86:123456/abcde/0816883533
	 * 
	 * @param transaction -> model table
	 * @param strSecond   -> string of code 86
	 * @return flag -> true if valid parse, else false
	 */
//    private static boolean parseSecondRec_old(Transaction transaction, String strSecond) {
//        String[] part = strSecond.trim().split(PostelConstant.DELIMETER);
//
//        if (part.length>=2) {
//
//            if (!part[0].trim().equals("") && !part[1].trim().equals("")) {
//
//                if (!part[0].trim().equals("") && part[0].trim().length()>8) {
//                    transaction.setClientId(part[0].trim().substring(0,8));
//                } else {
//                    transaction.setClientId(part[0].trim());
//                }
//
//                if (!part[1].trim().equals("") && part[1].trim().length()>16) {
//                    transaction.setInvoiceId(part[1].trim().substring(0,16));
//                } else {
//                    transaction.setInvoiceId(part[1].trim());
//                }
//
//                if (part.length==3) {
//                    if (!part[2].trim().equals("") && part[2].trim().length()>12) {
//                        transaction.setClientPhone(part[2].trim().substring(0,12));
//                    } else {
//                        transaction.setClientPhone(part[2].trim());
//                    }
//                } else {
//                    transaction.setClientPhone("");
//                }
//
//                return true;
//            } else {
//                transaction.setErrorDesc("Error parsing code 86, format clientId atau invoiceId tidak boleh kosong.");
//                return false;
//            }
//
//        } else {
//            transaction.setErrorDesc("Error parsing code 86, format harus (clientId/invoiceId/clientPhone).");
//            return false;
//        }
//    }

	/**
	 * used to parse code 86 to bean, ex: UBP60105000002FFFFFF01920996944
	 * 
	 * @param transaction -> model table
	 * @param strSecond   -> string of code 86
	 * @return flag -> true if valid parse, else false
	 */
	private static boolean parseSecondRec(Transaction transaction, String strSecond) {
		if (strSecond.length() == 48) {
			if (strSecond.startsWith("ISR")) {
				transaction.setBranchCode(strSecond.substring(3, 8).trim());
				transaction.setPaymentChannel(strSecond.substring(8, 15).trim());
				transaction.setClientName(strSecond.substring(15, 26).trim());
				transaction.setInvoiceId(strSecond.substring(26, 37).trim());
				transaction.setClientId(strSecond.substring(37, 48).trim());
				transaction.setTransactionCode(transaction.getTransactionCode().trim());
				transaction.setPaymentType(PostelConstant.HOST_TO_HOST);
				return true;
			} else {
				transaction.setErrorDesc("Error parsing code 86, format harus dimulai dengan ISR");
				transaction.setPaymentType(PostelConstant.NOT_HOST_TO_HOST);
				return true;
			}
		} else {
			if (strSecond.startsWith("ISR")) {
				transaction.setErrorDesc("Error parsing code 86, panjang string kurang dari 48 digit");
				return false;
			} else {
				transaction.setPaymentType(PostelConstant.NOT_HOST_TO_HOST);
				return true;
			}
		}
	}

	public static void main(String[] args) {
		String strSecond = "ISR3500 TELLER RADIO SUAR    0852179   00102089 ";
		if (strSecond.length() == 48) {
			if (strSecond.startsWith("ISR")) {
				String branchCode = strSecond.substring(3, 8).trim();
				String paymentChannel = strSecond.substring(8, 15).trim();
				String clientName = strSecond.substring(15, 26).trim();
				String invoiceNo = strSecond.substring(26, 37).trim();
				String clientId = strSecond.substring(37, 48).trim();

				System.out.println("branch: " + branchCode);
				System.out.println("channel: " + paymentChannel);
				System.out.println("name: " + clientName);
				System.out.println("invoie: " + invoiceNo);
				System.out.println("client: " + clientId);
			} else {
				System.out.println("NOT ISR");
			}
		} else {
			System.out.println("Length doesn't match");
		}
	}

	/**
	 * used to parse code 86 to bean, ex: H2Habcd efghijklmnopqrst06740490010251310
	 * --> ISR(3)abcd (5)efghij (7)klmnopqrst (11)1234567890 (11)1234567890 (11) -->
	 * ISR3500 TELLER RADIO SUAR 0852179 00102089
	 * 
	 * @param transaction -> model table
	 * @param strSecond   -> string of code 86
	 * @return flag -> true if valid parse, else false
	 */
	private boolean parseSecondRec(Transaction transaction, String strSecond, String endpointCode) {
		if (strSecond.length() == 48) {
			if (strSecond.startsWith("ISR")) {
				transaction.setBranchCode(strSecond.substring(3, 8).trim());
				transaction.setPaymentChannel(strSecond.substring(8, 15).trim());
				transaction.setClientName(strSecond.substring(15, 26).trim());
				transaction.setInvoiceId(strSecond.substring(26, 37).trim());
				transaction.setClientId(strSecond.substring(37, 48).trim());
				transaction.setTransactionCode(transaction.getTransactionCode().trim());
				transaction.setPaymentType(PostelConstant.HOST_TO_HOST);
				return true;
			} else {
				transaction.setErrorDesc("Error parsing code 86, format harus dimulai dengan ISR");
				transaction.setPaymentType(PostelConstant.NOT_HOST_TO_HOST);
				return true;
			}
		} else {
			if (strSecond.startsWith("ISR")) {
				transaction.setErrorDesc("Error parsing code 86, panjang string kurang dari 48 digit");
				return false;
			} else {
				transaction.setPaymentType(PostelConstant.NOT_HOST_TO_HOST);
				return true;
			}
		}
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
			String fileDate = fileName.substring(fileName.indexOf("MT940") + 5, fileName.length());
			java.util.Date trxDate = DateUtil.convertStringToDate(fileDate, DateUtil.YYYYMMDD_DatePattern, false);
			cal.setTime(trxDate);
			mt940.setTransactionDate(cal);

			String contentFile = mt940.getContentFile();

			if (!contentFile.equalsIgnoreCase("")) {

				// (update : 24-01-07), if use '\\r', cant process first source file is string (
				// ex : from axis webservice )
				// only can proses if source file is bytes, so remove '\\r' to solve this
				// problem.
				// String[] recordFile=contentFile.split("\\r\\n");
				String[] recordFile = contentFile.split("\\n");
				String[] code25 = recordFile[2].split(":25:");
				String[] code62 = recordFile[recordFile.length - 2].split(":62F:");

				// if (recordFile.length > 2 && recordFile[0].indexOf("BANK MANDIRI") != -1 &&
				// recordFile[0].indexOf("PTABC") != -1) {
				if (code62.length > 1) {
					if (code25.length > 1) {
						String filePattern = FileFormatUtilBri.getFilePattern();
						// cek rekening valid
						if (code25[1].trim().equals(filePattern.substring(0, filePattern.indexOf("MT940")))) {
							String[] code20 = recordFile[1].split(":20:");

							if (code20.length > 1) {

								if (code20[1].trim().length() >= 8) {
									// get body contain code 20 -> transaction date
									transactionDate = code20[1].trim().substring(0, 8);
									trxDate = DateUtil.convertStringToDate(transactionDate,
											DateUtil.YYYYMMDD_DatePattern, false);
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

								} else {
									message = PostelConstant.STATUS_FILE_INVALID;
									logger.error(
											"[MT940Parser.isValidFileTransac] transaction date between tag 20 and filname is different.");
								}
							} else {
								message = PostelConstant.STATUS_DATE_NA;
								logger.error("[MT940Parser.isValidFileTransac] no found transaction date in code 20.");
							}
						} else {
							message = PostelConstant.STATUS_ACCOUNT_INVALID;
							logger.error("[MT940Parser.isValidFileTransac] invalid account number.");
						}
					} else {
						message = PostelConstant.STATUS_ACCOUNT_NA;
						logger.error("[MT940Parser.isValidFileTransac] no found account number in code 25.");
						// logger.error("[MT940Parser.isValidFileTransac] no found header MANDIRI and
						// POSTEL in code 1 and 2.");
					}
				} else {
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
	 * only used to parse file transaction from reor and skor,
	 *
	 * @param mt940 -> model MT940
	 * @return flag -> true if match, else false
	 * @throws SQLException
	 */
	public String getFileTransacStatusSkorReor(MT940 mt940, String fileName) throws SQLException {

		logger.info("###Enter getFileTransacStatusSkorReor function###");

		String message = PostelConstant.STATUS_VALID;
		String transactionDate = "";

		try {
			Calendar cal = Calendar.getInstance();
			String fileDate = fileName.substring(fileName.indexOf("MT940") + 5, fileName.length());
			java.util.Date trxDate = DateUtil.convertStringToDate(fileDate, DateUtil.YYYYMMDD_DatePattern, false);
			cal.setTime(trxDate);
			mt940.setTransactionDate(cal);

			String contentFile = mt940.getContentFile();

			if (!contentFile.equalsIgnoreCase("")) {

				// (update : 24-01-07), if use '\\r', cant process first source file is string (
				// ex : from axis webservice )
				// only can proses if source file is bytes, so remove '\\r' to solve this
				// problem.
				// String[] recordFile=contentFile.split("\\r\\n");
				String[] recordFile = contentFile.split("\\n");
				String[] code25 = recordFile[2].split(":25:");
				String[] code62 = recordFile[recordFile.length - 2].split(":62F:");

				// if (recordFile.length > 2 && recordFile[0].indexOf("BANK MANDIRI") != -1 &&
				// recordFile[0].indexOf("PTABC") != -1) {
				if (code62.length > 1) {
					if (code25.length > 1) {
						String filePattern = FileFormatUtilBri.getFilePattern();
						// cek rekening valid
						if (code25[1].trim().equals(filePattern.substring(0, filePattern.indexOf("MT940")))) {
							String[] code20 = recordFile[1].split(":20:");

							if (code20.length > 1) {

								if (code20[1].trim().length() >= 8) {
									// get body contain code 20 -> transaction date
									transactionDate = code20[1].trim().substring(0, 8);
									trxDate = DateUtil.convertStringToDate(transactionDate,
											DateUtil.YYYYMMDD_DatePattern, false);
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
										if (!parseRecordToBeanSkorReor(recordTransac, mt940)) {
											message = PostelConstant.STATUS_ERROR_PARSE;
										}
									} catch (Exception e) {
										logger.error(e);
									}

								} else {
									message = PostelConstant.STATUS_FILE_INVALID;
									logger.error(
											"[MT940Parser.isValidFileTransac] transaction date between tag 20 and filname is different.");
								}
							} else {
								message = PostelConstant.STATUS_DATE_NA;
								logger.error("[MT940Parser.isValidFileTransac] no found transaction date in code 20.");
							}
						} else {
							message = PostelConstant.STATUS_ACCOUNT_INVALID;
							logger.error("[MT940Parser.isValidFileTransac] invalid account number.");
						}
					} else {
						message = PostelConstant.STATUS_ACCOUNT_NA;
						logger.error("[MT940Parser.isValidFileTransac] no found account number in code 25.");
						// logger.error("[MT940Parser.isValidFileTransac] no found header MANDIRI and
						// POSTEL in code 1 and 2.");
					}
				} else {
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
	public String getFileTransacStatus(MT940 mt940, String fileName, String sameAccountNoFlag, String transactionCode,
			String endpointCode) throws SQLException {

		logger.info("bri, getFileTransacStatus, sameAccountNoFlag : " + sameAccountNoFlag);
		logger.info("bri, getFileTransacStatus, endpointCode : " + endpointCode);

		String message = PostelConstant.STATUS_VALID;
		String transactionDate = "";

		try {
			Calendar cal = Calendar.getInstance();
			String fileDate = null;
			
			if(fileName.contains("_")) {
				fileDate = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
			}else {
				fileDate = fileName.substring(fileName.indexOf(".") + 1, fileName.lastIndexOf("."));
			}
			
			logger.info("bri, fileDate : " + fileDate);

			java.util.Date trxDate = DateUtil.convertStringToDate(fileDate, DateUtil.YYYYMMDD_DatePattern, false);
			cal.setTime(trxDate);
			mt940.setTransactionDate(cal);

			String contentFile = mt940.getContentFile();

			if (!contentFile.equalsIgnoreCase("")) {

				String[] recordFile = contentFile.split("\\n");
				String[] code25 = recordFile[2].split(":25:");
				String[] code62 = recordFile[recordFile.length - 3].split(":62F:");

				if (code62.length > 1) {
					if (code25.length > 1) {
						String filePattern = FileFormatUtilBri.getFilePattern();
						// cek rekening valid
						if (code25[1].trim().equals(filePattern.substring(0, filePattern.indexOf("+")))) {
							String[] code20 = recordFile[1].split(":20:");

							if (code20.length > 1) {

								if (code20[1].trim().length() >= 8) {
									// get body contain code 20 -> transaction date
									transactionDate = code20[1].trim().substring(0, 8);
									trxDate = DateUtil.convertStringToDate(transactionDate,
											DateUtil.YYYYMMDD_DatePattern, false);
								}

								if (transactionDate.equals(fileDate)) {

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
										if (!parseRecordToBean(recordTransac, mt940, sameAccountNoFlag, transactionCode,
												endpointCode)) {
											message = PostelConstant.STATUS_ERROR_PARSE;
										}
									} catch (Exception e) {
										logger.error(e);
									}

								} else {
									message = PostelConstant.STATUS_FILE_INVALID;
									logger.trace(
											"bri, [MT940Parser.isValidFileTransac] transaction date between tag 20 and filname is different.");
								}
							} else {
								message = PostelConstant.STATUS_DATE_NA;
								logger.trace(
										"bri, [MT940Parser.isValidFileTransac] no found transaction date in code 20.");
							}
						} else {
							message = PostelConstant.STATUS_ACCOUNT_INVALID;
							logger.trace("bri, [MT940Parser.isValidFileTransac] invalid account number.");
						}
					} else {
						message = PostelConstant.STATUS_ACCOUNT_NA;
						logger.trace("bri, [MT940Parser.isValidFileTransac] no found account number in code 25.");
						// logger.error("[MT940Parser.isValidFileTransac] no found header MANDIRI and
						// POSTEL in code 1 and 2.");
					}
				} else {
					message = PostelConstant.STATUS_INCOMPLETED_FILE;
					logger.trace("bri, [MT940Parser.isValidFileTransac] invalid account number.");
				}

			} else {
				message = PostelConstant.STATUS_CONTENT_NA;
				logger.error("bri, [MT940Parser.isValidFileTransac] empty file.");
			}
		} catch (Exception e1) {
			message = PostelConstant.STATUS_DATE_INVALID;
			logger.error("bri, [MT940Parser.isValidFileTransac] invalid transaction date in tag 20.");
			logger.error("Error : ", e1);
		}

		return message;
	}

}
