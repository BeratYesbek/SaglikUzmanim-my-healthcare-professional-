package com.example.salikuzmanim.Activity.meetingActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.salikuzmanim.Concrete.Constants;
import com.example.salikuzmanim.Network.ApiClient;
import com.example.salikuzmanim.Network.IApiService;
import com.example.salikuzmanim.R;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomignInvitationActivity extends AppCompatActivity {
    private TextView _textView_name;
    private TextView _textView_appointmentDate;
    private ImageView _imageView_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomign_invitation);

        try {
            _textView_name = findViewById(R.id.textView_meeting_name);
            _textView_appointmentDate = findViewById(R.id.textView_meeting_appointmentDate_inComing);
            _imageView_profile = findViewById(R.id.imageView_meeting_user_profile);

            String firstName = getIntent().getStringExtra(Constants.KEY_FIRST_NAME);
            String lastName = getIntent().getStringExtra(Constants.KEY_LAST_NAME);
            String image_uri = getIntent().getStringExtra(Constants.KEY_USER_PROFILE_IMAGE);
            String appointmentDate = getIntent().getStringExtra(Constants.KEY_APPOINTMENT_DATE);


            System.out.println(appointmentDate);
            _textView_name.setText((firstName + " " + lastName).toUpperCase());
            _textView_appointmentDate.setText(appointmentDate);
            if (image_uri != null) {
                Uri uri = Uri.parse(image_uri);
                Picasso.get().load(uri).into(_imageView_profile);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }


        Button imageAcceptInvitation = findViewById(R.id.btn_meeting_accept);
        Button imageRejectInvitation = findViewById(R.id.btn_meeting_reject);

        imageAcceptInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse(
                        Constants.REMOTE_MSG_INVITATION_ACCEPTED,
                        getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)
                );
            }
        });
        imageRejectInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse(
                        Constants.REMOTE_MSG_INVITATION_REJECTED,
                        getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)
                );
            }
        });


    }


    public void sendInvitationResponse(String type, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);
            sendRemoteMessage(body.toString(), type);

        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {

        ApiClient.getClient().create(IApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                        try {
                            URL serverURL = new URL("https://meet.jit.si");
                            JitsiMeetConferenceOptions conferenceOptions =
                                    new JitsiMeetConferenceOptions.Builder()
                                            .setServerURL(serverURL)
                                            .setWelcomePageEnabled(false)
                                            .setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM))
                                            .build();

                            JitsiMeetActivity.launch(IncomignInvitationActivity.this, conferenceOptions);
                            finish();
                        } catch (Exception exception) {
                            Toast.makeText(IncomignInvitationActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    } else {
                        Toast.makeText(IncomignInvitationActivity.this, "Invitation Rejected", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {

                    Toast.makeText(IncomignInvitationActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                Toast.makeText(IncomignInvitationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED)) {
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }
}