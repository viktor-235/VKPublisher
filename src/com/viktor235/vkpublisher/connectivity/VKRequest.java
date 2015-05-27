package com.viktor235.vkpublisher.connectivity;

import org.apache.http.client.HttpClient;

public class VKRequest extends Request<VKResponse>
{
	public VKRequest(HttpClient httpClient, String URI)
	{
		super(httpClient, URI);
	}

	@Override
	public VKResponse send() {
		return new VKResponse(compileAndSend());
	}
}
