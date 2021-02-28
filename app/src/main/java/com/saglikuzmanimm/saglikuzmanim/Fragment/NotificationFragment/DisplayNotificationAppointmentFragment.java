package com.saglikuzmanimm.saglikuzmanim.Fragment.NotificationFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Activity.NotificationTabLayout.NotificationTabLayoutActivity;
import com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters.AdapterDisplayNotification;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.NotificationManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseNotificationDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IRecyclerViewClick;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.saglikuzmanimm.saglikuzmanim.ui.UserMainUI.AppointmentFragmentForUser;
import com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI.AppointmentFragmentForExpert;

import java.util.ArrayList;


public class DisplayNotificationAppointmentFragment extends Fragment implements IRecyclerViewClick {


    private RecyclerView _recyclerView;

    private ArrayList<Notification> _notificationArrayList;

    private AdapterDisplayNotification adapterDisplayNotification;

    private String whichIntent;

    private TextView textView_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_notification_appointment, container, false);


        try{
            NotificationTabLayoutActivity activity = (NotificationTabLayoutActivity) getActivity();
            whichIntent = activity.getType();

        }catch (Exception e){

        }
        textView_info = view.findViewById(R.id.textView_notification_appointment);
        textView_info.setVisibility(View.INVISIBLE);


        _notificationArrayList = new ArrayList<>();



        _recyclerView = view.findViewById(R.id.recycler_display_appointment_notification);

        _recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterDisplayNotification = new AdapterDisplayNotification(_notificationArrayList,_recyclerView);
        _recyclerView.setAdapter(adapterDisplayNotification);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animation(_recyclerView);

        getNotificationDate();
        return view;
    }
    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation_fade_top);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }

   private void getNotificationDate(){
       NotificationManager notificationManager = new NotificationManager(new FireBaseNotificationDal());
       notificationManager.getData(null, new IGetNotificationListener() {
           @Override
           public void onSuccess(ArrayList<Notification> notificationArrayList) {
               _notificationArrayList.clear();
               _notificationArrayList.addAll(notificationArrayList);
               adapterDisplayNotification.notifyDataSetChanged();
               _recyclerView.scheduleLayoutAnimation();
               updateSeenNotification();
           }
           @Override
           public void onFailed(Exception exception) {
                textView_info.setVisibility(View.VISIBLE);
           }
       });

    }
    private void updateSeenNotification(){
        NotificationManager notificationManager = new NotificationManager(new FireBaseNotificationDal());

        for(int i=0; i<_notificationArrayList.size(); i++){
            Notification notification = _notificationArrayList.get(i);

            if(notification.get_isSeen() == false){
                notificationManager.updateData(notification, new IResult() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(Exception exception) {
                        System.out.println(exception);
                    }
                });
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if(whichIntent.equals("expert")){
            Intent intentToAppointment = new Intent(getContext(), AppointmentFragmentForExpert.class);
            getActivity().startActivity(intentToAppointment);

        }else{
            Intent intentToAppointment = new Intent(getContext(), AppointmentFragmentForUser.class);
            getActivity().startActivity(intentToAppointment);
        }
    }

    @Override
    public void onLongItemClick(int position) {

    }
}