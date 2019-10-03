package com.balicamp.service.user;

import java.util.List;

import com.balicamp.model.log.AuditLog;
import com.balicamp.model.user.UserCardNumber;
import com.balicamp.service.IManager;

public interface UserCardNumberManager extends IManager {

	public AuditLog saveChangeUserCardNumber(UserCardNumber userCardNumber);

	public List<UserCardNumber> findByCardNumber(String cardNumber);
}
