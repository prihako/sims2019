package com.balicamp.model.constant;

public class ModelConstant {

	static public class Print {
		public final static String KEY_PRINT_NAME = "printName";
		public final static String KEY_FILE_TYPE = "fileType";
	}

	static public class ChargeFee {
		public final static String WESELKU_BANK = "WESELKU_BANK";
		public final static String WESELKU_CASH_IN = "WESELKU_CASH_IN";
		public final static String WESELKU_CASH_OUT = "WESELKU_CASH_OUT";

		public final static String WESELKU_NEWS_BANK = "WESELKU_NEWS_BANK";
		public final static String WESELKU_NEWS_CASH_IN = "WESELKU_NEWS_CASH_IN";
		public final static String WESELKU_NEWS_CASH_OUT = "WESELKU_NEWS_CASH_OUT";

	}

	static public class Deposito {
		static public class ProductType {
			public final static String NON_ARO = "1";
			public final static String ARO = "2";
			public final static String ARO_PLUS = "3";

			public final static String[] LIST = { NON_ARO, ARO, ARO_PLUS };
		}

		static public class ProductTypeDescription {
			public final static String NON_ARO = "NonAro";
			public final static String ARO = "Aro";
			public final static String ARO_PLUS = "AroPlus";

		}
	}

	static public class Booklet {
		static public class RequestType {
			public final static int CHEQUE = 1;
			public final static int GIRO = 2;
		}

		static public class RequestTypeDescription {
			public final static String CHEQUE = "Cheque";
			public final static String GIRO = "Giro";
		}

		static public class AdminManageType {
			public final static String EXPORT = "export";
			public final static String REEXPORT = "reexport";
		}
	}

	static public class Weselku {
		static public class SmsPinDest {
			public final static int SENDER = 1;
			public final static int RECEIVER = 2;
		}

		static public class FlagRefund {
			public final static String REFUND = "R";
			public final static String CASH_OUT = "C";
		}
	}

	static public class Housekeeping {
		public final static String METADATA_KEY_GENERATE_DATE = "generate.date";
		public final static String METADATA_KEY_DAYS = "days";
		public final static String METADATA_KEY_DATE_TO_KEEP = "dateToKeep";
		public final static String METADATA_KEY_ACTOR = "actor";
		public final static String METADATA_KEY_QUERY_SELECT = "querySelect";
		public final static String METADATA_KEY_QUERY_DELETE = "queryDelete";
	}

	static public class Role {
		public final static long ROLE_ID_CUSTOMER = 1l;
		public final static long ROLE_ID_CUSTOMER_BY_BILLER = 7l;
		public static final Long ROLE_ID_CUSTOMER_WITH_MEMBER = 8l;
	}

	static public class SelularPrepaid {
		public final static String VOUCHER_TYPE_REGULAR = "R";
		public final static String VOUCHER_TYPE_SMS = "S";
		public final static String VOUCHER_TYPE_GPRS = "G";

		public final static String SELULAR_PREPAID = "SELULAR_PREPAID";

		public final static int CEK_STATUS_SUKSES = 1;
		public final static int CEK_STATUS_GAGAL = 0;
		public final static int CEK_STATUS_PROSES = 2;

		public final static String[] LIST = { VOUCHER_TYPE_REGULAR, VOUCHER_TYPE_SMS, VOUCHER_TYPE_GPRS };
	}

	static public class Option {
		public final static String NOT_SELECT = "-";
		public final static String SELECT_ALL = "*";
	}

	static public class PlnArea {
		public static final String TYPE_POSTPAID = "postpaid";
		public static final String TYPE_PREPAID = "prepaid";

		public static final String[] TYPE_LIST = { TYPE_POSTPAID, TYPE_PREPAID };
	}

	static public class MenuTree {
		public static final String TYPE_DISABLED = "disable";
	}

	static public class FormAction {
		public static final String ADD = "add";
		public static final String EDIT = "edit";
		public static final String DELETE = "delete";
	}

	static public class InternalEmailCategory {
		public static final String STATUS_AKTIF = "active";
		public static final String STATUS_NAKTIF = "nActive";
	}

	static public class Sms {
		public static final String TYPE_ADMIN_ALERT = "adminAlert";
		public static final String TYPE_CUST_ALERT = "custAlert";
		public static final String TYPE_CUST_TOKEN = "custToken";
		public static final String TYPE_OTHER = "other";
	}

	static public class BankBranch {
		public static final String TYPE_HEAD = "MAIN";
		public static final String TYPE_BRANCH = "BRANCH";
		public static final String TYPE_KAS = "KAS";

		public static final String[] TYPE_LIST = { TYPE_HEAD, TYPE_BRANCH, TYPE_KAS };
	}

	static public class AlertConfig {

		static public class FtpStatus {
			public static final String TYPE = "ftp";

			public static final String STATUS_UP = "up";
			public static final String STATUS_DOWN = "down";
		}

		static public class ItmIsoConnection {
			public static final String TYPE = "itmIsoConnection";

