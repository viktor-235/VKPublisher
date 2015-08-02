package com.viktor235.vkpublisher.connectivity;

import com.viktor235.vkpublisher.VKapi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Виктор on 29.07.2015.
 */
public class VKPost {
	private String message;
	private List<VKAttachment> attachments;

	public VKPost() {
		attachments = new ArrayList<>();
	}

	public VKPost(String message) {
		this();
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void addAttachment(VKAttachment attachment) {
		attachments.add(attachment);
	}

	public String getMessage() {
		return message;
	}

	public String getAttachments(VKapi vkApi, Integer groupID) {
		String result = "";
		if (attachments.isEmpty())
			return null;
		for (VKAttachment attachment : attachments)
			result += attachment.getAttachment(vkApi, groupID) + ",";
		return result;
	}
}
