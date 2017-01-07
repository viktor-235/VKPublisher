package com.viktor235.vkpublisher.connectivity;

import com.viktor235.vkpublisher.VKapi;
import com.viktor235.vkpublisher.connectivity.attachment.Attachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ������ on 29.07.2015.
 */
public class VKPost {
	private String message;
	private List<Attachment> attachments;

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

	public void addAttachment(Attachment attachment) {
		attachments.add(attachment);
	}

	public String getMessage() {
		return message;
	}

	public String getAttachments(VKapi vkApi, Integer groupID) {
		String result = "";
		if (attachments.isEmpty())
			return null;
		for (Attachment attachment : attachments)
			result += attachment.getAttachment(vkApi, groupID) + ",";
		return result;
	}
}
