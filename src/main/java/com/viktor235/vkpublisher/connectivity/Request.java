package com.viktor235.vkpublisher.connectivity;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Request {
	private HttpPost httpPost;
	private List<NameValuePair> params;

	public Request(String URI) {
		httpPost = new HttpPost(URI);
		params = null;
	}

	public void addParam(String name, String value) {
		if (params == null) {
			params = new ArrayList<NameValuePair>();
		}

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

	public Request compile() {
		UrlEncodedFormEntity entity = null;
		if (params == null)
			return this;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setEntity(entity);
        return this;
	}

	public HttpPost getHttpPost() {
		return httpPost;
	}

	@Override
	public String toString() {
		return "Request{" +
				"httpPost=" + httpPost.toString() +
				", params=" + params.toString() +
				'}';
	}
}
