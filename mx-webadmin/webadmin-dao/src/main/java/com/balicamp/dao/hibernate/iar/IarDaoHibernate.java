package com.balicamp.dao.hibernate.iar;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.iar.IarDao;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
@Transactional
public class IarDaoHibernate extends IarGenericDaoHibernate<BaseAdminModel, String> implements IarDao {

	public IarDaoHibernate() {
		super(BaseAdminModel.class);
	}

}
