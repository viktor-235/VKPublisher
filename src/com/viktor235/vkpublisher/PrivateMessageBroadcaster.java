package com.viktor235.vkpublisher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrivateMessageBroadcaster extends Broadcaster {
	private String userDomain, message;

	public PrivateMessageBroadcaster(VKapi vkApi, int pauseLength) {
		super(vkApi, pauseLength);
	}

	public void setParams(String userDomain, String message) {
		this.userDomain = userDomain;
		this.message = message;
	}

	@Override
	public void execute() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss_dd.MM.yyy");
		vkApi.sendMessage(userDomain, message + "_" + sdf.format(new Date()));
	}

}
