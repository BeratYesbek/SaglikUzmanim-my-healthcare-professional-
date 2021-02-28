package com.saglikuzmanimm.saglikuzmanim.Notification;

import com.saglikuzmanimm.saglikuzmanim.Constants.Constants;
import com.saglikuzmanimm.saglikuzmanim.Network.ApiClient;
import com.saglikuzmanimm.saglikuzmanim.Network.IApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSendNotification {

    public static void sendMessageNotification(String receiverToken,String receiverID,String receiverName,String message) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_CHAT);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, "chat");
            data.put(Constants.KEY_USER_NAME, receiverName);
            data.put(Constants.KEY_MESSAGE_BODY,message);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, receiverToken);
            data.put(Constants.KEY_USER_ID,receiverID);


            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendNotification(body.toString());


        } catch (Exception exception) {

        }
    }

    private static void sendNotification(String remoteMessageBody) {

        ApiClient.getClient().create(IApiService.class).sendRemoteMessage(
                RemoteMessageHeaders.getRemoteMessageHeaders(),
                remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
