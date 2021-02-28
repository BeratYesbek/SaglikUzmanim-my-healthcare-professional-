package com.saglikuzmanimm.saglikuzmanim.Activity.meetingActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Constants.Constants;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.Network.ApiClient;
import com.saglikuzmanimm.saglikuzmanim.Network.IApiService;
import com.saglikuzmanimm.saglikuzmanim.JitsiMeet.PreferenceManager;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInvitationActivity extends AppCompatActivity {
    private TextView _textView_name;

    private TextView _textView_checkCameraDevice;
    private TextView _textView_checkMicrophoneDevice;
    private TextView _textView_checkConnection;
    private TextView _textView_last_status;

    private ProgressBar _progressBar_cameraDevice;
    private ProgressBar _progressBar_MicrophoneDevice;
    private ProgressBar _progressBar_connection;
    private ProgressBar _progressBar_lastStatus;

    private ImageView _imageView_checkCamera;
    private ImageView _imageView_checkMicrophone;
    private ImageView _imageView_checkConnection;

    private ImageView _imageView_profile;
    private Button _btn_stop_invitation;
    private PreferenceManager preferenceManager;
    private String inviterToken = null;

    private String meetingRoom = null;
    private String appointmentDate;

    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);
        Intent intent = getIntent();
        preferenceManager = new PreferenceManager(getApplicationContext());


        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String image_uri = intent.getStringExtra("image_uri");
        appointmentDate = intent.getStringExtra("appointmentDate");
        token = intent.getStringExtra("token");
        String userID = intent.getStringExtra("userID");
        String email = intent.getStringExtra("email");

        _textView_name = findViewById(R.id.textView_meetingOut_name);
        _textView_checkCameraDevice = findViewById(R.id.textView_checkCameraDevice_Info_outGoing);
        _textView_checkMicrophoneDevice = findViewById(R.id.textView_checkMicrophoneDevice_Info_outGoing);
        _textView_checkConnection = findViewById(R.id.textView_checkConnection_Info_outGoing);
        _textView_last_status = findViewById(R.id.textView_last_status_info_outGoing);

        _progressBar_cameraDevice = findViewById(R.id.progressBar_check_camera_device_outGoing);
        _progressBar_MicrophoneDevice = findViewById(R.id.progressBar_checkMicrophoneDevice_outGoing);
        _progressBar_connection = findViewById(R.id.progressBar_checkConnection_outGoing);
        _progressBar_lastStatus = findViewById(R.id.progressBar_lastStatus_outGoing);


        _imageView_checkCamera = findViewById(R.id.imageView_checkCamera);
        _imageView_checkMicrophone = findViewById(R.id.imageView_checkMicrophone);
        _imageView_checkConnection = findViewById(R.id.imageView_checkConnection);

        _imageView_checkCamera.setVisibility(View.INVISIBLE);
        _imageView_checkMicrophone.setVisibility(View.INVISIBLE);
        _imageView_checkConnection.setVisibility(View.INVISIBLE);

        _textView_checkCameraDevice.setVisibility(View.INVISIBLE);
        _textView_checkMicrophoneDevice.setVisibility(View.INVISIBLE);
        _textView_checkConnection.setVisibility(View.INVISIBLE);
        _textView_last_status.setVisibility(View.INVISIBLE);


        PackageManager pm = getApplicationContext().getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            _textView_checkCameraDevice.setVisibility(View.VISIBLE);
            _progressBar_cameraDevice.setVisibility(View.INVISIBLE);
            _imageView_checkCamera.setVisibility(View.VISIBLE);
        } else {
            _textView_checkCameraDevice.setVisibility(View.VISIBLE);
            _progressBar_cameraDevice.setVisibility(View.VISIBLE);
        }
        Boolean check_microphone = true;//getMicrophoneAvailable(getApplicationContext());
        if (check_microphone == true) {
            _textView_checkMicrophoneDevice.setVisibility(View.VISIBLE);
            _progressBar_MicrophoneDevice.setVisibility(View.INVISIBLE);
            _imageView_checkMicrophone.setVisibility(View.VISIBLE);
        } else {
            _textView_checkMicrophoneDevice.setVisibility(View.VISIBLE);
            _progressBar_MicrophoneDevice.setVisibility(View.VISIBLE);

        }
        Boolean check_connection = isNetworkConnected();
        if (check_connection == true) {
            _textView_checkConnection.setVisibility(View.VISIBLE);
            _progressBar_connection.setVisibility(View.INVISIBLE);
            _textView_last_status.setVisibility(View.VISIBLE);
            _progressBar_lastStatus.setVisibility(View.VISIBLE);
            _imageView_checkConnection.setVisibility(View.VISIBLE);
        } else {
            _textView_checkConnection.setVisibility(View.VISIBLE);
            _progressBar_connection.setVisibility(View.VISIBLE);
        }


        _imageView_profile = findViewById(R.id.ImageView_meetingOut_user_profile);
        _btn_stop_invitation = findViewById(R.id.image_stop_invitation);

        _textView_name.setText((firstName + " " + lastName).toUpperCase());
        if (image_uri != null) {
            Uri uri = Uri.parse(image_uri);
            Picasso.get().load(uri).into(_imageView_profile);
        }
        _btn_stop_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token != null) {
                    cancelInvitation(token);
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    inviterToken = task.getResult().getToken();
                }
            }
        });
        getUserData();


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean getMicrophoneAvailable(Context context) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(new File(context.getCacheDir(), "MediaUtil#micAvailTestFile").getAbsolutePath());
        boolean available = true;
        try {
            recorder.prepare();
            recorder.start();

        } catch (Exception exception) {
            available = false;
        }
        recorder.release();
        return available;
    }


    public void getUserData() {


        UserManager userManager = new UserManager(new FireBaseUserDal());
        userManager.getData(null, new IGetUserListener() {
            @Override
            public void onSuccess(ArrayList<User> userArrayList) {
                User user = (User) userArrayList.get(0);

                if (token != null) {
                    initiateMeeting("video", token, user);
                }
            }

            @Override
            public void onFailed(Exception exception) {

            }
        });
    }

    private void initiateMeeting(String meetingType, String receiverToken, User user) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();


            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MGS_INVITATION);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.put(Constants.KEY_FIRST_NAME, user.get_firstName());
            data.put(Constants.KEY_LAST_NAME, user.get_lastName());
            data.put(Constants.KEY_EMAIL, user.get_email());
            data.put(Constants.KEY_USER_PROFILE_IMAGE, user.get_profileImage().toString());
            data.put(Constants.KEY_APPOINTMENT_DATE, appointmentDate);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

            meetingRoom = user.get_ID() + "_" + UUID.randomUUID().toString().substring(0, 5);

            data.put(Constants.REMOTE_MSG_MEETING_ROOM, meetingRoom);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MGS_INVITATION);


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
                    if (type.equals(Constants.REMOTE_MGS_INVITATION)) {
                        Toast.makeText(OutgoingInvitationActivity.this, "Aranıyor...", Toast.LENGTH_LONG).show();
                    } else if (type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)) {

                        finish();
                    }
                } else {

                    Toast.makeText(OutgoingInvitationActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                Toast.makeText(OutgoingInvitationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void cancelInvitation(String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, Constants.REMOTE_MSG_INVITATION_CANCELLED);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION_RESPONSE);

        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                    try {
                        URL serverURL = new URL("https://meet.jit.si");
                        JitsiMeetConferenceOptions conferenceOptions =
                                new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setWelcomePageEnabled(false)
                                        .setRoom(meetingRoom)
                                        .build();

                        JitsiMeetActivity.launch(OutgoingInvitationActivity.this, conferenceOptions);

                        finish();

                    } catch (Exception exception) {
                        Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else if (type.equals(Constants.REMOTE_MSG_INVITATION_REJECTED)) {
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_LONG).show();
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