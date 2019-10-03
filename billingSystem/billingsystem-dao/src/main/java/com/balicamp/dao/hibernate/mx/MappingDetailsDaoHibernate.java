package com.balicamp.dao.hibernate.mx;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.balicamp.dao.mx.MappingDetailsDao;
import com.balicamp.model.mx.MappingDetails;
import com.balicamp.model.mx.ParameterMx;

@Repository
public class MappingDetailsDaoHibernate extends 
	MxGenericDaoHibernate<MappingDetails, Integer> implements MappingDetailsDao {

	public MappingDetailsDaoHibernate() {
		super(MappingDetails.class);
	}

	@Override
	public void updateValues(List<ParameterMx> entites) throws Exception {
		try {
			for(ParameterMx entity : entites) {
				MappingDetails md = findById(entity.getIdMappingDetail());
				md.setSource(entity.getValueBaru());
				saveOrUpdate(md);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
