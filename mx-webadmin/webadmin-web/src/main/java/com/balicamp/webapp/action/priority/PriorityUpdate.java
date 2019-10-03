package com.balicamp.webapp.action.priority;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.model.mx.PriorityRouting;
import com.balicamp.webapp.constant.PriorityConstant.FromPage;
import com.balicamp.webapp.model.priority.ObjectModel;

public abstract class PriorityUpdate extends PriorityCommonBean {

	protected final static Logger LOG = Logger.getLogger(PriorityEntry.class.getName());

	public abstract void setErrormsg(String errormsg);

	public abstract String getErrormsg();

	public abstract void setTransactionCodeModel(BeanPropertySelectionModel transactionCodeModel);

	@Persist("client")
	public abstract BeanPropertySelectionModel getTransactionCodeModel();

	public abstract void setTransactionCodeTr(ObjectModel transactionCodeTr);

	@Persist("client")
	public abstract ObjectModel getTransactionCodeTr();

	public abstract void setProjectCodeModel(BeanPropertySelectionModel projectCodeModel);

	@Persist("client")
	public abstract BeanPropertySelectionModel getProjectCodeModel();

	public abstract void setProjectCodeTr(ObjectModel projectCodeTr);

	@Persist("client")
	public abstract ObjectModel getProjectCodeTr();

	public abstract void setPriorityRouting(PriorityRouting priorityRouting);

	@Persist("client")
	public abstract PriorityRouting getPriorityRouting();

	public abstract void setTransactionCode(String transactionCode);

	@Persist("client")
	public abstract String getTransactionCode();

	public abstract void setProjectCode(String projectCode);

	@Persist("client")
	public abstract String getProjectCode();

	public abstract void setDescription(String description);

	@Persist("client")
	public abstract String getDescription();

	public abstract void setProductCode(String productCode);

	@Persist("client")
	public abstract String getProductCode();

	public abstract void setRoutingCode(String routingCode);

	@Persist("client")
	public abstract String getRoutingCode();

	public abstract void setProductCodeExt(String productCodeExt);

	@Persist("client")
	public abstract String getProductCodeExt();

	@Persist("client")
	public abstract String getGlCode();

	public abstract void setGlCode(String glCode);

	@InjectPage("priorityConfirm")
	public abstract PriorityConfirm getPriorityConfirm();

	public IPage doSubmit(IRequestCycle cycle) {
		try {
			if (getDelegate().getHasErrors()) {
				return null;
			}

			// cekDoubleSubmit();
			if (getDelegate().getHasErrors()) {
				return null;
			}
			// serverValidate();
			if (getDelegate().getHasErrors()) {
				return null;
			}

			// cek trxCode
			if (getTransactionCodeTr() == null || getProjectCodeTr() == null) {
				setErrormsg(getText("priority.error.entry.trxcode.notnull"));
				return null;
			} else {
				setErrormsg(null);
			}

			PriorityConfirm nextPage = getPriorityConfirm();

			if (nextPage != null) {
				nextPage.setFromPage(FromPage.UPDATE);

				nextPage.setPriorityRouting(getPriorityRouting());

				nextPage.setTransactionCode(getTransactionCodeTr().getKey());
				nextPage.setProjectCode(getProjectCodeTr().getKey());
				nextPage.setDescription(getDescription());
				nextPage.setProductCode(getProductCode());
				nextPage.setRoutingCode(getRoutingCode());
				nextPage.setProductCodeExt(getProductCodeExt());
				nextPage.setGlCode(getGlCode());
			}

			return nextPage;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}
}