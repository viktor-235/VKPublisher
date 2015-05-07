package com.vkpublisher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

public class PrivateMessageBroadcaster extends Broadcaster
{
	private String userDomain,
			message;
	
	public PrivateMessageBroadcaster(VKapi vkApi, int pauseLength)
	{
		super(vkApi, pauseLength);
	}
	
	public void setParams(String userDomain, String message)
	{
		this.userDomain = userDomain;
		this.message = message;
	}
	
	@Override
	public void execute() throws ClientProtocolException, IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss_dd.MM.yyy");
		vkApi.sendMessage(userDomain, message + "_" + sdf.format(new Date()));
	}

}
