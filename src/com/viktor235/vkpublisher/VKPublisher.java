package com.viktor235.vkpublisher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.accesstoken.AccessTokenGetter;

public class VKPublisher {
	private static String accessTokenFile = "at";

	public static void main(String[] args) {
		loadSwtJar();
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

	private static void loadSwtJar() {
	    try {
	        String osName = System.getProperty("os.name").toLowerCase();
			String osArch = System.getProperty("os.arch").toLowerCase();

			/*URLClassLoader classLoader = (URLClassLoader) VKPublisher.class.getClassLoader();
			Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addUrlMethod.setAccessible(true);*/

			String swtFileNameOsPart =
					osName.contains("win") ? "win32" :
					osName.contains("linux") || osName.contains("nix") ? "linux-gtk" :
					osName.contains("mac") ? "osx" :
					"";
			if (swtFileNameOsPart.isEmpty())
				throw new Exception("Unsupported operation system");

	        String swtFileNameArchPart = osArch.contains("64") ? "x64" : "x86";
	        String swtFileName = "swt-" + swtFileNameOsPart + "-" + swtFileNameArchPart + ".jar";
	        
	        /*URL swtFileUrl = new URL("file://" + swtFileName);
	        addUrlMethod.invoke(classLoader, swtFileUrl);*/

	        //File jarFile = new File(swtFileName);
	        //URL url = jarFile.toURI().toURL();
	        
	        URL url = VKPublisher.class.getClassLoader().getResource(swtFileName);
	        File file = new File(url.getFile());
	        System.out.println(file);
	        System.out.println(file.exists());
	        System.out.println(url);
	        //addUrlMethod.invoke(classLoader, url);
	        
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<?> urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL", new Class<?>[] { URL.class });
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[] { url });
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}