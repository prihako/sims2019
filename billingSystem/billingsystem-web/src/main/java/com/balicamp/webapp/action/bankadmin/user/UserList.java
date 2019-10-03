package com.balicamp.webapp.action.bankadmin.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.concurrent.SessionRegistry;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
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
import com.balicamp.model.user.User;
import com.balicamp.model.user.UserDisplay;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.BasePageList;
import com.balicamp.webapp.tapestry.GenericTableModel;

public abstract class UserList extends BasePageList implements PageBeginRenderListener, PageEndRenderListener {
	@InjectPage("userDeleteConfirm")
	public abstract UserDeleteConfirm getUserDeleteConfirm();

	@InjectPage("userEntryEdit")
	public abstract UserEntryEdit getUserEntryEdit();

	@InjectPage("resetPasswordConfirm")
	public abstract ResetPasswordConfirm getUserResetConfirm();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectObject("spring:sessionRegistry")
	public abstract SessionRegistry getSessionRegistry();

	@Persist("client")
	public abstract String getUserId();

	public abstract void setUserId(String userId);

	@InjectPage("userNotFound")
	public abstract UserNotFound getUserNotFoundPage();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<UserDisplay>());
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IBasicTableModel getTableModel() {
		if (isRealRender()) {
			final SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("user");

			searchCriteria.addCriterion(Restrictions.eq("enabled", true));
			searchCriteria.addCriterion(Restrictions.or(Restrictions.eq("status", ModelConstant.User.STATUS_ADMIN),
					Restrictions.ilike("userFullName", "BPRKS%")));
			searchCriteria.addOrder(Order.asc("userName"));

			if (!CommonUtil.isEmpty(getUserId()))
				searchCriteria.addCriterion(Restrictions.ilike("userName", getUserId(), MatchMode.ANYWHERE));

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

	/**
	 * for object looping
	 * 
	 * @return
	 */
	public abstract Object getLoopObject();

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
		setRealRender(true);
		if (getTableModel().getRowCount() <= 0) {
			UserNotFound unf = getUserNotFoundPage();
			unf.setuName(getUserId());
			return unf;
		}
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

		UserDeleteConfirm userDeleteConfirm = getUserDeleteConfirm();
		userDeleteConfirm.setUserToDelete(getUniqueSet());
		userDeleteConfirm.setSearchUserId(getUserId());
		return userDeleteConfirm;
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

	public IPage doEdit(IRequestCycle cycle, Long userId, Long roleId, String userName, String email, String name) {
		//UserEntryEdit userEntryEdit = getUserEntryEdit();
		
		UserEntryEdit userEntryEdit = (UserEntryEdit) cycle.getPage("userEntryEdit");
		userEntryEdit.setUserId(userId);
		userEntryEdit.setRoleId(roleId);
		userEntryEdit.setUserName(userName);
		userEntryEdit.setEmail(email);
		userEntryEdit.setName(name);
		userEntryEdit.setSearchUserId(getUserId());
		return userEntryEdit;
		/*
		 * UserEntry userEntry = (UserEntry) cycle.getPage("userEntry");
		 * userEntry.setUserName(userName);
		 * return userEntry;
		 */
	}

	public IPage onResetPassword(IRequestCycle cycle, Long userId) {
		User user = getUserManager().findById(userId);
		getUserResetConfirm().setEntity(user);
		return getUserResetConfirm();
	}

	public boolean isResetLinkDisable() {
		UserDisplay userDisplay = (UserDisplay) getLoopObject();

		if (CommonUtil.isEmpty(userDisplay.getEmail()))
			return true;

		//		if ( userDisplay.getUserName().equals(getUserLoginFromSession().getUsername()) )
		//			return true;

		return false;
	}

}
