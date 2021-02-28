package com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Adapter.AppointmentAdapter.AdapterAppointmentForExpert;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.AppointmentManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseAppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetAppointmentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class AppointmentFragmentForExpert extends Fragment {


    private AdapterAppointmentForExpert appointmentAdapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Appointment> _appointmentArrayList;
    private ArrayList<User> _userArrayList;

    private Boolean check_animation= false;

    private TextView textView_info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_appointments_for_expert, container, false);

        _appointmentArrayList = new ArrayList<>();
        _userArrayList = new ArrayList<>();


        progressBar = view.findViewById(R.id.progressBar_appointment_for_expert);
        recyclerView = view.findViewById(R.id.recyclerView_appointment_for_expert);
        textView_info = view.findViewById(R.id.textView_appointment_info_ui_expert);
        textView_info.setVisibility(View.INVISIBLE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentAdapter = new AdapterAppointmentForExpert(_appointmentArrayList, _userArrayList,this.getChildFragmentManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animation(recyclerView);


        getAppointmentData();


        return view;
    }

    public void startAdapter() {
        if(check_animation != null){
            appointmentAdapter.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
            progressBar.setVisibility(View.INVISIBLE);
            textView_info.setVisibility(View.INVISIBLE);
            check_animation =true;
        }else{
            appointmentAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    public void getAppointmentData() {


        AppointmentManager appointmentManager = new AppointmentManager(new FireBaseAppointmentDal());
        appointmentManager.getData(null, new IGetAppointmentListener() {
            @Override
            public void onSuccess(ArrayList<Appointment> appointmentArrayList) {
                _appointmentArrayList.clear();
                _userArrayList.clear();
                _appointmentArrayList.addAll(appointmentArrayList);

                getUserData();
            }

            @Override
            public void onFailed(Exception exception) {
                progressBar.setVisibility(View.INVISIBLE);
                textView_info.setVisibility(View.VISIBLE);
                textView_info.setText("Randevu talebi bulunamamaktadır.");
            }
        });
    }

    public void getUserData() {

        UserManager userManager = new UserManager(new FireBaseUserDal());
        userManager.getAllUserQuery(new IGetQueryListener() {
            @Override
            public void onSuccess(QuerySnapshot queryDocument) {
                try {


                    for (int i = 0; i < _appointmentArrayList.size(); i++) {
                        Appointment appointment = _appointmentArrayList.get(i);
                        for (DocumentSnapshot document : queryDocument.getDocuments()) {
                            Map<String, Object> data = document.getData();

                            String userUid = (String) data.get("userUid");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String profileImage = (String) data.get("profileImage");
                            String token = (String) data.get("token");

                            if (appointment.get_senderID().equals(userUid)) {
                                Uri uriImage = null;
                                if (profileImage != null) {
                                    uriImage = Uri.parse(profileImage);
                                }
                                User user = new User();
                                user.set_firstName(firstName);
                                user.set_lastName(lastName);
                                user.set_profileImage(uriImage);
                                user.set_ID(userUid);
                                user.set_token(token);

                                _userArrayList.add(user);
                            }
                        }
                    }
                    startAdapter();
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            @Override
            public void onFailed(Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                textView_info.setVisibility(View.VISIBLE);
                textView_info.setText("Randevu talebi bulunamamaktadır.");

            }
        });
    }

    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }


    public static AppointmentFragmentForExpert newInstance() {
        return new AppointmentFragmentForExpert();
    }

}