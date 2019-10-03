package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.acegisecurity.concurrent.SessionRegistry;
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

import com.balicamp.model.mastermaintenance.license.LabelValue;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualRate;
import com.balicamp.model.page.InfoPageCommand;
import com.balicamp.service.mastermaintenance.variable.VariableAnnualRateManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.balicamp.webapp.action.common.InfoPage;
import com.balicamp.webapp.action.operational.InitialInvoiceVarietyRateCreate;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateEdit;
import com.balicamp.webapp.action.operational.ManageInvoiceVarietyRateView;

public abstract class AnnualRateBHPViewManage extends AdminBasePage implements
		PageBeginRenderListener {

	protected final Log log = LogFactory.getLog(AnnualRateBHPViewManage.class);

	@InjectObject("spring:variableAnnualRateManager")
	public abstract VariableAnnualRateManager getVariableAnnualRateManager();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
	}
	
//	public VariableAnnualRate getVariableAnnualRate(){
//		List<VariableAnnualRate> rates = getVariableAnnualRateManager().findByStatus(1);
//		VariableAnnualRate rate = rates.get(0);
//		
//		return rate;
//	}
	
	public VariableAnnualRate getVariableAnnualRate(){
		
		VariableAnnualRate result = (VariableAnnualRate) getObjectfromSession("BI_RATE_DETAIL_LIST");
		
		if(result == null){
			List<VariableAnnualRate> rateList = getVariableAnnualRateManager().findByStatus(1);
			if(rateList != null){
				result = rateList.get(0);
			}
		}
		
		return result;
	}
}
