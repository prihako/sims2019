/**
 * 
 */
package com.balicamp.webapp.tapestry.component;

import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: FormSearch.java 135 2013-01-03 04:20:57Z bagus.sugitayasa $
 */
public abstract class FormSearch extends BaseComponent {

	public abstract void setIdentifier(String identifier);

	@Persist("client")
	public abstract String getIdentifier();

	public abstract void setKodeTransaksi(String kodeTransaksi);

	@Persist("client")
	public abstract String getKodeTransaksi();

	public abstract void setNamaTransaksi(String namaTransaksi);

	@Persist("client")
	public abstract String getNamaTransaksi();

	public abstract void setNamaChannel(String namaChannel);

	@Persist("client")
	public abstract String getNamaChannel();

	public abstract Map<String, Object> getValue();

	public abstract void setValue(Map<String, Object> mapVal);

	public abstract SearchCriteria getFilterCriteria();

	public abstract void setFilterCriteria(SearchCriteria searchCriteria);

	public abstract boolean isRealRender();

	public abstract void setRealRender(boolean realRender);

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		super.renderComponent(writer, cycle);
		/*
		 * if (!cycle.isRewinding()) {
		 * if (isRealRender()) {
		 * if (getValue() == null) {
		 * Map<String, Object> result = new HashMap<String, Object>();
		 * setValue(result);
		 * }
		 * setRealRender(true);
		 * }
		 * }
		 */
	}

	public IPage onSearch() {

		createFilterCriteria();
		return null;
	}

	private void createFilterCriteria() {
		SearchCriteria searchCriteria = SearchCriteria.createSearchCriteria("transactionFee");
		SearchCriteria subCriteriaTransaction = SearchCriteria.createSearchCriteria("transactions");
		SearchCriteria subCriteriaEnpoint = SearchCriteria.createSearchCriteria("endpoints");
		if (getIdentifier() != null) {
			getValue().put("identifier", getIdentifier());
			searchCriteria.addCriterion(Restrictions.eq("id.identifier", getIdentifier()));

		}
		if (getKodeTransaksi() != null) {
			getValue().put("kodeTransaksi", getKodeTransaksi());
			subCriteriaTransaction.addCriterion(Restrictions.eq("code", getKodeTransaksi()));
		}

		searchCriteria.addSubSearchCriteria(subCriteriaTransaction);
		searchCriteria.addSubSearchCriteria(subCriteriaEnpoint);

		setFilterCriteria(searchCriteria);
	}
}
