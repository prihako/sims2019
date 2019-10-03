package com.balicamp.webapp.action.priority;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.BeanPropertySelectionModel;

import com.balicamp.webapp.model.priority.ObjectModel;

/**
 * @author <a href="mailto:snurma.wijayanti@gmail.com">antin</a>
 * @version
 */
public abstract class PriorityEntry extends PriorityCommonBean {

	protected final static Log log = LogFactory.getLog(PriorityEntry.class);

	public abstract void setErrormsg(String errormsg);

	public abstract String getErrormsg();

	private BeanPropertySelectionModel transactionCodeModel = null;

	public BeanPropertySelectionModel getTransactionCodeModel() {
		final List<ObjectModel> criteriaList = getTransactionCodeList();
		transactionCodeModel = new BeanPropertySelectionModel(criteriaList,
				"obj");
		return transactionCodeModel;
	}

	public abstract void setTransactionCodeTr(ObjectModel transactionCodeTr);

	@Persist("client")
	public abstract ObjectModel getTransactionCodeTr();

	private BeanPropertySelectionModel projectCodeModel = null;

	public BeanPropertySelectionModel getProjectCodeModel() {
		final List<ObjectModel> criteriaList = getProjectCodeList();
		projectCodeModel = new BeanPropertySelectionModel(criteriaList, "obj");
		return projectCodeModel;
	}

	public abstract void setProjectCodeTr(ObjectModel projectCodeTr);

	@Persist("client")
	public abstract ObjectModel getProjectCodeTr();

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

	@Persist("client")
	public abstract String getProductCodeExt();

	public abstract void setProductCodeExt(String productCodeExt);

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
			if (getTransactionCodeTr() == null || getProjectCodeTr() == null
					|| getProductCode() == null || getProductCodeExt() == null
					|| getDescription() == null || getRoutingCode() == null || getGlCode() == null) {
				setErrormsg(getText("priority.error.entry.trxcode.notnull"));
				return null;
			} else {
				setErrormsg(null);
			}

			PriorityConfirm nextPage = getPriorityConfirm();
			if (nextPage != null) {
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
			log.error(e.getCause(), e);
		}
		return null;
	}
	
	public static void main(String[] args){
		boolean b = Pattern.matches("\\w", "b");
		
		System.out.println(b);
	}

}