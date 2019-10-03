/**
 * 
 */
package com.balicamp.webapp.tapestry.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;
import com.balicamp.model.admin.BaseAdminModel;

/**
 * This Class wrapper {@link CustomField} component and {@link BaseGrid} component 
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: BaseCustomComponent.java 381 2013-03-25 03:55:12Z wayan.agustina $
 */
public abstract class BaseCustomComponent<T extends BaseAdminModel> extends BaseComponent {

	private static final String CRITERION_EQ = "eq";
	private static final String CRITERION_NE = "ne";
	private static final String CRITERION_LIKE = "like";
	private static final String CRITERION_ILIKE = "ilike";

	private static final String DATA_TYPE_STRING = "STRING";
	private static final String DATA_TYPE_INT = "INTEGER";
	private static final String DATA_TYPE_BIGDECIMAL = "BIGDECIMAL";
	private static final String DATA_TYPE_BOOLEAN = "BOOLEAN";

	// -------------- variabel global keperluan build filter -------------//
	private SearchCriteria searchCriteria;

	private Map<String, String[]> mapFilterTmp;

	// private Set<String[]> filters;

	private List<String[]> filters;

	private Map<String, String> mapFilter;

	public abstract String getTestHidden();

	public abstract String getStrFilter();

	public abstract String getObjectFilter();

	public abstract void setObjectFilter(String str);

	public abstract SearchCriteria getFilterCriteria();

	public abstract void setFilterCriteria(SearchCriteria searchCriteria);

	public abstract String getInitFilter();

	public abstract void setInitFilter(String strFilter);

	// -------------- untuk keperluan baseGrid--------------------//
	public abstract List<T> getGridData();

	public abstract void setGridData(List<T> data);

	public abstract int getGridRowCount();

	public abstract void setGridRowCount(int rowCount);

	public abstract String getColumns();

	public abstract void setColumns(String columns);

	public abstract Object getTableRow();

	public abstract void setTableRow(Object row);

	public abstract String getAdditionalLinkType();

	public abstract void setAdditionalLinkType(String type);

	public abstract ActionListenerWrapper getAdditionalLinkListener();

	public abstract void setAdditionalLinkListener(ActionListenerWrapper gridEditListener);

	public abstract String getAdditionalLinkLabel();

	public abstract void setAdditionalLinkLabel(String label);

	public abstract String getSelectorType();

	public abstract void setSelectorType(String type);

	public abstract ActionListenerWrapper getGridActionButtonListener();

	public abstract void setGridActionButtonListener(ActionListenerWrapper gridEditListener);

	public abstract void setUniqueSet(Set<T> selectedIds);

	public abstract Set<T> getUniqueSet();

	public abstract void setGridActionButtonLabel(String label);

	public abstract String getGridActionButtonLabel();

	public abstract boolean getShowDatePicker();

	public abstract void setShowDatePicker(boolean show);

	public abstract Date getStartDateFilterValue();

	public abstract void setStartDateFilterValue(Date date);

	public abstract Date getEndDateFilterValue();

	public abstract void setEndDateFilterValue(Date date);

	private int fistPage = 0;
	private int maxResult = 0;

	public int getFistPage() {
		return fistPage;
	}

