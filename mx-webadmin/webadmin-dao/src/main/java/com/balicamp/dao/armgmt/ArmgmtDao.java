package com.balicamp.dao.armgmt;

import java.util.Date;
import java.util.HashMap;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.admin.BaseAdminModel;

public interface ArmgmtDao extends GenericDao<BaseAdminModel, String> {
	public HashMap<String, Object[]> getMT940(Date transDate, Date transEndDate, String paymentType, String bankName);	
}
