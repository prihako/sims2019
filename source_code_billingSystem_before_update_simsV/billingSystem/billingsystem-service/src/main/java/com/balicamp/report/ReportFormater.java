package com.balicamp.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.balicamp.report.exception.RawDataFormatException;
import com.balicamp.report.param.ColumnParam;
import com.balicamp.util.DateConverter;
import com.balicamp.util.MoneyFormat;

public class ReportFormater {
		
	private static final String DATA_SPLITER = "[\r\n]";
	private static final String DOUBLR_COLOUM_SPLITER = "-";
	
	private static final String RESULT_DELIMITER = ",";
	private static final String RESULT_LINE_SPLITER = "\r";


	private static final String  SUMMARY_PREFIX = "Jumlah Total ";
	
	private static final String  ROWCOUNT_PREFIX = "Total ";
	
	private static final int NUMBER_PADDING_LENGTH = 5;
	
	private Map<String, Integer> dataMapFormater;
	
	public void setDataMapFormater(Map<String, Integer> dataMapFormater) {
		this.dataMapFormater = dataMapFormater;
	}
	
	
	/**
	 * fungsi untuk mengumpulkan semua informasi dan menyusun nya menjadi ReportPojo  yang nantinya dilempar ke template untuk ditampilkan
	 * @param datas
	 * @param colums
	 * @return
	 * @throws RawDataFormatException
	 */
	public ReportContent formatSparatedValue(List<String> datas, List<ColumnParam> colums,boolean isFixedLength)throws RawDataFormatException{
		
		ReportContent pojo = new ReportContent();
		
		int noLength = (datas == null ? 0 : (datas.isEmpty()?0:datas.size()));
		
		int lengthNumberCOlum = String.valueOf(noLength).length()+(isFixedLength?NUMBER_PADDING_LENGTH:0);
		StringBuffer columnName =  cereateHeaderTable(colums,lengthNumberCOlum,isFixedLength);
		pojo.setColumName(columnName);
		
		StringBuffer bfr = new StringBuffer();
		Map<String, Double>  summaryMap = new HashMap<String, Double>();
		Map<String, Integer>  rowCountMap = new HashMap<String, Integer>();
	
		int rowCounter = 1;
		for (String data : datas) {			
	
			StringBuffer tmpBuffer = new StringBuffer();
			
			/* data di column RAW setelah di split dengan SPLITER */
			String[] arrData = data.split(DATA_SPLITER);			
			for (ColumnParam colum : colums) {
				int colCounter = 1;
				boolean doubleCol = colum.getFieldName().contains(DOUBLR_COLOUM_SPLITER);

				String value = null;
				if(doubleCol){
					String[] cols = colum.getFieldName().split(DOUBLR_COLOUM_SPLITER);
					for (int i = 0; i < cols.length; i++) {
						for (int j = 0; j < arrData.length; j++) {	
							String xxx = arrData[j];
							String yyy = cols[i].trim();
							if(arrData[j].contains(cols[i].trim())){
								String tmpVal = arrData[j].substring((arrData[j].indexOf("=")+1));
								value = (null == value? tmpVal  : value+" "+DOUBLR_COLOUM_SPLITER+" "+tmpVal);					
								break;
							}
						}
					}
				}else{
					for (int i = 0; i < arrData.length; i++) {						
						if(arrData[i].contains(colum.getFieldName())){
							value = arrData[i].substring((arrData[i].indexOf("=")+1));					
							break;
						}
					}
				}
				
				/* ################################################ */
				
				if(null != value){
					String strSumValue = value;
					
					//cek nilainya bila kosong key=
					if(null == value || value.trim().length()== 0){
						value = (colum.getTipeData().equals(ColumnParam.RAW_TIPE_NUMERIC)?"0":" - ");
						strSumValue = value;
					}else{
						/* kondisi data harus di format sesuai konfigurasi di tabel m_mx_colum_param field fnq_formater 
						 * setting map nya ada di definisi bean spring di file xml, terpaksa di hardcode karena parameter class formater yang terpisah-pisah dan menerima parameter yang beda-beda
						 * bila terjadi exception maka data yang ditampilkan tidak diformat
						*/							
						try{
							if(null != colum.getKeyDataFormater()){
								value = formatData(colum.getKeyDataFormater(),value);
							}
						}catch(Exception e){ 
							//todo nothing								
						}
					}
					
					//format fixed length
					if(isFixedLength){
						value = formatFixedLength(value,colum.getLength(),colum.getMargin());
					}
					
					tmpBuffer.append(value);						
						
					/* mensumary kan data yang harus disummarikan berdasarkan flag summary di column*/
					if(!doubleCol && colum.isSum()){
						countSummary(colum,strSumValue,summaryMap);
					}
					
					/*menghitung row count untuk masing-masing conditional yang ada di konfigurasi */
					if(colum.isRowCount()){
						countRowCount(colum,strSumValue,rowCountMap);
					}
					
					if(colCounter < colums.size()){
						tmpBuffer.append(RESULT_DELIMITER);
					}
					colCounter++;
				}else {
					//kondisi bila column yang dicari tidak ada di data
					String tmp = (colum.getTipeData().equals(ColumnParam.RAW_TIPE_NUMERIC)?"0":"N/A");
					if(isFixedLength){
						tmp = formatFixedLength(tmp, colum.getLength(), ColumnParam.RAW_MARGIN_CENTER);
					}
					tmpBuffer.append(tmp);
					tmpBuffer.append(RESULT_DELIMITER);
				}
			}
			//set ke buffer asli
			if(!tmpBuffer.toString().trim().equals("")){
				String tmp = ","+(isFixedLength?formatFixedLength(String.valueOf(rowCounter),lengthNumberCOlum , ColumnParam.RAW_MARGIN_CENTER):String.valueOf(rowCounter))+","+tmpBuffer.toString();
				bfr.append(tmp);
				rowCounter++;
			}
			bfr.append(RESULT_LINE_SPLITER);		
		}
		
		pojo.setSparatedDetail(bfr);
		pojo.setJumlahData(rowCounter-1);
		
		//convert summaryMap menjadi summary list<string> dimana menggabungkan key dan value dan memformat angkanya menjadi 1.000
		Set<String> keys = summaryMap.keySet();
		List<String> summary = new ArrayList<String>();		
		for (String key : keys) {
			String tmp = key+" : "+MoneyFormat.getFormattedMoney(summaryMap.get(key));
			summary.add(tmp);
		}		
		pojo.setSummary(summary);
		
		
		Set<String> rowKeys = rowCountMap.keySet();
		List<String> strRowCount=  new ArrayList<String>();
		for (String key : rowKeys) {
			String tmp = key +" : "+ MoneyFormat.getFormattedMoney(rowCountMap.get(key));
			strRowCount.add(tmp);
		}
		
		pojo.setRowCount(strRowCount);
		
		//columLine sejumlah panjang line dihitung dari total max colum.length
		String line = "";
		for (int i = 0; i <= (pojo.getColumName().toString().length()+1); i++) {
			line = line+"-";
		}		
		pojo.setLine(line);
		
		return pojo;
		
	}

