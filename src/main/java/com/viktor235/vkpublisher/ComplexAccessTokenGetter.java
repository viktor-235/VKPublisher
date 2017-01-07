package com.viktor235.vkpublisher;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.accesstoken.AccessTokenGetter;
import com.viktor235.vkpublisher.accesstoken.FileAccessTokenGetter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComplexAccessTokenGetter implements AccessTokenGetter {
    private final static Logger logger = LogManager.getLogger();

	private VKapi vkApi;
	private String accessTokenFile;

	public ComplexAccessTokenGetter(VKapi vkApi, String accessTokenFile) {
		this.vkApi = vkApi;
		this.accessTokenFile = accessTokenFile;
	}

	@Override
	public AccessToken getAccessToken() {
		AccessTokenGetter atg;
		AccessToken at;

		atg = new FileAccessTokenGetter();
		((FileAccessTokenGetter) atg).setFileName(accessTokenFile);
		at = atg.getAccessToken();
		if (at != null && at.isValid(vkApi)) {
			logger.info("Access token was received from file");
			return at;
		}

		return null;
	}
}
