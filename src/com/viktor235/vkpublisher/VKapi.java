package com.viktor235.vkpublisher;

import com.viktor235.vkpublisher.connectivity.HttpRequestHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.viktor235.vkpublisher.accesstoken.AccessToken;
import com.viktor235.vkpublisher.connectivity.Request;
import com.viktor235.vkpublisher.connectivity.VKResponse;
import com.viktor235.vkpublisher.connectivity.VKResponseUtils;

public class VKapi {
    private HttpRequestHandler requestHandler;

    private String version;
    private AccessToken access_token = null;

    private final static String METHOD_PATH = "https://api.vk.com/method/";

    public VKapi(String version) {
        this.version = version;
        this.requestHandler = new HttpRequestHandler(new DefaultHttpClient());
    }

    public static String getConnectionUrl(String client_id, String scope, String redirect_uri,
                                   String display, String response_type, String version) {
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
        Request request = new Request(METHOD_PATH + "users.search");
        request.addParam("q", "Durov");
        request.addParam("count", "1");
        request.addParam("v", version);
        request.addParam("access_token", accessToken);
        VKResponse response = this.requestHandler.execute(request);
        return !response.isError();
    }

    public void setAccessToken(AccessToken accessToken) {
        access_token = accessToken;
    }

    public int users_get(String userDomain) {
        Request request = new Request(METHOD_PATH + "users.get");
        request.addParam("user_ids", userDomain);
        request.addParam("v", version);
        VKResponse vkResponse = this.requestHandler.execute(request);

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

    public String messages_get() {
        Request request = new Request(METHOD_PATH + "messages.get");
        request.addParam("out", "0");
        request.addParam("v", version);
        VKResponse vkResponse = this.requestHandler.execute(request);

        return vkResponse.toString();
    }

    public void messages_send(int userId, String message) {
        Request request = new Request(METHOD_PATH + "messages.send");
        request.addParam("user_id", userId);
        request.addParam("message", message);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.requestHandler.execute(request);
    }

    public void messages_send(String userDomain, String message) {
        Request request = new Request(METHOD_PATH + "messages.send");
        request.addParam("domain", userDomain);
        request.addParam("message", message);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.requestHandler.execute(request);
    }

    public void status_set(String status) {
        Request request = new Request(METHOD_PATH + "status.set");
        request.addParam("text", status);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.requestHandler.execute(request);
    }

    // Post to User wall or Public wall. if owner_id == null then post to SELF
    // user wall
    public void wall_post(Integer owner_id, String message, String attachments,
                boolean fromGroupName) {
        Request request = new Request(METHOD_PATH + "wall.post");
        if (owner_id != null)
            request.addParam("owner_id", owner_id);
        request.addParam("from_group", fromGroupName ? "1" : "0");
        request.addParam("message", message);
        if (attachments != null)
            request.addParam("attachments", attachments);
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        this.requestHandler.execute(request);
    }

    // If groupId = 0 then upload to user wall
    public String photos_getWallUploadServer(int userOrGroupID) {
        Request request = new Request(METHOD_PATH + "photos.getWallUploadServer");
        if (userOrGroupID != 0)
            request.addParam("group_id", Math.abs(userOrGroupID));
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        VKResponse vkResponse = this.requestHandler.execute(request);

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

    public VKResponse uploadFileToServer(String uploadServerURL, String requestParam, String fileName, byte[] photoByteArray) {
        HttpPost httppost = new HttpPost(uploadServerURL);
        MultipartEntity mpEntity = new MultipartEntity();

        ByteArrayBody byteArrayBody = new ByteArrayBody(photoByteArray, fileName);
        mpEntity.addPart(requestParam, byteArrayBody);

        httppost.setEntity(mpEntity);

        return requestHandler.execute(httppost);
    }

    public VKResponse photos_saveWallPhoto(int userOrGroupID, String server, String photo, String hash) {
        userOrGroupID = Math.abs(userOrGroupID);

        Request vkRequest = new Request(METHOD_PATH + "photos.saveWallPhoto");
        vkRequest.addParam("group_id", userOrGroupID);
        vkRequest.addParam("server", server);
        vkRequest.addParam("photo", photo);
        vkRequest.addParam("hash", hash);
        vkRequest.addParam("v", version);
        vkRequest.addParam("access_token", access_token.toString());
        return requestHandler.execute(vkRequest);
    }

    /*public void uploadPhotoToWall(int userOrGroupID, byte[] photoByteArray) {
        System.out.println("Getting upload server url");
        String uploadServerURL = getWallUploadServer(userOrGroupID);
        System.out.println(uploadServerURL);
        HttpPost httppost = new HttpPost(uploadServerURL);
        MultipartEntity mpEntity = new MultipartEntity();

        ByteArrayBody byteArrayBody = new ByteArrayBody(photoByteArray,
                "1.jpg");
        mpEntity.addPart("photo", byteArrayBody);

        httppost.setEntity(mpEntity);

        System.out.println("Sending photo to server");
        VKResponse vkResponse = requestHandler.execute(httppost);

        String server = VKResponseUtils.findString(vkResponse, "server");
        String photo = VKResponseUtils.findString(vkResponse, "photo");
        String hash = VKResponseUtils.findString(vkResponse, "hash");
        System.out.println(server + "___" + photo + "___" + hash);

		// Save photo

        Request vkRequest = new Request(METHOD_PATH + "photos.saveWallPhoto");
        vkRequest.addParam("group_id", userOrGroupID);
        vkRequest.addParam("server", server);
        vkRequest.addParam("photo", photo);
        vkRequest.addParam("hash", hash);
        vkRequest.addParam("v", version);
        vkRequest.addParam("access_token", access_token.toString());
        VKResponse response = requestHandler.execute(vkRequest);
        System.out.println("Saved");

		int media_id = VKResponseUtils.findIntInResponse(response, "id");
		String owner_id = VKResponseUtils.findStringInResponse(response, "owner_id");

        postToWall(userOrGroupID, "VK Api test message", "photo" + owner_id + "_" + media_id, false);
    }*/

    /* Docs */

    // Set null for upload documents to current user
    public String docs_getUploadServer(Integer groupID) {
        Request request = new Request(METHOD_PATH + "docs.getUploadServer");
        if (groupID != null)
            request.addParam("group_id", Math.abs(groupID));
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        VKResponse vkResponse = this.requestHandler.execute(request);

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

    // Set null for upload documents to current user
    public String docs_getWallUploadServer(Integer groupID) {
        Request request = new Request(METHOD_PATH + "docs.getWallUploadServer");
        if (groupID != null)
            request.addParam("group_id", Math.abs(groupID));
        request.addParam("v", version);
        request.addParam("access_token", access_token.toString());
        VKResponse vkResponse = this.requestHandler.execute(request);

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

    public VKResponse docs_save(String file, String title, String tags) {
        Request vkRequest = new Request(METHOD_PATH + "docs.save");
        vkRequest.addParam("file", file);
        vkRequest.addParam("title", title);
        vkRequest.addParam("tags", tags);
        vkRequest.addParam("v", version);
        vkRequest.addParam("access_token", access_token.toString());
        return requestHandler.execute(vkRequest);
    }
}