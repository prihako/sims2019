package com.balicamp.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.aop.support.AopUtils;

public class HibernateUtil {
	/**
	 * change CglibProxy object to actual object, with dept = 1
	 * @param inputObject
	 * @return
	 */
    public static Object castCglibProxyObjet(Object inputObject) 
	{
    	return castCglibProxyObjet(inputObject, 1);
	}
    
    /**
     * change CglibProxy object to actual object
     * @param inputObject
     * @param depth
     * @return
     */
    public static Object castCglibProxyObjet(Object inputObject, int depth) 
	{
    	if ( depth == 0 ){
    		return inputObject;
    	}
    	
    	try {
    		
    		if (!AopUtils.isCglibProxy(inputObject))  {
    			return inputObject;
    		}
    	
    		//get real class name
    		String name = inputObject.getClass().getName();
    		name = name.substring(0, name.indexOf("$$"));
    	
    		//instantiate new class
    		Class entityClass = Class.forName(name);
    		Object resultObject = entityClass.newInstance();
    		PropertyUtils.copyProperties(resultObject, inputObject);
    		
    		//recursive cast properties
    		Map<String,Object> beanDescMap = PropertyUtils.describe(resultObject);
    		for (Entry<String,Object> entry : beanDescMap.entrySet()) {
        		if (AopUtils.isCglibProxy(entry.getValue()))  {
        			Object newValue = castCglibProxyObjet(entry.getValue(),depth-1);
        			try {
            			PropertyUtils.setProperty(resultObject, entry.getKey(), newValue);        				
        			} catch (Exception e){													//NOPMD
        				//read only property, we can do noting just ignore it
        			}
        		}
			}
    		
    		return resultObject;    		
    	} catch (Exception e){
    		//if exception just return original object
    		return inputObject;
    	}
	}

}
