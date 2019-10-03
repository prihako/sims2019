package com.balicamp.webapp.tapestry.component;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

public abstract class ActionListenerWrapper implements IActionListener {

	public abstract void onAction(Object parameter);

	@Override
	public void actionTriggered(IComponent component, IRequestCycle cycle) {
		Object[] params = cycle.getListenerParameters();
		if (params != null && params.length > 0)
			onAction(cycle.getListenerParameters()[0]);
	}

	@Override
	public String getMethodName() {
		return null;
	}

}
