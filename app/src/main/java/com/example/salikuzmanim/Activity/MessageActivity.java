package com.example.salikuzmanim.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterShowMessages;
import com.example.salikuzmanim.DataBaseManager.FireBaseChatDal;
import com.example.salikuzmanim.DataBaseManager.FireBaseTokensDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Notification.Notification;
import com.example.salikuzmanim.Notification.NotificationManager;
import com.example.salikuzmanim.Concrete.Message;
import com.example.salikuzmanim.Concrete.Token;
import com.example.salikuzmanim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class MessageActivity extends AppCompatActivity {
    private TextView textView_name;
    private ImageView imageView_profile;
    private EditText editText_message_activity;
    private RecyclerView recyclerView_message_activity;
    private boolean control_animation = false;

    private String reciverName;
    private String reciverImage;
    private String reciverID;
    private Toolbar toolbar;


    private ArrayList<Message> messages;
    private AdapterShowMessages adapterShowMessages;

    IGetDataListener iGetDataListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messages = new ArrayList<>();

        textView_name = findViewById(R.id.textView_message_activity_name);
        imageView_profile = findViewById(R.id.imageView_message_activity);
        editText_message_activity = findViewById(R.id.editText_send_message);
        recyclerView_message_activity = findViewById(R.id.recyclerView_message_activity);
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        try {
            reciverName = intent.getStringExtra("receiverName");
            reciverImage = intent.getStringExtra("receiverImage");
            reciverID = intent.getStringExtra("receiverID");
            Uri imageUri = null;
            if (reciverImage != null) {
                imageUri = Uri.parse(reciverImage);
                Picasso.get().load(imageUri).into(imageView_profile);
            } else {
                imageView_profile.setImageResource(R.drawable.ic_profile);
            }

            textView_name.setText(reciverName.toUpperCase());

            recyclerView_message_activity.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setStackFromEnd(true);
            recyclerView_message_activity.setLayoutManager(linearLayoutManager);
            adapterShowMessages = new AdapterShowMessages(messages, imageUri);
            recyclerView_message_activity.setAdapter(adapterShowMessages);
            recyclerView_message_activity.refreshDrawableState();
            recyclerView_message_activity.setItemAnimator(new DefaultItemAnimator());
            layoutAnimation(recyclerView_message_activity);
            readMessages();
        } catch (Exception e) {
            System.out.println(e.toString());
        }


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
            fireBaseChatDal.seenMessages(reciverID);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }


    public void readMessages() {
        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        FireBaseChatDal fireBaseChatDal = new FireBaseChatDal();
        fireBaseChatDal.getMessage(new Message(senderID, reciverID), new IGetDataListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    messages.clear();
                    messages.addAll((Collection<? extends Message>) object);
                    if (control_animation == false) {
                        control_animation = true;
                        adapterShowMessages.notifyDataSetChanged();
                        recyclerView_message_activity.scheduleLayoutAnimation();
                        adapterShowMessages.notifyItemInserted(messages.size());
                        recyclerView_message_activity.scrollToPosition(adapterShowMessages.getItemCount() - 1);
                        onResume();
                    } else {
                        adapterShowMessages.notifyDataSetChanged();
                        adapterShowMessages.notifyItemInserted(messages.size());
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
                fireBaseChatDal.insertMessage(new Message(message, messageID, reciverID, senderID, formatter.format(date), false));
                readMessages();
                sendNotification(message, FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
                messages.clear();
            } else {

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void sendNotification(String message, String name) {
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
    }


}
