package com.viktor235.vkpublisher.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Виктор on 22.07.2015.
 */
public class ProjectProperties {
	private String fileDescription = "Configuration file for VKPublisher";
	private String accessTokenFileName = "at";

	public ProjectProperties(String configFileName) {
		Map<String, String> defaultProperties = new HashMap<>();
		defaultProperties.put("AccessTokenFile", accessTokenFileName);

		PropertiesHandler propHandler = new PropertiesHandler(configFileName, defaultProperties, fileDescription);

		accessTokenFileName = propHandler.getProperty("AccessTokenFile");
	}

	public String getAccessTokenFileName() {
		return accessTokenFileName;
	}
}
