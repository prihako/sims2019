package id.co.sigma.mx.project.ftpreconcile.constant;

/**
 * Created by IntelliJ IDEA.
 * User: ferdi
 * Date: Oct 12, 2006
 * Time: 4:32:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostelConstant {
    //for table listener_log
    public static final String STATUS_VALID = "VA"; // "VALID"
    public static final String STATUS_ERROR_PARSE = "EP"; //"ERROR PARSE" (fail format file and found different transaction date)
    public static final String STATUS_NOT_AVAILABLE = "NA"; //"NOT AVAILABLE"
    public static final String STATUS_FTP_DOWN = "FD"; //"NOT AVAILABLE"
    public static final String STATUS_IGNORED = "IG"; //"IGNORED"
    public static final String STATUS_ERROR_DECRYPT = "ED"; //"ERROR DECRYPT"
    public static final String STATUS_DATE_INVALID = "DI"; //"DATE INVALID"
    public static final String STATUS_FILE_INVALID = "FI"; //"DATE FILE NAME <> DATE TAG 20"
    public static final String STATUS_INCOMPLETED_FILE = "IF"; //FILE NOT COMPLETED
    public static final String STATUS_DATE_NA = "DN"; //"DATE NOT AVAILABLE"
    public static final String STATUS_ACCOUNT_INVALID = "AI"; //"ACCOUNT INVALID"
    public static final String STATUS_ACCOUNT_NA = "AN"; //"ACCOUNT NOT AVAILABLE"
    public static final String STATUS_CONTENT_NA = "CN"; //"CONTENT NOT AVAILABLE"

    //for table transaction_log
    public static final String STATUS_READY = "R"; //"READY"
    public static final String STATUS_ERROR = "E"; //"ERROR"

    //for table parser_log
    public static final String STATUS_SUCCESS = "S"; //"SUCCESS"
    public static final String STATUS_FAIL = "F"; //"FAIL"

    //for source_type
    public static final String FTP = "F"; //"FTP"
    public static final String EMAIL = "M"; //"EMAIL"
    public static final String FILEFOLDER = "D"; //"FILEFOLDER"

    //for transaction_src
    public static final String FILE = "F"; //"FILE"

    //for parser info
    public static final String DELIMETER = "/";
    public static final int LENGTH_CLIENTID = 8;
    public static final int LENGTH_INVOICEID = 16;
    public static final String CREDIT = "C";
    public static final String DEBIT = "D";
    public static final String CREDIT_BNI = "C";
    
    //add in 07-01-2014 for unknown transaction code if have same account, ex: skor and reor
    public static final String UNKNOWN_TRANSACTION_CODE = "Transaction Code Unknown or not exist";
    
    public static final String HOST_TO_HOST = "h2h";
    
    public static final String NOT_HOST_TO_HOST = "not_h2h";
    
    public static final String BILS0 = "10";
    
    public static final String PER01 = "70";
    
    public static final String PAP = "80";
    
    public static final String REOR = "20";
    
    public static final String SKOR = "30";

    public static final String UNAR = "40";
    
    public static final String IAR = "50";
    
    public static final String IKRAP = "60";
    
    public static final String KLBSI = "90";
}

