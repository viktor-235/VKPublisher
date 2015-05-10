package com.viktor235.vkpublisher;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.accesstoken.AccessTokenGetter;

public class VKPublisher {
	private static String accessTokenFile = "at";

	public static void main(String[] args) {
		VKapi vk = new VKapi();
		AccessTokenGetter atg = new ComplexAccessTokenGetter(vk, accessTokenFile);

		AccessToken at = atg.getAccessToken();
		if (at == null) {
			System.out.println("Access token wasn't received");
			return;
		}

		vk.setAccessToken(at);

		// System.out.println(vk.getNewMessage());
		// System.out.println(vk.getUserId("rissolenka"));
		System.out.println(vk.getUserId(""));

		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss_dd.MM.yyy");

		// vk.sendMessage("viktor_klochkov", sdf.format(new Date()));
		vk.sendMessage("viktor_klochkov", "Test Message");
		vk.postToWall(0, "Test_post_from_VK_api", true);

		// vk.sendMessage("std_full", "LALKA1");
		// vk.postToWall(62514768, "Test_post_from_VK_api", true);
		// vk.getUrlForUploadPhotoToWall(0);

		// PrivateMessageBroadcaster bc = new PrivateMessageBroadcaster(vk,
		// 3000);
		// bc.setParams("viktor_klochkov", "hello");
		/*
		 * PublicPostBroadcaster bc = new PublicPostBroadcaster(vk, 10000);
		 * bc.setParams(-22107853, "Test_message_from_VK.api"); Thread worker =
		 * new Thread(bc); worker.setDaemon(true); worker.run();
		 */
	}
}