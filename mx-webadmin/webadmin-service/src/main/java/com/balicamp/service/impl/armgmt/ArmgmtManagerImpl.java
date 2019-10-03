package com.balicamp.service.impl.armgmt;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balicamp.dao.armgmt.ArmgmtDao;
import com.balicamp.service.armgmt.ArmgmtManager;
import com.balicamp.service.impl.AbstractManager;

@Service("armgmtManagerImpl")
public class ArmgmtManagerImpl extends AbstractManager implements ArmgmtManager {

	private static final long serialVersionUID = -710098086253876468L;

	@Autowired
	private ArmgmtDao armgmtDao;

	@Override
	public HashMap<String, Object[]> getMT940(Date transDate, Date transEndDate, String paymentType, String bankName) {
		// TODO Auto-generated method stub
		return armgmtDao.getMT940(transDate, transEndDate, paymentType, bankName);
	}

	@Override
	public Object getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}


}
