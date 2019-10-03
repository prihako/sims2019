package com.balicamp.webapp.action.bankadmin.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.log.AuditLog;
import com.balicamp.model.ui.DefaultPropertySelectionData;
import com.balicamp.model.ui.PropertySelectionData;
import com.balicamp.service.log.AuditLogManager;
import com.balicamp.service.report.impl.ReportBase;
import com.balicamp.service.report.impl.ReportUserActivity;
import com.balicamp.util.DateUtil;
import com.balicamp.webapp.tapestry.GenericPropertySelectionModel;
import com.balicamp.webapp.tapestry.GenericTableModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 * @version $Id: ReportAuditLog.java 368 2013-03-08 04:09:59Z wayan.agustina $
 */
public abstract class ReportAuditLog extends ReportBasePage implements PageBeginRenderListener, PageEndRenderListener {
	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract Date getEndDate();

	public abstract void setEndDate(Date endDate);

	public abstract String getUserName();

	public abstract void setUserName(String userName);

	public abstract PropertySelectionData getActivityType();

	public abstract void setActivityType(PropertySelectionData activityType);

	public abstract GenericPropertySelectionModel getActivityTypeModel();

	public abstract void setActivityTypeModel(GenericPropertySelectionModel activityTypeModel);

	@InjectSpring("auditLogManager")
	public abstract AuditLogManager getAuditLogManager();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		populateSessionPropertyValue();
		if (getActivityTypeModel() == null) {
			initActivityTypeModel();
		}
		if (getStartDate() == null)
			setStartDate(new Date());

		if (getEndDate() == null)
			setEndDate(new Date());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		// save last state to session
		syncSessionPropertyValue(new String[] { "activityType", "startDate", "endDate" });
	}

	private void initActivityTypeModel() {
		int[] auditLogList = ModelConstant.ReffNumType.AUDITLOG_LIST;
		List<PropertySelectionData> dataList = new ArrayList<PropertySelectionData>();
		for (int i = 0; i < auditLogList.length; i++) {
			String label = getText(ModelConstant.ReffNumType.BASE_KEY + "."
					+ ModelConstant.ReffNumType.AUDITLOG_MESSAGE_LIST[i]);
			String value = String.valueOf(auditLogList[i]);
			dataList.add(new DefaultPropertySelectionData(label, value, false));
		}
		setActivityTypeModel(new GenericPropertySelectionModel(dataList, getMessageSourceWrapper(), getLocale(), false,
				true));
	}

	public IBasicTableModel createTableModel() {
		if (isRealRender()) {
			if (getActivityType() == null) {
				return new GenericTableModel<AuditLog>(null, null, (Map) null);
			}

			final SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("auditLog");

			if (!getActivityType().getPsdValue().equals(GenericPropertySelectionModel.VALUE_SELECT_ALL)) {
				searchCriteria.addCriterion(Restrictions.like("dataType", getActivityType().getPsdValue(),
						MatchMode.START));
			}

			if (getUserName() != null && !getUserName().equals("")) {
				searchCriteria.addCriterion(Restrictions.eq("createdBy", getUserManager().findByUserName(getUserName())
						.getId()));
			}

			searchCriteria.addCriterion(Restrictions.between("createdDate", DateUtil.generateStartDate(getStartDate()),
					DateUtil.generateEndDate(getEndDate())));

			searchCriteria.addOrder(Order.desc("createdDate"));

			return new IBasicTableModel() {

				@Override
				public int getRowCount() {
					return getAuditLogManager().findByCriteriaCount(searchCriteria).intValue();
				}

				@SuppressWarnings("unchecked")
				@Override
				public Iterator<AuditLog> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
						boolean bSortOrder) {
					Set<Long> userIds = new HashSet<Long>();
					List<AuditLog> data = (List<AuditLog>) getAuditLogManager().findByCriteria(searchCriteria, nFirst,
							nPageSize);
					for (AuditLog al : data) {
						userIds.add(al.getCreatedBy());
					}
					Map<Long, String> userMap = getAuditLogManager().findUserByIds(userIds);
					for (AuditLog al : data) {
						al.setUserName(userMap.get(al.getCreatedBy()));
					}

					return data.iterator();
				}
			};

			// return new GenericTableModel<AuditLog>((IManager)
			// getAuditLogManager(), searchCriteria, orderMaping);
		}
		return null;
	}

	public IPage onOk(IRequestCycle cycle) {
		if (getDelegate().getHasErrors())
			return null;
		serverValidate();
		// save to audit log
		getUserManager().viewAuditLog(initUserLoginFromDatabase(), getRequest().getRemoteHost());

		// refresh page
		return this;
	}

	public void serverValidate() {
		super.serverValidate();
		if (getStartDate() == null) {
			IFormComponent startDateField = (IFormComponent) getComponent("startDateField");
			addError(getDelegate(), startDateField,
					getText("common.message.validation.required", new String[] { startDateField.getDisplayName() }),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (getEndDate() == null) {
			IFormComponent endDateField = (IFormComponent) getComponent("endDateField");
			addError(getDelegate(), endDateField,
					getText("common.message.validation.required", new String[] { endDateField.getDisplayName() }),
					ValidationConstraint.CONSISTENCY);
			return;
		}
	}

	@Override
	public void initReportUrl(String fileType) {
		Date startDate = getStartDate();
		Date endDate = getEndDate();

		List<String> parameterList = new ArrayList<String>();
		parameterList.add("reportUserActivity");
		parameterList.add(fileType);
		parameterList.add(ReportUserActivity.PARAM_KEY_START_TRX_DATE + "="
				+ ReportBase.urlParamDateFormat.print(startDate.getTime()));
		parameterList.add(ReportUserActivity.PARAM_KEY_END_TRX_DATE + "="
				+ ReportBase.urlParamDateFormat.print(endDate.getTime()));
		if (getUserName() != null && !getUserName().equals("")) {
			parameterList.add(ReportUserActivity.PARAM_KEY_USERID + "=" + getUserName());
		}

		if (getActivityType() != null && getActivityType().getPsdValue() != null
				&& !getActivityType().getPsdValue().equals(GenericPropertySelectionModel.VALUE_SELECT_ALL)) {
			parameterList.add(ReportUserActivity.PARAM_KEY_TRX_TYPE + "=" + getActivityType().getPsdValue());
		}

		setReportUrl(getReportService().getLink(false, parameterList.toArray()).getURL());
	}

}