	public void setFistPage(int fistPage) {
		this.fistPage = fistPage;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {

		if (filters == null) {
			filters = new ArrayList<String[]>();
		}

		filters = initFilterTmp(getStrFilter());
		drawComponent(filters, writer, cycle);

		// ini indikasi jika pindah page criterion dari SearchCriteria tidak di
		// null kan
		// if (!cycle.isRewinding() && searchCriteria == null) {
		buildSearchCriteria(getInitFilter());
		// }

		if (getUniqueSet() == null)
			setUniqueSet(new HashSet<T>());

		super.renderComponent(writer, cycle);
	}

	public abstract void setCacheTableRowCount(int rowCount);

	public abstract int getCacheTableRowCount();

	public abstract void setCacheTableData(List<?> data);

	public abstract List<?> getCacheTableData();

	public IBasicTableModel getTableModel() {
		return new IBasicTableModel() {

			@Override
			public int getRowCount() {
				if (getCacheTableRowCount() <= 0) {
					setCacheTableRowCount(getGridRowCount());
				}
				return getCacheTableRowCount();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<T> getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn,
					boolean bSortOrder) {
				if (getCacheTableData() == null) {
					setFistPage(nFirst);
					setMaxResult(nPageSize);
					setCacheTableData(getGridData());
				}
				return ((List<T>) getCacheTableData()).iterator();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public void checkSelect() {
		if (getPage().getRequestCycle().isRewinding()) {
			T row = (T) getTableRow();
			if (row.getSelected())
				getUniqueSet().add(row);
		}
	}

	public void doAction() {
		getGridActionButtonListener().onAction(getUniqueSet());
	}

	private void drawComponent(List<String[]> setFilters, IMarkupWriter writer, IRequestCycle cycle) {
		// create table for wrap componen...
		writer.begin("table");
		writer.attribute("width", "99%");
		writer.attribute("class", "uniform");

		/*
		 * INI untuk menambahkan Tulisan Searching di atas
		 * writer.begin("tr");
		 * writer.begin("td");
		 * writer.begin("h4");
		 * writer.print("Search : ");
		 * writer.end("h4");
		 * writer.end("td");
		 * writer.end("tr");
		 */

		// wrap content here....
		for (String[] src : setFilters) {
			writer.begin("tr");
			writer.begin("td");
			writer.attribute("width", "200px");
			writer.begin("label");

			for (int i = 0; i < src.length; i++) {
				if (i == src.length - 1) {
					writer.attribute("class", "medium");
					writer.print(src[i].toString());
				}
			}
			writer.end("label");
			writer.end("td");

			// draw component
			writer.begin("td");
			writer.begin("input");
			writer.attribute("type", "text");
			writer.attribute("id", src[0].toString());
			writer.attribute("maxlenght", "30");
			writer.attribute("width", "208px");
			writer.attribute("cssclass", "TextBox");
			writer.attribute("translator", "translator:string,trim=true");
			writer.attribute("onChange", "getFilterValue(this.id, this.value)");
			writer.end("td");
			writer.end("tr");
		}

		// end wrap table...
		writer.end("table");
	}

	/**
	 * Listener onSearchBtn ketika LinkSubmit di click<br/>
	 * <ol>
	 * <li>Set nilai variabel global searchCriteria = null > menangani refresh/click berulang-ulang dengan filter yang sama</li>
	 * <li>Set nilai objectFilter (Hidden untuk menampung id:value dari TextField) = null</li>
	 * <li>
	 * Split dalam looping berdasarkan delimiter ":" untuk membentuk SearchCriteria awal yang isinya :
	 * <ol>String[0] : EntityName<br/>
	 * String[1] : PropertyName, Jika terdapat tanda $ makan sebagai embedeble ID, jika tidak maka sebagai subcriteria<br/>
	 * String[2] : Restrictions -> eq, like, ilike<br/>
	 * String[3] : DATA_TYPE -> STRING, INTEGER, ... <br/>
	 * String[4] : value<br/>
	 * </ol></li>
	 * <li>Bentuk SearchCriteria berdasarkan entityName</li>
	 * <li>Jika terdapat tanda $ makan addCriterion</li>
	 * <li>Jika tidak terdapat tanda $ makan addSubSearchCriteria</li>
	 * </ol>
	 * @param initFilter
	 */
	private void buildSearchCriteria(String initFilter) {
		searchCriteria = null; // 1.
		setObjectFilter(null); // 2.
		String directCriteria = "";

		String[] fltr = initFilter.split("[:]");// 3.
		searchCriteria = SearchCriteria.createSearchCriteria(fltr[0].toString()); // 4.

		if (fltr.length > 1) {

			if (fltr[1].contains("$")) { // 5.
				directCriteria = fltr[1].replaceFirst(".*\\$", "");
				addCriteriaBuilder(directCriteria, fltr[2], fltr[4], fltr[3]);
			} else { // 6.
				String[] str = fltr[1].split("[.]");
				String propertyName = "";

				for (int i = 1; i < str.length; i++) {

					propertyName = str[str.length - 1].toString();

					if (i > 0 && i < str.length - 1) {
						addSubCriteriaBuilder(fltr[2], str[i].toString(), propertyName, fltr[4], fltr[3]);
					}
				}
			}
		}

		setFilterCriteria(searchCriteria);
	}

	/**
	 * Method untuk inisialisasi filter awal dari parameter strFilter yang dipasang pada *.html parent : <br/>
	 * misal nilai parameter : id:transaction.$id.identifier:eq:Identifier|kode:transaction.transactions.code:like:Kode
	 * <ol>
	 * <li>Split terlebih dahulu berdasarkan delimiter "|"</li>
	 * <li>Split berdasarkan delimiter ":" untuk membentuk Set<String[]> yang isinya id, Criteria (propertyName, subcriteria jika ada), 
	 * 	Restrictions, DATA_TYPE, Label</li>
	 * <li>Simpan ke map temporary dengan format > key : id, value : String[]{Criteria, Restrictions, DATA_TYPE, Label}</li>
	 * </ol>
	 * @param strFilter
	 * @return Set<String[]>
	 */
	private List<String[]> initFilterTmp(String strFilter) {
		List<String[]> tmp = new ArrayList<String[]>();

		if (mapFilterTmp == null) {
			mapFilterTmp = new HashMap<String, String[]>();
		} else {
			mapFilterTmp.clear();
		}

		if (mapFilter == null) {
			mapFilter = new HashMap<String, String>();
		} else {
			mapFilter.clear();
		}

		String[] str1 = strFilter.split("[|]"); // 1.
		for (String src : str1) {
			String[] str2 = src.split("[:]"); // 2.
			tmp.add(str2);
			mapFilterTmp.put(str2[0], new String[] { str2[1], str2[2], str2[str2.length - 2], str2[str2.length - 1] }); // 3.
		}
		return tmp;
	}

	/**
	 * Listener onSearchBtn ketika LinkSubmit di click<br/>
	 * <ol>
	 * <li>Split input id:getValue dengan delimiter "," misal : id:value1,id:value2,...</li>
	 * <li>Split dalam looping berdasarkan delimiter ":" untuk membentuk map</li>
	 * <li>Bentuk Map dengan format > key : id (split[0]), value : value (split[1])</li>
	 * </ol>
	 * @return default null
	 */
	public IPage onSearchBtn() {
		if (mapFilter == null) {
			mapFilter = new HashMap<String, String>();
		} else {
			mapFilter.clear();
		}

		try {

			String[] str = getObjectFilter().replaceFirst("X,", "").split("[,]"); // 1.

			for (String st : str) {
				String[] loopStr = st.split("[:]"); // 2.
				mapFilter.put(loopStr[0], loopStr[1]); // 3.
			}
		} catch (Exception e) {
			buildSearchCriteria(getInitFilter());
			return null;
		}

		buildSearchCriteria(getInitFilter());
		compareWithCombination(mapFilter);
		setObjectFilter(null);

		return null;
	}

	/**
	 * Compare map dari hasil input pada TestField dengan map temporary dengan langkah berikut: 
	 * <ol>
	 * <li>Cek apakah value dari setiap inputan pada TextField kosong/tidak berdasarkan getValue() dari map input</li>
	 * <li>Bandingkan key dari map input dengan map temporary untuk proses selanjutnya</li>
	 * <li>Cek apakah ada karakter "$" nilai dari getValue() map temporary index ke [0] : kombinasi entityName.propertyName.value</li>
	 * <li>Jika terdapat karakter tanda "$" > ambil bagian belakang tanda untuk addCriterion()</li>
	 * <li>Simpan dataType dari map temporary value index ke [2]</li>
	 * <li>Tambahkan addCriterion pada searchCriteria dari hasil replace karakter "$"</li>
	 * <li>Masuk dalam pengecekn untuk addSubSearchCriteria</li>
	 * <li>Cek karakter "#" : menandakan subCriteria tersebut adalah @Embeddable, kemudian bentuk subCriteria</li>
	 * <li>Jika tidak ada karakter "#", bentuk subCriteria dari split getFilterTmp.getValue()[0] berdasarkan "."</li>
	 * <li>Konstruksi ParamMapStr pada SearchCriteria dengan nilai {key : Label, value : valueTxtField}</li>
	 * </ol>
	 * @param mapFilter
	 */
	private void compareWithCombination(Map<String, String> mapFilter) {
		for (Map.Entry<String, String> getFilter : mapFilter.entrySet()) {
			if (getFilter.getValue() != null) { // 1.
				for (Entry<String, String[]> getFilterTmp : mapFilterTmp.entrySet()) {
					if (getFilter.getKey().equals(getFilterTmp.getKey())) { // 2.

						String directCriteria = "";
						String dataType = "";
						if (getFilterTmp.getValue()[0].contains("$")) { // 3.
							directCriteria = getFilterTmp.getValue()[0].replaceFirst(".*\\$", ""); // 4.
						}

						dataType = getFilterTmp.getValue()[2]; // 5.

						if (directCriteria != "" && searchCriteria != null) { // 6.
							addCriteriaBuilder(directCriteria, getFilterTmp.getValue()[1], getFilter.getValue(),
									dataType);
						} else { // 7.
							String propertyName = "";
							String entityName = "";

							if (getFilterTmp.getValue()[0].contains("#")) { // 8.
								propertyName = getFilterTmp.getValue()[0].replaceFirst(".*\\#", "");
								String[] e = getFilterTmp.getValue()[0].replaceFirst("#.*", "").split("[.]");
								entityName = (e.length > 2) ? e[e.length - 2] : e[1];
							} else { // 9.
								String[] res = getFilterTmp.getValue()[0].split("[.]");
								propertyName = res[res.length - 1];
								entityName = (res.length > 2) ? res[res.length - 2] : res[1];
							}

							addSubCriteriaBuilder(getFilterTmp.getValue()[1], entityName, propertyName,
									getFilter.getValue(), dataType);
						}

						if (searchCriteria.getParamMapStr() != null) {
							searchCriteria.getParamMapStr().put(getFilterTmp.getValue()[3], getFilter.getValue()); // 10.
						}
					}
				}
			}
		}

		setFilterCriteria(searchCriteria);
	}

	private Object generateDataType(String value, String dataType) {
		Object dtTp = new Object();
		if (dataType.equals(DATA_TYPE_BIGDECIMAL)) {
			dtTp = new BigDecimal(value);
		} else if (dataType.equals(DATA_TYPE_INT)) {
			dtTp = new Integer(value);
		} else if (dataType.equals(DATA_TYPE_STRING)) {
			dtTp = value;
		} else if (dataType.equals(DATA_TYPE_BOOLEAN)) {
			dtTp = new Boolean(value);
		}
		return dtTp;
	}

	private void addSubCriteriaBuilder(String rest, String entityName, String propertyName, String value,
			String dataType) {

		Object val = generateDataType(value, dataType);

		if (rest.equals(CRITERION_EQ)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.eq(propertyName, val)));
		} else if (rest.equals(CRITERION_NE)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.ne(propertyName, (String) val)));
		} else if (rest.equals(CRITERION_LIKE)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.like(propertyName, (String) val, MatchMode.ANYWHERE)));
		} else if (rest.equals(CRITERION_ILIKE)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.ilike(propertyName, (String) val, MatchMode.ANYWHERE)));
		}
	}

	private void addCriteriaBuilder(String directCriteria, String rest, String value, String dataType) {
		Object val = generateDataType(value, dataType);

		if (rest.equals(CRITERION_EQ)) {
			searchCriteria.addCriterion(Restrictions.eq(directCriteria, val));
		} else if (rest.equals(CRITERION_NE)) {
			searchCriteria.addCriterion(Restrictions.ne(directCriteria, (String) val));
		} else if (rest.equals(CRITERION_LIKE)) {
			searchCriteria.addCriterion(Restrictions.like(directCriteria, (String) val, MatchMode.ANYWHERE));
		} else if (rest.equals(CRITERION_ILIKE)) {
			searchCriteria.addCriterion(Restrictions.ilike(directCriteria, (String) val, MatchMode.ANYWHERE));
		}
	}
}