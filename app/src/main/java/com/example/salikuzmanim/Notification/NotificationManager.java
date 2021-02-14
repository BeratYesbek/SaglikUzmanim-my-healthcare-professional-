package com.example.salikuzmanim.Notification;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationManager {

    public void sendMessageNotification(Notification notification) {
        try {
            OneSignal.postNotification(new JSONObject("{'headings' : {'en':'"+ notification.getUserName().toUpperCase() +"'}, 'contents': {'en':'"+notification.getMessage()+"'}, 'include_player_ids': ['" + notification.getToken() + "']}"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
