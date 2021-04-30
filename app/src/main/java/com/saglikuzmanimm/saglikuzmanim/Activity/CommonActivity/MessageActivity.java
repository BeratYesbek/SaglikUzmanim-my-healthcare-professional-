package com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.saglikuzmanimm.saglikuzmanim.Adapter.AdapterMessages.AdapterShowMessages;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ChatManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Chat;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.ChatDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetChatListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.Notification.ChatSendNotification;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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


            receiverName = intent.getStringExtra("receiverName");
            receiverImage = intent.getStringExtra("receiverImage");
            receiverID = intent.getStringExtra("receiverID");
            receiverToken = intent.getStringExtra("receiverToken");


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

        ChatManager chatManager = new ChatManager(new ChatDal());
        chatManager.seenMessages(receiverID);

    }


    public void readMessages() {
        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();




        ChatManager chatManager = new ChatManager(new ChatDal());
        chatManager.getData(new Chat(senderID,receiverID), new IGetChatListener() {
            @Override
            public void onSuccess(ArrayList<Chat> chatArrayList) {
                try {
                    chats.clear();
                    chats.addAll(chatArrayList);
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
            public void onFailed(Exception exception) {

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
                editText_message_activity.getText().clear();
                sendNotification(message);
                ChatManager chatManager = new ChatManager(new ChatDal());
                chatManager.addData(new Chat(message, messageID, receiverID, senderID, formatter.format(date), false), new IResult() {
                    @Override
                    public void onSuccess() {

                        readMessages();
                        chats.clear();
                    }
                    @Override
                    public void onFailed(Exception exception) {

                    }
                });

            } else {

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    private void sendNotification(String message)  {

        ChatSendNotification.sendMessageNotification(receiverToken,receiverID,receiverName,message);
    }




}
