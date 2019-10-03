package com.balicamp.webapp.action.bankadmin;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.valid.ValidationConstraint;

import com.balicamp.model.page.WaitingPageCommand;
import com.balicamp.model.user.Role;
import com.balicamp.model.user.User;
import com.balicamp.service.user.RoleManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.CommonUtil;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.bankadmin.user.UserConfirm;
import com.balicamp.webapp.action.bankadmin.user.UserEntry;
import com.balicamp.webapp.action.common.WaitingPage2;

public abstract class MemberEntry extends AdminBasePage implements
		PageBeginRenderListener {

	protected final Log log = LogFactory.getLog(UserEntry.class);

	@InjectObject("spring:roleManager")
	public abstract RoleManager getRoleManager();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	@InjectPage("userConfirm")
	public abstract UserConfirm getUserConfirm();

	private BeanPropertySelectionModel roleModel = null;

	public BeanPropertySelectionModel getRoleModel() {
		// if (roleModel == null) {
		List<Role> roleList = getRoleManager().getEditableAdminRoleList();
		roleModel = new BeanPropertySelectionModel(roleList, "name");
		// }
		return roleModel;
	}

	public abstract void setParentUsername(String parentUsername);

	public abstract String getParentUsername();

	public abstract void setParentCardNumber(String parentCardNumber);

	public abstract String getParentCardNumber();

	public abstract void setParentSourceTypeRadio(Integer parentSourceTypeRadio);

	public abstract Integer getParentSourceTypeRadio();

	public abstract void setMemberUsername(String memberUsername);

	public abstract String getMemberUsername();

	public abstract void setMemberCardNumber(String memberCardNumber);

	public abstract String getMemberCardNumber();

	public abstract void setMemberSourceTypeRadio(Integer memberSourceTypeRadio);

	public abstract Integer getMemberSourceTypeRadio();

	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	@InjectPage("waitingPage2")
	public abstract WaitingPage2 getWaitingPage();

	public IPage doSubmit(IRequestCycle cycle) {
		if (getDelegate().getHasErrors())
			return null;
		serverValidate();
		if (getDelegate().getHasErrors())
			return null;

		String parentCard = "";
		if (getParentSourceTypeRadio() == 1) {
			parentCard = getUserManager().findByUserName(getParentUsername())
					.getCardNumber().toString();
		} else {
			parentCard = getParentCardNumber();
		}

		String memberCard = "";
		if (getMemberSourceTypeRadio() == 1) {
			memberCard = getUserManager().findByUserName(getMemberUsername())
					.getCardNumber().toString();
		} else {
			memberCard = getMemberCardNumber();
		}

		/*
		 * CardNumberBeneficiary data = new CardNumberBeneficiary();
		 * data.setCardNumber(parentCard);
		 * data.setType(MessageConstant.TransactionType.CARD_NUMBER);
		 * data.setOperationCode(BeanOperationCode.INQUIRY);
		 * data.setTransactionDate(new Date()); data.setTransactionTime(new
		 * Date()); data.setNewCardNumber(memberCard);
		 */

		WaitingPageCommand cmd = new WaitingPageCommand();
		cmd.setPreviousPage(getPageName());

		cmd.setNextPage("memberConfirm");
		cmd.setNextPageInitMethod("setModel");

		return null;// FIXME : getWaitingPage().send(cmd, data, cycle);
	}

	private void serverValidate() {
		// cekDoubleSubmit();
		User parent = new User();
		User member = new User();

		if (getParentSourceTypeRadio() == null) {
			addError(getDelegate(), "parentUsername",
					getText("memberEntry.error"),
					ValidationConstraint.CONSISTENCY);
			return;
		}
		if (getMemberSourceTypeRadio() == null) {
			addError(getDelegate(), "memberUsername",
					getText("memberEntry.error"),
					ValidationConstraint.CONSISTENCY);
			return;
		}

		// cek jika username induk dan anggota sama
		if (getParentSourceTypeRadio() == getMemberSourceTypeRadio()
				&& getParentSourceTypeRadio() == 1) {
			if (getParentUsername().equals(getMemberUsername())) {
				addError(getDelegate(), "parentUsername",
						getText("memberEntry.error.same.username"),
						ValidationConstraint.CONSISTENCY);
				return;
			}
		}
		// cek jika nomer kartu sama
		if (getParentSourceTypeRadio() == getMemberSourceTypeRadio()
				&& getParentSourceTypeRadio() == 2) {
			if (getParentCardNumber().equals(getMemberCardNumber())) {
				addError(getDelegate(), "parentCardNumber",
						getText("memberEntry.error.same.cardNumber"),
						ValidationConstraint.CONSISTENCY);
				return;
			}
		}

		// cek tidak boleh kosong
		if (getParentSourceTypeRadio() == 1) {
			if (CommonUtil.isEmpty(getParentUsername())) {
				addError(getDelegate(), "parentUsername",
						getText("memberEntry.error.empty.username"),
						ValidationConstraint.CONSISTENCY);
				return;
			}
			// cek parent username terdaftar atau tidak
			if (getUserManager().findByUserName(getParentUsername()) == null) {
				addError(getDelegate(), "parentUsername",
						getText("memberEntry.error.username"),
						ValidationConstraint.CONSISTENCY);
				return;
			} else {
				parent = getUserManager().findByUserName(getParentUsername());
			}
		} else {
			if (CommonUtil.isEmpty(getParentCardNumber())) {
				addError(getDelegate(), "parentCardNumber",
						getText("memberEntry.error.empty.cardNumber"),
						ValidationConstraint.CONSISTENCY);
				return;
			}
			if (getUserManager().findByCardnumber(getParentCardNumber()) == null) {
				addError(getDelegate(), "parentCardNumber",
						getText("memberEntry.error.cardNumber"),
						ValidationConstraint.CONSISTENCY);
				return;
			} else {
				parent = getUserManager().findByCardnumber(
						getParentCardNumber());
			}
		}

		// cek username tidak boleh kosong
		if (getMemberSourceTypeRadio() == 1) {
			if (CommonUtil.isEmpty(getMemberUsername())) {
				addError(getDelegate(), "memberUsername",
						getText("memberEntry.error.empty.username"),
						ValidationConstraint.CONSISTENCY);
				return;
			}
			// cek apakah username terdaftar
			if (getUserManager().findByUserName(getMemberUsername()) == null) {
				addError(getDelegate(), "memberUsername",
						getText("memberEntry.error.username"),
						ValidationConstraint.CONSISTENCY);
				return;
			} else {
				member = getUserManager().findByUserName(getMemberUsername());
			}
		} else {
			if (CommonUtil.isEmpty(getMemberCardNumber())) {
				addError(getDelegate(), "memberCardNumber",
						getText("memberEntry.error.empty.cardNumber"),
						ValidationConstraint.CONSISTENCY);
				return;
			}
			if (getUserManager().findByCardnumber(getMemberCardNumber()) == null) {
				addError(getDelegate(), "memberCardNumber",
						getText("memberEntry.error.cardNumber"),
						ValidationConstraint.CONSISTENCY);
				return;
			} else {
				member = getUserManager().findByCardnumber(
						getMemberCardNumber());
			}
		}

		if (!new Long(0).equals(member.getUserParentId())) {
			addError(getDelegate(), "parentCardNumber",
					getText("memberEntry.error.alreadyChild"),
					ValidationConstraint.CONSISTENCY);
			return;
		}

		// cek apakah sudah pasangan parent member
		if (member.getUserParentId() == parent.getId()) {
			addError(getDelegate(), "parentCardNumber",
					getText("memberEntry.error.alreadyLinked"),
					ValidationConstraint.CONSISTENCY);
			return;
		}

	}

	public static final int USERNAME = 1;
	public static final int CARDNUMBER = 2;
}
