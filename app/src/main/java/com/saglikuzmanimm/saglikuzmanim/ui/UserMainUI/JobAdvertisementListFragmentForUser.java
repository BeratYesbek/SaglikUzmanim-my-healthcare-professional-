package com.saglikuzmanimm.saglikuzmanim.ui.UserMainUI;

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

import com.saglikuzmanimm.saglikuzmanim.Adapter.JobAdvertisementAdapter.AdapterJobAdvertisementDisplayUser;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.JobAdvertisementManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.JobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetJobAdvertisementListener;
import com.saglikuzmanimm.saglikuzmanim.R;

import java.util.ArrayList;

public class JobAdvertisementListFragmentForUser extends Fragment {


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
     


            JobAdvertisementManager jobAdvertisementManager = new JobAdvertisementManager(new JobAdvertisementDal());
            jobAdvertisementManager.getData(null, new IGetJobAdvertisementListener() {
                @Override
                public void onSuccess(ArrayList<JobAdvertisement> jobAdvertisementArrayList) {
                    advertisementArrayList.removeAll(advertisementArrayList);
                    advertisementArrayList.clear();
                    advertisementArrayList.addAll(jobAdvertisementArrayList);
                    progressBar_jobAdvertisement_list.setVisibility(View.INVISIBLE);
                    textView_fragment_list.setVisibility(View.INVISIBLE);
                    adapterJobAdvertisementDisplayUser.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();

                }

                @Override
                public void onFailed(Exception exception) {
                    textView_fragment_list.setVisibility(View.VISIBLE);
                    textView_fragment_list.setText("Herhangi bir ilan bulunmamaktadır");
                    progressBar_jobAdvertisement_list.setVisibility(View.INVISIBLE);
                }
            });
        }
}














