package com.balicamp.webapp.tapestry.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;

import test.Constants;

import com.balicamp.model.admin.BaseAdminModel;
import com.balicamp.model.parameter.SystemParameterId;
import com.balicamp.service.parameter.SystemParameterManager;

/**
 * Komponen grid yg sudah dilengkapi selector, view detail link.
 * 
 * @author Wayan Ari Agustina
 * @version $Id: $
 * @param <T> Entity type
 */
public abstract class ExtendedGrid<T extends BaseAdminModel> extends BaseComponent {

	private int firstPage = 0;
	private int maxResult = 0;

	/**
	 * @return Data yg akan dirender pada row grid.
	 */
	public abstract List<T> getGridData();

	public abstract void setGridData(List<T> data);

	/**
	 * @return Jumlah dari total data yg akan ditampilkan,
	 * diperlukan untuk paging.
	 */
	public abstract int getGridRowCount();

	public abstract void setGridRowCount(int rowCount);

	/**
	 * @return Pojo/record yg terpilih pada grid.
	 */
	public abstract Object getTableRow();

	public abstract void setTableRow(Object row);

	/**
	 * @return String yg sudah diformat untuk kerpeluan render header grid.
	 */
	public abstract String getColumns();

	public abstract void setColumns(String columns);

	public abstract void setAdditionalLinkHeaderLabel(String label);

	public abstract String getAdditionalLinkHeaderLabel();

	/**
	 * Bila diperlukan link khusus untuk view detail atau yg lainnya.
	 * bisa menggunakan method ini. method ini berhubungan dengan 
	 * paramater additionalLinkType.
	 * @param type anchor/button
	 */
	public abstract void setAdditionalLinkType(String type);

	/**
	 * @return Listener yg sudah diextends dari {@link ActionListenerWrapper}
	 * dan akan dipanggil jika additionalLinkType diatas diclick.
	 */
	public abstract ActionListenerWrapper getAdditionalLinkListener();

	public abstract void setAdditionalLinkListener(ActionListenerWrapper gridEditListener);

	/**
	 * @return String yg digunakan sebagai label untuk additional link.
	 */
	public abstract String getAdditionalLinkLabel();

	public abstract void setAdditionalLinkLabel(String label);

	public abstract void setStaticAdditionalLinkLabel(String label);

	public abstract String getStaticAdditionalLinkLabel();

	/**
	 * Mengambil value dari pojo dengan property name yg didapatkan dari
	 * getAddtionalLinkLabel().
	 * @return String label untuk additional link.
	 */
	public String getPropertyValueForAdditionalLinkLabel() {
		if (getStaticAdditionalLinkLabel() != null)
			return getStaticAdditionalLinkLabel();
		String label = null;
		String propertyName = getAdditionalLinkLabel();
		Object row = getTableRow();
		Class<?> clazz = row.getClass();
		Field field = null;
		try {
			field = clazz.getDeclaredField(propertyName);
			if (!field.isAccessible())
				field.setAccessible(true);
			label = (String) field.get(row);
		} catch (Exception e) {
			if (e instanceof IllegalAccessException) {
				try {
					field = clazz.getSuperclass().getDeclaredField(propertyName);
					return (String) field.get(row);
				} catch (Exception e2) {}
			}
		}
		return label;
	}

	/**
	 * @return Tipe selector yg akan digunakan untuk seleksi data
	 * pada grid. (checkbox/radiobutton).
	 */
	public abstract String getSelectorType();

	public abstract void setSelectorType(String type);

	/**
	 * @return Listener yg sudah diextends dari {@link ActionListenerWrapper}
	 * dan akan digunakan untuk keperluar action button grid. 
	 */
	public abstract ActionListenerWrapper getGridActionButtonListener();

	public abstract void setGridActionButtonListener(ActionListenerWrapper gridEditListener);

	// public abstract void setUniqueSet(Set<T> selectedIds);

	public abstract void setSortedUniqueSet(List<T> selectedIds);

	/**
	 * @return Data yg diseleksi pada grid.
	 */
	// public abstract Set<T> getUniqueSet();

	public abstract List<T> getSortedUniqueSet();

	/**
	 * @param label Label untuk button action.
	 */
	public abstract void setGridActionButtonLabel(String label);

	public abstract String getGridActionButtonLabel();

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	@Bean
	public abstract EvenOdd getEvenOdd();

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// if (getUniqueSet() == null) {
		// setUniqueSet(new HashSet<T>());
		// }

		if (getSortedUniqueSet() == null) {
			setSortedUniqueSet(new ArrayList<T>());
		}

		getEvenOdd().setEven(false);
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
				setFirstPage(nFirst);
				setMaxResult(nPageSize);
				// if (getCacheTableData() == null) {
				// setCacheTableData(getGridData());
				// return ((List<T>) getCacheTableData()).iterator();
				// }
				List<T> temp = new ArrayList<T>();
				for (int i = nFirst; i < nFirst + getPageSize(); i++) {
					if (i < getGridData().size())
						temp.add(getGridData().get(i));
				}

				return ((List<T>) temp).iterator();
				// return ((List<T>) getCacheTableData()).iterator();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public void checkSelect() {

		if (getPage().getRequestCycle().isRewinding()) {
			T row = (T) getTableRow();
			if (row.getSelected()) {
				// getUniqueSet().add(row);
				getSortedUniqueSet().add(row);
			}
		}

	}

	public void doAction() {
		getGridActionButtonListener().onAction(getSortedUniqueSet());
	}

	@InjectObject("spring:systemParameterManager")
	public abstract SystemParameterManager getSystemParameterManager();

	public int getPageSize() {
		return getSystemParameterManager().getIntValue(
				new SystemParameterId(Constants.SystemParameter.Page.GROUP,
						Constants.SystemParameter.Page.TABLE_PAGESIZE), 30);
	}
}
