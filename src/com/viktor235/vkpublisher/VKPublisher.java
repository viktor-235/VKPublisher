package com.viktor235.vkpublisher;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.accesstoken.AccessTokenGetter;
import com.viktor235.vkpublisher.connectivity.VKClient;
import com.viktor235.vkpublisher.connectivity.VKPost;
import com.viktor235.vkpublisher.connectivity.attachment.DocAttachment;
import com.viktor235.vkpublisher.connectivity.attachment.PhotoAttachment;
import com.viktor235.vkpublisher.properties.ProjectProperties;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VKPublisher {
	private final static Logger logger = LogManager.getLogger();

	private final static String CONFIG_FILE = "config.properties";

	public static void main(String[] args) {
		ProjectProperties properties = new ProjectProperties(CONFIG_FILE);

		VKapi vkApi = new VKapi("5.29");
		AccessTokenGetter atg = new ComplexAccessTokenGetter(vkApi, properties.getAccessTokenFileName());
		AccessToken at = atg.getAccessToken();
		if (at == null) {
			logger.error("Access token wasn't received");
			return;
		}
		vkApi.setAccessToken(at);

		VKClient vkClient = new VKClient(vkApi);

		//vkClient.send(vkApi.getUserId("viktor_klochkov"), new VKPost("Test message"));

		/*VKPost post = new VKPost("VK Api test message");
		vkClient.post(vkApi.getUserId("viktor_klochkov"), post);*/

		VKPost post = new VKPost("VK Api test post");
		post.addAttachment(new PhotoAttachment(true, "http://www.zooblog.ru/uploads/posts/2011-03/1300872933_14.jpg"));
		post.addAttachment(new PhotoAttachment(false, "content\\1.jpg"));
		//post.addAttachment(new PhotoAttachment("C:\\1.gif"));
		vkClient.post(vkApi.users_get("viktor_klochkov"), post);

		/*VKPost post = new VKPost("VK Api - test GIF");
		post.addAttachment(new DocAttachment(true, "http://cs6299.vk.me/u193104126/docs/2e4750bef49a/3263-vault-boy-approves_zpsd28b23c1.gif"));
		post.addAttachment(new DocAttachment(false, "content\\1.gif"));
		vkClient.post(null, post);*/

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