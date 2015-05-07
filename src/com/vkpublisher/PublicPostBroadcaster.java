package com.vkpublisher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

public class PublicPostBroadcaster extends Broadcaster
{
	private int userOrPublicId;
	private String message;
	
	public PublicPostBroadcaster(VKapi vkApi, int pauseLength)
	{
		super(vkApi, pauseLength);
	}
	
	public void setParams(int userOrPublicId, String message)
	{
		this.userOrPublicId = userOrPublicId;
		this.message = message;
	}
	
	@Override
	public void execute() throws ClientProtocolException, IOException
	{
		vkApi.postToWall(userOrPublicId, message, false);
	}

}
