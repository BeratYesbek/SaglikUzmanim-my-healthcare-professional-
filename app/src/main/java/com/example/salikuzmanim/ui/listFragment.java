package com.example.salikuzmanim.ui;

import android.content.Context;
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

import com.example.salikuzmanim.Adapter.AdapterJobAdvertisementDisplayUser;
import com.example.salikuzmanim.DataBaseManager.FireBaseJobAdvertisementDal;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.R;

import java.util.ArrayList;

public class listFragment extends Fragment {


    private ProgressBar progressBar_jobAdvertisement_list;
    private TextView textView_fragment_list;
    private RecyclerView recyclerView;

    private AdapterJobAdvertisementDisplayUser adapterJobAdvertisementDisplayUser;

    private ArrayList<JobAdvertisement> advertisementArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);


        progressBar_jobAdvertisement_list = view.findViewById(R.id.progressBar_adFor_list);
        textView_fragment_list = view.findViewById(R.id.textView_fragment_list);
        textView_fragment_list.setVisibility(View.INVISIBLE);

        advertisementArrayList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView_ad);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterJobAdvertisementDisplayUser = new AdapterJobAdvertisementDisplayUser(advertisementArrayList);
        recyclerView.setAdapter(adapterJobAdvertisementDisplayUser);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animation(recyclerView);


        getJobAdvertisementData();
        return view;
    }

    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation_bottom);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }


    public void getJobAdvertisementData() {
        try {

            FireBaseJobAdvertisementDal fireBaseJobAdvertisementDal = new FireBaseJobAdvertisementDal();
            fireBaseJobAdvertisementDal.getJobAdvertisement(new IGetListDataListener() {
                @Override
                public void onSuccess(ArrayList entity) {
                    try {
                        advertisementArrayList.removeAll(advertisementArrayList);
                        advertisementArrayList.addAll(entity);
                        adapterJobAdvertisementDisplayUser.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                        progressBar_jobAdvertisement_list.setVisibility(View.INVISIBLE);
                        textView_fragment_list.setVisibility(View.INVISIBLE);

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailed(Exception exception) {
                    textView_fragment_list.setVisibility(View.VISIBLE);
                    textView_fragment_list.setText("Herhangi bir ilan bulunmamaktadır");
                    progressBar_jobAdvertisement_list.setVisibility(View.INVISIBLE);
                }
            });

        } catch (Exception e) {

        }

    }
}














