package com.viktor235.vkpublisher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.viktor235.vkpublisher.connectivity.VKClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.connectivity.Request;
import com.viktor235.vkpublisher.connectivity.VKResponse;
import com.viktor235.vkpublisher.connectivity.VKResponseUtils;

public class VKapi {
    // private CloseableHttpClient httpClient;
    private VKClient vkClient;
    // private BasicHttpContext localContext;
    // private BasicCookieStore cookieStore;

    private String client_id = "4786761";
    private String scope = "offline,messages,status,wall,photos";
    private String redirect_uri = "http://oauth.vk.com/blank.html";
    private String display = "popup";
    private String response_type = "token";
    private String version = "5.29";
    private AccessToken access_token = null;

    public VKapi() {
        this.vkClient = new VKClient(new DefaultHttpClient());
    }

    public String getConnectionUrl() {
        return "http://oauth.vk.com/authorize?" + "client_id=" + client_id
                + "&scope=" + scope + "&redirect_uri=" + redirect_uri
                + "&display=" + display + "&response_type=" + response_type;
    }

    public String parseAccessToken(String urlWithAT) {
        // System.out.println(urlWithAT);
        if (urlWithAT == null)
            return null;
        String accessToken = urlWithAT, startKey = "#access_token=", endKey = "&";

        int atStart = accessToken.indexOf(startKey) + startKey.length();
        if (atStart < startKey.length())
            return null;

        accessToken = accessToken.substring(atStart);

        int atEnd = accessToken.indexOf(endKey);
        if (atEnd < 0)
            return accessToken;

        // System.out.println(accessToken.substring(0, atEnd));
        return accessToken.substring(0, atEnd);
    }

    public boolean validateAccessToken(String accessToken) {
        Request request = new Request("https://api.vk.com/method/users.search");
        request.addParam("q", "Durov");
        request.addParam("count", "1");
        request.addParam("v", version);
        request.addParam("access_token", accessToken);
        VKResponse response = this.vkClient.execute(request);
        return !response.isError();
    }

    public void setAccessToken(AccessToken accessToken) {
        access_token = accessToken;
    }

    public int getUserId(String userDomain) {
        Request request = new Request("https://api.vk.com/method/users.get");
        request.addParam("user_ids", userDomain);
        request.addParam("v", version);
        VKResponse vkResponse = this.vkClient.execute(request);

        if (!vkResponse.isError())
            try {
                return VKResponseUtils.getFirstUserId(vkResponse);
            } catch (IllegalArgumentException e) {
                System.out.println("Error in user ID parsing: "
                        + e.getMessage());
                return 0;
            }
        return 0;
    }

	/*
	 * public int getGroupId(String groupDomain) { String post =
	 * "https://api.vk.com/method/users.get?" + "user_ids=" + userDomain + "&v="
	 * + version + "&access_token=" + access_token; String jsonResponse =
	 * sendPost(post);
	 * 
	 * VKResponse vkResponse = VKResponse.parseReponse(jsonResponse); if
	 * (vkResponse.isResponseIsArrayList()) return vkResponse.getFirstUserId();
	 * 
	 * System.out.println("Error in ID parsing"); return 0; }
	 */

    public String getNewMessage() {
        Request request = new Request("https://api.vk.com/method/messages.get");
        request.addParam("out", "0");
        request.addParam("v", version);
        VKResponse vkResponse = this.vkClient.execute(request);

        return vkResponse.toString();
    }

    /*public static String encodeToURL(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "(Unsupported+Encoding)";
        }
    }*/

    public void sendMessage(int userId, String message) {
        Request request = new Request("https://api.vk.com/method/messages.send");
        request.addParam("ser_id", userId);
        request.addParam("message", message);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());

        this.vkClient.execute(request);
    }

    public void sendMessage(String userDomain, String message) {
        Request request = new Request("https://api.vk.com/method/messages.send");
        request.addParam("domain", userDomain);
        request.addParam("message", message);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.vkClient.execute(request);
    }

    public void setStatus(String status) {
        Request request = new Request("https://api.vk.com/method/status.set");
        request.addParam("text", status);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.vkClient.execute(request);
    }

    // Post to User wall or Public wall. if owner_id == null then post to SELF
    // user wall
    public void postToWall(Integer owner_id, String message, String attachments,
                boolean fromGroupName) {
        Request request = new Request("https://api.vk.com/method/wall.post");
        if (owner_id != null)
            request.addParam("owner_id", owner_id);
        request.addParam("from_group", fromGroupName ? "1" : "0");
        request.addParam("message", message);
        if (attachments != null)
            request.addParam("attachments", attachments);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.vkClient.execute(request);
    }

	/* Photo */

    // If groupId = 0 then upload to user wall
    public String getWallUploadServer(int groupId) {
        Request request = new Request("https://api.vk.com/method/photos.getWallUploadServer");
        if (groupId != 0)
            request.addParam("group_id", Math.abs(groupId));
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        VKResponse vkResponse = this.vkClient.execute(request);

        if (!vkResponse.isError())
            try {
                return VKResponseUtils.getUploadURL(vkResponse);
            } catch (IllegalArgumentException e) {
                System.out.println("Error in response parsing: "
                        + e.getMessage());
                return null;
            }
        return null;
    }

    public void uploadPhotoToWall(int groupID, byte[] photoByteArray) {
        System.out.println("Getting upload server url");
        String uploadServerURL = getWallUploadServer(groupID);
        System.out.println(uploadServerURL);
        HttpPost httppost = new HttpPost(uploadServerURL);
        MultipartEntity mpEntity = new MultipartEntity();

        /*String param = "p";
        Pattern pat = Pattern.compile("[\\&\\?]hash=([0123456789abcdef]+)");
        Matcher m = pat.matcher(uploadServerURL);
        m.find();
        String hash = m.group(1);
        System.out.println(hash);*/


        ByteArrayBody byteArrayBody = new ByteArrayBody(photoByteArray,
                "1.jpg");
        mpEntity.addPart("photo", byteArrayBody);

        httppost.setEntity(mpEntity);

        System.out.println("Sending photo to server");
        VKResponse vkResponse = vkClient.execute(httppost);

        String server = VKResponseUtils.findString(vkResponse, "server");
        String photo = VKResponseUtils.findString(vkResponse, "photo");
        String hash = VKResponseUtils.findString(vkResponse, "hash");
        System.out.println(server + "___" + photo + "___" + hash);

		/* Save photo */

        Request vkRequest = new Request("https://api.vk.com/method/photos.saveWallPhoto");
        vkRequest.addParam("group_id", groupID);
        vkRequest.addParam("server", server);
        vkRequest.addParam("photo", photo);
        vkRequest.addParam("hash", hash);
        vkRequest.addParam("v", version);
        vkRequest.addParam("access_token", access_token.toString());

		/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("access_token", access_token
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("photo", photo));

		nameValuePairs.add(new BasicNameValuePair("server", server));
		nameValuePairs.add(new BasicNameValuePair("hash", hash));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(IOUtils.toString(httpPost.getEntity()
					.getContent(), "UTF-8"));
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
        VKResponse response = vkClient.execute(vkRequest);
        System.out.println("Saved");

		int media_id = VKResponseUtils.findIntInResponse(response, "id");
		String owner_id = VKResponseUtils.findStringInResponse(response, "owner_id");

        postToWall(groupID, "VK Api test message", "photo" + owner_id + "_" + media_id, false);
    }
}