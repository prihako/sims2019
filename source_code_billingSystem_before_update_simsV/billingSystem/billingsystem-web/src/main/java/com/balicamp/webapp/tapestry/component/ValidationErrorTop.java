package com.balicamp.webapp.tapestry.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.Submit;
import org.apache.tapestry.valid.IFieldTracking;

import com.balicamp.util.CommonUtil;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public abstract class ValidationErrorTop extends BaseComponent {
	public abstract IFieldTracking getCurrentFieldTracking();

	@Override
	protected void renderComponent(IMarkupWriter arg0, IRequestCycle arg1) {
		super.renderComponent(arg0, arg1);
	}

	public boolean isShowError() {
		if (getCurrentFieldTracking().isInError()) {
			if (CommonUtil.isEmpty(getCurrentFieldTracking().getFieldName()))
				return true;

			if (getCurrentFieldTracking().getComponent() instanceof Submit)
				return true;
		}
		return false;
	}

}