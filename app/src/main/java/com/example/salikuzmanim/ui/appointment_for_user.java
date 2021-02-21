package com.example.salikuzmanim.ui;

import android.app.Activity;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterAppointmentForUser;
import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.Concrete.Person;
import com.example.salikuzmanim.DataBaseManager.FireBaseAppointmentDal;
import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IVideoMeetingListener;
import com.example.salikuzmanim.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class appointment_for_user extends Fragment implements IVideoMeetingListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Appointment> appointmentArrayList;
    private ArrayList<Expert> expertArrayList;


    private AdapterAppointmentForUser adapterAppointmentForUser;
    private Boolean check_animation = false;
    private FragmentActivity myContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointment_for_user, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_appointment_for_user);
        progressBar = view.findViewById(R.id.progressBar_appointment_for_user);


        appointmentArrayList = new ArrayList<>();
        expertArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterAppointmentForUser = new AdapterAppointmentForUser(appointmentArrayList, expertArrayList, this, this.getChildFragmentManager());
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

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
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
        appointmentArrayList.clear();
        expertArrayList.clear();

        FireBaseAppointmentDal fireBaseAppointmentDal = new FireBaseAppointmentDal();
        fireBaseAppointmentDal.getAppointment(new IGetAppointmentDataListener() {
            @Override
            public void onSuccess(ArrayList entity) {
                appointmentArrayList.clear();
                expertArrayList.clear();
                expertArrayList.removeAll(expertArrayList);
                appointmentArrayList.removeAll(appointmentArrayList);

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
                                expert.set_email(email);;

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


    @Override
    public void initiateVideoMeeting(Person person,Appointment appointment) {
        /*
        if (person.get_token() == null || person.get_token().trim().isEmpty()) {
            Toast.makeText(getContext(), ((person.get_firstName() + " " + person.get_lastName()).toUpperCase() + " şu anda görüşme için hazır değil"), Toast.LENGTH_LONG).show();

        } else {
            try {
                Intent intentToOutGoingInvitation = new Intent(getContext(), OutgoingInvitationActivity.class);
                intentToOutGoingInvitation.putExtra("firstName", person.get_firstName());
                intentToOutGoingInvitation.putExtra("lastName", person.get_lastName());
                intentToOutGoingInvitation.putExtra("image_uri", person.get_profileImage().toString());
                intentToOutGoingInvitation.putExtra("token", person.get_token());
                intentToOutGoingInvitation.putExtra("userID", person.get_ID());
                intentToOutGoingInvitation.putExtra("email", person.get_email());
                intentToOutGoingInvitation.putExtra("appointmentDate",appointment.get_timestamp_appointment_date().toDate().toString());
                startActivity(intentToOutGoingInvitation);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }
        */

    }
}