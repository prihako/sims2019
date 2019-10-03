package com.balicamp.webapp.action.webadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.user.Role;
import com.balicamp.service.EndpointsManager;
import com.balicamp.webapp.action.AdminBasePage;
import com.javaforge.tapestry.spring.annotations.InjectSpring;

import edu.emory.mathcs.backport.java.util.Collections;

public abstract class ViewConnectionMonitoring extends AdminBasePage implements PageBeginRenderListener,
		PageEndRenderListener {

	@InjectSpring("endpointsManagerImpl")
	public abstract EndpointsManager getEndpointsManager();

	public abstract Endpoints getEndpointData();

	public abstract void setEndpointData(Endpoints e);

	@Override
	public void pageBeginRender(PageEvent event) {

		super.pageBeginRender(event);

	}

	@Override
	public void pageEndRender(PageEvent event) {
		super.pageEndRender(event);

	}

	public List<Endpoints> getEndpointList() {
		if (getUserLoginFromDatabase() == null) {
			try {
				getResponse().sendRedirect(getBaseUrl() + "/main.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<Endpoints> endpoints = new ArrayList<Endpoints>();
		Set<Role> roles = getUserLoginFromDatabase().getRoleSet();

		for (Role tempRole : roles) {
			if (tempRole.getName().equalsIgnoreCase("ADMIN")) {
				endpoints = new ArrayList<Endpoints>();
				endpoints.addAll(getEndpointsManager().getAllEndpointsByState("ready"));

				return sortEndpoints(endpoints);
			} else if (getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()) != null) {
				endpoints.add(getEndpointsManager().findEndpointsByCode(tempRole.getName().toLowerCase()));
			}
		}

		return sortEndpoints(endpoints);
	}

	public List<Endpoints> sortEndpoints(List<Endpoints> list) {
		if (list.size() > 1) {
			Collections.sort(list, new Comparator<Endpoints>() {

				@Override
				public int compare(Endpoints end1, Endpoints end2) {
					return end1.getName().compareTo(end2.getName());
				}

			});
			return list;
		} else {
			return list;
		}
	}

}
