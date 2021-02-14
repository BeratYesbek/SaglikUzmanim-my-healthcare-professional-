package com.example.salikuzmanim.ui.main;

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

import com.example.salikuzmanim.Activity.ActivityExpert.FragmentActivityForExpert;
import com.example.salikuzmanim.Adapter.AdapterDisplayJobAdvertisementToExpert;
import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.DataBaseManager.FireBaseJobAdvertisementDal;
import com.example.salikuzmanim.DataBaseManager.FireBaseUserDal;
import com.example.salikuzmanim.Fragment.SingleChoiceLocationFragment;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetQueryListener;
import com.example.salikuzmanim.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class MainPageFragment extends Fragment {
    /*
        private FragmentActivity myContext;


        AdapterGridViewForHomeFragment adapterGridViewForHomeFragment;

        String[] text = {"Doktor", "Fizyoterapist", "Hemşire", "Diyetisyen"};


        int[] numberImage = {R.drawable.doctor, R.drawable.physicaltherapy, R.drawable.nursing, R.drawable.nutritionist};
        GridView gridView;
    */
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

        final View viewFragment = inflater.inflate(R.layout.fragment_main_page, container, false);
        //final Activity activity = this.getActivity();
        /*
        gridView = viewFragment.findViewById(R.id.grid_view_expert);
        gridView.setAdapter(new AdapterGridViewForHomeFragment(text, numberImage, this.getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView expert = view.findViewById(R.id.text_home);
                String expertName = expert.getText().toString();

                Intent intentToShowAdd = new Intent(getActivity(), ReyclerViewShowAddForExpertActivity.class);
                intentToShowAdd.putExtra("expertChoice", expertName);
                startActivity(intentToShowAdd);


            }

        });
        */

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
        advertisementArrayList.removeAll(advertisementArrayList);
        userArrayList.removeAll(advertisementArrayList);
        progressBar_main_page_fragment.setVisibility(View.VISIBLE);

      FireBaseJobAdvertisementDal fireBaseJobAdvertisementDal = new FireBaseJobAdvertisementDal();
        fireBaseJobAdvertisementDal.getAllJobAdvertisement(location, department, new IGetListDataListener() {
            @Override
            public void onSuccess(ArrayList entity) {
                textView_main_page_fragment.setVisibility(View.INVISIBLE);
                imageView_main_page_fragmet.setVisibility(View.INVISIBLE);
                advertisementArrayList.addAll(entity);
                getUser();
            }

            @Override
            public void onFailed(Exception exception) {
                textView_main_page_fragment.setVisibility(View.VISIBLE);
                textView_main_page_fragment.setText("Üzgünüz ilan bulunamadı");
                imageView_main_page_fragmet.setVisibility(View.VISIBLE);
                progressBar_main_page_fragment.setVisibility(View.INVISIBLE);

                startAdapter();
            }
        });
    }

    public static void getUser() {

        FireBaseUserDal fireBaseUserDal = new FireBaseUserDal();
        fireBaseUserDal.getAllUser(new IGetQueryListener() {
            @Override
            public void onSuccess(QuerySnapshot queryDocument) {
                textView_main_page_fragment.setVisibility(View.INVISIBLE);
                imageView_main_page_fragmet.setVisibility(View.INVISIBLE);
                if (queryDocument.size() > 0) {
                    for (int i = 0; i < advertisementArrayList.size(); i++) {
                        JobAdvertisement jobAdvertisement = advertisementArrayList.get(i);
                        for (DocumentSnapshot document : queryDocument.getDocuments()) {
                            Map<String, Object> data = document.getData();

                            String userUid = (String) data.get("userUid");
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String profileImage = (String) data.get("profileImage");
                            Uri uriImage = null;
                            if (profileImage != null) {
                                uriImage = Uri.parse(profileImage);
                            }

                            if (jobAdvertisement.get_uploaderID().equals(userUid)) {
                                User user = new User();
                                user.set_firstName(firstName);
                                user.set_lastName(lastName);
                                user.set_profileImage(uriImage);
                                user.set_ID(userUid);
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

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    public static void setLocation(String location) {
        _location = location;
        getJobAdvertisementData(_location,_department);

    }

}
