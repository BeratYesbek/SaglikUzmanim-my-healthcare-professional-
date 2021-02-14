package com.example.salikuzmanim.gridViewClickManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterDisplayJobAdvertisementToExpert;
import com.example.salikuzmanim.Fragment.SingleChoiceLocationFragment;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;
import com.example.salikuzmanim.Concrete.JobAdvertisement;
import com.example.salikuzmanim.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class ReyclerViewShowAddForExpertActivity extends AppCompatActivity implements SingleChoiceLister {

    String choiceExpert;
    String choiceLocation;
    int j=0;
    int i =0;
    int documentCounrt;
    private static RecyclerView recyclerView;
    Button btn_for_add_show_activity;
    private static TextView textView_for_add_show_activity;
    private static ProgressBar progressBar;
    private static ImageView imageView;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;

    private static  ArrayList<String> title;
    private static  ArrayList<String> explanation;
    private static  ArrayList<String> location;
    private static  ArrayList<String> expert;
    private static  ArrayList<String> name;
    private static  ArrayList<String> date;
    private static  ArrayList<Uri> Uri;
    private static AdapterDisplayJobAdvertisementToExpert addForExpert;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reycler_view_show_add_for_expert);
        textView_for_add_show_activity = findViewById(R.id.textView_for_show_add_activity);
        btn_for_add_show_activity = findViewById(R.id.btn_choice_location_for_show_add_activity);
        progressBar = findViewById(R.id.progressBar_for_show_add_expert);
        imageView = findViewById(R.id.sad_image);

        Sprite fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        btn_for_add_show_activity.setVisibility(View.INVISIBLE);


        title = new ArrayList<>();
        explanation = new ArrayList<>();
        location = new ArrayList<>();
        expert = new ArrayList<>();
        date = new ArrayList<>();
        Uri = new ArrayList<>();
        name = new ArrayList<>();





        Intent intent = getIntent();
        choiceExpert = intent.getStringExtra("expertChoice");

        getAlertDialog();

        recyclerView = findViewById(R.id.recyclerView_add_show_for_expert_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // addForExpert = new AdaptarAddForExpert(title,explanation,location,expert,date,name,Uri);
        recyclerView.setAdapter(addForExpert);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animation(recyclerView);

        btn_for_add_show_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlertDialog();
            }
        });
    }
    public static void layout_animation(RecyclerView recyclerView){
        Context context = recyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context,R.anim.recycler_view_animation_fade_top);
        recyclerView.setLayoutAnimation(animationController);


    }

    public void getDataForAd() {
        progressBar.setVisibility(View.VISIBLE);
/*
        UserAdDal userAdDal = new UserAdDal();
        userAdDal.getAdDataForExpert(new JobAdvertisement(choiceLocation,choiceExpert));*/

    }

    public static void accessData(JobAdvertisement jobAdvertisement, Boolean result) {
        if(result == false){
            textView_for_add_show_activity.setVisibility(View.VISIBLE);
            textView_for_add_show_activity.setText("Üzgünüz aradğınız kiriterde bir ilan bulunamadı.");
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            /*
            title.add(jobAdvertisement.getTitle());
            explanation.add(jobAdvertisement.getExplanation());
            location.add(jobAdvertisement.getLocation_ad());
            expert.add(jobAdvertisement.getDepartmant_ad());
            name.add(jobAdvertisement.getName());
            date.add(jobAdvertisement.getDate());
            */

        }

    }
    public static void accessImage(Uri uri,Boolean result) {
        Uri.add(uri);
        if(result==true){
            progressBar.setVisibility(View.INVISIBLE);
            addForExpert.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
    }




    public void getAlertDialog(){
        DialogFragment singleChoiceLister = new SingleChoiceLocationFragment();
        singleChoiceLister.setCancelable(false);
        singleChoiceLister.show(getSupportFragmentManager(),"choice location");
    }

    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        btn_for_add_show_activity.setVisibility(View.INVISIBLE);
        textView_for_add_show_activity.setVisibility(View.INVISIBLE);
        choiceLocation = list[position];
        getDataForAd();
    }

    @Override
    public void onNegativeButtonClicked() {
        textView_for_add_show_activity.setText("Lütfen bir konum seçiniz");
        btn_for_add_show_activity.setVisibility(View.VISIBLE);


    }
}