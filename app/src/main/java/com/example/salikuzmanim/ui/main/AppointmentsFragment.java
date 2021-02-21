package com.example.salikuzmanim.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterAppointmentForExpert;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.DataBaseManager.FireBaseAppointmentDal;
import com.example.salikuzmanim.DataBaseManager.FireBaseUserDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetQueryListener;
import com.example.salikuzmanim.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class AppointmentsFragment extends Fragment {


    private AdapterAppointmentForExpert appointmentAdapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Appointment> appointmentArrayList;
    private ArrayList<User> userArrayList;

    private Boolean check_animation= false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        appointmentArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();


        progressBar = view.findViewById(R.id.progressBar_appointment_for_expert);
        recyclerView = view.findViewById(R.id.recyclerView_appointment_for_expert);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentAdapter = new AdapterAppointmentForExpert(appointmentArrayList, userArrayList,this.getChildFragmentManager());
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
            check_animation =true;
        }else{
            appointmentAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    public void getAppointmentData() {
        appointmentArrayList.clear();
        userArrayList.clear();
        FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();
        fireBaseAppointmentDal.getAppointment(new IGetAppointmentDataListener() {
            @Override
            public void onSuccess(ArrayList entity) {
                appointmentArrayList.clear();
                userArrayList.clear();

                appointmentArrayList.addAll(entity);

                getUserData();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    public void getUserData() {
        FireBaseUserDal fireBaseUserDal = new FireBaseUserDal();
        fireBaseUserDal.getAllUser(new IGetQueryListener() {
            @Override
            public void onSuccess(QuerySnapshot queryDocument) {
                try {


                    for (int i = 0; i < appointmentArrayList.size(); i++) {
                        Appointment appointment = appointmentArrayList.get(i);
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

                                userArrayList.add(user);
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

            }
        });

    }

    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }


    public static AppointmentsFragment newInstance() {
        return new AppointmentsFragment();
    }

}