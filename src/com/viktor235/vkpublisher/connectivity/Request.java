package com.viktor235.vkpublisher.connectivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Request<T> {
	private HttpClient httpClient;
	private HttpPost httpPost;
	private List<NameValuePair> params;
	
	public Request(HttpClient httpClient, String URI)
	{
		this.httpClient = httpClient;
		httpPost = new HttpPost(URI);
		params = null;

		HttpPost post = new HttpPost("https://api.vk.com/method/users.search");
	}

	public void addParam(String name, String value)
	{
		if (params == null)
			params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair(name, value));
	}
	
	public String send()
	{
		UrlEncodedFormEntity entity = null;
		try
		{
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		httpPost.setEntity(entity);
		
		return execute(httpClient, httpPost);
	}

	public static String execute(HttpClient httpClient, HttpPost request) {
		String responseText = null;
		try {
			HttpResponse response = httpClient.execute(request);
			//jsonResponse = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.print("REQUEST: " + request);
		try
		{
			System.out.println(" ENTITY: " + EntityUtils.toString(request.getEntity(), "UTF-8"));
		}
		catch (ParseException | IOException e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("RESPONSE: " + responseText);

		return responseText;
	}
}
