package com.balicamp.service.impl.mx;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.balicamp.service.AbitMessageParser;
import com.balicamp.service.parameter.SystemParameterManager;
import com.balicamp.util.mx.AlphabitParameters;

@Component("abitMessageParser")
public class AbitMessageParserImpl implements AbitMessageParser {

	public static final String CHAR_NEWLINE = "\n";
	public static final String CHAR_TAB = "\t";
	public static final String MESSAGE_TYPE_QS = "QS";
	public static final String PARAM_EMPTY = "EMPTY";

	@Autowired
	private SystemParameterManager parameterManager;

	private List<AlphabitParameters> getAlphabitParameters(String fileContent) {
		List<AlphabitParameters> params = new ArrayList<AlphabitParameters>();

		String[] splits = fileContent.split(CHAR_NEWLINE);
		for (String split : splits) {
			String[] resplits = split.split(CHAR_TAB);
			params.add(new AlphabitParameters(resplits[0], resplits[1], Integer.parseInt(resplits[2]), Integer
					.parseInt(resplits[3])));
		}

		return params;
	}

	private List<String> getExcludedAlphabitParameters(String fileContent, String msgType) {
		List<String> params = new ArrayList<String>();

		String[] splits = fileContent.split(CHAR_NEWLINE);
		for (String split : splits) {
			String[] resplits = split.split(CHAR_TAB);

			if (!(resplits[1].equals(msgType) || resplits[1].equals(MESSAGE_TYPE_QS))) {
				params.add(resplits[0]);
			}
		}

		return params;
	}

	private String getFileContent(File abitConfig) {
		String content = new String();
		BufferedInputStream bis = null;
		byte[] bytes = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(abitConfig));
			bytes = new byte[bis.available()];

			bis.read(bytes);

			content = new String(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return content;
	}

	public HashMap<String, String> parseToHashMap(String message) {
		String msgType = message.substring(0, 2);
		String mxCode = message.substring(74, 77);

		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();

		File abitConfig = new File(configPath + System.getProperty("file.separator") + "ABIT" + mxCode + ".csv");
		if (!abitConfig.exists()) {
			return null;
		}

		try {
			HashMap<String, String> result = new HashMap<String, String>();
			String fileContent = getFileContent(abitConfig);
			List<AlphabitParameters> alphabitParams = getAlphabitParameters(fileContent);
			List<String> excludedAlphabitParams = getExcludedAlphabitParameters(fileContent, msgType);

			int startIndex = 0;
			for (int i = 0; i < alphabitParams.size(); i++) {
				AlphabitParameters param = alphabitParams.get(i);
				if (param.getName().equals(PARAM_EMPTY) || excludedAlphabitParams.contains(param.getName())) {
					startIndex += param.getLength();
					continue;
				}

				String sub = message.substring(startIndex, startIndex + param.getLength());
				result.put(param.getName(), sub);
				startIndex += param.getLength();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String parse(String message, boolean html) {
		String msgType = message.substring(0, 2);
		String mxCode = message.substring(74, 77);

		String separator = html ? "<br />" : "\r\n";

		String configPath = parameterManager.findByParamName("other.messageLogs.configFile").getParamValue();

		File abitConfig = new File(configPath + System.getProperty("file.separator") + "ABIT" + mxCode + ".csv");
		if (!abitConfig.exists()) {
			return null;
		}

		try {
			String result = new String();

			String fileContent = getFileContent(abitConfig);
			List<AlphabitParameters> alphabitParams = getAlphabitParameters(fileContent);
			List<String> excludedAlphabitParams = getExcludedAlphabitParameters(fileContent, msgType);

			int startIndex = 0;
			for (int i = 0; i < alphabitParams.size(); i++) {
				AlphabitParameters param = alphabitParams.get(i);
				if (param.getName().equals(PARAM_EMPTY) || excludedAlphabitParams.contains(param.getName())) {
					startIndex += param.getLength();
					continue;
				}

				String sub = message.substring(startIndex, startIndex + param.getLength());
				result += "\t" + param.getName() + " = " + sub.trim();
				if (i < alphabitParams.size() - 1) {
					result += separator;
				}

				startIndex += param.getLength();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}