	private String formatData(String dataFormater, String value) {
		
		try{
			Integer formater = dataMapFormater.get(dataFormater);
			if(null != formater){
				switch (formater ){				
					
					case	1: { //key MONEY_FORMAT result 1000000 --> 1.000.000
						value = MoneyFormat.getFormattedMoney(value);
						break;
					}case 2 : { //key DATE_FORMAT -- dd/MM/YYYY
						Long time = new Long(value);
						Date date = new Date();
						date.setTime(time.longValue());
						
						value = DateConverter.dateToString(date, "dd/MM/yyyy");
						break;
					}case 3 :{ // DATE_TIME_FORMAT dd/MM/YYYY hh:mm 
						Long time = new Long(value);
						Date date = new Date();
						date.setTime(time.longValue());
						
						value = DateConverter.dateToString(date, "dd/MM/yyyy hh:mm");
						break;
					}
				}
			}else {
				throw new RuntimeException("format data "+dataFormater+" belum di support silahkan hubungi programer terdekat ");
			}
			
		}catch (Exception e) {
			// todo nothing;
		}
		
		
		return value;
	}


	/**
	 * fungsi untuk menghitung row count sesuai dengan condition yang sudah di tentukan di konfigurasi 
	 * @param colum
	 * @param value
	 * @param rowCountMap
	 */
	private void countRowCount(ColumnParam colum, String value,
			Map<String, Integer> rowCountMap) {

			Set<String> keySet = colum.getConditional().keySet();
			for (String key : keySet) {
				if(colum.getConditional().get(key).equals(value.trim())){
					String resultKey = ROWCOUNT_PREFIX+" "+key;
					Integer result = rowCountMap.get(resultKey);
					rowCountMap.put(resultKey, (null == result?1:result + 1));
				}
				
			}
			
	}

	/**
	 * fungsi untuk menghitung summay yang akan di tamplikan di bawah tabel data detail sesuai konfigurasi
	 * 
	 * @param colum
	 * @param value
	 * @param summaryMap
	 * @throws RawDataFormatException
	 */
	private void countSummary(ColumnParam colum, String value, Map<String, Double> summaryMap) throws RawDataFormatException  {
		if(colum.isSum()){
			String key = SUMMARY_PREFIX+colum.getFieldString();
			Double dblValue = Double.valueOf(0);
			
			try{
				dblValue = (null == value || value.trim().length() == 0? Double.valueOf(0): Double.valueOf(value));
			}catch (NumberFormatException e) {
				throw new RawDataFormatException(colum.getFieldName()+" --> "+value);
			}
			
			Double tmp = summaryMap.get(key);								
			dblValue = (null == tmp? dblValue : new Double(dblValue.doubleValue()+tmp.doubleValue()));
			
			summaryMap.put(key, dblValue);			
		}
		
	}


