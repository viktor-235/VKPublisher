package com.viktor235.vkpublisher.accesstokengetters;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.viktor235.utils.FileUtils;
import com.viktor235.vkpublisher.VKapi;

public class WebGUIAccessTokenGetter
{
	private VKapi vkApi;
	private String fileWithAT = "config.cfg";
	
	public WebGUIAccessTokenGetter(VKapi vkApi)
	{
		this.vkApi = vkApi;
	}
	
	private String getUrlWithATFromBrowser(String URL)
	{
		AccessForm af = new AccessForm();
		af.openForm(URL);
		//System.out.println(af.getUrl());
		return af.getUrl();
	}
	
	public String getAccessToken() throws ClientProtocolException, IOException
	{
		String at = null;
		boolean atFileExist = new File(fileWithAT).exists();
		boolean validatingSuccess = false;
		
		if (atFileExist)
		{
			at = FileUtils.readFile(fileWithAT);
			validatingSuccess = vkApi.validateAccessToken(at);
			System.out.println("Validating from file result: " + validatingSuccess);
		}
		
		if (!atFileExist || !validatingSuccess)
		{
			at = vkApi.parseAccessToken(getUrlWithATFromBrowser(vkApi.getConnectionUrl()));
			validatingSuccess = vkApi.validateAccessToken(at);
			if (validatingSuccess)
				FileUtils.writeFile(fileWithAT, at);
			System.out.println("Validating from browser result: " + validatingSuccess);
		}
		
		if (validatingSuccess)
			System.out.println("Access token received");
		else
			System.out.println("Access token NOT received");
		
		return at; // May be null
	}
}
