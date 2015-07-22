package com.viktor235.vkpublisher.connectivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by battlemaster on 12.07.15.
 */
public class VKClient {

    private final HttpClient client;

    public VKClient (HttpClient client) {
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
		/*try {
			System.out.println(" ENTITY: "
					+ EntityUtils.toString(request.getEntity(), "UTF-8"));
		} catch (ParseException | IOException e) {
			System.out.println(e.getMessage());
		}*/
        System.out.println("RESPONSE: " + responseText);

        return new VKResponse(responseText);
    }

}
