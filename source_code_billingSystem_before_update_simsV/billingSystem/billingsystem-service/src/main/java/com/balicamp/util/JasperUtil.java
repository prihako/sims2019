package com.balicamp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;

public class JasperUtil {
	public static Map<String, JRBand> getJRBandMap(JasperDesign jasperDesign){
		Map<String, JRBand> bandMap = new HashMap<String, JRBand>();
		
		//backgroundBand
		JRBand backgroundBand = jasperDesign.getBackground();
		if ( backgroundBand != null ) bandMap.put("background", backgroundBand);

		//title
		JRBand titleBand = jasperDesign.getTitle();
		if ( titleBand != null ) bandMap.put("title",titleBand);

		//pageHeader
		JRBand pageHeaderBand = jasperDesign.getPageHeader();
		if ( pageHeaderBand != null ) bandMap.put("pageHeader",pageHeaderBand);

		//columnHeader
		JRBand columnHeaderBand = jasperDesign.getColumnHeader();
		if ( columnHeaderBand != null ) bandMap.put("columnHeader",columnHeaderBand);

		//detail
		JRBand detailBand = jasperDesign.getDetail();
		if ( detailBand != null ) bandMap.put("detail",detailBand);

		//columnFooter
		JRBand columnFooterBand = jasperDesign.getColumnFooter();
		if ( columnFooterBand != null ) bandMap.put("columnFooter", columnFooterBand);

		//pageFooter
		JRBand pageFooterBand = jasperDesign.getPageFooter();
		if ( pageFooterBand != null ) bandMap.put("pageFooter", pageFooterBand);

		//lastPageFooter
		JRBand lastPageFooterBand = jasperDesign.getLastPageFooter();
		if ( lastPageFooterBand != null ) bandMap.put("lastPageFooter", lastPageFooterBand);

		//summary
		JRBand summaryBand = jasperDesign.getSummary();
		if ( summaryBand != null ) bandMap.put("summary", summaryBand);

		//noData
		JRBand noDataBand = jasperDesign.getNoData();
		if ( noDataBand != null ) bandMap.put("noData", noDataBand);
		
		return bandMap;
	}
	
	public static List<JRDesignImage> getDesignImageList(JasperDesign jasperDesign){
		Map<String, JRBand> bandMap = getJRBandMap(jasperDesign);
		return getDesignImageList(bandMap);
	}
	public static List<JRDesignImage> getDesignImageList(Map<String, JRBand> bandMap){
		List<JRDesignImage> designImageList = new ArrayList<JRDesignImage>(); 
		for (Entry<String, JRBand> entry : bandMap.entrySet()) {
			JRElement[] elementList =  entry.getValue().getElements();
			for (JRElement element : elementList) {
				if ( element instanceof JRDesignImage ){
					designImageList.add((JRDesignImage) element );
				}				
			}
		}
		return designImageList;	
	}

	
	public static void adjustResourceLocation(JasperDesign jasperDesign, String baseDir){
		Map<String, JRBand> bandMap = getJRBandMap(jasperDesign);
		for (Entry<String, JRBand> entry : bandMap.entrySet()) {
			JRElement[] elementList =  entry.getValue().getElements();
			for (JRElement element : elementList) {
				
				//image
				if ( element instanceof JRDesignImage ){
					adjustResourceImageLocation((JRDesignImage) element, baseDir);
				}
				
			}
		}
	}
	
	public static void adjustResourceImageLocation(JRDesignImage designImage, String baseDir){
		String textExpression = designImage.getExpression().getText();
		textExpression = textExpression.replaceAll("\"", "");
		String fileName = new File(textExpression).getName();
		String fullFileNamePath = 
			new StringBuilder("\"")
			.append(baseDir)
			.append(File.separator)
			.append(fileName)
			.append("\"")
			.toString();

		String osName = System.getProperty("os.name", "Linux");
		if ( osName.toLowerCase().indexOf("windows") != -1 ){
			fullFileNamePath = fullFileNamePath.replaceAll("\\\\", "\\\\\\\\");			
		}
		
		((JRDesignExpression)designImage.getExpression()).setText( fullFileNamePath );		
	}

}
