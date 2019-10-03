package com.balicamp.dao.hibernate.ikrap;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.ikrap.IkrapDao;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
@Transactional
public class IkrapDaoHibernate extends IkrapGenericDaoHibernate<BaseAdminModel, String> implements IkrapDao {

	public IkrapDaoHibernate() {
		super(BaseAdminModel.class);
	}

}
