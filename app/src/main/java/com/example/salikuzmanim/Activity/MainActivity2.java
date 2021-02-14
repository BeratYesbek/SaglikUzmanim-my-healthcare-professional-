package com.example.salikuzmanim.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salikuzmanim.R;

public class MainActivity2 extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_view_animation);
        a.reset();
        textView = findViewById(R.id.textView_info);
        textView.clearAnimation();
        textView.startAnimation(a);


    }
    public void btnGiveService(View view){
        Intent intentToLoginGiveService = new Intent(getApplicationContext(), LoginGiveServiceActivity.class);
        startActivity(intentToLoginGiveService);
    }
    public void btnRecevingService(View view){
        Intent intenToLoginReceivingService =  new Intent(getApplicationContext(), LoginReceivingServiceActivity.class);
        startActivity(intenToLoginReceivingService);
    }
}