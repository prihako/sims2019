/**
 *
 */
package id.co.sigma.mx.project.epayment;

import id.co.sigma.mx.util.PropertiesUtil;

import java.util.Properties;

/**
 * @author hendy
 *
 */
public class UtilDao {
	static String PROPS_LOCATION = "sigma-data/mx";

	public static String defineRcDescription(String endpointCode, String responseCode){
		String rcDescription	= null;
		Properties properties 	= PropertiesUtil.fromFile(PROPS_LOCATION + "/rc.properties");
		rcDescription 			= properties.getProperty(endpointCode+"."+responseCode);
		return rcDescription;
	}
}
