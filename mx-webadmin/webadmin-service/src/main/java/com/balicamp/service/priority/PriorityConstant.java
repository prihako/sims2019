package com.balicamp.service.priority;

import java.util.ArrayList;
import java.util.List;

public final class PriorityConstant {
	private PriorityConstant() {
	}

	public static final class Parameter {
		public static final List<String> getRoutingPospaid() {
			List<String> list = new ArrayList<String>();
			list.add("00303");
			list.add("00304");
			list.add("00117");
			list.add("00118");
			return list;
		}
	}
}
