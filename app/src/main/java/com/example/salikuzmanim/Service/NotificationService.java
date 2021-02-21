package com.example.salikuzmanim.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.salikuzmanim.Activity.MessageActivity;
import com.example.salikuzmanim.Activity.meetingActivity.IncomignInvitationActivity;
import com.example.salikuzmanim.Concrete.Constants;
import com.example.salikuzmanim.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;


public class NotificationService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);
        System.out.println(type + " type   " + "5");
        if (type.equals(Constants.REMOTE_MGS_INVITATION)) {
            if (type.equals(Constants.REMOTE_MGS_INVITATION)) {
                Intent intent = new Intent(getApplicationContext(), IncomignInvitationActivity.class);
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_TYPE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE)
                );
                intent.putExtra(
                        Constants.KEY_FIRST_NAME,
                        remoteMessage.getData().get(Constants.KEY_FIRST_NAME)
                );
                intent.putExtra(
                        Constants.KEY_USER_PROFILE_IMAGE,
                        remoteMessage.getData().get(Constants.KEY_USER_PROFILE_IMAGE)
                );
                intent.putExtra(
                        Constants.KEY_LAST_NAME,
                        remoteMessage.getData().get(Constants.KEY_LAST_NAME)
                );
                intent.putExtra(
                        Constants.KEY_EMAIL,
                        remoteMessage.getData().get(Constants.KEY_EMAIL)
                );
                intent.putExtra(
                        Constants.REMOTE_MSG_INVITER_TOKEN,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_INVITER_TOKEN)
                );
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_ROOM,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM)
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)) {
                Intent intent = new Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE);
                intent.putExtra(
                        Constants.REMOTE_MSG_INVITATION_RESPONSE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_INVITATION_RESPONSE)
                );
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            }
        }
        else if (type.equals(Constants.REMOTE_CHAT)) {
            sendMessageNotification(remoteMessage);
        }
        else if(type.equals(Constants.REMOTE_APPOINTMENT)){
            System.out.println("279");
            sendAppointmentNotification(remoteMessage);

        }
    }


    private void sendAppointmentNotification(RemoteMessage remoteMessage){
        System.out.println(450);
        String user_id = remoteMessage.getData().get(Constants.KEY_USER_ID);
        String title = remoteMessage.getData().get(Constants.KEY_TITLE);
        String body = remoteMessage.getData().get(Constants.KEY_MESSAGE_BODY);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.doctor)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(defaultSound);

        Intent resultIntent = new Intent(getApplicationContext(), MessageActivity.class);
        resultIntent.putExtra("user_id", user_id);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void sendMessageNotification(RemoteMessage remoteMessage) {




        String user = remoteMessage.getData().get(Constants.KEY_USER_ID);
        String userName = remoteMessage.getData().get(Constants.KEY_USER_NAME);
        String message = remoteMessage.getData().get(Constants.KEY_MESSAGE_BODY);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.doctor)
                        .setContentTitle(userName.toUpperCase())
                        .setContentText(message)
                        .setSound(defaultSound);




        Intent resultIntent = new Intent(getApplicationContext(), MessageActivity.class);
        resultIntent.putExtra("user_id", user);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}