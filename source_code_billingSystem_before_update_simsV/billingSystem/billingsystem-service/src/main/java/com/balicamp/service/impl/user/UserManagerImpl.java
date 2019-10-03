package com.balicamp.service.impl.user;

import static com.balicamp.Constants.SystemParameter.Security.GROUP;
import static com.balicamp.Constants.SystemParameter.Security.INITIAL_USER_ROLE;
import static com.balicamp.Constants.SystemParameter.Security.LOGINFAIL_BLOCK_INTERVAL;
import static com.balicamp.Constants.SystemParameter.Security.LOGINFAIL_BLOCK_TIMES;
import static com.balicamp.Constants.SystemParameter.Security.SOFTBLOCK_RESET_TIMES;
import static com.balicamp.Constants.SystemParameter.Security.USERNAME_NAME_LENGTH;
import static com.balicamp.Constants.SystemParameter.Security.USERNAME_TOTAL_LENGTH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.Constants;
import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.user.UserCardNumberDao;
import com.balicamp.dao.user.UserDao;
import com.balicamp.dao.user.UserRoleDao;
import com.balicamp.model.common.MethodResponse;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserCardNumber;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.model.user.UserGroupDisplay;
import com.balicamp.model.user.UserProperties;
import com.balicamp.model.user.UserRegister;
import com.balicamp.model.user.UserRole;
import com.balicamp.model.user.UserRoleId;
import com.balicamp.model.user.UserRoleSingle;
import com.balicamp.model.user.UserSingle;
import com.balicamp.service.common.ObjectSerializerManager;
import com.balicamp.service.function.FunctionRoleManager;
import com.balicamp.service.impl.AbstractManager;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.util.SecurityContextUtil;
import com.balicamp.util.SecurityLogUtil;

/**
 * @version $Id: UserManagerImpl.java 368 2013-03-08 04:09:59Z wayan.agustina $
 */
