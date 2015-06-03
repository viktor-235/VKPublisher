package com.viktor235.vkpublisher;


public class PublicPostBroadcaster extends Broadcaster {
	private int userOrPublicId;
	private String message;

	public PublicPostBroadcaster(VKapi vkApi, int pauseLength) {
		super(vkApi, pauseLength);
	}

	public void setParams(int userOrPublicId, String message) {
		this.userOrPublicId = userOrPublicId;
		this.message = message;
	}

	@Override
	public void execute() {
		vkApi.postToWall(userOrPublicId, message, null, false);
	}

}
