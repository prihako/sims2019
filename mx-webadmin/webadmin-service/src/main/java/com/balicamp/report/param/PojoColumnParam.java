package com.balicamp.report.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balicamp.model.report.ReportColumModel;
import com.balicamp.report.exception.RawColumnFormatException;

/**
 * Class Plain Column parameter yang berasal dari Model dari database
 * 
 * @author Nyoman Budi Parwata
 *
 */
public class PojoColumnParam extends ColumnParam {
	
	private static final Log log = LogFactory.getLog(PojoColumnParam.class); 
	
	private List<ReportColumModel> listModel;	
	
	public PojoColumnParam(Collection<ReportColumModel> models){
		if(null != models){
			listModel = new ArrayList<ReportColumModel>();
			
			for (ReportColumModel reportColumModel : models) {
				listModel.add(reportColumModel);
			}
		}
	}
	
	private PojoColumnParam() {
	}
	
	@Override
	public List<ColumnParam> format() throws RawColumnFormatException {
		if(null == listModel){throw new RawColumnFormatException("List Model tidak boleh null");}
		
		List<ColumnParam> listParam = new ArrayList<ColumnParam>();
		for (Iterator iterator = listModel.iterator(); iterator.hasNext();) {
			ReportColumModel columnParam = (ReportColumModel) iterator.next();
			listParam.add(convertModel(columnParam));				
		}		
		return listParam;
	}


	private ColumnParam convertModel(ReportColumModel model) {
		PojoColumnParam param = new PojoColumnParam();
		
		param.setFieldName(model.getFieldName());
		param.setFieldString(model.getFieldString());
		param.setLength(model.getLength().intValue());
		param.setMargin(model.getMargin());
		param.setRowCount(model.isRowCount());
		param.setSum(model.isSum());
		param.setTipeData(model.getTipeData());		
		param.setKeyDataFormater(model.getKeyDataFormater());
		
		return param;
	}

}
