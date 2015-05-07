package com.vkpublisher.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VKResponse
{
	private String jsonText;
	private JsonObject jsonObject;
	
	private boolean error = false;
	private int errorCode;
	private String errorMsg;
	
	public VKResponse(String json)
	{
		jsonText = json;
		
		JsonElement jsonElement = new JsonParser().parse(json);
		jsonObject = jsonElement.getAsJsonObject();
		
		if (jsonObject.has("error"))
		{
			error = true;
			JsonObject errorObject = jsonObject.getAsJsonObject("error");
			errorCode = errorObject.get("error_code").getAsInt();
			errorMsg = errorObject.get("error_msg").getAsString();
		}
	}
	
	public boolean isError()
	{
		return error;
	}

	public String getErrorMsg()
	{
		return String.format("Error %d: %s", errorCode, errorMsg);
	}

	public JsonObject getJsonObject()	
	{
		return jsonObject;
	}
	
	@Override
	public String toString()
	{
		return jsonText;
	}
}
