package com.example.salikuzmanim.ui;

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
import com.example.salikuzmanim.Adapter.AdapterGridViewForHomeFragment;
import com.example.salikuzmanim.R;
import com.example.salikuzmanim.gridViewClickManager.ReyclerViewShowExpertForUserActivity;

import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment {

    String[] text = {"Doktor", "Fizyoterapist", "Hemşire", "Diyetisyen"};
    int[] numberImage = {R.drawable.doctor, R.drawable.physicaltherapy, R.drawable.nursing, R.drawable.nutritionist};
    GridView gridView;
    ImageSlider imageSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> slide = new ArrayList<SlideModel>();
        slide.add(new SlideModel("https://bit.ly/2YoJ77H", "template"));
        slide.add(new SlideModel("https://bit.ly/2BteuF2", "template"));
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
                intentToShowExpert.putExtra("departmant", expertChoice);
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
