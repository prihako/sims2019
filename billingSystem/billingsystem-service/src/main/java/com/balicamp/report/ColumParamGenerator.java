package com.balicamp.report;

import java.util.Collection;
import java.util.List;

import com.balicamp.model.report.ReportColumModel;
import com.balicamp.report.exception.RawColumnFormatException;
import com.balicamp.report.param.ColumnParam;
import com.balicamp.report.param.PojoColumnParam;
import com.balicamp.report.param.RawStringColumnParam;


public class ColumParamGenerator {
	
	/**
	 * fungsi untuk menggenarete list ColumParameter yang akan di gunakan di report yang sesuai dengan object param yang di kirim 
	 * sejak class ini dibuat baru support 2 yaitu RawString dan Pojo 
	 * 
	 * @param param 
	 * @return
	 * @throws RawColumnFormatException
	 */
	public List<ColumnParam> generateColumParam(Object param) throws RawColumnFormatException{
		ColumnParam columParam = null;
		
		if(null != param){
			if (param instanceof String) {
				columParam = new RawStringColumnParam((String)param);
				
			}else if (param instanceof Collection) {
				Object tmpObj= ((Collection)param).iterator().next();
				
				if (tmpObj instanceof ReportColumModel) {
					columParam =  new PojoColumnParam ((Collection<ReportColumModel>)param);					
				}
			}
		}
		
		return null== columParam? null:columParam.format();
	}

}
