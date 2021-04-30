package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity.MainChoiceActivity;
import com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity.SettingActivity;
import com.saglikuzmanimm.saglikuzmanim.Activity.NotificationTabLayout.NotificationTabLayoutActivity;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.NotificationManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.TokenManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.NotificationDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.TokenDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.SingleChoiceLister;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI.MainPageFragmentForExpert;
import com.saglikuzmanimm.saglikuzmanim.ui.ExpertMainUI.SectionsPagerAdapter;

import java.util.ArrayList;

//Main activity of expert

public class FragmentActivityForExpert extends AppCompatActivity implements SingleChoiceLister {
    private String department;
    private ImageView _imageView_notification_button;

    private TextView _textView_notification_counter;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_for_expert);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar_activity);
        this.setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        Intent intent = getIntent();
        department = intent.getStringExtra("department");
        if (department != null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("department", department);
            editor.commit();
        }

        department = sharedpreferences.getString("department", "");

        _imageView_notification_button = findViewById(R.id.ImageView_notification_button_for_expert);
        _textView_notification_counter = findViewById(R.id.textView_notification_counter_for_expert);
        _textView_notification_counter.setVisibility(View.INVISIBLE);


        _imageView_notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationTabLayoutActivity.class);
                intent.putExtra("type", "expert");
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MessageListActivityForExpert.class);
                startActivity(intent);
            }
        });
        updateTokens();
        getNotificationCounter();
    }

    public String getDepartment() {
        return department;
    }

    private void updateTokens() {
        TokenManager tokenManager = new TokenManager(new TokenDal());
        tokenManager.getToken(getApplicationContext(), "Expert_users");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotificationCounter();
    }

    private void getNotificationCounter() {

        NotificationManager notificationManager = new NotificationManager(new NotificationDal());
        notificationManager.getData(null, new IGetNotificationListener() {
            @Override
            public void onSuccess(ArrayList<Notification> notificationArrayList) {
                Integer total = 0;

                for (int i = 0; i < notificationArrayList.size(); i++) {
                    Notification notification = notificationArrayList.get(i);
                    if (notification.get_isSeen() != true) {
                        total++;
                    }
                }

                if (total > 0) {

                    _textView_notification_counter.setText(total.toString());
                } else {
                    _textView_notification_counter.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailed(Exception exception) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exper_activitiy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_aboutUs:
                //this part not necessary now

            case R.id.action_complaintsAndSuggestions:
                //this part not necessary now


            case R.id.action_support:
                //this part not necessary now


            case R.id.action_logout:
                getLogOutDialog();
                return true;
            case R.id.action_settings:

                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLogOutDialog() {
        //this part display one alert dialog for logout

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Uyarı");
        builder.setMessage("Çıkış yapmak istediğinize emin misiniz ?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainChoiceActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    @Override
    public void onPossitiveButtonClicked(String[] list, int position, String whichList) {
        String location = list[position];
        MainPageFragmentForExpert.setLocation(location);
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}