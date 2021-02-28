package com.saglikuzmanimm.saglikuzmanim.Activity.CommonActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityExpert.LoginExpertActivity;
import com.saglikuzmanimm.saglikuzmanim.Activity.ActivityUser.LoginUserActivity;
import com.saglikuzmanimm.saglikuzmanim.R;

public class MainChoiceActivity extends AppCompatActivity {

    private TextView textView;
    private int REQUEST_CODE_BATTERY_OPTIMIZATIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choice);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_view_animation);
        a.reset();
        textView = findViewById(R.id.textView_info);
        textView.clearAnimation();
        textView.startAnimation(a);
        checkForBatteryOptimizations();


    }

    public void btnExpert(View view) {

        Intent intentToLoginExpert = new Intent(getApplicationContext(), LoginExpertActivity.class);
        startActivity(intentToLoginExpert);
    }

    public void btnUser(View view) {
        Intent intentToLoginUser = new Intent(getApplicationContext(), LoginUserActivity.class);
        startActivity(intentToLoginUser);
    }

    private void checkForBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATIONS) {
            checkForBatteryOptimizations();
        }
    }
}