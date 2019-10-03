package com.balicamp.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.balicamp.model.mx.AnalisaTransactionLogsDto;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.service.EndpointsManager;
import com.balicamp.service.TransactionLogsManager;
import com.google.gson.Gson;

public class Dashboard extends HttpServlet {
	protected final Log LOGGER = LogFactory.getLog(getClass());
	private Gson gson = new Gson();

	@Autowired
	public TransactionLogsManager transactionLogsManagerImpl;

	@Autowired
	public EndpointsManager endpointsManagerImpl;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action == null)
			return;

		if (action.equals("getTransactionChart")) {
			this.getChart(req, resp);
		} else if (action.equals("listEndpoint")) {
			this.listEndpoint(req, resp);
		} else if (action.equals("listTransaction")) {
			this.listTransaction(req, resp);
		} else if (action.equals("getLastTransaction")) {
			this.getLastTransaction(req, resp);
		} else if (action.equals("getSummaryTransaction")) {
			this.getSummaryTransaction(req, resp);
		}
	}

	private void getChart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long sysdate = new Date().getTime() - (1000 * 60 * 60);
		// Long sysdate = timeFrom;
		Map<String, Long> mapValue = new HashMap<String, Long>();
		Map<String, List<List<Long>>> map = new HashMap<String, List<List<Long>>>();
		List<List<Long>> listsSuccess = new ArrayList<List<Long>>();
		List<List<Long>> listsFail = new ArrayList<List<Long>>();
		List<List<Long>> listsTimeout = new ArrayList<List<Long>>();
		List<Long> list;

		try {
			while (sysdate <= new Date().getTime()) {
				Date dateFrom = new Date(sysdate);
				sysdate += (60000);
				Date dateTo = new Date(sysdate);

				mapValue = transactionLogsManagerImpl.getChartTotalTransaction(dateFrom, dateTo);

				// GET SUCCESS TRANSACTION
				list = new ArrayList<Long>();
				Long valueSuccess = mapValue.get("totalSuccess");

				list.add(sysdate);
				list.add(valueSuccess);
				listsSuccess.add(list);

				// GET FAILED TRANSACTION
				list = new ArrayList<Long>();
				Long valueFail = mapValue.get("totalFailed");

				list.add(sysdate);
				list.add(valueFail);
				listsFail.add(list);

				// GET TIMEOUT TRANSACTION
				list = new ArrayList<Long>();
				Long valueTimeout = mapValue.get("totalTimeOut");

				list.add(sysdate);
				list.add(valueTimeout);
				listsTimeout.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error Chart Data " + e.getMessage());
		}
		map.put("success", listsSuccess);
		map.put("fail", listsFail);
		map.put("timeout", listsTimeout);

		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(map));
	}

	private void listEndpoint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String state = req.getParameter("state");
		Integer countReady = 0;
		Integer countDiscn = 0;
		List<Endpoints> endpoints = endpointsManagerImpl.getListEndpointsByState(state);
		List<EndpointsHelper> listEndpoints = new ArrayList<EndpointsHelper>();
		if (endpoints != null && endpoints.size() > 0) {
			for (Endpoints endpoint : endpoints) {
				EndpointsHelper newEp = new EndpointsHelper();
				newEp.setCode(endpoint.getCode());
				newEp.setId(endpoint.getId());
				newEp.setName(endpoint.getName());
				newEp.setState(endpoint.getState());
				listEndpoints.add(newEp);
				if ("ready".equals(endpoint.getState()))
					countReady++;
				if ("disconnected".equals(endpoint.getState()))
					countDiscn++;
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", listEndpoints);
		map.put("ready", countReady);
		map.put("discn", countDiscn);
		map.put("total", (countReady + countDiscn));
		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(map));
	}

	private void listTransaction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long fromTimeMilis = Long.parseLong(req.getParameter("fromTimeMilis"));
		Long toTimeMilis = Long.parseLong(req.getParameter("toTimeMilis"));

		Map<String, Object> map = new HashMap<String, Object>();

		Date fromDate = new Date(fromTimeMilis);
		Date toDate = new Date(toTimeMilis);
		List<AnalisaTransactionLogsDto> transactionLogs = transactionLogsManagerImpl.findTransactions(null, null, null,
				null, null, null, null, fromDate, toDate, null, 0, -1);

		Integer totalTransaction = transactionLogs.size();
		Integer totalSuccess = 0;
		Integer totalTimeOut = 0;
		Integer totalFailed = 0;
		for (AnalisaTransactionLogsDto transactionLog : transactionLogs) {
			if ("00".equals(transactionLog.getChannelRc()))
				totalSuccess++;
			else if ("ZZ".equals(transactionLog.getChannelRc()))
				totalTimeOut++;
			else
				totalFailed++;

		}

		map.put("success", totalSuccess);
		map.put("fail", totalFailed);
		map.put("timeout", totalTimeOut);
		map.put("total", totalTransaction);
		map.put("list", transactionLogs);

		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(map));
	}

	private void getLastTransaction(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
		IOException {
		List<AnalisaTransactionLogsDto> transactionLogs = transactionLogsManagerImpl.getLastTransactionsLog(new Date(),
				12);
		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(transactionLogs));
	}

	private void getSummaryTransaction(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
		IOException {
		Map<String, Long> totalTransactionForDashboard = this.transactionLogsManagerImpl
				.getTotalTransactionForDashboard(new Date());
		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(totalTransactionForDashboard));
	}
}
