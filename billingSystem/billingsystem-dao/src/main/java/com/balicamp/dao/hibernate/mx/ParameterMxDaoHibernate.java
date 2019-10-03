package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.balicamp.dao.hibernate.admin.AdminGenericDaoImpl;
import com.balicamp.dao.mx.ParameterMxDao;
import com.balicamp.model.mx.ParameterMx;
import com.balicamp.util.CommonUtil;

/**
 * @author Wayan Ari Agustina
 * @version $Id: ParameterMxDaoHibernate.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
@Repository
public class ParameterMxDaoHibernate extends AdminGenericDaoImpl<ParameterMx, Integer> implements ParameterMxDao {

	public ParameterMxDaoHibernate() {
		super(ParameterMx.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParameterMx> findAll(String description, int first, int max) {
		Criteria criteria = getSession().createCriteria(ParameterMx.class);
		if (!CommonUtil.isEmpty(description)) {
			criteria.add(Restrictions.like("descriptions", description, MatchMode.ANYWHERE));
		}
		List<ParameterMx> rs = criteria.list();
		return rs;
	}

	@Override
	public int getRowCount(String description) {
		Criteria criteria = getSession().createCriteria(ParameterMx.class);
		if (!CommonUtil.isEmpty(description)) {
			criteria.add(Restrictions.like("descriptions", description, MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		Object count = criteria.uniqueResult();
		return count == null ? 0 : (Integer) count;
	}

	@Override
	public void updateValues(List<ParameterMx> entites) throws Exception {
		try {
			for (ParameterMx entity : entites)
				saveOrUpdate(entity);
		} catch (Exception e) {
			throw e;
		}
	}

}
