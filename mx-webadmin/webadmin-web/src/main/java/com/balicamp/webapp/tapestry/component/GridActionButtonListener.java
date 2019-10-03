package com.balicamp.webapp.tapestry.component;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

public abstract class GridActionButtonListener implements IActionListener {

	public abstract void onAction(Object parameter);
	
	@Override
	public void actionTriggered(IComponent component,
			IRequestCycle cycle) {
		onAction(cycle.getListenerParameters()[0]);
	}

	@Override
	public String getMethodName() {
		return "onDelete";
	}
}
