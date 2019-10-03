/**
 * 
 */
package com.balicamp.webapp.tapestry.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.hibernate.criterion.Restrictions;

import com.balicamp.dao.helper.SearchCriteria;

/**
 * Class Wrap {@link SearchCriteria} dalam 1 Componen.<br/>
 * Doc Pemakaiannya : <ol>
 * <li>Nilai parameter : id:namaEntity.$propertyName1.propertyName2:Restrictions(eq, like, ilike):DATA_TYPE:Label|</li>
 * <li>Tanda "$" menyatakan embedded entity sehingga langsung dibentuk sebagai addCriterion()</li>
 * </ol>
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: CustomField.java 135 2013-01-03 04:20:57Z bagus.sugitayasa $
 */
public abstract class CustomField extends BaseComponent {

	private static final String CRITERION_EQ = "eq";
	private static final String CRITERION_LIKE = "like";
	private static final String CRITERION_ILIKE = "ilike";

	private static final String DATA_TYPE_STRING = "STRING";
	private static final String DATA_TYPE_INT = "INTEGER";
	private static final String DATA_TYPE_BIGDECIMAL = "BIGDECIMAL";

	public abstract String getStrFilter();

	public abstract String getObjectFilter();

	public abstract SearchCriteria getFilterCriteria();

	public abstract void setFilterCriteria(SearchCriteria searchCriteria);

	private SearchCriteria searchCriteria;

	private Map<String, String[]> mapFilterTmp;

	private Set<String[]> filters;

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		super.renderComponent(writer, cycle);

		if (cycle.isRewinding()) {
			return;
		}

		if (filters == null) {
			filters = new HashSet<String[]>();
		} else {
			filters.clear();
		}

		filters = initFilterTmp(getStrFilter());

