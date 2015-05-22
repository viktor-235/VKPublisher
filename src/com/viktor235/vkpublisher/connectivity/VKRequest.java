package com.viktor235.vkpublisher.connectivity;

import org.apache.http.client.HttpClient;

public class VKRequest extends Request
{
	public VKRequest(HttpClient httpClient, String URI)
	{
		super(httpClient, URI);
	}

	public VKResponse send()
	{
		return new VKResponse(super.send());
	}
}
