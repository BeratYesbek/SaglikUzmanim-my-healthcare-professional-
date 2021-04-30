package com.saglikuzmanimm.saglikuzmanim.ui.UserMainUI;

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

import com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity.MessageActivity;
import com.saglikuzmanimm.saglikuzmanim.Adapter.AdapterMessages.AdapterMessages;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ChatManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.MessageArrayList;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Person;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.ChatDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetMessageArrayListListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IRecyclerViewClick;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.util.ArrayList;
import java.util.HashMap;


public class MessageFragmentForUser extends Fragment implements IRecyclerViewClick {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textView;
    private TextView textView_info;

    private AdapterMessages adapterMessages;

    private ArrayList<Person> person;
    private ArrayList<HashMap> hashMapArrayList;

    private boolean animation_control = false;

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

    public void messageCalculator() {


        HashMap<String, Object> hashMap = hashMapArrayList.get(0);
        int totalIsntSeen = (int) hashMap.get("totalIsntSeen");


        textView_info.setText("(" + totalIsntSeen + ")" + " yeni sohbet");

    }

    public void remove_adapter_item() {
        for (int index = 0; index < person.size(); index++) {
            adapterMessages.notifyItemRemoved(index);
        }
    }

    public void getUser() {


        ChatManager chatManager = new ChatManager(new ChatDal());
        chatManager.getMessageForList("expert", new IGetMessageArrayListListener() {
            @Override
            public void onSuccess(MessageArrayList messageArrayLists) {
                try {
                    remove_adapter_item();
                    person.clear();
                    hashMapArrayList.clear();
                    MessageArrayList _messageArrayList = (MessageArrayList) messageArrayLists;
                    person.addAll(_messageArrayList.getArrayListPerson());
                    hashMapArrayList.addAll(_messageArrayList.getHashMapArrayList());

                    adapterMessages.notifyDataSetChanged();
                    if (hashMapArrayList.size() != 0) {
                        messageCalculator();
                    } else {
                        textView_info.setText("(" + 0 + ")" + " yeni sohbet");
                    }
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
            public void onFailed(Exception exception) {
                progressBar.setVisibility(View.INVISIBLE);
                textView.setText("Mesajınız bulunmamaktadır");
            }
        });

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
            System.out.println(receiverName + "  " + receiverID + " " + receiverImage);
            Intent intentToMessageActivity = new Intent(getActivity(), MessageActivity.class);

            intentToMessageActivity.putExtra("receiverID", receiverID);
            intentToMessageActivity.putExtra("receiverName", receiverName);
            intentToMessageActivity.putExtra("receiverImage", receiverImage.toString());
            intentToMessageActivity.putExtra("receiverToken", _person.get_token());

            startActivity(intentToMessageActivity);
        } catch (Exception e) {
            System.out.println(e.toString());

        }
    }

    @Override
    public void onLongItemClick(int position) {

    }

}
