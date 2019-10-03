package com.balicamp.service.report.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class GenericJRDataSource<T> implements JRDataSource {
	private static final Log log = LogFactory.getLog(GenericJRDataSource.class);// NOPMD

	protected List<T> reporDataList;
	protected int currentRow;

	public GenericJRDataSource(List<T> reporDataList) {
		this.reporDataList = reporDataList;
		currentRow = -1;
	}

	public GenericJRDataSource(Collection<T> reporDataCollection, String dummy) {
		this.reporDataList = new ArrayList<T>();
		for (T t : reporDataCollection) {
			reporDataList.add(t);
		}
		currentRow = -1;
	}

	public void resetPointer() {
		currentRow = -1;
	}

	public Object getFieldValue(JRField field) throws JRException {
		try {
			return PropertyUtils.getSimpleProperty(reporDataList.get(currentRow), field.getName());
		} catch (Exception e) {
			log.error("fail get property", e);
			throw new JRException("fail get property", e);
		}
	}

	public boolean next() throws JRException {
		if (currentRow >= reporDataList.size() - 1)
			return false;
		currentRow++;
		return true;
	}

}
