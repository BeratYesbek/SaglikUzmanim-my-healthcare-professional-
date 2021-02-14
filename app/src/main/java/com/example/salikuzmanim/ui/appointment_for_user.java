package com.example.salikuzmanim.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterAppointmentForUser;
import com.example.salikuzmanim.DataBaseManager.FireBaseAppointmentDal;
import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class appointment_for_user extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Appointment> appointmentArrayList;
    private ArrayList<Expert> expertArrayList;


    private AdapterAppointmentForUser adapterAppointmentForUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointment_for_user, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_appointment_for_user);
        progressBar = view.findViewById(R.id.progressBar_appointment_for_user);


        appointmentArrayList = new ArrayList<>();
        expertArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterAppointmentForUser = new AdapterAppointmentForUser(appointmentArrayList, expertArrayList);
        recyclerView.setAdapter(adapterAppointmentForUser);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animatior(recyclerView);

        getAppointmentData();

        return view;
    }

    public void layout_animatior(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }

    public void startAdapter() {
        adapterAppointmentForUser.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        progressBar.setVisibility(View.INVISIBLE);
    }


    public void getAppointmentData() {
        FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();
        fireBaseAppointmentDal.getAppointment(new IGetAppointmentDataListener() {
            @Override
            public void onSuccess(ArrayList entity) {
                appointmentArrayList.addAll(entity);
                getExpertData();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    public void getExpertData() {
        FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
        fireBaseExpertDal.getExpertList(new IGetDataListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
                    for (int i = 0; i < appointmentArrayList.size(); i++) {
                        Appointment appointment = appointmentArrayList.get(i);
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = document.getData();

                            String expertUid = (String) data.get("expertUid");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String profileImage = (String) data.get("profileImage");
                            String department = (String) data.get("department");
                            Boolean check_expert = (Boolean) data.get("check_expert");
                            if (appointment.get_receiverID().equals(expertUid)) {
                                Uri uriImage = null;
                                if (profileImage != null) {
                                    uriImage = Uri.parse(profileImage);
                                }

                                Expert expert = new Expert();

                                expert.set_firstName(firstName);
                                expert.set_lastName(lastName);
                                expert.set_check_expert(check_expert);
                                expert.set_profileImage(uriImage);
                                expert.set_department(department);

                                expertArrayList.add(expert);
                            }

                        }
                    }
                    startAdapter();


                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailed(Object object) {

            }
        });
    }



}