	/**
	 * fungsi untuk melakukan memformat atau menambahkan spasi sesuai dengan jumlah panjang kolom dan margin
	 * @param value
	 * @param length
	 * @param margin
	 * @return
	 */
	private String formatFixedLength(String value, int length, String margin) {
	
		if(value.length() > length){
			return value;
		}
		
		StringBuffer fixedValue = new StringBuffer();
		int selisih = length - value.length();
		
		if(margin.equals(ColumnParam.RAW_MARGIN_RIGHT)){
			for (int i = 0; i < selisih; i++) {
				fixedValue.append(" ");
			}
			fixedValue.append(value);
		}else if(margin.equals(ColumnParam.RAW_MARGIN_LEFT)){
			fixedValue.append(value);
			for (int i = 0; i < selisih; i++) {
				fixedValue.append(" ");
			}			
		}else if(margin.equals(ColumnParam.RAW_MARGIN_CENTER)){
			
			int newSelisih = selisih / 2;			
			
			fixedValue.append(formatFixedLength(value, (value.length() + newSelisih), ColumnParam.RAW_MARGIN_RIGHT));
			
			newSelisih = length - fixedValue.toString().length();
			for (int i = 0; i < newSelisih; i++) {
				fixedValue.append(" ");
			}			

		}		
		return (fixedValue.toString().length()<=0?value:fixedValue.toString());
	}

	/**
	 * membuat row paling atas di file separated sebagai header 
	 * @param colums list column
	 * @return buffer with header
	 */
	private StringBuffer cereateHeaderTable(List<ColumnParam> colums, int lengthNumberCOlum,boolean isFixLength) {
		StringBuffer bfr = new StringBuffer();
		bfr.append(","+(isFixLength?formatFixedLength("NO", lengthNumberCOlum, ColumnParam.RAW_MARGIN_CENTER):lengthNumberCOlum)+",");
		loopHeader(bfr, colums, false);		
		return bfr;
	}
	/**
	 * looping semua COlum param untuk membentuk 
	 * @param bfr
	 * @param colums
	 * @param isKey --> apakah columString atau columnName nya yang di looping 
	 * @return
	 */
	private StringBuffer loopHeader (StringBuffer bfr, List<ColumnParam> colums, boolean isKey){
		int counter = 1;
		for (ColumnParam columnParam : colums) {
			String strColumn = formatFixedLength(isKey? columnParam.getFieldName():columnParam.getFieldString(),columnParam.getLength(),ColumnParam.RAW_MARGIN_CENTER);
			bfr.append(strColumn) ;
			
			if(counter < colums.size()){				
				bfr.append(RESULT_DELIMITER);
			}
			counter++;
		}
//		bfr.append(RESULT_LINE_SPLITER);
		return bfr;
	}

	public ReportHeader createHeader(ReportParamHeader headerParam) {
		ReportHeader header = new ReportHeader();
		
		// Nama Bank
		String bank = formatFixedLength(headerParam.getBank(), headerParam.getReportLength(), ColumnParam.RAW_MARGIN_CENTER);
		header.setBank(bank);
		
		// judul
		String judul = formatFixedLength(headerParam.getJudul(), headerParam.getReportLength(), ColumnParam.RAW_MARGIN_CENTER);
		header.setJudul(judul);
		
		// line
		header.setLine(headerParam.getLine());
		
		// tgl cetak
		String tglCetak = DateConverter.dateToString(headerParam.getTglCetak(), "dd MMM yyyy");		
		header.setTglCetak(formatFixedLength(tglCetak,headerParam.getReportLength(), ColumnParam.RAW_MARGIN_LEFT));
		
		//userId
		header.setUserId(formatFixedLength(headerParam.getUserId(),headerParam.getReportLength(), ColumnParam.RAW_MARGIN_LEFT));
		
		//periode
		StringBuffer bfr = new StringBuffer();
		if(null != headerParam.getTglAwal()){
			String strTglAwal = DateConverter.dateToString(headerParam.getTglAwal(), "dd/MM/yyyy");
			bfr.append(strTglAwal);			
		} 
		if(null != headerParam.getTglAkhir()){
			bfr.append(" - ");
			String strTglAkhir = DateConverter.dateToString(headerParam.getTglAkhir(), "dd/MM/yyyy");
			bfr.append(strTglAkhir);			
		}		
		header.setPeriode(formatFixedLength(bfr.toString(), headerParam.getReportLength(), ColumnParam.RAW_MARGIN_CENTER));
		
		
		return header;
	}
}
