/**
 * 
 */
package com.balicamp.util.mx;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

/**
 * Bean Utils dengan tambahan method
 * @version $Id: BeanUtility.java 504 2013-05-24 08:15:06Z rudi.sadria $
 */
public class BeanUtility extends BeanUtils {

	private static BeanUtility instance = new BeanUtility();

	private BeanUtility() {
	}

	public static BeanUtility getInstance() {
		if (instance == null)
			instance = new BeanUtility();
		return instance;
	}

	/**
	 * copy property dari bean 1 ke bean lain
	 * @param source Bean source
	 * @param target Bean target
	 * @param copiedProperties hanya property yang di assign saja
	 */
	public static void copyPropertiesWithSpecifiedItemOnly(Object source, Object target, String[] copiedProperties) {
		if (source == null || target == null)
			return;
		PropertyDescriptor[] desc = BeanUtils.getPropertyDescriptors(source.getClass());

		Map<String, Integer> dummyIndexer = new HashMap<String, Integer>();
		for (String p : copiedProperties) {
			dummyIndexer.put(p, 1);

		}
		List<String> ignored = new ArrayList<String>();
		for (PropertyDescriptor scn : desc) {

			System.out.println(" prop : " + scn.getName());
			if (!dummyIndexer.containsKey(scn.getName())) {
				ignored.add(scn.getName());

			}
		}
		String[] ignoredProps = new String[ignored.size()];
		ignored.toArray(ignoredProps);

		copyProperties(source, target, ignoredProps);
	}

	/**
	 * copy bean antara 2 List<BEAN>
	 * @param source ; list asal
	 * @param target ; list tujuan
	 * @param cl ; class dari target(tujuan)
	 * @param copiedProperties
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public <SOURCE, DEST> void copyListPropertiesWithSpecifiedItemOnly(List<SOURCE> source, List<DEST> target,
			DEST classTarget, String[] copiedProperties) {

		for (SOURCE obj : source) {
			DEST ds = (DEST) this.instantiate(classTarget.getClass());
			copyPropertiesWithSpecifiedItemOnly(obj, ds, copiedProperties);
			target.add(ds);
		}

	}
}