			public static final String STATUS_UP = "up";
			public static final String STATUS_DOWN = "down";
		}

		static public class SmsIsoConnection {
			public static final String TYPE = "smsIsoConnection";

			public static final String STATUS_UP = "up";
			public static final String STATUS_DOWN = "down";
		}

		static public class SmtpStatus {
			public static final String TYPE = "smtpStatus";

			public static final String STATUS_UP = "up";
			public static final String STATUS_DOWN = "down";
		}

		static public class WebPublicStatus {
			public static final String TYPE = "webPublicStatus";

			public static final String STATUS_UP = "up";
			public static final String STATUS_DOWN = "down";
		}

		static public class OverbookingSource {
			public static final String TYPE = "overbookingSource";

			public static final String STATUS_SUCCESS = Transaction.END_USER_STATUS_SUCCESS;
			public static final String STATUS_FAIL = Transaction.END_USER_STATUS_FAIL;
		}

		static public class OverbookingBeneficiary {
			public static final String TYPE = "overbookingBeneficiary";

			public static final String STATUS_SUCCESS = Transaction.END_USER_STATUS_SUCCESS;
			public static final String STATUS_FAIL = Transaction.END_USER_STATUS_FAIL;
		}

		static public class TrfNonAtmBersama {
			public static final String TYPE = "trfNonAtmBersama";

			public static final String STATUS_SUCCESS = Transaction.END_USER_STATUS_SUCCESS;
			public static final String STATUS_FAIL = Transaction.END_USER_STATUS_FAIL;
		}

		static public class TrfAtmBersama {
			public static final String TYPE = "trfAtmBersama";

			public static final String STATUS_SUCCESS = Transaction.END_USER_STATUS_SUCCESS;
			public static final String STATUS_FAIL = Transaction.END_USER_STATUS_FAIL;
		}

	}

	static public class ReffNumType {
		public static final int OB = 0;
		public static final int NON_ATM_BERSAMA = 1;
		public static final int ATM_BERSAMA = 2;
		public static final int ALTO = 24;
		public static final int BATCH_TRANSFER = 3;
		// public static final int TELKOM_FIX_PHONE = 4;
		// public static final int PLN = 5;
		public static final int SCHEDULED_TRANSACTION = 6;
		public static final int SELULAR_POSTPAID = 7;
		public static final int SELULAR_PREPAID = 8;
		// public static final int CREDIT_LOAN = 9;
		// public static final int INTERNET = 10;
		public static final int OTHER = 11;
		// public static final int EWALLET = 12;
		// public static final int PLN_PREPAID = 13;
		// public static final int WESEL_CASHIN = 14;
		// public static final int WESEL_CASHOUT = 15;
		// public static final int WESEL_CANCEL = 16;
		// public static final int CREDIT_CARD = 17;
		// public static final int TICKET_TRAIN = 18;
		// public static final int TICKET_PLANE = 19;
		// public static final int WATER = 20;
		// public static final int TV = 21;
		public static final int DEPOSITO = 22;
		public static final int SKNRTGS = 23;

		public static final int ACTIVATION = 100; // Transaction Activation,

		// including change,
		// Authorization, Admin entry
		// new user, Add New Account,
		// View report, Informasi
		// Rekening,

		public static final int CHANGE = 101; // Change Pin, Email Address,

		// VPin, Phone Number, Admin
		// change user, Change Add
		// Transfer List, Change Add Batch
		// Item, Save Batch, Change Delete
		// Transfer List, Change Delete
		// Batch Item

		public static final int USER_ACTIVITY = 102; // unblock, login, logout,

		// view report, account
		// information, account
		// maintenance, view
		// information
		public static final int PARAMETER = 103;
		public static final int TOKEN = 104; // hard token
		public static final int REQUEST = 105; // Request Hard Token
		public static final int REPLICATE = 106; // replicate
		public static final int RECALCULATE = 107; // recalculate

		public static final String BASE_KEY = "modelConstant.ReffNumType";

		public static final int[] AUDITLOG_LIST = {
		// OB,
		// NON_ATM_BERSAMA,
		// BATCH_TRANSFER,
		// PLN,
		// PLN_PREPAID,
		// TELKOM_FIX_PHONE,
		// INTERNET,
		// SELULAR_POSTPAID,
		// SELULAR_PREPAID,
		// OTHER,
		// ACTIVATION,
		// CHANGE,
		USER_ACTIVITY,
		// PARAMETER,
		// CREDIT_LOAN,
		// WESEL_CASHIN,
		// WESEL_CASHOUT,
		// CREDIT_CARD,
		// TICKET_TRAIN,
		// TICKET_PLANE,
		// WATER,
		// TV,
		// DEPOSITO,
		// SKNRTGS
		};

