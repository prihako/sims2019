package com.balicamp.dao.hibernate.kalibrasi;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.kalibrasi.KalibrasiDao;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
@Transactional
public class KalibrasiDaoHibernate extends KalibrasiGenericDaoHibernate<BaseAdminModel, String> implements KalibrasiDao {

	public KalibrasiDaoHibernate() {
		super(BaseAdminModel.class);
	}

}
