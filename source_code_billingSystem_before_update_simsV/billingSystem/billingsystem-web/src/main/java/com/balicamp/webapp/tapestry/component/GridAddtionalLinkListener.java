package com.balicamp.webapp.tapestry.component;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;


public abstract class GridAddtionalLinkListener implements IActionListener {
	
	public abstract void onEdit(Object parameter);
	
	@Override
	public void actionTriggered(IComponent component,
			IRequestCycle cycle) {
		onEdit(cycle.getListenerParameters()[0]);
	}

	@Override
	public String getMethodName() {
		return null;
	}
}
