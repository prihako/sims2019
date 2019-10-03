package com.balicamp.util.mx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nucleus8583.core.Iso8583Message;
import org.nucleus8583.core.Iso8583MessageSerializer;

/**
 * @author GloryElishua
 */
public class Iso8583Utility {

	public static String getResponseCode(String raw, String configPath, String endpointCode) {
        Iso8583Message m = unpack(raw, configPath, endpointCode);
		return (m != null) ? m.getString(39) : "";
	}
	
	public static String printToHTMLFormatted(String raw, String configPath, String endpointCode) {
        Iso8583Message m = unpack(raw, configPath, endpointCode);
		
		StringBuffer strBuf = new StringBuffer();
		if (m != null) {
			if(endpointCode.equals("chws")) {
				raw = raw.replace("\n", "<br/>");
				strBuf.append(raw);
			}else {
				try {
					Map<Integer, Object> map = new HashMap<Integer, Object>();
					m.dump(map);

					List<Integer> keyList = new ArrayList<Integer>(map.keySet());
					Collections.sort(keyList);				
					
					strBuf.append("<dl><dt>&ltiso-Iso8583Message&gt</dt><dd>");
					for (Integer key : keyList) {
						strBuf.append("&ltiso-field id=\"" + key + "\"" + " value=\"" + map.get(key) + "\"&gt<br/>");
					}
					strBuf.append("</dd><dt>&ltiso-Iso8583Message&gt</dt></dl>");
					System.out.println(strBuf.toString());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error printing ISO-8583 Iso8583Message!!!");
				}
			}
			
		}
		
		return strBuf.toString();
	}

	public static String print(String raw, String configPath,
			String endpointCode) {
        Iso8583Message m = unpack(raw, configPath, endpointCode);
		return (m != null) ? m.toString() : "";
	}

	private static Iso8583Message unpack(String raw, String configPath, String endpointCode) {
		Iso8583MessageSerializer mzer = new Iso8583MessageSerializer(
				configPath + System.getProperty("file.separator") + endpointCode + ".xml");
		Iso8583Message m = new Iso8583Message();
		
		//20-8-2013 edited by Sangbas
		if(endpointCode.equals("bils0")) {
			try {
				mzer.read(raw.getBytes(), m);
			} catch (IOException e) {
				m = null;
				System.out.println("UNPACK ISO Iso8583Message GAGAL !!!! " + e.toString());
			}
		}
		
		
		return m;
	}
	
}
