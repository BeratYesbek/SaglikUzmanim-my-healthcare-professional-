package com.example.salikuzmanim.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterShowMessages;
import com.example.salikuzmanim.Concrete.Constants;
import com.example.salikuzmanim.Concrete.Chat;
import com.example.salikuzmanim.DataBaseManager.FireBaseChatDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Network.ApiClient;
import com.example.salikuzmanim.Network.IApiService;
import com.example.salikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private TextView textView_name;
    private ImageView imageView_profile;
    private EditText editText_message_activity;
    private RecyclerView recyclerView_message_activity;
    private boolean control_animation = false;

    private String receiverName;
    private String receiverImage;
    private String receiverID;
    private String receiverToken;


    private Toolbar toolbar;


    private ArrayList<Chat> chats;
    private AdapterShowMessages adapterShowMessages;

    private Button button_goToBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        chats = new ArrayList<>();

        textView_name = findViewById(R.id.textView_message_activity_name);
        imageView_profile = findViewById(R.id.imageView_message_activity);
        editText_message_activity = findViewById(R.id.editText_send_message);
        recyclerView_message_activity = findViewById(R.id.recyclerView_message_activity);

        button_goToBack = findViewById(R.id.btn_message_activity_goToBck);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        try {
            receiverName = intent.getStringExtra("receiverName");
            receiverImage = intent.getStringExtra("receiverImage");
            receiverID = intent.getStringExtra("receiverID");
            receiverToken = intent.getStringExtra("receiverToken");
            System.out.println("token : " + receiverToken);
            Uri imageUri = null;
            if (receiverImage != null) {
                imageUri = Uri.parse(receiverImage);
                Picasso.get().load(imageUri).into(imageView_profile);
            } else {
                imageView_profile.setImageResource(R.drawable.ic_profile);
            }

            textView_name.setText(receiverName.toUpperCase());

            recyclerView_message_activity.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setStackFromEnd(true);
            recyclerView_message_activity.setLayoutManager(linearLayoutManager);
            adapterShowMessages = new AdapterShowMessages(chats, imageUri);
            recyclerView_message_activity.setAdapter(adapterShowMessages);
            recyclerView_message_activity.refreshDrawableState();
            recyclerView_message_activity.setItemAnimator(new DefaultItemAnimator());
            layoutAnimation(recyclerView_message_activity);
            readMessages();
        } catch (Exception e) {

        }

        button_goToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    private void layoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation_bottom);
        recyclerView.setLayoutAnimation(animationController);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            FireBaseChatDal fireBaseChatDal = new FireBaseChatDal();
            fireBaseChatDal.seenMessages(receiverID);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }


    public void readMessages() {
        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        FireBaseChatDal fireBaseChatDal = new FireBaseChatDal();
        fireBaseChatDal.getMessage(new Chat(senderID, receiverID), new IGetDataListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    chats.clear();
                    chats.addAll((Collection<? extends Chat>) object);
                    if (control_animation == false) {
                        control_animation = true;
                        adapterShowMessages.notifyDataSetChanged();
                        recyclerView_message_activity.scheduleLayoutAnimation();
                        adapterShowMessages.notifyItemInserted(chats.size());
                        recyclerView_message_activity.scrollToPosition(adapterShowMessages.getItemCount() - 1);
                        onResume();
                    } else {
                        adapterShowMessages.notifyDataSetChanged();
                        adapterShowMessages.notifyItemInserted(chats.size());
                        recyclerView_message_activity.scrollToPosition(adapterShowMessages.getItemCount() - 1);
                        onResume();
                    }


                } catch (Exception e) {

                }


            }

            @Override
            public void onFailed(Object object) {

            }
        });


    }

    public void sendMessage(View view) {
        try {
            String message = editText_message_activity.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            if (!message.isEmpty()) {
                String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                UUID uuid = UUID.randomUUID();
                String messageID = uuid.toString();
                FireBaseChatDal fireBaseChatDal = new FireBaseChatDal();
                editText_message_activity.getText().clear();
                fireBaseChatDal.insertMessage(new Chat(message, messageID, receiverID, senderID, formatter.format(date), false));
                setJsonData(message);
                readMessages();
                chats.clear();
            } else {

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void setJsonData(String message)  {
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

            System.out.println(body.toString());
            sendNotification(body.toString());


        } catch (Exception exception) {

            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }

    }


    public void sendNotification(String remoteMessageBody) {
        ApiClient.getClient().create(IApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "gönderiliyor...", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });


        /*
        FireBaseTokensDal fireBaseTokensDal = new FireBaseTokensDal();
        fireBaseTokensDal.getTokens(new Token(null, reciverID), new IGetDataListener() {
            @Override
            public void onSuccess(Object object) {
                Notification notification = (Notification) object;
                notification.setMessage(message);
                notification.setUserName(name);
                NotificationManager notificationManager = new NotificationManager();
                notificationManager.sendMessageNotification(notification);

            }

            @Override
            public void onFailed(Object object) {

            }
        });
        */
    }


}
