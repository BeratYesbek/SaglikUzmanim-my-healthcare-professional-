package com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert.FragmentActivityForExpert;
import com.saglikuzmanimm.saglikuzmanim.Adapter.JobAdvertisementAdapter.AdapterDisplayJobAdvertisementToExpert;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.JobAdvertisementManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseJobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment.SingleChoiceLocationFragment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetJobAdvertisementListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class MainPageFragmentForExpert extends Fragment {

    private static Button button_filter_main_page_fragment;
    private static RecyclerView recyclerView_main_page_fragment;
    private static TextView textView_main_page_fragment;
    private static ProgressBar progressBar_main_page_fragment;
    private static ImageView imageView_main_page_fragmet;
    private static AdapterDisplayJobAdvertisementToExpert adapterDisplayJobAdvertisementToExpert;

    private static String _location = null;
    private static String _department = null;

    private static ArrayList<JobAdvertisement> advertisementArrayList;
    private static ArrayList<User> userArrayList;

    private static FragmentActivityForExpert activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        try {

            activity = (FragmentActivityForExpert) getActivity();
            _department = activity.getDepartment();


        } catch (Exception e) {
            System.out.println(e.toString());
        }

        final View viewFragment = inflater.inflate(R.layout.fragment_main_page_for_expert, container, false);


        advertisementArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();


        button_filter_main_page_fragment = viewFragment.findViewById(R.id.btn_filter_expert_main_fragment);
        progressBar_main_page_fragment = viewFragment.findViewById(R.id.progressBar_display_jobAdvertisement);
        textView_main_page_fragment = viewFragment.findViewById(R.id.textView_for_show_add_activity);
        imageView_main_page_fragmet = viewFragment.findViewById(R.id.sad_image);
        textView_main_page_fragment.setVisibility(View.INVISIBLE);
        imageView_main_page_fragmet.setVisibility(View.INVISIBLE);

        try {
            button_filter_main_page_fragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment singleChoiceLister = new SingleChoiceLocationFragment();
                    singleChoiceLister.setCancelable(false);
                    singleChoiceLister.show(activity.getSupportFragmentManager(), "choice location");

                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }


        getJobAdvertisementData(_location, _department);


        recyclerView_main_page_fragment = viewFragment.findViewById(R.id.recyclerView_add_show_for_expert_activity);
        recyclerView_main_page_fragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterDisplayJobAdvertisementToExpert = new AdapterDisplayJobAdvertisementToExpert(advertisementArrayList, userArrayList);
        recyclerView_main_page_fragment.setAdapter(adapterDisplayJobAdvertisementToExpert);
        recyclerView_main_page_fragment.setItemAnimator(new DefaultItemAnimator());
        layout_animation(recyclerView_main_page_fragment);

        return viewFragment;

    }


    public static void startAdapter() {
        adapterDisplayJobAdvertisementToExpert.notifyDataSetChanged();
        recyclerView_main_page_fragment.scheduleLayoutAnimation();
        progressBar_main_page_fragment.setVisibility(View.INVISIBLE);

    }

    public static void getJobAdvertisementData(String location, String department) {

        progressBar_main_page_fragment.setVisibility(View.VISIBLE);

        JobAdvertisement jobAdvertisement = new JobAdvertisement();
        jobAdvertisement.set_job_advertisement_department(department);
        jobAdvertisement.set_job_advertisement_location(location);
        JobAdvertisementManager jobAdvertisementManager = new JobAdvertisementManager(new FireBaseJobAdvertisementDal());
        jobAdvertisementManager.getJobAdvertisementByLocationAndDepartment(location,department, new IGetJobAdvertisementListener() {
            @Override
            public void onSuccess(ArrayList<JobAdvertisement> jobAdvertisementArrayList) {
                advertisementArrayList.removeAll(advertisementArrayList);
                userArrayList.removeAll(advertisementArrayList);
                textView_main_page_fragment.setVisibility(View.INVISIBLE);
                imageView_main_page_fragmet.setVisibility(View.INVISIBLE);
                advertisementArrayList.addAll(jobAdvertisementArrayList);
                getUser();
            }
            @Override
            public void onFailed(Exception exception) {
                advertisementArrayList.removeAll(advertisementArrayList);
                userArrayList.removeAll(advertisementArrayList);
                textView_main_page_fragment.setVisibility(View.VISIBLE);
                textView_main_page_fragment.setText("Üzgünüz ilan bulunamadı");
                imageView_main_page_fragmet.setVisibility(View.VISIBLE);
                progressBar_main_page_fragment.setVisibility(View.INVISIBLE);
                startAdapter();
            }
        });
    }


    public static void getUser() {


        UserManager userManager = new UserManager(new FireBaseUserDal());
        userManager.getAllUserQuery(new IGetQueryListener() {
            @Override
            public void onSuccess(QuerySnapshot queryDocument) {
                textView_main_page_fragment.setVisibility(View.INVISIBLE);
                imageView_main_page_fragmet.setVisibility(View.INVISIBLE);
                if(queryDocument.size() > 0) {
                    for (int i = 0; i < advertisementArrayList.size(); i++) {
                        JobAdvertisement jobAdvertisement = advertisementArrayList.get(i);
                        for (DocumentSnapshot document : queryDocument.getDocuments()) {
                            Map<String, Object> data;
                            data = document.getData();

                            String userUid = (String) data.get("userUid");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String profileImage = (String) data.get("profileImage");
                            String token = (String) data.get("token");
                            Uri uriImage = null;
                            if (profileImage != null) {
                                uriImage = Uri.parse(profileImage);
                            }

                            if (jobAdvertisement.get_uploaderID().equals(userUid)) {
                                User user = new User();
                                user.set_firstName(firstName);
                                user.set_lastName(lastName);
                                user.set_profileImage(uriImage);
                                System.out.println(uriImage);
                                user.set_ID(userUid);
                                user.set_token(token);
                                userArrayList.add(user);
                            }
                        }
                    }
                    startAdapter();
                } else {
                    textView_main_page_fragment.setVisibility(View.VISIBLE);
                    textView_main_page_fragment.setText("Üzgünüz size göre ilan bulunamadı");
                    imageView_main_page_fragmet.setVisibility(View.VISIBLE);
                    progressBar_main_page_fragment.setVisibility(View.INVISIBLE);
                    startAdapter();
                }
            }

            @Override
            public void onFailed(Exception e) {
                textView_main_page_fragment.setVisibility(View.VISIBLE);
                textView_main_page_fragment.setText("Üzgünüz size göre ilan bulunamadı");
                imageView_main_page_fragmet.setVisibility(View.VISIBLE);
                progressBar_main_page_fragment.setVisibility(View.INVISIBLE);
                startAdapter();
            }
        });
    }

    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation_fade_top);
        recyclerView.setLayoutAnimation(animationController);

    }

    public static MainPageFragmentForExpert newInstance() {
        return new MainPageFragmentForExpert();
    }

    public static void setLocation(String location) {
        _location = location;
        getJobAdvertisementData(_location,_department);

    }

}