		public static final String[] AUDITLOG_MESSAGE_LIST = {
		// "OB",
		// "NON_ATM_BERSAMA",
		// "BATCH_TRANSFER",
		// "PLN",
		// "PLN_PREPAID",
		// "TELKOM_FIX_PHONE",
		// "INTERNET",
		// "SELULAR_POSTPAID",
		// "SELULAR_PREPAID",
		// "OTHER",
		// "ACTIVATION",
		// "CHANGE",
		"USER_ACTIVITY",
		// "PARAMETER",
		// "CREDIT_LOAN",
		// "WESEL_CASHIN",
		// "WESEL_CASHOUT",
		// "CREDIT_CARD",
		// "TICKET_TRAIN",
		// "TICKET_PLANE",
		// "WATER",
		// "TV",
		// "DEPOSITO",
		// "SKNRTGS"
		};
	}

	static public class AuditLogSubType {
		public static final String USER_ACTIVITY_LOGIN_SUCCESS = "ls";
		public static final String USER_ACTIVITY_LOGIN_FAIL = "lf";
		public static final String USER_ACTIVITY_LOGOUT = "lo";
		public static final String USER_ACTIVITY_UNBLOCK = "ub";

		public static final String CHANGE_ADD_TRANSFER_LIST = "addTrfList";

		/* Tambahan SubType */
		public static final String CHANGE_DELETE_TRANSFER_LIST = "delTrfList";
		public static final String CHANGE_ADD_BATCH_ITEM = "addbatchitem";
		public static final String CHANGE_DELETE_BATCH_ITEM = "delbatchitem";
		public static final String DELETE_SCHEDULE = "delsched";
		public static final String SELULAR_POSTPAID = "slpost";
		public static final String SELULAR_PREPAID = "slpre";
		public static final String BATCH_TRANSFER = "tbacth";
		public static final String USER_MANAGEMENT = "usermng";
		public static final String VIEW_REPORT = "vr";
		public static final String INITIAL_INVOICE 		= "initial";
		public static final String MANAGE_INVOICE 		= "manage";
		public static final String SEARCH_INVOICE 		= "search";
		public static final String SIMULASI_INVOICE 		= "simulasi";
		public static final String HISTORY_INVOICE 		= "history";
		public static final String USER 		= "user";
		public static final String UPLOAD 		= "upload";
		public static final String BI_RATE 		= "bi_rate";
		public static final String BHP_RATE 		= "bhp_rate";
		public static final String VIEW_FAIL_REPORT = "vfr";
		public static final String ACCOUNT_INFORMATION = "accinfo";
		public static final String ACCOUNT_MANAGEMENT = "accmng";
		public static final String VIEW_INFORMATION = "vinfo";
		public static final String DELETE_EMAIL = "delemail";
		public static final String SEND_EMAIL = "sendemail";
		public static final String DELETE_PREREGISTERED_PAYMENT = "delpayment";
		public static final String ROLE_MANAGEMENT = "rolemng";
		public static final String BANK_MANAGEMENT = "bankmng";
		public static final String ALERT_CONFIG_MANAGEMENT = "alertmng";
		public static final String GROUP_EMAIL_MANAGEMENT = "grpemailmng";
		public static final String EULA_MAINTENANCE = "eulamntc";
		public static final String TOKEN = "token";
		public static final String LOCAL_BRANCH_MANAGEMENT = "branchmng";
		public static final String MERCHANT_MANAGEMENT = "merchantmng";
		public static final String PLN_AREA_MANAGEMENT = "plnareamng";
		public static final String PRODUCT_MANAGEMENT = "productmng";
		public static final String PROVIDER_MANAGEMENT = "providermng";
		public static final String SELULAR_PREPAID_MANAGEMENT = "prepaidmng";
		public static final String CEK_GIRO = "cekgiro";
		public static final String UPDATE_SUSPECT_TRANSACTION = "suspect";
		public static final String USER_REGISTER_MANAGEMENT = "rgstrmng";
		public static final String PENDING_CHANGE_MANAGEMENT = "chaprv";
		public static final String TEST_SYSTEM_MONITOR = "testsystem";
		public static final String HOUSE_KEEPING = "housekpng";
		/* Tambahan SubType */
	}

	static public class User {
		public static final Long USER_ID_SYSTEM = new Long(0);

		public static final String STATUS_ADMIN = "A";
		public static final String STATUS_LOGIN_ONLY = "L";
		public static final String STATUS_INQUIRY_ONLY = "I";
		public static final String STATUS_TRANSACTION_ENABLE = "T";
		public static final String STATUS_BLOCKED = "B";
		public static final String STATUS_RESET = "R";

		public static final String BLOCK_WRONG_TOKEN = "token";
		public static final String BLOCK_WRONG_PASSWORD = "password";
		public static final String BLOCK_BY_ADMIN = "admin";

		public static final Long STATUS_PARENT = new Long(0);
	}

	static public class UserAccount {
		public static final String IB_STATUS_AKTIF = "I";
		public static final String IB_STATUS_NAKTIF = "N";

		public static final String ACCOUNT_TYPE_INDIVIDUAL = "Individu";
		public static final String ACCOUNT_TYPE_MERCHANT = "Merchant";

	}

	static public class Merchant {
		public static final String individualMerchantId = "IBS";
	}

