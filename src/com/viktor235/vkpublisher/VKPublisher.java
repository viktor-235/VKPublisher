package com.viktor235.vkpublisher;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.accesstoken.AccessTokenGetter;
import com.viktor235.vkpublisher.properties.ProjectProperties;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class VKPublisher {
	private final static String CONFIG_FILE = "config.properties";

	public static void main(String[] args) {
		ProjectProperties properties = new ProjectProperties(CONFIG_FILE);

		VKapi vk = new VKapi("4786761", "offline,messages,status,wall,photos",
				"http://oauth.vk.com/blank.html", "popup", "token", "5.29");
		AccessTokenGetter atg = new ComplexAccessTokenGetter(vk, properties.getAccessTokenFileName());

		AccessToken at = atg.getAccessToken();
		if (at == null) {
			System.out.println("Access token wasn't received");
			return;
		}

		vk.setAccessToken(at);

		vk.sendMessage("viktor_klochkov", "Test Message");
		// System.out.println(vk.getNewMessage());
		// System.out.println(vk.getUserId("rissolenka"));
		// System.out.println(vk.getUserId("viktor_klochkov"));

		// System.out.println(vk.getWallUploadServer(62514768));

		String path = "C:\\1.jpg";
		
		/*URL url = null;
		try {
			url = new URL("http://moi-portal.ru/uploads/images/00/00/02/2013/05/30/dc70d6.jpg");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		File file = new File(path);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vk.uploadPhotoToWall(vk.getUserId("viktor_klochkov"), bytes);

		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss_dd.MM.yyy");

		// vk.sendMessage("viktor_klochkov", sdf.format(new Date()));
		// vk.sendMessage("viktor_klochkov", "Test Message");
		// vk.postToWall(0, "Test_post_from_VK_api", true);

		// vk.sendMessage("std_full", "LALKA1");
		// vk.postToWall(62514768, "Test_post_from_VK_api", true);
		// vk.getUrlForUploadPhotoToWall(0);

		// PrivateMessageBroadcaster bc = new PrivateMessageBroadcaster(vk,
		// 1000);
		// bc.setParams("viktor_klochkov", "hello");

		// PublicPostBroadcaster bc = new PublicPostBroadcaster(vk, 10000);
		// bc.setParams(-22107853, "Test_message_from_VK.api");

		// Thread worker = new Thread(bc); worker.setDaemon(true); worker.run();

		// vk.dispose();
	}
}