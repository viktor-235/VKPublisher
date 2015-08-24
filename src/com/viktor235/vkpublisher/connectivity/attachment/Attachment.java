package com.viktor235.vkpublisher.connectivity.attachment;

import com.viktor235.vkpublisher.VKapi;

/**
 * Created by Виктор on 29.07.2015.
 */
public interface Attachment {
	String getAttachment(VKapi vkApi, Integer groupID);
}
