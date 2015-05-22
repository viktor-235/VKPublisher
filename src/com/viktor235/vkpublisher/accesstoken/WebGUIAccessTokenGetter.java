package com.viktor235.vkpublisher.accesstoken;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.viktor235.vkpublisher.VKPublisher;
import com.viktor235.vkpublisher.VKapi;

public class WebGUIAccessTokenGetter implements AccessTokenGetter {
	private VKapi vkApi;
	
	static {
		loadSwtJar();
	}

	public WebGUIAccessTokenGetter(VKapi vkApi) {
		this.vkApi = vkApi;
	}

	private String getUrlWithATFromBrowser(String URL) {
		AccessForm af = new AccessForm();
		af.openForm(URL);
		// System.out.println(af.getUrl());
		return af.getUrl();
	}

	@Override
	public AccessToken getAccessToken() {
		String atText = vkApi.parseAccessToken(getUrlWithATFromBrowser(vkApi.getConnectionUrl()));
		return new AccessToken(atText);
	}

	private static void loadSwtJar() {
	    
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
		if (swtFileNameOsPart.isEmpty()) {
			System.out.println("Unsupported operation system");
			return;
		}

        String swtFileNameArchPart = osArch.contains("64") ? "x64" : "x86";
        String swtFileName = "swt-" + swtFileNameOsPart + "-" + swtFileNameArchPart + ".jar";
        
        /*URL swtFileUrl = new URL("file://" + swtFileName);
        addUrlMethod.invoke(classLoader, swtFileUrl);*/

        //File jarFile = new File(swtFileName);
        //URL url = jarFile.toURI().toURL();
	    loadJar(swtFileName);
	}
	
	public static void loadJar(String fileName) {
		try {
	        URL url = VKPublisher.class.getClassLoader().getResource(fileName);
	        //File file = new File(url.getFile());
	        
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<?> urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL", new Class<?>[] { URL.class });
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[] { url });

	        System.out.println("Loaded: " + url);
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}
