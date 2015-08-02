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
public class VKDocFromFileAttachment implements VKAttachment {
	private byte[] bytes;
	private String shortFileName;

	public VKDocFromFileAttachment(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("Attachment file(" + fileName + ") not found");
			return;
		}
		this.shortFileName = file.getName();

		try(InputStream is = new FileInputStream(file)) {
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAttachment(VKapi vkApi, Integer userOrGroupID) {
		String uploadServerURL = vkApi.docs_getWallUploadServer(userOrGroupID);
		VKResponse response;

		response = vkApi.uploadFileToServer(uploadServerURL, "file", shortFileName, bytes);
		String f = VKResponseUtils.findString(response, "file");
		response = vkApi.docs_save(f, shortFileName, "");
		System.out.println(response);

		int media_id = VKResponseUtils.findIntInResponse(response, "id");
		String owner_id = VKResponseUtils.findStringInResponse(response, "owner_id");

		return "doc" + owner_id + "_" + media_id;
	}
}
