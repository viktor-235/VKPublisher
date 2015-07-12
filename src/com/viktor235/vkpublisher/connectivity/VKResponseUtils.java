package com.viktor235.vkpublisher.connectivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class VKResponseUtils {
	private static JsonArray getResponseArray(VKResponse response)
	{
		return response.getJsonObject().getAsJsonArray("response");
	}

	private static JsonObject getResponseObject(VKResponse response)
	{
		return response.getJsonObject().getAsJsonObject("response");
	}

	public static String findString(VKResponse response, String key) {
		try {
			return response.getJsonObject().get(key).getAsString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}

	public static int findInt(VKResponse response, String key) {
		try {
			return response.getJsonObject().get(key).getAsInt();
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}

	public static int getFirstUserId(VKResponse response) throws IllegalArgumentException {
		try {
			return getResponseArray(response).
					get(0).getAsJsonObject().
					get("id").getAsInt();
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}

	public static String getUploadURL(VKResponse response) throws IllegalArgumentException {
		try {
			return getResponseObject(response).
					get("upload_url").getAsString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}

	/* Photo */
	public static String getServer(VKResponse response) throws IllegalArgumentException {
		try {
			return response.getJsonObject().get("server").getAsString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}

	public static String getPhoto(VKResponse response) throws IllegalArgumentException {
		try {
			String photo = response.getJsonObject().get("photo").getAsString();
			/*JsonElement jsonElement = new JsonParser().parse(photo);
			return jsonElement.getAsJsonArray().get(0).getAsJsonObject().get("photo").getAsString();*/
			return photo;

		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}

	public static String getHash(VKResponse response) throws IllegalArgumentException {
		try {
			return response.getJsonObject().get("hash").getAsString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad input params(" +
					e.getMessage() + "): " + response);
		}
	}
}
