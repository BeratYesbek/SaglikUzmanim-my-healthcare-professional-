package com.saglikuzmanimm.saglikuzmanim.Notification;

import com.saglikuzmanimm.saglikuzmanim.Constants.Constants;
import com.saglikuzmanimm.saglikuzmanim.Constants.Messages;
import com.saglikuzmanimm.saglikuzmanim.Network.ApiClient;
import com.saglikuzmanimm.saglikuzmanim.Network.IApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentSendNotification {
    // about appointment notifications is sent by this class

    public static void sendAppointmentRequestNotification(String receiverToken, String receiverID) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();


            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_APPOINTMENT);
            data.put(Constants.KEY_TITLE, Messages.APPOINTMENT_MESSAGE_TITLE);
            data.put(Constants.KEY_MESSAGE_BODY, Messages.APPOINTMENT_MESSAGES_DEMAND);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, receiverToken);
            data.put(Constants.KEY_USER_ID, receiverID);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);
            System.out.println(body.toString());
            sendNotification(body.toString());

        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    public static void sendAppointmentApprovedNotification(String receiverToken, String receiverID) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_APPOINTMENT);
            data.put(Constants.KEY_TITLE, Messages.APPOINTMENT_MESSAGE_TITLE);
            data.put(Constants.KEY_MESSAGE_BODY, Messages.APPOINTMENT_MESSAGES_APPROVED_BY_EXPERT);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, receiverToken);
            data.put(Constants.KEY_USER_ID, receiverID);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendNotification(body.toString());

        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    public static void sendAppointmentAbortNotification(String receiverToken, String receiverID, String byWho) {

        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_APPOINTMENT);
            data.put(Constants.KEY_TITLE, Messages.APPOINTMENT_MESSAGE_TITLE);
            if (byWho.equals("user")) {
                data.put(Constants.KEY_MESSAGE_BODY, Messages.APPOINTMENT_MESSAGES_ABORT_BY_USER);
            } else if (byWho.equals("expert")) {
                data.put(Constants.KEY_MESSAGE_BODY, Messages.APPOINTMENT_MESSAGES_ABORT_BY_EXPERT);
            }

            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, receiverToken);
            data.put(Constants.KEY_USER_ID, receiverID);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendNotification(body.toString());

        } catch (Exception exception) {
            System.out.println(exception.toString());
        }


    }

    public static void sendAppointmentRejectNotification(String receiverToken, String receiverID) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_APPOINTMENT);
            data.put(Constants.KEY_TITLE, Messages.APPOINTMENT_MESSAGE_TITLE);
            data.put(Constants.KEY_MESSAGE_BODY, Messages.APPOINTMENT_MESSAGES_REJECT_BY_EXPERT);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, receiverToken);
            data.put(Constants.KEY_USER_ID, receiverID);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendNotification(body.toString());

        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    public static void sendAppointmentPayNotification(String receiverToken,String receiverID) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_APPOINTMENT);
            data.put(Constants.KEY_TITLE, Messages.APPOINTMENT_MESSAGE_TITLE);
            data.put(Constants.KEY_MESSAGE_BODY, Messages.APPOINTMENT_MESSAGES_PAY_BY_USER);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, receiverToken);
            data.put(Constants.KEY_USER_ID, receiverID);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendNotification(body.toString());

        } catch (Exception exception) {
            System.out.println(exception.toString());
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
