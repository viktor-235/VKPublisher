package com.viktor235.vkpublisher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

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

		vk.sendMessage("viktor_klochkov", "Test Message");
		//System.out.println(vk.getNewMessage());
		//System.out.println(vk.getUserId("rissolenka"));
		//System.out.println(vk.getUserId("viktor_klochkov"));

		//System.out.println(vk.getWallUploadServer(62514768));
		
		URL url = null;
		try
		{
			url = new URL("http://moi-portal.ru/uploads/images/00/00/02/2013/05/30/dc70d6.jpg");
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        InputStream is = null;
		try
		{
			is = url.openStream();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] bytes = null;
		try
		{
			bytes = IOUtils.toByteArray(is);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try
		{
			is.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("picture loaded");
		vk.uploadPhotoToWall(62514768, bytes);

		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss_dd.MM.yyy");

		// vk.sendMessage("viktor_klochkov", sdf.format(new Date()));
		//vk.sendMessage("viktor_klochkov", "Test Message");
		//vk.postToWall(0, "Test_post_from_VK_api", true);

		// vk.sendMessage("std_full", "LALKA1");
		// vk.postToWall(62514768, "Test_post_from_VK_api", true);
		// vk.getUrlForUploadPhotoToWall(0);

		//PrivateMessageBroadcaster bc = new PrivateMessageBroadcaster(vk, 1000);
		//bc.setParams("viktor_klochkov", "hello");
		
		//PublicPostBroadcaster bc = new PublicPostBroadcaster(vk, 10000);
		//bc.setParams(-22107853, "Test_message_from_VK.api");
		
		//Thread worker = new Thread(bc); worker.setDaemon(true); worker.run();

		//vk.dispose();
	}
}