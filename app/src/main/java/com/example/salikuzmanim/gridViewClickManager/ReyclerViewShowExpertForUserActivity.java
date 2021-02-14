package com.example.salikuzmanim.gridViewClickManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salikuzmanim.Adapter.AdapterShowExpertToUser;
import com.example.salikuzmanim.Concrete.Expert;
import com.example.salikuzmanim.Concrete.Order;
import com.example.salikuzmanim.DataBaseManager.FireBaseExpertDal;
import com.example.salikuzmanim.Fragment.SingleChoiceDoctorDepartmantFragment;
import com.example.salikuzmanim.Fragment.fragment_order_expert;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;
import com.example.salikuzmanim.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ReyclerViewShowExpertForUserActivity extends AppCompatActivity implements SingleChoiceLister {

    private String sendDepartmat;

    private static ArrayList<Expert> expertArrayList;


    private static String choiceDepartmant;
    private static RecyclerView recyclerView;
    private static TextView textView_show_message;
    private static ProgressBar progressBar;
    private static ImageView imageView;

    private Button buttonOrder;
    private Button buttonFilter;
    private Button choiceExpert;


    private static AdapterShowExpertToUser adapterShowExpertToUser;
    private static FragmentTransaction fragmentTransaction;
    private static fragment_order_expert fragment;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reycler_view_show_expert_for_user);



        context = getApplicationContext();

        expertArrayList = new ArrayList<>();

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
        sendDepartmat = intent.getStringExtra("departmant");

        if (sendDepartmat.equals("Doktor")) {

            getAlertDialog();

        }

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new fragment_order_expert(choiceDepartmant);
                fragmentTransaction.setCustomAnimations(R.anim.enter_to_right, R.anim.exit_to_right);
                fragmentTransaction.add(R.id.framelayout_for_order_expert, fragment).commit();
                fragmentManager.popBackStack();


            }
        });
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendDepartmat.equals("Doktor")) {
                    getAlertDialog();
                } else {
                    String message = sendDepartmat + " için filtre uygulanamamaktadır.";
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.barColor));
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setAction("Action", null).show();


                }

            }
        });


        textView_show_message.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterShowExpertToUser = new AdapterShowExpertToUser(expertArrayList,getApplicationContext(),getSupportFragmentManager());
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
            FireBaseExpertDal fireBaseExpertDal = new FireBaseExpertDal();
            Order order = new Order();
            order.setDepartment(choiceDepartmant);
            fireBaseExpertDal.getAllExpert(order, new IGetListDataListener() {
                @Override
                public void onSuccess(ArrayList entity) {
                    expertArrayList.removeAll(expertArrayList);
                    expertArrayList.addAll(entity);
                    progressBar.setVisibility(View.INVISIBLE);
                    adapterShowExpertToUser.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                }

                @Override
                public void onFailed(Exception exception) {
                    expertArrayList.removeAll(expertArrayList);
                    progressBar.setVisibility(View.INVISIBLE);
                    adapterShowExpertToUser.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                    textView_show_message.setVisibility(View.VISIBLE);
                    textView_show_message.setText("Üzgünüz " + choiceDepartmant + " bulunamadı!");
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            });
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public static void accessActivity(ArrayList<Expert> arrayList,Boolean result){

        if(result == true){
            expertArrayList.removeAll(expertArrayList);
            expertArrayList.addAll(arrayList);
            textView_show_message.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            adapterShowExpertToUser.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }else{
            System.out.println(result);
            expertArrayList.removeAll(expertArrayList);
            expertArrayList.removeAll(arrayList);
            adapterShowExpertToUser.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
            progressBar.setVisibility(View.INVISIBLE);
            textView_show_message.setVisibility(View.VISIBLE);
            textView_show_message.setText("Üzgünüz " + choiceDepartmant + " bulunamadı!");
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("search here");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        choiceDepartmant = list[position];
        choiceExpert.setVisibility(View.INVISIBLE);
        getExpertData();
    }

    @Override
    public void onNegativeButtonClicked() {
        if (choiceDepartmant.equals(null)) {
            choiceExpert.setVisibility(View.VISIBLE);
        }

    }

}