package id.co.sigma.mx.project.ftpreconcile.constant;

public class PaymentConstant {

	/* 180000 ms = 3 minute timeout period */
	public static final long DEFAULT_TIMEOUT_PERIOD = 180000;

	public static final String INQUIRY_TRANSACTION_CODE = "000001";
	public static final String PAYMENT_TRANSACTION_CODE = "000002";

	public static final String SUCCESS_RESPONSE_CODE = "00";
	public static final String SIMF_DOWN_RESPONSE_CODE = "06";
	public static final String SIGN_ON_ERROR_RESPONSE_CODE = "07";
	public static final String TIMEOUT_RESPONSE_CODE = "08";
	public static final String DATABASE_DOWN_RESPONSE_CODE = "09";
	public static final String NO_REPLY_RESPONSE_CODE = "11";
	public static final String INVALID_AMOUNT_RESPONSE_CODE = "13";
	public static final String INVALID_INVOICE_ID_RESPONSE_CODE = "25";
	public static final String INVALID_CLIENT_ID_RESPONSE_CODE = "26";
	public static final String INVOICE_ALREADY_PAID_RESPONSE_CODE = "97";

	public static final Long INVALID_TRANSACTION_ERROR_CODE = 66L;

	public static final String PAYMENT_AMOUNT_FOR_INQUIRY = "000000000000";

	public static final String SYS_MSG_ID_SUCCESS = SUCCESS_RESPONSE_CODE;
	public static final String SYS_MSG_ID_INVALID_PAYMENT_AMOUNT = INVALID_AMOUNT_RESPONSE_CODE;
	public static final String SYS_MSG_ID_DATA_MISMATCH = "10";

	public static final String RECONCILE_STATUS_FULL_MATCH = "FM";

	public static final String RECONCILE_STATUS_PARTIAL_MATCH = "PM";

	public static final String RECONCILE_STATUS_NOT_MATCH = "NM";

	public static final String RECONCILE_STATUS_NOT_AVAILABLE = "NA";

	public static final String TRANSACTION_STATUS_PAID = "P";

	public static final String TRANSACTION_STATUS_UNPAID = "U";

	public static final String TRANSACTION_STATUS_FAILED = "F";

	public static final String TRANSACTION_STATUS_ERROR = "E";

	public static final int DEFAULT_PAYMENT_INITIAL_DELAY = 60;

	public static final int DEFAULT_PAYMENT_PROCESSING_DELAY = 1800;

	public static final int DEFAULT_PAYMENT_REVERSAL_PERIOD = 600;

	public static final int DEFAULT_FTP_HOUR = 5;

	public static final int DEFAULT_FTP_MINUTE = 0;

	public static final int DEFAULT_FTP_SECOND = 0;

	public static final String YES = "Y";

	public static final String NO = "N";

	public static final String BATCH_STATUS_STARTED = "S";

	public static final String BATCH_STATUS_COMPLETED = "C";

	public static final String SIMF_STATUS_PAID = "P";

	public static final String SIMF_STATUS_WAITING_FOR_REVERSAL = "T";

	public static final String SIMF_STATUS_REVERSED = "R";

	public static final String SIMF_STATUS_ERROR = "E";

	public static final long SYSTEM_MESSAGE_NO_SETTLEMENT_REQUIRED = 35;

	public static final long SYSTEM_MESSAGE_SETTLEMENT_SUBMITTED = 39;

	public static final String AUTO_SETTLEMENT_CREATOR = "AUTO";

	public static final String SETTLEMENT_REMARKS = "Auto Settlement Performed by TransactionIssuer";

	public static final String SETTLEMENT_REMARKS_RECOVERY = "Settlement Performed Automatically after Database Problem";

	public static final String LISTENER_STATUS_VALID = "VA";

	public static final String PARSER_STATUS_SUCCESS = "S";

	public static final String SOURCE_TYPE_FTP = "F";

	public static final String TRANSACTION_SRC_FILE = "F";

	public static final String TRANSACTION_SRC_SETTLEMENT = "S";

	public static final String TRANSACTION_STATUS_READY = "R";

	public static final String INQUIRY_PROCESSING_CODE = "000001";
    public static final String PAYMENT_PROCESSING_CODE = "000002";

    public static final String COMPANY_CODE_MANDIRI = "50000";

	// -- mti used for Mandiri financial request
    public static String MTI_FINANCIAL_REQUEST    = "0200";

    // -- mti used for Mandiri financial request response
    public static String MTI_FINANCIAL_REQUEST_RESPONSE    = "0210";

 // -- mti used for Mandiri reversal request
    public static String MTI_REVERSAL_REQUEST    = "0400";

    // -- mti used for Mandiri reversal request response
    public static String MTI_REVERSAL_REQUEST_RESPONSE    = "0410";

    public static enum MESSAGE_TYPE {INQUIRY, PAYMENT, REVERSAL};

}