		drawComponent(filters, writer, cycle);
		renderInformalParameters(writer, cycle);
	}

	private void drawComponent(Set<String[]> setFilters, IMarkupWriter writer, IRequestCycle cycle) {
		// create table for wrap componen...
		writer.begin("table");
		writer.attribute("width", "99%");
		writer.attribute("class", "uniform");
		writer.begin("tr");
		writer.begin("td");
		writer.begin("h4");
		writer.print("Search : ");
		writer.end("h4");
		writer.end("td");
		writer.end("tr");

		// wrap content here....
		for (String[] src : setFilters) {
			writer.begin("tr");
			writer.begin("td");
			writer.attribute("class", "short");
			writer.begin("label");

			for (int i = 0; i < src.length; i++) {
				if (i == src.length - 1) {
					writer.attribute("class", "short");
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
			writer.attribute("value", null);
			writer.attribute("translator", "translator:string,trim=true");
			writer.end("td");
			writer.end("tr");
		}

		// end wrap table...
		writer.end("table");
	}

	/**
	 * Method untuk inisialisasi filter awal dari parameter strFilter yang dipasang pada *.html parent : <br/>
	 * misal nilai parameter : id:transaction.$id.identifier:eq:Identifier|kode:transaction.transactions.code:like:Kode
	 * <ol>
	 * <li>Split terlebih dahulu berdasarkan delimiter "|"</li>
	 * <li>Split berdasarkan delimiter ":" untuk membentuk Set<String[]> yang isinya id, Criteria (propertyName, subcriteria jika ada), 
	 * 	Restrictions, DATA_TYPE, Label</li>
	 * <li>Simpan ke map temporary dengan format > key : id, value : String[]{Criteria, Restrictions, DATA_TYPE}</li>
	 * </ol>
	 * @param strFilter
	 * @return Set<String[]>
	 */
	private Set<String[]> initFilterTmp(String strFilter) {
		Set<String[]> tmp = new HashSet<String[]>();

		if (mapFilterTmp == null) {
			mapFilterTmp = new HashMap<String, String[]>();
		} else {
			mapFilterTmp.clear();
		}

		String[] str1 = strFilter.split("[|]"); // 1.

		for (String src : str1) {
			String[] str2 = src.split("[:]"); // 2.
			tmp.add(str2);
			mapFilterTmp.put(str2[0], new String[] { str2[1], str2[2], str2[str2.length - 2] }); // 3.
		}
		return tmp;
	}

	/**
	 * Listener onSearchBtn ketika LinkSubmit di click<br/>
	 * <ol>
	 * <li>Set nilai variabel global searchCriteria = null > menangani refresh/click berulang-ulang dengan filter yang sama</li>
	 * <li>Split input id:getValue dengan delimiter "," misal : id:value1,id:value2,...</li>
	 * <li>Split dalam looping berdasarkan delimiter ":" untuk membentuk map</li>
	 * <li>Bentuk Map dengan format > key : id (split[0]), value : value (split[1])</li>
	 * </ol>
	 * @return default null
	 */
	public IPage onSearchBtn() {
		searchCriteria = null; // 1.
		String[] str = getObjectFilter().split("[,]"); // 2.

		Map<String, String> mapFilter = new HashMap<String, String>();
		for (String st : str) {
			String[] loopStr = st.split("[:]"); // 3.
			mapFilter.put(loopStr[0], loopStr[1]); // 4.
		}

		compareWithCombination(mapFilter);

		return null;
	}

	/**
	 * Compare map dari hasil input pada TestField dengan map temporary dengan langkah berikut: 
	 * <ol>
	 * <li>Cek apakah value dari setiap inputan pada TextField kosong/tidak berdasarkan getValue() dari map input</li>
	 * <li>Bandingkan key dari map input dengan map temporary untuk proses selanjutnya</li>
	 * <li>Cek apakah ada karakter "$" nilai dari getValue() map temporary index ke [0] : kombinasi entityName.propertyName.value</li>
	 * <li>Jika terdapat karakter tanda "$" > ambil bagian belakang tanda untuk addCriterion()</li>
	 * <li>Split getValue() map temporary untuk mendapatkan entityNameExpression dengan delimiter ":"</li>
	 * <li>Simpan dataType dari map temporary value index ke [2]</li>
	 * <li>Bentuk {@link SearchCriteria} dengan nilai index ke [0] hasil split langkah sebelumnya</li>
	 * <li>Tambahkan addCriterion pada searchCriteria jika ada</li>
	 * <li>Tambahkan addSubCriteria pada searchCriteria jika ada</li>
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

						String[] res = getFilterTmp.getValue()[0].split("[.]"); // 5.

						dataType = getFilterTmp.getValue()[2]; // 6.

						if (searchCriteria == null) {
							searchCriteria = SearchCriteria.createSearchCriteria(res[0].toString()); // 7.
						}

						if (directCriteria != "" && searchCriteria != null) {
							addCriteriaBuilder(directCriteria, getFilterTmp.getValue()[1], getFilter.getValue(),
									dataType); // 8.
						} else {
							String propertyName = "";

							for (int i = 1; i < res.length; i++) {

								propertyName = res[res.length - 1].toString();

								if (i > 0 && i < res.length - 1) { // 9.
									addSubCriteriaBuilder(getFilterTmp.getValue()[1], res[i].toString(), propertyName,
											getFilter.getValue(), dataType);
								}
							}
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
		}
		return dtTp;
	}

	private void addSubCriteriaBuilder(String rest, String entityName, String propertyName, String value,
			String dataType) {

		Object val = generateDataType(value, dataType);

		if (rest.equals(CRITERION_EQ)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.eq(propertyName, val)));
		} else if (rest.equals(CRITERION_LIKE)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.like(propertyName, val)));
		} else if (rest.equals(CRITERION_ILIKE)) {
			searchCriteria.addSubSearchCriteria(SearchCriteria.createSearchCriteria(entityName).addCriterion(
					Restrictions.ilike(propertyName, val)));
		}
	}

	private void addCriteriaBuilder(String directCriteria, String rest, String value, String dataType) {
		Object val = generateDataType(value, dataType);

		if (rest.equals(CRITERION_EQ)) {
			searchCriteria.addCriterion(Restrictions.eq(directCriteria, val));
		} else if (rest.equals(CRITERION_LIKE)) {
			searchCriteria.addCriterion(Restrictions.like(directCriteria, val));
		} else if (rest.equals(CRITERION_ILIKE)) {
			searchCriteria.addCriterion(Restrictions.ilike(directCriteria, val));
		}
	}

}
