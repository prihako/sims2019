package com.balicamp.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.balicamp.model.ISequencesModel;
import com.balicamp.model.admin.BaseAdminModel;

@Entity
@Table(name = "s_user")
public class UserSingle extends BaseAdminModel implements ISequencesModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5585538558860316207L;
	/**
	* <br/>
	* column :id
	**/
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	/**
	* <br/>
	* column :user_name
	**/
	@Column(name = "user_name", nullable = false)
	private String userName;
	/**
	* <br/>
	* column :user_full_name
	**/
	@Column(name = "user_full_name", nullable = true)
	private String userFullName;
	/**
	* <br/>
	* column :birth_date
	**/
	@Column(name = "birth_date", nullable = true)
	private Date birthDate;
	/**
	* <br/>
	* column :limit_transaction
	**/
	@Column(name = "limit_transaction", nullable = true)
	private Double limitTransaction;
	/**
	* <br/>
	* column :active_date
	**/
	@Column(name = "active_date", nullable = true)
	private Date activeDate;
	/**
	* <br/>
	* column :inactive_date
	**/
	@Column(name = "inactive_date", nullable = true)
	private Date inactiveDate;
	/**
	* <br/>
	* column :remark
	**/
	@Column(name = "remark", nullable = true)
	private String remark;
	/**
	* <br/>
	* column :last_login
	**/
	@Column(name = "last_login", nullable = true)
	private Date lastLogin;
	/**
	* <br/>
	* column :security_type
	**/
	@Column(name = "security_type", nullable = true)
	private String securityType;
	/**
	* <br/>
	* column :password
	**/
	@Column(name = "password", nullable = false)
	private String password;
	/**
	* <br/>
	* column :virtual_pin
	**/
	@Column(name = "virtual_pin", nullable = true)
	private String virtualPin;
	/**
	* <br/>
	* column :phone_number
	**/
	@Column(name = "phone_number", nullable = true)
	private String phoneNumber;
	/**
	* <br/>
	* column :block_time
	**/
	@Column(name = "block_time", nullable = true)
	private Date blockTime;
	/**
	* <br/>
	* column :status
	**/
	@Column(name = "status", nullable = false)
	private String status;
	/**
	* <br/>
	* column :block_interval
	**/
	@Column(name = "block_interval", nullable = true)
	private Integer blockInterval;
	/**
	* <br/>
	* column :card_number
	**/
	@Column(name = "card_number", nullable = true)
	private String cardNumber;
	/**
	* <br/>
	* column :email
	**/
	@Column(name = "email", nullable = true)
	private String email;
	/**
	* <br/>
	* column :locale
	**/
	@Column(name = "locale", nullable = true)
	private String locale;
	/**
	* <br/>
	* column :properties
	**/
	@Column(name = "properties", nullable = true)
	private String userProperties;
	/**
	* <br/>
	* column :changed_password_date
	**/
	@Column(name = "changed_password_date", nullable = true)
	private Date changePasswordDate;
	/**
	* <br/>
	* column :password_history
	**/
	@Column(name = "password_history", nullable = true)
	private String passwordHistory;
	/**
	* <br/>
	* column :created_by
	**/
	@Column(name = "created_by", nullable = true)
	private Long createdBy;
	/**
	* <br/>
	* column :created_date
	**/
	@Column(name = "created_date", nullable = true)
	private Date createdDate;
	/**
	* <br/>
	* column :changed_by
	**/
	@Column(name = "changed_by", nullable = true)
	private Double changedBy;
	/**
	* <br/>
	* column :changed_date
	**/
	@Column(name = "changed_date", nullable = true)
	private Date changedDate;
	/**
	* <br/>
	* column :user_parent_id
	**/
	@Column(name = "user_parent_id", nullable = true)
	private Long userParentId;
	/**
	* <br/>
	* column :enabled
	**/
	@Column(name = "enabled", nullable = true)
	private Boolean enabled;
	/**
	* <br/>
	* column :account_expired
	**/
	@Column(name = "account_expired", nullable = true)
	private Boolean accountExpired;
	/**
	* <br/>
	* column :account_locked
	**/
	@Column(name = "account_locked", nullable = true)
	private Boolean accountLocked;
	/**
	* <br/>
	* column :credential_expired
	**/
	@Column(name = "credential_expired", nullable = true)
	private Boolean credentialExpired;
	/**
	* <br/>
	* column :must_change_password
	**/
	@Column(name = "must_change_password", nullable = false)
	private boolean mustChangePassword;

	public UserSingle() {
	}

	public UserSingle(Long id) {
		this.id = id;
	}

	public UserSingle copyFromUser(User user) {
		setUserName(user.getUserName());
		setBirthDate(user.getBirthDate());
		setLimitTransaction(user.getLimitTransaction());
		setActiveDate(user.getActiveDate());
		setInactiveDate(user.getInactiveDate());
		setRemark(user.getRemark());
		setLastLogin(user.getLastLogin());
		setSecurityType(user.getSecurityType());
		setPassword(user.getPassword());
		setVirtualPin(user.getVirtualPin());
		setPhoneNumber(user.getPhoneNumber());
		setBlockInterval(user.getBlockInterval());
		setBlockTime(user.getBlockTime());
		setStatus(user.getStatus());
		setCardNumber(user.getCardNumber());
		setEmail(user.getEmail());
		setLocale(user.getLocale());
		setUserProperties(user.getUserProperties());
		setChangePasswordDate(user.getChangePasswordDate());
		setPasswordHistory(user.getPasswordHistory());
		setCreatedBy(user.getCreatedBy());
		setCreatedDate(user.getCreatedDate());
		setChangedBy(user.getChangedBy());
		setChangedDate(user.getChangedDate());
		setUserParentId(user.getUserParentId());
		setAccountLocked(user.getAccountLocked());
		setUserFullName(user.getUserFullName());

		return this;
	}

	/**
	* <br/>
	* column :user_name
	**/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	* <br/>
	* column :user_name
	**/
	public String getUserName() {
		return this.userName;
	}

	/**
	* <br/>
	* column :user_name
	**/
	public String getUsername() {
		return this.userName;
	}

	/**
	* <br/>
	* column :user_full_name
	**/
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	/**
	* <br/>
	* column :user_full_name
	**/
	public String getUserFullName() {
		return this.userFullName;
	}

	/**
	* <br/>
	* column :birth_date
	**/
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	* <br/>
	* column :birth_date
	**/
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	* <br/>
	* column :limit_transaction
	**/
	public void setLimitTransaction(Double limitTransaction) {
		this.limitTransaction = limitTransaction;
	}

	/**
	* <br/>
	* column :limit_transaction
	**/
	public Double getLimitTransaction() {
		return this.limitTransaction;
	}

	/**
	* <br/>
	* column :active_date
	**/
	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	/**
	* <br/>
	* column :active_date
	**/
	public Date getActiveDate() {
		return this.activeDate;
	}

	/**
	* <br/>
	* column :inactive_date
	**/
	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	/**
	* <br/>
	* column :inactive_date
	**/
	public Date getInactiveDate() {
		return this.inactiveDate;
	}

	/**
	* <br/>
	* column :remark
	**/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	* <br/>
	* column :remark
	**/
	public String getRemark() {
		return this.remark;
	}

	/**
	* <br/>
	* column :last_login
	**/
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	* <br/>
	* column :last_login
	**/
	public Date getLastLogin() {
		return this.lastLogin;
	}

	/**
	* <br/>
	* column :security_type
	**/
	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	/**
	* <br/>
	* column :security_type
	**/
	public String getSecurityType() {
		return this.securityType;
	}

	/**
	* <br/>
	* column :password
	**/
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	* <br/>
	* column :password
	**/
	public String getPassword() {
		return this.password;
	}

	/**
	* <br/>
	* column :virtual_pin
	**/
	public void setVirtualPin(String virtualPin) {
		this.virtualPin = virtualPin;
	}

	/**
	* <br/>
	* column :virtual_pin
	**/
	public String getVirtualPin() {
		return this.virtualPin;
	}

	/**
	* <br/>
	* column :phone_number
	**/
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	* <br/>
	* column :phone_number
	**/
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	* <br/>
	* column :block_time
	**/
	public void setBlockTime(Date blockTime) {
		this.blockTime = blockTime;
	}

	/**
	* <br/>
	* column :block_time
	**/
	public Date getBlockTime() {
		return this.blockTime;
	}

	/**
	* <br/>
	* column :status
	**/
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	* <br/>
	* column :status
	**/
	public String getStatus() {
		return this.status;
	}

	/**
	* <br/>
	* column :block_interval
	**/
	public void setBlockInterval(Integer blockInterval) {
		this.blockInterval = blockInterval;
	}

	/**
	* <br/>
	* column :block_interval
	**/
	public Integer getBlockInterval() {
		return this.blockInterval;
	}

	/**
	* <br/>
	* column :card_number
	**/
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	* <br/>
	* column :card_number
	**/
	public String getCardNumber() {
		return this.cardNumber;
	}

	/**
	* <br/>
	* column :email
	**/
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	* <br/>
	* column :email
	**/
	public String getEmail() {
		return this.email;
	}

	/**
	* <br/>
	* column :locale
	**/
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	* <br/>
	* column :locale
	**/
	public String getLocale() {
		return this.locale;
	}

	/**
	* <br/>
	* column :properties
	**/
	public void setUserProperties(String userProperties) {
		this.userProperties = userProperties;
	}

	/**
	* <br/>
	* column :properties
	**/
	public String getUserProperties() {
		return userProperties;
	}

	/**
	* <br/>
	* column :changed_password_date
	**/
	public void setChangePasswordDate(Date changePasswordDate) {
		this.changePasswordDate = changePasswordDate;
	}

	/**
	* <br/>
	* column :changed_password_date
	**/
	public Date getChangePasswordDate() {
		return this.changePasswordDate;
	}

	/**
	* <br/>
	* column :password_history
	**/
	public void setPasswordHistory(String passwordHistory) {
		this.passwordHistory = passwordHistory;
	}

	/**
	* <br/>
	* column :password_history
	**/
	public String getPasswordHistory() {
		return this.passwordHistory;
	}

	/**
	* <br/>
	* column :created_by
	**/
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	* <br/>
	* column :created_by
	**/
	public Long getCreatedBy() {
		return this.createdBy;
	}

	/**
	* <br/>
	* column :created_date
	**/
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	* <br/>
	* column :created_date
	**/
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	* <br/>
	* column :changed_by
	**/
	public void setChangedBy(Double changedBy) {
		this.changedBy = changedBy;
	}

	/**
	* <br/>
	* column :changed_by
	**/
	public Double getChangedBy() {
		return this.changedBy;
	}

	/**
	* <br/>
	* column :changed_date
	**/
	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	/**
	* <br/>
	* column :changed_date
	**/
	public Date getChangedDate() {
		return this.changedDate;
	}

	/**
	* <br/>
	* column :user_parent_id
	**/
	public void setUserParentId(Long userParentId) {
		this.userParentId = userParentId;
	}

	/**
	* <br/>
	* column :user_parent_id
	**/
	public Long getUserParentId() {
		return this.userParentId;
	}

	/**
	* <br/>
	* column :enabled
	**/
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	* <br/>
	* column :enabled
	**/
	public boolean isEnabled() {
		return this.enabled;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	/**
	* <br/>
	* column :account_locked
	**/
	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	/**
	* <br/>
	* column :account_locked
	**/
	public Boolean getAccountLocked() {
		return this.accountLocked;
	}

	/**
	* <br/>
	* column :credential_expired
	**/
	public void setCredentialExpired(Boolean credentialExpired) {
		this.credentialExpired = credentialExpired;
	}

	/**
	* <br/>
	* column :credential_expired
	**/
	public Boolean isCredentialExpired() {
		return this.credentialExpired;
	}

	/**
	* <br/>
	* column :must_change_password
	**/
	public void setMustChangePassword(Boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}

	/**
	* <br/>
	* column :must_change_password
	**/
	public boolean isMustChangePassword() {
		return mustChangePassword;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}

	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}

	public boolean isCredentialsNonExpired() {
		return !isCredentialsExpired();
	}

	public boolean isCredentialsExpired() {
		return credentialExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialExpired = credentialsExpired;
	}

	@Override
	public String getSequenceName() {
		return "s_user_id_seq";
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserSingle [id=" + id + ", userName=" + userName + ", userFullName=" + userFullName + ", birthDate="
				+ birthDate + ", limitTransaction=" + limitTransaction + ", activeDate=" + activeDate
				+ ", inactiveDate=" + inactiveDate + ", remark=" + remark + ", lastLogin=" + lastLogin
				+ ", securityType=" + securityType + ", password=" + password + ", virtualPin=" + virtualPin
				+ ", phoneNumber=" + phoneNumber + ", blockTime=" + blockTime + ", status=" + status
				+ ", blockInterval=" + blockInterval + ", cardNumber=" + cardNumber + ", email=" + email + ", locale="
				+ locale + ", userProperties=" + userProperties + ", changePasswordDate=" + changePasswordDate
				+ ", passwordHistory=" + passwordHistory + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", changedBy=" + changedBy + ", changedDate=" + changedDate + ", userParentId=" + userParentId
				+ ", enabled=" + enabled + ", accountExpired=" + accountExpired + ", accountLocked=" + accountLocked
				+ ", credentialExpired=" + credentialExpired + ", mustChangePassword=" + mustChangePassword + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountExpired == null) ? 0 : accountExpired.hashCode());
		result = prime * result + ((accountLocked == null) ? 0 : accountLocked.hashCode());
		result = prime * result + ((activeDate == null) ? 0 : activeDate.hashCode());
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((blockInterval == null) ? 0 : blockInterval.hashCode());
		result = prime * result + ((blockTime == null) ? 0 : blockTime.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((changePasswordDate == null) ? 0 : changePasswordDate.hashCode());
		result = prime * result + ((changedBy == null) ? 0 : changedBy.hashCode());
		result = prime * result + ((changedDate == null) ? 0 : changedDate.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((credentialExpired == null) ? 0 : credentialExpired.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inactiveDate == null) ? 0 : inactiveDate.hashCode());
		result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
		result = prime * result + ((limitTransaction == null) ? 0 : limitTransaction.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + (mustChangePassword ? 1231 : 1237);
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((passwordHistory == null) ? 0 : passwordHistory.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((securityType == null) ? 0 : securityType.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((userFullName == null) ? 0 : userFullName.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userParentId == null) ? 0 : userParentId.hashCode());
		result = prime * result + ((userProperties == null) ? 0 : userProperties.hashCode());
		result = prime * result + ((virtualPin == null) ? 0 : virtualPin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSingle other = (UserSingle) obj;
		if (accountExpired == null) {
			if (other.accountExpired != null)
				return false;
		} else if (!accountExpired.equals(other.accountExpired))
			return false;
		if (accountLocked == null) {
			if (other.accountLocked != null)
				return false;
		} else if (!accountLocked.equals(other.accountLocked))
			return false;
		if (activeDate == null) {
			if (other.activeDate != null)
				return false;
		} else if (!activeDate.equals(other.activeDate))
			return false;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (blockInterval == null) {
			if (other.blockInterval != null)
				return false;
		} else if (!blockInterval.equals(other.blockInterval))
			return false;
		if (blockTime == null) {
			if (other.blockTime != null)
				return false;
		} else if (!blockTime.equals(other.blockTime))
			return false;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (changePasswordDate == null) {
			if (other.changePasswordDate != null)
				return false;
		} else if (!changePasswordDate.equals(other.changePasswordDate))
			return false;
		if (changedBy == null) {
			if (other.changedBy != null)
				return false;
		} else if (!changedBy.equals(other.changedBy))
			return false;
		if (changedDate == null) {
			if (other.changedDate != null)
				return false;
		} else if (!changedDate.equals(other.changedDate))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (credentialExpired == null) {
			if (other.credentialExpired != null)
				return false;
		} else if (!credentialExpired.equals(other.credentialExpired))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inactiveDate == null) {
			if (other.inactiveDate != null)
				return false;
		} else if (!inactiveDate.equals(other.inactiveDate))
			return false;
		if (lastLogin == null) {
			if (other.lastLogin != null)
				return false;
		} else if (!lastLogin.equals(other.lastLogin))
			return false;
		if (limitTransaction == null) {
			if (other.limitTransaction != null)
				return false;
		} else if (!limitTransaction.equals(other.limitTransaction))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (mustChangePassword != other.mustChangePassword)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (passwordHistory == null) {
			if (other.passwordHistory != null)
				return false;
		} else if (!passwordHistory.equals(other.passwordHistory))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (securityType == null) {
			if (other.securityType != null)
				return false;
		} else if (!securityType.equals(other.securityType))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (userFullName == null) {
			if (other.userFullName != null)
				return false;
		} else if (!userFullName.equals(other.userFullName))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userParentId == null) {
			if (other.userParentId != null)
				return false;
		} else if (!userParentId.equals(other.userParentId))
			return false;
		if (userProperties == null) {
			if (other.userProperties != null)
				return false;
		} else if (!userProperties.equals(other.userProperties))
			return false;
		if (virtualPin == null) {
			if (other.virtualPin != null)
				return false;
		} else if (!virtualPin.equals(other.virtualPin))
			return false;
		return true;
	}

	@Override
	public String getPKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
