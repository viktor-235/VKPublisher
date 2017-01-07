package com.viktor235.vkpublisher;

public abstract class Broadcaster implements Runnable {
	protected VKapi vkApi;
	private int pauseLength;

	public Broadcaster(VKapi vkApi, int pauseLength) {
		this.vkApi = vkApi;
		this.pauseLength = pauseLength;
	}

	@Override
	public void run() {
		while (true) {
			try {
				execute();
				Thread.sleep(pauseLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void execute();
}
