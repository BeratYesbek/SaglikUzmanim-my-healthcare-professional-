package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saglikuzmanimm.saglikuzmanim.Adapter.OtherAdapters.AdapterShowExpertToUser;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.ExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Collection;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.FireBaseDataBase.FireBaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Fragment.CollectionExpertFragment;
import com.saglikuzmanimm.saglikuzmanim.Fragment.SingleChoiceFragment.SingleChoiceDoctorDepartmantFragment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.SingleChoiceLister;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ReyclerViewShowExpertForUserActivity extends AppCompatActivity implements SingleChoiceLister {

    private String sendDepartment;

    private static ArrayList<Expert> _expertArrayList;


    private static String choiceDepartment;
    private static RecyclerView recyclerView;
    private static TextView textView_show_message;
    private static ProgressBar progressBar;
    private static ImageView imageView;

    private Button buttonOrder;
    private Button buttonFilter;
    private Button choiceExpert;


    private static AdapterShowExpertToUser adapterShowExpertToUser;
    private static FragmentTransaction fragmentTransaction;
    private static CollectionExpertFragment fragment;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reycler_view_show_expert_for_user);



        context = getApplicationContext();

        _expertArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView_for_user);
        textView_show_message = findViewById(R.id.textView_reyclerView_for_user);
        imageView = findViewById(R.id.imageView_sad_for_user);
        progressBar = findViewById(R.id.progressBar_rc_for_user);
        buttonOrder = findViewById(R.id.btn_ordinary_something1);
        buttonFilter = findViewById(R.id.btn_filter_something);
        choiceExpert = findViewById(R.id.btn_choice_expert_for_rc);
        choiceExpert.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        textView_show_message.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        sendDepartment = intent.getStringExtra("department");

        if (sendDepartment.equals("Doktor")) {

            getAlertDialog();

        }else{
            choiceDepartment = sendDepartment;
            getExpertData();
        }

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new CollectionExpertFragment(choiceDepartment);
                fragmentTransaction.setCustomAnimations(R.anim.enter_to_right, R.anim.exit_to_right);
                fragmentTransaction.add(R.id.framelayout_for_order_expert, fragment).commit();
                fragmentManager.popBackStack();


            }
        });
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this part important but not a solid software.This must change later, we cannot implement filter for other expert. we can filter only doctor
                //This is the reason I controlled "sendDepartment" whether a doctor or not. if incoming a data is not doctor you can't filter

                if (sendDepartment.equals("Doktor")) {
                    getAlertDialog();
                } else {
                    String message = sendDepartment + " için filtre uygulanamamaktadır.";
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.barColor));
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setAction("Action", null).show();


                }

            }
        });


        textView_show_message.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterShowExpertToUser = new AdapterShowExpertToUser(_expertArrayList,getApplicationContext(),getSupportFragmentManager());
        recyclerView.setAdapter(adapterShowExpertToUser);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layout_animation(recyclerView);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void layout_animation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation_fade_top);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }




    public void choiceExpert(View view) {
        getAlertDialog();
    }

    public void getAlertDialog() {
        DialogFragment singleChoice = new SingleChoiceDoctorDepartmantFragment();
        singleChoice.setCancelable(false);
        singleChoice.show(getSupportFragmentManager(), "choice filter");
    }





    public void getExpertData(){
        try{
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView_show_message.setVisibility(View.INVISIBLE);

            Collection collection = new Collection();
            collection.setDepartment(choiceDepartment);

            ExpertManager expertManager = new ExpertManager(new FireBaseExpertDal());
            expertManager.getAllExpert(collection, new IGetExpertListener() {
                @Override
                public void onSuccess(ArrayList<Expert> expertArrayList) {
                    _expertArrayList.removeAll(_expertArrayList);
                    _expertArrayList.addAll(expertArrayList);
                    progressBar.setVisibility(View.INVISIBLE);
                    adapterShowExpertToUser.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                    textView_show_message.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailed(Exception exception) {
                    _expertArrayList.removeAll(_expertArrayList);
                    progressBar.setVisibility(View.INVISIBLE);
                    adapterShowExpertToUser.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                    textView_show_message.setVisibility(View.VISIBLE);
                    textView_show_message.setText("Üzgünüz " + choiceDepartment + " bulunamadı!");
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            });
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public static void accessActivity(ArrayList<Expert> arrayList,Boolean result){
        // that's static method for if you implement a filter "CollectionExpertFragment" class can access this method  and can set data easily
        if(result == true){
            _expertArrayList.removeAll(_expertArrayList);
            _expertArrayList.addAll(arrayList);
            textView_show_message.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            adapterShowExpertToUser.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }else{
            _expertArrayList.removeAll(_expertArrayList);
            adapterShowExpertToUser.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
            progressBar.setVisibility(View.INVISIBLE);
            textView_show_message.setVisibility(View.VISIBLE);
            textView_show_message.setText("Üzgünüz " + choiceDepartment + " bulunamadı!");
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        choiceDepartment = list[position];
        choiceExpert.setVisibility(View.INVISIBLE);
        getExpertData();
    }

    @Override
    public void onNegativeButtonClicked() {
        if (choiceDepartment == null) {

        }

    }

}