	static public class SystemParameter {
		public static final String GROUP_TIMEOUT = "timeout";
		public static final String NAME_TIMEOUT_TRIGGER_ISO_SENDER = "timeout.triggerIsoSender";
		public static final String MULTI_CARD_NUMBER = "1";
		public static final String SINGLE_CARD_NUMBER = "0";

		public static final String GROUP_ISO = "iso";
		public static final String ISO_BIT41 = "iso.bit41";

	}

	static public class Transaction {
		public static final String BASE_KEY = "modelConstant.transaction";

		public static final String END_USER_STATUS_NOT_PROSESSED = "notProcessed";
		public static final String END_USER_STATUS_PROSESSED = "processed";
		public static final String END_USER_STATUS_SUCCESS = "POS:SUCCESS";
		public static final String END_USER_STATUS_FAIL = "fail";
		public static final String END_USER_STATUS_NOT_YET_PROSESSED = "schedule";
		public static final String END_USER_STATUS_TIMEOUT = "POS:TIMEOUT";

		public static final String[] END_USER_STATUS_LIST = { END_USER_STATUS_PROSESSED, END_USER_STATUS_SUCCESS, END_USER_STATUS_FAIL, END_USER_STATUS_TIMEOUT };

		public static final String GROUP_TIMEOUT = "timeout";
		public static final String NAME_TIMEOUT_TRIGGER_ISO_SENDER = "timeout.triggerIsoSender";

	}

	static public class Transaction2 {
		public static final String END_USER_STATUS_PROSESSED = "POS:PROCESS";
		public static final String END_USER_STATUS_SUCCESS = "POS:SUCCESS";
		public static final String END_USER_STATUS_FAIL = "POS:FAIL";
		public static final String END_USER_STATUS_TIMEOUT = "POS:TIMEOUT";

	}

	static public class TransactionLimit {
		public static final String LIMIT_TYPE_DAILY = "daily";
		public static final String LIMIT_TYPE_ONCE = "once";

		public static final String LIMIT_TYPE_ALL[] = { LIMIT_TYPE_DAILY, LIMIT_TYPE_ONCE };
	}

	static public class PreTransfer {
		public static final String TYPE_OVERBOOKING = "overBooking";
		public static final String TYPE_ATM_BERSAMA = "atmBersama";
		public static final String TYPE_NON_ATM_BERSAMA = "nonAtmBersama";
		public static final String TYPE_SKN_RTGS = "sknRtgs";
		public static final String TYPE_ALTO = "alto";
		public static final String TYPE_SETOR_ALTO = "setorAlto";
		public static final String TYPE_BATCH = "batch";
	}

	static public class PreTransferOtherBank {
	}

	static public class TransferNonAtmBersama {
		public static final String BASE_KEY = "modelConstant.transferNonAtmBersama";

		public static final String STATUS_PENDING_OVB = "pendingOvb";

		public static final String STATUS_NOT_HANDLED = "PENDING";

		public static final String STATUS_ON_PROGRESS = "ON_PROGRESS";

		public static final String STATUS_SUCCESS = "SUCCESS";

		public static final String STATUS_FAIL = "FAIL";

		public static final String[] STATUS_LIST = { STATUS_NOT_HANDLED, STATUS_ON_PROGRESS, STATUS_SUCCESS, STATUS_FAIL };

	}

	static public class PendingChange {
		public static final String BASE_KEY = "modelConstant.pendingChange";

		public static final String DATA_TYPE_TRANSACTION_LIMIT = "transactionLimit";
		public static final String DATA_TYPE_BANK = "bank";
		public static final String DATA_TYPE_BANK_BRANCH = "bankBranch";
		public static final String DATA_TYPE_CITY = "city";
		public static final String DATA_TYPE_ALERT_CONFIG = "alertConfig";
		public static final String DATA_TYPE_INTERNAL_EMAIL_CATEGORY = "internalEmailCategory";
		// public static final String DATA_TYPE_LOCAL_BRANCH = "localBranch";
		public static final String DATA_TYPE_PRODUCT = "product";
		public static final String DATA_TYPE_PROVIDER = "provider";
		public static final String DATA_TYPE_SELULAR_PREPAID = "selularPrepaid";
		public static final String DATA_TYPE_PLN_AREA = "plnArea";
		public static final String DATA_TYPE_PREFIX = "prefix";
		public static final String DATA_TYPE_CONTENT_PROVIDER = "contentProvider";

		public static final String ACTION_TYPE_ADD = "add";
		public static final String ACTION_TYPE_EDIT = "edit";
		public static final String ACTION_TYPE_DELETE = "delete";

		public static final String STATUS_INITIAL = "initial";
		public static final String STATUS_APPROVED = "approved";
		public static final String STATUS_REJECTED = "rejected";
		public static final String STATUS_ERROR = "error";

	}

	static public class InternalEmail {
		public static final String STATUS_READ = "read";
		public static final String STATUS_NREAD = "nRead";
		public static final String STATUS_REPLIED = "replied";
		public static final String[] STATUS_LIST = { STATUS_READ, STATUS_NREAD, STATUS_REPLIED };

