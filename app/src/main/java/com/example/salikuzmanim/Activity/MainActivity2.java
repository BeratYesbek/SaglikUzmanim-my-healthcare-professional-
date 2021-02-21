package com.example.salikuzmanim.Activity;

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

import com.example.salikuzmanim.R;

public class MainActivity2 extends AppCompatActivity {

    TextView textView;
    private int REQUEST_CODE_BATTERY_OPTIMIZATIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_view_animation);
        a.reset();
        textView = findViewById(R.id.textView_info);
        textView.clearAnimation();
        textView.startAnimation(a);
        checkForBatteryOptimizations();






    }

    public void btnGiveService(View view) {
        Intent intentToLoginGiveService = new Intent(getApplicationContext(), LoginGiveServiceActivity.class);
        startActivity(intentToLoginGiveService);
    }

    public void btnRecevingService(View view) {
        Intent intenToLoginReceivingService = new Intent(getApplicationContext(), LoginReceivingServiceActivity.class);
        startActivity(intenToLoginReceivingService);
    }

    private void checkForBatteryOptimizations() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        /*
        System.out.println("12");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            System.out.println("13");
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Warning");
                builder.setMessage("Battery optimization is enabled,It can interrupt running background service");
                builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                            startActivityForResult(intent,REQUEST_CODE_BATTERY_OPTIMIZATIONS);
                        }catch (Exception e){
                            System.out.println(e.toString());
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        }

*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATIONS) {
            checkForBatteryOptimizations();
        }
    }
}