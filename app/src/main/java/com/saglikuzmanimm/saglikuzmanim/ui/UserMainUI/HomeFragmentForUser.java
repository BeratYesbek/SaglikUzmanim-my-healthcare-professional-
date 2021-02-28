package com.saglikuzmanimm.saglikuzmanim.ui.UserMainUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters.AdapterGridViewForHomeFragment;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser.ReyclerViewShowExpertForUserActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentForUser extends Fragment {

    String[] text = {"Doktor", "Fizyoterapist", "Hemşire", "Diyetisyen", "Psikolog", "Hasta Bakıcı"};
    int[] numberImage = {R.drawable.doctor, R.drawable.physicaltherapy, R.drawable.nursing, R.drawable.nutritionist, R.drawable.psychologist, R.drawable.nurse_2};
    GridView gridView;
    ImageSlider imageSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_for_user, container, false);

        imageSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> slide = new ArrayList<SlideModel>();
        //that's slider link is temporary
        slide.add(new SlideModel("https://www.nabizosgb.com.tr/images/pages/actual/saglik.jpg", ""));
        slide.add(new SlideModel("https://www.kamubulteni.com/images/haberler/2019/07/2019_temmuz_zamli_hemsire_maaslari_hemsireler_temmuz_da_ne_kadar_maas_alacak_h11689_d9749.jpg", ""));
        imageSlider.setImageList(slide, true);

        gridView = view.findViewById(R.id.grid_view);

        gridView.setAdapter(new AdapterGridViewForHomeFragment(text, numberImage, this.getActivity()));
        layout_animation(gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView expert = view.findViewById(R.id.text_home);
                String expertChoice = expert.getText().toString();


                Intent intentToShowExpert = new Intent(getActivity(), ReyclerViewShowExpertForUserActivity.class);
                intentToShowExpert.putExtra("department", expertChoice);
                startActivity(intentToShowExpert);


            }
        });
        return view;

    }

    public void layout_animation(GridView gridView) {
        Context context = gridView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_view_animation);
        gridView.setLayoutAnimation(animationController);
        gridView.scheduleLayoutAnimation();
    }
}