		public static final String TYPE_INBOX = "inbox";
		public static final String TYPE_OUTBOX = "outbox";
		public static final String TYPE_OUTBOX_BROADCAST_ALL = "outboxBcAll";
		public static final String TYPE_OUTBOX_BROADCAST_MERCHANT = "outboxBcMerchant";
		public static final String TYPE_OUTBOX_BROADCAST_INDIVIDUAL = "outboxBcIndividual";
	}

	static public class Schedule {
		public static final String SCHEDULETYPE_EXACT_DATE = "E";
		public static final String SCHEDULETYPE_RECURRING_LONGDATE = "L";
		public static final String SCHEDULETYPE_RECURRING_DAY = "Y";
		public static final String SCHEDULETYPE_RECURRING_DATE = "D";
	}

	static public class TransactionExecution {
		static public String NOW = "EXECUTE_NOW";
		static public String AT_DATE = "EXECUTE_AT_DATE";
		static public String RECURRENCE = "EXECUTE_RECURRENCLY";
	}

	static public class UserToken {
		static public String REGISTER_NO = "N";
		static public String REGISTER_YES = "Y";

		static public String ACTIVATE_NO = "N";
		static public String ACTIVATE_YES = "Y";
	}

	static public class Token {
		static public String STATUS_IMPORT = "IMP"; // just imported, not

		// allocated yet

		static public String STATUS_READY = "REA"; // ready to use, after

		// allocation

		static public String STATUS_PAIR = "PAI"; // sudah dipasangkan dengan

		// Token request

		static public String STATUS_LINK = "LIN"; // linked to user

		static public String STATUS_ACTIVE = "ACT"; // token has been activated

		// by user

		static public String STATUS_BROKEN = "BBR"; // token being return

		// because it's broken

		static public String STATUS_CLOSE = "BCL"; // user no longer want to use

		// token and return the token

		static public String STATUS_BATTERY = "BBA"; // token being return

		// because battery is empty

		static public String STATUS_WRONG_LINK = "WRO"; // token dikembalikan

		// karena salah link
		// (human error) dan blm
		// dipakai sama sekali

		static public String STATUS_LOST = "LOS"; // token is lost

		static public String STATUS_VENDOR = "PHI"; // token returned to Vendor

		// (phintraco)

		static public String[] STATUS_LIST = { STATUS_IMPORT, STATUS_READY, STATUS_PAIR, STATUS_LINK, STATUS_ACTIVE, STATUS_BROKEN, STATUS_LOST, STATUS_CLOSE, STATUS_BATTERY, STATUS_VENDOR };

		static public String[] STATUS_LIST_BACK = { STATUS_BROKEN, STATUS_CLOSE, STATUS_BATTERY, STATUS_WRONG_LINK };

		static public String[] STATUS_LIST_UNLINK = { STATUS_BROKEN, STATUS_CLOSE, STATUS_BATTERY };

		/*
		 * note : token with status CLOSE and BATTERY will be returned to
		 * Phintraco via menu "kembalikan token ke phintraco"
		 */
	}

	static public class TokenRequest {
		static public String STATUS_REQUEST = "R"; // baru request
		static public String STATUS_READY = "E"; // diassign serial token
	}

	static public class RecurranceType {

		public static final String END_OF_MONTH_DATE = "99";

		static public String NOW = "EXECUTE_NOW";
		static public String AT_DATE = "EXECUTE_AT_DATE";
		static public String RECURRENCE = "EXECUTE_RECURRENCLY";

		static public String RECURRENCE_DATE = "REC_DATE";
		static public String RECURRENCE_END = "REC_END";
	}

	static public class Product {

		static public class Type {
			static public String PLN_PREPAID = "PLN_PREPAID";
			static public String PLN_POSTPAID = "PLN_POSTPAID";

			static public String TELKOM_FIX_PHONE = "TELKOM_FIX_PHONE";
			static public String E_WALLET = "E_WALLET";

			static public String SELULAR_PREPAID = "SELULAR_PREPAID";
			static public String SELULAR_POSTPAID = "SELULAR_POSTPAID";

			static public String CREDIT_LOAN = "CREDIT_LOAN";
			static public String INTERNET = "INTERNET";
			static public String OTHER = "OTHER";
			static public String CREDIT_CARD = "CREDIT_CARD";
			static public String TICKET_TRAIN = "TICKET_TRAIN";
			static public String TICKET_PLANE = "TICKET_PLANE";

			static public String WATER = "WATER";
			static public String TV = "TV";

			static public String[] LIST = { PLN_PREPAID, PLN_POSTPAID, TELKOM_FIX_PHONE, SELULAR_PREPAID, SELULAR_POSTPAID, CREDIT_LOAN, INTERNET, OTHER, CREDIT_CARD, TICKET_TRAIN, TICKET_PLANE, WATER, TV };
		}

