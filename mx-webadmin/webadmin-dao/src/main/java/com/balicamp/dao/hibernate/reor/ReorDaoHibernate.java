package com.balicamp.dao.hibernate.reor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.balicamp.dao.reor.ReorDao;
import com.balicamp.model.admin.BaseAdminModel;

@Repository
@Transactional
public class ReorDaoHibernate extends ReorGenericDaoHibernate<BaseAdminModel, String> implements ReorDao {

	public ReorDaoHibernate() {
		super(BaseAdminModel.class);
	}

}
