package com.balicamp.service.impl.rpc;

import org.springframework.stereotype.Service;

import com.balicamp.service.rpc.SchedulerMonitor;

@Service("schedulerMonitor")
public class SchedulerMonitorImpl implements SchedulerMonitor {

	public boolean isRunning() {		
		return true;
	}

}
