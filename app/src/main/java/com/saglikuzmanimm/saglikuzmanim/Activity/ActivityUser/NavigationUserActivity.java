package com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity.MainChoiceActivity;
import com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity.SettingActivity;
import com.saglikuzmanimm.saglikuzmanim.Activity.NotificationTabLayout.NotificationTabLayoutActivity;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.NotificationManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.TokenManager;
import com.saglikuzmanimm.saglikuzmanim.Business.Concrete.UserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.NotificationDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.TokenDal;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.UserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NavigationUserActivity extends AppCompatActivity {


    private ImageView _imageView_notification_button;
    private TextView _textView_notification_counter;
    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;

    private ImageButton addButton;

    private ImageView nav_header_imageView;
    private TextView nav_header_email;
    private TextView nav_header_name;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        addButton = findViewById(R.id.btn_add_ad);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAddAdActivity = new Intent(getApplicationContext(), JobAdvertisementAddActivity.class);
                startActivity(intentToAddAdActivity);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        nav_header_imageView = headerView.findViewById(R.id.nav_header_profile);
        nav_header_email = headerView.findViewById(R.id.nav_header_email);
        nav_header_name = headerView.findViewById(R.id.nav_header_name);


        _textView_notification_counter = findViewById(R.id.textView_notification_counter_for_user);
        _imageView_notification_button = findViewById(R.id.ImageView_notification_button);
        _textView_notification_counter.setVisibility(View.INVISIBLE);


        _imageView_notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationTabLayoutActivity.class);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_message, R.id.nav_list, R.id.nav_apponitment, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        Activity activity = this;

        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) activity, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        updateTokens();
        getNotificationCounter();
        getData();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getNotificationCounter();
    }

    private void updateTokens() {
        TokenManager tokenManager = new TokenManager(new TokenDal());
        tokenManager.getToken(getApplicationContext(), "users");
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
                    _textView_notification_counter.setVisibility(View.VISIBLE);
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
        getMenuInflater().inflate(R.menu.navigation_user, menu);

        return true;
    }

    private void getData() {
        // this method get data for nav_header from database
        UserManager userManager = new UserManager(new UserDal());
        userManager.getData(null, new IGetUserListener() {
            @Override
            public void onSuccess(ArrayList<User> userArrayList) {
                User user = userArrayList.get(0);
                if(user.get_profileImage() != null){
                    Picasso.get().load(user.get_profileImage()).into(nav_header_imageView);
                }
                nav_header_email.setText(user.get_email());
                nav_header_name.setText(user.get_firstName() + " " + user.get_lastName());

            }

            @Override
            public void onFailed(Exception exception) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_aboutUs:
                // this part not necessary now
            case R.id.action_complaintsAndSuggestions:
                // this part not necessary now

            case R.id.action_support:
                // this part not necessary now


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
        // this alertDialog for logout from app
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}