		static public class Biller {
			static public String ARTA_JASA = "ARTA_JASA";
			static public String FINET = "FINET";
			static public String SERA = "SERA";
			static public String EURONET = "EURONET";
			static public String SYB = "SYB";
			static public String BRI_SYB = "BRI_SYB";
			static public String MITRACOM = "MITRACOM";
			static public String NUSAPRO = "NUSAPRO";
			static public String INDOSMART = "INDOSMART";

			static public String[] LIST = { ARTA_JASA, FINET, SERA, EURONET, SYB, BRI_SYB, MITRACOM, NUSAPRO };
			static public String[] LIST_PLN = { ARTA_JASA, SYB };

		}

		static public class Id {
			static public Long PLN_PREPAID_SYB = 1l;
			static public Long PLN_POSTPAID_SYB = 2l;
			static public Long PLN_POSTPAID_AJ = 3l;
			static public Long TELKOM_FIX_PHONE_FINET = 4l;
		}

	}

	static public class ParentChild {
		static public class Type {
			public static String PRODUCT_TYPE = "PRODUCT_TYPE";
			public static String PRODUCT_ISO = "PRODUCT_ISO";
			public static String PRODUCT_BILLER = "PRODUCT_BILLER";
		}
	}

	static public class PreRegisteredPayment {
		static public class Type {
			public static String PLN = "PLN";
			public static String PHONE_TELKOM = "PHONE_TELKOM";
			public static String INTERNET = "INTERNET";
			public static String SELULAR_PREPAID = "SELULAR_PREPAID";
			public static String SELULAR_POSTPAID = "SELULAR_POSTPAID";
			public static String OTHERS = "OTHERS";
			static public String CREDIT_LOAN = "CREDIT_LOAN";
			public static String E_WALLET = "E_WALLET";
			public static String CREDIT_CARD = "CREDIT_CARD";
			public static String TICKET_TRAIN = "TICKET_TRAIN";
			public static String TICKET_PLANE = "TICKET_PLANE";
			public static String WATER = "WATER";
			public static String TV = "TV";
		}
	}

	static public class SourcePayment {
		public static final String FROM_PAGE = "FROM_PAGE";
		public static final String FROM_DATA = "FROM_DATA";
	}

	static public class SourceAmount {
		public static final String AMOUNT = "AMOUNT";
		public static final String TOKEN1 = "TOKEN1";
		public static final String TOKEN2 = "TOKEN2";
	}

	// BER
	static public class SystemParameterConstant {

		static public String VELIS = "VELIS";

		static public class Velis {
			static public String FLAG = "ACTIVE_FLAG";
			static public String IP = "IP";
			static public String PORT_CLIENT = "PORT_CLIENT";
			static public String PORT_ADMIN = "PORT_ADMIN";

			static public String ADMIN_KEY_CLIENT = "KEY_ADMIN";
			static public String AGENT_KEY_CLIENT = "KEY_CLIENT";

			static public String AGENT_ID = "AGENT_ID";
			static public String ADMIN_ID = "ADMIN_ID";

			static public String CHANNEL = "CHANNEL";
			static public String BRANCH = "BRANCH";

			static public String TIMEOUT = "TIMEOUT";
		}

		static public String ADM = "ADMIN_CHG";

		static public class AdministrationCharge {
			static public String PLN = "PLN";
		}

		static public String EMAIL = "EMAIL_PROP";

		static public String USER_LIST = "LIST_USER";

		static public class UserList {
			static public String MAXIMUM_LIST_USER = "MAXIMUM_LIST_USER";
		}

		static public String SKN_RTGS = "SKN_RTGS";

		static public String SKN_ = "SKN_";

		static public String _RTGS = "_RTGS";

		static public String SWIFT = "SWIFT";

		static public class SknRtgs {
			static public String WINDOW_TIME_START = "START_TIME";
			static public String WINDOW_TIME_END = "END_TIME";
			static public String SKRT_OPEN_TRX = "SKRT_OPEN_TRX";

			static public String UPPER_LIMIT_SKN = "UPPER_LIMIT_SKN";
			static public String LOWER_LIMIT_SKN = "LOWER_LIMIT_SKN";

			static public String UPPER_LIMIT_RTGS = "UPPER_LIMIT_RTGS";
			static public String LOWER_LIMIT_RTGS = "LOWER_LIMIT_RTGS";
		}

		static public class Swift {
			static public String SWIFT_UPPER_LIMIT = "SWIFT_UPPER_LIMIT";
		}

		static public String SENDER_SCHEDULAR = "SND_SCHED";

		static public class SenderSchedular {
			static public String SCHEDULAR_SWITCH = "SCHED_SWITCH";

			static public String SCHEDULAR_ON = "ON";
			static public String SCHEDULAR_OFF = "OFF";
		}

		static public String REVERSAL_GROUP = "USE_REV";

		static public class nonReversalTrx {
			static public String REVERSAL_TRX = "USE_REV_GROUP";
		}

		static public String SYSTEM_OBSERVER_GROUP = "SCHEDMSG";

