package com.viktor235.vkpublisher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.viktor235.vkpublisher.response.VKResponse;
import com.viktor235.vkpublisher.response.VKResponseUtils;

public class VKapi{
	private HttpClient httpClient;
	
    private String client_id = "4786761";
    private String scope = "offline,messages,status,wall,photos";
    private String redirect_uri = "http://oauth.vk.com/blank.html";
    private String display = "popup";
    private String response_type = "token";
    private String version = "5.29";
    private String access_token = null;
    
    public VKapi()
    {
    	httpClient = new DefaultHttpClient();
    }
    
	public String getConnectionUrl()
	{
		return "http://oauth.vk.com/authorize?" +
				"client_id=" + client_id +
				"&scope=" + scope + 
				"&redirect_uri=" + redirect_uri +
				"&display=" + display +
				"&response_type=" + response_type;
    }
    
    public String parseAccessToken(String urlWithAT)
    {
    	//System.out.println(urlWithAT);
    	if (urlWithAT == null)
    		return null;
    	String accessToken = urlWithAT,
    			startKey = "#access_token=",
    			endKey = "&";
    	
    	int atStart = accessToken.indexOf(startKey) + startKey.length();
    	if (atStart < startKey.length())
    		return null;
    	
    	accessToken = accessToken.substring(atStart);
    	
    	int atEnd = accessToken.indexOf(endKey);
    	if (atEnd < 0)
    		return accessToken;
    	
    	//System.out.println(accessToken.substring(0, atEnd));
    	return accessToken.substring(0, atEnd);
    }
    
    public boolean validateAccessToken(String accessToken) throws ClientProtocolException, IOException
    {
		String post = "https://api.vk.com/method/users.search?" +
		        "q=Durov" +
		        "&count=1" +
		        "&v=" + version +
		        "&access_token=" + accessToken;
		return !sendRequest(post).isError();
    }
    
    public void setAccessToken(String accessToken)
    {
    	access_token = accessToken;
    }
    
    /*public static String responseToString(HttpResponse response) throws IllegalStateException, IOException
    {
		InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
		BufferedReader buff = new BufferedReader(isr);
        StringBuffer strBuff = new StringBuffer();
        
        String s;
        while ((s = buff.readLine()) != null)
            strBuff.append(s);
        
        return strBuff.toString();
    }*/
    
    public VKResponse sendRequest(String request) throws ClientProtocolException, IOException
    {
		HttpPost httpPost = new HttpPost(request);
        HttpResponse response = httpClient.execute(httpPost);
        String jsonResponse = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		
        System.out.println("REQUEST: " + request);
		System.out.println("RESPONSE: " + jsonResponse);
        
        VKResponse	vkResponse = new VKResponse(jsonResponse);
        if (vkResponse.isError())
        	System.out.println(vkResponse.getErrorMsg());
		
		return vkResponse;
    }

    public int getUserId(String userDomain) throws ClientProtocolException, IOException
    {
		String post = "https://api.vk.com/method/users.get?" +
		        "user_ids=" + userDomain +
		        "&v=" + version;
		VKResponse vkResponse = sendRequest(post);

		if (!vkResponse.isError())
			try
			{
				return VKResponseUtils.getFirstUserId(vkResponse);
			}
			catch (IllegalArgumentException e)
			{
				System.out.println("Error in user ID parsing: " + e.getMessage());
				return 0;
			}
		return 0;
    }

    /*public int getGroupId(String groupDomain) throws ClientProtocolException, IOException
    {
		String post = "https://api.vk.com/method/users.get?" +
		        "user_ids=" + userDomain +
		        "&v=" + version +
		        "&access_token=" + access_token;
		String jsonResponse = sendPost(post);
		
		VKResponse vkResponse = VKResponse.parseReponse(jsonResponse);
		if (vkResponse.isResponseIsArrayList())
			return vkResponse.getFirstUserId();
		
		System.out.println("Error in ID parsing");
		return 0;
    }*/
    
	public String getNewMessage() throws ClientProtocolException, IOException
	{
		String post = "https://api.vk.com/method/messages.get?out=0&access_token=" + access_token;
		VKResponse vkResponse = sendRequest(post);
		
		return vkResponse.toString();
	}
	
	public static String encodeToURL(String text)
	{
		try
		{
			return URLEncoder.encode(text, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return "(Unsupported+Encoding)";
		}
	}
	
	public void sendMessage(int userId, String message) throws ClientProtocolException, IOException
	{
		message = encodeToURL(message);

		System.out.println("Sending message: " + message);

		String post = "https://api.vk.com/method/messages.send?" +
		        "user_id=" + userId +
		        "&message=" + message +
		        "&v=" + version +
		        "&access_token=" + access_token;
		sendRequest(post);
	}

	public void sendMessage(String userDomain, String message) throws ClientProtocolException, IOException
	{
		message = encodeToURL(message);

		System.out.println("Sending message: " + message);

		String post = "https://api.vk.com/method/messages.send?" +
		        "domain=" + userDomain +
		        "&message=" + message +
		        "&v=" + version +
		        "&access_token=" + access_token;
		sendRequest(post);
	}
	
	public void setStatus(String status) throws ClientProtocolException, IOException
	{
		String post = "https://api.vk.com/method/status.set?" +
		        "text=" + status +
		        "&v=" + version +
		        "&access_token=" + access_token;
		sendRequest(post);
	}
	
	// Post to User wall or Public wall. if owner_id == null then post to SELF user wall
	public void postToWall(Integer owner_id, String message, boolean fromGroupName) throws ClientProtocolException, IOException
	{
		String post = "https://api.vk.com/method/wall.post?" +
		        (owner_id != null ? "owner_id=" + owner_id : "") +
		        "&from_group=" + (fromGroupName ? "1" : "0") +
		        "&message=" + message +
		        "&v=" + version +
		        "&access_token=" + access_token;
		sendRequest(post);
	}
	
	/* Photo */
	
	public void getUrlForUploadPhotoToWall(int groupId) throws ClientProtocolException, IOException // If groupId = 0 then upload to user wall
	{
		String post = "https://api.vk.com/method/photos.getWallUploadServer?" +
		        (groupId != 0 ? "group_id=" + Math.abs(groupId) : "") +
		        "&v=" + version +
		        "&access_token=" + access_token;
		sendRequest(post);
		//parse response
	}
}