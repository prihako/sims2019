import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

import com.balicamp.webapp.action.report.XlstoStringConverter;

public class XlsImportTest {

	// @Test
	public void testXLSdataImport() {

		// MyClass is tested
		XlstoStringConverter tester = new XlstoStringConverter();
		String filepath = "C:\\x\\MT9402015090310X.xls";
		HashMap<String, String> value = tester.getMt940Data(filepath);
		// assert statements
		assertEquals("test", value.get("0372597"));

	}

	@Test
	public void TestHasmap() {
		HashMap<String, Integer> team1 = new HashMap();
		Set<String> keys = team1.keySet();
		team1.put("1", 1);
		team1.put("2", 2);
		team1.put("3", 1);
		team1.put("4", 2);
		String[] keys1 = keys.toArray(new String[keys.size()]);
		// for (String key : team1.keySet()) {
		// keys.a
		//
		// }
		System.out.println(keys1);
	}

}
