package com.balicamp.webapp.tapestry.component;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.components.AbstractTableViewComponent;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.util.ComponentAddress;

public abstract class TablePages extends AbstractTableViewComponent {
	// Bindings
	public abstract int getPagesDisplayed();

	// Transient
	private int m_nDisplayPage;

	/**
	 * Returns the displayPage.
	 *
	 * @return int
	 */
	public int getDisplayPage() {
		return m_nDisplayPage;
	}

	/**
	 * Sets the displayPage.
	 *
	 * @param displayPage The displayPage to set
	 */
	public void setDisplayPage(int displayPage) {
		m_nDisplayPage = displayPage;
	}

	public int getCurrentPage() {
		return getTableModelSource().getTableModel().getPagingState().getCurrentPage() + 1;
	}

	public int getPageCount() {
		return getTableModelSource().getTableModel().getPageCount();
	}

	public boolean getCondBack() {
		return getCurrentPage() > 1;
	}

	public boolean getCondFwd() {
		return getCurrentPage() < getPageCount();
	}

	public boolean getCondCurrent() {
		return getDisplayPage() == getCurrentPage();
	}

	public int getStartPage() {
		int nCurrent = getCurrentPage();
		int nPagesDisplayed = getPagesDisplayed();

		int nRightMargin = nPagesDisplayed / 2;
		int nStop = nCurrent + nRightMargin;
		int nLastPage = getPageCount();

		int nLeftAddon = 0;
		if (nStop > nLastPage)
			nLeftAddon = nStop - nLastPage;

		int nLeftMargin = (nPagesDisplayed - 1) / 2 + nLeftAddon;
		int nStart = nCurrent - nLeftMargin;
		int nFirstPage = 1;
		if (nStart < nFirstPage)
			nStart = nFirstPage;
		return nStart;
	}

	public int getStopPage() {
		int nCurrent = getCurrentPage();
		int nPagesDisplayed = getPagesDisplayed();

		int nLeftMargin = (nPagesDisplayed - 1) / 2;
		int nStart = nCurrent - nLeftMargin;
		int nFirstPage = 1;

		int nRightAddon = 0;
		if (nStart < nFirstPage)
			nRightAddon = nFirstPage - nStart;

		int nRightMargin = nPagesDisplayed / 2 + nRightAddon;
		int nStop = nCurrent + nRightMargin;
		int nLastPage = getPageCount();
		if (nStop > nLastPage)
			nStop = nLastPage;
		return nStop;
	}

	public Integer[] getPageList() {
		// int nStart = getStartPage();
		// int nStop = getStopPage();
		//
		// Integer[] arrPages = new Integer[nStop - nStart + 1];
		// for (int i = nStart; i <= nStop; i++)
		// arrPages[nStop - i] = Integer.valueOf(i);
		//
		// return arrPages;

		int nStart = getStartPage();
		int nStop = getStopPage();

		Integer[] arrPages = new Integer[nStop - nStart + 1];
		for (int i = nStart; i <= nStop; i++)
			arrPages[i - nStart] = Integer.valueOf(i);

		return arrPages;

	}

	public Object[] getFirstPageContext() {
		ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
		return new Object[] { objAddress, new Integer(1) };
	}

	public Object[] getLastPageContext() {
		ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
		return new Object[] { objAddress, new Integer(getPageCount()) };
	}

	public Object[] getBackPageContext() {
		ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
		int newPage = new Integer(getCurrentPage() - 1);
		if (newPage <= 0)
			newPage = 0;
		return new Object[] { objAddress, newPage };
	}

	public Object[] getFwdPageContext() {
		ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
		int newPage = new Integer(getCurrentPage() + 1);
		if (newPage >= getPageCount())
			newPage = getPageCount();
		return new Object[] { objAddress, newPage };
	}

	public Object[] getDisplayPageContext() {
		ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
		return new Object[] { objAddress, Integer.valueOf(m_nDisplayPage) };
	}

	public void changePage(IRequestCycle objCycle) {
		Object[] arrParameters = objCycle.getListenerParameters();
		if (arrParameters.length != 2 && !(arrParameters[0] instanceof ComponentAddress)
				&& !(arrParameters[1] instanceof Integer)) {
			// error
			return;
		}

		ComponentAddress objAddress = (ComponentAddress) arrParameters[0];
		ITableModelSource objSource = (ITableModelSource) objAddress.findComponent(objCycle);
		objSource.getTableModel().getPagingState().setPageSize(30);
		setCurrentPage(objSource, ((Integer) arrParameters[1]).intValue());

		// ensure that the change is saved
		objSource.fireObservedStateChange();
	}

	public void setCurrentPage(ITableModelSource objSource, int nPage) {
		objSource.getTableModel().getPagingState().setCurrentPage(nPage - 1);
	}

}
