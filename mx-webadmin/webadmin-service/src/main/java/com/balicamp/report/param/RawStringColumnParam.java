package com.balicamp.report.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.balicamp.report.exception.RawColumnFormatException;

public class RawStringColumnParam extends ColumnParam {

	private static final String RAW_FORMAT = "\n format rawColum : fieldName[:]fieldString[:]tipeData[:]length[:]margin[:]summary:count_row{conditiona_l;conditiona_2}[|]\n"
			+ "KETENTUAN : \n"
			+ "\t - fieldName [Mandatory]    --> key yang akan dijadikan acuan pencarian di RAW DATA  \n"
			+ "\t - fieldString [Mandatory]  --> nama colum yang akan di gunakan untuk di file hasil generate \n"
			+ "\t - tipeData [Mandatory]     -->  tipe data column {NUMERIC,TEXT,DATE} \n"
			+ "\t - length [Mandatory]       -->  max length masing-masing column  \n"
			+ "\t - margin [Mandatory]       -->  margin masing-masing column  {L:Left , R:Right, C:Center } \n"
			+ "\t - summary [Mandatory]      -->  field ini khusus untuk  tipe NUMERIC, dimana nantinya akan di buat summary dari keseluruhan data {SUM_Y, SUM_N}\n"
			+ "\t - row_count [Mandatory]    -->  {ROW_COUNT_Y, ROW_COUNT_N}field untuk menghitung jumlah row berdasarkan kondisi yang di tentukan di conditional dg format {value:label} tanpa conditional berarti dihitung row count semua\n"
			+ "CONTOH : \n"
			+ "\t nama:Nama:NUMERIC:10:R:SUM_N:ROW_COUNT_N|alamat:Alamat Rumah:TEXT:15:L:SUM_N:ROW_COUNT_N|transaksi:Transaksi:NUMERIC:15:L:SUM_Y:ROW_COUNT_Y{SUKSES;00,GAGAL;01}";

	private String rawColum;

	/**
	 * format rawColum : 
	 * 
	 * 	fieldName[:]fieldString[:]tipeData[:]summary[|]
	 *  KETENTUAN : 
	 *  	- tanpa tanda []
	 *      - pilihan tipe data {INTEGER,DOUBLE,TEXT,DATE}
	 *      - field summary , khusus untuk field numeric yang mendapat perlakuan khusus 
	 *         yaitu dengan menghitung jumlah total keseluruhan data selain tipe numeric optional 
	 *         pilihan nya : {Y,T} 
	 *  
	 *  contoh : 
	 *  nama:Nama:INTEGER:Y|alamat:Alamat Rumah:TEXT
	 * @throws RawColumnFormatException 
	  */
	public RawStringColumnParam(String rawColum) {
		this.rawColum = rawColum;
	}

	private RawStringColumnParam(String fieldName, String fieldString, String tipeData, int length, String margin,
			boolean isSum, boolean isRowCount, Map<String, String> condition) {
		setFieldName(fieldName);
		setFieldString(fieldString);
		setTipeData(tipeData);
		setSum(isSum);
		setLength(length);
		setMargin(margin);
		setRowCount(isRowCount);
		setConditional(condition);
	}

	/**
	 * fungsing untuk memvalidasi format parameter yang ada sesuai dengan yang di butuhkan oleh report formater
	 * 
	 * @param colum array of colum
	 * @return true : kalau format yang dikirim sudah benar, throw exception kalau ada kesalahan format 
	 * @throws RawColumnFormatException
	 */
	private boolean validateFormat(String[] colum) throws RawColumnFormatException {
		boolean validate = true;

		if (null == colum || colum.length != 7)
			throw new RawColumnFormatException(RAW_FORMAT);

		if (colum[2].startsWith(ColumnParam.RAW_TIPE_NUMERIC) || colum[2].equals(ColumnParam.RAW_TIPE_DATE)
				|| colum[2].equals(ColumnParam.RAW_TIPE_TEXT)) {
			// todo nothing
		} else {
			throw new RawColumnFormatException(RAW_FORMAT);
		}

		try {
			Integer.getInteger(colum[3]);
		} catch (Exception e) {
			throw new RawColumnFormatException(RAW_FORMAT);
		}

		if (colum[4].equals(ColumnParam.RAW_MARGIN_LEFT) || colum[4].equals(ColumnParam.RAW_MARGIN_CENTER)
				|| colum[4].equals(ColumnParam.RAW_MARGIN_RIGHT)) { /*
																	 * todo
																	 * nothing
																	 */} else {
			throw new RawColumnFormatException(RAW_FORMAT);
		}

		if (colum[5].equals(ColumnParam.RAW_SUMMARY_YES) || colum[5].equals(ColumnParam.RAW_SUMMARY_NO)) { /*
																											 * todo
																											 * nothing
																											 */} else {
			throw new RawColumnFormatException(RAW_FORMAT);
		}

		if (colum[6].equals(ColumnParam.RAW_COUNT_NO) || colum[6].indexOf(ColumnParam.RAW_COUNT_YES) > -1) { /*
																											 * todo
																											 * nothing
																											 */} else {
			throw new RawColumnFormatException(RAW_FORMAT);
		}

		return validate;

	}

	@Override
	public List<ColumnParam> format() throws RawColumnFormatException {
		List<ColumnParam> listColumObject = new ArrayList<ColumnParam>();
		String[] colums = rawColum.split("[|]");
		if (null == colums || colums.length <= 0)
			throw new RawColumnFormatException(RAW_FORMAT);

		for (int i = 0; i < colums.length; i++) {
			String[] colum = colums[i].split("[:]");

			if (!validateFormat(colum)) {
				return null;
			}
			boolean sum = (colum[5].trim().equalsIgnoreCase(ColumnParam.RAW_SUMMARY_YES)) ? true : false;
			boolean isRowCount = (colum[6].trim().indexOf(ColumnParam.RAW_COUNT_YES) > -1) ? true : false;
			Map<String, String> condition = null;
			if (isRowCount) {
				// ROW_COUNT_Y{SUKSES;00,GAGAL;01} --> MAP( SUKSES (KEY) , 00
				// (VALUE))
				String strCon = colum[6].substring(colum[6].indexOf("{") + 1, colum[6].indexOf("}"));
				String[] arrCon = strCon.split(",");
				for (int j = 0; j < arrCon.length; j++) {
					String tmp = arrCon[j];
					String[] strKeyValue = tmp.split(";");
					if (strKeyValue.length != 2) {
						throw new RawColumnFormatException(RAW_FORMAT);
					} else {
						if (null == condition) {
							condition = new HashMap<String, String>();
						}
						condition.put(strKeyValue[0], strKeyValue[1]);
					}

				}
			}

			RawStringColumnParam tmp = new RawStringColumnParam(colum[0], colum[1], colum[2],
					Integer.valueOf(colum[3]), colum[4], sum, isRowCount, condition);
			listColumObject.add(tmp);
		}
		return listColumObject;
	}

}
