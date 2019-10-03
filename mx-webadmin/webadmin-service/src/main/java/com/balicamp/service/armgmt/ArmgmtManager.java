package com.balicamp.service.armgmt;

import java.util.Date;
import java.util.HashMap;

import com.balicamp.service.IManager;

public interface ArmgmtManager extends IManager {
	public HashMap<String, Object[]> getMT940(Date transDate, Date transEndDate, String paymentType, String bankName);
}
