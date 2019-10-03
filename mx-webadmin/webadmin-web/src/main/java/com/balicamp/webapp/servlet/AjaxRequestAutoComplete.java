/**
 * 
 */
package com.balicamp.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.balicamp.dao.mx.EndpointsDao;
import com.balicamp.dao.mx.TransactionsDao;
import com.balicamp.model.mx.Endpoints;
import com.balicamp.model.mx.Transactions;

/**
 * Ajax Request for Auto Complete Transaction Code, Description and Endpoint Code
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: AjaxRequestAutoComplete.java 505 2013-05-24 08:15:51Z rudi.sadria $
 */
public class AjaxRequestAutoComplete extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String TRANSACTION_CODE = "transactionCode";
	private static final String TRANSACTION_DESC = "transactionDesc";
	private static final String ENDPOINT_CODE = "endpointCode";

	private TransactionsDao transactionsDao;
	private EndpointsDao endpointsDao;

	private String[] transCode = null;
	private String[] transDesc = null;
	private String[] endpCode = null;

	public AjaxRequestAutoComplete() {
		super();
	}

	@Override
	public void init() throws ServletException {
		if (null == transactionsDao) {
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			transactionsDao = context.getBean(TransactionsDao.class);
			endpointsDao = context.getBean(EndpointsDao.class);
		}

		List<Transactions> trans = transactionsDao.findAll();
		List<Endpoints> endps = endpointsDao.findAll();
		List<String> codes = new ArrayList<String>();
		List<String> descs = new ArrayList<String>();
		List<String> endpc = new ArrayList<String>();
		for (Transactions t : trans) {
			codes.add(t.getCode());
			descs.add(t.getName());
		}

		for (Endpoints e : endps) {
			endpc.add(e.getCode());
		}

		transCode = new String[codes.size()];
		transDesc = new String[descs.size()];
		endpCode = new String[endpc.size()];

		System.arraycopy(codes.toArray(), 0, transCode, 0, codes.size());
		System.arraycopy(descs.toArray(), 0, transDesc, 0, descs.size());
		System.arraycopy(endpc.toArray(), 0, endpCode, 0, endpc.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		resp.setHeader("Cache-control", "no-cache, no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Expires", "-1");

		JSONArray arrayObj = new JSONArray();

		Map<String, String> maps = req.getParameterMap();
		for (Map.Entry<String, String> m : maps.entrySet()) {
			if (m.getKey().equals(TRANSACTION_CODE)) {
				String query = req.getParameter(TRANSACTION_CODE);
				query = query.toLowerCase();
				for (int i = 0; i < transCode.length; i++) {
					String code = transCode[i].toLowerCase();
					if (code.startsWith(query)) {
						arrayObj.add(transCode[i]);
					}
				}
				out.println(arrayObj.toString());
			}

			if (m.getKey().equals(TRANSACTION_DESC)) {
				String query = req.getParameter(TRANSACTION_DESC);
				query = query.toLowerCase();
				for (int i = 0; i < transDesc.length; i++) {
					String code = transDesc[i].toLowerCase();
					if (code.startsWith(query)) {
						arrayObj.add(transDesc[i]);
					}
				}
				out.println(arrayObj.toString());
			}

			if (m.getKey().equals(ENDPOINT_CODE)) {
				String query = req.getParameter(ENDPOINT_CODE);
				query = query.toLowerCase();
				for (int i = 0; i < endpCode.length; i++) {
					String code = endpCode[i].toLowerCase();
					if (code.startsWith(query)) {
						arrayObj.add(endpCode[i]);
					}
				}
				out.println(arrayObj.toString());
			}
		}
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
}