		static public class SystemObserver {
			static public String INFO_MESSAGE = "INFO";
			static public String TOKEN_MESSAGE = "TOKEN";
			static public String ACCBALANCE_MESSAGE = "ACCBALANCE";
			static public String PAYROLL_MESSAGE = "PAYROLL";
			static public String CURS_MESSAGE = "CURS";
			static public String SWIFT_MESSAGE = "SWIFT";
			static public String RTGS_MESSAGE = "RTGS";
			static public String SKN_MESSAGE = "SKN";
		}

		static public String ROUNDING_EXCEPTION = "ROUNDING_E";

		static public class roundingException {
			static public String ROUNDING_EXCEPTION = "ROUNDING_EXC";
		}

		public static final String EXCHANGE = "EXCHANGE_GROUP";

		static public class ExchangeGroup {
			public static final String NAME_DEFAULT_EXCHANGE = "DEFAULT_ER_GROUP";
		}
	}

	static public class CorpAccountFields {
		public static String CORP_ACC_ID = "CORP_ACC_ID";
		public static String TYPE = "TYPE";
		public static String ACCOUNT_NUMBER = "ACC_NUM";
		public static String CURR_CODE = "CURR_CODE";
		public static String CURR_ISO_CODE = "CURR_ISO_CODE";
		public static String ACC_TYPE = "ACC_TYPE";
		public static String ACC_STATUS = "ACC_STAT";
		public static String AVAILABLE_BALANCE = "BALANCE_AVAIL";
	}

	static public class TransferListFields {
		static public class TransferListType {
			public static String OVERBOOKING = "OVB";
			public static String SKN = "SKN";
			public static String RTGS = "RTGS";
			public static String LLG = "LLG";
			public static String SKNRTGS = "SRT";
			public static String SWIFT = "SWF";
		}

		static public class TransferListTypeDesc {
			public static String OVERBOOKING = "Transfer Antar Panin";
			public static String SKN = "Kliring/LLG";
			public static String RTGS = "RTGS";
			public static String SKNRTGS = "Kliring/RTGS";
			public static String SWIFT = "SWIFT";
		}

		static public class ServiceType {
			public static String FULL = "F";
			public static String FULL_NAME = "Full";
			public static String SHARE = "S";
			public static String SHARE_NAME = "Share";
		}

		static public class BicType {
			public static String BIC = "B";
			public static String FEDWIRE = "F";
		}

		public static String IDR = "IDR";
		public static String USD = "USD";
		public static String JPY = "JPY";
		public static String PANIN = "Panin";
		public static String PANIN_BANK_CODE = "019";
		public static String TRANSFER_INSERT_TYPE = "TRF_INSERT_TYPE";
		public static String TRANSFER_NEWS = "TRF_NEWS";

		public static String TRANSFER_LIST_ID = "TRF_LIST_ID";
		public static String TRANSFER_LIST_ACCOUNT = "TRF_LIST_ACC";
		public static String TRANSFER_LIST_CURRENCY = "TRF_LIST_CURR";
		public static String TRANSFER_LIST = "TRF_LIST";
		public static String TRANSACTION_TYPE = "TRANSACTION_TYPE";

		// swift
		public static String SWIFT_ACCOUNT_TO = "ACCOUNT_TO";
		public static String SWIFT_NAME = "NAME";
		public static String SWIFT_ADDRESS = "ADDRESS";
		public static String SWIFT_BIC = "BIC";
		public static String SWIFT_BIC_TYPE = "BIC_TYPE";
		public static String SWIFT_COUNTRY = "COUNTRY";
		public static String SWIFT_BANK = "BANK";
		public static String SWIFT_SERVICE = "SERVICE";
		public static String SWIFT_CHARGE = "CHARGE";
		public static String SWIFT_TTCHARGE = "TTCHARGE";
		public static String SWIFT_TTCHARGE_IN_CURR = "SWIFT_TTCHARGE_IN_CURR";
		public static String SWIFT_TTCHARGE_OUT_CURR = "SWIFT_TTCHARGE_OUT_CURR";
		public static String SWIFT_DEBET = "DEBET";
		public static String SWIFT_TRFLISTID = "TRFLISTID";
		public static String SWIFT_SERVICE_LABEL = "SERVICE_LABEL";
		public static String SWIFT_CURS = "CURS";
		public static String SWIFT_FULL = "FULL";
		public static String SWIFT_SHARE = "SHARE";
		public static String SWIFT_CURRCODE_ACC = "CURRCODE_ACC";
		public static String SWIFT_CURRCODE_TRLIST = "CURRCODE_TRLIST";
		public static String SWIFT_TRX_AMOUNT = "TRX_AMOUNT";

		// sknrt
		public static String SKN = "SKN";
		public static String RTGS = "RTGS";
		public static String RTGS_CODE = "RTGS_CODE";

		// overbooking inquiry response fields

