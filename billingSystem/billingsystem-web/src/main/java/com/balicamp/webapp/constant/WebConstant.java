package com.balicamp.webapp.constant;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @version $Id: WebConstant.java 337 2013-02-19 09:16:01Z bagus.sugitayasa $
 */
public class WebConstant {

	/** The name of the ResourceBundle used in this application */
	public static final String BUNDLE_KEY = "ApplicationResources";

	public static final String WEB_ROOT = "/";

	/** The name of the configuration hashmap stored in application scope. */
	public static final String CONFIG = "appConfig";

	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";

	/** The encryption algorithm key to be used for passwords */
	public static final String ENC_ALGORITHM = "algorithm";

	/** A flag to indicate if passwords should be encrypted */
	public static final String ENCRYPT_PASSWORD = "encryptPassword";

	public static final String CONFIRM_PASSWORD_CHANGE = "Anda masih menggunakan password default,\r\n "
			+ "terkait alasan keamanan disarankan untuk mengubah password default Anda,\r\n "
			+ "silahkan gunakan link berikut untuk mengganti password.";

	public static String extractLocalizedMessage(String messageKey, Locale locale) {
		return ResourceBundle.getBundle(WebConstant.BUNDLE_KEY, locale).getString(messageKey);
	}

	/** Set editor state to EDIT Mode*/
	public static final String EDITOR_STATE_EDIT = "EDIT";

	/** Set editor state to DELETE Mode*/
	public static final String EDITOR_STATE_DELETE = "DELETE";

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
}
