package com.balicamp.webapp.action.bankadmin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.concurrent.SessionRegistry;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericTableModel;

public abstract class MemberList extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {
	@InjectPage("memberDeleteConfirm")
	public abstract MemberDeleteConfirm getMemberDeleteConfirm();

	@InjectPage("memberEntry")
	public abstract MemberEntry getMemberEntry();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	public abstract User getSearchUser();

	public abstract void setSearchUser(User searchUser);

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		populateSessionPropertyValue();

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<UserDisplay>());
		if (getSearchUser() == null)
			setSearchUser(new User());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

		//save last state to session
		syncSessionPropertyValue(new String[] { "searchUser" });
	}

	public IBasicTableModel getTableModel() {
		if (isRealRender()) {
			final SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("user");

			searchCriteria.addCriterion(Restrictions.eq("enabled", true));
			//searchCriteria.addCriterion(Restrictions.eq("status", ModelConstant.User.STATUS_ADMIN));
			searchCriteria.addCriterion(Restrictions.ne("userParentId", ModelConstant.User.STATUS_PARENT));

			if (!CommonUtil.isEmpty(getSearchUser().getUsername()))
				searchCriteria.addCriterion(Restrictions.ilike("userName", getSearchUser().getUsername(),
						MatchMode.ANYWHERE));

			return new IBasicTableModel() {
				private Integer rowCount = null;

				public Iterator<UserDisplay> getCurrentPageRows(int first, int pageSize, ITableColumn objSortColumn,
						boolean sortOrder) {
					return getUserManager().findUser(searchCriteria, first, pageSize).iterator();
				}

				public int getRowCount() {
					if (rowCount == null) {
						rowCount = getUserManager().findByCriteriaCount(searchCriteria);
					}

					return rowCount.intValue();
				}
			};
		}
		//do not need table while rewinding
		return new GenericTableModel<UserDisplay>(null, null, (Map) null);
	}

	public abstract void setUniqueSet(Set<UserDisplay> selectedIds);

	public abstract Set<UserDisplay> getUniqueSet();

	public void checkSelect(IRequestCycle cycle) {
		if (cycle.isRewinding()) {
			UserDisplay userDisplay = (UserDisplay) getLoopObject();
			if (userDisplay != null) {
				Set<UserDisplay> uniqueSet = getUniqueSet();
				if (userDisplay.isSelected()) {
					uniqueSet.add(userDisplay);
				} else
					uniqueSet.remove(userDisplay);
				setUniqueSet(uniqueSet);
			}
		}
	}

	/**
	 * Listener search button
	 * @return
	 */
	public IPage onSearch() {
		//do nothing just refresh
		return null;
	}

	public IPage doDelete() {

		validateServerBeforeDelete();

		if (getDelegate().getHasErrors())
			return null;

		if (getUniqueSet().size() == 0) {
			addError(getDelegate(), (IFormComponent) null, getText("userList.required"), ValidationConstraint.REQUIRED);
			return null;
		}

		MemberDeleteConfirm memberDeleteConfirm = getMemberDeleteConfirm();
		memberDeleteConfirm.setMemberToDelete(getUniqueSet());
		return memberDeleteConfirm;
	}

	private void validateServerBeforeDelete() {

		if (getDelegate().getHasErrors())
			return;

		Object[] allPrincipals = getSessionRegistry().getAllPrincipals();

		for (UserDisplay userDisplay : getUniqueSet()) {
			for (Object tmpObject : allPrincipals) {
				String userNameLogin = (String) tmpObject;
				if (userDisplay.getUserName().equals(userNameLogin)) {
					addError(getDelegate(), (IFormComponent) null,
							getText("userList.message.deleteFail.userLogin", userNameLogin),
							ValidationConstraint.CONSISTENCY);
					break;
				}
			}
			if (getDelegate().getHasErrors())
				return;

		}

	}

	public IPage doEdit(Long userId, String userName, String parentUsername) {
		MemberEntry memberEntry = getMemberEntry();
		memberEntry.setMemberUsername(userName);
		memberEntry.setParentUsername(parentUsername);
		memberEntry.setParentSourceTypeRadio(1);
		memberEntry.setMemberSourceTypeRadio(1);
		return memberEntry;
	}

	public boolean isResetLinkDisable() {
		UserDisplay userDisplay = (UserDisplay) getLoopObject();

		if (CommonUtil.isEmpty(userDisplay.getEmail()))
			return true;

		if (userDisplay.getUserName().equals(getUserLoginFromSession().getUsername()))
			return true;

		return false;
	}

}
