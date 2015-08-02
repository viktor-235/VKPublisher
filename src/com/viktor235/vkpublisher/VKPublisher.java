package com.viktor235.vkpublisher;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.accesstoken.AccessTokenGetter;
import com.viktor235.vkpublisher.connectivity.VKClient;
import com.viktor235.vkpublisher.connectivity.VKDocFromFileAttachment;
import com.viktor235.vkpublisher.connectivity.VKPost;
import com.viktor235.vkpublisher.properties.ProjectProperties;

public class VKPublisher {
	private final static String CONFIG_FILE = "config.properties";

	public static void main(String[] args) {
		ProjectProperties properties = new ProjectProperties(CONFIG_FILE);

		VKapi vkApi = new VKapi("5.29");
		AccessTokenGetter atg = new ComplexAccessTokenGetter(vkApi, properties.getAccessTokenFileName());
		AccessToken at = atg.getAccessToken();
		if (at == null) {
			System.out.println("Access token wasn't received");
			return;
		}
		vkApi.setAccessToken(at);

		VKClient vkClient = new VKClient(vkApi);

		//vkClient.send(vkApi.getUserId("viktor_klochkov"), new VKPost("Test message"));

		/*VKPost post = new VKPost("VK Api test message");
		vkClient.post(vkApi.getUserId("viktor_klochkov"), post);*/

		/*VKPost post = new VKPost("VK Api test post");
		post.addAttachment(new VKPhotoFromFileAttachment("C:\\1.jpg"));
		post.addAttachment(new VKPhotoFromFileAttachment("C:\\2.jpg"));
		//post.addAttachment(new VKPhotoFromFileAttachment("C:\\1.gif"));
		vkClient.post(vkApi.getUserId("viktor_klochkov"), post);*/

		VKPost post = new VKPost("VK Api - test GIF");
		post.addAttachment(new VKDocFromFileAttachment("C:\\2.gif"));
		vkClient.post(null, post);

		//vkClient.saveDocument("C:\\2.gif", "Test file", "tag1, tag2");

		// std_full

		// PrivateMessageBroadcaster bc = new PrivateMessageBroadcaster(vkApi,
		// 1000);
		// bc.setParams("viktor_klochkov", "hello");

		// PublicPostBroadcaster bc = new PublicPostBroadcaster(vkApi, 10000);
		// bc.setParams(-22107853, "Test_message_from_VK.api");
		// Thread worker = new Thread(bc); worker.setDaemon(true); worker.run();
	}
}