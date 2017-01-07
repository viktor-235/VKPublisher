package com.viktor235.vkpublisher.properties;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Виктор on 18.07.2015.
 */
public class PropertiesHandler {
	protected Properties properties;
	private String fileName;
	private Map<String, String> defaultPropertiesMap;
	private Map<String, String> propertiesMap;
	private String fileDescription;

	public PropertiesHandler(String fileName, Map<String, String> defaultProperties, String fileDescription) {
		this.fileName = fileName;
		this.defaultPropertiesMap = defaultProperties;
		this.fileDescription = fileDescription;
		properties = new Properties();

		File file = new File(fileName);
		if (file.exists() && !file.isDirectory()) {
			propertiesMap = readProperties();
		} else {
			writeProperties(fileName, defaultProperties);
			propertiesMap = defaultProperties;
		}
	}

	private Map<String, String> readProperties() {
		HashMap<String, String> result = new HashMap<>();
		try (InputStream input = new FileInputStream(fileName)) {
			properties.load(input);
			Enumeration enumeration = properties.keys();
			String key;
			while (enumeration.hasMoreElements()) {
				key = enumeration.nextElement().toString();
				result.put(key, properties.getProperty(key));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private void writeProperties(String fileName, Map<String, String> propertiesMap) {
		try (OutputStream output = new FileOutputStream(fileName)) {
			properties.putAll(propertiesMap);
			properties.store(output, fileDescription);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getProperties() {
		return propertiesMap;
	}

	public String getProperty(String key) {
		return propertiesMap.get(key);
	}
}
