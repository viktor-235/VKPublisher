package com.viktor235.vkpublisher.connectivity.attachment;

import com.viktor235.vkpublisher.VKapi;
import com.viktor235.vkpublisher.connectivity.VKResponse;
import com.viktor235.vkpublisher.connectivity.VKResponseUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Виктор on 29.07.2015.
 */
public class DocAttachment implements Attachment {
	private final static Logger logger = LogManager.getLogger();

	private byte[] bytes;
	private String fileName;

	public DocAttachment(boolean isUrl, String path) {
		fileName = FilenameUtils.getName(path);
		if (isUrl) {
			try {
				URL url = new URL(path);
				bytes = IOUtils.toByteArray(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			File file = new File(path);
			if (!file.exists()) {
				logger.error("File(" + path + ") not found");
				return;
			}
			try(InputStream is = new FileInputStream(file)) {
				bytes = IOUtils.toByteArray(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getAttachment(VKapi vkApi, Integer userOrGroupID) {
		String uploadServerURL = vkApi.docs_getWallUploadServer(userOrGroupID);
		VKResponse response;

		response = vkApi.uploadFileToServer(uploadServerURL, "file", fileName, bytes);
		String f = VKResponseUtils.findString(response, "file");
		response = vkApi.docs_save(f, fileName, "");
		System.out.println(response);

		int media_id = VKResponseUtils.findIntInResponse(response, "id");
		String owner_id = VKResponseUtils.findStringInResponse(response, "owner_id");

		return "doc" + owner_id + "_" + media_id;
	}
}
