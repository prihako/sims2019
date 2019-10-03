package com.balicamp.webapp.action.priority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.model.user.Approval;
import com.balicamp.service.MessageLogsManager;
import com.balicamp.service.priority.PriorityManager;
import com.balicamp.service.user.ApprovalManager;
import com.balicamp.webapp.constant.PriorityConstant.ApprovalStatus;
import com.balicamp.webapp.constant.PriorityConstant.Parameter;
import com.balicamp.webapp.model.priority.ObjectModel;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

/**
 *
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version
 */
public abstract class PriorityAppList extends PriorityCommonBean {
	private static final Logger LOGGER = Logger.getLogger(PriorityAppList.class.getSimpleName());

	public abstract void setMsgnotif(String msgnotif);

	public abstract String getMsgnotif();

	public abstract void setKeys(String keys);

	@Persist("client")
	public abstract String getKeys();

	@InjectSpring("messageLogsManagerImpl")
	public abstract MessageLogsManager getMessageLogsManager();

	@InjectSpring("approvalManager")
	public abstract ApprovalManager getApprovalManager();

	@InjectSpring("priorityManager")
	public abstract PriorityManager getPriorityManager();

	@InjectPage("priorityAppConfirm")
	public abstract PriorityAppConfirm getPriorityAppConfirm();

	private BeanPropertySelectionModel criteriaModel = null;

	public BeanPropertySelectionModel getCriteriaModel() {
		final List<ObjectModel> criteriaList = Parameter.getCriteria();
		criteriaModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return criteriaModel;
	}

	public abstract void setCriteria(ObjectModel criteria);

	public abstract ObjectModel getCriteria();

	public abstract void setUniqueSet(Set<PriorityRouting> selectedIds);

	public abstract Set<PriorityRouting> getUniqueSet();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<PriorityRouting>());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	/**
	 *
	 * @param cycle
	 */
	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			PriorityRouting priorityRouting = (PriorityRouting) getLoopObject();
			if (priorityRouting != null) {
				Set<PriorityRouting> uniqueSet = getUniqueSet();
				if (priorityRouting.isSelected()) {
					uniqueSet.add(priorityRouting);
				} else
					uniqueSet.remove(priorityRouting);
				setUniqueSet(uniqueSet);
			}
		}
	}

	/**
	*
	* @return
	*/
	public IPage onSearch() {
		setRealRender(true);
		if (getTableModel().getRowCount() <= 0) {
			setMsgnotif(getText("priority.message.notif.notfound", new Object[] { getKeys() }));
		} else {
			setMsgnotif(null);
		}
		return null;
	}

	@Override
	public IBasicTableModel createTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				try {
					if (getCriteria() != null && getKeys() != null && !getKeys().equals("")) {
						return getApprovalManager().findCountPriorityApproval(getCriteria().getKey(), getKeys());
					} else {
						return getApprovalManager().findCountPriorityApproval();
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					return 0;
				}
			}

			@Override
			public Iterator<PriorityRouting> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				try {
					List<PriorityRouting> results = new ArrayList<PriorityRouting>();
					List<Approval> apprs;

					if (getCriteria() != null && getKeys() != null && !getKeys().equals("")) {
						apprs = getApprovalManager().findPriorityApproval(getCriteria().getKey(), getKeys(), nFirst,
								nPageSize);

					} else {
						apprs = getApprovalManager().findPriorityApproval(nFirst, nPageSize);
					}

					if (apprs != null) {
						for (Approval appr : apprs) {
							PriorityRouting priority = getApprovalManager().getDataPriority(appr.getData());
							if(priority != null){
								priority.setAppStatusTr(ApprovalStatus.getAppStatus(appr.getStatus()));
								results.add(priority);
							}
						}
					}
					Iterator<PriorityRouting> rows = results.iterator();
					return rows;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new ArrayList<PriorityRouting>().iterator();
			}
		};
	}

	/**
	 *
	 * @return
	 */
	public IPage onViewDetail(Long refId) {
		PriorityAppConfirm conf = getPriorityAppConfirm();
		conf.setPriority(getApprovalManager().getDataPriority(refId));
		conf.setDataApp(getApprovalManager().getDataByRefId(refId));
		return conf;
	}

}
