package com.vkpublisher.response;

public class VKResponseUtils
{
	public static int getFirstUserId(VKResponse response) throws IllegalArgumentException
	{
		try
		{
			return response.getJsonObject().getAsJsonArray("response").
					get(0).getAsJsonObject().
					get("id").getAsInt();
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Bad input params(" + e.getMessage() + "): " + response);
		}
	}
}
