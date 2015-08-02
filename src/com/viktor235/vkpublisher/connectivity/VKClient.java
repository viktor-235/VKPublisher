package com.viktor235.vkpublisher.connectivity;

import com.viktor235.vkpublisher.VKapi;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Виктор on 29.07.2015.
 */
public class VKClient {
	private VKapi vkApi;

	public VKClient(VKapi vkApi) {
		this.vkApi = vkApi;
	}

	// For group ID need prefix '-'
	public void post(Integer userOrGroupID, VKPost post) {
		vkApi.postToWall(userOrGroupID, post.getMessage(), post.getAttachments(vkApi, userOrGroupID), false);
	}

	public void send(int userID, VKPost message) {
		vkApi.sendMessage(userID, message.getMessage());
	}

	public void saveDocument(String fileName, String title, String tags) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("Attachment file(" + fileName + ") not found");
			return;
		}

		byte[] bytes;
		try(InputStream is = new FileInputStream(file)) {
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String uploadServerURL = vkApi.docs_getUploadServer(null);
		VKResponse response = vkApi.uploadFileToServer(uploadServerURL, "file", fileName, bytes);
		String f = VKResponseUtils.findString(response, "file");
		vkApi.docs_save(f, title, tags);
	}
}
