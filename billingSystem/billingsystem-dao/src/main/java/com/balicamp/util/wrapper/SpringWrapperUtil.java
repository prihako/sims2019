package com.balicamp.util.wrapper;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class SpringWrapperUtil {

	public static void moduleMapCoppier(Map destinationMap, ApplicationContext applicationContext, String tailBeanNameSearch){
		String beanNameList[] = applicationContext.getBeanNamesForType(MapWrapper.class);
		for (String beanName : beanNameList) {
			if (beanName.endsWith(tailBeanNameSearch)) {
				MapWrapper moduleIsoConverterMaping = (MapWrapper) applicationContext.getBean(beanName);
				destinationMap.putAll(moduleIsoConverterMaping.getMap());
			}
		}		
	}

	public static void moduleListCoppier(List destinationList, ApplicationContext applicationContext, String tailBeanNameSearch){
		String beanNameList[] = applicationContext.getBeanNamesForType(ListWrapper.class);
		for (String beanName : beanNameList) {
			if (beanName.endsWith(tailBeanNameSearch)) {
				ListWrapper moduleIsoConverterMaping = (ListWrapper) applicationContext.getBean(beanName);
				destinationList.addAll(moduleIsoConverterMaping.getList());
			}
		}		
	}

}
