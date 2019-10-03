package com.balicamp.webapp.action.mastermaintenance.ipsfr.variabel;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentage;
import com.balicamp.model.mastermaintenance.variable.VariableAnnualPercentageDetail;
import com.balicamp.service.mastermaintenance.service.ServicesManager;
import com.balicamp.service.mastermaintenance.service.SubServicesManager;
import com.balicamp.service.mastermaintenance.variable.VariableDetailManager;
import com.balicamp.service.mastermaintenance.variable.VariableManager;
import com.balicamp.webapp.action.AdminBasePage;

public abstract class AnnualPercentageBHPViewManage extends AdminBasePage implements
		PageBeginRenderListener {
	
	public abstract void setNotFirstLoad(boolean firsloadFlag);
	public abstract boolean isNotFirstLoad();
	
	@Override
	public void pageBeginRender(PageEvent pageEvent) {
		super.pageBeginRender(pageEvent);
		
		if(!pageEvent.getRequestCycle().isRewinding()){
			if(!isNotFirstLoad()){
				setNotFirstLoad(true);
			}
		}
	}
	
	public abstract VariableAnnualPercentageDetail getRow();
	
	public VariableAnnualPercentage getVariableAnnualPercentage(){
		VariableAnnualPercentage variable = (VariableAnnualPercentage) getObjectfromSession("ANNUAL_PERCENTAGE");
		return variable;
	}
	
	public List<VariableAnnualPercentageDetail> getAnnualPercentageDetailList(){
		List<VariableAnnualPercentageDetail> list = (List<VariableAnnualPercentageDetail>) getObjectfromSession("ANNUAL_PERCETAGE_DETAIL_LIST");
		return list;
	}
	
}
