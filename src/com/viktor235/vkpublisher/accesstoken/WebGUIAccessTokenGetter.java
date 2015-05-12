package com.viktor235.vkpublisher.accesstoken;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.viktor235.vkpublisher.VKapi;

public class WebGUIAccessTokenGetter implements AccessTokenGetter {
	private VKapi vkApi;
	
	static {
		//loadSwtJar();
	}

	public WebGUIAccessTokenGetter(VKapi vkApi) {
		this.vkApi = vkApi;
	}

	private String getUrlWithATFromBrowser(String URL) {
		AccessForm af = new AccessForm();
		af.openForm(URL);
		// System.out.println(af.getUrl());
		return af.getUrl();
	}

	@Override
	public AccessToken getAccessToken() {
		String atText = vkApi.parseAccessToken(getUrlWithATFromBrowser(vkApi.getConnectionUrl()));
		return new AccessToken(atText);
	}
	
}
