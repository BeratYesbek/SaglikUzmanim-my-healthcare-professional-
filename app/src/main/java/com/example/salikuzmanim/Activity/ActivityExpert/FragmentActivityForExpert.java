package com.example.salikuzmanim.Activity.ActivityExpert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.salikuzmanim.Concrete.Token;
import com.example.salikuzmanim.DataBaseManager.FireBaseTokensDal;
import com.example.salikuzmanim.Interfaces.SingleChoiceLister;
import com.example.salikuzmanim.R;
import com.example.salikuzmanim.ui.main.MainPageFragment;
import com.example.salikuzmanim.ui.main.SectionsPagerAdapter;
import com.example.salikuzmanim.ui.main.messageActivityForExpert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class FragmentActivityForExpert extends AppCompatActivity implements SingleChoiceLister {
    private String department;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_for_expert);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        Intent intent = getIntent();
        department = intent.getStringExtra("department");
        
        

        //startOneSignalManager(getApplicationContext());
        try{
            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseApp.initializeApp(this);
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
            firebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String token) {
                    FireBaseTokensDal fireBaseTokensDal = new FireBaseTokensDal();

                    fireBaseTokensDal.updateTokens(new Token(token,ID),"Expert_users");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });

        }catch (Exception e){
            System.out.println(e.toString());
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), messageActivityForExpert.class);
                startActivity(intent);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                 */
            }
        });
    }

    public String getDepartment() {
        return department;
    }

    public FragmentManager getFragmentManager(FragmentActivity activity) {
        return activity.getSupportFragmentManager();
    }
    
    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        String location = list[position];
        MainPageFragment.setLocation(location);
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}