		public static final String TO_ACCOUNT = "INQ_ACC_TO";
		public static final String TO_ACCOUNT_CURR_ISO = "INQ_ACC_CURR";
		public static final String TO_ACCOUNT_CURR = "INQ_ACC_CURR_MONEY";
		public static final String CREDIT_E = "INQ_CREDIT_E";
		public static final String DEBET_E = "INQ_DEBET_E";
		public static final String BUY_E = "INQ_BUY_E";
		public static final String SELL_E = "INQ_SELL_E";
		public static final String ORIG_AMOUNT = "VAL_ORIG";
		public static final String CONVERTED_AMOUNT = "VAL_CONVERTED";
		public static final String DEBET_AMOUNT = "DEBET_AMOUNT";
		public static final String RESIDENT_STAT = "INQ_RESIDENT_STAT";
		public static final String CITIZENSHIP_STATUS = "INQ_CITIZENSHIP_STAT";
		public static final String RATE_DIFFER_WARNING = "RATE_DIFFER_WARNING";
		public static final String MONEY_ROUNDING_WARNING = "MONEY_ROUNDING_WARNING";

		// sk rt
		public static String TYPE_TRANSFER = "TYPE_TRANSFER";
		public static String FIELD_TRANSFER = "FIELDS";
		public static final String BANK_NAME = "INQ_BANK_NAME";
		public static final String BRANCH_NAME = "INQ_BRANCH_NAME";
		public static final String CLEAR_CODE = "INQ_CLEAR_CODE";
		public static final String BEN_NAME = "INQ_BENE_NAME";
		public static final String BEN_ACC = "INQ_BENE_ACC";
		public static final String BANK_CODE = "INQ_BANK_CODE";
		public static final String BRANCH_CODE = "INQ_BRANCH_CODE";
		public static final String BANK_BRANCH = "INQ_BANK_BRANCH";
		public static final String NEWS = "INQ_NEWS";
		public static final String BANK_ADDRESS = "INQ_BANK_ADDRESS";
		public static final String BEN_ADDRESS = "INQ_BENE_ADDRESS";
		public static final String CITY = "CITY";
		public static final String AREA_CODE = "AREA_CODE";
		public static final String CURRENCY_CODE = "CURRENCY_CODE";
		public static final String BIC_TYPE = "BIC_TYPE";
		public static final String BIC_CODE = "BIC_CODE";
		public static final String COUNTRY_ID = "COUNTRY_ID";
		public static final String COUNTRY_NAME = "COUNTRY_NAME";
		public static final String SERVICE_TYPE = "SERVICE_TYPE";
		public static final String SERVICE_NAME = "SERVICE_NAME";
		public static final String BEN_SERVICE = "INQ_BENE_SERVICE";
		public static final String BEN_FEE = "INQ_BENE_FEE";
		public static final String TRANSFER_LIST_TYPE = "TRANSFER_LIST_TYPE";
		public static final String TRX_CODE = "TRX_CODE";
		public static final String NEXT_DAY_FLAG = "NEXT_DAY_FLAG";

		public static final String CREATED_DATE = "CREATED_DATE";
		public static final String CREATED_BY = "CREATED_BY";
		public static final String CHANGED_DATE = "CHANGED_DATE";
		public static final String CHANGED_BY = "CHANGED_BY";
		public static final String CORPORATE_ID = "CORPORATE_ID";
		public static final String CORPORATE = "CORPORATE";

		public static final String LIST_ID_DELETED = "TR_LIST_DELETED_LIST";
		public static final String LIST_BRANCH = "TR_LIST_BRANCH";
	}

	static public class AccountMailer {
		public static final String STATUS_NEW = "0";
		public static final String STATUS_PROCESSED = "1";
		public static final String STATUS_FAIL = "2";
	}

	static public final String TRX_REFF_NUM = "TRX_REFF_NUM_IIB";

	// Untuk audit Log
	static public final class AdministrationChangeType {
		public static final String APPROVAL = "approval";
		public static final String DIRECT = "direct";
		public static final String APPROVE = "Approve";
		public static final String REJECT = "Reject";
	}

	static public final class SystemMonitorTestType {
		public static final String ITM = "Koneksi ITM";
		public static final String SMTP = "Koneksi SMTP";
		public static final String FTP = "Koneksi FTP";
		public static final String SERA = "Koneksi SERA";
		public static final String MITRACOM = "Koneksi MITRACOM";
		public static final String EDC = "Koneksi EDC";
		public static final String SCHEDULER = "Modul Scheduler";
		public static final String WEBPUBLIC = "IB Nasabah";
		public static final String ALL = "Semua";
	}

	static public final class MetodeBhp {
		public static final String FLAT_RATE 		= "FR";
		public static final String VARIATED_RATE 	= "VR";
		public static final String CONVERSION 		= "C";
	}
	
	static public final class UserBHPActivity {
		public static final int SEARCH = 201;
		public static final int DRAFT_INVOICE = 202;
		public static final int SUBMIT_INVOICE = 203;
		public static final int VIEW_INVOICE = 204;
		public static final int EDIT_INVOICE = 205;
		public static final int SIMULASI = 206;
		public static final int INPUT = 207;
	}
}