@Service("userManager")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class UserManagerImpl extends AbstractManager implements UserManager, InitializingBean {

	private static final long serialVersionUID = 1L;

	private static final Log securityLog = LogFactory.getLog(Constants.Log.SECURITY_LOG);

	private static final int RANDOM_PASSWORD_LENGTH = 10;

	private static final int ACTIVATION_CODE_LENGTH = 12;

	private static final Log log = LogFactory.getLog(UserManagerImpl.class);

	private UserDao userDao;

	private UserCardNumberDao userCardNumberDao;

	private UserRoleDao userRoleDao;

	private SaltSource saltSource;

	private PasswordEncoder passwordEncoder;

	private SystemParameterManager systemParameterManager;

	private ObjectSerializerManager objectSerializerManager;

	private AuditLogManager auditLogManager;

	private RoleManager roleManager;

	private FunctionRoleManager functionRoleManager;

	private int loginFailBlockTimes;

	private int loginFailBlockInterval;

	private int softBlockResetTimes;

	private List<Role> initialRole;

	@Autowired
	public UserManagerImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	@Autowired
	public void setSystemParameterManager(SystemParameterManager systemParameterManager) {
		this.systemParameterManager = systemParameterManager;
	}

	@Autowired
	public void setObjectSerializerManager(ObjectSerializerManager objectSerializerManager) {
		this.objectSerializerManager = objectSerializerManager;
	}

	@Autowired
	public void setAuditLogManager(AuditLogManager auditLogManager) {
		this.auditLogManager = auditLogManager;
	}

	@Autowired
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Autowired
	public void setUserCardNumberDao(UserCardNumberDao userCardNumberDao) {
		this.userCardNumberDao = userCardNumberDao;
	}

	@Autowired
	public void setFunctionRoleManager(FunctionRoleManager functionRoleManager) {
		this.functionRoleManager = functionRoleManager;
	}

	/*
	 * @Autowired
	 * public void setMessageSourceWrapper(MessageSourceWrapper
	 * messageSourceWrapper) {
	 * this.messageSourceWrapper = messageSourceWrapper;
	 * }
	 */

	public void afterPropertiesSet() throws Exception {
		loadConfigurations();
		setInitialRole();
	}

	private void loadConfigurations() {

		loginFailBlockTimes = systemParameterManager
				.getIntValue(new SystemParameterId(GROUP, LOGINFAIL_BLOCK_TIMES), 3);
		loginFailBlockInterval = systemParameterManager.getIntValue(new SystemParameterId(GROUP,
				LOGINFAIL_BLOCK_INTERVAL), 300);
		softBlockResetTimes = systemParameterManager
				.getIntValue(new SystemParameterId(GROUP, SOFTBLOCK_RESET_TIMES), 2);

	}

	private void setInitialRole() {
		String initialUserRoleString = systemParameterManager.getStringValue(new SystemParameterId(GROUP,
				INITIAL_USER_ROLE), "customer");
		String initialUserRoleSplitted[] = initialUserRoleString.split(";");

		initialRole = new ArrayList<Role>();
		for (String roleName : initialUserRoleSplitted) {
			Role role = roleManager.findByName(roleName);
			if (role != null) {
				initialRole.add(role);
			}
		}
	}

	public void softReset() {
		loadConfigurations();
		setInitialRole();
	}

	private User findUserFromDatabase(String username) {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("user '" + username + "' not found...");
		}

		// eager fetch
		user.eagerFetch();

		// recheck user role for merchant
		// boolean merchant = false;

		// check whether given user is merchant or not
		/*
		 * for (UserAccount account : user.getActiveUserAccounts()) {
		 * if (merchantManager.findByAccountNo(account.getAccountNo()) != null)
		 * {
		 * merchant = true;
		 * break;
		 * }
		 * }
		 */

		/*
		 * if (merchant) {
		 * user.getRoleSet();
		 * }
		 */

		return user;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		username = username.toUpperCase();

		User user = findUserFromDatabase(username);

		// authorities
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.addAll(functionRoleManager.getFunctions(user.getRoleSet()));

		list.add(new GrantedAuthority() {
			private static final long serialVersionUID = 6547673651830906736L;

			public String getAuthority() {
				return "user";
			}
		});

		user.setAuthorities(list.toArray(new GrantedAuthority[0]));

		// customer type
		/*
		 * if (user.getCustomerType() == null) {
		 * user.setCustomerType(userAccountManager.findCustomerType(user));
		 * }
		 */

		return user;
	}

	public User findByCardnumber(String cardNumber) {
		return userDao.findByCriteria(Arrays.<Criterion> asList(Restrictions.eq("cardNumber", cardNumber)), null, -1,
				-1).get(0);
	}

	public User findByUserName(String userName) {
		/*
		 * List<Criterion> criterionList = new ArrayList<Criterion>();
		 * criterionList.add(Restrictions.ilike("userName", userName));
		 * return userDao.findByCriteria(criterionList, null, -1, -1).get(0);
		 */
		return userDao.findByUsername(userName.toUpperCase());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public User addNew(String fullName, Date dob, String cardNumber, String plainPassword, List<String> accountList) {
		User user = new User();

		user.setUserFullName(fullName);
		if (dob != null) {
			user.setBirthDate(dob);
		}

		user.setCardNumber(cardNumber);
		user.setUserParentId(new Long(0));
		user.setStatus(ModelConstant.User.STATUS_LOGIN_ONLY);

		return addNew(user, plainPassword, accountList);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public User addNew(User user, String plainPassword, List<String> accountList) {
		String username = generateUserName(user.getUserFullName(), accountList);

		if (findByUserName(username) != null) {
			return null;
		}

		CommonUtil.interceptAuditModel(user, ModelConstant.User.USER_ID_SYSTEM);

		// save user
		user.setUserName(username);
		user.setPassword(passwordEncoder.encodePassword(plainPassword, saltSource.getSalt(user)));
		user.setEnabled(true);
		user.setMustChangePassword(false);

		userDao.saveOrUpdate(user);

		// save user role
		if (initialRole != null) {
			for (Role role : initialRole) {
				UserRole ur = new UserRole(new UserRoleId(user, role));

				userRoleDao.saveOrUpdate(ur);
				user.getUserRoleSet().add(ur);
			}
		}

		userDao.saveOrUpdate(user);

		String cardNumber = user.getCardNumber();

		// save user account
		/*
		 * for (String accountNo : accountList) {
		 * UserAccount userAccount = new UserAccount();
		 * userAccount.setUserId(user);
		 * userAccount.setAccountNo(accountNo);
		 * userAccount.setAccountName(user.getUserFullName());
		 * userAccount.setPrimaryAccount(Boolean.TRUE);
		 * userAccount.setAccountType("00");
		 * userAccount.setAccountCurrency("IDR");
		 * userAccount.setCardNumber(cardNumber);
		 * userAccount.setIbStatus(ModelConstant.UserAccount.IB_STATUS_AKTIF);
		 * 
		 * CommonUtil.interceptAuditModel(userAccount,
		 * ModelConstant.User.USER_ID_SYSTEM);
		 * userAccountDao.save(userAccount);
		 * }
		 */

		// save card number
		UserCardNumber ucn = new UserCardNumber();
		ucn.setUser(user);
		ucn.setCardNumber(cardNumber);
		ucn.setCreatedDate(new Date());

		userCardNumberDao.save(ucn);

		return user;
	}

	public String generateUserName(String fullName, List<String> accountList) {
		String userName = null;
		String trimName = removeOtherCharacter(fullName.toUpperCase());

		int userNameNameLength = systemParameterManager.getIntValue(new SystemParameterId(GROUP, USERNAME_NAME_LENGTH),
				8);
		int userNameTotalLength = systemParameterManager.getIntValue(
				new SystemParameterId(GROUP, USERNAME_TOTAL_LENGTH), 12);

		if (trimName.length() > userNameNameLength) {
			trimName = trimName.substring(0, userNameNameLength);
		}

		int tailLength = userNameTotalLength - trimName.length();
		int indexAccount = 0;
		int randomNumberLength = 0;

		for (int seq = 0; seq < 1000; seq++) {

			String tailAccount = accountList.get(indexAccount);
			if (tailAccount.length() > tailLength) {
				tailAccount = tailAccount.substring(tailAccount.length() - tailLength, tailAccount.length());
			}

			if (randomNumberLength > 0) {
				Random random = new Random();
				String randomString = String.valueOf(random.nextInt(99));

				tailAccount = randomString + tailAccount.substring(randomString.length());
			}

			StringBuilder tmpUserName = new StringBuilder().append(trimName).append(tailAccount);

			if (userDao.isUniqueAvailableByCriteria(null,
					Arrays.asList(Restrictions.ilike("userName", tmpUserName.toString())))) {
				// success
				userName = tmpUserName.toString().toLowerCase();
				break;
			}

			// not success try other
			if (accountList.size() - 1 > indexAccount) {
				indexAccount++;
			} else {
				randomNumberLength = 2;
			}

		}

		return userName.toUpperCase();
	}

	public String generateUserName(String fullName, Date birthDate) {
		String userName = null;
		String firstName = fullName.split(" ")[0];

		Calendar birthDateCalendar = Calendar.getInstance();
		birthDateCalendar.setTime(birthDate);

		String birthDateDate = String.valueOf(birthDateCalendar.get(Calendar.DAY_OF_MONTH));
		String birthDateMonth = String.valueOf(birthDateCalendar.get(Calendar.MONTH) + 1);

		for (int seq = 0; seq < 1000; seq++) {

			StringBuilder tmpUserName = new StringBuilder().append(firstName).append(birthDateDate)
					.append(birthDateMonth);

			if (seq > 0)
				tmpUserName.append(seq);

			if (userDao.isUniqueAvailableByCriteria(null,
					Arrays.asList(Restrictions.ilike("userName", tmpUserName.toString())))) {
				userName = tmpUserName.toString();
				break;
			}
		}
		return userName;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void savePassword(User user, String plainPassword) {
		user.setPassword(encodePassword(user, plainPassword));
		user.setChangePasswordDate(new Date());
		user.setAccountLocked(Boolean.FALSE);

		userDao.updatePassword(user);
	}

	public String encodePassword(User user, String plainPassword) {
		String encodedPassword = plainPassword;
		if (passwordEncoder != null && saltSource != null) {
			// encrypt password
			Object salt = saltSource.getSalt(user);
			// encodedPassword =
			// passwordEncoder.encodePassword(plainPassword,salt);
			encodedPassword = passwordEncoder.encodePassword(plainPassword, salt);
		}
		return encodedPassword;
	}

	public boolean isPasswordValid(User user, String plainPassword) {
		Object salt = saltSource.getSalt(user);
		return passwordEncoder.isPasswordValid(user.getPassword(), plainPassword, salt);
	}

	public void loadUserProperties(User user, boolean initIfNull) {
		if (initIfNull && user.getUserProperties() == null) {
			UserProperties userPropertiesObject = new UserProperties();
			user.setUserPropertiesObject(userPropertiesObject);
			try {
				user.setUserProperties(objectSerializerManager.parseToString(userPropertiesObject));
			} catch (IOException e) {
				log.error("fail serialized UserProperties ", e);
			}
		}

		if (user.getUserProperties() != null && user.getUserPropertiesObject() == null) {
			try {
				user.setUserPropertiesObject((UserProperties) objectSerializerManager.parseToObject(user
						.getUserProperties()));
			} catch (Exception e) {
				log.error("fail deserialized UserProperties ", e);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveUserProperties(User user, boolean initIfNull, boolean saveToDb) {

		// init if null
		if (initIfNull && user.getUserProperties() == null && user.getUserPropertiesObject() == null) {
			user.setUserPropertiesObject(new UserProperties());
		}

		if (user.getUserPropertiesObject() == null && user.getUserProperties() != null) { // NOPMD
			// existing serialized object will not set to null
		} else if (user.getUserPropertiesObject() != null) {
			try {
				// serialized userproperties
				user.setUserProperties(objectSerializerManager.parseToString(user.getUserPropertiesObject()));

				// save to db
				if (saveToDb)
					userDao.saveOrUpdate(user);
			} catch (IOException e) {
				log.error("fail serialized UserProperties ", e);
			}
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public MethodResponse<?> unblockUser(User user) {
		MethodResponse<?> methodResponse = new MethodResponse<Object>();

		// unblock
		loadUserProperties(user, false);
		String previousStatus = ModelConstant.User.STATUS_INQUIRY_ONLY;
		if (user.getUserPropertiesObject() != null && user.getUserPropertiesObject().getPreviousStatus() != null) {
			previousStatus = user.getUserPropertiesObject().getPreviousStatus();
			user.getUserPropertiesObject().setBlockReason(null);
		}
		user.setStatus(previousStatus);
		user.setAccountLocked(false);
		userDao.saveOrUpdate(user);

		// save auditlog
		Long createdBy = SecurityContextUtil.getCurrentUserId();
		if (createdBy == null) {
			createdBy = user.getId();
		}

		/*
		 * auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
		 * ModelConstant.AuditLogSubType.USER_ACTIVITY_UNBLOCK,
		 * "auditLog.info.format.unBlockUser",
		 * new Object[] { user.getId(), previousStatus }, createdBy, new
		 * Date());
		 */

		return methodResponse;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void logLoginSuccess(User user, Date lastLoginDate, String ipAddress) {
		// set default if null
		if (lastLoginDate != null)
			user.setLastLogin(lastLoginDate);
		else
			user.setLastLogin(new Date());

		// reset counter fail login
		loadUserProperties(user, true);
		if (user.getUserPropertiesObject() != null) {
			user.getUserPropertiesObject().setLoginFailDateList(null);
			user.getUserPropertiesObject().setSoftBlockDateList(null);
			saveUserProperties(user, false, false);
		}

		// save to db
		userDao.saveOrUpdate(user);

		// save audit log
		/*
		 * auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
		 * ModelConstant.AuditLogSubType.USER_ACTIVITY_LOGIN_SUCCESS,
		 * "auditLog.info.format.loginSuccess",
		 * new Object[] { ipAddress }, user.getId(), new Date());
		 */
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String logLoginFail(User user, Date failLoginDate, String ipAddress) {
		securityLog.info(SecurityLogUtil.generateSecurityLogMessage("gagal login password tidak cocok"));
		loginFailBlockInterval = Integer.valueOf(systemParameterManager.findByParamName(LOGINFAIL_BLOCK_INTERVAL)
				.getParamValue());
		loginFailBlockTimes = Integer.valueOf(systemParameterManager.findByParamName(LOGINFAIL_BLOCK_TIMES)
				.getParamValue());

		// loginFailBlockInterval = 300;
		// loginFailBlockTimes = 3;
		// set default if null
		if (failLoginDate == null) {
			failLoginDate = new Date();
		}

		// update user
		loadUserProperties(user, true);

		if (user.getStatus().equals(ModelConstant.User.STATUS_ADMIN)) {
			user.getUserPropertiesObject().addLoginFailDateList(failLoginDate);
			if (user.getUserPropertiesObject().getLoginFailDateList().size() >= loginFailBlockTimes) {
				user.setAccountLocked(true);
				user.getUserPropertiesObject().setLoginFailDateList(null);
			}

			saveUserProperties(user, false, true);
		} else if (!user.getStatus().equals(ModelConstant.User.STATUS_RESET)) {
			user.getUserPropertiesObject().addLoginFailDateList(failLoginDate);

			// check if user must be block
			if (user.getUserPropertiesObject().getLoginFailDateList().size() >= loginFailBlockTimes) {
				// save previous user status to
				String previousStatus = user.getStatus();
				if (user.getUserPropertiesObject().getPreviousStatus() != null
						&& (user.getStatus().equals(ModelConstant.User.STATUS_BLOCKED) || user.getStatus().equals(
								ModelConstant.User.STATUS_RESET))) {
					previousStatus = user.getUserPropertiesObject().getPreviousStatus();
				}

				user.getUserPropertiesObject().setPreviousStatus(previousStatus);

				// reset LoginFailDateList
				user.getUserPropertiesObject().setLoginFailDateList(null);
				user.getUserPropertiesObject().addSoftBlockDateList(failLoginDate);

				if (user.getUserPropertiesObject().getSoftBlockDateList().size() >= softBlockResetTimes) {
					user.getUserPropertiesObject().setSoftBlockDateList(null);
					user.setStatus(ModelConstant.User.STATUS_RESET);
				} else {
					user.setStatus(ModelConstant.User.STATUS_BLOCKED);
					user.getUserPropertiesObject().setBlockReason(ModelConstant.User.BLOCK_WRONG_PASSWORD);
					user.setBlockTime(new Date());
					user.setBlockInterval(new Integer(loginFailBlockInterval));
				}
			}

			saveUserProperties(user, false, true);
		}

		// save audit log
		/*
		 * auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
		 * ModelConstant.AuditLogSubType.USER_ACTIVITY_LOGIN_FAIL,
		 * "auditLog.info.format.loginFail",
		 * new Object[] { ipAddress }, user.getId(), new Date());
		 */
		return user.getStatus();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public User reRegistration(User user, String fullName, Date dob, String plainPassword, List<String> accountList) {
		// update user master data
		user.setUserFullName(fullName);
		user.setBirthDate(dob);

		if (!user.getStatus().equalsIgnoreCase(ModelConstant.User.STATUS_TRANSACTION_ENABLE)) {
			user.setStatus(ModelConstant.User.STATUS_LOGIN_ONLY);
		}

		loadUserProperties(user, true);

		user.getUserPropertiesObject().setLoginFailDateList(null);
		user.getUserPropertiesObject().setSoftBlockDateList(null);

		user.setPassword(passwordEncoder.encodePassword(plainPassword, saltSource.getSalt(user)));

		saveUserProperties(user, false, true);

		// update user accounts
		/*
		 * Set<UserAccount> userAccountSet = user.getUserAccountSet();
		 * List<String> newAccountList = new ArrayList<String>();
		 * 
		 * for (String accountNo : accountList) {
		 * UserAccount found = null;
		 * 
		 * for (UserAccount userAccount : userAccountSet) {
		 * if (userAccount.getAccountNo().equals(accountNo)) {
		 * found = userAccount;
		 * break;
		 * }
		 * }
		 * 
		 * if (found == null) {
		 * newAccountList.add(accountNo);
		 * } else {
		 * found.setAccountName(fullName);
		 * }
		 * }
		 * 
		 * for (String newAccount : newAccountList) {
		 * UserAccount userAccount = new UserAccount();
		 * userAccount.setUserId(user);
		 * userAccount.setAccountNo(newAccount);
		 * userAccount.setAccountName(fullName);
		 * userAccount.setIbStatus(ModelConstant.UserAccount.IB_STATUS_AKTIF);
		 * 
		 * CommonUtil.interceptAuditModel(userAccount,
		 * ModelConstant.User.USER_ID_SYSTEM);
		 * 
		 * userAccountSet.add(userAccount);
		 * }
		 * 
		 * user.setUserAccountSet(userAccountSet);
		 */

		userDao.update(user);

		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditLog activateUserSToken(User user, String msisdn) {
		// save to db
		user.setPhoneNumber(msisdn);
		user.setSecurityType("S"); // Soft Token
		user.setStatus("T"); // Transaction Enabled

		// update session
		User currentLogin = SecurityContextUtil.getCurrentUser();
		currentLogin.setSecurityType("S");
		currentLogin.setStatus(ModelConstant.User.STATUS_TRANSACTION_ENABLE);

		// save log
		return null; /*
					 * auditLogManager.save(ModelConstant.ReffNumType.ACTIVATION,
					 * "sToken",
					 * "auditLog.info.format.activation.sToken", new Object[] {
					 * msisdn }, user.getId(), new Date());
					 */
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditLog activateUserTToken(User user) {
		user.setSecurityType("T");
		user.setStatus(ModelConstant.User.STATUS_TRANSACTION_ENABLE);

		// update session
		User currentLogin = SecurityContextUtil.getCurrentUser();
		currentLogin.setSecurityType("T");
		currentLogin.setStatus(ModelConstant.User.STATUS_TRANSACTION_ENABLE);

		// save log
		return null; /*
					 * auditLogManager.save(ModelConstant.ReffNumType.ACTIVATION,
					 * "tToken",
					 * "auditLog.info.format.activation.tToken.reactivate", new
					 * Object[] { user.getId() }, user.getId(),
					 * new Date());
					 */
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditLog changePassword(User user, String newPassword) {
		// save new password
		savePassword(user, newPassword);

		return null;/*
					 * auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
					 * "chPwd", "auditLog.info.format.changePassword",
					 * new Object[] {}, user.getId(), new Date());
					 */
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditLog changeEmail(User user, String newEmail) {
		String oldEmail = user.getEmail();
		if (CommonUtil.isEmpty(oldEmail))
			oldEmail = "none";

		// save new email
		user.setEmail(newEmail);
		userDao.saveOrUpdate(user);

		return null;/*
					 * auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
					 * "chEmail", "auditLog.info.format.changeEmail",
					 * new Object[] { oldEmail, newEmail }, user.getId(), new
					 * Date());
					 */
	}

	/**
	 * @param user
	 *            - object to save encodedPassword
	 * @return plainPassword
	 */
	public String generatePassword(User user) {
		Random random = new Random();
		StringBuffer plainPassword = new StringBuffer();

		for (int i = 0; i < RANDOM_PASSWORD_LENGTH; i++) {
			int number = random.nextInt(10);
			if (number < 10) {
				plainPassword.append(number);
				// } else if (number >= 10) {
				// plainPassword.append(((char) (number + 87)));
			}
		}

		String encodedPassword = encodePassword(user, plainPassword.toString());
		user.setPassword(encodedPassword);

		return plainPassword.toString();
	}

	public List<UserDisplay> findUser(String userName, String email, String roleName, int firstResult, int maxResult) {
		return userDao.findUser(userName, email, roleName, firstResult, maxResult);
	}

	public List<UserDisplay> findUser(SearchCriteria searchCriteria, int firstResult, int maxResult) {
		List<User> userList = userDao.findByCriteria(searchCriteria, firstResult, maxResult);

		List<UserDisplay> userDisplayList = new ArrayList<UserDisplay>();
		for (User tmpUser : userList) {
			Long userRoleId = null;
			String roleName = null;
			for (UserRole userRole : tmpUser.getUserRoleSet()) {
				userRoleId = userRole.getUserRoleId().getRole().getId();
				System.out.println("userRoleId:" + userRoleId);
				roleName = userRole.getUserRoleId().getRole().getName();
				break;
			}

			UserDisplay toAdd = new UserDisplay(tmpUser.getId(), userRoleId, tmpUser.getUsername(), tmpUser.getEmail(),
					roleName, tmpUser.getUserFullName(), tmpUser.getCardNumber(), tmpUser.getUserParentId());

			User parentUser = userDao.findById(tmpUser.getUserParentId());
			if (parentUser == null) {
				toAdd.setParentUsername(null);
			} else {
				toAdd.setParentUsername(parentUser.getUsername());
			}

			userDisplayList.add(toAdd);
		}

		return userDisplayList;
	}

	public int findUserCount(String userName, String email, String roleName) {
		return userDao.findUserCount(userName, email, roleName);
	}

	public List<UserGroupDisplay> findAdminUser() {
		return userDao.findAdminUser();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public User addNewAdmin(String userName, String name, String email, String plainPassword, Role role) {
		User user = new User();
		user.setUserName(userName);
		user.setUserFullName(name);
		user.setEmail(email);
		user.setEnabled(Boolean.TRUE);
		user.setStatus(ModelConstant.User.STATUS_ADMIN);
		user.setMustChangePassword(Boolean.TRUE);
		user.setPassword(encodeResetPassword(user));
		user.setUserParentId(new Long(0));
		// CommonUtil.interceptAuditModel(user,
		// ModelConstant.User.USER_ID_SYSTEM);

		UserRole userRole = new UserRole(new UserRoleId(user, role));
		user.getUserRoleSet().add(userRole);

		boolean addRole = true;

		UserSingle userSingle = new UserSingle().copyFromUser(user);
		try {
			userSingle.setEnableds("Y");
			userSingle.setAccountExpireds("N");
			userSingle.setAccountLockeds("N");
			userSingle.setCredentialExpireds("N");
//			userSingle.setCredentialsExpired(Boolean.FALSE);
			userSingle.setCreatedBy(SecurityContextUtil.getCurrentUserId());
			userSingle.setCreatedDate(new Date());
			userSingle.setMustChangePasswords("Y");
			((AdminGenericDaoImpl) userDao).setIdentifier(userSingle);
			userDao.getCurrentHibernateTemplate().persist(userSingle);
			userDao.getCurrentHibernateTemplate().flush();
		} catch (Throwable e) {
			addRole = false;
		}

		if (addRole) {
			UserRoleSingle userRoleSingle = new UserRoleSingle(userSingle.getId(), role.getId());
			userRoleDao.getCurrentHibernateTemplate().save(userRoleSingle);
		}

		return user;
	}

	public List<UserGroupDisplay> findByGroupId(Long groupId) {
		return userDao.findByGroupId(groupId);
	}

	public String generateActivationCode(User user) {
		Random random = new Random();
		StringBuffer activationCode = new StringBuffer();

		for (int i = 0; i < ACTIVATION_CODE_LENGTH; i++) {
			int number = random.nextInt(36);

			if (number < 10) {
				activationCode.append(number);
			} else if (number >= 10) {
				activationCode.append(((char) (number + 87)));
			}
		}

		user.getUserPropertiesObject().setEmailActivationCode(activationCode.toString());
		return activationCode.toString();
	}

	public AuditLog linkNewCard(User user, String cardNumber, String newCardNumber, User actor) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE, "cardNumber",
				"auditLog.info.format.change.cardNumber",
				new Object[] { actor.getUsername(), user.getUsername(), cardNumber, newCardNumber }, actor.getId(),
				new Date());

		if (user.getCardNumber().equals(cardNumber)) {
			// set to new card number
			user.setCardNumber(newCardNumber);

			// reset user
			// user.setStatus(ModelConstant.User.STATUS_RESET);
			// user.setSecurityType(null);

			CommonUtil.interceptAuditModel(user, actor.getId());

			userDao.saveOrUpdate(user);
		}

		return auditLog;
	}

	public User resetUserAdminPassword(User actor, User user, String ipAddress) {
		// String newPlainPassword = generatePassword(user);
		String newPlainPassword = encodeResetPassword(user);
		user.setAccountLocked(Boolean.FALSE);
		user.setPassword(newPlainPassword);
		user.setChangePasswordDate(new Date());

		userDao.updatePassword(user);

		return user;
	}

	public User resetUserToInquiryOnly(User user) {
		User saveUser = userDao.findById(user.getId());
		saveUser.setSecurityType(null);
		saveUser.setStatus(ModelConstant.User.STATUS_INQUIRY_ONLY);

		userDao.saveOrUpdate(saveUser);

		return user;
	}

	private String removeOtherCharacter(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if ('A' <= str.charAt(i) && str.charAt(i) <= 'Z')
				sb.append(str.charAt(i));
		}
		return sb.toString();
	}

	public AuditLog actionTransaction(User user, String type, Long TransactionId, String ReffNo) {
		if (ModelConstant.Product.Type.SELULAR_POSTPAID.equals(type)) {
			return null;/*
						 * auditLogManager.save(ModelConstant.ReffNumType.
						 * SELULAR_POSTPAID,
						 * ModelConstant.AuditLogSubType.SELULAR_POSTPAID,
						 * "auditLog.info.format.selularPostpaid.norm",
						 * new Object[] { TransactionId, ReffNo }, user.getId(),
						 * new Date());
						 */
		}

		if (ModelConstant.Product.Type.SELULAR_PREPAID.equals(type)) {
			return null;/*
						 * auditLogManager.save(ModelConstant.ReffNumType.
						 * SELULAR_PREPAID,
						 * ModelConstant.AuditLogSubType.SELULAR_PREPAID,
						 * "auditLog.info.format.selularPrepaid.norm",
						 * new Object[] { TransactionId, ReffNo }, user.getId(),
						 * new Date());
						 */
		}

		return actionTransaction(user, TransactionId, ReffNo);
	}

	public AuditLog tryingLogin(String ipAddress) {
		return null;/*
					 * auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY
					 * ,
					 * ModelConstant.AuditLogSubType.USER_ACTIVITY_LOGIN_FAIL,
					 * "auditLog.info.format.loginFail",
					 * new Object[] { ipAddress }, 0l, new Date());
					 */
	}

	public AuditLog actionTransaction(User user, Long TransactionId, String ReffNo) {
		return auditLogManager.save(ModelConstant.ReffNumType.OTHER, "slother", "auditLog.info.format.selular.other",
				new Object[] { TransactionId, ReffNo }, user.getId(), new Date());
	}

	public AuditLog viewTransactionHistoryFail(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_FAIL_REPORT, "auditLog.info.format.report.transactionHistory.fail",
				new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewTransferBatchHistoryFail(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_FAIL_REPORT, "auditLog.info.format.report.transferBatch.fail",
				new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewAccountBalance(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_INFORMATION, "auditLog.info.format.view.accountBalance",
				new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewAccountBalanceDetail(User user, String accountNo, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_INFORMATION, "auditLog.info.format.view.accountBalance.detail",
				new Object[] { accountNo, ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewDeposito(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_INFORMATION, "auditLog.info.format.view.deposito",
				new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewDepositoDetail(User user, String depositoNo, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_INFORMATION, "auditLog.info.format.view.deposito.detail",
				new Object[] { depositoNo, ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewMutationDetail(User user, String accountNo, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_INFORMATION, "auditLog.info.format.view.mutation.detail",
				new Object[] { accountNo, ipAddress }, user.getId(), new Date());
	}

	public AuditLog viewTransactionHistory(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY, ModelConstant.AuditLogSubType.VIEW_REPORT,
				"auditLog.info.format.report.transactionHistory", new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog RequestHardToken(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.REQUEST, "tToken",
				"auditLog.info.format.request.hardToken", new Object[] { user.getId(), ipAddress }, user.getId(),
				new Date());
	}

	public AuditLog DeleteEmail(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.DELETE_EMAIL, "auditLog.info.format.deleteEmail",
				new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog SendEmailToBank(User user, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY, ModelConstant.AuditLogSubType.SEND_EMAIL,
				"auditLog.info.format.sendEmailToBank", new Object[] { ipAddress }, user.getId(), new Date());
	}

	public AuditLog DeletePreRegisteredPayment(User user, String ipAddress) {
		return auditLogManager
				.save(ModelConstant.ReffNumType.USER_ACTIVITY,
						ModelConstant.AuditLogSubType.DELETE_PREREGISTERED_PAYMENT,
						"auditLog.info.format.deletePreRegisteredPayment", new Object[] { ipAddress }, user.getId(),
						new Date());
	}

	public AuditLog addAccountSuccess(User user, String accountNo, String ipAddress) {
		return auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_MANAGEMENT, "auditLog.info.format.accountMaintenance.addSuccess",
				new Object[] { accountNo, ipAddress }, user.getId(), new Date());
	}

	public AuditLog addAccountFail(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_MANAGEMENT, "auditLog.info.format.accountMaintenance.addFail",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog activateAccountSuccess(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_MANAGEMENT,
				"auditLog.info.format.accountMaintenance.activateSuccess", new Object[] { ipAddress }, user.getId(),
				new Date());
		return auditLog;
	}

	public AuditLog activateAccountFail(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.ACCOUNT_MANAGEMENT,
				"auditLog.info.format.accountMaintenance.activateFail", new Object[] { ipAddress }, user.getId(),
				new Date());
		return auditLog;
	}

	public AuditLog viewExchangeRateList(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_INFORMATION, "auditLog.info.format.informationExchange",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog viewInterestRateList(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_INFORMATION, "auditLog.info.format.informationInterest",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog changeProfile(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE, "chProf",
				"auditLog.info.format.changeProfile", new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog adminEntryUser(User actor, User newUser, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.ACTIVATION,
				ModelConstant.AuditLogSubType.USER_MANAGEMENT, "auditLog.info.format.activation.entryUser",
				new Object[] { newUser.getId(), ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog adminEditUser(User actor, User newUser, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
				ModelConstant.AuditLogSubType.USER_MANAGEMENT, "auditLog.info.format.change.editUser",
				new Object[] { newUser.getId(), ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog adminDeleteUser(User actor, Long deletedUserId, String ipAddress) {
		User user = userDao.findById(deletedUserId);
		userDao.delete(user);
		userRoleDao.deleteByUserId(user.getId());
		return null;
	}

	public AuditLog adminEntryRole(User actor, Role role, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.ACTIVATION,
				ModelConstant.AuditLogSubType.ROLE_MANAGEMENT, "auditLog.info.format.activation.entryRole",
				new Object[] { role.getId(), ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog adminEditRole(User actor, Role role, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
				ModelConstant.AuditLogSubType.ROLE_MANAGEMENT, "auditLog.info.format.change.editRole",
				new Object[] { role.getId(), ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog adminResetRole(User actor, Role role, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
				ModelConstant.AuditLogSubType.ROLE_MANAGEMENT, "auditLog.info.format.change.resetRole",
				new Object[] { role.getId(), ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog adminDeleteRole(User actor, Long deletedRoleId, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
				ModelConstant.AuditLogSubType.ROLE_MANAGEMENT, "auditLog.info.format.change.deleteRole",
				new Object[] { deletedRoleId, ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog SendBroadcastEmailToCustomer(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.SEND_EMAIL, "auditLog.info.format.sendBroadcastEmailToCustomer",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog SendEmailToCustomer(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.SEND_EMAIL, "auditLog.info.format.sendEmailToCustomer",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog viewUserIb(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_INFORMATION, "auditLog.info.format.viewUserIb",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog changeUserSignUpStatus(User actor, UserRegister user, String ipAddress) {
		String status = null;

		if (user.getStatus().equals(UserRegister.STATUS_NEW_OR_ADDED)) {
			status = "baru atau ditambahkan";
		} else if (user.getStatus().equals(UserRegister.STATUS_SUCCESS_OR_REGISTERED)) {
			status = "teregistrasi";
		} else if (user.getStatus().equals(UserRegister.STATUS_DELETE)) {
			status = "ditolak";
		}

		String username = user.getFullname();

		return auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
				ModelConstant.AuditLogSubType.USER_REGISTER_MANAGEMENT, "auditLog.info.format.changeUserSignUpStatus",
				new Object[] { username, status, ipAddress }, actor.getId(), new Date());
	}

	public AuditLog testSystemMonitor(User user, String testType, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.TEST_SYSTEM_MONITOR, "auditLog.info.format.systemMonitor",
				new Object[] { testType, ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog viewUserLogin(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_INFORMATION, "auditLog.info.format.viewUserLogin",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog kickUser(User actor, User user, String ipAddress) {
		/*
		 * AuditLog auditLog =
		 * auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
		 * ModelConstant.AuditLogSubType.USER_MANAGEMENT,
		 * "auditLog.info.format.kickUser",
		 * new Object[] { user.getUsername(), actor.getUsername(), ipAddress },
		 * actor.getId(), new Date());
		 */
		return null;// auditLog;
	}

	public AuditLog csResetUser(User actor, User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
				ModelConstant.AuditLogSubType.USER_MANAGEMENT, "auditLog.info.format.customerService.resetUser",
				new Object[] { user.getId(), ipAddress }, actor.getId(), new Date());
		return auditLog;
	}

	public AuditLog csBlockUser(User actor, User user, String ipAddress) {
		/*
		 * AuditLog auditLog =
		 * auditLogManager.save(ModelConstant.ReffNumType.CHANGE,
		 * ModelConstant.AuditLogSubType.USER_MANAGEMENT,
		 * "auditLog.info.format.customerService.blockUser",
		 * new Object[] { user.getId(), ipAddress }, actor.getId(), new Date());
		 */
		return null;
	}

	public AuditLog viewTransactionPage(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_REPORT, "auditLog.info.format.report.transaction",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog viewTransactionFailPage(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_FAIL_REPORT, "auditLog.info.format.report.transaction.fail",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	public AuditLog viewSMSLog(User user, String Biller, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_REPORT, "auditLog.info.format.report.smsLog",
				new Object[] { Biller, ipAddress }, user.getId(), new Date());
		return auditLog;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public AuditLog viewAuditLog(User user, String ipAddress) {
		AuditLog auditLog = auditLogManager.save(ModelConstant.ReffNumType.USER_ACTIVITY,
				ModelConstant.AuditLogSubType.VIEW_REPORT, "auditLog.info.format.report.auditLog",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}
	
	public AuditLog viewInitialInvoiceSearch(User user, String ipAddress){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
				ModelConstant.AuditLogSubType.SEARCH_INVOICE, "auditLog.info.format.report.initialInvoiceSearch",
				new Object[] { ipAddress }, user.getId(), new Date());

		return auditLog;
	}
	
	public AuditLog viewInitialInvoiceCreate(User user, String ipAddress, String bhpMethod, String licenseNo){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
				ModelConstant.AuditLogSubType.INITIAL_INVOICE, "auditLog.info.format.report.initialInvoiceCreate",
				new Object[] { ipAddress, bhpMethod, licenseNo}, user.getId(), new Date());

		return auditLog;
	}
	
	public AuditLog saveInvoice(User user, String ipAddress, String saveMethod, String licenseNo, String invoiceNo, String bhpMethod){
		if(saveMethod.endsWith("submit")){
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SUBMIT_INVOICE,
					ModelConstant.AuditLogSubType.INITIAL_INVOICE, "auditLog.info.format.report.initialInvoiceCreate.submit",
					new Object[] { ipAddress, bhpMethod, licenseNo, invoiceNo }, user.getId(), new Date());

			return auditLog;
		}else{
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.DRAFT_INVOICE,
					ModelConstant.AuditLogSubType.INITIAL_INVOICE, "auditLog.info.format.report.initialInvoiceCreate.draft",
					new Object[] { ipAddress, bhpMethod, licenseNo }, user.getId(), new Date());

			return auditLog;
		}
	}
	
	public AuditLog viewManageInvoiceSearch(User user, String ipAddress){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
				ModelConstant.AuditLogSubType.MANAGE_INVOICE, "auditLog.info.format.report.manageInvoiceSearch",
				new Object[] { ipAddress }, user.getId(), new Date());

		return auditLog;
	}
	
	public AuditLog viewManageInvoiceView(User user, String ipAddress, String bhpMethod, String licenseNo, boolean isFine){
		if(isFine){
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.VIEW_INVOICE,
					ModelConstant.AuditLogSubType.MANAGE_INVOICE, "auditLog.info.format.report.manageInvoiceView.fine",
					new Object[] { ipAddress, bhpMethod, licenseNo, "Fine"}, user.getId(), new Date());

			return auditLog;
		}else{
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.VIEW_INVOICE,
					ModelConstant.AuditLogSubType.MANAGE_INVOICE, "auditLog.info.format.report.manageInvoiceView",
					new Object[] { ipAddress, bhpMethod, licenseNo}, user.getId(), new Date());

			return auditLog;
		}
	}
	
	public AuditLog viewManageInvoiceEdit(User user, String ipAddress, String bhpMethod, String licenseNo){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.EDIT_INVOICE,
				ModelConstant.AuditLogSubType.MANAGE_INVOICE, "auditLog.info.format.report.manageInvoiceEdit",
				new Object[] { ipAddress, bhpMethod, licenseNo,}, user.getId(), new Date());

		return auditLog;
	}
	
	public AuditLog viewSimulasi(User user, String ipAddress, String desc){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SIMULASI,
				ModelConstant.AuditLogSubType.SIMULASI_INVOICE, "auditLog.info.format.report.simulasi",
				new Object[] { ipAddress, desc}, user.getId(), new Date());

		return auditLog;
	}
	
	public AuditLog biRateAuditLog(User user, String ipAddress, String desc, String year){
		if(year != null){
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.INPUT,
					ModelConstant.AuditLogSubType.BI_RATE, "auditLog.info.format.report.biRate.save",
					new Object[] { ipAddress, desc, year}, user.getId(), new Date());
	
			return auditLog;
		}else{
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
					ModelConstant.AuditLogSubType.BI_RATE, "auditLog.info.format.report.biRate",
					new Object[] { ipAddress, desc}, user.getId(), new Date());
	
			return auditLog;
		}
	}
	
	public AuditLog bhpRateAuditLog(User user, String ipAddress, String desc, String year){
		if(year != null){
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.INPUT,
					ModelConstant.AuditLogSubType.BHP_RATE, "auditLog.info.format.report.bhpRate.save",
					new Object[] { ipAddress, desc, year}, user.getId(), new Date());
	
			return auditLog;
		}else{
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
					ModelConstant.AuditLogSubType.BHP_RATE, "auditLog.info.format.report.bhpRate",
					new Object[] { ipAddress, desc}, user.getId(), new Date());
	
			return auditLog;
		}
	}
	
	public AuditLog viewHistoryInvoice(User user, String ipAddress){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
				ModelConstant.AuditLogSubType.HISTORY_INVOICE, "auditLog.info.format.report.historyInvoice",
				new Object[] { ipAddress }, user.getId(), new Date());
		return auditLog;
	}
	
	public AuditLog uploadDokumen(User user, String ipAddress, String desc){
		if(desc != null){
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.INPUT,
					ModelConstant.AuditLogSubType.UPLOAD, "auditLog.info.format.report.uploadDokumen.input",
					new Object[] { ipAddress, desc }, user.getId(), new Date());
			return auditLog;
		}else{
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
					ModelConstant.AuditLogSubType.UPLOAD, "auditLog.info.format.report.uploadDokumen",
					new Object[] { ipAddress }, user.getId(), new Date());
			return auditLog;
		}
	}
	
	public AuditLog userAuditLog(User user, String ipAddress, String userName, boolean isAddNew){
		if(isAddNew){
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.INPUT,
					ModelConstant.AuditLogSubType.USER, "auditLog.info.format.report.user.input",
					new Object[] { ipAddress, userName }, user.getId(), new Date());
			return auditLog;
		}else{
			AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.SEARCH,
					ModelConstant.AuditLogSubType.UPLOAD, "auditLog.info.format.report.user.edit",
					new Object[] { ipAddress, userName }, user.getId(), new Date());
			return auditLog;
		}
	}
	
	public AuditLog changePasswordAuditLog(User user, String ipAddress){
		AuditLog auditLog = auditLogManager.save(ModelConstant.UserBHPActivity.INPUT,
				ModelConstant.AuditLogSubType.USER, "auditLog.info.format.report.user.changePassword",
				new Object[] { ipAddress, user.getUsername() }, user.getId(), new Date());
		return auditLog;
	}

	public List<User> findChildUserByParentId(Long parentId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("userParentId", parentId));

		List<User> userList = (List<User>) userDao.findByCriteria(criterionList, null, -1, -1);
		if ((userList == null) || userList.isEmpty()) {
			return null;
		}

		return userList;
	}

	@Override
	public User findById(Long id) {
		return userDao.findById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveOrUpdate(User user) {
		userDao.saveOrUpdate(user);
	}

	@Override
	public List<User> findByCriteria(SearchCriteria searchCriteria, int i, int j) {
		return userDao.findByCriteria(searchCriteria, i, j);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(User memberUser) {
		userDao.save(memberUser);
	}

	@Override
	public Object getDefaultDao() {
		return userDao;
	}

	@Override
	public Integer findByCriteriaCount(SearchCriteria searchCriteria) {

		return userDao.findByCriteriaCount(searchCriteria);
	}

	@Override
	public String encodeResetPassword(User user) {
		return encodePassword(user, "000000");
	}

}
