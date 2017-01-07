package com.viktor235.vkpublisher.accesstoken;

import com.viktor235.vkpublisher.VKapi;

public class AccessToken {
	private String accessToken;

	public AccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public boolean isValid(VKapi vkApi) {
		return vkApi.validateAccessToken(accessToken);
	}

	@Override
	public String toString() {
		return accessToken;
	}
}
