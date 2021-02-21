package com.example.salikuzmanim.ui.main;

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

import com.example.salikuzmanim.Activity.MessageActivity;
import com.example.salikuzmanim.Adapter.AdapterMessages;
import com.example.salikuzmanim.DataBaseManager.FireBaseChatDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Interfaces.IRecyclerViewClick;
import com.example.salikuzmanim.Concrete.MessageArrayList;
import com.example.salikuzmanim.Concrete.Person;
import com.example.salikuzmanim.R;

import java.util.ArrayList;
import java.util.HashMap;

public class messageActivityForExpert extends AppCompatActivity implements IRecyclerViewClick {

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


    public void messageCalculater() {
        int total = 0;
        for (int i = 0; i < hashMapArrayList.size(); i++) {
            HashMap<String, Object> hashMap = hashMapArrayList.get(i);
            int totalIsntSeen = (int) hashMap.get("totalIsntSeen");
            total = total + totalIsntSeen;

        }
        textView_toolBar.setText("MESAJLAR" + "(" + total + ")");

    }

    public void remove_adapter_item() {
        for (int index = 0; index < person.size(); index++) {
            adapterMessages.notifyItemRemoved(index);
        }
    }

    public void getUserChatData() {
        try {
            FireBaseChatDal fireBaseChatDal = new FireBaseChatDal();
            fireBaseChatDal.getMessageForList("user", new IGetDataListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        remove_adapter_item();
                        person.clear();
                        hashMapArrayList.clear();


                        MessageArrayList messageArrayList = (MessageArrayList) object;
                        person.addAll(messageArrayList.getArrayListPerson());

                        hashMapArrayList.addAll(messageArrayList.getHashMapArrayList());
                        if (hashMapArrayList.size() != 0) {
                            messageCalculater();
                        }

                        adapterMessages.notifyDataSetChanged();

                        if (animation_control == false) {
                            recyclerView.scheduleLayoutAnimation();
                            animation_control = true;
                        }

                        progressBar.setVisibility(View.INVISIBLE);


                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }


                }

                @Override
                public void onFailed(Object object) {

                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    @Override
    public void onItemClick(int position) {

        try {
            Intent intentToMessageActivity = new Intent(getApplicationContext(), MessageActivity.class);
            Person _person = person.get(position);
            System.out.println(_person.get_firstName() + "  " + _person.get_lastName() + "  " + _person.get_profileImage());


            String receiverName = _person.get_firstName() + " " + _person.get_lastName();
            String receiverID = _person.get_ID();
            Uri receiverImage = _person.get_profileImage();

            intentToMessageActivity.putExtra("receiverID", receiverID);
            intentToMessageActivity.putExtra("receiverName", receiverName);
            intentToMessageActivity.putExtra("receiverImage", receiverImage.toString());
            intentToMessageActivity.putExtra("receiverToken",_person.get_token());


            startActivity(intentToMessageActivity);

        } catch (Exception e) {

        }

    }

    @Override
    public void onLongItemClick(int position) {

    }
}