package com.viktor235.vkpublisher.accesstoken;

import java.io.File;

import com.viktor235.utils.FileUtils;

public class FileAccessTokenGetter implements AccessTokenGetter {
	private String fileName = "at";

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public AccessToken getAccessToken() {
		if (!new File(fileName).exists())
			return null;
		return new AccessToken(FileUtils.readFile(fileName));
	}
}
