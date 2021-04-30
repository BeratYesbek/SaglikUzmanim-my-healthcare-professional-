package com.saglikuzmanimm.saglikuzmanim.ui.UserMainUI;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.saglikuzmanimm.saglikuzmanim.Adapter.AppointmentAdapter.AdapterAppointmentForUser;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.AppointmentManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.AppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.ExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetAppointmentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.util.ArrayList;
import java.util.Map;


public class AppointmentFragmentForUser extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Appointment> _appointmentArrayList;
    private ArrayList<Expert> _expertArrayList;


    private AdapterAppointmentForUser adapterAppointmentForUser;
    private Boolean check_animation = false;

    private TextView textView_info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointment_for_user, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_appointment_for_user);
        progressBar = view.findViewById(R.id.progressBar_appointment_for_user);
        textView_info = view.findViewById(R.id.textView_info_appointment_ui_user);


        _appointmentArrayList = new ArrayList<>();
        _expertArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterAppointmentForUser = new AdapterAppointmentForUser(_appointmentArrayList, _expertArrayList, this.getChildFragmentManager());
        recyclerView.setAdapter(adapterAppointmentForUser);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animation(recyclerView);


        getAppointmentData();

        return view;
    }


    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }


    public void startAdapter() {
        if (check_animation != true) {
            adapterAppointmentForUser.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
            progressBar.setVisibility(View.INVISIBLE);
            check_animation = true;

        } else {
            adapterAppointmentForUser.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }


    public void getAppointmentData() {


        AppointmentManager appointmentManager = new AppointmentManager(new AppointmentDal());
        appointmentManager.getData(null, new IGetAppointmentListener() {
            @Override
            public void onSuccess(ArrayList<Appointment> appointmentArrayList) {
                _appointmentArrayList.removeAll(_appointmentArrayList);
                _appointmentArrayList.addAll(appointmentArrayList);

                getExpertData();

            }

            @Override
            public void onFailed(Exception exception) {

                textView_info.setText("Henüz randevu talebiniz bulunmamaktadır");
                textView_info.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    public void getExpertData() {

        ExpertManager expertManager = new ExpertManager(new ExpertDal());
        expertManager.getExpertQuery(new IGetQueryListener() {
            @Override
            public void onSuccess(QuerySnapshot queryDocument) {

                    QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) queryDocument;
                    for (int i = 0; i < _appointmentArrayList.size(); i++) {
                        Appointment appointment = _appointmentArrayList.get(i);
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = document.getData();

                            String expertUid = (String) data.get("expertUid");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String profileImage = (String) data.get("profileImage");
                            String department = (String) data.get("department");
                            String token = (String) data.get("token");
                            String email = (String) data.get("email");
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
                                expert.set_token(token);
                                expert.set_email(email);
                                expert.set_ID(expertUid);

                                _expertArrayList.add(expert);
                            }

                        }
                    }
                    startAdapter();
            }

            @Override
            public void onFailed(Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                textView_info.setVisibility(View.VISIBLE);
                textView_info.setText("Henüz randevu talebiniz bulunmamaktadır");
            }
        });


    }


}