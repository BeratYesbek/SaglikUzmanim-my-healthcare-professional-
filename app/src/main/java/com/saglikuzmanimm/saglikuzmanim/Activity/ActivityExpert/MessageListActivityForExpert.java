package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity.MessageActivity;
import com.saglikuzmanimm.saglikuzmanim.Adapter.AdapterMessages.AdapterMessages;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ChatManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.MessageArrayList;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Person;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseChatDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetMessageArrayListListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IRecyclerViewClick;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageListActivityForExpert extends AppCompatActivity implements IRecyclerViewClick {

    private RecyclerView recyclerView;
    private AdapterMessages adapterMessages;
    private ArrayList<Person> person;

    private ProgressBar progressBar;
    private TextView textView;
    private ArrayList<HashMap> hashMapArrayList;
    private boolean animation_control = false;

    private TextView textView_toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_for_expert);

        person = new ArrayList<>();
        hashMapArrayList = new ArrayList<>();


        recyclerView = findViewById(R.id.reyclerView_displayUser_messages2);
        progressBar = findViewById(R.id.progressBar_message_acitivity_for_expert);
        textView = findViewById(R.id.textView_message_fragmentExpert_for_info);
        textView_toolBar = findViewById(R.id.textView_toolbar_activity_expert_show_list_user);
        textView.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterMessages = new AdapterMessages(person, hashMapArrayList, this);
        recyclerView.setAdapter(adapterMessages);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutAnimation(recyclerView);

        getUserChatData();


    }

    private void layoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(animationController);

    }


    public void messageCalculator() {

        HashMap<String, Object> hashMap = hashMapArrayList.get(0);
        int totalIsntSeen = (int) hashMap.get("totalIsntSeen");

        if (totalIsntSeen == 0) {
            textView_toolBar.setText("MESAJLAR");
        } else {
            textView_toolBar.setText("MESAJLAR" + "(" + totalIsntSeen + ")");
        }


    }

    public void remove_adapter_item() {
        for (int index = 0; index < person.size(); index++) {
            adapterMessages.notifyItemRemoved(index);
        }
    }

    public void getUserChatData() {


        ChatManager chatManager = new ChatManager(new FireBaseChatDal());
        chatManager.getMessageForList("user", new IGetMessageArrayListListener() {
            @Override
            public void onSuccess(MessageArrayList messageArrayLists) {

                    remove_adapter_item();
                    person.clear();
                    hashMapArrayList.clear();


                    MessageArrayList _messageArrayList = (MessageArrayList) messageArrayLists;
                    person.addAll(_messageArrayList.getArrayListPerson());
                    hashMapArrayList.addAll(_messageArrayList.getHashMapArrayList());


                    if(hashMapArrayList.size() != 0){
                        messageCalculator();
                    }else{
                        textView_toolBar.setText("MESAJLAR");
                    }


                    adapterMessages.notifyDataSetChanged();

                    if (animation_control == false) {
                        recyclerView.scheduleLayoutAnimation();
                        animation_control = true;
                    }
                    progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailed(Exception exception) {
                textView.setText("Mesajınız bulunmamaktadır");
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }


    @Override
    public void onItemClick(int position) {

        // that's section if you are click on recyclerView item, you can go message activity
        Intent intentToMessageActivity = new Intent(getApplicationContext(), MessageActivity.class);
        Person _person = person.get(position);


        String receiverName = _person.get_firstName() + " " + _person.get_lastName();
        String receiverID = _person.get_ID();
        Uri receiverImage = _person.get_profileImage();

        // in this section, data between activities is transferred
        intentToMessageActivity.putExtra("receiverID", receiverID);
        intentToMessageActivity.putExtra("receiverName", receiverName);
        if (receiverImage != null) {
            intentToMessageActivity.putExtra("receiverImage", receiverImage.toString());
        }

        intentToMessageActivity.putExtra("receiverToken", _person.get_token());


        startActivity(intentToMessageActivity);


    }

    @Override
    public void onLongItemClick(int position) {

    }
}