package com.balicamp.webapp.action;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.Constants;
import com.balicamp.model.constant.ModelConstant;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.user.User;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.service.operational.LicenseManager;
import com.balicamp.service.user.UserManager;
import com.balicamp.util.DateFormatSymbolsIn;
import com.balicamp.util.SecurityContextUtil;
import com.balicamp.webapp.constant.WebConstant;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class Wellcome extends AdminBasePage implements
		PageBeginRenderListener {

	@SuppressWarnings("unused")
	private String notification = "";
	
	private String MCHP = "Change Password";

	public abstract void setInformation(String information);

	public abstract String getInformation();

	public abstract void setUnreadMailCount(Integer unreadMailCount);

	public abstract Integer getUnreadMailCount();
	
	public abstract Object getData();

	@InjectObject("spring:userManager")
	public abstract UserManager getUserManager();

	/*
	 * @InjectObject("spring:internalEmailManager") public abstract
	 * InternalEmailManager getInternalEmailManager();
	 */

	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		if (isRealRender()) {
			String msg = pageEvent.getRequestCycle().getParameter("msg");
			if (msg != null) {
				setInformation(getText(msg));
			} else {
				setInformation("");
			}
		} else {
			if (getUnreadMailCount() == null) {
				setUnreadMailCount(0);
			}
		}
	}

	public User getUserLoginFromSession() {
		return super.getUserLoginFromSession();
	}

	public Date getLastLogin() {
		return (Date) getSession().getAttribute(
				Constants.HttpSessionAttribute.LAST_LOGIN_DATE);
	}

	@SuppressWarnings("unused")
	public boolean isShowActivationPageLink() {
		String userStatus = getUserLoginFromSession().getStatus();
		String userSecurityType = getUserLoginFromSession().getSecurityType();

		if (userStatus.equals(ModelConstant.User.STATUS_INQUIRY_ONLY))
			return true;

		return false;
	}

	public Format getDateFormatWellcome() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm",
				new DateFormatSymbolsIn());
	}

	public String getUnreadMail() {
		if (SecurityContextUtil.getCurrentUser().getStatus()
				.equalsIgnoreCase(ModelConstant.User.STATUS_ADMIN))
			return getText("wellcome.unreadMailAdmin", " "
					+ getUnreadMailCount());
		return getText("wellcome.unreadMail", " " + getUnreadMailCount());
	}

	public boolean isCustomer() {
		return !SecurityContextUtil.getCurrentUser().getStatus()
				.equals(ModelConstant.User.STATUS_ADMIN);
	}

	public String getNotification() {
		User user = getUserLoginFromSession();
		if (user.isMustChangePassword()) {
			// return getText("login.passwordhint.change");
			return WebConstant.CONFIRM_PASSWORD_CHANGE;
		}
		return "";
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getMCHP() {
		if (getUserLoginFromSession().isMustChangePassword())
			return MCHP;
		else
			return "";
	}

	public void setMCHP(String mCHP) {
		MCHP = mCHP;
	}

	public void gotoCHP(IRequestCycle cycle) {
		cycle.activate(cycle.getPage("changePassword"));
	}
	
	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
//	Prihako added this for tell user if bi rate for this year is not active
	public String getNewsBiRate(){
		String message = null;
		
		Date now = new Date();
		SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		Integer yearNow = Integer.valueOf(formatYear.format(now));
		
		List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
		if(rateList.size() > 0){
			VariableAnnualRate rate = rateList.get(0);
			Integer biYear = rate.getRateYear().intValue();
			
			if(biYear < yearNow){
				message = getText("wellcome.info.biRateNotActive");
			}
		}else{
			message = getText("wellcome.info.thereIsNoActiveBiRate");
		}
			
		return message;
	}
	
	//alert for invoice due date - Hendy H. Y. - 6/5/2015
	
	@InjectObject("spring:licenseManager")
	public abstract LicenseManager getLicenseManager();
	
	public List<Object> getInvoiceList(){
		List<Object> invoiceList 	= null;
		invoiceList 				= getLicenseManager().findInvoiceDueDate();
			
		return invoiceList;
	}
}