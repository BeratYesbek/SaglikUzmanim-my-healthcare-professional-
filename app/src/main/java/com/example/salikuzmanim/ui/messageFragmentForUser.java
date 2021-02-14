package com.example.salikuzmanim.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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


public class messageFragmentForUser extends Fragment implements IRecyclerViewClick {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textView;
    private TextView textView_info;


    private AdapterMessages adapterMessages;
    private boolean animation_control=false;
    private ArrayList<Person> person;
    private ArrayList<HashMap> hashMapArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_for_user, container, false);

        person = new ArrayList<>();
        hashMapArrayList = new ArrayList<>();


        recyclerView = view.findViewById(R.id.reyclerView_displayUser_messages);
        progressBar = view.findViewById(R.id.progressBar_message_fragment_for_user);
        textView = view.findViewById(R.id.textView_message_fragmentUser_for_info);
        textView_info = view.findViewById(R.id.textView_info_messages_user_list);
        textView.setVisibility(View.INVISIBLE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterMessages = new AdapterMessages(person, hashMapArrayList, (IRecyclerViewClick) this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutAnimation(recyclerView);
        recyclerView.setAdapter(adapterMessages);

        getUser();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }

    public void messageCalculater(){
        System.out.println("naptın haci");
        int total = 0;
        for(int i=0; i<hashMapArrayList.size();i++){
            HashMap<String,Object> hashMap = hashMapArrayList.get(i);
            int totalIsntSeen = (int) hashMap.get("totalIsntSeen");
            total = total +totalIsntSeen;
            System.out.println(total);

        }
        textView_info.setText("("+total+")" + " yeni sohbet");

    }
    public void remove_adapter_item(){
        for (int index = 0; index<person.size();index++){
            adapterMessages.notifyItemRemoved(index);
        }
    }
    public void getUser() {
        try {
            FireBaseChatDal fireBaseChatDal = new FireBaseChatDal();
            fireBaseChatDal.getMessageForList("expert", new IGetDataListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        remove_adapter_item();
                        person.clear();
                        hashMapArrayList.clear();
                        MessageArrayList messageArrayList = (MessageArrayList) object;
                        person.addAll(messageArrayList.getArrayListPerson());
                        hashMapArrayList.addAll(messageArrayList.getHashMapArrayList());
                        messageCalculater();
                        adapterMessages.notifyDataSetChanged();

                        if(animation_control == false){
                            recyclerView.scheduleLayoutAnimation();
                            animation_control=true;
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


    private void layoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(animationController);

    }

    @Override
    public void onItemClick(int position) {
        try {
            Person _person = person.get(position);

            String receiverName = _person.get_firstName().toString() + " " + _person.get_lastName().toString();
            String receiverID = _person.get_ID();
            Uri receiverImage = _person.get_profileImage();
            Intent intentToMessageActivity = new Intent(getActivity(), MessageActivity.class);

            intentToMessageActivity.putExtra("receiverID", receiverID);
            intentToMessageActivity.putExtra("receiverName", receiverName);
            intentToMessageActivity.putExtra("receiverImage", receiverImage.toString());

            startActivity(intentToMessageActivity);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void onLongItemClick(int position) {

    }

}
