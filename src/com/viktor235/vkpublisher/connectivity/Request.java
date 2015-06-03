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
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public abstract class Request<T> {
	private HttpClient httpClient;
	private HttpPost httpPost;
	private List<NameValuePair> params;

	protected abstract T send();

	public Request(HttpClient httpClient, String URI) {
		this.httpClient = httpClient;
		httpPost = new HttpPost(URI);
		params = null;
	}

	public void addParam(String name, String value) {
		if (params == null)
			params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair(name, value));
	}

	public void addParam(String name, int value) {
		addParam(name, String.valueOf(value));
	}

	/*public void addParam(String name, byte[] value) {
		if (params == null)
			params = new ArrayList<NameValuePair>();

		ByteArrayBody byteArrayBody = new ByteArrayBody(value,
				"photo.jpg");
		mpEntity.addPart("photo", byteArrayBody);

		httppost.setEntity(mpEntity);
		
		params.add(new BasicNameValuePair(name, value));
	}*/

	public String compileAndSend() {
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setEntity(entity);

		return execute(httpClient, httpPost);
	}

	public static String execute(HttpClient httpClient, HttpPost request) {
		String responseText = null;
		try {
			HttpResponse response = httpClient.execute(request);
			// jsonResponse =
			// IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.print("REQUEST: " + request);
		/*try {
			System.out.println(" ENTITY: "
					+ EntityUtils.toString(request.getEntity(), "UTF-8"));
		} catch (ParseException | IOException e) {
			System.out.println(e.getMessage());
		}*/
		System.out.println("RESPONSE: " + responseText);

		return responseText;
	}
}
