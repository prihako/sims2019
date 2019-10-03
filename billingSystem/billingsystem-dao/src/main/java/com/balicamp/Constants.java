package com.balicamp;

import java.util.Date;

import org.joda.time.DateTime;

/**
 * Constant values used throughout the application.
 *
 * <p>
 * <a href="Constants.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public abstract class Constants {

	/**
	 * isi true pada saat development
	 */
	public static final boolean DEVELOPMENT_MODE = false;

	public static Date DEVELOPMENT_DATE = new DateTime(2008, 8, 24, 0, 0, 0, 0).toDate();

	// public static DateFormat concatDateFormat = new
	// SimpleDateFormat("yyyyMMddHHmmss");

	public static final Long SYSTEM_USER_ID = 0l;

	// ~ Static fields/initializers
	// =============================================

	public static final String[] AVAILABLE_LOCALE = { "in", "en" };

	/** The name of the ResourceBundle used in this application */
	public static final String BUNDLE_KEY = "ApplicationResources";

	/** The encryption algorithm key to be used for passwords */
	public static final String ENC_ALGORITHM = "algorithm";

	/** A flag to indicate if passwords should be encrypted */
	public static final String ENCRYPT_PASSWORD = "encryptPassword";

	/** File separator from System properties */
	public static final String FILE_SEP = System.getProperty("file.separator");

	/** User home from System properties */
	public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

	/** The name of the configuration hashmap stored in application scope. */
	public static final String CONFIG = "appConfig";

	/**
	 * Session scope attribute that holds the locale set by the user. By setting
	 * this key to the same one that Struts uses, we get synchronization in
	 * Struts w/o having to do extra work or have two session-level variables.
	 */
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";

	/**
	 * The request scope attribute under which an editable user form is stored
	 */
	public static final String USER_KEY = "userForm";

	/**
	 * The request scope attribute that holds the user list
	 */
	public static final String USER_LIST = "userList";

	/**
	 * The request scope attribute for indicating a newly-registered user
	 */
	public static final String REGISTERED = "registered";

	/**
	 * The name of the Administrator role, as specified in web.xml
	 */
	public static final String ADMIN_ROLE = "admin";

	/**
	 * The name of the User role, as specified in web.xml
	 */
	public static final String USER_ROLE = "user";

	/**
	 * The name of the user's role list, a request-scoped attribute when
	 * adding/editing a user.
	 */
	public static final String USER_ROLES = "userRoles";

	/**
	 * The name of the available roles list, a request-scoped attribute when
	 * adding/editing a user.
	 */
	public static final String AVAILABLE_ROLES = "availableRoles";

	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";

	/**
	 * Security Type Name
	 */
	public static final String SECURITY_TYPE_T = "Token";

	public static final String SECURITY_TYPE_S = "SMS Token";

	static public class SecurityToken {
		public static final String SECURITY_HARD_TOKEN = "T";

		public static final String SECURITY_SMS_TOKEN = "S";

		public static final String[] SelectAllSecurity = { SECURITY_TYPE_S, SECURITY_TYPE_T };

		public static final String[] SelectSecurityWithoutToken = { SECURITY_TYPE_S };
	}

	static public class UserTokenConstant {
		public static final String PENDING = "P";

		public static final String SENDING = "S";

		public static final String DELIVERY = "D";
	}

	/**
	 * application parameter
	 */
	static public class SystemParameter {

		static public class TimeOut {
			public static final String GROUP = "timeout";

			public static final String REGISTRATION_TRIGGER_JMS_MONITORING_PERIODE = "timeout.registration.triggerJms.monitoringPeriode";

			public static final String REGISTRATION_TRIGGER_JMS_PERIODE = "timeout.registration.triggerJms.periode";

			public static final String REGISTRATION_INQ_ACCOUNT_MUTATION_MONITORING_PERIODE = "timeout.registration.inqAccountMutation.monitoringPeriode";

			public static final String REGISTRATION_INQ_ACCOUNT_MUTATION_PERIODE = "timeout.registration.inqAccountMutation.periode";

			public static final String REGISTRATION_TRIGGER_JMS_COMMLINK_MONITORING_PERIODE = "timeout.registration.triggerJmsCommlink.monitoringPeriode";

			public static final String REGISTRATION_TRIGGER_JMS_COMMLINK_PERIODE = "timeout.registration.triggerJmsCommlink.periode";

			public static final String WAIT_ON_THREADPOOL_FULL = "timeout.waitOnThreadPoolFull";
		}

		static public class Page {
			public static final String GROUP = "page";

			public static final String TABLE_PAGESIZE = "page.tablePageSize";
		}

		static public class Account {
			public static final String GROUP = "account";

			public static final String MERCHANT_ACCOUNT_PATTERN = "account.merchantAccountPattern";

			public static final String TRANSFER_NONATMBERSAMA_DEST_ACCOUNT = "account.transferNonAtmBersama.destAccount";

			public static final String TRANSFER_SKN_DEST_ACCOUNT = "account.transferSkn.destAccount";

			public static final String TRANSFER_RTGS_DEST_ACCOUNT = "account.transferRtgs.destAccount";

			public static final String ADDITIONAL_CARDNUMBER = "account.cardNumber.additional";
		}

		static public class Mail {
			public static final String GROUP = "mail";

			public static final String HOST = "mail.host";

			public static final String USERNAME = "mail.username";

			public static final String PASSWORD = "mail.password";

			public static final String FROM = "mail.from";

			public static final String DESTINATION_TEST = "mail.destinationTest";

			public static final String INCOMING_NOTIFICATION = "mail.incomingNotification";

			public static final String USER_ADMIN_DOMAIN_LIST = "mail.userAdminDomainList";

			public static final String USER_ADMIN_DOMAIN_LIST_DEFAULT = "sigma.co.id";
		}

		static public class Ftp {
			public static final String GROUP = "ftp";

			public static final String REMOTE_FOLDER_PATH = "ftp.remoteFolderPath";

			public static final String REMOTE_RATE_FOLDER_PATH = "ftp.remoteFolderRatePath";

			public static final String REMOTE_FOLDER_PATH_SYNCHRONIZATION = "ftp.remoteFolderRatePath.synchronization";

			public static final String LOCAL_FOLDER_PATH = "ftp.localFolderPath";

			public static final String LOCAL_RATE_FOLDER_PATH = "ftp.localFolderRatePath";

			public static final String IP = "ftp.ip";

			public static final String PORT = "ftp.port";

			public static final String USERNAME = "ftp.userName";

			public static final String PASSWORD = "ftp.password";

			public static final String LAST_DOWNLOAD = "ftp.lastDownload";

			public static final String DAYS_TO_KEEP = "ftp.daysToKeep";

			public static final String ER_FILENAME = "ftp.filename.exchange";

			public static final String IR_FILENAME = "ftp.filename.interest";

			public static final String REMOTE_FOLDER_PATH_POIN = "ftp.remoterFolderPath.poin";

			public static final String LOCAL_FOLDER_PATH_POIN = "ftp.localFolderPath.poin";

		}

		static public class Scheduler {
			public static final String GROUP = "scheduler";

			public static final String ACCOUNT_STATEMENT_INTERVAL = "scheduler.interval";

			public static final String TRANSACTION_START_TIME = "scheduler.startTime";

			public static final String REPLICATION_CRON = "scheduler.replicationCron";

			public static final String TRANSACTION_CRON = "scheduler.transactionCron";

			public static final String CHECK_WEB_PUBLIC_STATUS_CRON = "scheduler.checkWebPublicStatusCron";

			public static final String CHECK_WEB_PUBLIC_STATUS_URL = "scheduler.checkWebPublicStatusUrl";

			public static final String EOD_CRON = "scheduler.eodCron";

			public static final String EOD_STARTTIME = "scheduler.startTimeEOD";

			public static final String EOD_ENDTIME = "scheduler.endTimeEOD";

			public static final String SYNCHRONIZATION_CRON = "scheduler.syncrhonizationCron";
		}

		static public class Security {
			public static final String GROUP = "security";

			public static final String PASSWORD_LENGTH = "security.passwordLength";

			public static final String USERNAME_NAME_LENGTH = "security.userName.nameLength";

			public static final String USERNAME_TOTAL_LENGTH = "security.userName.totalLength";

			public static final String ITM_PIN_ENCODER_KEY = "security.itm.pinEncoderKey";

			public static final String VPIN_MOD63_PATTERN = "security.vPin.pattern63";

			public static final String VPIN_3DES_SEEDKEY = "security.vPin.seedKey";

			public static final String VPIN_MOD63_MASTERKEY = "security.vPin.masterKey";

			public static final String SOFTTOKEN_LIVETIME = "security.softToken.liveTime";

			public static final String SOFTTOKEN_LENGTH = "security.softToken.length";

			public static final String SOFTTOKEN_ACTIVATION_LIVETIME = "security.softToken.activation.liveTime";

			public static final String LOGINFAIL_BLOCK_TIMES = "security.loginFail.block.times";

			public static final String LOGINFAIL_BLOCK_INTERVAL = "security.loginFail.block.interval";

			public static final String SOFTBLOCK_RESET_TIMES = "security.softBlock.reset.times";

			public static final String INITIAL_USER_ROLE = "security.initialUserRole";

			public static final String SESION_TIMEOUT_PERIODE = "security.sessionTimeoutPeriode";

			public static final String ADMIN_PASS_ALPHANUMERIC = "security.admin.password.alphanumeric";

			public static final String ADMIN_PASS_MAX_LENGTH = "security.admin.password.maxLength";

			public static final String ADMIN_PASS_MIN_LENGTH = "security.admin.password.minLength";

			public static final String ADMIN_PASS_EXPIRED_DAYS = "security.admin.password.expiration.days";

			public static final String ADMIN_PASS_EXPIRED_MODE = "security.admin.password.expiration.mode";

			public static final String ADMIN_PASS_HISTORY_COUNT = "security.admin.password.history.count";

			public static final String ADMIN_PASS_HISTORY_MODE = "security.admin.password.history.mode";
		}

		static public class ISO {
			public static final String GROUP = "iso";

			public static final String ISO_BIT18 = "iso.bit18";

			public static final String ISO_BIT41 = "iso.bit41";

			public static final String ISO_BIT47 = "iso.bit47";

			public static final String ISO_DEFAULT_CURRENCY_CODE = "iso.defaultCurrencyCode";

			public static final String ISO_ATMB_BIT41 = "iso.atmb.bit41";

			public static final String ISO_ATMB_ADDRESS = "iso.atmb.address";

			public static final String ISO_ATMB_DSPRV = "iso.atmb.dsprv";

			public static final String ISO_ATMB_DSSTE = "iso.atmb.dsste";

			public static final String ISO_DIRECT_REPRINT = "iso.directReprint";
		}

		static public class HouseKeeping {
			public static final String GROUP = "houseKeeping";

			public static final String LAST_EXECUTED = "houseKeeping.lastExecuted";

			public static final String ACCOUNT_MUTATION_MAX_DAY_INTERVAL = "houseKeeping.accountMutationMaxDayInterval";

			public static final int ACCOUNT_MUTATION_MAX_DAY_INTERVAL_DEFAULT = 30;

			public static final String IB_TRANSACTION_HISTORY_MAX_DAY_INTERVAL = "houseKeeping.ibTransactionHistoryMaxDayInterval";

			public static final int IB_TRANSACTION_HISTORY_MAX_DAY_INTERVAL_DEFAULT = 30;

			public static final String DIR_HOUSE_KEEPING = "houseKeeping.dirHouseKeeping";

			public static final String DIR_TMP_UNZIP = "houseKeeping.dirTmpUnzip";

			public static final String DIR_HOUSE_KEEPING_POIN = "houseKeeping.dirHouseKeepingPoin";

			public static final String COMMLINK_DAY = "houseKeeping.commlinkDay";

			public static final int COMMLINK_DAY_DEFAULT = 5;
		}

		static public class Portal {
			public static final String GROUP = "portal";

			public static final String URL_REQUEST_SESSION = "portal.urlRequestSession";

			public static final String URL_MAIN = "portal.urlMain";

			public static final String URL_HOME = "portal.urlHome";
		}

		static public class Other {
			public static final String GROUP = "other";

			public static final String EULA = "other.eula";

			public static final String TRF_NON_ATM_BERSAMA_LIMIT_TIME = "other.trfNonAtmBersama.limitTime";

			public static final String TRF_NON_ATM_BERSAMA_LIMIT_TIME_MESSAGE = "other.trfNonAtmBersama.limitTime.message";

			public static final String TRF_SKN_START_TIME = "other.skn.start";

			public static final String TRF_SKN_STOP_TIME = "other.skn.stop";

			public static final String TRF_RTGS_START_TIME = "other.rtgs.start";

			public static final String TRF_RTGS_STOP_TIME = "other.rtgs.stop";

			public static final String TRF_LIMIT_TIME_MESSAGE = "other.limitTime.message";

			public static final String PHONE_AREACODE_3DIGIT = "other.phoneAreaCode.3digit";

			public static final String TMP_FILE_UPLOAD_DIR = "other.tmpFileUploadDir";

			public static final String WESEL_MSISDN_PREFIX = "other.wesel.msisdnPrefix";

			public static final String REFERENCE_NUMBER = "other.referenceNumber";

			public static final String LIMITATION_BASED_ON = "other.limit.basedOn";

			public static final String CARD_NUMBER = "other.cardNumber";
		}

		static public class Configuration {
			public static final String CONTEXT_PATH_REAL_DIR = "contextPathRealDir";

			public static final String CONTEXT_BASE_URL = "contextBaseUrl";

			public static final String CONTEXT_TYPE = "contextType";

			static public class ContextType {
				public static final String ALL = "all";

				public static final String PUBLIC = "public";

				public static final String ADMIN = "admin";
			}

		}

		static public class Sms {
			public static final String GROUP = "sms";

			public static final String PREFIX = "sms.prefix";

			public static final String PREFIX_SERA = "sms.prefix.sera";

			public static final String PREFIX_MITRACOM = "sms.prefix.mitracom";
		}

		static public class Token {
			public static final String GROUP = "token";

			public static final String ADMIN_IP = "token.adminIp";

			public static final String AGENT_IP = "token.agentIp";

			public static final String TIMEOUT = "token.timeout";

			public static final String ADMIN_ID = "token.adminId";

			public static final String ADMIN_KEY = "token.adminKey";

			public static final String AGENT_ID = "token.agentId";

			public static final String AGENT_KEY = "token.agentKey";

			public static final String ADMIN_PORT = "token.adminPort";

			public static final String AGENT_PORT = "token.agentPort";

			public static final String CHANNEL = "token.channel";

			public static final String CUSTOMER_GROUP = "token.customerGroup";
		}

		static public class Pln {
			public static final String GROUP = "pln";

			public static final String PREPAID_MIN_AMOUNT = "pln.prepaid.minAmount";

			public static final String PREPAID_MAX_AMOUNT = "pln.prepaid.maxAmount";

			public static final String PREPAID_CODE_SEPARATOR = "pln.prepaid.codeSeparator";

			public static final String PREPAID_BUTTOM_MESSAGE_1 = "pln.prepaid.buttomMessage.1";

			public static final String PREPAID_BUTTOM_MESSAGE_2 = "pln.prepaid.buttomMessage.2";

			public static final String PREPAID_PREFIX_DEFAULD = "pln.prepaid.prefix.defauld";

			public static final String REGION_PREFIX = "pln.regionPrefix";
		}

		static public class Edc {
			public static final String GROUP = "edc";

			public static final String ISO_BIT_22 = "edc.iso.bit22";

			public static final String ISO_BIT_24 = "edc.iso.bit24";

			public static final String ISO_BIT_35 = "edc.iso.bit35";

			public static final String ISO_BIT_41 = "edc.iso.bit41";

			public static final String ISO_BIT_42 = "edc.iso.bit42";

		}

	}

	/*
	 * End of Application Parameter
	 *
	 *
	 * /** HTTP_SESSION
	 */
	static public class HttpSessionAttribute {
		public static final String TOKEN_AUTH = "token.auth";

		public static final String TOKEN_AUTH_FAIL_COUNTER = "token.auth.fail.counter";

		public static final String SOFTTOKEN_ACTIVATION = "softToken.activation.auth";

		public static final String LAST_LOGIN_DATE = "lastLoginDate";

		public static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";

		public static final String AUTH_MAIN_PAGE = "auth.mainPage";

		public static final String WESEL_PRINT_DATA = "wesel.print.data";

		public static final String PRINT_DATA = "print.data";

		public static final String PRINT_TRANSACTION = "print.transaction";

	}

	static public class MethodResponse {
		static public class TransactionLimit {
			public static final String BASE_KEY_CODE = "methodResponse.transactionLimit.code";

			public static final String CODE_VALID = "valid";

			public static final String CODE_MORE_THAN_MAXIMUM = "moreThanMaximum";

			public static final String CODE_LESS_THAN_MINIMUM = "lessThanMinimum";
		}

	}

	static public class Log {
		public static final String ISO_LOG = "ISO_LOG";

		public static final String ISO_LOG_SERA = "ISO_LOG_SERA";

		public static final String SECURITY_LOG = "SECURITY_LOG";
	}

	static public class InputSchedule {
		public final static int END_MONTH 	= 32;
	}

	static public class FileLocation {
		public final static String FTP 		= "FTP";

		public final static String LOCAL 	= "LOCAL";

		public final static String[] LIST 	= { FTP, LOCAL };
	}

	static public class Status {
		public final static String DRAFT 		= "D";
		public final static String SUBMIT 		= "S";
		
		public final static String SUBMITTED	= "1";

		public final static String YES 			= "1";
		public final static String NO 			= "0";

		public final static String PAID			= "P";
		
		public final static String CANCELLED		= "3";
		
		public final static String EXPIRED			= "2";
		
		public final static String UNSUBMITTED		= "0";
	}
	
	static public class InvoiceStatus{
		public final static String PAID = "P";
		public final static String UNPAID = "U";
		public final static String DRAFT = "D";
		public final static String BAD_DEBT = "BD";
		public final static String CANCEL = "C";
	}
	
	static public class InvoiceType{
		public final static int POKOK = 1;
		public final static int DENDA = 2;
		public final static int POKOK_BG = 3;
		public final static int POKOK_SISA_BG = 4;
	}

}
