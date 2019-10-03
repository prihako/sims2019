package com.balicamp.dao.hibernate.report;

import java.util.List;

import com.balicamp.dao.GenericDao;
import com.balicamp.model.mx.TransactionLogs;

public interface ReportCriteriaWraperDao extends GenericDao<TransactionLogs, Long> {

	List<TransactionLogs> testCriteriaWrapper(String criteriaHQL);

}
