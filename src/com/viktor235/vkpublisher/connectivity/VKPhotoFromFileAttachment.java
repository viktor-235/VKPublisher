package com.viktor235.vkpublisher.connectivity;

import com.viktor235.vkpublisher.VKapi;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by Виктор on 29.07.2015.
 */
public class VKPhotoFromFileAttachment implements VKAttachment {
	private byte[] bytes;
	private String fileName;

	public VKPhotoFromFileAttachment(String fileName) {
		this.fileName = fileName;
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("Attachment file(" + fileName + ") not found");
			return;
		}

		try(InputStream is = new FileInputStream(file)) {
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAttachment(VKapi vkApi, Integer userOrGroupID) {
		String uploadServerURL = vkApi.getWallUploadServer(userOrGroupID);
		VKResponse response;

		response = vkApi.uploadFileToServer(uploadServerURL, "photo", fileName, bytes);
		String server = VKResponseUtils.findString(response, "server");
		String photo = VKResponseUtils.findString(response, "photo");
		String hash = VKResponseUtils.findString(response, "hash");

		response = vkApi.photos_saveWallPhoto(userOrGroupID, server, photo, hash);
		int media_id = VKResponseUtils.findIntInResponse(response, "id");
		String owner_id = VKResponseUtils.findStringInResponse(response, "owner_id");

		return "photo" + owner_id + "_" + media_id;
	}
}
