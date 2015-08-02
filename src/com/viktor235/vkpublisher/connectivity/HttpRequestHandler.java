package com.viktor235.vkpublisher.connectivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by battlemaster on 12.07.15.
 */
public class HttpRequestHandler {
    private final HttpClient client;

    public HttpRequestHandler(HttpClient client) {
        this.client = client;
    }

    public VKResponse execute(Request request) {
        String responseText = null;
        request.compile();
        return execute(request.getHttpPost());
    }

    public VKResponse execute(HttpPost httpPost) {
        String responseText = null;
        try {
            HttpResponse response = client.execute(httpPost);
            // IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("REQUEST: " + httpPost.toString());
		try {
            HttpEntity entity = httpPost.getEntity();
            System.out.print("  ENTITY: ");
            if (entity instanceof MultipartEntity)
                System.out.println("MultipartEntity...");
            else
                System.out.println(EntityUtils.toString(entity, "UTF-8"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        System.out.println("RESPONSE: " + responseText);

        return new VKResponse(responseText);
    }

}
